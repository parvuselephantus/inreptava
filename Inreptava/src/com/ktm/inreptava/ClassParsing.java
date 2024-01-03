package com.ktm.inreptava;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import com.ktm.inreptava.model.ClassModel;

public class ClassParsing {
	public static String setterName(String fieldName) {
		return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}
	public static String getterName(String typeName, String fieldName) {
		return ((typeName.equals("boolean") || typeName.equals("Boolean")) ? "is" : "get")
				+ Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}
	
	public static String setterName(FieldSource<JavaClassSource> field) {
		return setterName(field.getName());
	}
	public static String getterName(FieldSource<JavaClassSource> field) {
		return ((field.getType().getSimpleName().equals("boolean") || field.getType().getSimpleName().equals("Boolean")) ? "is" : "get")
				+ Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
	}

	
	public static void checkGetterAndSetter(ClassModel trg, FieldSource<JavaClassSource> field) {
		if (!trg.hasGetter(field)) {
			String getterName = getterName(field);
			trg.getJavaClass().addMethod()
				.setReturnType(field.getType())
				.setName(getterName)
				.setPublic()
				.setBody("return " + field.getName() + ";");
		}
		
		if (!trg.hasSetter(field)) {
			String setterName = setterName(field);
			trg.getJavaClass().addMethod()
				.setReturnType("void")
				.setName(setterName)
				.setPublic()
				.setBody("this." + field.getName() + " = " + field.getName()  + " ;")
				.addParameter(field.getType().getQualifiedNameWithGenerics(), field.getName());
		}
	}
}
