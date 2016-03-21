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

import com.ibm.gswt.campus.bean.FeedbackQuestion;
import com.ibm.gswt.campus.bean.Survey;
import com.ibm.gswt.campus.service.FeedbackService;
import com.ibm.gswt.campus.serviceImpl.FeedbackServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;
import com.ibm.gswt.campus.util.TimerUtil;

@Path("/SaveFeedbackQuestion")
public class SaveFeedbackQuestionWebService {
	
	private FeedbackService feedbackService = new FeedbackServiceImpl();

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void saveFeedbackQuestion(@QueryParam("jsonStr") String jsonStr,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession();
		if (session.getAttribute("admin") == null) {
			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
			return;
		}
		
		JSONObject object = JSONObject.fromObject(jsonStr);
		
		FeedbackQuestion question = null;
		try {
			if (object.get("titleId") != null) {
				feedbackService.deleteQuestionsByTitleId((String) object.get("titleId"));
				
				feedbackService.deleteAnswers((String) object.get("titleId"));
			}
			
			Survey survey = new Survey();
			survey.setsId((String) object.get("titleId"));
			survey.setContent((String) object.get("title"));
			survey.setDescription((String) object.get("description"));
			Date date = new Date();
			
			survey.setCreatedTs(TimerUtil.formateDate(date));
			survey.setModifiedTs(TimerUtil.formateDate(date));
			
			int tid = feedbackService.saveTitle(survey);
			
			JSONArray array = object.getJSONArray("questions");
			
			for (int i = 0; i < array.size(); i++) {
				JSONObject itemObject = (JSONObject) array.get(i);
				question = new FeedbackQuestion();
				question.setQuestion((String) itemObject.get("question"));
				question.setType((String) itemObject.get("type"));
				question.setTid(tid);
				question.setOrderId(String.valueOf(i + 1));
				if (itemObject.get("options") != null) {
					JSONArray optionArray = itemObject.getJSONArray("options");
					
					String options = null;
					for (int j = 0; j < optionArray.size(); j++) {
						String subObject = (String) optionArray.get(j);

						if (j == 0) {
							options = subObject;
						} else {
							options = options + "_" + subObject;
						}
					}
					question.setAlternate(options);
				}
				feedbackService.saveQuestion(question);
			}
			JsonSendUtil.sendJsonData("Message", tid, request, response);
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
}
