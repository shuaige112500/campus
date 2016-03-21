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

import com.ibm.gswt.campus.bean.FeedbackQuestion;
import com.ibm.gswt.campus.bean.Title;
import com.ibm.gswt.campus.service.FeedbackService;
import com.ibm.gswt.campus.serviceImpl.FeedbackServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;
import com.ibm.gswt.campus.util.SessionUtil;


/**
 * 
 * @author wangguoying
 * Get feedback questions
 */
@Path("/GetQuestions")
public class GetQuestionsWebService {
	
	private FeedbackService feedbackService = new FeedbackServiceImpl();
	
	private static Logger logger = Logger.getLogger(GetQuestionsWebService.class);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void getQuestionsById(@QueryParam("id") int id,
			@QueryParam("isUser") String isUser,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		if (isUser == null) {
			HttpSession session = request.getSession();
			if (!SessionUtil.isAdminUser(session, request, response)) {
				return;
			}
		}
		
		try {
			Title title = feedbackService.getTitleById(String.valueOf(id));
			
			List<FeedbackQuestion> questions = feedbackService.getQuestionByTitle(String.valueOf(id));
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", title.getTitle());
			map.put("description", title.getDescription());
			map.put("questionList", questions);
			
			JsonSendUtil.sendJsonData("questions", map, request, response);
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
			e.printStackTrace();
		}
	}
}
