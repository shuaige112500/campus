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

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ibm.gswt.campus.bean.Event;
import com.ibm.gswt.campus.service.EventService;
import com.ibm.gswt.campus.serviceImpl.EventServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;
import com.ibm.gswt.campus.util.TimerUtil;

@Path("/SaveEventUrl")
public class SaveEventUrlWebService {
	
private static Logger logger = Logger.getLogger(SaveEventUrlWebService.class);
	
	private EventService eventService = new EventServiceImpl();

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void saveEvent(@QueryParam("jsonStr") String jsonStr,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		logger.info("save eventUrl");
		
		HttpSession session = request.getSession();
		if (session.getAttribute("admin") == null) {
			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
			return;
		}
		
		try{
			System.out.println(jsonStr);
			JSONObject itemObject = JSONObject.fromObject(jsonStr);
			
			Event event = new Event();
			event.setId((String) itemObject.get("id"));
			event.setEventUrl((String) itemObject.get("eventUrl"));
			event.setQrcode((String) itemObject.get("qrcode"));
			
			Date date = new Date();
			event.setModifyTS(TimerUtil.formateDate(date));
			
			eventService.saveEventUrl(event);
			
			JsonSendUtil.sendJsonData("Message", "Success", request, response);
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
}
