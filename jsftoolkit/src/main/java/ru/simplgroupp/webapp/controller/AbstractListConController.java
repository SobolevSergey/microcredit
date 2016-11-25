package ru.simplgroupp.webapp.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.model.SequenceRange;

import ru.simplgroupp.dao.interfaces.ListContainerDAO;
import ru.simplgroupp.dao.interfaces.ListDAO;
import ru.simplgroupp.interfaces.BizActionBeanLocal;
import ru.simplgroupp.interfaces.BizActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.service.ListService;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.util.ListContainer;
import ru.simplgroupp.webapp.exception.WebAppException;

public abstract class AbstractListConController<T extends Identifiable, C extends ListContainer<T>> extends AbstractListController implements ListConController<T,C> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5777511360464531313L;
	
	protected C data;
	
	@EJB
	protected ListDAO listDAO;
	
	@EJB
	protected ListService listService;	
	
	@EJB
	protected BizActionBeanLocal bizBean;
	
	@EJB
	protected BizActionProcessorBeanLocal bizProc;	

	public void prepareControllerParams() {

	}
	abstract public ListContainerDAO<T> getListContainerDAO1();
	abstract protected T modelGetRowObject(Integer rowObjectId);
	
	@Override
	public BizActionBeanLocal getBizBean() {
		return bizBean;
	}
	
	@Override
	public BizActionProcessorBeanLocal getBizProc() {
		return bizProc;
	}
	
	public void clearLsn(ActionEvent event) {
		data.clearParams();
		refreshSearch();
	}	
	
	public class StdRequestConDataModel extends StdRequestDataModel<T> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -1760061947335237548L;

		public StdRequestConDataModel() {
			super();
			prepareControllerParams();
			data.nullIfEmpty();
		}

		@Override
		protected int internalRowCount() throws WebAppException {
			return getListContainerDAO1().countData(data);
		}

		@Override
		protected List<T> internalList(SequenceRange seqRange, SortCriteria[] sorting) throws WebAppException {
			data.setSorting(sorting);
			return getListContainerDAO1().listData(seqRange.getFirstRow(), seqRange.getRows(), data);
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

	public C getData() {
		return data;
	}

	@Override
	public StdRequestDataModel<T> createModel() {
		return new StdRequestConDataModel();
	}
	
	@Override
	public void refreshSearch() {
		model = createModel();
	}
	
	public ListDAO getListDAO() {
		return listDAO;
	}
	public ListService getListService() {
		return listService;
	}	
}
