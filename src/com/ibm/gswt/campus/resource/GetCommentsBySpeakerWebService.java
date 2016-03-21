package com.ibm.gswt.campus.resource;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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


@Path("/GetCommentsBySpeaker")
public class GetCommentsBySpeakerWebService {
	
	private CommentService commentService = new CommentServiceImpl();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void getComment(@QueryParam("speakerId") String speakerId,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws IOException {

		List<Comment> comments = commentService.getCommentsBySpeaker(speakerId);
		
		JsonSendUtil.sendJsonData("Comments", comments, request, response);
	}
}
