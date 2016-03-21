package com.ibm.gswt.campus.bean;

import java.util.List;

public class Feedback {

	private String question;
	
	private String answer;
	
	private String title;
	
	private String tel;
	
	private List<String> alternate;
	
	private String type;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public List<String> getAlternate() {
		return alternate;
	}

	public void setAlternate(List<String> alternate) {
		this.alternate = alternate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
