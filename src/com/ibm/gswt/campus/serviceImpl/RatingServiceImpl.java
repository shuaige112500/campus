package com.ibm.gswt.campus.serviceImpl;

import com.ibm.gswt.campus.dao.RatingDao;
import com.ibm.gswt.campus.daoImpl.RatingDaoImpl;
import com.ibm.gswt.campus.service.RatingService;

public class RatingServiceImpl implements RatingService {
	
	private RatingDao ratingDao = new RatingDaoImpl();
	
	public void insertRatingRelated(int topicID, int uid) {
		
		ratingDao.insertRatingRelated(topicID, uid);
		
	}

	public void deleteRatingRelated(int topicID, int uid) {
		
		ratingDao.deleteRatingRelated(topicID, uid);
		
	}

	public void handleRating(int topicID, String checked) {
		
		ratingDao.handleRating(topicID, checked);
		
	}
	
	public String getIsRated(int uid, int tid) {
		
		String rate = "N";
		rate = ratingDao.getIsRated(uid, tid);
		return rate;
		
	}
}
