package com.ibm.gswt.campus.dao;

import java.util.List;

import com.ibm.gswt.campus.bean.Comment;
import com.ibm.gswt.campus.bean.CommentList;
import com.ibm.gswt.campus.bean.Topic;

public interface CommentDao {

	public List<Comment> getAllComments(String eventId);
	
	public List<Comment> getCommentsBySpeaker(String speakerId);
	
	public List<Comment> getAdminComments(String eventId);
	
	public void deleteAdminComments(String eventId);
	
	public void addComment(Comment comment);
	
	public void saveAdminComment(Comment comment);
	
	public void updateComments(int cid, String choosen);
	
	public List<Topic> getTopicAndCommentCount(String eventId);
	
	public List<Comment> getQuestions(String eventId);
	
	public void deleteComment(String cid);
	
	public List<CommentList> getCommentListByEventId(String eventId);
}
