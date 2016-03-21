package com.ibm.gswt.campus.resource;

import java.io.IOException;

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

import com.ibm.gswt.campus.service.CommentService;
import com.ibm.gswt.campus.serviceImpl.CommentServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/SaveQuestion")
public class CollectQuestionWebService {
	
	private CommentService commentService = new CommentServiceImpl();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void collectQuestion(@QueryParam("jsonStr") String jsonStr,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws IOException {
		
//		HttpSession session = request.getSession();
//		if (session.getAttribute("admin") == null) {
//			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
//			return;
//		}
		
		JSONArray jarr = JSONArray.fromObject(jsonStr);

		try {
			for(int i=0; i<jarr.size(); i++) {
				int cid = Integer.valueOf((String)jarr.getJSONObject(i).get("cid")).intValue();
				String choosen = (String) jarr.getJSONObject(i).get("choosen");
				commentService.updateComments(cid, choosen);
			}
			JsonSendUtil.sendJsonData("Message", "Success", request, response);
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
}
