package com.ibm.gswt.campus.serviceImpl;

import java.util.List;

import com.ibm.gswt.campus.bean.Speaker;
import com.ibm.gswt.campus.dao.SpeakerDao;
import com.ibm.gswt.campus.daoImpl.SpeakerDaoImpl;
import com.ibm.gswt.campus.service.SpeakerService;

public class SpeakerServiceImpl implements SpeakerService {

	private SpeakerDao speakerDao = new SpeakerDaoImpl();

	@Override
	public int saveSpeaker(Speaker speaker) {
		return speakerDao.saveSpeaker(speaker);
	}

	@Override
	public List<Speaker> getSpeakerByEid(String eid) {
		return speakerDao.getSpeakerByEid(eid);
	}

	@Override
	public void deleteSpeaker(String eid) {
		speakerDao.deleteSpeaker(eid);
		
	}

	@Override
	public void deleteSpeakerById(String speakerId) {
		try {
			speakerDao.deleteSpeakerById(speakerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Speaker getSpeaker(String speakerId, String userId) {
		Speaker speaker = null;
		try {
			speaker = speakerDao.getSpeaker(speakerId, userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return speaker;
	}

	@Override
	public Speaker getSpeakerById(String speakerId) {
		Speaker speaker = null;
		try {
			speaker = speakerDao.getSpeakerById(speakerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return speaker;
	}
}
