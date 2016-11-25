
package ru.simplgroupp.persistence;

import java.io.Serializable;
/**
 * Работа с Кронос, пока не используется
 */
public class ScoreCronosEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected Integer txVersion = 0;
   
    private Integer id;
    private String vectorCode;
    private String vectorText;
    private ScoringEntity scoringId;
   
    private ReferenceEntity vectorId;

    public ScoreCronosEntity() {
    }

    public ScoreCronosEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getVectorCode() {
        return vectorCode;
    }

    public void setVectorCode(String vectorCode) {
        this.vectorCode = vectorCode;
    }

    public String getVectorText() {
        return vectorText;
    }

    public void setVectorText(String vectorText) {
        this.vectorText = vectorText;
    }

  
    public ReferenceEntity getVectorId() {
        return vectorId;
    }

    public void setVectorId(ReferenceEntity vectorId) {
        this.vectorId = vectorId;
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
 	    
 	    ScoreCronosEntity cast = (ScoreCronosEntity) other;
 	    
 	    if (this.id == null) return false;
 	       
 	    if (cast.getId() == null) return false;       
 	       
 	    if (this.id.intValue() != cast.getId().intValue())
 	    	return false;
 	    
 	    return true;
    }

    @Override
    public String toString() {
        return "ru.simplgroupp.persistence.ScoreCronosEntity[ scoreCronosEntityPK=" ;
    }

    public ScoringEntity getScoringId() {
        return scoringId;
    }

    public void setScoringId(ScoringEntity scoringId) {
        this.scoringId = scoringId;
    }
    
}
