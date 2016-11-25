package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ru.simplgroupp.persistence.RolesEntity;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

public class Roles extends BaseTransfer<RolesEntity> implements Serializable, Initializable, Identifiable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2700047519622246931L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Roles.class.getConstructor(RolesEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }		
    
	public static final int ROLE_DECISION = 1;
	public static final int ROLE_RULES = 2;
	public static final int ROLE_CALL = 3;
	public static final int ROLE_CLIENT = 4;
	public static final int ROLE_VERIFICATOR = 5;
	public static final int ROLE_ADMIN = 6;
	public static final int ROLE_COLLECTOR = 7;
	public static final int ROLE_COLLECTOR_SUPERVISER = 8;

	public enum Options {        
        INIT_USERS;
	}
	
	
    private List<Users> users;
	
    public Roles(){
    	super();
    	entity = new RolesEntity();
    }
    
    public Roles(RolesEntity value) {
    	super(value);
    	users=new ArrayList<Users>(0); 
    }
    
    public Integer getId() {
        return entity.getId();
    }
    
    public void setId(Integer id) {
        entity.setId(id);
    }
    
    public String getName() {
        return entity.getName();
    }

    public void setName(String name) {
        entity.setName(name);
    }    
       
    public String getRealName() {
        return entity.getRealName();
    }

    public void setRealName(String realName) {
        entity.setRealName(realName);
    }    
    public Boolean getRightToEdit() {
        return entity.getRighttoedit();
    }

    public void setRightToEdit(Boolean righttoedit) {
        entity.setRighttoedit(righttoedit);
    }

    public Boolean getRightToWrite() {
        return entity.getRighttowrite();
    }

    public void setRightToWrite(Boolean righttowrite) {
        entity.setRighttowrite(righttowrite);
    }

    public Boolean getRightToAll() {
        return entity.getRighttoall();
    }

    public void setRightToAll(Boolean righttoall) {
        entity.setRighttoall(righttoall);
    }

    public Boolean getRightToCall() {
        return entity.getRighttocall();
    }

    public void setRightToCall(Boolean righttocall) {
        entity.setRighttocall(righttocall);
    }
    
    public Boolean getRightToAddRules() {
        return entity.getRighttoaddrules();
    }

    public void setRightToAddRules(Boolean righttoaddrules) {
        entity.setRighttoaddrules(righttoaddrules);
    }
    public Boolean getRightToView() {
        return entity.getRighttoview();
    }

    public void setRightToView(Boolean righttoview) {
        entity.setRighttoview(righttoview);
    }

    public List<Users> getUsers()
    {
    	return users;
    }

	@Override
	public void init(Set options) {
		if (options != null && options.contains(Options.INIT_USERS)) {
			Utils.initCollection(entity.getUsers(), options);
        	for (UsersEntity user1: entity.getUsers()) {
        		Users user = new Users(user1);
        		user.init(options);
        		users.add(user);
        	}
		}
		
	}

	
}

