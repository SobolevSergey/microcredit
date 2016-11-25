package ru.simplgroupp.webapp.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

public class DialogListManagerController implements Serializable {

	protected ListManagerController listMgr;
	
	@PostConstruct
	public void init() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		if (!facesCtx.isPostback()) {
			listMgr = (ListManagerController) facesCtx.getExternalContext().getFlash().get("listMgr");
			listMgr.returnOutcome = (String) facesCtx.getExternalContext().getFlash().get("returnOutcome");
		}
	}

	public ListManagerController getListMgr() {
		return listMgr;
	}

	public void setListMgr(ListManagerController listMgr) {
		this.listMgr = listMgr;
	}

}
