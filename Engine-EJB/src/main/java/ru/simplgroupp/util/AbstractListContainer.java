package ru.simplgroupp.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import ru.simplgroupp.interfaces.RuntimeServices;
import ru.simplgroupp.persistence.BusinessListEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.SortCriteria;

@XmlTransient
abstract public class AbstractListContainer<T extends Identifiable> implements ListContainer<T>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8620377284046408502L;
	
	@XmlTransient
	protected Class itemClass;
	
	@XmlTransient
	protected SortCriteria[] sorting = null;
	
	@XmlTransient
	protected Set objectOptions = null;
	
	@XmlTransient
	protected Map<String, Object> sortMapping = new HashMap<String, Object>(0);
	
	@XmlTransient
	protected BusinessListEntity bizList;
	
	protected Integer prmListId; 
	
	@XmlTransient
	protected RuntimeServices runServ;
	
	abstract public String buildHQL(int bForList);
	abstract public void setHQLParams(Query qry, int bForList);
	abstract public void nullIfEmpty();
	abstract public void prepareParams();
	abstract public void clearParams();
	/**
	 * Копирует себе параметры из списка source
	 * @param source
	 */
	abstract public void copyParams(ListContainer<T> source);
	abstract public String generateListName();
	
	public int calcCount(EntityManager em) {
		prepareParams();
		String hql = buildHQL(ListContainer.FOR_COUNT);
		
		Query qry = em.createQuery(hql);
		setHQLParams(qry,ListContainer.FOR_COUNT);
		List lstRes = qry.getResultList();
		if (lstRes.size() == 0) {
			return 0;
		} else {
			if (lstRes.get(0) == null) {
				return 0;
			} else {
				Number nres = (Number) lstRes.get(0);
				return nres.intValue();
			}
		}
	}
	
	public List<? extends Number> calcIds(int nFirst, int nCount, EntityManager em) {
		prepareParams();
		String hql = buildHQL(ListContainer.FOR_IDS);
        
		Query qry = em.createQuery(hql);
		setHQLParams(qry,ListContainer.FOR_LIST);		
		
	    if (nFirst >= 0)
			qry.setFirstResult(nFirst);
		if (nCount > 0)
			qry.setMaxResults(nCount);
			
		List lst=qry.getResultList();		
		return lst;
	}
	
	public List<T> calcList(int nFirst, int nCount, EntityManager em) {
		prepareParams();
		String hql = buildHQL(ListContainer.FOR_LIST);
        hql = hql + SearchUtil.sortToString(sortMapping, sorting);
        String sortSelect = SearchUtil.sortSelectToString(sortMapping, sorting);
        hql = hql.replace("[$SELECT_SORTING$]", sortSelect);
        
		Query qry = em.createQuery(hql);
		setHQLParams(qry,ListContainer.FOR_LIST);		
		
	    if (nFirst >= 0)
			qry.setFirstResult(nFirst);
		if (nCount > 0)
			qry.setMaxResults(nCount);
			
		List lst=qry.getResultList();
		if (lst.size() > 0){
			List<T> lstRes = new ArrayList<T>(lst.size());
			if (hasSorting()) {				
				for (Object row: lst) {
					Object pmain = wrapEntity(((Object[]) row)[0]);
					
					if ( (pmain instanceof Initializable) && objectOptions != null && objectOptions.size() > 0) {
		    			((Initializable)pmain).init(objectOptions);
		    		}
					lstRes.add((T) pmain);
				}
			} else {
				for (Object pmain:lst){
					pmain = wrapEntity(pmain);
					if ( (pmain instanceof Initializable) && objectOptions != null && objectOptions.size() > 0) {
		    			((Initializable)pmain).init(objectOptions);
		    		}
					lstRes.add((T) pmain);
				}
			}
			return lstRes;
		} else {
			return new ArrayList<T>(0);
		}
	}
	
	protected abstract T wrapEntity(Object entity);
	
	public boolean hasSorting() {
		return (sorting != null && sorting.length > 0 && sorting[0] != null);
	}
	public SortCriteria[] getSorting() {
		return sorting;
	}
	public void setSorting(SortCriteria[] sorting) {
		this.sorting = sorting;
	}
	public Set getObjectOptions() {
		return objectOptions;
	}
	public void setObjectOptions(Set objectOptions) {
		this.objectOptions = objectOptions;
	}
	public Map<String, Object> getSortMapping() {
		return sortMapping;
	}
	public void setSortMapping(Map<String, Object> sortMapping) {
		this.sortMapping = sortMapping;
	}
	
	public RuntimeServices getRuntimeServices() {
		return runServ;
	}
	
	public void setRuntimeServices(RuntimeServices value) {
		runServ = value;
	}
	
	@Override
	public Class getItemClass() {
		return itemClass;
	}
	
	@Override
	public void setItemClass(Class itemClass) {
		this.itemClass = itemClass;
	}
	public BusinessListEntity getBizList() {
		return bizList;
	}
	public void setBizList(BusinessListEntity bizList) {
		this.bizList = bizList;
	}
	
	@Override
	@XmlElement
	public Integer getPrmListId() {
		return prmListId;
	}
	@Override
	public void setPrmListId(Integer prmListId) {
		this.prmListId = prmListId;
	}
}
