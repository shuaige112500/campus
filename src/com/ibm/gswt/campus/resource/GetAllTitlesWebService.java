package com.ibm.gswt.campus.resource;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.ibm.gswt.campus.bean.Title;
import com.ibm.gswt.campus.service.FeedbackService;
import com.ibm.gswt.campus.serviceImpl.FeedbackServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/GetAllTitles")
public class GetAllTitlesWebService {
	
	private FeedbackService feedbackService = new FeedbackServiceImpl();
	
	private static Logger logger = Logger.getLogger(GetAllTitlesWebService.class);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void getAllEvents(@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
//		HttpSession session = request.getSession();
//		if (session.getAttribute("admin") == null) {
//			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
//			return;
//		}
		
		logger.info("get all titles");
		
		List<Title> titles = feedbackService.getAllTitles();
		JsonSendUtil.sendJsonData("titles", titles, request, response);
	}
}
