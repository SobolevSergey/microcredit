package ru.simplgroupp.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ru.simplgroupp.persistence.BusinessListEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.SortCriteria;

public interface ListContainer<T extends Identifiable> {
	public static final Integer NULL_INT_VALUE = -9999;
	public static final String NULL_LABEL = "-- неважно --";	
	
	public static final int FOR_COUNT = 1;
	public static final int FOR_LIST = 2;
	public static final int FOR_IDS = 3;	
	
	abstract public String buildHQL(int bForList);
	abstract public void setHQLParams(Query qry, int bForList);
	abstract public void nullIfEmpty();
	
	public BusinessListEntity getBizList();
	public void setBizList(BusinessListEntity bizList);
	
	public int calcCount(EntityManager em);
	
	public List<T> calcList(int nFirst, int nCount, EntityManager em);
	public List<? extends Number> calcIds(int nFirst, int nCount, EntityManager em);
	public SortCriteria[] getSorting();
	public void setSorting(SortCriteria[] sorting);
	public Set getObjectOptions();
	public void setObjectOptions(Set objectOptions);
	public Map<String, Object> getSortMapping();
	public void setSortMapping(Map<String, Object> sortMapping);
	public Class getItemClass();
	public void setItemClass(Class itemClass);
	public void clearParams();
	public void copyParams(ListContainer<T> source);
	public String generateListName();
	Integer getPrmListId();
	void setPrmListId(Integer prmListId);

}
