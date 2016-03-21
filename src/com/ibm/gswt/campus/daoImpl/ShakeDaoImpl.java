package com.ibm.gswt.campus.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import com.ibm.gswt.campus.dao.ShakeDao;
import com.ibm.gswt.campus.util.Constant;
import com.ibm.gswt.campus.util.DBConUtil;

public class ShakeDaoImpl implements ShakeDao {

	@Override
	public void addStatusRecord(String eventId) {
		String sql = "INSERT INTO " + Constant.schema + ".STATUS (READY, EVENTID) VALUES (?, ?)";
		
		List<String> userParamList = new ArrayList<String>();
		userParamList.add("N");
		userParamList.add(eventId);
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			DBConUtil.executeUpdate(userParamList, conn, pstmt);
			conn.setAutoCommit(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
	}
}
