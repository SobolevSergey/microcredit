package ru.simplgroupp.webapp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletRequest;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.richfaces.component.SortOrder;
import org.richfaces.model.Arrangeable;
import org.richfaces.model.ArrangeableState;

import ru.simplgroupp.dao.interfaces.ListDAO;
import ru.simplgroupp.interfaces.service.ListService;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.webapp.exception.WebAppException;
import ru.simplgroupp.webapp.util.JSFUtils;

@XmlAccessorType(XmlAccessType.NONE)
abstract public class AbstractListController extends AbstractSessionController implements Serializable 
{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int SEARCH_SHORT = 1;
    public static final int SEARCH_FULL = 2;
    private static final Logger logger = Logger.getLogger(AbstractListController.class.getName());
    protected int searchType = SEARCH_SHORT;
	
	protected StdRequestDataModel model;
	
	@EJB
	protected ListService listServ;
	
	@EJB
	protected ListDAO listDAO;
	
	@PostConstruct
	public void init() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
        if (!facesCtx.isPostback() && !facesCtx.isValidationFailed())
        {				
			ServletRequest preq = (ServletRequest) facesCtx.getExternalContext().getRequest();			
			try {
				initData();
			
				model = createModel();
			} catch (Exception ex) {
				JSFUtils.handleAsError(facesCtx, null, ex);
			}
        }
	}	
	
	@PreDestroy
	public void finit() {
		FacesContext facesCtx = FacesContext.getCurrentInstance();
		
        if (facesCtx!=null&&!facesCtx.isPostback() && !facesCtx.isValidationFailed())
	        {			
			try {
				closeData();
			} catch (Exception e) {
				// TODO logger
			}
        }
	}
	
	public StdRequestDataModel getModel() {
		return model;
	}
	
	public void refreshSearch() {
		model = createModel();
	}
	
	public void closeData() throws Exception {
		
	}
	abstract public void initData() throws Exception;
	abstract public StdRequestDataModel createModel();
	
	public void searchLsn(ActionEvent event) 
	{
		refreshSearch();
	}	
	
	public void switchSearchLsn(ActionEvent event) {
	    if (searchType == SEARCH_SHORT)
	        searchType = SEARCH_FULL;
	    else
	        searchType = SEARCH_SHORT;
	}	
	
	public abstract class StdRequestDataModel<T extends Identifiable>  extends ExtendedDataModel<T> implements Arrangeable
	{

		protected Object rowKey;
		protected ArrangeableState arrangeableState;	
	    
		protected Map<String, SortOrder> sortsOrders;
		protected List<String> sortPriorities;
	 
		protected boolean multipleSorting = false;
	 
		protected static final String SORT_PROPERTY_PARAMETER = "sortProperty";   
		
		protected int rowCount;

		public StdRequestDataModel() {
			super();
			
	        sortsOrders = new HashMap<String, SortOrder>();
	        sortPriorities = new ArrayList<String>();	
	        
	        prepare();
	     
	        // row count
			try {
				rowCount = internalRowCount();
			} catch (WebAppException ex) {
				logger.severe("Произошла ошибка rowcount "+ex);
			}
			
			// row List
			
		}
		
		public void prepare() {
			
		}
		
		@Override
		public void arrange(FacesContext facesCtx, ArrangeableState state) {
			arrangeableState = state;		
		}

	    @Override
	    public void setRowKey(Object key) {
	        rowKey = key;
	    }
	 
	    @Override
	    public Object getRowKey() {
	        return rowKey;
	    }

		@Override
		public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) 
		{
			SequenceRange seqRange = (SequenceRange) range;
			
			SortCriteria[] sorting = new SortCriteria[sortPriorities.size()];
			int i = -1;
			for (String sprop: sortPriorities) 
			{
				SortOrder so = sortsOrders.get(sprop);
				if (so == null || so.equals(SortOrder.unsorted))
					continue;
				
				i++;
				SortCriteria srt = null;
				
				if (so.equals(SortOrder.ascending))
					srt = new SortCriteria(sprop, true);
				else
					srt = new SortCriteria(sprop, false);
				sorting[i] = srt;
			}
			
			try {
				List<T> lst = internalList(seqRange, sorting); 
		        for (T t : lst) {
		            visitor.process(context, t.getId(), argument);
		        }	
			} catch (WebAppException ex) {
				logger.severe("Произошла ошибка walk "+ex);
			}
		
		}
		
		abstract protected List<T> internalList(SequenceRange seqRange, SortCriteria[] sorting) throws WebAppException;
		abstract protected int internalRowCount() throws WebAppException;

		@Override
		public int getRowCount() {
			return rowCount;
		}

		@Override
		public int getRowIndex() {
			return -1;
		}

		@Override
		public Object getWrappedData() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isRowAvailable() {
			return rowKey != null;
		}

		@Override
		public void setRowIndex(int arg0) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWrappedData(Object arg0) {
			throw new UnsupportedOperationException();
			
		}

	    public void modeChanged(ValueChangeEvent event) {
	        reset();
	    }
	 
	    public void reset() {
	        sortPriorities.clear();
	        sortsOrders.clear();
	    }
	 
	    public boolean isMultipleSorting() {
	        return multipleSorting;
	    }
	 
	    public void setMultipleSorting(boolean multipleSorting) {
	        this.multipleSorting = multipleSorting;
	    }
	 
	    public List<String> getSortPriorities() {
	        return sortPriorities;
	    }
	 
	    public Map<String, SortOrder> getSortsOrders() {
	        return sortsOrders;
	    }
	    
	    public void sort() {
	        String property = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
	            .get(SORT_PROPERTY_PARAMETER);
	        if (property != null) {
	            SortOrder currentPropertySortOrder = sortsOrders.get(property);
	            if (multipleSorting) {
	                if (!sortPriorities.contains(property)) {
	                    sortPriorities.add(property);
	                }
	            } else {
	            	sortPriorities.clear();
	                sortsOrders.clear();
	                
	                sortPriorities.add(property);
	            }
	            if (currentPropertySortOrder == null || currentPropertySortOrder.equals(SortOrder.descending)) {
	                sortsOrders.put(property, SortOrder.ascending);
	            } else {
	                sortsOrders.put(property, SortOrder.descending);
	            }
	        }
	    }    
	}	
}