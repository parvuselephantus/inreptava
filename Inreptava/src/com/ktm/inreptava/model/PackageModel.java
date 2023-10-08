package com.ktm.inreptava.model;

import java.util.LinkedList;
import java.util.List;

public class PackageModel implements PackageChild {
	private PackageModel parent;
	private String name;
	private List<PackageChild> children;
	
	public PackageModel(PackageModel parent, String name) {
		this.parent = parent;
		this.name = name;
		this.children = new LinkedList<PackageChild>();
	}
	
	public void addChild(PackageChild child) {
		children.add(child);
	}
	
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public PackageModel getParent() {
		return parent;
	}

	public void setParent(PackageModel parent) {
		this.parent = parent;
	}

	@Override
	public void getClasses(List<ClassModel> controllerList, ClassRule rule) {
		children.stream().forEach(c -> c.getClasses(controllerList, rule));
	}

	public ClassModel getClass(String qualifiedName) {
		if (parent != null) return parent.getClass(qualifiedName);
		String[] path = qualifiedName.split(".");
		PackageChild p = this;
		for (int i = 0; i < path.length; i++) {
			p = ((PackageModel) p).getChild(path[i]);
		}
		return (ClassModel) p;
	}

	private PackageChild getChild(String name) {
		return children.stream().filter(child -> child.getName().equals(name)).findAny().orElse(null);
	}
}
