package com.ibm.gswt.campus.resource;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ibm.gswt.campus.bean.Comment;
import com.ibm.gswt.campus.service.CommentService;
import com.ibm.gswt.campus.serviceImpl.CommentServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;
import com.ibm.gswt.campus.util.TimerUtil;

@Path("/SaveAdminComments")
public class SaveAdminCommentsWebService {
	
	private CommentService commentService = new CommentServiceImpl();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void saveAdminComments(@QueryParam("eventId") String eventId,
			@QueryParam("jsonStr") String jsonStr,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession();
		if (session.getAttribute("admin") == null) {
			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
			return;
		}
		
		try {
			JSONArray jsonArray = JSONArray.fromObject(jsonStr);
			JSONObject jsonObject = new JSONObject();
			
			commentService.deleteAdminComments(eventId);
			
			Date date = new Date();
			Comment comment = null;
			for (int i = 0; i < jsonArray.size(); i++) {
				
				jsonObject = jsonArray.getJSONObject(i);
				
				comment = new Comment();
				if (jsonObject.get("cid") != null) {
					comment.setCid(jsonObject.getString("cid"));
				}
				comment.setComments(jsonObject.getString("comments"));
				comment.setUserName(jsonObject.getString("from"));
				comment.setChoosen(jsonObject.getString("checked"));
				comment.setUID(0);
				comment.setTopicId(Integer.parseInt(eventId));
				comment.setCreatedTS(TimerUtil.formateDate(date));
				comment.setModifiedTS(TimerUtil.formateDate(date));

				// add the new admin comments
				commentService.saveAdminComment(comment);
				
//				List<Comment> adminComments = commentService.getAdminComments(eventId);
			}
			JsonSendUtil.sendJsonData("Message", "Success", request, response);
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
}
