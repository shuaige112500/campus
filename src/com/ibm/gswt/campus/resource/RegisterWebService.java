package com.ibm.gswt.campus.resource;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

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

@Path("/Register")
public class RegisterWebService {
	
	private static Logger logger = Logger.getLogger(RegisterWebService.class);
	private UserService userService = new UserServiceImpl();
	private UserLogService userLogService = new UserLogServiceImpl();
	private static String phoneReg = "^(1)\\d{10}$";
	private static String emailReg = "^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$";
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void Register(@QueryParam("name") String name,
			@QueryParam("school") String school, @QueryParam("tel") String tel,
//			@QueryParam("weichat") String weichat,
//			@QueryParam("code") String code,
			@QueryParam("major") String major,
			@QueryParam("email") String email,
			@QueryParam("grade") String grade,
			@QueryParam("eventId") String eventId,
			@QueryParam("checkIn") String checkIn,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		logger.info("---Register---");
		
		logger.info("---name---" + name);
		logger.info("---school---" + school);
		logger.info("---tel---" + tel);
//		logger.info("---weichat---" + weichat);
//		logger.info("---code---" + code);
		logger.info("---major---" + major);
		logger.info("---mail---" + email);
		logger.info("---grade---" + grade);
		logger.info("---eventId---" + eventId);
		logger.info("---checkIn---" + checkIn);
		
		JSONObject itemObject = new JSONObject();
		Pattern telPattern = Pattern.compile(phoneReg);
		Pattern emailPattern = Pattern.compile(emailReg);
		
		
		if(null==name||"".equals(name)
				||null==school||"".equals(school)
				||null==tel||"".equals(tel)
				||null==major||"".equals(major)
				||null==email||"".equals(email)
				||null==grade||"".equals(grade)){
			
			itemObject.put("flag", "0");
			itemObject.put("msg", "error_blank_fields");
			
		}else if(!telPattern.matcher(tel).matches()){
			
			itemObject.put("flag", "0");
			itemObject.put("msg", "error_tel");
			
		}else if(!emailPattern.matcher(email).matches()){
			
			itemObject.put("flag", "0");
			itemObject.put("msg", "error_email");
			
		}else{
		
			User user = userService.getUser(tel, eventId);
			
			if (null == user) {
				
//				if (null != request.getSession().getAttribute("num")) {
//					if (code.equals(request.getSession().getAttribute("num"))) {
//						if (null != request.getSession().getAttribute("num")){
//							request.getSession().removeAttribute("num");
//						}
					
					User dbuser = new User();
					dbuser.setUsername(name);
					dbuser.setSchool(school);
					dbuser.setPnum(tel);
//					dbuser.setWechat(weichat);
					dbuser.setMajor(major);
					dbuser.setEmail(email);
					dbuser.setGrade(grade);
					dbuser.setEventId(eventId);
					
					userService.addUser(dbuser);
					User newuser = userService.getUser(dbuser.getPnum(), eventId);
					
					if("Y".equals(checkIn)){
						UserLog userLog = new UserLog();
						userLog.setUsername(newuser.getUsername());
						userLog.setPnum(newuser.getPnum());
						userLog.setChecked("1");
						userLog.setLocation(eventId);
						userLogService.addUserLog(userLog);
						
						HttpSession userSession = request.getSession();
						userSession.setAttribute("user", newuser);
					}
					
					itemObject.put("flag", "1");
					itemObject.put("msg", "success");
					itemObject.put("uname", newuser.getUsername());
					itemObject.put("uid", newuser.getUID());
						
//					} else {
//						itemObject.put("flag", "0");
//						itemObject.put("msg", "error_code");
//					}
//				} 
				
			} else {
				
				itemObject.put("flag", "0");
				itemObject.put("msg", "error_tel_existed");
			}
		
		}
		
		logger.info("itemObject====" + itemObject);
		PrintWriter out = response.getWriter();
		out.println(request.getParameter("callback") + "("+itemObject+")");
		out.flush();
		out.close();
		
	}
}