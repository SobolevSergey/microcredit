package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

import ru.simplgroupp.persistence.MessagesEntity;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

public class Messages extends BaseTransfer<MessagesEntity> implements Serializable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -976603053759458754L;
	
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
	static {
    	try {
			wrapConstructor = Messages.class.getConstructor(MessagesEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }

    // как было послано собщение
    public static final Integer AUTO = 1;
    public static final Integer MANUAL = 2;

    // вид сообщения
	public static final int SMS = 1;
    public static final int EMAIL = 2;
    public static final int MESSAGE = 3;

    // статус сообщения
    public static final int UNREAD = 0;
    public static final int READ = 1;
    public static final int ARHIVE = 2;

    // тип сообщения
    public static final int OUTCOME = 0;
    public static final int INCOME = 1;

	protected PeopleMain peopleMain;
    protected Users user; 
    protected Reference messageType;
    protected Reference messageWay;
    protected PeopleContact peopleContact;
    
    public Messages(){
    	super();
    	entity=new MessagesEntity();
    }
    
    public Messages(MessagesEntity value){
    	super(value);
    	peopleMain = new PeopleMain(entity.getPeopleMainId());
    	if (entity.getUserId()!=null){
    		user = new Users(entity.getUserId());
    	}
    	if (entity.getMessageTypeId()!=null){
    		messageType=new Reference(entity.getMessageTypeId());
    	}
    	if (entity.getMessageWayId()!=null){
    		messageWay=new Reference(entity.getMessageWayId());
    	}
        if (entity.getPeopleContactId() != null) {
            peopleContact = new PeopleContact(entity.getPeopleContactId());
        }
    }
    
    public Integer getId() {
        return entity.getId();
    }

    public void setId(Integer id) {
        entity.setId(id);
    }
    public PeopleMain getPeopleMain() {
        return peopleMain;
    }

    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain = peopleMain;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    
    public Reference getMessageType() {
        return messageType;
    }

    public void setMessageType(Reference messageType) {
        this.messageType=messageType;
    }
    
    public Reference getMessageWay() {
        return messageWay;
    }

    public void setMessageWay(Reference messageWay) {
        this.messageWay=messageWay;
    }
    
    public Date getMessageDate() {
        return entity.getMessageDate();
    }

    public void setMessageDate(Date messageDate) {
        entity.setMessageDate(messageDate);
    }
    
    public String getMessageHeader() {
        return entity.getMessageHeader();
    }

    public void setMessageHeader(String messageHeader) {
        entity.setMessageHeader(messageHeader);
    }
    
    public String getMessageBody() {
        return entity.getMessageBody();
    }

    public void setMessageBody(String messageBody) {
        entity.setMessageBody(messageBody);
    }

    public PeopleContact getPeopleContact() {
        return peopleContact;
    }

    public void setPeopleContact(PeopleContact peopleContact) {
        this.peopleContact = peopleContact;
    }

    public void setStatus(Integer status) {
        entity.setStatus(status);
    }

    public Integer getStatus() {
        return entity.getStatus();
    }

    public void setInOut(Integer inOut) {
        entity.setInOut(inOut);
    }

    public Integer getInOut() {
        return entity.getInOut();
    }

    @Override
	public void init(Set options) {
        if (user != null) {
            user.init(options);
        }
        if (peopleMain != null) {
            peopleMain.init(options);
        }
	}
}
