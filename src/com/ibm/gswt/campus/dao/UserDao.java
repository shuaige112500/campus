package com.ibm.gswt.campus.dao;

import java.util.Date;
import java.util.List;

import com.ibm.gswt.campus.bean.User;

public interface UserDao {

	public void addUser(User user);

	public User getUser(String pnum, String eventId);

	public void checkIn();

	/**
	 * base on location find regist user
	 * 
	 * @param location
	 * @return
	 */
	public List<User> getRegistUserList(String location, Date startDate, Date endDate);

	/**
	 * get sign-in user
	 * 
	 * @param location
	 * @param start
	 * @param end
	 * @return
	 */
	public List<User> getCheckInUserList(String location, Date startDate, Date endDate);
	
	public List<User> getLuckyUsers(String eventId);
}
