package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Роли в системе
 */
public class RolesEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * название роли
     */
    private String name;
    /**
     * название роли по-русски
     */
    private String realName;
    /**
     * право редактировать
     */
    private Boolean righttoedit;
    /**
     * право записывать
     */
    private Boolean righttowrite;
    /**
     * все права
     */
    private Boolean righttoall;
    /**
     * право посмотреть
     */
    private Boolean righttoview;
    /**
     * право добавлять правила
     */
    private Boolean righttoaddrules;
    /**
     * право позвонить
     */
    private Boolean righttocall;
    /**
     * пользователи
     */
     private List<UsersEntity> users=new ArrayList<UsersEntity>(0);
    
    public RolesEntity() {
    }

    public RolesEntity(Integer id) {
        this.id = id;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
    public Boolean getRighttoedit() {
        return righttoedit;
    }

    public void setRighttoedit(Boolean righttoedit) {
        this.righttoedit = righttoedit;
    }

    public Boolean getRighttowrite() {
        return righttowrite;
    }

    public void setRighttowrite(Boolean righttowrite) {
        this.righttowrite = righttowrite;
    }

    public Boolean getRighttoall() {
        return righttoall;
    }

    public void setRighttoall(Boolean righttoall) {
        this.righttoall = righttoall;
    }

    public Boolean getRighttoview() {
        return righttoview;
    }

    public void setRighttoview(Boolean righttoview) {
        this.righttoview = righttoview;
    }

    public Boolean getRighttocall() {
        return righttocall;
    }

    public void setRighttocall(Boolean righttocall) {
        this.righttocall = righttocall;
    }
    
    public Boolean getRighttoaddrules() {
        return righttoaddrules;
    }

    public void setRighttoaddrules(Boolean righttoaddrules) {
        this.righttoaddrules = righttoaddrules;
    }
    public List<UsersEntity> getUsers()
    {
    	return users;
    }
    
    public void setUsers(List<UsersEntity> users)
    {
    	this.users=users;
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
  	    
  	    RolesEntity cast = (RolesEntity) other;
  	    
  	    if (this.id == null) return false;
  	       
  	    if (cast.getId() == null) return false;       
  	       
  	    if (this.id.intValue() != cast.getId().intValue())
  	    	return false;
  	    
  	    return true;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
}