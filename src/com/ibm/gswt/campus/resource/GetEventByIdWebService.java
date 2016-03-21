package com.ibm.gswt.campus.resource;

import java.io.IOException;
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

import org.apache.log4j.Logger;

import com.ibm.gswt.campus.bean.Event;
import com.ibm.gswt.campus.bean.Speaker;
import com.ibm.gswt.campus.service.EventService;
import com.ibm.gswt.campus.service.SpeakerService;
import com.ibm.gswt.campus.serviceImpl.EventServiceImpl;
import com.ibm.gswt.campus.serviceImpl.SpeakerServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/GetEventById")
public class GetEventByIdWebService {
	
	private EventService eventService = new EventServiceImpl();
	
	private SpeakerService speakerSerive = new SpeakerServiceImpl();
	
	private static Logger logger = Logger.getLogger(GetEventByIdWebService.class);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void getEventById(@QueryParam("eventId") int eventId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession();
		if (session.getAttribute("admin") == null) {
			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
			return;
		}
		
		logger.info("get event by id = " + eventId);
		
		Event event = eventService.getEventById(eventId);
		
		List<Speaker> speakers = speakerSerive.getSpeakerByEid(String.valueOf(eventId));
		
		event.setSpeakers(speakers);
		
		JsonSendUtil.sendJsonData("event", event, request, response);
	}
}
