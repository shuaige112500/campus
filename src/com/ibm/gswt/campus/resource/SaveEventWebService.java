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
import com.ibm.gswt.campus.service.ShakeService;
import com.ibm.gswt.campus.serviceImpl.EventServiceImpl;
import com.ibm.gswt.campus.serviceImpl.ShakeServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;
import com.ibm.gswt.campus.util.TimerUtil;

@Path("/SaveEvent")
public class SaveEventWebService {
	
	private static Logger logger = Logger.getLogger(SaveEventWebService.class);
	
	private EventService eventService = new EventServiceImpl();
	
	private ShakeService shakeService = new ShakeServiceImpl();

	/**
	 * when id is null, insert. otherwise, update
	 * @param jsonStr
	 * @param request
	 * @param response
	 * @param servletContext
	 * @throws IOException
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void saveEvent(@QueryParam("jsonStr") String jsonStr,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		logger.info("event insert start!");
		
		HttpSession session = request.getSession();
		if (session.getAttribute("admin") == null) {
			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
			return;
		}
		
		try{
			JSONObject itemObject = JSONObject.fromObject(jsonStr);
			
			Event event = new Event();
			
			if (itemObject.get("eventId") != null) {
				event.setId((String) itemObject.get("eventId"));
			}
			event.setCollege((String) itemObject.get("college"));
			event.setType((String) itemObject.get("type"));
			event.setDate((String) itemObject.get("date"));
			event.setStartTime((String) itemObject.get("starttime"));
			event.setEndTime((String) itemObject.get("endtime"));
			event.setAddress((String) itemObject.get("address"));
			event.setDescribe((String) itemObject.get("describe"));
			event.setTemplate((String) itemObject.get("template"));
			event.setShakeFlag((Boolean) itemObject.get("shakeFlag"));
			event.setLocation("");
			
			Date date = new Date();
			event.setCreateTS(TimerUtil.formateDate(date));
			event.setModifyTS(TimerUtil.formateDate(date));
			
			// save event
			int id = eventService.saveEvent(event);
			
			if (itemObject.get("eventId") == null) {
				shakeService.addStatusRecord(String.valueOf(id));
			}
			
			JsonSendUtil.sendJsonData("EventID", String.valueOf(id), request, response);
			logger.info("event insert end!");
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
	
}
