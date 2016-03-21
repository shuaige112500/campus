package com.ibm.gswt.campus.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ibm.gswt.campus.bean.Speaker;
import com.ibm.gswt.campus.dao.SpeakerDao;
import com.ibm.gswt.campus.util.Constant;
import com.ibm.gswt.campus.util.DBConUtil;

public class SpeakerDaoImpl implements SpeakerDao {

	@Override
	public int saveSpeaker(Speaker speaker) {
		
		String sql = null;
		
		int id = 0;
		
		if (speaker.getId() == null) {
			sql = "INSERT INTO " + Constant.schema 
				+ ".TOPIC (LOCATION, SPEAKER_NAME, SPEAKER_DES, SPEAKER_IMG, ATTACHMENT, TOPIC_NAME, TOPIC_DESC, START_TIME, END_TIME, RATING, CREATED_TS, MODIFIED_TS) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		} else {
			sql = "UPDATE " + Constant.schema + ".TOPIC T SET "
				+ " T.SPEAKER_NAME = ?,"
				+ " T.SPEAKER_DES = ?,"
				+ " T.SPEAKER_IMG = ?,"
				+ " T.ATTACHMENT = ?,"
				+ " T.TOPIC_NAME = ?,"
				+ " T.TOPIC_DESC = ?,"
				+ " T.START_TIME = ?,"
				+ " T.END_TIME = ?,"
				+ " T.MODIFIED_TS = ?"
				+ " WHERE T.TID = ?";
			id = Integer.valueOf(speaker.getId());
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			List<String> paramList = new ArrayList<String>();
			if (speaker.getId() == null) {
				paramList.add(speaker.getEventId());
			}
			paramList.add(speaker.getSpeakerName());
			paramList.add(speaker.getSpeakerDesc());
			paramList.add(speaker.getSpeakerImg());
			paramList.add(speaker.getSpeakerFile());
			paramList.add(speaker.getTopicName());
			paramList.add(speaker.getTopicDesc());
			paramList.add(speaker.getStartTime());
			paramList.add(speaker.getEndTime());
			if (speaker.getId() == null) {
				// rating
				paramList.add("0");
			}
			if (speaker.getId() == null) {
				paramList.add(speaker.getCreateTS());
				paramList.add(speaker.getModifyTS());
			} else {
				paramList.add(speaker.getModifyTS());
				paramList.add(speaker.getId());
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
	public void deleteSpeaker(String eid) {
		String sql = "DELETE FROM " + Constant.schema + ".TOPIC T WHERE T.LOCATION = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add(eid);
			
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

	@Override
	public List<Speaker> getSpeakerByEid(String eid) {
		List<Speaker> speakerList = new ArrayList<Speaker>();
		String sql = "SELECT T.* FROM " + Constant.schema + ".TOPIC T WHERE T.LOCATION = ? ORDER BY T.TID ASC";

		List<String> paramList = new ArrayList<String>();
		paramList.add(eid);
		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		Speaker speaker = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			result = DBConUtil.query(paramList, conn, pstmt);

			if (result != null) {
				while (result.next()) {
					speaker = new Speaker();
					speaker.setId(result.getString("tid"));
					speaker.setEventId(result.getString("location"));
					speaker.setSpeakerName(result.getString("speaker_name"));
					speaker.setSpeakerDesc(result.getString("speaker_des"));
					speaker.setSpeakerImg(result.getString("speaker_img"));
					speaker.setSpeakerFile(result.getString("attachment"));
					speaker.setTopicName(result.getString("topic_name"));
					speaker.setTopicDesc(result.getString("topic_desc"));
					speaker.setStartTime(result.getString("start_time"));
					speaker.setEndTime(result.getString("end_time"));
					speaker.setRating(result.getInt("rating"));
					speakerList.add(speaker);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return speakerList;
	}

	@Override
	public void deleteSpeakerById(String speakerId) {
		String sql = "DELETE FROM " + Constant.schema + ".TOPIC T WHERE T.TID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add(speakerId);
			
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
	
	@Override
	public Speaker getSpeaker(String speakerId, String userId) {
		String sql = "SELECT T.*, COALESCE(R.RATING, 0) AS IS_RATING FROM " + Constant.schema + ".TOPIC T LEFT JOIN " + Constant.schema + ".RATING R ON T.TID = R.TOPIC_ID AND R.UID = ? WHERE T.TID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		Speaker speaker = new Speaker();
		
		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add(userId);
			paramList.add(speakerId);
			
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			ResultSet result = DBConUtil.query(paramList, conn, pstmt);
			
			if (result != null) {
				while (result.next()) {
					speaker = new Speaker();
					speaker.setId(result.getString("tid"));
					speaker.setEventId(result.getString("location"));
					speaker.setSpeakerName(result.getString("speaker_name"));
					speaker.setSpeakerDesc(result.getString("speaker_des"));
					speaker.setSpeakerImg(result.getString("speaker_img"));
					speaker.setSpeakerFile(result.getString("attachment"));
					speaker.setTopicName(result.getString("topic_name"));
					speaker.setTopicDesc(result.getString("topic_desc"));
					speaker.setStartTime(result.getString("start_time"));
					speaker.setEndTime(result.getString("end_time"));
					speaker.setRating(result.getInt("rating"));
					speaker.setIsRating(String.valueOf(result.getInt("IS_RATING")));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt);
		}
		return speaker;
	}

	@Override
	public Speaker getSpeakerById(String speakerId) {
		String sql = "SELECT T.* FROM " + Constant.schema + ".TOPIC T WHERE T.TID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		Speaker speaker = new Speaker();
		
		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add(speakerId);
			
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			ResultSet result = DBConUtil.query(paramList, conn, pstmt);
			
			if (result != null) {
				while (result.next()) {
					speaker = new Speaker();
					speaker.setId(result.getString("tid"));
					speaker.setEventId(result.getString("location"));
					speaker.setSpeakerName(result.getString("speaker_name"));
					speaker.setSpeakerDesc(result.getString("speaker_des"));
					speaker.setSpeakerImg(result.getString("speaker_img"));
					speaker.setSpeakerFile(result.getString("attachment"));
					speaker.setTopicName(result.getString("topic_name"));
					speaker.setTopicDesc(result.getString("topic_desc"));
					speaker.setStartTime(result.getString("start_time"));
					speaker.setEndTime(result.getString("end_time"));
					speaker.setRating(result.getInt("rating"));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt);
		}
		return speaker;
	}
}
