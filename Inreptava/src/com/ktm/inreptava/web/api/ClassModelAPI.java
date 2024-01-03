package com.ktm.inreptava.web.api;

import java.util.LinkedList;
import java.util.List;

import com.ktm.inreptava.model.ClassModel;

public class ClassModelAPI {
	private String fullClassName;
	private String name;
	private List<FieldAPI> fields;

	public ClassModelAPI() {
	}
	
	public static ClassModelAPI create(ClassModel clazz) {
		ClassModelAPI ret = new ClassModelAPI();
		ret.name = clazz.getName();
		ret.fields = new LinkedList<>();
		clazz.getFields().forEach(f -> {
			ret.fields.add(FieldAPI.build(f));	
		});
		ret.fullClassName = clazz.getFullName();
		return ret;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<FieldAPI> getFields() {
		return fields;
	}
	public void setFields(List<FieldAPI> fields) {
		this.fields = fields;
	}
	public String getFullClassName() {
		return fullClassName;
	}
	public void setFullClassName(String fullClassName) {
		this.fullClassName = fullClassName;
	}
}
