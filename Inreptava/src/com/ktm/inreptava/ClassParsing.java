package com.ktm.inreptava;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

public class ClassParsing {
	private static String setterName(FieldSource<JavaClassSource> field) {
		return "set" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
	}
	public static String getterName(FieldSource<JavaClassSource> field) {
		return ((field.getType().getSimpleName().equals("boolean") || field.getType().getSimpleName().equals("boolean")) ? "is" : "get")
				+ Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
	}
	private static MethodSource<JavaClassSource> getGetter(JavaClassSource javaClass, FieldSource<JavaClassSource> field) {
		String getterName = getterName(field);
		return javaClass.getMethods().stream().filter(m -> 
				   m.getName().equals(getterName)
				&& m.getReturnType().getQualifiedNameWithGenerics().equals(field.getType().getQualifiedNameWithGenerics())
				&& m.getParameters().size() == 0).findAny().orElse(null);
	}
	
	private MethodSource<JavaClassSource> getSetter(JavaClassSource javaClass, FieldSource<JavaClassSource> field) {
		String setterName = setterName(field);
		return javaClass.getMethods().stream().filter(m -> m.getName().equals(setterName)
				&& m.getReturnType().getSimpleName().equals("void")
				&& m.getParameters().size() == 1
				&& m.getParameters().get(0).getType().getQualifiedNameWithGenerics().equals(field.getType().getQualifiedNameWithGenerics())).findAny().orElse(null);
	}
	
	
	private void parsingPOC() throws IOException {
		FileInputStream classFIS = new FileInputStream("../licensing-service/Asecla-licenses/src\\main\\java\\com\\asecla\\licenses\\server\\model/LicenseType.java");
		JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, classFIS);
		javaClass.getFields().stream().forEach(field -> {
			System.out.println(getGetter(javaClass, field) != null ? "Has getter" : "NO GETTER");
			System.out.println(getSetter(javaClass, field) != null ? "Has setter" : "NO SETTER");
			System.out.println("\tprivate " + field.getType().toString() + " " + field.getName() + ";");
		});
		
		createAPIFile(javaClass);
	}
	
	private void createAPIFile(JavaClassSource srcClass) throws IOException {
		ProjectRules rules = new ProjectRules();

		final JavaClassSource trgClass = Roaster.create(JavaClassSource.class);
		trgClass.setPackage("com.ktm.inreptava.target").setName(rules.classNameToAPIClassName(srcClass.getName()));
		
		trgClass.getJavaDoc().setFullText("Generated with Inreptava");

		srcClass.getFields().stream().forEach(field -> {
			trgClass.addField()
			  .setName(field.getName())
			  .setType(field.getType().getQualifiedNameWithGenerics())
			  .setPrivate();
		});
		
		srcClass.getFields().stream().forEach(field -> {
			trgClass.addMethod()
				.setReturnType(field.getType())
				.setName(getterName(field))
				.setPublic()
				.setBody("return " + field.getName() + ";");

			trgClass.addMethod()
				.setReturnType("void")
				.setName(setterName(field))
				.setPublic()
				.setBody("this." + field.getName() + " = " + field.getName()  + " ;")
				.addParameter(field.getType().getQualifiedNameWithGenerics(), field.getName());
		});
		
		FileOutputStream fos = new FileOutputStream("src/com/ktm/inreptava/target/" + srcClass.getName() + ".java");
		fos.write(trgClass.toString().getBytes());
		fos.flush();
		fos.close();
	}
	
	public static void checkGetterAndSetter(JavaClassSource trg, FieldSource<JavaClassSource> field) {
		String getterName = getterName(field);
		if (!trg.hasMethodSignature(getterName)) {
			trg.addMethod()
				.setReturnType(field.getType())
				.setName(getterName)
				.setPublic()
				.setBody("return " + field.getName() + ";");
		}
		
		String setterName = setterName(field);
		if (!trg.hasMethodSignature(setterName)) {
			trg.addMethod()
				.setReturnType("void")
				.setName(setterName)
				.setPublic()
				.setBody("this." + field.getName() + " = " + field.getName()  + " ;")
				.addParameter(field.getType().getQualifiedNameWithGenerics(), field.getName());
		}
	}
	
	public static void main(String[] args) {
		try {
			new ClassParsing().parsingPOC();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
