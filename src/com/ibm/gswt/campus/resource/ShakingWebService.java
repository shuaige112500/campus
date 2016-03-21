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

import org.apache.log4j.Logger;

import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.service.TopicService;
import com.ibm.gswt.campus.serviceImpl.TopicServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/Shaking")
public class ShakingWebService {
	private TopicService topicService = new TopicServiceImpl();
	
	private Logger logger = Logger.getLogger(ShakingWebService.class);
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void doShake(@QueryParam("pnum") String pnum,
			@QueryParam("uid") String uid,
			@QueryParam("eventId") String eventId,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws IOException {
		
		logger.info("start shake");
		logger.info("uid====" + uid);
		logger.info("pnum====" + pnum);
		
		try {
			// check status
			String status = topicService.checkStatus(eventId);
			
			User user = (User) request.getSession().getAttribute("user");
			if("Y".equals(status)) {// success
				// the user who is winning is not allowed shake again
				if (!"1".equals(user.getLucky())) {
					// insert CANDIDATE table
					topicService.insertCan(uid, pnum, eventId);
				}
				JsonSendUtil.sendJsonData("status", "1", request, response);
				
			} else {// not start yet
				JsonSendUtil.sendJsonData("status", "2", request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			JsonSendUtil.sendJsonData("status", "0", request, response);
		}
	}
}
