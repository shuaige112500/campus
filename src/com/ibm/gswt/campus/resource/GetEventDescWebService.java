package com.ibm.gswt.campus.resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

import com.ibm.gswt.campus.bean.Comment;
import com.ibm.gswt.campus.bean.LotteryReport;
import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.service.CommentService;
import com.ibm.gswt.campus.service.TopicService;
import com.ibm.gswt.campus.service.UserLogService;
import com.ibm.gswt.campus.serviceImpl.CommentServiceImpl;
import com.ibm.gswt.campus.serviceImpl.TopicServiceImpl;
import com.ibm.gswt.campus.serviceImpl.UserLogServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/GetEventDesc")
public class GetEventDescWebService {

	private UserLogService userLogService = new UserLogServiceImpl();
	
	private CommentService commentService = new CommentServiceImpl();
	
	private TopicService topicService = new TopicServiceImpl();
	
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
			
		 try{
			 	List<Comment> comments = commentService.getAllComments(eventId);
				
				List<Comment> adminComments = commentService.getAdminComments(eventId);
				
				List<User> registerUsers = userLogService.getRegisterUsers(eventId);
				
				List<User> checkedUsers = userLogService.getCheckedUsers(eventId);
				
				List<LotteryReport> lotteryList = topicService.getLotteryByLocation(eventId);
				
				Map<String, Object> descInfo = new HashMap<String, Object>();
				descInfo.put("comments", comments);
				descInfo.put("adminComments", adminComments);
				descInfo.put("registerUsers", registerUsers);
				descInfo.put("checkedUsers", checkedUsers);
				descInfo.put("lotteryList", lotteryList);
				
				JsonSendUtil.sendJsonData("descInfo", descInfo, request, response);
		  } catch (Exception e) {
				JsonSendUtil.sendJsonData("Message", "Error", request, response);
			e.printStackTrace();
		  }
	}
}
