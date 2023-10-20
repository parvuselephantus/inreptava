package com.ktm.inreptava;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.PropertySource;
import org.springframework.stereotype.Service;

import com.ktm.inreptava.model.ClassModel;
import com.ktm.inreptava.model.ProjectModel;

@Service
public class ProjectService {
	private ProjectModel projectModel;
	
	{
		scan();
	}

	public void scan() {
		projectModel = new ProjectModel();
		try {
			projectModel.scan(new File("src"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public List<ClassModel> getResponseClasses() {
		return projectModel.getControllers()
			.stream()
			.map(c -> c.listResponseClasses())
			.flatMap(List::stream)
			.toList();
	}
	
	public Stream<ClassModel> getClasses() {
		return projectModel.getClasses();
	}
	
	public void adaptAPIClassClassModel(ClassModel srcClass, ClassModel trgClass) {
		List<FieldSource<JavaClassSource>> fields = srcClass.getJavaClass().getFields();
		JavaClassSource trg = trgClass.getJavaClass();
		fields.stream().forEach(field -> {
			PropertySource<JavaClassSource> property = trg.getProperty(field.getName());
			if (property == null) {
				trg.addProperty(field.getOrigin(), field.getName());
			} else {
				if (!property.getField().getOrigin().equals(field.getOrigin())) {
					System.out.println("Pole " + field.getName() + " konflikt typów: " + field.getOrigin() + " vs " + property.getField().getOrigin());
				}
			}
			
			ClassParsing.checkGetterAndSetter(trg, field);
		});
	}
	
	private String getterCommand(FieldSource<JavaClassSource> field) {
		return "\t\tret." + field.getName() + " = obj." + ClassParsing.getterName(field) + ";";
	}
	
	private void manageBuild(JavaClassSource trg) {
		MethodSource<JavaClassSource> method = trg.getMethod("build");
		List<String> cmds = Arrays.asList(method.getBody().split(";"));
		method.setBody(String.join(";\r\n\t\t", cmds));
	}

}
