package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * Тип события для лога
 */
public class EventTypeEntity extends BaseEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2982018447451864348L;
	private Integer id;
	private String name;
    private String shortname;
    
    public EventTypeEntity() {
    }

    public EventTypeEntity(Integer id) {
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

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }
    
    @Override
    public String toString() {
        return name.trim()+" | "+shortname.trim();
    }
}
