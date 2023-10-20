package com.ktm.inreptava.web.api;

import java.util.List;
import java.util.stream.Stream;

import com.ktm.inreptava.model.ClassModel;

public class GetClassesResponse {
	private List<ClassModelAPI> classes;
	
	public GetClassesResponse() {
	}
	
	public static GetClassesResponse create(Stream<ClassModel> classes) {
		GetClassesResponse resp = new GetClassesResponse();
		resp.classes = classes.map(c -> ClassModelAPI.create(c)).toList();
		return resp;
	}
	
	public List<ClassModelAPI> getClasses() {
		return classes;
	}
	public void setClasses(List<ClassModelAPI> classes) {
		this.classes = classes;
	}
}
