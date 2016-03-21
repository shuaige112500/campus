package com.ibm.gswt.campus.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.gswt.campus.bean.LotteryReport;
import com.ibm.gswt.campus.bean.Topic;
import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.dao.TopicDao;
import com.ibm.gswt.campus.util.Constant;
import com.ibm.gswt.campus.util.DBConUtil;
import com.ibm.gswt.campus.util.TimerUtil;

public class TopicDaoImpl implements TopicDao {
	
	private static Logger logger = Logger.getLogger(TopicDaoImpl.class);
	
	// get topic list
	public List<Topic> getTopicList(String pnum, String location) {
		List<Topic> topics = new ArrayList<Topic>();
		String sql = "with T1 AS("
				+ " select tid,topic_name topicName,topic_desc topicDesc,start_time startTime,end_time endTime,speaker_name speakerName,speaker_des speakerDes,attachment attachment,rating rating,meeting_day meetingDay,meeting_room meetingRoom,speaker_img speakerIMG,is_rated isRated,comment_num commentNUM,created_ts, location from "
				+ Constant.schema
				+ ".agenda where pnum = ?"
				+ "),"
				+ "T2 as ( "
				+ "select tid,topic_name topicName,topic_desc topicDesc,start_time startTime,end_time endTime,speaker_name speakerName,speaker_des speakerDes,attachment attachment,rating rating,meeting_day meetingDay,meeting_room meetingRoom,speaker_img speakerIMG,is_rated isRated,comment_num commentNUM,created_ts, location from "
				+ Constant.schema
				+ ".agenda where pnum = ?"
				+ "),"
				+ "T3 as ("
				+ "select * from T1 "
				+ " union "
				+ "select * from T2"
				+ ")"
				+ "select * from T3 "
				+ " union "
				+ "select distinct tid,topic_name topicName,topic_desc topicDesc,start_time startTime,end_time endTime,speaker_name speakerName,speaker_des speakerDes,attachment attachment,rating rating,meeting_day meetingDay,meeting_room meetingRoom,speaker_img speakerIMG,0 isRated,comment_num commentNUM,created_ts, location from "
				+ Constant.schema
				+ ".agenda where tid not in(select TID from T3)";
		
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(pnum);
		userParamList.add(pnum);
		String maskPnum = pnum.substring(0,3) + "****" + pnum.substring(7);
		
		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		Topic topic = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			conn.setAutoCommit(true);
			result = DBConUtil.query(userParamList, conn, pstmt);

			while (result.next()) {
				if(location.equals(result.getString("location"))) {
					topic = new Topic();
					topic.setRating(result.getInt("rating"));
					topic.setTopicID(result.getInt("tid"));
					topic.setTopicName(result.getString("topicName"));
					topic.setAttachment(result.getString("attachment"));
					topic.setStartTime(result.getString("startTime"));
					topic.setEndTime(result.getString("endTime"));
					topic.setSpeakerName(result.getString("speakerName"));
					topic.setSpeakerDesc(result.getString("speakerDes"));
					topic.setTopicDesc(result.getString("topicDesc"));
					topic.setMeetingDay(result.getString("meetingDay"));
					topic.setMeetingRoom(result.getString("meetingRoom"));
					topic.setCommentsCount(result.getString("commentNUM"));
					topic.setSpeakerimg(result.getString("speakerIMG"));
					topic.setIsRating(String.valueOf(result.getInt("isRated")));
					topic.setUserName(maskPnum);
					topic.setLocation(result.getString("location"));
					topics.add(topic);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return topics;
	}
	
	public List<Topic> getTopics(String intranetID, String userName,
			String userImg) {
		List<Topic> topics = new ArrayList<Topic>();
		String sql = "with T1 AS("
				+ " select tid,topic_name topicName,topic_desc topicDesc,start_time startTime,end_time endTime,speaker_name speakerName,speaker_des speakerDes,attachment attachment,rating rating,meeting_day meetingDay,meeting_room meetingRoom,speaker_img speakerIMG,is_rated isRated,comment_num commentNUM,created_ts from "
				+ Constant.schema
				+ ".agenda where intranet_id = ?"
				+ "),"
				+ "T2 as ( "
				+ "select tid,topic_name topicName,topic_desc topicDesc,start_time startTime,end_time endTime,speaker_name speakerName,speaker_des speakerDes,attachment attachment,rating rating,meeting_day meetingDay,meeting_room meetingRoom,speaker_img speakerIMG,is_rated isRated,comment_num commentNUM,created_ts from "
				+ Constant.schema
				+ ".agenda where intranet_id = ?"
				+ "),"
				+ "T3 as ("
				+ "select * from T1 "
				+ " union "
				+ "select * from T2"
				+ ")"
				+ "select * from T3 "
				+ " union "
				+ "select distinct tid,topic_name topicName,topic_desc topicDesc,start_time startTime,end_time endTime,speaker_name speakerName,speaker_des speakerDes,attachment attachment,rating rating,meeting_day meetingDay,meeting_room meetingRoom,speaker_img speakerIMG,0 isRated,comment_num commentNUM,created_ts from "
				+ Constant.schema
				+ ".agenda where tid not in(select TID from T3)";
		List<String> userParamList = new ArrayList<String>();

		userParamList.add("");
		userParamList.add("");
		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		Topic topic = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			conn.setAutoCommit(true);
			result = DBConUtil.query(userParamList, conn, pstmt);

			while (result.next()) {
				topic = new Topic();
				topic.setRating(result.getInt("rating"));
				topic.setTopicID(result.getInt("tid"));
				topic.setTopicName(result.getString("topicName"));
				topic.setAttachment(result.getString("attachment"));
				topic.setStartTime(result.getString("startTime"));
				topic.setEndTime(result.getString("endTime"));
				topic.setSpeakerName(result.getString("speakerName"));
				topic.setSpeakerDesc(result.getString("speakerDes"));
				topic.setTopicDesc(result.getString("topicDesc"));
				topic.setMeetingDay(result.getString("meetingDay"));
				topic.setMeetingRoom(result.getString("meetingRoom"));
				topic.setCommentsCount(result.getString("commentNUM"));
				topic.setSpeakerimg(result.getString("speakerIMG"));
				topic.setIsRating(String.valueOf(result.getInt("isRated")));
				topic.setUserName(userName);
				topic.setImage(userImg);
				topics.add(topic);
				logger.info("int : " +String.valueOf(result.getInt("isRated")));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return topics;
	}

	

	public Topic getTopicByID(int topicID) {
		Topic currentTopic = new Topic();
		String sqlSearch = "select tid,topic_name,topic_desc,start_time,end_time,speaker_name,speaker_des,attachment,rating,meeting_day,meeting_room,speaker_img from "
				+ Constant.schema + ".topic where tid=? ";
		List<String> userParamList = new ArrayList<String>();
		userParamList.add(String.valueOf(topicID));
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
					currentTopic.setRating(result.getInt("rating"));
					currentTopic.setTopicID(result.getInt("tid"));
					currentTopic.setTopicName(result.getString("topic_name"));
					currentTopic.setAttachment(result.getString("attachment"));
					currentTopic.setStartTime(result.getString("start_time"));
					currentTopic.setEndTime(result.getString("end_time"));
					currentTopic.setSpeakerName(result
							.getString("speaker_name"));
					currentTopic
							.setSpeakerDesc(result.getString("speaker_des"));
					currentTopic.setTopicDesc(result.getString("topic_desc"));
					currentTopic.setMeetingDay(result.getString("meeting_day"));
					currentTopic.setMeetingRoom(result
							.getString("meeting_room"));
					currentTopic.setSpeakerimg(result.getString("speaker_img"));
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return currentTopic;
	}

	public int updateTopic(Topic topic) {
		String updateSql = "update "
				+ Constant.schema
				+ ".topic  set topic_name = ?, topic_desc = ?, start_time = ?, end_time = ?, "
				+ "speaker_name = ?, speaker_des = ?, meeting_room = ?, attachment = ?, speaker_img = ? where tid=?";
		List<String> paramList = new ArrayList<String>();

		// Put user_Id;
		paramList.add(topic.getTopicName());
		paramList.add(topic.getTopicDesc());
		paramList.add(topic.getStartTime());
		paramList.add(topic.getEndTime());
		paramList.add(topic.getSpeakerName());
		paramList.add(topic.getSpeakerDesc());
		paramList.add(topic.getMeetingRoom());
		paramList.add(topic.getAttachment());
		paramList.add(topic.getSpeakerimg());
		paramList.add(String.valueOf(topic.getTopicID()));

		int returnCd = -100;
		// Define Connection and statement
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(updateSql);
			conn.setAutoCommit(true);
			returnCd = DBConUtil.executeUpdate(paramList, conn, pstmt);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
		return returnCd;
	}

	// add topic to DB
	public void addTopic(Topic topic) {
		String sql = "insert into "
				+ Constant.schema
				+ ".topic (topic_name, start_time, end_time, speaker_name, speaker_des, attachment, speaker_img, rating) values (?,?,?,?,?,?,?,?)";
		List<String> paramList = new ArrayList<String>();
		paramList.add(topic.getTopicName());
		paramList.add(topic.getStartTime());
		paramList.add(topic.getEndTime());
		paramList.add(topic.getSpeakerName());
		paramList.add(topic.getSpeakerDesc());
		paramList.add(topic.getAttachment());
		paramList.add(topic.getSpeakerimg());
		paramList.add(String.valueOf(0));

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(paramList, conn, pstmt);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
	}

	// delete a topic
	public void deleteTopic(int topicID) {
		String delete = "delete from " + Constant.schema + ".topic where tid=?";
		List<String> userParamList = new ArrayList<String>();
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

	// get url and link text
	public List<Topic> getLinks() {
		List<Topic> topics = new ArrayList<Topic>();
		Topic tp = null;
		String sqlSearch = "select lid,label,url from " + Constant.schema
				+ ".link";

		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			conn.setAutoCommit(true);
			result = DBConUtil.query(conn, pstmt);

			if (result != null) {
				while (result.next()) {
					tp = new Topic();
					tp.setTopicID(result.getInt("lid"));
					tp.setLabel(result.getString("label"));
					tp.setUrl(result.getString("url"));
					topics.add(tp);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return topics;
	}

	// update url and link text
	public int updateLinkByID(Topic topic) {
		String updateSql = "update " + Constant.schema
				+ ".link  set url = ?, label = ? where lid=?";
		List<String> paramList = new ArrayList<String>();

		// Put user_Id;
		paramList.add(topic.getUrl());
		paramList.add(topic.getLabel());
		paramList.add(String.valueOf(topic.getTopicID()));

		int returnCd = -100;
		// Define Connection and statement
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(updateSql);
			conn.setAutoCommit(true);
			returnCd = DBConUtil.executeUpdate(paramList, conn, pstmt);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
		return returnCd;
	}

	// update User lucky status
	public void updateUser(int uid) {
		List<String> userParamList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		String updateSql = "update " + Constant.schema
				+ ".USER set lucky=? where uid=?";

		try {
			userParamList = new ArrayList<String>();
			userParamList.add("1");
			userParamList.add(""+uid);
			
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
	
	// check if already in bonus list
	public User searchUser(int uid) {
		List<String> userParamList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		String sqlSearch = "select * from " + Constant.schema
				+ ".USER where uid=?";
		User user = new User();

		try {
			userParamList = new ArrayList<String>();
			userParamList.add("" + uid);
			
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			conn.setAutoCommit(true);
			result = DBConUtil.query(userParamList, conn, pstmt);
			if (result != null) {
				while (result.next()) {
					user.setUsername(result.getString("USERNAME"));
					user.setUID(result.getInt("UID"));
					user.setPnum(result.getString("PNUM"));
					user.setSchool(result.getString("SCHOOL"));
					user.setEmail(result.getString("EMAIL"));
					user.setMajor(result.getString("MAJOR"));
					user.setGrade(result.getString("GRADE"));
					user.setLucky(result.getString("lucky"));
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		
		return user;
	}
	
	public List<String> getSpeakerName() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		String sqlSearch = "select speaker_name from " + Constant.schema
				+ ".TOPIC";
		List<String> spkerNameList = new ArrayList<String>();

		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			conn.setAutoCommit(true);
			result = DBConUtil.query(conn, pstmt);
			if (result != null) {
				while (result.next()) {
					spkerNameList.add(result.getString("speaker_name"));
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		
		return spkerNameList;
	}

	public void insertCan(String uid, String pnum, String eventId) {
		String sql = "insert into " + Constant.schema
				+ ".CANDIDATE (user_id,pnum, eventId) values (?,?,?)";
		List<String> paramList = new ArrayList<String>();
		paramList.add(uid);
		paramList.add(pnum);
		paramList.add(eventId);

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(paramList, conn, pstmt);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
	}

	public List<User> getCan(String eventId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		String sqlSearch = "select distinct user_id, pnum from " + Constant.schema + ".CANDIDATE where eventId = ?";
		List<User> pnumList = new ArrayList<User>();
		
		List<String> paramList = new ArrayList<String>();
		paramList.add(eventId);
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			result = DBConUtil.query(paramList, conn, pstmt);
			User user = null;
			if (result != null) {
				while (result.next()) {
					user = new User();
					user.setUID(result.getInt("USER_ID"));
					user.setPnum(result.getString("PNUM"));
					pnumList.add(user);
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		
		return pnumList;
	}
	
	public void deleteCan(String eventId) {
		String delete = "delete from " + Constant.schema + ".CANDIDATE where eventId = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;

		List<String> paramList = new ArrayList<String>();
		paramList.add(eventId);
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(delete);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(paramList, conn, pstmt);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
	}
	
	// switch shake
	public void updateStatus(String ready, String eventId) {
		List<String> userParamList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		String updateSql = "update " + Constant.schema
				+ ".STATUS set ready=? where eventId=?";

		try {
			userParamList = new ArrayList<String>();
			userParamList.add(ready);
			userParamList.add(eventId);
			
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
	
	public String checkStatus(String eventId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		String sqlSearch = "select ready from " + Constant.schema + ".STATUS where eventId = ?";
		String status = "N";

		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add(eventId);
			
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sqlSearch);
			result = DBConUtil.query(paramList, conn, pstmt);
			if (result != null) {
				while (result.next()) {
					status = result.getString("ready");
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		
		return status;
	}
	
	@Override
	public List<LotteryReport> getLotteryByLocation(String location) {

		List<LotteryReport> lotteryList = new ArrayList<LotteryReport>();
		String sql = "select e.COLLEGE as LOCATION, TO_CHAR(l.START_TS, 'YYYY-MM-DD HH24:MM:SS') AS START_TS, TO_CHAR(l.END_TS, 'YYYY-MM-DD HH24:MM:SS') AS END_TS," +
				" l.NUMBER, l.UID, l.USERNAME, l.PNUM, l.SCHOOL, l.EMAIL, l.MAJOR, l.GRADE from " + Constant.schema + ".LOTTERYREPORT l left join "
				+ Constant.schema + ".EVENTS e on l.LOCATION=e.ID  where l.LOCATION=?";
		List<String> paramList = new ArrayList<String>();
		paramList.add(location);
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		LotteryReport lottery = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			result = DBConUtil.query(paramList, conn, pstmt);
			if (result != null) {
				while (result.next()) {
					lottery = new LotteryReport();
					lottery.setLocation(result.getString("LOCATION"));
					lottery.setStartTs(result.getString("START_TS"));
					lottery.setEndTs(result.getString("END_TS"));
					lottery.setNumber(result.getString("NUMBER"));
					lottery.setUid(result.getString("UID"));
					lottery.setUsername(result.getString("USERNAME"));
					lottery.setPnum(result.getString("PNUM"));
					lottery.setSchool(result.getString("SCHOOL"));
					lottery.setEmail(result.getString("EMAIL"));
					lottery.setMajor(result.getString("MAJOR"));
					lottery.setGrade(result.getString("GRADE"));
					lotteryList.add(lottery);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
		return lotteryList;
	}

	@Override
	public void updateShakeStartTime(String eventId, String date) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		String sql = "insert into " + Constant.schema + ".LOTTERYREPORT (START_TS, LOCATION) values (?,?) ";

		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add(date);
			paramList.add(eventId);
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(paramList, conn, pstmt);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
	}

	@Override
	public void updateShakeEndTime(LotteryReport lottery) {
		
		String sql = "update " + Constant.schema + ".LOTTERYREPORT set LOCATION=?, END_TS=?,NUMBER=?,UID=?,USERNAME=?,PNUM=?,SCHOOL=?,EMAIL=?,MAJOR=?,GRADE=? where END_TS is null ";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		
		try {
			List<String> paramList = new ArrayList<String>();
			Date currentDate = new Date();
			String date = TimerUtil.formateDate(currentDate);
			paramList.add(lottery.getLocation());
			paramList.add(date);
			paramList.add(lottery.getNumber());
			paramList.add(lottery.getUid());
			paramList.add(lottery.getUsername());
			paramList.add(lottery.getPnum());
			paramList.add(lottery.getSchool());
			paramList.add(lottery.getEmail());
			paramList.add(lottery.getMajor());
			paramList.add(lottery.getGrade());
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(paramList, conn, pstmt);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
	}

	@Override
	public boolean findShakeStart(String location) {
		
		String sql = "select * from " + Constant.schema + ".LOTTERYREPORT where END_TS is null and LOCATION=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		List<String> paramList = new ArrayList<String>();
		paramList.add(location);
		
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			result = DBConUtil.query(paramList, conn, pstmt);
			if (result != null) {
				if(result.next()){
					return true;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// close connections.
			DBConUtil.close(conn, pstmt);
		}
		
		return false;
	}

	@Override
	public List<Topic> getTopicAndCommentCount(String eventId) {
		List<Topic> topicList = new ArrayList<Topic>();
		String sql = "SELECT DISTINCT A.TOPIC_NAME, A.SPEAKER_NAME, A.RATING, A.COMMENT_NUM FROM " + Constant.schema + ".AGENDA A WHERE A.LOCATION = ?";
		List<String> paramList = new ArrayList<String>();
		paramList.add(eventId);
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			result = DBConUtil.query(paramList, conn, pstmt);
			if (result != null) {
				while (result.next()) {
					Topic topic = new Topic();
					topic .setTopicName(result.getString("TOPIC_NAME"));
					topic.setSpeakerName(result.getString("SPEAKER_NAME"));
					topic.setRating(result.getInt("RATING"));
					topic.setCommentsCount(result.getString("COMMENT_NUM"));
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
}
