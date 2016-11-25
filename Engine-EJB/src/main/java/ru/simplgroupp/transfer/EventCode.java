package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.EventCodeEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class EventCode extends BaseTransfer<EventCodeEntity> implements Serializable, Initializable {   
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2489689304929376103L;
	/**
	 * 
	 */
	
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = EventCode.class.getConstructor(EventCodeEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	public static final int ANY = 0;
	public static final int NEW_CREDIT_REQUEST = 1; 
    public static final int MANAGER_CALL = 2;
    public static final int COLLECTOR_CALL = 3;
    public static final int GET_CREDIT_REQUEST_RESULT = 4;
    public static final int GET_MONEY = 5;
    public static final int PAY_MONEY = 6;
    public static final int LONG_MONEY_REQUEST = 7;
    public static final int ADD_USER = 8;
    public static final int DELETE_USER = 9;
    public static final int SAVE_CREDIT_REQUEST = 10;
    public static final int DELETE_CREDIT_REQUEST = 11;
    public static final int INNER_SCORING = 12;
    public static final int OUTER_SCORING_CRONOS = 13;
    public static final int OUTER_SCORING_EQUIFAX = 14;
    public static final int SIGNATURE_NOT_VALID_EQUIFAX = 15;
    public static final int SIGNATURE_NOT_VALID_CRONOS = 16;
    public static final int SIGNATURE_NOT_VALID_OKB = 17;
    public static final int OUTER_SCORING_OKB_CAIS = 18;
    public static final int OUTER_SCORING_OKB_IDV = 19;
    public static final int OUTER_SCORING_OKB_HUNTER = 20;
    public static final int SIGNATURE_NOT_VALID_RSTANDARD = 21;
    public static final int OUTER_SCORING_RSTANDARD = 22;
    public static final int UPLOAD_EQUIFAX=23;
    public static final int UPLOAD_OKB=24;
    public static final int UPLOAD_RSTANDARD=25;
    public static final int ERROR_UPLOAD_EQUIFAX=26;
    public static final int ERROR_UPLOAD_OKB=27;
    public static final int ERROR_UPLOAD_RSTANDARD=28;
    public static final int CLIENT_REFUSE=29;
    public static final int LONG_MONEY = 30;
    public static final int STOP_CREDIT = 31;
    public static final int RESTART_CREDIT = 32;
    public static final int VERIFICATOR_QUESTIONS = 33;
    public static final int CREATE_PAYMENT = 34;
    public static final int ERROR_PAYMENT = 35;
    public static final int SEND_CALL_TO_FRIEND = 36;
    public static final int PAYMENT_COMPLETED_SUCCESS = 37;
    public static final int PAYMENT_COMPLETED_FAILURE = 38;
    public static final int BIZACTION_CHANGED = 39;
    public static final int BIZACTION_DELETED = 40;
    public static final int START_REFINANCE = 41;
    public static final int CRYPTO_COMMON_SETTINGS_CHANGED = 42;
    public static final int EARLY_CLOSE_CREDIT = 43;
    public static final int CLOSE_CREDIT_WITHOUT_DELAY = 44;
    public static final int BONUS_ADDED = 45;
    public static final int OUTER_SCORING_SOCIOHUB = 46;
    public static final int OUTER_SCORING_SKB = 50;
    public static final int UPLOAD_SKB=51;
    public static final int ERROR_UPLOAD_SKB=52;
    public static final int CREDIT_CLOSE=53;
    public static final int OFERTA_SIGNED=54;
    public static final int CREDIT_START=55;
    public static final int EXTERNAL_PAYMENT_RECEIVED = 56;
    public static final int OUTER_SCORING_NBKI = 57;
    public static final int UPLOAD_NBKI=58;
    public static final int SIGNATURE_NOT_VALID_NBKI = 59;
    public static final int ERROR_UPLOAD_NBKI=60;
    public static final int ROLE_PERM_CHANGED=61;
    public static final int CALLBACK_TAKEN=62;
    public static final int REQUEST_OUTER_DB=63;
    public static final int CREDIT_TO_COLLECTOR=64;
    public static final int CREDIT_TO_OUTER_COLLECTOR=65;
    public static final int CREDIT_TO_COURT=66;
    public static final int COLLECTOR_CALL_RESULT = 67;
    public static final int COLLECTOR_CLIENT_GET = 68;
    public static final int OFFER_ANOTHER_CREDIT_CONDITIONS=69;
    public static final int ACCEPT_ANOTHER_CREDIT_CONDITIONS=70;
    public static final int DECLINE_ANOTHER_CREDIT_CONDITIONS=71;
    public static final int SAVE_IDENTIFICATION_POINTS = 72;
    public static final int BLACKLIST_ADD=73;
    public static final int BLACKLIST_REMOVE=74;
    public static final int CHECK_ANTIFRAUD_RULES=75;
    public static final int ROLE_UPDATED=76;
    public static final int ROLE_DELETED=77;
    public static final int ROLE_CREATED=78;
    
    public EventCode() {
        super();
        entity = new EventCodeEntity();
    }
    
    public EventCode(EventCodeEntity entity) {
        super(entity);
    }   
    
    public EventCode(int id) {
        super();
        entity = new EventCodeEntity();
        entity.setId(id);
    }    
    
    public Integer getId() {
        return entity.getId();
    }

    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }

    public String getName() {
        return entity.getName();
    }

    @XmlElement
    public void setName(String name) {
        entity.setName(name);
    }

    @Override
    public void init(Set options) {
        entity.getName();        
    }
    
}
