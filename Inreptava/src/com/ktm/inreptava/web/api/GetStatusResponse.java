package com.ktm.inreptava.web.api;

public class GetStatusResponse {
	private String status;
	
	public GetStatusResponse() {
	}
	
	public static GetStatusResponse create(String status) {
		GetStatusResponse resp = new GetStatusResponse();
		resp.setStatus(status);
		return resp;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
