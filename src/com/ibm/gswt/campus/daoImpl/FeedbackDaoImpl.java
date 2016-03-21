package com.ibm.gswt.campus.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.ibm.gswt.campus.bean.Answer;
import com.ibm.gswt.campus.bean.FeedbackQuestion;
import com.ibm.gswt.campus.bean.Survey;
import com.ibm.gswt.campus.bean.Title;
import com.ibm.gswt.campus.dao.FeedbackDao;
import com.ibm.gswt.campus.util.Constant;
import com.ibm.gswt.campus.util.DBConUtil;
import com.ibm.gswt.campus.util.TimerUtil;

public class FeedbackDaoImpl implements FeedbackDao {

	@Override
	public boolean isSubmitted(String tel, String titleId) {
		
		String sql = "SELECT A.* FROM " + Constant.schema + ".ANSWER A INNER JOIN " +
				Constant.schema + ".QUESTION Q ON A.QUEST_ID = Q.QUEST_ID AND Q.S_ID = ? AND A.ID_DELETED = ? WHERE A.TEL = ? ";

		List<String> paramList = new ArrayList<String>();
		paramList.add(titleId);
		paramList.add("0");
		paramList.add(tel);
		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			result = DBConUtil.query(paramList, conn, pstmt);

			if (result != null) {
				while (result.next()) {
					return true;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return false;
	}

	@Override
	public int saveTitle(Survey survey) {
		
		int returnNum = 0;
		
		String sql = null;
		if (survey.getsId() == null) {
			sql = "INSERT INTO " + Constant.schema + ".SURVEY (SURVEY_CONTENT, DESCRIPTION, DELETE_FLAG, CREATED_TS, MODIFIED_TS) VALUES (?, ?, ?, ?, ?)";
		} else {
			sql = "UPDATE " + Constant.schema + ".SURVEY SET SURVEY_CONTENT = ?, DESCRIPTION = ?, MODIFIED_TS = ? WHERE S_ID = ?";
		}
		
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			List<String> paramList = new ArrayList<String>();
			
			if (survey.getsId() == null) {
				paramList.add(survey.getContent());
				paramList.add(survey.getDescription());
				paramList.add("0");
				paramList.add(survey.getCreatedTs());
				paramList.add(survey.getModifiedTs());
			} else {
				returnNum = Integer.parseInt(survey.getsId());
				paramList.add(survey.getContent());
				paramList.add(survey.getDescription());
				paramList.add(survey.getModifiedTs());
				paramList.add(survey.getsId());
			}
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(paramList, conn, pstmt);
			
			ResultSet rs = pstmt.getGeneratedKeys();
			
			while (rs.next()) {
				returnNum = rs.getInt(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt);
		}
		return returnNum;
	}

	@Override
	public int saveQuestion(FeedbackQuestion question) {
		
		int id = 0;
		
		String sql = "INSERT INTO " + Constant.schema + ".QUESTION (S_ID, TITLE, TYPE, ORDER, ALTERNATE, DELETE_FLAG) VALUES (?, ?, ?, ?, ?, ?)";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add(String.valueOf(question.getTid()));
			paramList.add(question.getQuestion());
			paramList.add(question.getType());
			paramList.add(question.getOrderId());
			paramList.add(question.getAlternate());
			paramList.add("0");
			
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			conn.setAutoCommit(true);
			DBConUtil.executeUpdate(paramList, conn, pstmt);
			
			ResultSet rs  = pstmt.getGeneratedKeys();
			
			while (rs.next()) {
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
	public List<Title> getAllTitles() {
		List<Title> titleList = new ArrayList<Title>();
		String sql = "SELECT S.* FROM " + Constant.schema + ".SURVEY S WHERE S.DELETE_FLAG = ? ORDER BY S.MODIFIED_TS DESC";

		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		Title title = null;
		
		List<String> paramList = new ArrayList<String>();
		paramList.add("0");
		
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			result = DBConUtil.query(paramList, conn, pstmt);

			if (result != null) {
				while (result.next()) {
					title = new Title();
					title.setId(result.getInt("s_id"));
					title.setTitle(result.getString("survey_content"));
					title.setDescription(result.getString("description"));
					titleList.add(title);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return titleList;
	}
	
	@Override
	public List<FeedbackQuestion> getQuestionByTitle(String id) {
		List<FeedbackQuestion> questions = new ArrayList<FeedbackQuestion>();
		String sql = "SELECT Q.* FROM " + Constant.schema + ".QUESTION Q WHERE Q.S_ID = ? AND Q.DELETE_FLAG = ?";

		List<String> paramList = new ArrayList<String>();
		paramList.add(id);
		paramList.add("0");
		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		FeedbackQuestion question = null;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			result = DBConUtil.query(paramList, conn, pstmt);

			if (result != null) {
				while (result.next()) {
					question = new FeedbackQuestion();
					question.setId(result.getInt("quest_id"));
					question.setTid(result.getInt("s_id"));
					question.setQuestion(result.getString("title"));
					question.setType(result.getString("type"));
					question.setOrderId(result.getString("order"));
					question.setAlternate(result.getString("alternate"));
					
					if (result.getString("alternate") != null) {
						String[] alternate = result.getString("alternate").split("_");
						List<String> options = new ArrayList<String>();
						Collections.addAll(options, alternate);
						question.setOptions(options);
					}
					questions.add(question);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return questions;
	}
	
	@Override
	public void saveAnswer(Answer answer) {
		
		String sql = "INSERT INTO " + Constant.schema + ".ANSWER (QUEST_ID, CONTENT, TEL, CREATETIME, ID_DELETED) VALUES (?, ?, ?, ?, ?)";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add(answer.getQuestionId());
			paramList.add(answer.getAnswer());
			paramList.add(answer.getTel());
			paramList.add(answer.getCreateTime());
			paramList.add("0");
			
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
	public Title getTitleById(String id) {
		String sql = "SELECT S.SURVEY_CONTENT, DESCRIPTION FROM " + Constant.schema + ".SURVEY S WHERE S.S_ID = ? AND S.DELETE_FLAG = ?";

		List<String> paramList = new ArrayList<String>();
		paramList.add(id);
		paramList.add("0");
		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		Title title = new Title();;
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			result = DBConUtil.query(paramList, conn, pstmt);

			if (result != null) {
				while (result.next()) {
					title.setTitle(result.getString("survey_content"));
					title.setDescription(result.getString("description"));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return title;
	}

	@Override
	public void deleteTitleById(String titleId) {
		String sql = "UPDATE " + Constant.schema + ".SURVEY SET DELETE_FLAG = ?, MODIFIED_TS = ? WHERE S_ID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add("1");
			Date date = new Date();
			paramList.add(TimerUtil.formateDate(date));
			paramList.add(titleId);
			
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
	public void deleteQuestionsByTitleId(String titleId) {
		String sql = "UPDATE " + Constant.schema + ".QUESTION SET DELETE_FLAG = '1' WHERE S_ID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add(titleId);
			
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
	public void deleteQuestionById(String id) {
		String sql = "UPDATE " + Constant.schema + ".QUESTION SET DELETE_FLAG = '1' WHERE QUEST_ID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add(id);
			
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
	public List<Answer> getAllAnswers(String titleId) {
		String sql = "SELECT A.CONTENT, A.TEL FROM HT.ANSWER A "
				+ "INNER JOIN HT.QUESTION Q ON A.QUEST_ID = Q.QUEST_ID AND Q.DELETE_FLAG = ? " 
				+ "INNER JOIN HT.SURVEY S ON Q.S_ID = S.S_ID AND S.DELETE_FLAG = ? AND S.S_ID = ? "
				+ "ORDER BY TEL, Q.QUEST_ID";

		List<Answer> answers = new ArrayList<Answer>();
		Answer answer = null;
		
		// Define Connection, statement and resultSet
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		
		List<String> paramList = new ArrayList<String>();
		paramList.add("0");
		paramList.add("0");
		paramList.add(titleId);
		
		try {
			conn = DBConUtil.getConn();
			pstmt = conn.prepareStatement(sql);
			result = DBConUtil.query(paramList, conn, pstmt);

			if (result != null) {
				while (result.next()) {
					answer = new Answer();
					answer.setAnswer(result.getString("content"));
					answer.setTel(result.getString("tel"));
					answers.add(answer);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConUtil.close(conn, pstmt, result);
		}
		return answers;
	}

	@Override
	public void deleteAnswers(String questId) {
		String sql = "UPDATE " + Constant.schema + ".ANSWER SET ID_DELETED = ? WHERE QUEST_ID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			List<String> paramList = new ArrayList<String>();
			paramList.add("1");
			paramList.add(questId);
			
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
