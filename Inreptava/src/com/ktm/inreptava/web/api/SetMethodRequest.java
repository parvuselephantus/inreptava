package com.ktm.inreptava.web.api;

public class SetMethodRequest {
    private String fullClassName;
    private String method;
    private boolean add;
    private FieldAPI field;

    public SetMethodRequest() {
	}
    
    public String getFullClassName() {
		return fullClassName;
	}
    public void setFullClassName(String fullClassName) {
		this.fullClassName = fullClassName;
	}
    
    public String getMethod() {
		return method;
	}
    public void setMethod(String method) {
		this.method = method;
	}
    
    public void setAdd(boolean add) {
		this.add = add;
	}
    public boolean isAdd() {
		return add;
	}
    
    public FieldAPI getField() {
		return field;
	}
    public void setField(FieldAPI field) {
		this.field = field;
	}
}
