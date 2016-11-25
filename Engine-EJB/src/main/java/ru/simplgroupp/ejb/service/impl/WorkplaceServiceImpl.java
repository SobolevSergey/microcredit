package ru.simplgroupp.ejb.service.impl;

import ru.simplgroupp.interfaces.service.WorkplaceService;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.WorkplaceEntity;
import ru.simplgroupp.toolkit.common.Utils;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

/**
 * Workplace service implementation
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class WorkplaceServiceImpl implements WorkplaceService {

	@PersistenceContext(unitName = "MicroPU")
	private EntityManager emMicro;	
	
    @Override
    public List<WorkplaceEntity> findAll() {
        return JPAUtils.getResultListFromSql(emMicro, "from ru.simplgroupp.persistence.WorkplaceEntity order by name", Utils.mapOf());
        	
    }

    @Override
    @TransactionAttribute
    public void save(List<WorkplaceEntity> workplaces) {
        for (WorkplaceEntity workplace:workplaces){
        	save(workplace);
        }
    }

    @Override
    @TransactionAttribute
    public void save(WorkplaceEntity workplace) {
        WorkplaceEntity workPlace=emMicro.merge(workplace);
        emMicro.persist(workPlace);
    }
}
