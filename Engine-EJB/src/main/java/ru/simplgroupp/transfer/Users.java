package ru.simplgroupp.transfer;

import org.apache.commons.lang.ArrayUtils;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.util.UserUtil;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Users extends BaseTransfer<UsersEntity> implements Serializable, Initializable, Identifiable {
    public static final String GUEST_NAME = "guest";
    public static final int USER_ADMIN = 1;
    public static final int USER_CLIENT = 2;
    public static final int USER_SPECIALIST = 3;
    public static final int USER_SYSTEM = 4;
    /**
     *
     */
    private static final long serialVersionUID = 1129345806102318608L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = Users.class.getConstructor(UsersEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    protected PeopleMain peopleMain;
    protected UserType userType;
    protected List<Roles> roles;
    protected List<EventLog> logs;
    protected List<Messages> messages;


    public Users() {
        super();
        entity = new UsersEntity();
    }

    public Users(UsersEntity value) {
        super(value);
        peopleMain = new PeopleMain(entity.getPeopleMainId());
        userType = new UserType(entity.getUsertype());
        roles = new ArrayList<>(0);
        logs = new ArrayList<>(0);
        messages = new ArrayList<>(0);
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
        //роли
        if (options != null && options.contains(Options.INIT_ROLES)) {

            Utils.initCollection(entity.getRoles(), options);
            for (RolesEntity role1 : entity.getRoles()) {
                Roles role = new Roles(role1);
                role.init(options);
                roles.add(role);
            }
        }
        //логи
        if (options != null && options.contains(Options.INIT_LOGS)) {

            Utils.initCollection(entity.getLogs(), options);
            for (EventLogEntity log : entity.getLogs()) {
                EventLog log1 = new EventLog(log);
                log1.init(options);
                logs.add(log1);
            }
        }
        //сообщения
        if (options != null && options.contains(Options.INIT_MESSAGES)) {

            Utils.initCollection(entity.getMessages(), options);
            for (MessagesEntity ent : entity.getMessages()) {
                Messages message = new Messages(ent);
                message.init(options);
                messages.add(message);
            }
        }

        if (peopleMain != null) {
            peopleMain.init(options);
        }
  
    }

    public Integer getId() {
        return entity.getId();
    }

    public String getUserName() {
        return entity.getUsername();
    }

    public void setUserName(String username) {
        entity.setUsername(username);
    }

    public String getPassword() {
        return entity.getPassword();
    }

    public void setPassword(String password) {
        entity.setPassword(password);
    }

    public String getPrivateToken() {
        return entity.getPrivateToken();
    }

    public void setPrivateToken(String privateToken) {
        entity.setPrivateToken(privateToken);
    }

    public String getTempLink() {
        return entity.getTempLink();
    }

    public void setTempLink(String tempLink) {
        entity.setTempLink(tempLink);
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUsertype(UserType userType) {
        this.userType = userType;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    public List<EventLog> getLogs() {
        return logs;
    }

    public String getRolesDescription() {
        return entity.getRolesDescription();
    }

    public String getRoleNameDescription() {
        return entity.getRoleNameDescription();
    }

    public PeopleMain getPeopleMain() {
        return peopleMain;
    }

    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain = peopleMain;
    }

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    public Integer getHasMessages() {
        return entity.getHasMessages();
    }

    public boolean hasRolesAll(String... roleNames) {
        int n = 0;
        for (Roles role : getRoles()) {
            if (ArrayUtils.contains(roleNames, role.getName())) {
                n++;
            }
        }
        return (n == roleNames.length);
    }

    public boolean hasRoleAny(String... roleNames) {
        for (Roles role : getRoles()) {
            if (ArrayUtils.contains(roleNames, role.getName())) {
                return true;
            }
        }
        return false;
    }

    public WorkplaceEntity getWorkplace() {
        return this.entity.getWorkplace();
    }

    public void setWorkplace(WorkplaceEntity workplace) {
        this.entity.setWorkplace(workplace);
    }


    /**
     * является ли пользователь администратором
     */
    public boolean isAdmin() {
        Roles radmin = UserUtil.findRoleById(roles, Roles.ROLE_ADMIN);
        return (radmin != null);
    }

    /**
     * является ли пользователь кредитным менеджером
     */
    public boolean isManager() {
        Roles manager = UserUtil.findRoleById(roles, Roles.ROLE_DECISION);
        return (manager != null);
    }
    
    /**
     * является ли пользователь верификатором
     */
    public boolean isVerificator() {
        Roles verificator = UserUtil.findRoleById(roles, Roles.ROLE_VERIFICATOR);
        return (verificator != null);
    }
    
    /**
     * является ли пользователь специалистом call-центра
     */
    public boolean isCallCenter() {
        Roles call = UserUtil.findRoleById(roles, Roles.ROLE_CALL);
        return (call != null);
    }
    
    /**
     * является ли пользователь специалистом call-центра и у него нет других ролей
     */
    public boolean isCallCenterOnly() {
        return (isCallCenter()&&!isAdmin()&&!isManager()&&!isVerificator());
    }
    /**
     * возвращает инициалы в виде Иванов И.И.
     */
    public String getInitials() {
        return this.getPeopleMain().getPeoplePersonalActive().getInitials();
    }

    /**
     * возвращает фио в виде Иванов Иван Иванович
     */
    public String getFullName() {
    	if (this.getPeopleMain().getPeoplePersonalActive()!=null){
            return this.getPeopleMain().getPeoplePersonalActive().getFullName();
    	} 
    	return "";
    }

    public String getOktellUuid() {
        return entity.getOktellUuid();
    }

    public void setOktellUuid(String oktellUuid) {
        entity.setOktellUuid(oktellUuid);
    }

    public enum Options {
        INIT_ROLES,
        INIT_MESSAGES,
        INIT_LOGS;
    }
}
