package ru.simplgroupp.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.model.SequenceRange;
import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.ListContainerDAO;
import ru.simplgroupp.dao.interfaces.ListDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.BizActionBeanLocal;
import ru.simplgroupp.interfaces.BizActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.service.ListService;
import ru.simplgroupp.persistence.BusinessListEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.util.ListContainer;
import ru.simplgroupp.webapp.exception.WebAppException;
import ru.simplgroupp.webapp.util.JSFUtils;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.richfaces.component.SortOrder;
import org.richfaces.event.ItemChangeEvent;

/**
 * контроллер для поиска в случае двойного поиска - краткого и полного
 * @author irina
 *
 * @param <T>
 * @param <C>
 * @param <Q>
 */
public abstract class AbstractListConFQController<T extends Identifiable, C extends ListContainer<T>, Q extends ListContainer<T>> extends AbstractListController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5777511360464531313L;
	
	@EJB
	protected ListDAO listDAO;
	
	@EJB
	protected ListService listService;
	
	@EJB
	protected BizActionBeanLocal bizBean;	
	
	@EJB
	protected BizActionProcessorBeanLocal bizProc;
	
	protected C data;
	protected Q quick;
	protected Boolean searchFull = false;
	
	protected ListManagerController quickManager;
	protected ListManagerController dataManager;

	public void prepareControllerParams() {

	}
	abstract public ListContainerDAO<T> getListContainerDAO();
	abstract protected T modelGetRowObject(Integer rowObjectId);
	
	public void initData() throws Exception {
		initManagers();
	}
	
	public void initManagers() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		
		Integer quickPrmListId = null;
		String ss = facesCtx.getExternalContext().getRequestParameterMap().get("quickprmListId");
		if (StringUtils.isBlank(ss)) {
			quickPrmListId = (Integer) facesCtx.getExternalContext().getFlash().get("quickprmListId");
		} else {
			quickPrmListId = Integer.parseInt(ss);
		}
		quickManager = new ListManagerController();
		quickManager.setUserCtl(this.getUserCtl());
		quickManager.setListCtl(new LCController(quick));
		quickManager.setSubType("quick");
		if (quickPrmListId != null) {
			quickManager.loadList(quickPrmListId);
		} else {
			quickManager.loadImplicitList();
		}
		quickManager.init();
		
		Integer fullPrmListId = null;
		ss = facesCtx.getExternalContext().getRequestParameterMap().get("fullprmListId");
		if (StringUtils.isBlank(ss)) {
			fullPrmListId = (Integer) facesCtx.getExternalContext().getFlash().get("fullprmListId");
		} else {
			fullPrmListId = Integer.parseInt(ss);
		}		
		dataManager = new ListManagerController();
		dataManager.setUserCtl(this.getUserCtl());
		dataManager.setListCtl(new LCController(data));
		dataManager.setSubType("full");
		if (fullPrmListId != null) {
			dataManager.loadList(fullPrmListId);
		} else {
			dataManager.loadImplicitList();
		}
		dataManager.init();
	}
	
	public void closeData() throws Exception {
		closeManagers();
	}
	
	public void closeManagers() {
		quickManager.close();
		dataManager.close();
	}
	
	class LCController implements ListConController {

		private ListContainer<T> listCont;
		
		public LCController(ListContainer<T> lc) {
			listCont = lc;
		}
		
		@Override
		public ListContainer getData() {
			return listCont;
		}

		@Override
		public ListDAO getListDAO() {
			return listDAO;
		}

		@Override
		public ListService getListService() {
			return listService;
		}

		@Override
		public ListContainerDAO getListContainerDAO1() {
			return getListContainerDAO();
		}

		@Override
		public void refreshSearch() {
			searchFull = (listCont == data);
			AbstractListConFQController.this.refreshSearch();
		}

		@Override
		public BizActionBeanLocal getBizBean() {
			return bizBean;
		}

		@Override
		public BizActionProcessorBeanLocal getBizProc() {
			return bizProc;
		}
		
	}
	
	public void tabChangeLsn(ItemChangeEvent event) {
		String sfNew = (String) event.getNewItem().getAttributes().get("searchFull");
		searchFull = "true".equalsIgnoreCase(sfNew);
	}
	
	public boolean isListExists() {
		return (searchFull && data.getBizList() != null) || ((! searchFull) && quick.getBizList() != null);
	}
	
	public boolean inList(Integer itemId) {
		ListContainer lc=  null;
		if (searchFull) {
			lc = data;
		} else {
			lc = quick;
		}
		if (lc.getBizList() == null) {
			return false;
		}
		boolean bRes = listDAO.isItemInList(lc.getBizList().getId(), itemId);
		return bRes;
	}
	
	public String dummy() {
		return null;
	}	
	
	public void searchLsn(ActionEvent event) 
	{
		searchFull = true;
		data.setPrmListId(null);
		quick.setPrmListId(null);
		refreshSearch();
	}
	
	public void searchQuickLsn(ActionEvent event) 
	{
		searchFull = false;
		data.setPrmListId(null);
		quick.setPrmListId(null);		
		refreshSearch();
	}	
	
	public void clearLsn(ActionEvent event) {
		searchFull = true;
		data.clearParams();
		refreshSearch();
	}	
	
	public void clearQuickLsn(ActionEvent event) {
		searchFull = false;
		quick.clearParams();
		refreshSearch();
	}
	
	public void addToListLsn(ActionEvent event) 
	{
		Number nid = (Number) event.getComponent().getAttributes().get("listItemId");

		ListContainer lc=  null;
		if (searchFull) {
			lc = data;
		} else {
			lc = quick;
		}
		
		listServ.addItemToList(lc.getBizList(), nid.intValue());
	}
	
	public void switchFromListLsn(ActionEvent event) 
	{
		Number nid = (Number) event.getComponent().getAttributes().get("listItemId");
		
		ListContainer lc=  null;
		if (searchFull) {
			lc = data;
		} else {
			lc = quick;
		}
		
		boolean bRes = listDAO.isItemInList(lc.getBizList().getId(), nid.intValue());
		if (bRes) {
			listServ.removeItemFromList(lc.getBizList(), nid.intValue());
		} else {
			listServ.addItemToList(lc.getBizList(), nid.intValue());
		}
	}	
	
	public class StdRequestConDataModel extends StdRequestDataModel<T> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -1760061947335237548L;

		public StdRequestConDataModel() {
			super();
		}
		
		@Override
		public void prepare() {
			prepareControllerParams();
			if (searchFull) {
				data.nullIfEmpty();
				for (SortCriteria cr: data.getSorting()) {
					sortPriorities.add(cr.getExpression());
					sortsOrders.put(cr.getExpression(), cr.getIsAscending()?SortOrder.ascending:SortOrder.descending );
				}
			} else {
				quick.nullIfEmpty();
				for (SortCriteria cr: quick.getSorting()) {
					sortPriorities.add(cr.getExpression());
					sortsOrders.put(cr.getExpression(), cr.getIsAscending()?SortOrder.ascending:SortOrder.descending );
				}				
			}			
		}

		@Override
		protected int internalRowCount() throws WebAppException {
			if (searchFull) {
				return getListContainerDAO().countData(data);
			} else {
				return getListContainerDAO().countData(quick);
			}
		}

		@Override
		protected List<T> internalList(SequenceRange seqRange, SortCriteria[] sorting) throws WebAppException {
			if (searchFull) {
				data.setSorting(sorting);
				return getListContainerDAO().listData(seqRange.getFirstRow(), seqRange.getRows(), data);
			} else {
				quick.setSorting(sorting);
				return getListContainerDAO().listData(seqRange.getFirstRow(), seqRange.getRows(), quick);				
			}
		}

		@Override
		public T getRowData() {
			if (rowKey == null) {
				return null;
			} else {
				return (T) modelGetRowObject((Integer) rowKey);
			}
		}

	}
	
	public Q getQuick() {
		return quick;
	}	

	public C getData() {
		return data;
	}

	@Override
	public StdRequestDataModel<T> createModel() {
		return new StdRequestConDataModel();
	}
	public Boolean getSearchFull() {
		return searchFull;
	}

	public void setSearchFull(Boolean searchFull) {
		this.searchFull = searchFull;
	}
	public ListManagerController getQuickManager() {
		return quickManager;
	}
	public ListManagerController getDataManager() {
		return dataManager;
	}
	public ListDAO getListDAO() {
		return listDAO;
	}
	public ListService getListService() {
		return listService;
	}
	
}
