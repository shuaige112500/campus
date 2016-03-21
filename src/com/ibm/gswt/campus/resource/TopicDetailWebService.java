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

import org.apache.log4j.Logger;

import com.ibm.gswt.campus.bean.Comment;
import com.ibm.gswt.campus.bean.Speaker;
import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.service.CommentService;
import com.ibm.gswt.campus.service.SpeakerService;
import com.ibm.gswt.campus.serviceImpl.CommentServiceImpl;
import com.ibm.gswt.campus.serviceImpl.SpeakerServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/TopicDetail")
public class TopicDetailWebService {
	
	private SpeakerService speakerSerive = new SpeakerServiceImpl();
	
	private CommentService commentService = new CommentServiceImpl();
	
	private static Logger logger = Logger.getLogger(TopicDetailWebService.class);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void getEventById(@QueryParam("topicId") String topicId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String userId = String.valueOf(user.getUID());
		
		Speaker speaker = speakerSerive.getSpeaker(topicId, userId);
		
		List<Comment> comments = commentService.getCommentsBySpeaker(topicId);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("comments", comments);
		map.put("topic", speaker);
		
		JsonSendUtil.sendJsonData("result", map, request, response);
	}
}
