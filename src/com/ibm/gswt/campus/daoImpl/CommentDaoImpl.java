package com.ibm.gswt.campus.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ibm.gswt.campus.bean.Comment;
import com.ibm.gswt.campus.bean.CommentList;
import com.ibm.gswt.campus.bean.Topic;
import com.ibm.gswt.campus.dao.CommentDao;
import com.ibm.gswt.campus.util.Constant;
import com.ibm.gswt.campus.util.DBConUtil;

public class CommentDaoImpl implements CommentDao {
	
	// get questions which are selected comments
	public List<Comment> getQuestions(String location) {
		List<Comment> comments = new ArrayList<Comment>();
		String sqlSearch = "SELECT C.CID, C.COMMENTS, C.IS_DELETED, C.UID, C.USERNAME, C.CHOOSEN, C.TOPIC_ID, U.PNUM"
				+ " FROM HT.COMMENTS C INNER JOIN HT.USER U ON C.UID = U.UID"
				+ " INNER JOIN HT.TOPIC T ON T.TID = C.TOPIC_ID AND T.LOCATION = ?"
				+ " WHERE C.IS_DELETED = ? AND C.CHOOSEN = ?" 
				+ " UNION "
				+ " SELECT C.CID, C.COMMENTS, C.IS_DELETED, C.UID, C.USERNAME, C.CHOOSEN, 0, '' "
				+ " FROM HT.ADMINCOMMENTS C WHERE C.EVENT_ID = ? AND C.CHOOSEN = ? ";
		
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(location);
		userParamList.add("0");
		userParamList.add("1");
		userParamList.add(location);
		userParamList.add("1");
		
		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		Comment comment = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			conn.setAutoCommit(true);
			result = DBConUtil.query(userParamList, conn, pstmt);

			if (result != null) {
				while (result.next()) {
					comment = new Comment();
					comment.setComments(result.getString("comments"));
					if (result.getString("username") == null) {
						comment.setUserName("");
					} else {
						comment.setUserName(result.getString("username"));
					}
					comment.setCid(result.getString("cid"));
					comment.setChoosen(result.getString("choosen"));
					comment.setIsDeleted(result.getString("is_deleted"));
					comment.setUID(result.getInt("UID"));
					comment.setTopicId(result.getInt("TOPIC_ID"));
					String pnum = result.getString("pnum");
					if(pnum != null) {
						if (pnum.length() > 7) {
							String maskPnum = pnum.substring(0,3) + "****" + pnum.substring(7);
							comment.setPnum(maskPnum);
						} else {
							// when user is admin
							comment.setPnum(pnum);
						}
					}
					
					comments.add(comment);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		
		return comments;
	}

	// get all comments
	public List<Comment> getAllComments(String location) {
		List<Comment> comments = new ArrayList<Comment>();
		String sqlSearch = "select c.CID, c.COMMENTS, c.IS_DELETED, c.UID, c.USERNAME, c.CHOOSEN, c.CREATED_TS, t.TOPIC_NAME, t.TID, t.SPEAKER_NAME from " + 
				Constant.schema + ".comments c, "+Constant.schema+".topic t" +
				" where c.TOPIC_ID = t.TID and t.LOCATION = ? and c.is_deleted = ? order by speaker_name desc";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(location);
		userParamList.add("0");
		
		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		Comment comment = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			conn.setAutoCommit(true);
			result = DBConUtil.query(userParamList, conn, pstmt);

			if (result != null) {
				while (result.next()) {
					comment = new Comment();
					comment.setTopicId(result.getInt("tid"));
					comment.setComments(result.getString("comments"));
					if (result.getString("username") == null) {
						comment.setUserName("");
					} else {
						comment.setUserName(result.getString("username"));
					}
					comment.setCid(result.getString("cid"));
					comment.setChoosen(result.getString("choosen"));
					comment.setIsDeleted(result.getString("is_deleted"));
					comment.setTopicName(result.getString("topic_name"));
					comment.setSpeakerName(result.getString("speaker_name"));
					comment.setCreatedTS(result.getString("created_ts"));
					
					comments.add(comment);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		
		return comments;
	}

	// get all comments
	public List<Comment> getCommentsBySpeaker(String location, String speaker) {
		List<Comment> comments = new ArrayList<Comment>();
		String sqlSearch = "select c.CID, c.COMMENTS, c.IS_DELETED, c.UID, c.USERNAME, c.CHOOSEN, c.CREATED_TS, t.TOPIC_NAME, t.SPEAKER_NAME from " + 
				Constant.schema + ".comments c, "+Constant.schema+".topic t" +
				" where c.TOPIC_ID = t.TID and t.LOCATION = ? and t.SPEAKER_NAME = ? and c.is_deleted = ? order by speaker_name desc";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(location);
		userParamList.add(speaker);
		userParamList.add("0");
		
		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		Comment comment = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			conn.setAutoCommit(true);
			result = DBConUtil.query(userParamList, conn, pstmt);

			if (result != null) {
				while (result.next()) {
					comment = new Comment();
					comment.setComments(result.getString("comments"));
					if (result.getString("username") == null) {
						comment.setUserName("");
					} else {
						comment.setUserName(result.getString("username"));
					}
					comment.setCid(result.getString("cid"));
					comment.setChoosen(result.getString("choosen"));
					comment.setIsDeleted(result.getString("is_deleted"));
					comment.setTopicName(result.getString("topic_name"));
					comment.setSpeakerName(result.getString("speaker_name"));
					comment.setCreatedTS(result.getString("created_ts"));
					
					comments.add(comment);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		
		return comments;
	}

	// add a comment
	public void addComment(Comment comment) {
		String SQL_USER_INSERT = "insert into "
				+ Constant.schema
				+ ".comments (comments,username,uid,topic_id,is_deleted,choosen,created_ts,modified_ts) values (?,?,?,?,?,?,?,?)";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(comment.getComments());
		userParamList.add(comment.getUserName());
		userParamList.add(String.valueOf(comment.getUID()));
		userParamList.add(String.valueOf(comment.getTopicId()));
		userParamList.add("0");
		userParamList.add(comment.getChoosen());
		userParamList.add(comment.getCreatedTS());
		userParamList.add(comment.getModifiedTS());

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
	
	// add a comment
	public void saveAdminComment(Comment comment) {
		
		String sql = null;
		if (comment.getCid() == null) {
			sql = "INSERT INTO " + Constant.schema + ".ADMINCOMMENTS " +
					"(COMMENTS, USERNAME, UID, EVENT_ID, IS_DELETED, CHOOSEN, CREATED_TS, MODIFIED_TS) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		} else {
			sql = "UPDATE " + Constant.schema + ".ADMINCOMMENTS " 
					+ " SET COMMENTS = ?,"
					+ " USERNAME = ?,"
					+ " UID = ?,"
					+ " EVENT_ID = ?,"
					+ " IS_DELETED = ?,"
					+ " CHOOSEN = ?,"
					+ " MODIFIED_TS = ?"
					+ " WHERE CID = ?";
		}
		
		List<String> userParamList = new ArrayList<String>();
		if (comment.getCid() == null) {
			userParamList.add(comment.getComments());
			userParamList.add(comment.getUserName());
			userParamList.add(String.valueOf(comment.getUID()));
			userParamList.add(String.valueOf(comment.getTopicId()));
			userParamList.add("0");
			userParamList.add(comment.getChoosen());
			userParamList.add(comment.getCreatedTS());
			userParamList.add(comment.getModifiedTS());
		} else {
			userParamList.add(comment.getComments());
			userParamList.add(comment.getUserName());
			userParamList.add(String.valueOf(comment.getUID()));
			userParamList.add(String.valueOf(comment.getTopicId()));
			userParamList.add("0");
			userParamList.add(comment.getChoosen());
			userParamList.add(comment.getModifiedTS());
			userParamList.add(comment.getCid());
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(userParamList, conn, pstmt);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
	}
	
	// get admin comments
	public List<Comment> getAdminComments(String location) {
		List<Comment> comments = new ArrayList<Comment>();
		String sqlSearch = "select c.CID, c.COMMENTS, c.USERNAME AS FROM, c.CHOOSEN from " + 
				Constant.schema + ".admincomments c where event_id = ?";
		
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(location);
		
		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		Comment comment = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			result = DBConUtil.query(userParamList, conn, pstmt);

			if (result != null) {
				while (result.next()) {
					comment = new Comment();
					comment.setComments(result.getString("comments"));
					comment.setUserName(result.getString("from"));
					comment.setCid(result.getString("cid"));
					comment.setChoosen(result.getString("choosen"));
					comments.add(comment);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		
		return comments;
	}
	
	// delete admin comments
	public void deleteAdminComments(String eventId) {
		
		String sqlSearch = "delete from " + Constant.schema + ".admincomments c where event_id = ?";
		
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(eventId);
		
		ResultSet result = null;
		
		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(userParamList, conn, pstmt);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
	}
	
	@Override
	public List<Topic> getTopicAndCommentCount(String location) {

		List<Topic> topicList = new ArrayList<Topic>();
		String sql = "select t.TOPIC_NAME, t.SPEAKER_NAME, t.RATING, count(c.CID) COMMENT_COUNT from "
				+ Constant.schema
				+ ".TOPIC t left join "
				+ Constant.schema
				+ ".COMMENTS c on t.TID=c.TOPIC_ID where t.LOCATION=? and c.is_deleted = ? group by t.TOPIC_NAME, t.SPEAKER_NAME, t.RATING";
		List<String> paramList = new ArrayList<String>();
		paramList.add(location);
		paramList.add("0");
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		Topic topic = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			result = DBConUtil.query(paramList, conn, pstmt);
			if (result != null) {
				while (result.next()) {
					topic = new Topic();
					topic.setTopicName(result.getString("TOPIC_NAME"));
					topic.setSpeakerName(result.getString("SPEAKER_NAME"));
					topic.setRating(result.getInt("RATING"));
					topic.setCommentsCount(result.getString("COMMENT_COUNT"));
					topicList.add(topic);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
		return topicList;
	}

	// update all comments choosen status
	public void updateComments(int cid, String choosen) {
		List<String> userParamList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		String updateSql = "update " + Constant.schema
				+ ".COMMENTS set choosen=? where cid=?";

		try {
			userParamList = new ArrayList<String>();
			userParamList.add(choosen);
			userParamList.add(String.valueOf(cid));
			
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(updateSql);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(userParamList, conn, pstmt);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
	}

	@Override
	public List<Comment> getCommentsBySpeaker(String speakerId) {
		List<Comment> comments = new ArrayList<Comment>();
		String sqlSearch = "SELECT C.CID, C.COMMENTS, C.IS_DELETED, C.UID, C.USERNAME, C.CHOOSEN, C.CREATED_TS, T.TOPIC_NAME, T.SPEAKER_NAME "
				+ "FROM " + Constant.schema + ".COMMENTS C LEFT JOIN " + Constant.schema + ".TOPIC T ON C.TOPIC_ID = T.TID WHERE " +
						"C.TOPIC_ID = ? AND C.IS_DELETED = ? ORDER BY C.CID ASC";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(speakerId);
		userParamList.add("0");
		
		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		Comment comment = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			conn.setAutoCommit(true);
			result = DBConUtil.query(userParamList, conn, pstmt);

			if (result != null) {
				while (result.next()) {
					comment = new Comment();
					comment.setComments(result.getString("comments"));
					if (result.getString("username") == null) {
						comment.setUserName("");
					} else {
						comment.setUserName(result.getString("username"));
					}
					comment.setCid(result.getString("cid"));
					comment.setChoosen(result.getString("choosen"));
					comment.setIsDeleted(result.getString("is_deleted"));
					comment.setTopicName(result.getString("topic_name"));
					comment.setSpeakerName(result.getString("speaker_name"));
					comment.setCreatedTS(result.getString("created_ts"));
					
					comments.add(comment);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		
		return comments;
	}

	@Override
	public void deleteComment(String cid) {
		String sql = "UPDATE " + Constant.schema + ".COMMENTS SET IS_DELETED = ? WHERE CID = ?";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add("1");
		userParamList.add(cid);

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
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
	public List<CommentList> getCommentListByEventId(String eventId) {

		List<CommentList> comments = new ArrayList<CommentList>();
		String sqlSearch = "select * from " + Constant.schema + ".COMMENTLIST where LOCATION=?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		CommentList comment = null;
		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add(eventId);
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			conn.setAutoCommit(true);
			result = DBConUtil.query(paramList, conn, pstmt);

			if (result != null) {
				while (result.next()) {
					comment = new CommentList();
					comment.setComments(result.getString("COMMENTS"));
					comment.setTopicName(result.getString("TOPIC_NAME"));
					comment.setSpeakerName(result.getString("SPEAKER_NAME"));
					comment.setLocation(result.getString("LOCATION"));
					comment.setClocation(result.getString("CLOCATION"));
					comment.setCreateTs(result.getString("CREATED_TS"));
					comment.setUsername(result.getString("USERNAME"));
					comment.setPnum(result.getString("PNUM"));
					comment.setSchool(result.getString("SCHOOL"));
					comment.setEmail(result.getString("EMAIL"));
					comment.setMajor(result.getString("MAJOR"));
					comment.setGrade(result.getString("GRADE"));
					comment.setChoosen(result.getString("CHOOSEN"));
					comments.add(comment);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return comments;
	}
}
