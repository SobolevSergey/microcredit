package ru.simplgroupp.ejb.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ru.simplgroupp.interfaces.service.WorkplaceHistoryService;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.UserWorkplaceHistoryEntity;
import ru.simplgroupp.toolkit.common.Utils;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class WorkplaceHistoryServiceImpl implements WorkplaceHistoryService{

	@PersistenceContext(unitName = "MicroPU")
	private EntityManager emMicro;	
	
	@Override
	public List<UserWorkplaceHistoryEntity> findAll() {
		return JPAUtils.getResultListFromSql(emMicro, "from ru.simplgroupp.persistence.UserWorkplaceHistoryEntity ", Utils.mapOf());
	}

	@Override
	@TransactionAttribute
	public void save(List<UserWorkplaceHistoryEntity> workplaces) {
		 for (UserWorkplaceHistoryEntity workplace:workplaces){
	        	save(workplace);
	     }
		
	}

	@Override
	@TransactionAttribute
	public void save(UserWorkplaceHistoryEntity workplace) {
		UserWorkplaceHistoryEntity workPlace=emMicro.merge(workplace);
        emMicro.persist(workPlace);
		
	}

}
