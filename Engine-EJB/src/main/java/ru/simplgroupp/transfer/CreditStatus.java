package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.CreditStatusEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

public class CreditStatus  extends BaseTransfer<CreditStatusEntity> implements Serializable, Initializable, Identifiable {
    
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -7768485698441330669L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = CreditStatus.class.getConstructor(CreditStatusEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }
    
	public static final int FILLED = 1;
	public static final int FOR_COURT = 3;
	public static final int DECISION = 4;
	public static final int IN_PROCESS = 5;
	public static final int FOR_COLLECTOR = 6;
	public static final int CLOSED = 7;
	public static final int REJECTED = 2;
	public static final int CLIENT_REFUSE = 8;

	//на рассмотрении
	public static final int UPLOAD_EQUIFAX_FILLED=1;
	public static final int UPLOAD_RS_FILLED=0;
	public static final String UPLOAD_FILLED_TEXT="Получена заявка на предоставление кредитных средств";
	//одобрена
	public static final int UPLOAD_APPROVED=2;
	public static final String UPLOAD_APPROVED_TEXT="Одобрена заявка на предоставление кредитных средств";
	//отказана
	public static final int UPLOAD_REJECTED=3;
	public static final String UPLOAD_REJECTED_TEXT="Отказано в предоставлении кредитных средств";
	//одобрена и выдан кредит
	public static final int UPLOAD_EQUIFAX_APPROVED_WITH_CREDIT=4;
	public static final int UPLOAD_RS_APPROVED_WITH_CREDIT=5;
	public static final String UPLOAD_RS_APPROVED_WITH_CREDIT_TEXT="Оформлен кредитный договор";
	//одобрена и клиент отказался
	public static final int UPLOAD_EQUIFAX_APPROVED_CLIENT_REFUSE=5;
	public static final int UPLOAD_RS_APPROVED_CLIENT_REFUSE=4;
	public static final String UPLOAD_RS_APPROVED_CLIENT_REFUSE_TEXT="Отказ заявителя от получения кредитных средств";
	//обязательства исполнены
	public static final int UPLOAD_RS_CLOSED=10;
	public static final String UPLOAD_RS_CLOSED_TEXT="Кредитные обязательства заемщика исполнены в полном объеме";
	
	public CreditStatus(CreditStatusEntity value) {
    	super(value);
    }
    
    public CreditStatus() {
    	super();
    	entity = new CreditStatusEntity();
    }
    
    @Override
    public void init(Set options) {
        entity.getName();
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

  

}
