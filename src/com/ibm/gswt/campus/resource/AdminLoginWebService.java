package com.ibm.gswt.campus.resource;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.ibm.gswt.campus.serviceImpl.AdminServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/AdminLogin")
public class AdminLoginWebService {
	
	private AdminServiceImpl adminService = new AdminServiceImpl();
	
	private static Logger logger = Logger.getLogger(AdminLoginWebService.class);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void isAdmin(@QueryParam("username") String username,
			@QueryParam("password") String password,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws IOException {
		
		List<String> levelList = adminService.isAdmin(username, password);
		
		HttpSession session = request.getSession();
		
		if (levelList.size() > 0) {
			logger.info("admin ID is " + username);
			session.setAttribute("admin", "true");
			String message = "User " + username + " have successfully logged in."; 
			JsonSendUtil.sendJsonData("Message", message, request, response);
		} else {
			logger.info("log in failed.");
			JsonSendUtil.sendJsonData("Message", "User or Password is not correct!", request, response);
		}
	}
}
