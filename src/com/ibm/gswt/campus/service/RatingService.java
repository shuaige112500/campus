package com.ibm.gswt.campus.service;

public interface RatingService {
	public void insertRatingRelated(int topicID, int uid);

	public void deleteRatingRelated(int topicID, int uid);

	public void handleRating(int topicID, String checked);
	
	public String getIsRated(int uid, int tid);
}
