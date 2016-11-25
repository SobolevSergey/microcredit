package ru.simplgroupp.persistence;

import java.io.Serializable;
/**
 * Справочник для верификации и валидации
 */
public class VerificationMarkEntity implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4434833585483521528L;

	protected Integer txVersion = 0;
   
	private Integer id;
	/**
	 * партнер
	 */
	private PartnersEntity partnersId;
	/**
	 * название
	 */
	private String name;
	/**
	 * минимальная оценка
	 */
	private Double markMin;
	/**
	 * максимальная оценка
	 */
	private Double markMax;
	/**
	 * вид - 1 валидация, 2 верификация
	 */
	private Integer kind;
	
    public VerificationMarkEntity(){
		
	}

	public VerificationMarkEntity(Integer id){
		this.id=id;
	}
	
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }
    
    public String getName()	{
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public Double getMarkMin(){
		return markMin;
	}
	
	public void setMarkMin(Double markMin){
		this.markMin=markMin;
	}
	
	public Double getMarkMax(){
		return markMax;
	}
	
	public void setMarkMax(Double markMax){
		this.markMax=markMax;
	}
	
	public Integer getKind(){
		return kind;
	}
	
	public void setKind(Integer kind){
		this.kind=kind;
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
	    
	    VerificationMarkEntity cast = (VerificationMarkEntity) other;
	    
	    if (this.id == null) return false;
	       
	    if (cast.getId() == null) return false;       
	       
	    if (this.id.intValue() != cast.getId().intValue())
	    	return false;
	    
	    return true;
   }
}
