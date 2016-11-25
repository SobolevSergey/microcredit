package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * Справочник для статей негатива
 */
public class RefNegativeEntity  implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3879163246525592863L;

	protected Integer txVersion = 0;
	
	private Integer id;
	/**
	 * название
	 */
    private String name;
    /**
     * статья
     */
    private String article;
    /**
     * группа
     */
    private String groupId;
    /**
     * подгруппа
     */
    private String subgroupId;
    /**
     * в какой стране действует
     */
    private String country;
    /**
     * сколько продолжается
     */
    private Integer lasts;
    
    public RefNegativeEntity(){
    	
    }
    
    public RefNegativeEntity(Integer id){
    	this.id=id;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
    
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getSubgroupId() {
        return subgroupId;
    }

    public void setSubgroupId(String subgroupId) {
        this.subgroupId = subgroupId;
    }
    
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    public Integer getLasts(){
    	return lasts;
    }
    
    public void setLasts(Integer lasts){
    	this.lasts=lasts;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other == this) {
            return true;
        }

        if (!(other.getClass() == getClass())) {
            return false;
        }

        BaseEntity entity = (BaseEntity) other;

        if (this.id == null) {
            return false;
        }

        if (entity.getId() == null) {
            return false;
        }

        return this.id.equals(entity.getId());
    }
   
}
