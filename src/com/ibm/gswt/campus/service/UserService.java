package com.ibm.gswt.campus.service;

import java.util.List;

import com.ibm.gswt.campus.bean.User;

public interface UserService {

	public void addUser(User user);
	
	public User getUser(String pnum, String eventId);
	
	public void checkIn();
	
	public List<User> getLuckyUsers(String eventId);
}
