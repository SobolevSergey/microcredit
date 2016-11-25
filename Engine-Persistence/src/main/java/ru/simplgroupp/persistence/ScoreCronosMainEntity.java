package ru.simplgroupp.persistence;

import java.io.Serializable;
/**
 * Работа с Кронос, пока не используется
 */
public class ScoreCronosMainEntity extends ScoringEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected Integer txVersion = 0;
	
	private Integer scoringId;
	private String answerRtf;
	private String answerHtml;
	private String answerPpfms;
	private String vectorFl;
	
	public ScoreCronosMainEntity() {
	}

	public ScoreCronosMainEntity(Integer scoringId) {
	        this.scoringId = scoringId;
	}

	public Integer getScoringId() {
	        return scoringId;
	}

	public void setScoringId(Integer scoringId) {
	        this.scoringId = scoringId;
	}

	public String getAnswerRtf()
	{
		return answerRtf;
	}
	
	public void setAnswerRtf(String answerRtf)
	{
		this.answerRtf=answerRtf;
	}
	
	public String getAnswerHtml()
	{
		return answerHtml;
	}
	
	public void setAnswerHtml(String answerHtml)
	{
		this.answerHtml=answerHtml;
	}
	
	public String getAnswerPpfms()
	{
		return answerPpfms;
	}
	
	public void setAnswerPpfms(String answerPpfms)
	{
		this.answerPpfms=answerPpfms;
	}
	
	public String getVectorFl()
	{
		return vectorFl;
	}
	
	public void setVectorFl(String vectorFl)
	{
		this.vectorFl=vectorFl;
	}
}
