package com.ibm.gswt.campus.serviceImpl;

import java.util.List;

import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.bean.UserLog;
import com.ibm.gswt.campus.dao.UserLogDao;
import com.ibm.gswt.campus.daoImpl.UserLogDaoImpl;
import com.ibm.gswt.campus.service.UserLogService;

public class UserLogServiceImpl implements UserLogService {

	private UserLogDao userDao = new UserLogDaoImpl();
	
	@Override
	public void addUserLog(UserLog userLog) {
		userDao.addUserLog(userLog);
	}

	@Override
	public List<User> getRegisterUsers(String eventId) {
		return userDao.getRegisterUsers(eventId);
	}

	@Override
	public List<User> getCheckedUsers(String eventId) {
		return userDao.getCheckedUsers(eventId);
	}

}
