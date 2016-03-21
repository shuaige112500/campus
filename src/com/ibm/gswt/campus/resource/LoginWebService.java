package com.ibm.gswt.campus.resource;

import java.io.IOException;
import java.io.PrintWriter;

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

import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.bean.UserLog;
import com.ibm.gswt.campus.service.UserLogService;
import com.ibm.gswt.campus.service.UserService;
import com.ibm.gswt.campus.serviceImpl.UserLogServiceImpl;
import com.ibm.gswt.campus.serviceImpl.UserServiceImpl;


@Path("/Login")
public class LoginWebService {
	
	private static Logger logger = Logger.getLogger(LoginWebService.class);
	private UserService userService = new UserServiceImpl();
	private UserLogService userLogService = new UserLogServiceImpl();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void Login(@QueryParam("tel") String tel, 
			@QueryParam("checkIn") String checkIn,
			@QueryParam("eventId") String eventId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		logger.info("tel====" + tel);
		logger.info("checkIn====" + checkIn);
		
		User user = userService.getUser(tel, eventId);
		
		JSONObject itemObject = new JSONObject();
		
		if (null != user) {
			//insert log table
			UserLog userLog = new UserLog();
			userLog.setUsername(user.getUsername());
//			userLog.setWechat(user.getWechat());
			userLog.setPnum(user.getPnum());
			userLog.setLocation(eventId);
			if("Y".equals(checkIn)){
				userLog.setChecked("1");
			}else{
				userLog.setChecked("");
			}
			
			userLogService.addUserLog(userLog);
			
			HttpSession userSession = request.getSession();
			userSession.setAttribute("user", user);
			
			itemObject.put("flag", "1");
			itemObject.put("msg", "success");
			itemObject.put("uname", user.getUsername());
			itemObject.put("uid", user.getUID());
			
			// check in
//			userService.checkIn();
			
		} else {
			
			itemObject.put("flag", "0");
			itemObject.put("msg", "error_tel_not_existed");
		}
		
		logger.info("itemObject====" + itemObject);
		PrintWriter out = response.getWriter();
		out.println(request.getParameter("callback") + "("+itemObject+")");
		out.flush();
		out.close();
	}

}