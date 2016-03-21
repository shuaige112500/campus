package com.ibm.gswt.campus.dao;

import java.util.List;


public interface AdminDao {

	public List<String> isAdmin(String username, String password);
}
