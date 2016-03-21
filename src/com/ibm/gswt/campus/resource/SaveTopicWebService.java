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

import com.ibm.gswt.campus.bean.Speaker;
import com.ibm.gswt.campus.service.SpeakerService;
import com.ibm.gswt.campus.serviceImpl.SpeakerServiceImpl;
import com.ibm.gswt.campus.util.ApplicationProperties;
import com.ibm.gswt.campus.util.JsonSendUtil;
import com.ibm.gswt.campus.util.TimerUtil;

@Path("/SaveTopic")
public class SaveTopicWebService {

	private static Logger logger = Logger.getLogger(SaveTopicWebService.class);	
	
	private SpeakerService speakerService = new SpeakerServiceImpl();
	
	String directory = ApplicationProperties.getProps().getProperty("user.upload.path");
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void saveEvent(@QueryParam("jsonStr") String jsonStr,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		logger.info("topic save start!");
		
		HttpSession session = request.getSession();
		if (session.getAttribute("admin") == null) {
			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
			return;
		}
		
		try {
			
			JSONObject itemObject = JSONObject.fromObject(jsonStr);
			
			String topicId = "0";
			
			Speaker speaker = new Speaker();
			if (itemObject.get("id") != null) {
				speaker.setId((String) itemObject.get("id"));
				topicId = (String) itemObject.get("id");
			}
			Speaker oldSpeaker = speakerService.getSpeakerById(topicId);
			
			speaker.setEventId((String) itemObject.get("eventId"));
			speaker.setSpeakerName((String) itemObject.get("speakerName"));
			speaker.setSpeakerDesc((String) itemObject.get("speakerDesc"));
			
			// speakerImg had been uploaded
			if (itemObject.get("speakerImg") != null && !"".equals(itemObject.get("speakerImg"))) {
				if (oldSpeaker.getSpeakerImg() != null) {
					// image didn't change
					if (oldSpeaker.getSpeakerImg().equals((String) itemObject.get("speakerImg"))) {
						speaker.setSpeakerImg((String) itemObject.get("speakerImg"));
					} else {
						// delete old file
//						deleteFile(oldSpeaker.getSpeakerImg());
						
						speaker.setSpeakerImg(directory + (String) itemObject.get("speakerImg"));
					}
				} else {
					// new Image
					speaker.setSpeakerImg(directory + (String) itemObject.get("speakerImg"));
				}
			}
			
			if (itemObject.get("speakerFile") != null && !"".equals(itemObject.get("speakerFile"))) {
				if (oldSpeaker.getSpeakerFile() != null) {
					// attachment didn't change
					if (oldSpeaker.getSpeakerFile().equals((String) itemObject.get("speakerFile"))) {
						speaker.setSpeakerFile((String) itemObject.get("speakerFile"));
					} else {
						// delete old file
//						deleteFile(oldSpeaker.getSpeakerFile());
						
						speaker.setSpeakerFile(directory + (String) itemObject.get("speakerFile"));
					}
				} else {
					// new attachment
					speaker.setSpeakerFile(directory + (String) itemObject.get("speakerFile"));
				}
			}
			speaker.setTopicName((String) itemObject.get("topicName"));
			speaker.setTopicDesc((String) itemObject.get("topicDesc"));
			speaker.setStartTime((String) itemObject.get("startTime"));
			speaker.setEndTime((String) itemObject.get("endTime"));
			
			Date date = new Date();
			speaker.setCreateTS(TimerUtil.formateDate(date));
			speaker.setModifyTS(TimerUtil.formateDate(date));
			
			int id = speakerService.saveSpeaker(speaker);
			
			JsonSendUtil.sendJsonData("TopicID", String.valueOf(id), request, response);
			logger.info("topic save end!");
		
		} catch (Exception e) {
			JsonSendUtil.sendJsonData("Message", "Error", request, response);
		}
	}
	
//	private void deleteFile(String fileName) {
//		
//		String fullFilename = ApplicationProperties.getProps().getProperty("user.file.path") + fileName.substring(directory.length());
//		
//		int index = fullFilename.lastIndexOf("/");
//		
//		File file = new File(fullFilename);
//		if (file.exists()) {
//			file.delete();
//		}
//		
//		File directory = new File(fullFilename.substring(0, index));
//		if (directory.isDirectory()) {
//			directory.delete();
//		}
//	}
}
