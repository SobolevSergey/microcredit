package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.RolesEntity;
import ru.simplgroupp.persistence.ScoreCronosEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class ScoreCronos extends BaseTransfer<ScoreCronosEntity> implements Serializable, Initializable{

	private static final long serialVersionUID = 1L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = ScoreCronos.class.getConstructor(ScoreCronosEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }
    
	protected Scoring scoring;
	protected Reference vector;
	
	public ScoreCronos()
	{
		super();
		entity = new ScoreCronosEntity();
	}

	public ScoreCronos(ScoreCronosEntity value)
	{
		super(value);
		scoring=new Scoring(entity.getScoringId());
		vector=new Reference(entity.getVectorId());
	}
	
	public Integer getId() {
        return entity.getId();
    }
    
	@XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }
      
    public String getVectorCode() {
        return entity.getVectorCode();
    }

    @XmlElement
    public void setVectorCode(String vectorCode) {
        entity.setVectorCode(vectorCode);
    }

    public String getVectorText() {
        return entity.getVectorText();
    }

    @XmlElement
    public void setVectorText(String vectorText) {
        entity.setVectorText(vectorText);
    }

  
    public Reference getVector() {
        return vector;
    }

    @XmlElement
    public void setVector(Reference vector) {
        this.vector = vector;
    }
    
    public Scoring getScoring() {
        return scoring;
    }

    public void setScoring(Scoring scoring) {
        this.scoring = scoring;
    }
    
	@Override
	public void init(Set options) {
	    entity.getVectorCode();		
	}
   
}