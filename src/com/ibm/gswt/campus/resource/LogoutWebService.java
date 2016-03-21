package com.ibm.gswt.campus.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

@Path("/Logout")
public class LogoutWebService {
	
	private static Logger logger = Logger.getLogger(LogoutWebService.class);
	
	private static final String LOGOUT_URL = "/ibm_security_logout?logout=Logout&logoutExitPage=";

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void Login(@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		logger.info("logout...");
		
		// Call the WebSphere logout servlet to remove tokens and cookies
		String logoutUrl = request.getContextPath() + LOGOUT_URL + "/login";
		
		response.sendRedirect(response.encodeURL(logoutUrl));
	}
}
