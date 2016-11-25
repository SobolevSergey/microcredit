package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * Добавленные данные по друзьям
 */

public class PeopleFriendEntity extends BaseEntity implements Serializable {
 
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 8458221739596104224L;

	protected Integer txVersion = 0;
    
     /**
     * Фамилия
     */
    private String surname;
    
    /**
     * Имя
     */
    private String name;
    
    /**
     * Email
     */
    private String email;
    
    /**
     * дата изменений
     */
    private Date dateactual;
    /**
     * телефон друга
     */
    private String phone;
    /**
     * можно ли звонить
     */
    private Boolean available;
    /**
     * считаем по нему бонусы
     */
    private Integer forBonus;
    /**
     * человек, пригласивший друга
     */
    private PeopleMainEntity peopleMainId;


    public PeopleFriendEntity() {

    }
    
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public Date getDateactual() {
		return dateactual;
	}

	public void setDateactual(Date dateactual) {
		this.dateactual = dateactual;
	}

	public PeopleMainEntity getPeopleMainId() {
		return peopleMainId;
	}

	public void setPeopleMainId(PeopleMainEntity peopleMainId) {
		this.peopleMainId = peopleMainId;
	}
    
 
    @Override
    public String toString() {
        return this.name +" "+ this.surname;
    }

	public PeopleFriendEntity clone() throws CloneNotSupportedException {
		PeopleFriendEntity ent = (PeopleFriendEntity) super.clone();
		return ent;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Integer getForBonus() {
		return forBonus;
	}

	public void setForBonus(Integer forBonus) {
		this.forBonus = forBonus;
	}
    
	/**
	 * возвращает фамилию-имя
	 * @return
	 */
	public String getInitials(){
		String initials="";
		if (StringUtils.isNotBlank(surname)){
			initials+=surname+" ";
		}
		if (StringUtils.isNotBlank(name)){
			initials+=name+" ";
		}
	    return initials; 
	}
	
}
