package com.ibm.gswt.campus.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import com.ibm.gswt.campus.bean.Comment;
import com.ibm.gswt.campus.bean.Topic;
import com.ibm.gswt.campus.dao.CommentDao;
import com.ibm.gswt.campus.daoImpl.CommentDaoImpl;
import com.ibm.gswt.campus.service.CommentService;

public class CommentServiceImpl implements CommentService {
	
	private CommentDao commentDao = new CommentDaoImpl();

	@Override
	public List<Comment> getCommentsBySpeaker(String speakerId) {
		List<Comment> result = new ArrayList<Comment>();
		
		try {
			result = commentDao.getCommentsBySpeaker(speakerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void addComment(Comment comment) {
		
		try {
			commentDao.addComment(comment);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void saveAdminComment(Comment comment) {
		
		try {
			commentDao.saveAdminComment(comment);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAdminComments(String eventId) {
		try {
			commentDao.deleteAdminComments(eventId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Comment> getAllComments(String eventId) {
		List<Comment> comments = new ArrayList<Comment>(); 
		try {
			comments = commentDao.getAllComments(eventId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comments;
	}
	
	public List<Comment> getAdminComments(String eventId) {
		
		List<Comment> result = new ArrayList<Comment>();
		
		try {
			result = commentDao.getAdminComments(eventId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void updateComments(int cid, String choosen) {
		
		try {
			commentDao.updateComments(cid, choosen);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Topic> getTopicAndCommentCount(String location) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Comment> getQuestions(String eventId) {
		List<Comment> result = new ArrayList<Comment>();
		
		try {
			result = commentDao.getQuestions(eventId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void deleteComment(String cid) {
		try {
			commentDao.deleteComment(cid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
