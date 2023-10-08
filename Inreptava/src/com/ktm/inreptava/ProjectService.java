package com.ktm.inreptava;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ktm.inreptava.model.ClassModel;
import com.ktm.inreptava.model.ProjectModel;

@Service
public class ProjectService {
	private ProjectModel projectModel;

	public void scan() {
		projectModel = new ProjectModel();
		try {
			projectModel.scan(new File("src"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void getControllers() {
		List<ClassModel> controllers = projectModel.getControllers();
		controllers.stream()
			.map(c -> c.listResponseClasses())
			.flatMap(List::stream)
			.forEach(c -> {
				System.out.println(c);
			});
	}

}
