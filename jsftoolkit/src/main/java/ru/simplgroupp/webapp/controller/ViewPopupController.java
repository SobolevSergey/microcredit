package ru.simplgroupp.webapp.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.richfaces.component.UIPopupPanel;

public class ViewPopupController implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1782914434456577725L;
	protected UIPopupPanel popupIndicator;
	
	@PostConstruct
	public void init() {
		if (popupIndicator != null)
			popupIndicator.setShow(false);
	}
	
	public UIPopupPanel getPopupIndicator() {
		return popupIndicator;
	}

	public void setPopupIndicator(UIPopupPanel popupIndicator) {
		this.popupIndicator = popupIndicator;
	}	
}
