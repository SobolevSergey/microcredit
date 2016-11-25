package ru.simplgroupp.transfer;

import java.io.Serializable;

public class QAutoAnswer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4559098891534734234L;
	private String questionKey;
	private String questionText;
	private int answerType;
	private Object answerValue;
	private String answerValueText;
	
	public String getQuestionKey() {
		return questionKey;
	}
	public void setQuestionKey(String questionKey) {
		this.questionKey = questionKey;
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public Object getAnswerValue() {
		return answerValue;
	}
	public void setAnswerValue(Object answerValue) {
		this.answerValue = answerValue;
	}
	public int getAnswerType() {
		return answerType;
	}
	public void setAnswerType(int answerType) {
		this.answerType = answerType;
	}
	public String getAnswerValueText() {
		return answerValueText;
	}
	public void setAnswerValueText(String answerValueText) {
		this.answerValueText = answerValueText;
	}
}
