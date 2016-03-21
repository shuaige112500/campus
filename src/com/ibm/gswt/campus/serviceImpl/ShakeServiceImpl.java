package com.ibm.gswt.campus.serviceImpl;

import com.ibm.gswt.campus.dao.ShakeDao;
import com.ibm.gswt.campus.daoImpl.ShakeDaoImpl;
import com.ibm.gswt.campus.service.ShakeService;

public class ShakeServiceImpl implements ShakeService {
	
	private ShakeDao shakeDao = new ShakeDaoImpl();

	@Override
	public void addStatusRecord(String eventId) {
		shakeDao.addStatusRecord(eventId);
	}
}
