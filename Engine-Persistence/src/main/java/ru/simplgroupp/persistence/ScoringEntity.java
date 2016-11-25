
package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * Скоринг
 */
public class ScoringEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected Integer txVersion = 0;
    private Integer id;
    /**
     * скоринговая оценка
     */
    private Double score;
    /**
     * скоринговая ошибка
     */
    private BigInteger scoreError;
    /**
     * текст ошибки
     */
    private String textError;
    /**
     * комментарии
     */
    private String comment;
    /**
     * причина ошибки
     */
    private Short reasonError;
    /**
     * модель скоринга
     */
    private ScoreModelEntity scoreModelId;
    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;
    /**
     * заявка
     */
    private CreditRequestEntity creditRequestId;
    /**
     * источник информации - партнер
     */
    private PartnersEntity partnersId;
    /**
     * оценка риска
     */
    private Double scoreRisk;
    /**
	 * дата скоринга
	 */
	private Date scoringDate;
	
    public ScoringEntity() {
    }

    public ScoringEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public BigInteger getScoreError() {
        return scoreError;
    }

    public void setScoreError(BigInteger scoreError) {
        this.scoreError = scoreError;
    }

    public String getTextError() {
        return textError;
    }

    public void setTextError(String textError) {
        this.textError = textError;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Short getReasonError() {
        return reasonError;
    }

    public void setReasonError(Short reasonError) {
        this.reasonError = reasonError;
    }

    public ScoreModelEntity getScoreModelId() {
        return scoreModelId;
    }

    public void setScoreModelId(ScoreModelEntity scoreModelId) {
        this.scoreModelId = scoreModelId;
    }

   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
    	 if (other == null) return false;
	       
  	    if (other == this) return true;
  	       
  	    if (! (other.getClass() ==  getClass()))
  	    	return false;
  	    
  	    ScoringEntity cast = (ScoringEntity) other;
  	    
  	    if (this.id == null) return false;
  	       
  	    if (cast.getId() == null) return false;       
  	       
  	    if (this.id.intValue() != cast.getId().intValue())
  	    	return false;
  	    
  	    return true;
    }

    @Override
    public String toString() {
        return "ru.simplgroupp.persistence.ScoringEntity[ id=" + id + " ]";
    }
    
    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }



    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }
 
    public CreditRequestEntity getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequestEntity creditRequestId) {
        this.creditRequestId = creditRequestId;
    }
    
    public PartnersEntity getPartnersId() {
        return partnersId;
    }


    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }

	public Double getScoreRisk() {
		return scoreRisk;
	}

	public void setScoreRisk(Double scoreRisk) {
		this.scoreRisk = scoreRisk;
	}

	public Date getScoringDate() {
		return scoringDate;
	}

	public void setScoringDate(Date scoringDate) {
		this.scoringDate = scoringDate;
	}
  
	
    
}
