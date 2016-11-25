
package ru.simplgroupp.persistence;

import java.io.Serializable;
/**
 * Вид события для лога
 */
public class EventCodeEntity extends BaseEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8146825451767841263L;
	
	private Integer id;
    private String name;
   
    public EventCodeEntity() {
    }
    
    public EventCodeEntity(Integer id) {
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

    @Override
    public String toString() {
        return name.trim();
    }
    
}
