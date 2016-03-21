package com.ibm.gswt.campus.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/LoginError")
public class LoginErrorWebService {

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void sendError(@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		JsonSendUtil.sendJsonData("Message", "User or password is not correct!", request, response);
	}
}
