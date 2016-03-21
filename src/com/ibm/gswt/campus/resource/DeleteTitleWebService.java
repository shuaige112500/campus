package com.ibm.gswt.campus.resource;

import java.io.IOException;

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

import com.ibm.gswt.campus.service.FeedbackService;
import com.ibm.gswt.campus.serviceImpl.FeedbackServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/DeleteTitle")
public class DeleteTitleWebService {
	
	private FeedbackService feedbackService = new FeedbackServiceImpl();
	
	private static Logger logger = Logger.getLogger(DeleteEventWebService.class);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void deleteEvent(@QueryParam("titleId") String titleId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		try {
			
//			HttpSession session = request.getSession();
//			if (session.getAttribute("admin") == null) {
//				JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
//				return;
//			}
			
			feedbackService.deleteTitleById(titleId);
			
			feedbackService.deleteQuestionsByTitleId(titleId);
			
			JsonSendUtil.sendJsonData("Message", "Success", request, response);
			
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
}
