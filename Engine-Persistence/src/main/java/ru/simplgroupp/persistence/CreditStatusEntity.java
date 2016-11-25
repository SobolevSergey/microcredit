
package ru.simplgroupp.persistence;

import java.io.Serializable;
/**
 * статусы кредитной заявки
 */
public class CreditStatusEntity extends BaseEntity implements Serializable {
   
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 810750560740233439L;
	private Integer id;
    private String name;

    public CreditStatusEntity() {
    }

    public CreditStatusEntity(Integer id) {
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
        return this.name;
    }
    
}
