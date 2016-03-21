package com.ibm.gswt.campus.resource;

import java.io.IOException;
import java.util.ArrayList;
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

import com.ibm.gswt.campus.bean.Comment;
import com.ibm.gswt.campus.service.CommentService;
import com.ibm.gswt.campus.serviceImpl.CommentServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;
import com.ibm.jjson.JsonMap;

@Path("/GetQuestion")
public class GetQuestionWebService {
	
	private CommentService commentService = new CommentServiceImpl();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void getQuestion(@QueryParam("eventId") String eventId,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession();
		if (session.getAttribute("admin") == null) {
			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
			return;
		}
		
		try {
			List<Comment> comments = commentService.getQuestions(eventId);
			
			JsonMap<String, Object> modelMap = new JsonMap<String, Object>();
			modelMap.put("Questions", comments);
			
			List<JsonMap> reslist = new ArrayList<JsonMap>();
			reslist.add(modelMap);
			// return data
			JsonSendUtil.sendJsonData("allQuestions", reslist, request, response);
			
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
}
