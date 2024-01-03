package com.ktm.inreptava.model;

import java.util.List;

import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class FieldModel {
	private String name;
	private String type;
	private boolean getter;
	private boolean setter;
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isGetter() {
		return getter;
	}
	public void setGetter(boolean getter) {
		this.getter = getter;
	}
	public boolean isSetter() {
		return setter;
	}
	public void setSetter(boolean setter) {
		this.setter = setter;
	}
	
	public FieldSource<JavaClassSource> getFieldImpl(ClassModel clazz) {
		List<FieldSource<JavaClassSource>> fields = clazz.getJavaClass().getFields();
		return fields.stream().filter(f -> f.getName().equals(name)).findAny().orElse(null);
	}
}
