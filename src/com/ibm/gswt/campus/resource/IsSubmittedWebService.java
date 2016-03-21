package com.ibm.gswt.campus.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.ibm.gswt.campus.service.FeedbackService;
import com.ibm.gswt.campus.serviceImpl.FeedbackServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/IsSubmitted")
public class IsSubmittedWebService {
	
	private FeedbackService feedbackService = new FeedbackServiceImpl();

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void isSubmitted(@QueryParam("tel") String tel,
			@QueryParam("titleId") String titleId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		if (!feedbackService.isSubmitted(tel, titleId)) {
			JsonSendUtil.sendJsonData("Success", true, request, response);
		} else {
			JsonSendUtil.sendJsonData("Success", false, request, response);
		}
	}
}
