package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * Информация по негативу - статьи КОАП и УК 
 */
public class NegativeEntity extends BaseEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7872835956524056497L;
	protected Integer txVersion = 0;
	
	/**
	 * человек
	 */
	private PeopleMainEntity peopleMainId;
	/**
	 * заявка
	 */
	private CreditRequestEntity creditRequestId;
	/**
	 * партнер
	 */
	private PartnersEntity partnersId;
	/**
	 * модуль
	 */
	private String module;
	/**
	 * ссылка на статью по справочнику
	 */
	private RefNegativeEntity articleId;
	/**
	 * год статьи
	 */
	private Integer yearArticle;
	
	public NegativeEntity(){
		
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

    public String getModule(){
    	return module;
    }
    
    public void setModule(String module){
    	this.module=module;
    }
    
    public Integer getYearArticle(){
    	return yearArticle;
    }
    
    public void setYearArticle(Integer yearArticle){
    	this.yearArticle=yearArticle;
    }
    
    public RefNegativeEntity getArticleId(){
    	return articleId;
    }
    
    public void setArticleId(RefNegativeEntity articleId){
    	this.articleId=articleId;
    }
    
    
}
