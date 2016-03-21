package com.ibm.gswt.campus.dao;

public interface RatingDao {

	public void insertRatingRelated(int topicID, int uid);

	public void deleteRatingRelated(int topicID, int uid);

	public void handleRating(int topicID, String checked);
	
	public String getIsRated(int uid, int tid);
}
