package com.ibm.gswt.campus.serviceImpl;

import java.util.List;

import com.ibm.gswt.campus.bean.Answer;
import com.ibm.gswt.campus.bean.FeedbackQuestion;
import com.ibm.gswt.campus.bean.Survey;
import com.ibm.gswt.campus.bean.Title;
import com.ibm.gswt.campus.dao.FeedbackDao;
import com.ibm.gswt.campus.daoImpl.FeedbackDaoImpl;
import com.ibm.gswt.campus.service.FeedbackService;

public class FeedbackServiceImpl implements FeedbackService {

	FeedbackDao feedbackDao = new FeedbackDaoImpl();
	
	@Override
	public boolean isSubmitted(String tel, String titleId) {
		return feedbackDao.isSubmitted(tel, titleId);
	}

	@Override
	public int saveTitle(Survey survey) {
		return feedbackDao.saveTitle(survey);
	}

	@Override
	public int saveQuestion(FeedbackQuestion question) {
		return feedbackDao.saveQuestion(question);
	}

	@Override
	public List<Title> getAllTitles() {
		return feedbackDao.getAllTitles();
	}

	@Override
	public List<FeedbackQuestion> getQuestionByTitle(String id) {
		return feedbackDao.getQuestionByTitle(id);
	}
	
	@Override
	public void saveAnswer(Answer answer) {
		feedbackDao.saveAnswer(answer);
	}

	@Override
	public Title getTitleById(String id) {
		return feedbackDao.getTitleById(id);
	}

	@Override
	public void deleteTitleById(String titleId) {
		feedbackDao.deleteTitleById(titleId);
	}

	@Override
	public void deleteQuestionsByTitleId(String titleId) {
		feedbackDao.deleteQuestionsByTitleId(titleId);
	}

	@Override
	public void deleteQuestionById(String id) {
		feedbackDao.deleteQuestionById(id);
	}

	@Override
	public List<Answer> getAllAnswers(String titleId) {
		return feedbackDao.getAllAnswers(titleId);
	}

	@Override
	public void deleteAnswers(String titleId) {
		List<FeedbackQuestion> questions = feedbackDao.getQuestionByTitle(titleId);
		
		for (FeedbackQuestion feedbackQuestion : questions) {
			feedbackDao.deleteAnswers(String.valueOf(feedbackQuestion.getId()));
		}
	}
}
