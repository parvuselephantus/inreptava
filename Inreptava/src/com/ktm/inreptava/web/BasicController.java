package com.ktm.inreptava.web;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ktm.inreptava.ProjectService;
import com.ktm.inreptava.web.api.GetClassesResponse;
import com.ktm.inreptava.web.api.GetStatusResponse;
import com.ktm.inreptava.web.api.SetMethodRequest;

@CrossOrigin(maxAge = 1, origins = "*")
@RestController
public class BasicController {
	@Autowired
	private ProjectService projects;
	
	@RequestMapping(method=RequestMethod.GET, value = "/getStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetStatusResponse handleGetPrice(Principal principal) {
		return GetStatusResponse.create("AAAAAAAAA");
    }
	
	@RequestMapping(method=RequestMethod.GET, value = "/scan", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetStatusResponse handleScan(Principal principal) {
//		projects.scan();
		projects.getResponseClasses();
		return GetStatusResponse.create("AAAAAAAAA");
    }

	@RequestMapping(method=RequestMethod.GET, value = "/classes", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetClassesResponse handleClasses(Principal principal) {
		return GetClassesResponse.create(projects.getClasses());
    }
	
	@RequestMapping(method=RequestMethod.POST, value = "/setMethod", produces = MediaType.APPLICATION_JSON_VALUE)
	public GetStatusResponse setMethod(Principal principal, @RequestBody SetMethodRequest request) throws IOException {
		projects.setMethod(request);
		return GetStatusResponse.create("");
	}
}