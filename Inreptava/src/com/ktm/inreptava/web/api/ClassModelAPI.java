package com.ktm.inreptava.web.api;

import com.ktm.inreptava.model.ClassModel;

public class ClassModelAPI {
	private String name;
	
	public ClassModelAPI() {
	}
	
	public static ClassModelAPI create(ClassModel clazz) {
		ClassModelAPI ret = new ClassModelAPI();
		ret.name = clazz.getName();
		return ret;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
