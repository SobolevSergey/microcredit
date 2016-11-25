package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.ScoringEntity;
import ru.simplgroupp.toolkit.common.Initializable;

abstract public class BaseScoring<E extends ScoringEntity> extends BaseTransfer<E> implements Serializable, Initializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2967757876961673105L;
	protected ScoreModel scoreModel;
    protected PeopleMain peopleMain;
	protected CreditRequest creditRequest;
	protected Partner partner;
    
    public BaseScoring() {
    	super();
    }
    
    public BaseScoring(E value) {
    	super(value);
    	if (entity.getScoreModelId()!=null) {
    	  scoreModel=new ScoreModel(entity.getScoreModelId());
    	}
    	peopleMain=new PeopleMain(entity.getPeopleMainId());
        partner=new Partner(entity.getPartnersId());
        creditRequest=new CreditRequest(entity.getCreditRequestId());
    }
    
    public Integer getId() {
        return entity.getId();
    }
    
    public void setId(Integer id) {
        entity.setId(id);
    }
       
    public Double getScore() {
        return entity.getScore();
    }

    @XmlElement
    public void setScore(Double score) {
        entity.setScore(score);
    }

    public Double getScoreRisk() {
        return entity.getScoreRisk();
    }

    @XmlElement
    public void setScoreRisk(Double scoreRisk) {
        entity.setScoreRisk(scoreRisk);
    }
    
    public BigInteger getScoreError() {
        return entity.getScoreError();
    }

    public void setScoreError(BigInteger scoreError) {
        entity.setScoreError(scoreError);
    }

    public String getTextError() {
        return entity.getTextError();
    }

    public void setTextError(String textError) {
        entity.setTextError(textError);
    }

    public String getComment() {
    	return entity.getComment();
    }
    
    public void setComment(String comment){
    	entity.setComment(comment);
    }
    
    public Short getReasonError() {
        return entity.getReasonError();
    }

    public void setReasonError(Short reasonError) {
        entity.setReasonError(reasonError);
    }
    
    public ScoreModel getScoreModel() {
    	return scoreModel;
    }
    
    public void setScoreModel(ScoreModel scoreModel){
    	this.scoreModel=scoreModel;
    }
    
    public PeopleMain getPeopleMain() {
        return peopleMain;
    }

    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain=peopleMain;
    }
    
    public CreditRequest getCreditRequest() {
        return creditRequest;
    }

    public void setCreditRequest(CreditRequest creditRequest) {
        this.creditRequest=creditRequest;
    }
    
    public Partner getPartner() {
        return partner;
    }

    @XmlElement
    public void setPartner(Partner partner) {
        this.partner=partner;
    }
    
    public Date getScoringDate(){
    	return entity.getScoringDate();
    }
    
    @XmlElement
    public void setScoringDate(Date scoringDate){
    	entity.setScoringDate(scoringDate);
    }
    
	@Override
	public void init(Set options) {
	    entity.getScore();		
	}
}
