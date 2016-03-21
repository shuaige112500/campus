package com.ibm.gswt.campus.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ibm.gswt.campus.dao.AdminDao;
import com.ibm.gswt.campus.util.Constant;
import com.ibm.gswt.campus.util.DBConUtil;

public class AdminDaoImpl implements AdminDao {

	public List<String> isAdmin(String username, String password) {
		
		List<String> levelList = new ArrayList<String>();
		
		String sqlSearch = "SELECT P.LEVEL FROM " + Constant.schema + ".ADMIN A " +
				"LEFT JOIN " + Constant.schema + ".PRIVILEGE P ON A.ID = P.ADMIN_ID WHERE A.USER=? AND A.PASSWORD = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add(username);
			paramList.add(password);
			
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			result = DBConUtil.query(paramList, conn, pstmt);
			
			while (result.next()) {
				if (result.getString("level") != null) 
				levelList.add(result.getString("level"));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return levelList;
	}
}
