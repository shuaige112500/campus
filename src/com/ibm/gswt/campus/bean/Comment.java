package com.ibm.gswt.campus.bean;

public class Comment {
	private String cid;
	
	private String comments;
	
	private String isDeleted;
	
	private int topicId;
	
	private int UID;
	
	private String userName;
	
	private String createdTS;
	
	private String choosen;
	
	private String modifiedTS;
	
	private String pnum;
	
	private String topicName;
	
	private String speakerName;

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public int getUID() {
		return UID;
	}

	public void setUID(int uID) {
		UID = uID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCreatedTS() {
		return createdTS;
	}

	public void setCreatedTS(String createdTS) {
		this.createdTS = createdTS;
	}

	public String getChoosen() {
		return choosen;
	}

	public void setChoosen(String choosen) {
		this.choosen = choosen;
	}

	public String getModifiedTS() {
		return modifiedTS;
	}

	public void setModifiedTS(String modifiedTS) {
		this.modifiedTS = modifiedTS;
	}

	public String getPnum() {
		return pnum;
	}

	public void setPnum(String pnum) {
		this.pnum = pnum;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getSpeakerName() {
		return speakerName;
	}

	public void setSpeakerName(String speakerName) {
		this.speakerName = speakerName;
	}
}
