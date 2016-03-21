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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ibm.gswt.campus.bean.Answer;
import com.ibm.gswt.campus.service.FeedbackService;
import com.ibm.gswt.campus.serviceImpl.FeedbackServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;
import com.ibm.gswt.campus.util.TimerUtil;

@Path("/SaveAnswers")
public class SaveAnswersWebService {
	
	private static Logger logger = Logger.getLogger(SaveAnswersWebService.class);
	
	private FeedbackService feedbackService = new FeedbackServiceImpl();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void saveEvent(@QueryParam("jsonStr") String jsonStr,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		try{
			JSONObject itemObject = JSONObject.fromObject(jsonStr);
			
			JSONArray array = itemObject.getJSONArray("answers");
			
			String tel = (String) itemObject.get("tel");
			
			Date date = new Date();
			String dateStr = TimerUtil.formateDate(date);
			
			JSONObject object = null;
			Answer answer = null;
			for (int i = 0; i < array.size(); i++) {
				object = (JSONObject) array.get(i);
				answer = new Answer();
				answer.setQuestionId((String) object.get("questionId"));
				answer.setAnswer((String) object.get("answer"));
				answer.setTel(tel);
				answer.setCreateTime(dateStr);
				feedbackService.saveAnswer(answer);
			}
			
			JsonSendUtil.sendJsonData("Message", "Success", request, response);
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
	
}
