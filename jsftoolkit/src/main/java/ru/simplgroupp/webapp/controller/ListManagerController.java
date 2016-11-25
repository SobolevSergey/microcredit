package ru.simplgroupp.webapp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.ListDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.BizActionBeanLocal;
import ru.simplgroupp.interfaces.service.ListService;
import ru.simplgroupp.persistence.BizActionParamEntity;
import ru.simplgroupp.persistence.BusinessListEntity;
import ru.simplgroupp.rules.AbstractContext;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.BizAction;
import ru.simplgroupp.util.AppUtil;
import ru.simplgroupp.util.BizActionKeys;
import ru.simplgroupp.util.BizActionUtils;
import ru.simplgroupp.webapp.util.JSFUtils;

import org.primefaces.context.RequestContext;

public class ListManagerController extends AbstractSessionController implements Serializable {
	
	protected String listName;
	protected Integer selectedListId;
	protected List<BusinessListEntity> lists;
	protected ListConController listCtl;
	protected BizAction bizAction;
	protected List<Parameter> bizActionParams;
	protected String subType;	
	protected List<BizAction> bizActions;
	protected String returnOutcome;
	
	public void init() {
		reloadBizActions();	
	}
	
	protected void reloadBizActions() {
		if (listCtl.getData().getBizList() == null) {
			bizActions = new ArrayList(0);
		} else {
			List<BizAction> lst = listCtl.getBizBean().listBizActions(0, -1, null, null, null, null, listCtl.getData().getBizList().getBusinessObjectClass(), null, 1, null, null, null, null, false, null);
			bizActions = new ArrayList(lst.size());
			for (BizAction biz: lst) {
				if (biz.getIsActive() == ActiveStatus.ACTIVE && BizActionUtils.canExecute(biz, this.userCtl.getUser())) { 
					bizActions.add(biz);
				}
			}
		}		
	}
	
	public void close() {
		// удаляем неявные списки
		/*
		if (listCtl.getData().getBizList() != null && listCtl.getData().getBizList().getIsExplicit() == 0) {
			ListService listServ = listCtl.getListService();
			listServ.removeList(listCtl.getData().getBizList().getId());
			listCtl.getData().setBizList(null);
		}
		*/
		return;
	}

	public ListConController getListCtl() {
		return listCtl;
	}

	public void setListCtl(ListConController listCtl) {
		this.listCtl = listCtl;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}
	
	public String getListNameSaved() {
		BusinessListEntity lst = listCtl.getData().getBizList();
		if (lst == null) {
			return null;
		} else {
			return lst.getName();
		}
	}
	
	public void prepareSaveAsLsn(ActionEvent event) {
		listName = listCtl.getData().generateListName();
	}
	
