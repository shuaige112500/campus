package com.ibm.gswt.campus.service;

import java.util.List;

import com.ibm.gswt.campus.bean.Comment;
import com.ibm.gswt.campus.bean.Topic;

public interface CommentService {

	public List<Comment> getCommentsBySpeaker(String speakerId);
	
	public void addComment(Comment comment);
	
	public void saveAdminComment(Comment comment);
	
	public void deleteAdminComments(String eventId);
	
	public List<Comment> getAllComments(String eventId);
	
	public List<Comment> getAdminComments(String eventId);
	
	public void updateComments(int cid, String choosen);
	
	public List<Topic> getTopicAndCommentCount(String eventId);
	
	public List<Comment> getQuestions(String eventId);
	
	public void deleteComment(String cid);
}
