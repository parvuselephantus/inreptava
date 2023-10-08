package com.ktm.inreptava.model;

import java.util.List;

public interface PackageChild {
	public PackageModel getParent();
	public String getName();
	public void getClasses(List<ClassModel> controllerList, ClassRule rule);
}
