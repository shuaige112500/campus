package com.ibm.gswt.campus.dao;

import java.util.List;

import com.ibm.gswt.campus.bean.Answer;
import com.ibm.gswt.campus.bean.FeedbackQuestion;
import com.ibm.gswt.campus.bean.Survey;
import com.ibm.gswt.campus.bean.Title;

public interface FeedbackDao {

	public boolean isSubmitted(String tel, String titleId);
	
	public int saveTitle(Survey survey);
	
	public int saveQuestion(FeedbackQuestion question);

	public List<Title> getAllTitles();

	public List<FeedbackQuestion> getQuestionByTitle(String id);
	
	public void saveAnswer(Answer answer);
	
	public Title getTitleById(String id);
	
	public void deleteTitleById(String titleId);
	
	public void deleteQuestionsByTitleId(String titleId);
	
	public void deleteQuestionById(String id);
	
	public List<Answer> getAllAnswers(String titleId);
	
	public void deleteAnswers(String questId);
}
