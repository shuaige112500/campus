package com.ibm.gswt.campus.service;

import java.util.List;

import com.ibm.gswt.campus.bean.Speaker;

public interface SpeakerService {

	public int saveSpeaker(Speaker speaker);
	
	public void deleteSpeaker(String eid);
	
	public List<Speaker> getSpeakerByEid(String eid);
	
	public void deleteSpeakerById(String speakerId);
	
	public Speaker getSpeaker(String speakerId, String userId);
	
	public Speaker getSpeakerById(String speakerId);
}
