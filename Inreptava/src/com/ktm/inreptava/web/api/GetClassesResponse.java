package com.ktm.inreptava.web.api;

import java.util.List;

public class GetClassesResponse {
	private List<ClassModelAPI> classes;
	
	public GetClassesResponse() {
	}
	public List<ClassModelAPI> getClasses() {
		return classes;
	}
	public void setClasses(List<ClassModelAPI> classes) {
		this.classes = classes;
	}
}
