package com.ibm.gswt.campus.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ibm.gswt.campus.bean.LotteryReport;
import com.ibm.gswt.campus.bean.Topic;
import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.dao.TopicDao;
import com.ibm.gswt.campus.daoImpl.TopicDaoImpl;
import com.ibm.gswt.campus.service.TopicService;
import com.ibm.gswt.campus.util.TimerUtil;

public class TopicServiceImpl implements TopicService {

	private TopicDao topicDao = new TopicDaoImpl();
	
	public List<Topic> getTopicList(String pnum, String location) {
		
		List<Topic> result = new ArrayList<Topic>();
		
		try {
			result = topicDao.getTopicList(pnum, location);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	public List<Topic> getTopics(String intranetID, String userName, String userImg) {
		
		List<Topic> result = new ArrayList<Topic>();
		
		try {
			result = topicDao.getTopics(intranetID, userName, userImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
		
	}

	public Topic getTopicByID(int topicID) {
		
		Topic currentTopic = new Topic();
		currentTopic = topicDao.getTopicByID(topicID);
		return currentTopic;
	}

	public int updateTopic(Topic topic) {
		
		int returnCd = -100;
		returnCd = topicDao.updateTopic(topic);
		return returnCd;
	}

	public void addTopic(Topic topic) {
		
		topicDao.addTopic(topic);
		
	}

	public void deleteTopic(int topicID) {
		
		topicDao.deleteTopic(topicID);
		
	}

	public List<Topic> getLinks() {
		
		List<Topic> result = new ArrayList<Topic>();
		
		try {
			result = topicDao.getLinks();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int updateLinkByID(Topic topic) {
		
		int returnCd = -100;
		returnCd = topicDao.updateLinkByID(topic);
		return returnCd;
		
	}
	
	public void updateUser(int uid) {
		topicDao.updateUser(uid);
	}
	
	public User searchUser(int uid) {
		return topicDao.searchUser(uid);
	}
	
	public List<String> getSpeakerName() {
		return topicDao.getSpeakerName();
	}
	
	public void insertCan(String uid, String pnum, String eventId) {
		topicDao.insertCan(uid, pnum, eventId);
	}
	
	public List<User> getCan(String eventId) {
		return topicDao.getCan(eventId);
	}
	
	public void deleteCan(String eventId) {
		topicDao.deleteCan(eventId);
	}
	
	public void updateStatus(String ready, String eventId) {
		topicDao.updateStatus(ready, eventId);
	}
	
	public String checkStatus(String eventId) {
		return topicDao.checkStatus(eventId);
	}
	
	@Override
	public void updateShakeStartTime(String eventId) {
		
		Date currentDate = new Date();
		String dateStr1 = TimerUtil.formateDate(currentDate);
//		if(!topicDao.findShakeStart(location)){
			topicDao.updateShakeStartTime(eventId, dateStr1);
//		}
	}

	@Override
	public void updateShakeEndTime(LotteryReport lottery) {
		
		topicDao.updateShakeEndTime(lottery);
		
	}

	@Override
	public List<LotteryReport> getLotteryByLocation(String location) {
		return topicDao.getLotteryByLocation(location);
	}
}
