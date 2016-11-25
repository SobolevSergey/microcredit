package ru.simplgroupp.webapp.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;

public abstract class AbstractSessionController extends AbstractController 
{
	@ManagedProperty(name = "userCtl", value = "#{userData}")
	protected UserDataController userCtl;

	public UserDataController getUserCtl() {
		return userCtl;
	}

	public void setUserCtl(UserDataController userCtl) {
		this.userCtl = userCtl;
	}
	
	@PostConstruct
	public void init() {
		checkSecurityFeatures();
	}
	
	public void checkSecurityFeatures() {
		if (userCtl == null) {
			return;
		}
		
		SecurityFeatures ant = this.getClass().getAnnotation(SecurityFeatures.class);
		if (ant == null) {
			return;
		}
		
		if (!userCtl.isFeaturesEnabled(ant.value(), ant.all()) ) {
			throw new RuntimeException("у вас нет необходимых прав доступа");
		}
	}
}
