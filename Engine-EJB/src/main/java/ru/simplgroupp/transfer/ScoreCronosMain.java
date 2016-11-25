package ru.simplgroupp.transfer;

import java.lang.reflect.Constructor;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.ScoreCronosEntity;
import ru.simplgroupp.persistence.ScoreCronosMainEntity;

public class ScoreCronosMain extends BaseScoring<ScoreCronosMainEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = ScoreCronosMain.class.getConstructor(ScoreCronosMainEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
        
	public ScoreCronosMain()
	{
		super();
		entity = new ScoreCronosMainEntity();
	}
	
	public ScoreCronosMain(ScoreCronosMainEntity value)
	{
		super(value);
	}
	 
	 public String getAnswerRtf()
	 {
		 return entity.getAnswerRtf();
	 }
	 
	 public void setAnswerRtf(String answerRtf)
	 {
		 entity.setAnswerRtf(answerRtf);
	 }
	 
	 public String getAnswerHtml()
	 {
		 return entity.getAnswerHtml();
	 }
	 
	 public void setAnswerHtml(String answerHtml)
	 {
		 entity.setAnswerHtml(answerHtml);
	 }
	 
	 public String getAnswerPpfms()
	 {
		 return entity.getAnswerPpfms();
	 }
	 
	 public void setAnswerPpfms(String answerPpfms)
	 {
		 entity.setAnswerPpfms(answerPpfms);
	 }
	 
	 public String getVectorFl()
	 {
		 return entity.getVectorFl();
	 }

	 @XmlElement
	 public void setVectorFl(String vectorFl)
	 {
		 entity.setVectorFl(vectorFl);
	 }
}
