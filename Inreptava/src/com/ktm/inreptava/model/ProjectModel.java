package com.ktm.inreptava.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class ProjectModel {
	private PackageModel model;
	

	public void scan(File srcFolder) throws FileNotFoundException {
		model = new PackageModel(null, "root");
		scan(srcFolder, model);
	}

	public void scan(File folder, PackageModel parent) throws FileNotFoundException {
		for (File f: folder.listFiles()) {
			if (f.isDirectory()) {
				PackageModel p = new PackageModel(parent, f.getName());
				parent.addChild(p);
				scan(f, p);
			} else if (f.isFile() && f.getName().toLowerCase().endsWith(".java")) {
				ClassModel c = new ClassModel(parent, f.getName().replace(".java", ""));
				parent.addChild(c);
				c.scan(f);
			}
		}
	}

	public List<ClassModel> getControllers() {
		List<ClassModel> controllers = new LinkedList<ClassModel>();
		model.getClasses(controllers, c -> c.getJavaClass() != null && c.getJavaClass().getAnnotation("RestController") != null);
		return controllers;
	}
	
	public Stream<ClassModel> getClasses() {
		return model.getClasses();
	}
}
