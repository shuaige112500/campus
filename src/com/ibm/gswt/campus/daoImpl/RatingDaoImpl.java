package com.ibm.gswt.campus.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ibm.gswt.campus.dao.RatingDao;
import com.ibm.gswt.campus.util.Constant;
import com.ibm.gswt.campus.util.DBConUtil;

public class RatingDaoImpl implements RatingDao {
	
	public String getIsRated(int uid, int tid) {
		String rate = "N";
		String sqlSearch = "select rating from " + Constant.schema
				+ ".rating where topic_id=? and uid=? ";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(String.valueOf(tid));
		userParamList.add(String.valueOf(uid));
		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			conn.setAutoCommit(true);
			result = DBConUtil.query(userParamList, conn, pstmt);

			if (result != null) {
				while (result.next()) {
					rate = result.getString("rating");
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return rate;
	}

	public void insertRatingRelated(int topicID, int uid) {
		String insert = "insert into " + Constant.schema
				+ ".rating (uid,topic_id,rating) values (?,?,?)";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(String.valueOf(uid));
		userParamList.add(String.valueOf(topicID));
		userParamList.add("1");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(insert);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(userParamList, conn, pstmt);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
	}

	public void deleteRatingRelated(int topicID, int uid) {
		String delete = "delete from " + Constant.schema
				+ ".rating where uid=? and topic_id=?";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(String.valueOf(uid));
		userParamList.add(String.valueOf(topicID));
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(delete);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(userParamList, conn, pstmt);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
	}

	public void handleRating(int topicID, String checked) {

		String updateSql1 = "update " + Constant.schema
				+ ".topic set rating=rating+1 where tid=?";
		String updateSql2 = "update " + Constant.schema
				+ ".topic set rating=rating-1 where tid=?";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(String.valueOf(topicID));
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		try {
			conn = DBConUtil.getConn();
			if (checked.equals("Y")) {
				pstmt = conn.prepareStatement(updateSql1);
			}
			if (checked.equals("N")) {
				pstmt = conn.prepareStatement(updateSql2);
			}
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(userParamList, conn, pstmt);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
	}
}
