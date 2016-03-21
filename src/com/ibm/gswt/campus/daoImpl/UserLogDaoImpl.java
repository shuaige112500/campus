package com.ibm.gswt.campus.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.bean.UserLog;
import com.ibm.gswt.campus.dao.UserLogDao;
import com.ibm.gswt.campus.util.Constant;
import com.ibm.gswt.campus.util.DBConUtil;
import com.ibm.gswt.campus.util.TimerUtil;

public class UserLogDaoImpl implements UserLogDao  {
	

	public void addUserLog(UserLog userLog) {
		
		Logger logger = Logger.getLogger(UserLogDaoImpl.class);
		
		String SQL_USER_LOG_INSERT = "insert into "
				+ Constant.schema
				+ ".user_log (USERNAME,WECHAT,PNUM,CREATED_TS,CHECKED,LOCATION) values (?,?,?,?,?,?)";
		List<String> userParamList = new ArrayList<String>();
		Date currentDate = new Date();
		String date = TimerUtil.formateDate(currentDate);
		
		userParamList.add(userLog.getUsername());
		userParamList.add(userLog.getWechat());
		userParamList.add(userLog.getPnum());
		userParamList.add(date);
		userParamList.add(userLog.getChecked());
		userParamList.add(userLog.getLocation());

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(SQL_USER_LOG_INSERT);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(userParamList, conn, pstmt);
			logger.info("finish add user in user log");
		} catch (Exception ex) {
			logger.info(ex);
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
	}

	@Override
	public List<User> getRegisterUsers(String eventId) {
		
		List<User> users = new ArrayList<User>();
		User user = null;
		
		String sql = "SELECT MAX(L.CREATED_TS) AS CREATED_TS, U.PNUM, U.LOCATION, U.SCHOOL, U.LUCKY, U.USERNAME, U.EMAIL, U.MAJOR, U.GRADE,"
				+ " CASE WHEN MAX(L.CHECKED) IS NULL OR MAX(L.CHECKED) = '' THEN '0' ELSE MAX(L.CHECKED) END AS CHECKED"
				+ " FROM " + Constant.schema + ".USER_LOG L INNER JOIN " + Constant.schema + ".USER U ON L.PNUM = U.PNUM AND L.LOCATION = U.LOCATION"
				+ " WHERE U.LOCATION = ? GROUP BY U.PNUM, U.LOCATION, U.SCHOOL, U.LUCKY, U.USERNAME, U.EMAIL, U.MAJOR, U.GRADE";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(eventId);

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = DBConUtil.query(userParamList, conn, pstmt);
			while (rs.next()) {
				user = new User();
				user.setCreatedTS(rs.getString("CREATED_TS"));
				user.setPnum(rs.getString("PNUM"));
				user.setEventId(rs.getString("LOCATION"));
				user.setSchool(rs.getString("SCHOOL"));
				user.setLucky(rs.getString("LUCKY"));
				user.setUsername(rs.getString("USERNAME"));
				user.setEmail(rs.getString("EMAIL"));
				user.setMajor(rs.getString("MAJOR"));
				user.setGrade(rs.getString("GRADE"));
				user.setChecked(rs.getString("CHECKED"));
				users.add(user);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
		return users;
	}

	@Override
	public List<User> getCheckedUsers(String eventId) {
		List<User> users = new ArrayList<User>();
		User user = null;
		
		String sql = "SELECT MAX(L.CREATED_TS) AS CREATED_TS, U.PNUM, U.LOCATION, U.SCHOOL, U.LUCKY, U.USERNAME, U.EMAIL, U.MAJOR, U.GRADE,"
				+ " CASE WHEN MAX(L.CHECKED) IS NULL OR MAX(L.CHECKED) = '' THEN '0' ELSE MAX(L.CHECKED) END AS CHECKED"
				+ " FROM " + Constant.schema + ".USER_LOG L INNER JOIN " + Constant.schema + ".USER U ON L.PNUM = U.PNUM AND L.LOCATION = U.LOCATION"
				+ " WHERE U.LOCATION = ? AND L.CHECKED = ? GROUP BY U.PNUM, U.LOCATION, U.SCHOOL, U.LUCKY, U.USERNAME, U.EMAIL, U.MAJOR, U.GRADE";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(eventId);
		userParamList.add("1");

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = DBConUtil.query(userParamList, conn, pstmt);
			while (rs.next()) {
				user = new User();
				user.setCreatedTS(rs.getString("CREATED_TS"));
				user.setPnum(rs.getString("PNUM"));
				user.setEventId(rs.getString("LOCATION"));
				user.setSchool(rs.getString("SCHOOL"));
				user.setLucky(rs.getString("LUCKY"));
				user.setUsername(rs.getString("USERNAME"));
				user.setEmail(rs.getString("EMAIL"));
				user.setMajor(rs.getString("MAJOR"));
				user.setGrade(rs.getString("GRADE"));
				user.setChecked(rs.getString("CHECKED"));
				users.add(user);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
		return users;
	}
}