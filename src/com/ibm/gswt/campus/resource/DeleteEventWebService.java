package com.ibm.gswt.campus.resource;

import java.io.IOException;

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

import com.ibm.gswt.campus.service.EventService;
import com.ibm.gswt.campus.service.SpeakerService;
import com.ibm.gswt.campus.serviceImpl.EventServiceImpl;
import com.ibm.gswt.campus.serviceImpl.SpeakerServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/DeleteEvent")
public class DeleteEventWebService {
	
	private EventService eventService = new EventServiceImpl();
	
	private SpeakerService speakerService = new SpeakerServiceImpl();
	
	private static Logger logger = Logger.getLogger(DeleteEventWebService.class);

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void deleteEvent(@QueryParam("id") String id,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
//		HttpSession session = request.getSession();
//		if (session.getAttribute("admin") == null) {
//			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
//			return;
//		}
		
		try {
			eventService.deleteEvent(Integer.parseInt(id));
			
			speakerService.deleteSpeaker(id);
			
			logger.info("delete event, id = " + id);
			JsonSendUtil.sendJsonData("Message", "Success", request, response);
			
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
}
