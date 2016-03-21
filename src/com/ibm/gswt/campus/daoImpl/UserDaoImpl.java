package com.ibm.gswt.campus.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.dao.UserDao;
import com.ibm.gswt.campus.util.Constant;
import com.ibm.gswt.campus.util.DBConUtil;
import com.ibm.gswt.campus.util.TimerUtil;

public class UserDaoImpl implements UserDao {

	public void addUser(User user) {
		
		String SQL_USER_INSERT = "insert into "
				+ Constant.schema
				+ ".user (USERNAME,SCHOOL,PNUM,CREATED_TS,MAJOR,EMAIL,GRADE,LOCATION) values (?,?,?,?,?,?,?,?)";
		List<String> userParamList = new ArrayList<String>();
		Date currentDate = new Date();
		String date = TimerUtil.formateDate(currentDate);
		
		userParamList.add(user.getUsername());
		userParamList.add(user.getSchool());
		userParamList.add(user.getPnum());
//		userParamList.add(user.getWechat());
		userParamList.add(date);
		userParamList.add(user.getMajor());
		userParamList.add(user.getEmail());
		userParamList.add(user.getGrade());
		userParamList.add(user.getEventId());

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(SQL_USER_INSERT);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(userParamList, conn, pstmt);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
	}

	@Override
	public User getUser(String pnum, String eventId) {
		
		User user = null;
		String SQL_USER_CHECK = "SELECT * FROM " + Constant.schema
				+ ".USER WHERE PNUM = ? and LOCATION = ?";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(pnum);
		userParamList.add(eventId);
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(SQL_USER_CHECK);
			result = DBConUtil.query(userParamList, conn, pstmt);
			while (result.next()) {
				user = new User();
				user.setUID(result.getInt("UID"));
				user.setUsername(result.getString("USERNAME"));
				user.setSchool(result.getString("SCHOOL"));
				user.setPnum(result.getString("PNUM"));
//					user.setWechat(result.getString("WECHAT"));
				user.setMajor(result.getString("MAJOR"));
				user.setEmail(result.getString("EMAIL"));
				user.setGrade(result.getString("GRADE"));
				user.setEventId(result.getString("LOCATION"));
				user.setLucky(result.getString("LUCKY"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
		return user;
	}
	
	public void checkIn() {
		String SQL_USER_UPDATE = "update "
				+ Constant.schema
				+ ".user set modified_ts = ?";
		List<String> userParamList = new ArrayList<String>();
		Date currentDate = new Date();
		String date = TimerUtil.formateDate(currentDate);
		
		userParamList.add(date);

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(SQL_USER_UPDATE);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(userParamList, conn, pstmt);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
	}

	public List<User> getRegistUserList(String location, Date startDate, Date endDate) {
		
		List<User> userList = new ArrayList<User>();
//		String sqlSearch = "select * from " + Constant.schema + ".user where location = ? and username != 'Admin'";
		String sqlSearch = "select u.UID, u.USERNAME, u.PNUM, u.SCHOOL, u.LUCKY, u.CREATED_TS, u.MODIFIED_TS, u.MAJOR, u.EMAIL, u.GRADE, max(l.CHECKED) CHECKED from " + Constant.schema + ".USER u left join " + Constant.schema + ".USER_LOG l on u.PNUM=l.PNUM and l.CHECKED='1' and l.CREATED_TS > ? and l.CREATED_TS < ?  where u.LOCATION = ? and u.USERNAME != 'Admin' GROUP BY u.UID, u.USERNAME, u.PNUM, u.SCHOOL, u.LUCKY, u.CREATED_TS, u.MODIFIED_TS, u.MAJOR, u.EMAIL, u.GRADE";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		User user = null;
		try {
			List<String> userParamList = new ArrayList<String>();
			String start = TimerUtil.formateDate(startDate);
			String end = TimerUtil.formateDate(endDate);
			userParamList.add(start);
			userParamList.add(end);
			userParamList.add(location);
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			conn.setAutoCommit(true);
			result = DBConUtil.query(userParamList, conn, pstmt);

			while (result.next()) {
				user = new User();
				user.setUID(result.getInt("UID"));
				user.setUsername(result.getString("USERNAME"));
//					user.setWechat(result.getString("wechat"));
				user.setPnum(result.getString("PNUM"));
				user.setSchool(result.getString("SCHOOL"));
				if(result.getString("LUCKY")==null){
					user.setLucky("");
				}else{
					user.setLucky(result.getString("LUCKY"));
				}
				user.setCreatedTS(result.getString("CREATED_TS"));
				if(result.getString("MODIFIED_TS")==null){
					user.setModifiedTS("");
				}else{
					user.setModifiedTS(result.getString("MODIFIED_TS"));
				}
				user.setMajor(result.getString("MAJOR"));
				user.setEmail(result.getString("EMAIL"));
				user.setGrade(result.getString("GRADE"));
				user.setChecked(result.getString("CHECKED"));
				userList.add(user);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return userList;
	}

	public List<User> getCheckInUserList(String location, Date startDate, Date endDate) {
		
		List<User> userList = new ArrayList<User>();
		String sqlSearch = "select distinct USERNAME, PNUM from " + Constant.schema + ".user_log where checked = '1' and created_ts > ? and created_ts < ? and username!='Admin' and location=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		User user = null;
		try {
			
			List<String> userParamList = new ArrayList<String>();
			String start = TimerUtil.formateDate(startDate);
			String end = TimerUtil.formateDate(endDate);
			userParamList.add(start);
			userParamList.add(end);
			userParamList.add(location);
			
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			conn.setAutoCommit(true);
			result = DBConUtil.query(userParamList,conn, pstmt);

			if (result != null) {
				while (result.next()) {
					user = new User();
					user.setUsername(result.getString("username"));
					user.setPnum(result.getString("pnum"));
					userList.add(user);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return userList;
	}

	@Override
	public List<User> getLuckyUsers(String eventId) {
		
		List<User> users = new ArrayList<User>();
		User user = null;
		String SQL_USER_CHECK = "SELECT * FROM " + Constant.schema
				+ ".USER WHERE LUCKY = ? and LOCATION = ?";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add("1");
		userParamList.add(eventId);
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(SQL_USER_CHECK);
			result = DBConUtil.query(userParamList, conn, pstmt);
			while (result.next()) {
				user = new User();
				user.setUID(result.getInt("UID"));
				user.setUsername(result.getString("USERNAME"));
				user.setSchool(result.getString("SCHOOL"));
				user.setPnum(result.getString("PNUM"));
//					user.setWechat(result.getString("WECHAT"));
				user.setMajor(result.getString("MAJOR"));
				user.setEmail(result.getString("EMAIL"));
				user.setGrade(result.getString("GRADE"));
				user.setEventId(result.getString("LOCATION"));
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
