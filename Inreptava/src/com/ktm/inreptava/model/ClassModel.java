package com.ktm.inreptava.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class ClassModel implements PackageChild {
	private PackageModel parent;
	private String name;
	private JavaClassSource javaClass;
	
	public ClassModel(PackageModel parent, String name) {
		this.parent = parent;
		this.name = name;
	}
	
	@Override
	public PackageModel getParent() {
		return parent;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public JavaClassSource getJavaClass() {
		return javaClass;
	}
	
	@Override
	public void getClasses(List<ClassModel> controllerList, ClassRule rule) {
		if (rule.is(this)) controllerList.add(this);
	}
	@Override
	public void getClasses(List<ClassModel> collectorList) {
		collectorList.add(this);
	}
	

	public void scan(File f) throws FileNotFoundException {
		FileInputStream classFIS = new FileInputStream(f);
		try {
			JavaType<?> parsed = Roaster.parse(classFIS);
			if (parsed.isClass()) {
				javaClass = (JavaClassSource) parsed;
			}
		} catch (ParserException e) {
			System.err.println(f.getAbsolutePath());
			System.err.println(e.getMessage());
		}
	}
	
	public List<ClassModel> listResponseClasses() {
		Set<ClassModel> controllers = new HashSet<ClassModel>();
		if (javaClass != null) {
			javaClass.getMethods().stream()
				.filter(m -> m.hasAnnotation("RequestMapping") && !m.getReturnType().getName().equals("void"))
				.map(m -> parent.getClass(m.getReturnType().getQualifiedName()))
				.forEach(classModel -> controllers.add(classModel));
		}
		return controllers.stream().toList();
	}
	
	@Override
	public int hashCode() {
		if (javaClass == null) return name.hashCode();
		return javaClass.getQualifiedName().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}
}
