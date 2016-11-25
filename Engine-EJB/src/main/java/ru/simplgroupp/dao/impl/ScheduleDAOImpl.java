package ru.simplgroupp.dao.impl;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ru.simplgroupp.dao.interfaces.ScheduleDAO;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ScheduleDAOImpl implements ScheduleDAO {
	
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    
}
