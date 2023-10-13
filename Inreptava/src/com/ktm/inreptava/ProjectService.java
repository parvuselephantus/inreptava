package com.ktm.inreptava;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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
			.flatMap(Set::stream)
			.toList();
	}
	
	public Stream<ClassModel> getClasses() {
		return projectModel.getClasses();
	}

}
