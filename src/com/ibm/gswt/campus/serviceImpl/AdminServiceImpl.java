package com.ibm.gswt.campus.serviceImpl;

import java.util.List;

import com.ibm.gswt.campus.daoImpl.AdminDaoImpl;
import com.ibm.gswt.campus.service.AdminService;

public class AdminServiceImpl implements AdminService {
	
	private AdminDaoImpl admindao = new AdminDaoImpl();

	public List<String> isAdmin(String username, String password) {
		return admindao.isAdmin(username, password);
	}
}
