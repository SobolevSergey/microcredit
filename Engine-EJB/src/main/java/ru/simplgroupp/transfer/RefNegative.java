package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.RefNegativeEntity;
import ru.simplgroupp.persistence.RefinanceEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class RefNegative extends BaseTransfer<RefNegativeEntity> implements Serializable, Initializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3203802930039138478L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 
    static {
    	try {
			wrapConstructor = RefNegative.class.getConstructor(RefNegativeEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }    
	public static final String ARTICLE_TERROR="200205";
	
	public RefNegative(){
		super();
		entity = new RefNegativeEntity();
	}
	
	public RefNegative(RefNegativeEntity entity){
		super(entity);
	}
	
	 public Integer getId() {
	     return entity.getId();
	 }

	 @XmlElement
	 public void setId(Integer id) {
	     entity.setId(id);
	 }
	
	 public String getName() {
	     return entity.getName();
	 }

	 @XmlElement
	 public void setName(String name) {
	     entity.setName(name);
	 }
	 
	 public String getArticle() {
	     return entity.getArticle();
	 }

	 @XmlElement
	 public void setArticle(String article) {
	     entity.setArticle(article);
	 }
	 
	 public String getGroupId() {
	     return entity.getGroupId();
	 }

	 @XmlElement
	 public void setGroupId(String groupId) {
	     entity.setGroupId(groupId);
	 }
	 
	 public String getSubgroupId() {
	     return entity.getSubgroupId();
	 }
	 
	 @XmlElement
	 public void setSubgroupId(String subgroupId) {
	     entity.setSubgroupId(subgroupId);
	 }
	 
	 public String getCountry() {
	     return entity.getCountry();
	 }

	 @XmlElement
	 public void setCountry(String country) {
	     entity.setCountry(country);
	 }
	 
	 public Integer getLasts() {
	     return entity.getLasts();
	 }

	 @XmlElement
	 public void setLasts(Integer lasts) {
	     entity.setLasts(lasts);
	 }
	 
	@Override
	public void init(Set options) {
		entity.getName();
		
	}

}
