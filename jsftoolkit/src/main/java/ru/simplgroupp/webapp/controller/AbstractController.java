package ru.simplgroupp.webapp.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;

import ru.simplgroupp.interfaces.ServiceBeanLocal;

public class AbstractController {
	
	@ManagedProperty(name = "app", value = "#{app}")
	protected transient AppController app;
	
	protected Logger log = Logger.getLogger(this.getClass().getName());
	
	@EJB
	protected ServiceBeanLocal servBean;
	
	protected Set<IEventListener> eventListeners;
	
	public AppController getApp() {
		return app;
	}

	public void setApp(AppController app) {
		this.app = app;
	}
	
	public void afterEvent(String eventName, Map<String,Object> params) {
		servBean.firedEvent(eventName, params);
	}
	
	public void setEventListener(IEventListener value) {
		if (eventListeners == null) {
			eventListeners = new HashSet<IEventListener>(1);
		}
		if (value != null) {
			eventListeners.add(value);
		}
	}
	
	public void afterEvent(String eventName) {
		if (eventListeners == null) {
			return;
		}
		for (IEventListener lsn: eventListeners) {
			try {
				lsn.eventFired(eventName, this);
			} catch (Exception e) {
				log.log(Level.SEVERE, "Ошибка при обработке события " + eventName, e);
			}
		}
	}
}
