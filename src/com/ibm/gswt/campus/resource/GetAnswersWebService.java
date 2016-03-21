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

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ibm.gswt.campus.bean.Answer;
import com.ibm.gswt.campus.service.FeedbackService;
import com.ibm.gswt.campus.serviceImpl.FeedbackServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/GetAnswers")
public class GetAnswersWebService {
	
	private FeedbackService feedbackService = new FeedbackServiceImpl();
	
	private static Logger logger = Logger.getLogger(GetAllTitlesWebService.class);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void getAllEvents(@QueryParam("titleId") String titleId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		logger.info("get all answers");
		
//		HttpSession session = request.getSession();
//		if (session.getAttribute("admin") == null) {
//			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
//			return;
//		}
		
		try {
			List<Answer> answers = feedbackService.getAllAnswers(titleId);
			
			List<JSONObject> feedbackList = new ArrayList<JSONObject>();
			List<String> answerList = null;
			JSONObject object = new JSONObject();
			String tempTel = null;
			
			for (Answer answer : answers) {
				if (tempTel == null) {
					tempTel = answer.getTel();
					answerList = new ArrayList<String>();
				} else if (!tempTel.equals(answer.getTel())) {
					object.put("tel", tempTel);
					object.put("answer", answerList);
					feedbackList.add(object);
					object = new JSONObject();
					answerList = new ArrayList<String>();
					tempTel = answer.getTel();
				}
				answerList.add(answer.getAnswer());
			}
			object.put("tel", tempTel);
			object.put("answer", answerList);
			feedbackList.add(object);
			
			JsonSendUtil.sendJsonData("answers", feedbackList, request, response);
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
}
