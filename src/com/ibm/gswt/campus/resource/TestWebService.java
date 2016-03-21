package com.ibm.gswt.campus.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.ibm.gswt.campus.util.ApplicationProperties;

@Path("/test")
public class TestWebService {

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void getTestList(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
		
		String path = ApplicationProperties.getProps().getProperty("user.file.path");
		System.out.println("aaa");
	}
}
