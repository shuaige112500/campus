package com.ibm.gswt.campus.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ibm.gswt.campus.bean.Event;
import com.ibm.gswt.campus.dao.EventDao;
import com.ibm.gswt.campus.util.Constant;
import com.ibm.gswt.campus.util.DBConUtil;

public class EventDaoImpl implements EventDao {

	@Override
	public int saveEvent(Event event) {
		
		String sql = null;
		
		int id = 0;
		
		if (event.getId() == null) {
			sql = "INSERT INTO " + Constant.schema 
					+ ".EVENTS (COLLEGE, TYPE, DATE, START_TIME, END_TIME, ADDRESS, DESCRIBE, TEMPLATE, SHAKE_FLAG, LOCATION, CREATED_TS, MODIFIED_TS) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		} else {
			sql = "UPDATE " + Constant.schema + ".EVENTS E "
					+ "SET E.COLLEGE = ?, "
					+ "E.TYPE = ?, "
					+ "E.DATE = ?, "
					+ "E.START_TIME = ?, "
					+ "E.END_TIME = ?, "
					+ "E.ADDRESS = ?, "
					+ "E.DESCRIBE = ?, "
					+ "E.TEMPLATE = ?, "
					+ "E.SHAKE_FLAG = ?, "
					+ "E.LOCATION = ?, "
					+ "E.MODIFIED_TS = ? "
					+ "WHERE E.ID = ?";
			id = (Integer.parseInt(event.getId()));
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add(event.getCollege());
			paramList.add(event.getType());
			paramList.add(event.getDate());
			paramList.add(event.getStartTime());
			paramList.add(event.getEndTime());
			paramList.add(event.getAddress());
			paramList.add(event.getDescribe());
			paramList.add(event.getTemplate());
			if (event.getShakeFlag() == true) {
				paramList.add("1");
			} else {
				paramList.add("0");
			}
			paramList.add(event.getLocation());
			
			if (event.getId() == null) {
				paramList.add(event.getCreateTS());
				paramList.add(event.getModifyTS());
			} else {
				paramList.add(event.getModifyTS());
				paramList.add(event.getId());
			}
			
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(paramList, conn, pstmt);

			ResultSet rs = pstmt.getGeneratedKeys();
			
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt);
		}
		return id;
	}

	@Override
	public void deleteEvent(int id) {
		String sqlDelete = "DELETE FROM " + Constant.schema + ".EVENTS E WHERE E.ID = ?";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(String.valueOf(id));
		
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlDelete);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(userParamList, conn, pstmt);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt);
		}
		
	}

	@Override
	public List<Event> getAllEvents() {
		String sqlSearch = "SELECT *"
				+  " FROM " + Constant.schema + ".EVENTS E ORDER BY MODIFIED_TS DESC";

		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		
		List<Event> eventList = new ArrayList<Event>();
		Event event = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			result = DBConUtil.query(conn, pstmt);

			if (result != null) {
				while(result.next()) {
					event = new Event();
					event.setId(result.getString("id"));
					event.setCollege(result.getString("college"));
					event.setType(result.getString("type"));
					event.setDate(result.getString("date"));
					event.setStartTime(result.getString("start_time"));
					event.setEndTime(result.getString("end_time"));
					event.setAddress(result.getString("address"));
					event.setDescribe(result.getString("describe"));
					event.setTemplate(result.getString("template"));
					if ("1".equals(result.getString("shake_flag"))) {
						event.setShakeFlag(true);
					} else {
						event.setShakeFlag(false);
					}
					
					event.setLocation(result.getString("location"));
					event.setEventUrl(result.getString("event_url"));
					event.setQrcode(result.getString("qrcode"));
					eventList.add(event);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return eventList;
	}

	@Override
	public Event getEventById(int id) {
		String sqlSearch = "SELECT *"
				+  " FROM " + Constant.schema + ".EVENTS E WHERE E.ID = ?";

		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		
		List<String> paramList = new ArrayList<String>();
		paramList.add(String.valueOf(id));
		
		Event event = new Event();
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			result = DBConUtil.query(paramList, conn, pstmt);

			if (result != null) {
				while (result.next()) {
					event.setId(result.getString("id"));
					event.setCollege(result.getString("college"));
					event.setType(result.getString("type"));
					event.setDate(result.getString("date"));
					event.setStartTime(result.getString("start_time"));
					event.setEndTime(result.getString("end_time"));
					event.setAddress(result.getString("address"));
					event.setDescribe(result.getString("describe"));
					event.setTemplate(result.getString("template"));
					if ("1".equals(result.getString("shake_flag"))) {
						event.setShakeFlag(true);
					} else {
						event.setShakeFlag(false);
					}
					event.setLocation(result.getString("location"));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return event;
	}
	
	@Override
	public void saveEventUrl(Event event) {
		
		String sql = "UPDATE " + Constant.schema + ".EVENTS E "
				+ "SET E.EVENT_URL = ?, "
				+ "E.QRCODE = ?, "
				+ "E.MODIFIED_TS = ? "
				+ "WHERE E.ID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add(event.getEventUrl());
			paramList.add(event.getQrcode());
			paramList.add(event.getModifyTS());
			paramList.add(event.getId());
			
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(paramList, conn, pstmt);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt);
		}
	}
}
