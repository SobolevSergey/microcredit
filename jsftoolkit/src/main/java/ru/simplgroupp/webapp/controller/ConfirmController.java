package ru.simplgroupp.webapp.controller;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.MailBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.webapp.util.JSFUtils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

public class ConfirmController extends AbstractSessionController implements Serializable, ClientBehaviorHolder {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4216623221027460214L;


	private static Logger logger = Logger.getLogger(ConfirmController.class.getName());
	
	
	@EJB
	protected MailBeanLocal mailBean;
	@EJB
	protected PeopleBeanLocal peopleBean;
	@EJB
	protected PeopleDAO peopleDAO;
	protected String smsCode;
	protected String confirmSmsCode;
	protected PeopleMain ppl;
	protected int smsCount;
	protected int timeInterval;
	protected int currentCountDown;

	protected Map<java.lang.String,List<ClientBehavior>> behaviors = new HashMap<java.lang.String,List<ClientBehavior>>();
	
	@PostConstruct
	public void init(){
		int peopleId = userCtl.getUser().getPeopleMain().getId();
		ppl=peopleDAO.getPeopleMain(peopleId,Utils.setOf(PeopleMain.Options.INIT_PEOPLE_CONTACT));
		smsCount = 0;
		
	}
	
	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}
	
	public boolean isSmsCodeSended() {
		return (!StringUtils.isEmpty(smsCode));
	}
	
	
	public String dummy() {
		return null;
	}
	
		
	public String getConfirmSmsCode() {
		return confirmSmsCode;
	}

	public void setConfirmSmsCode(String confirmSmsCode) {
		this.confirmSmsCode = confirmSmsCode;
	}
	
	public void sendSmsCode() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		sendSmsCode(facesCtx);
	}
	
	private void sendSmsCode(FacesContext facesCtx) {
	//	smsCode = GenUtils.genCode(6);
		smsCode=mailBean.generateCodeForSending();
		logger.info("Sms code "+smsCode);
		
		try {			
			mailBean.sendSMSV2(ppl.getCellPhone().getValue(), "Код: "+smsCode+". Это код для подтверждения редактирования личных данных в системе Растороп.");
			smsCount++;
		} catch (Exception ex) {
			JSFUtils.handleAsError(facesCtx, null, new KassaException("Не удалось отправить смс. "));
		}		
	}
	
	public void sendCodeLsn(ActionEvent event)
	{
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		Number nint = (Number) event.getComponent().getAttributes().get("timeInterval");
		if (nint == null) {
			timeInterval = 60;
		} else {
			timeInterval = nint.intValue();
		}			
		
		sendSmsCode(facesCtx);
	}

	@Override
	public void addClientBehavior(String eventName, ClientBehavior behavior) {
		List<ClientBehavior> lst = behaviors.get(eventName);
		if (lst == null) {
			lst = new ArrayList<ClientBehavior>(1);
			behaviors.put(eventName, lst);
		}
		lst.add(behavior);
	}

	@Override
	public Map<String, List<ClientBehavior>> getClientBehaviors() {
		return Collections.unmodifiableMap(behaviors);
	}

	@Override
	public String getDefaultEventName() {
		return "smsSendedEvent";
	}

	@Override
	public Collection<String> getEventNames() {
		ArrayList<String> lst = new ArrayList<String>(1);
		lst.add("smsSendedEvent");
		return Collections.unmodifiableCollection(lst);
	}

	public int getSmsCount() {
		return smsCount;
	}

	public int getCurrentCountDown() {
		return currentCountDown;
	}

}
