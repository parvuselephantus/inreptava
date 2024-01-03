package com.ktm.inreptava.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jboss.forge.roaster.ParserException;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.JavaType;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;

import com.ktm.inreptava.ClassParsing;
import com.ktm.inreptava.web.api.FieldAPI;

/**
 * FIXME - getter/setter zle sie wykrywaja
 * 
 * @author lordu
 *
 */
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
		if (rule.is(this))
			controllerList.add(this);
	}
	@Override
	public void getClasses(List<ClassModel> collectorList) {
		collectorList.add(this);
	}

	public List<FieldModel> getFields() {
		List<FieldModel> fields = new LinkedList<>();
		if (javaClass != null) {
			javaClass.getFields().stream().forEach(f -> {
				FieldModel fm = new FieldModel();
				fm.setName(f.getName());
				fm.setGetter(hasGetter(f));
				fm.setSetter(hasSetter(f));
				fm.setType(f.getType().getQualifiedNameWithGenerics());
				fields.add(fm);
			});
		}
		return fields;
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

	private boolean equalOrHasNested(JavaClassSource superClass, JavaClassSource childClass) {
		return superClass.equals(childClass) || superClass.hasNestedType(childClass);
	}

	private boolean argsEqual(JavaClassSource[] childArgs, JavaClassSource[] superArgs) {
		if (childArgs.length != superArgs.length)
			return false;
		for (int i = 0; i < childArgs.length; i++) {
			if (!equalOrHasNested(superArgs[i], childArgs[i])) {
				return false;
			}
		}
		return true;
	}
	private boolean argsEqual(List<ParameterSource<JavaClassSource>> childArgs, JavaClassSource[] superArgs) {
		if (childArgs.size() != superArgs.length)
			return false;
		for (int i = 0; i < childArgs.size(); i++) {
			ParameterSource<JavaClassSource> childArg = childArgs.get(i);
			if (!equalOrHasNested(superArgs[i], childArg.getOrigin())) {
				return false;
			}
		}
		return true;
	}

	public MethodSource<JavaClassSource> getMethod(String name, JavaClassSource[] args, JavaClassSource ret) {
		return javaClass.getMethods().stream()
				.filter(m -> m.getName().equals(name) && argsEqual(m.getParameters(), args)
						&& (ret == null || equalOrHasNested(m.getReturnType().getOrigin(), ret)))
				.findAny().orElse(null);
	}

	public boolean hasGetter(FieldSource<JavaClassSource> field) {
		return getMethod(ClassParsing.getterName(field), new JavaClassSource[0], field.getOrigin()) != null;
	}
	public boolean hasSetter(FieldSource<JavaClassSource> field) {
		return getMethod(ClassParsing.setterName(field), new JavaClassSource[]{field.getOrigin()}, null) != null;
	}
	@Override
	public int hashCode() {
		if (javaClass == null)
			return name.hashCode();
		return javaClass.getQualifiedName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}

	public String getFullName() {
		return (getParent() == null ? "" : (getParent().getFullName() + ".")) + getName();
	}

	public void changeMethod(String method, boolean add, FieldAPI field) {
		if (add) {
			if (method.equals("set")) {
				addSetter(field.getShortType(), field.getName());
			} else if (method.equals("get")) {
				addGetter(field, field.getShortType(), field.getName());
			} else {
				throw new RuntimeException("Can't implement method: " + method);
			}
		} else {
			if (method.equals("set")) {
				String methodName = ClassParsing.setterName(field.getName());
				MethodSource<JavaClassSource> methodSource = javaClass.getMethod(methodName, field.getShortType());
				if (methodSource != null) {
					javaClass.removeMethod(methodSource);
				}
			} else if (method.equals("get")) {
				String methodName = ClassParsing.getterName(field.getShortType(), field.getName());
				MethodSource<JavaClassSource> methodSource = javaClass.getMethod(methodName);
				if (methodSource != null) {
					javaClass.removeMethod(methodSource);
				}
			} else {
				throw new RuntimeException("Can't remove method: " + method);
			}
		}
	}

	public void addGetter(FieldAPI field, String fieldType, String fieldName) {
		getJavaClass().addMethod()
			.setReturnType(fieldType).setName(ClassParsing.getterName(fieldType, fieldName	)).setPublic()
			.setBody("return " + fieldName + ";");
	}

	public void addSetter(String fieldType, String fieldName) {
		getJavaClass().addMethod()
			.setReturnType("void").setName(ClassParsing.setterName(fieldName)).setPublic()
			.setBody("this." + fieldName + " = " + fieldName + " ;").addParameter(fieldType, fieldName);
	}

	public void writeToFile() throws IOException {
		FileOutputStream fos = new FileOutputStream(getFilePath());
		fos.write(javaClass.toString().getBytes());
		fos.flush();
		fos.close();
	}

	private String getFilePath() {
		return "src" + File.separatorChar + getFullName().replace('.', File.separatorChar) + ".java";
	}
}
