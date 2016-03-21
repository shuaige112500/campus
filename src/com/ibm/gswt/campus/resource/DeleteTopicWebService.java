package com.ibm.gswt.campus.resource;

import java.io.File;
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

import com.ibm.gswt.campus.service.SpeakerService;
import com.ibm.gswt.campus.serviceImpl.SpeakerServiceImpl;
import com.ibm.gswt.campus.util.ApplicationProperties;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/DeleteTopic")
public class DeleteTopicWebService {
	
private SpeakerService speakerService = new SpeakerServiceImpl();
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void deleteTopic(@QueryParam("speakerId") String speakerId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
//		HttpSession session = request.getSession();
//		if (session.getAttribute("admin") == null) {
//			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
//			return;
//		}
		
		try {
			speakerService.deleteSpeakerById(speakerId);
			
			deleteFile(speakerService.getSpeakerById(speakerId).getSpeakerImg());
			deleteFile(speakerService.getSpeakerById(speakerId).getSpeakerFile());
			
			JsonSendUtil.sendJsonData("Message", "Success", request, response);
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
	
	private void deleteFile(String fileName) {
		
		String fullFilename = ApplicationProperties.getProps().getProperty("user.file.path")
				+ fileName.substring(ApplicationProperties.getProps().getProperty("user.upload.path").length());
		
		int index = fullFilename.lastIndexOf("/");
		
		File file = new File(fullFilename);
		if (file.exists()) {
			file.delete();
		}
		
		File directory = new File(fullFilename.substring(0, index));
		if (directory.isDirectory()) {
			directory.delete();
		}
	}
}
