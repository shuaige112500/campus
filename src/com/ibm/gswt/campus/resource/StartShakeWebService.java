package com.ibm.gswt.campus.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

import com.ibm.gswt.campus.bean.LotteryReport;
import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.service.TopicService;
import com.ibm.gswt.campus.serviceImpl.TopicServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;
import com.ibm.gswt.campus.util.TimerUtil;
import com.ibm.jjson.JsonMap;

@Path("/StartShake")
public class StartShakeWebService {
	
	private TopicService topicService = new TopicServiceImpl();

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void startShake(@QueryParam("eventId") String eventId,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession();
		if (session.getAttribute("admin") == null) {
			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
			return;
		}
		
		try {
			
			String ready = request.getParameter("shakeSwitch");// Y or N

			// update shake status
			topicService.updateStatus(ready, eventId);

			if("N".equals(ready)){
				List<User> pnumList = topicService.getCan(eventId);
				JsonMap<String, Object> jsonMap = new JsonMap<String, Object>();
				User user = new User();
				boolean selected = false;
				LotteryReport lottery = new LotteryReport();
				lottery.setNumber("" + pnumList.size());
				lottery.setLocation(eventId);
				
				List<String> list = new ArrayList<String>();
				if(pnumList != null && pnumList.size() > 0) {
					for(int i=0; i < pnumList.size(); i++) {
						list.add(pnumList.get(i).getPnum());
					}
					int random = (int) (Math.random() * list.size());
					String pnum = pnumList.get(random).getPnum();
					int uid = pnumList.get(random).getUID();
					String maskPnum = pnum.substring(0,3)+"****"+pnum.substring(7);
					user = topicService.searchUser(uid);
					if(user != null && !"1".equals(user.getLucky())) {
						//update User table
						topicService.updateUser(uid);
						selected = true;
						jsonMap.put("status", "N");
						jsonMap.put("userName", user.getUsername());
						jsonMap.put("cellphone", maskPnum);
						JsonSendUtil.sendJsonData("Result", jsonMap, request, response);
						lottery.setUid(""+user.getUID());
						lottery.setUsername(user.getUsername());
						lottery.setPnum(user.getPnum());
						lottery.setSchool(user.getSchool());
						lottery.setEmail(user.getEmail());
						lottery.setMajor(user.getMajor());
						lottery.setGrade(user.getGrade());
					}
					
					if(!selected) {
						JsonSendUtil.sendJsonData("Message", "no one is selected", request, response);
					}
					
				} else {
					JsonSendUtil.sendJsonData("Message", "no phone number", request, response);
				}
				
				Date currentDate = new Date();
				lottery.setEndTs(TimerUtil.formateDate(currentDate));
				topicService.updateShakeEndTime(lottery);
				
				// clear CANDIDATE table
				topicService.deleteCan(eventId);

			} else {
				
				topicService.updateShakeStartTime(eventId);
				
				JsonSendUtil.sendJsonData("Message", "Shake start", request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
}
