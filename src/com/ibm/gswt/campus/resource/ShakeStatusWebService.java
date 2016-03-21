package com.ibm.gswt.campus.resource;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.ibm.gswt.campus.service.TopicService;
import com.ibm.gswt.campus.serviceImpl.TopicServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/ShakeStatus")
public class ShakeStatusWebService {
	private TopicService topicService = new TopicServiceImpl();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void checkShake(@QueryParam(value = "eventId") String eventId,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession();
		if (session.getAttribute("admin") == null) {
			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
			return;
		}
		
		try {
			// get status info
			String status = topicService.checkStatus(eventId);
			Map<String,String> result = new HashMap<String, String>();
			Date d = new Date();
			if("Y".equals(status)) {
				result.put("available", "1");
				JsonSendUtil.sendJsonData("available", d.getTime(), request, response);
			} else {
				result.put("available", "0");
				JsonSendUtil.sendJsonData("available", "0", request, response);
			}
			
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
}
