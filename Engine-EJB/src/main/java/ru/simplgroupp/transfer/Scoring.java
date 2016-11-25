package ru.simplgroupp.transfer;


import java.lang.reflect.Constructor;

import ru.simplgroupp.persistence.ScoringEntity;

public class Scoring extends BaseScoring<ScoringEntity> {
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 3257899973828791390L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Scoring.class.getConstructor(ScoringEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
  
	public Scoring() {
    	super();
    	entity = new ScoringEntity();
    }
    
    public Scoring(ScoringEntity value) {
    	super(value);
    
    }
    
   
}
