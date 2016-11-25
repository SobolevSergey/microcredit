package ru.simplgroupp.ejb.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;

import ru.simplgroupp.dao.interfaces.ListContainerDAO;
import ru.simplgroupp.dao.interfaces.ListDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.service.ListService;
import ru.simplgroupp.persistence.BusinessListEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.IntegerRange;
import ru.simplgroupp.toolkit.common.MoneyRange;
import ru.simplgroupp.util.ListContainer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ListServiceImpl implements ListService {
	
	@EJB
	ListDAO listDAO;
	
	@Override
	public Integer countItems(int listId) {
		return listDAO.countItems(listId);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void loadList(int listId, ListContainer dest) throws ActionException {
		
		BusinessListEntity biz = listDAO.getList(listId);
		Class clzListCon = dest.getClass();
		
		// загружаем условия
		String scond = listDAO.loadCond(listId);
		ListContainer listCon = null;
		try {
			listCon = loadParams(scond, clzListCon);
			dest.copyParams(listCon);
		} catch (JAXBException e) {
			throw new ActionException("Не удалось загрузить список",e);
		}
		
		// загрузить сохранённые данные
		dest.setPrmListId(listId);
		dest.setBizList(biz);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public BusinessListEntity createAndSave(ListContainer listCon, ListContainerDAO dao) throws ActionException {
		return createAndSave(listCon,dao, null, null, 1);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public BusinessListEntity createAndSaveImplicit(ListContainer listCon, ListContainerDAO dao, String subType ) throws ActionException {
		return internalCreateAndSave(listCon, dao, null, subType, 0, true, false);
	}	
	
	protected BusinessListEntity internalCreateAndSave(ListContainer listCon, ListContainerDAO dao, String listName, String subType, Integer isExplicit, boolean bSaveCond, boolean bSaveItems) throws ActionException {
		BusinessListEntity biz = new BusinessListEntity();
		biz.setBusinessObjectClass(listCon.getItemClass().getName());
		if (listName == null && isExplicit == 1) {
			biz.setName(listCon.generateListName());
		} else {
			biz.setName(listName);
		}
		biz.setSubType(subType);
		biz.setIsExplicit(isExplicit);
		biz = listDAO.saveList(biz);
		
		// сохранять условия
		if (bSaveCond) {
			try {
				listCon.nullIfEmpty();
				String sxml = saveParams(listCon);
				listDAO.saveCond(biz.getId(), sxml);
			} catch (Exception e) {
				throw new ActionException("Не удалось сохранить список",e);
			} 
		}
		
		// сохраняем содержание
		if (bSaveItems) {
			int nrec = dao.countData(listCon);
			for (int nStart = 0; nStart < nrec; nStart = nStart + 500) {
				List lstIds = dao.listIds(nStart, 500, listCon);
				listDAO.addListItems(biz, lstIds);
			}
		}
		return biz;
	}	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public BusinessListEntity createAndSave(ListContainer listCon, ListContainerDAO dao, String listName, String subType, Integer isExplicit) throws ActionException {
		return internalCreateAndSave(listCon, dao, listName, subType, isExplicit, true, true);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void addItemToList(BusinessListEntity biz, Integer itemId) {
		listDAO.addListItem(biz.getId(), itemId);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void removeItemFromList(BusinessListEntity biz, Integer itemId) {
		listDAO.removeListItem(biz.getId(), itemId);
	}
	
	private String saveParams(ListContainer listCon) throws JAXBException, IOException {
		String sres = null;
		
        JAXBContext jc = JAXBContext.newInstance(listCon.getClass(), DateRange.class, IntegerRange.class, MoneyRange.class);
        Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);       
        StringWriter wrt = new StringWriter();
        try {
        	marshaller.marshal(listCon, wrt);
        	sres = wrt.toString();
        } finally {
        	wrt.close();
        }
        return sres;
	}
	
	private ListContainer loadParams(String xmlSource, Class clzListCon) throws JAXBException {
		ListContainer listCon = null; 
	
		JAXBContext jaxbContext = JAXBContext.newInstance(clzListCon, DateRange.class, IntegerRange.class, MoneyRange.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		StringReader rdr = new StringReader(xmlSource);
		try {
			listCon = (ListContainer) jaxbUnmarshaller.unmarshal(rdr);
		} finally {
			rdr.close();
		}
		return listCon;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public BusinessListEntity save(BusinessListEntity biz, ListContainer listCon, ListContainerDAO dao)  throws ActionException {
		boolean bExplicit = (biz.getIsExplicit() == 1);
		// если был активен неявный список, делаем его явным
		if (biz.getIsExplicit() == 0) {
			if (StringUtils.isBlank(biz.getName())) {
				biz.setName(listCon.generateListName());
			}
			biz.setIsExplicit(1);
		}
		biz = listDAO.saveList(biz);
		
		// сохранять условия
		try {
			listCon.nullIfEmpty();
			String sxml = saveParams(listCon);
			listDAO.saveCond(biz.getId(), sxml);
		} catch (Exception e) {
			throw new ActionException("Не удалось сохранить список",e);
		} 
		
		// сохраняем содержание
		if (bExplicit) {
			listDAO.clearListItems(biz.getId());
			int nrec = dao.countData(listCon);
			for (int nStart = 0; nStart < nrec; nStart = nStart + 500) {
				List lstIds = dao.listIds(nStart, 500, listCon);
				listDAO.addListItems(biz, lstIds);
			}
		}
		return biz;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)	
	public void removeList(int listId) {
		listDAO.removeList(listId);
	}
}
