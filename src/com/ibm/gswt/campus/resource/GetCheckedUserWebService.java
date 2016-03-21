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

import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.service.UserLogService;
import com.ibm.gswt.campus.serviceImpl.UserLogServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/GetCheckedUsers")
public class GetCheckedUserWebService {

	private UserLogService userLogService = new UserLogServiceImpl();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void getEventDesc(@QueryParam("eventId") String eventId,
		@Context HttpServletRequest request,
		@Context HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession();
		if (session.getAttribute("admin") == null) {
			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
			return;
		}
			
		try {
			List<User> GetCheckedUsers = userLogService.getCheckedUsers(eventId);
			
			JsonSendUtil.sendJsonData("GetCheckedUsers", GetCheckedUsers, request, response);
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
			e.printStackTrace();
		}
	}
}
