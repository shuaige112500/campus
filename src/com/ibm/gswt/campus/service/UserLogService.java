package com.ibm.gswt.campus.service;

import java.util.List;

import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.bean.UserLog;

public interface UserLogService {

	public void addUserLog(UserLog userLog);
	
	public List<User> getRegisterUsers(String eventId);
	
	public List<User> getCheckedUsers(String eventId);
}
