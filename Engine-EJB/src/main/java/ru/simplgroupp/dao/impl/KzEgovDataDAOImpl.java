package ru.simplgroupp.dao.impl;

import ru.simplgroupp.dao.interfaces.KzEgovDataDAO;
import ru.simplgroupp.persistence.KzEgovDataAtsTypeEntity;
import ru.simplgroupp.persistence.KzEgovDataGeonimsTypeEntity;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class KzEgovDataDAOImpl implements KzEgovDataDAO {
	
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    
	@Override
    public KzEgovDataAtsTypeEntity getKzEgovDataAtsTypeEntity(long id) {
		KzEgovDataAtsTypeEntity ent = emMicro.find(KzEgovDataAtsTypeEntity.class, id);
	    return ent;
	}


	@Override
    public KzEgovDataGeonimsTypeEntity getKzEgovDataGeonimsTypeEntity(long id) {
		KzEgovDataGeonimsTypeEntity ent = emMicro.find(KzEgovDataGeonimsTypeEntity.class, id);
		return ent;
	}

}
