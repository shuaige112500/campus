package com.ibm.gswt.campus.serviceImpl;

import java.util.List;

import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.dao.UserDao;
import com.ibm.gswt.campus.daoImpl.UserDaoImpl;
import com.ibm.gswt.campus.service.UserService;

public class UserServiceImpl implements UserService {

	private UserDao userDao = new UserDaoImpl();

	@Override
	public void addUser(User user) {
		
		userDao.addUser(user);
	
	}

	@Override
	public User getUser(String pnum, String eventId) {
		
		User user = userDao.getUser(pnum, eventId);
		return user;
		
	}
	
	public void checkIn() {
		userDao.checkIn();
	}

	@Override
	public List<User> getLuckyUsers(String eventId) {
		return userDao.getLuckyUsers(eventId);
	}
	
}