	public String prepareSaveAs() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		UIComponent uic = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());    
		
		listName = listCtl.getData().generateListName();		
		facesCtx.getExternalContext().getFlash().put("listMgr", this);
		String returnOutcome = (String) uic.getAttributes().get("returnOutcome");
		if (StringUtils.isBlank(returnOutcome)) {
			returnOutcome = facesCtx.getExternalContext().getRequestPathInfo();
		}
		facesCtx.getExternalContext().getFlash().put("returnOutcome", returnOutcome);
		
		return "/views/include/dlgSaveAs.xhtml?faces-redirect=true";
	}	
	
	public String prepareLoad() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		UIComponent uic = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());    
		
		String className = listCtl.getData().getItemClass().getName();
		lists = listCtl.getListDAO().findLists(className, subType, 1);
		selectedListId = null;	
		
		facesCtx.getExternalContext().getFlash().put("listMgr", this);
		String returnOutcome = (String) uic.getAttributes().get("returnOutcome");
		if (StringUtils.isBlank(returnOutcome)) {
			returnOutcome = facesCtx.getExternalContext().getRequestPathInfo();
		}
		facesCtx.getExternalContext().getFlash().put("returnOutcome", returnOutcome);
		
		return "/views/include/dlgLoad.xhtml?faces-redirect=true";
	}	
	
	public void prepareLoadLsn(ActionEvent event) {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		
		String className = listCtl.getData().getItemClass().getName();
		lists = listCtl.getListDAO().findLists(className, subType, 1);
		selectedListId = null;

		// TODO
	}
	
	public String executeBizAction() {
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		
		try {
			AbstractContext ctx = AppUtil.getDefaultContext(null, null);
			for (Parameter prm: bizActionParams) {
				ctx.getParams().put(BizActionKeys.PREFIX_PARAM_CONTEXT + prm.info.getCode(), prm.getValue());
			}
			listCtl.getBizProc().executeBizActionAsync(bizAction.getEntity(), listCtl.getData().getBizList().getId(), false, ctx);
		} catch (ActionException e) {
			JSFUtils.handleAsError(facesCtx, "txtQuickListName", new Exception("Ошибка при выполнении"));
			return null;	
		}
		
		int listId = 0;
		if (listCtl.getData().getBizList() == null) {
			return returnOutcome + "?faces-redirect=true";
		} else {
			listId = listCtl.getData().getBizList().getId();
			facesCtx.getExternalContext().getFlash().put(subType + "prmListId", listId);
			return returnOutcome + "?" + subType + "prmListId="+ listId +"faces-redirect=true";					
		}
	}	
	
	public Integer getBizListItemCount() {
		if (listCtl.getData().getBizList() == null || listCtl.getData().getBizList().getId() == null) {
			return null;
		} else {			
			Integer n = listCtl.getListService().countItems( listCtl.getData().getBizList().getId());
			return n;
		}
	}	
	
	public String getCurrentPath() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		String surl = facesCtx.getExternalContext().getRequestPathInfo();
		return surl;
	}
	
	public void executeBizActionLsn(ActionEvent event) {
		
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		
		try {
			AbstractContext ctx = AppUtil.getDefaultContext(null, null);
			for (Parameter prm: bizActionParams) {
				ctx.getParams().put(BizActionKeys.PREFIX_PARAM_CONTEXT + prm.info.getCode(), prm.getValue());
			}
			listCtl.getBizProc().executeBizActionAsync(bizAction.getEntity(), listCtl.getData().getBizList().getId(), false, ctx);
		} catch (ActionException e) {
			JSFUtils.handleAsError(facesCtx, "txtQuickListName", new Exception("Ошибка при выполнении"));
			return;	
		}
		// TODO
	}
	
	public String prepareBizAction() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		UIComponent uic = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());
		
		Number bizId = (Number) uic.getAttributes().get("bizActionId");
		bizAction = listCtl.getBizBean().getBizAction(bizId.intValue(), null);
		
		bizActionParams = new ArrayList<Parameter>(bizAction.getEntity().getParams().size());
		for (BizActionParamEntity prm: bizAction.getEntity().getParams()) {
			Parameter aparam = new Parameter();
			aparam.info = prm;
			aparam.value = null;
			bizActionParams.add(aparam);
		}
		
		facesCtx.getExternalContext().getFlash().put("listMgr", this);
		String returnOutcome = (String) uic.getAttributes().get("returnOutcome");
		if (StringUtils.isBlank(returnOutcome)) {
			returnOutcome = facesCtx.getExternalContext().getRequestPathInfo();
		}
		facesCtx.getExternalContext().getFlash().put("returnOutcome", returnOutcome);		
		
		return "/views/include/dlgBizAction.xhtml?faces-redirect=true";
	}	
	
	public void prepareBizActionLsn(ActionEvent event) {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		
		Number bizId = (Number) event.getComponent().getAttributes().get("bizActionId");
		bizAction = listCtl.getBizBean().getBizAction(bizId.intValue(), null);
		
		bizActionParams = new ArrayList<Parameter>(bizAction.getEntity().getParams().size());
		for (BizActionParamEntity prm: bizAction.getEntity().getParams()) {
			Parameter aparam = new Parameter();
			aparam.info = prm;
			aparam.value = null;
			bizActionParams.add(aparam);
		}
	}

	public String dummy() {
		return null;
	}
	
	public void loadImplicitList() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		try {
			ListService listServ = listCtl.getListService();
			BusinessListEntity lst = listServ.createAndSaveImplicit(listCtl.getData(), listCtl.getListContainerDAO1(), subType);
			listCtl.getData().setBizList(lst);
			reloadBizActions();	
		} catch (ActionException ex) {
			JSFUtils.handleAsError(facesCtx, null, ex);
		}		
	}
	
	public void loadList(int listId) {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		selectedListId = listId;
		try {
			listCtl.getListService().loadList(listId, listCtl.getData());
			reloadBizActions();	
			listCtl.refreshSearch();
		} catch (ActionException e) {
			JSFUtils.handleAsError(facesCtx, null, e);
		}		
	}
	
	public void loadLsn(ActionEvent event) 
	{
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		if (selectedListId == null) {
			JSFUtils.handleAsError(facesCtx, "txtQuickListName", new Exception("Необходимо выбрать список"));
			return;			
		}
		
		try {
			listCtl.getListService().loadList(selectedListId, listCtl.getData());
			reloadBizActions();	
			listCtl.refreshSearch();
		} catch (ActionException e) {
			JSFUtils.handleAsError(facesCtx, null, e);
		}

		
	}
	
	public String load() 
	{
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		if (selectedListId == null) {
			JSFUtils.handleAsError(facesCtx, "txtQuickListName", new Exception("Необходимо выбрать список"));
			return null;			
		}
		
		try {
			listCtl.getListService().loadList(selectedListId, listCtl.getData());
			reloadBizActions();	
			listCtl.refreshSearch();
		} catch (ActionException e) {
			JSFUtils.handleAsError(facesCtx, null, e);
			return null;
		}

		facesCtx.getExternalContext().getFlash().put(subType + "prmListId", selectedListId);
		return returnOutcome + "?" + subType + "prmListId="+ selectedListId +"faces-redirect=true";		
	}
	
	public String saveAs() 
	{
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		if (StringUtils.isBlank(listName)) {
			JSFUtils.handleAsError(facesCtx, "txtQuickListName", new Exception("Необходимо заполнить название списка"));
			return null;
		}
		
		int listId = 0;
		if (listCtl.getData().getBizList() == null || listCtl.getData().getBizList().getIsExplicit() == 1) {
			try {
				ListService listServ = listCtl.getListService();
				BusinessListEntity lst = listServ.createAndSave(listCtl.getData(), listCtl.getListContainerDAO1(), listName, subType, 1);
				listId = lst.getId();
				listCtl.getData().setBizList(lst);
				reloadBizActions();	
			} catch (ActionException ex) {
				JSFUtils.handleAsError(facesCtx, null, ex);
				return null;
			}
		} else {
			listCtl.getData().getBizList().setName(listName);
			try {
				listCtl.getData().setBizList(listCtl.getListService().save(listCtl.getData().getBizList(), listCtl.getData(), listCtl.getListContainerDAO1()));
				listId = listCtl.getData().getBizList().getId();
				reloadBizActions();					
			} catch (ActionException ex) {
				JSFUtils.handleAsError(facesCtx, null, ex);
				return null;
			}
		}
		facesCtx.getExternalContext().getFlash().put(subType + "prmListId", listId);
		return returnOutcome + "?" + subType + "prmListId="+ listId +"faces-redirect=true";
	}
	
	public void clearLsn(ActionEvent event) {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		if (listCtl.getData().getBizList() == null || listCtl.getData().getBizList().getIsExplicit() == 0) {
			return;
		}
		
		try {
			ListService listServ = listCtl.getListService();
			BusinessListEntity lst = listServ.createAndSaveImplicit(listCtl.getData(), listCtl.getListContainerDAO1(), subType);			
			listCtl.getData().setBizList(lst);
			reloadBizActions();
		} catch (Exception ex) {
			JSFUtils.handleAsError(facesCtx, null, ex);
			return;			
		}
	}
	
	public boolean getExplicitList() {
		return (listCtl.getData().getBizList() != null && listCtl.getData().getBizList().getIsExplicit() == 1);
	}
	
	public String cancel() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		
		int listId = 0;
		if (listCtl.getData().getBizList() == null) {
			return returnOutcome + "?faces-redirect=true";
		} else {
			listId = listCtl.getData().getBizList().getId();
			facesCtx.getExternalContext().getFlash().put(subType + "prmListId", listId);
			return returnOutcome + "?" + subType + "prmListId="+ listId +"faces-redirect=true";					
		}
	}
	
	public void saveAsLsn(ActionEvent event) 
	{
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		if (StringUtils.isBlank(listName)) {
			JSFUtils.handleAsError(facesCtx, "txtQuickListName", new Exception("Необходимо заполнить название списка"));
			return;
		}
		
		if (listCtl.getData().getBizList() == null || listCtl.getData().getBizList().getIsExplicit() == 1) {
			try {
				ListService listServ = listCtl.getListService();
				BusinessListEntity lst = listServ.createAndSave(listCtl.getData(), listCtl.getListContainerDAO1(), listName, subType, 1);
				listCtl.getData().setBizList(lst);
				reloadBizActions();	
			} catch (ActionException ex) {
				JSFUtils.handleAsError(facesCtx, null, ex);
			}
		} else {
			listCtl.getData().getBizList().setName(listName);
			try {
				listCtl.getData().setBizList(listCtl.getListService().save(listCtl.getData().getBizList(), listCtl.getData(), listCtl.getListContainerDAO1()));
				reloadBizActions();					
			} catch (ActionException ex) {
				JSFUtils.handleAsError(facesCtx, null, ex);
			}
		}
	}
	
	public List<BusinessListEntity> getLists() {
		return lists;
	}

	public Integer getSelectedListId() {
		return selectedListId;
	}

	public void setSelectedListId(Integer selectedListId) {
		this.selectedListId = selectedListId;
	}	
	
	public List<BizAction> getBizActions() {
		return bizActions;		
	}

	public BizAction getBizAction() {
		return bizAction;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}
	
	public class Parameter {
		BizActionParamEntity info;
		Object value;

		public BizActionParamEntity getInfo() {
			return info;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}
	}

	public List<Parameter> getBizActionParams() {
		return bizActionParams;
	}

	public String getReturnOutcome() {
		return returnOutcome;
	}

	public void setReturnOutcome(String returnOutcome) {
		this.returnOutcome = returnOutcome;
	}
}
