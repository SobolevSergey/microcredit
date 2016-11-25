package ru.simplgroupp.webapp.controller;

import javax.ejb.EJB;

import ru.simplgroupp.dao.interfaces.ListContainerDAO;
import ru.simplgroupp.dao.interfaces.ListDAO;
import ru.simplgroupp.interfaces.BizActionBeanLocal;
import ru.simplgroupp.interfaces.BizActionProcessorBeanLocal;
import ru.simplgroupp.interfaces.service.ListService;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.util.ListContainer;

public interface ListConController<T extends Identifiable, C extends ListContainer<T>> {
	public C getData();
	public ListContainerDAO<T> getListContainerDAO1();
	public ListDAO getListDAO();
	public ListService getListService();
	public void refreshSearch();
	public BizActionBeanLocal getBizBean();
	public BizActionProcessorBeanLocal getBizProc();
}
