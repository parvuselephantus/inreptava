package com.ktm.inreptava.web.api;

import com.ktm.inreptava.model.FieldModel;

public class FieldAPI {
	private String name;
	private String type;
	private boolean getter;
	private boolean setter;
	
	public FieldAPI() {
	}
	
	public static FieldAPI build(FieldModel f) {
		FieldAPI ret = new FieldAPI();
		ret.name = f.getName();
		ret.type = f.getType();
		ret.getter = f.isGetter();
		ret.setter = f.isSetter();
		return ret;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortType() {
		String[] path = type.split("\\.");
		return path[path.length-1];
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
	
	
}
