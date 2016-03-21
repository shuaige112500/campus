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

import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.service.RatingService;
import com.ibm.gswt.campus.serviceImpl.RatingServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/HandleRating")
public class HandleRatingWebService {
	
	private RatingService ratingService = new RatingServiceImpl();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void addRating(@QueryParam("checked") String checked,
			@QueryParam("topicId") String topicId,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws IOException {
		
		User user =(User) request.getSession().getAttribute("user");
		int uid=user.getUID();
		
		try {
			int id = Integer.parseInt(topicId);
			
			ratingService.handleRating(id, checked);
			if(checked.equals("Y")){
				ratingService.insertRatingRelated(id, uid);
			}
			if(checked.equals("N")){
				ratingService.deleteRatingRelated(id, uid);
			}
			JsonSendUtil.sendJsonData("message", "Success", request, response);

		} catch (Exception e) {
			e.printStackTrace();
			JsonSendUtil.sendJsonData("message", "Error", request, response);
		}
	}
}
