package com.ibm.gswt.campus.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/AdminLogout")
public class AdminLogoutWebService {
	
	private static Logger logger = Logger.getLogger(AdminLogoutWebService.class);
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void logout(@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws IOException {
		
		
		
		HttpSession session = request.getSession();
		
		if ("true".equals(session.getAttribute("admin"))) {
			logger.info("session remove");
			session.removeAttribute("admin");
			JsonSendUtil.sendJsonData("Message", "You have been signed out", request, response);
		} else {
			logger.info("not logged in");
			JsonSendUtil.sendJsonData("Message", "You are not logged in", request, response);
		}
	}
}
