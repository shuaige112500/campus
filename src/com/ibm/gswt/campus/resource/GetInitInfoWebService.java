package com.ibm.gswt.campus.resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.ibm.gswt.campus.bean.Event;
import com.ibm.gswt.campus.service.EventService;
import com.ibm.gswt.campus.serviceImpl.EventServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/GetInitInfo")
public class GetInitInfoWebService {
	
	private EventService eventService = new EventServiceImpl();
	
	private static Logger logger = Logger.getLogger(GetEventByIdWebService.class);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void getEventById(@QueryParam("eventId") int eventId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		logger.info("get event by id = " + eventId);
		
		Event event = eventService.getEventById(eventId);
		
		Map<String, String> initInfo = new HashMap<String, String>();
		initInfo.put("template", event.getTemplate());
		initInfo.put("date", event.getDate());
		initInfo.put("college", event.getCollege());
		initInfo.put("isShaked", event.getShakeFlag() ? "1" : "0");
		initInfo.put("description", event.getDescribe());
		
		
		JsonSendUtil.sendJsonData("initInfo", initInfo, request, response);
	}
}
