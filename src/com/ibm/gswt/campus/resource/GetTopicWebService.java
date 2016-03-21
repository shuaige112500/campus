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

import com.ibm.gswt.campus.bean.Topic;
import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.service.TopicService;
import com.ibm.gswt.campus.serviceImpl.TopicServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/GetTopicList")
public class GetTopicWebService {
	
	private TopicService topicService = new TopicServiceImpl();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void getTopicList(@QueryParam("eventId") String eventId,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws IOException {
		
		try {
			User user = (User)request.getSession().getAttribute("user");
			String pnum = user.getPnum();
			List<Topic> tplist = topicService.getTopicList(pnum, eventId);
			JsonSendUtil.sendJsonData("topics", tplist, request, response);
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
			e.printStackTrace();
		}
		
	}
}
