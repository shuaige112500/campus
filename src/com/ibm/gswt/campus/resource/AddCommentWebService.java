package com.ibm.gswt.campus.resource;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.ibm.gswt.campus.bean.Comment;
import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.service.CommentService;
import com.ibm.gswt.campus.serviceImpl.CommentServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;
import com.ibm.gswt.campus.util.TimerUtil;

@Path("/AddComment")
public class AddCommentWebService {
	
	private CommentService commentService = new CommentServiceImpl();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void addComment(@QueryParam("comments") String comments,
			@QueryParam("topicId") String topicId,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws IOException {
		
		try {
			User user = (User)request.getSession().getAttribute("user");
			
			Comment comment=new Comment();
			comment.setTopicId(Integer.parseInt(topicId));
			comment.setComments(comments);
			comment.setUserName(user.getUsername());
			comment.setPnum(user.getPnum());
			comment.setUID(user.getUID());
			comment.setChoosen("0");
			comment.setIsDeleted("0");
			
			Date currentDate = new Date();
			String date = TimerUtil.formateDate(currentDate);
			comment.setCreatedTS(date);
			comment.setModifiedTS(date);
			
			commentService.addComment(comment);
			JsonSendUtil.sendJsonData("Message", "Success", request, response);
			
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
}
