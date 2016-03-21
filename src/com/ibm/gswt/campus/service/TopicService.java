package com.ibm.gswt.campus.service;

import java.util.List;

import com.ibm.gswt.campus.bean.LotteryReport;
import com.ibm.gswt.campus.bean.Topic;
import com.ibm.gswt.campus.bean.User;

public interface TopicService {

	public List<Topic> getTopicList(String pnum, String location);
	
	public List<Topic> getTopics(String intranetID, String userName, String userImg);

	public Topic getTopicByID(int topicID);

	public int updateTopic(Topic topic);

	public void addTopic(Topic topic);

	public void deleteTopic(int topicID);

	public List<Topic> getLinks();

	public int updateLinkByID(Topic topic);
	
	public void updateUser(int uid);
	
	public User searchUser(int uid);
	
	public List<String> getSpeakerName();
	
	public void insertCan(String uid, String pnum, String eventId);
	
	public List<User> getCan(String eventId);
	
	public void deleteCan(String eventId);
	
	public void updateStatus(String ready, String eventId);
	
	public String checkStatus(String eventId);
	
	public void updateShakeStartTime(String eventId);
	
	public void updateShakeEndTime(LotteryReport lottery);
	
	public List<LotteryReport> getLotteryByLocation(String location);
}
