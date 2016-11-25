package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Пользователи
 */
public class UsersEntity implements Serializable {

    private static final long serialVersionUID = -3314236498497981674L;

    protected Integer txVersion = 0;

    private Integer id;

    /**
     * имя пользователя 
     */
    private String username;

    /**
     * пароль
     */
    private String password;

    /**
     * token авторизации
     */
    private String privateToken;

    /**
     * временная ссылка
     */
    private String tempLink;

    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;

    /**
     * вид пользователя
     */
    private UsertypeEntity usertype;

    /**
     * роли
     */
    private List<RolesEntity> roles = new ArrayList<>(0);

    /**
     * логи
     */
    private List<EventLogEntity> logs = new ArrayList<>(0);

    /**
     * сообщения
     */
    private List<MessagesEntity> messages = new ArrayList<>(0);

    private WorkplaceEntity workplace;

    /**
     * UUID из oktell
     */
    private String oktellUuid;


    public UsersEntity() {
    }

    public UsersEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateToken() {
        return privateToken;
    }

    public void setPrivateToken(String privateToken) {
        this.privateToken = privateToken;
    }

    public String getTempLink() {
        return tempLink;
    }

    public void setTempLink(String tempLink) {
        this.tempLink = tempLink;
    }

    public UsertypeEntity getUsertype() {
        return usertype;
    }

    public void setUsertype(UsertypeEntity usertype) {
        this.usertype = usertype;
    }

    public List<RolesEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RolesEntity> roles) {
        this.roles = roles;
    }

    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public List<EventLogEntity> getLogs() {
        return logs;
    }

    public void setLogs(List<EventLogEntity> logs) {
        this.logs = logs;
    }

    public WorkplaceEntity getWorkplace() {
        return workplace;
    }

    public void setWorkplace(WorkplaceEntity workplace) {
        this.workplace = workplace;
    }

    public int getHasLogs() {
        if (getLogs().size() > 0) {
            return getLogs().size();
        } else {
            return 0;
        }
    }

    public int getHasRoles() {
        if (getRoles().size() > 0) {
            return getRoles().size();
        }
        return 0;
    }

    public int getHasMessages() {
        if (getMessages().size() > 0) {
            return getMessages().size();
        }
        return 0;
    }

    public String getRolesDescription() {
        String str = "";
        if (getRoles().size() > 0) {
            for (RolesEntity rl : getRoles()) {
                str += rl.getRealName() + "; ";
            }
        }
        return str;
    }

    public String getRoleNameDescription() {
        String str = "";
        if (getRoles().size() > 0) {
            for (RolesEntity rl : getRoles()) {
                str += rl.getName() + "; ";
            }
        }
        return str;
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

        UsersEntity cast = (UsersEntity) other;

        if (this.id == null) {
            return false;
        }

        if (cast.getId() == null) {
            return false;
        }

        return this.id.intValue() == cast.getId().intValue();

    }

    @Override
    public String toString() {
        return this.username;
    }

    public List<MessagesEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesEntity> messages) {
        this.messages = messages;
    }

    public String getOktellUuid() {
        return oktellUuid;
    }

    public void setOktellUuid(String oktellUuid) {
        this.oktellUuid = oktellUuid;
    }
}
