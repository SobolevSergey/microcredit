package ru.simplgroupp.dao.impl;

import ru.simplgroupp.dao.interfaces.TmpStorageDAO;
import ru.simplgroupp.persistence.TmpStorageEntity;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TmpStorageDAOImpl implements TmpStorageDAO {
    
	@PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

    @Override
    public void persist(TmpStorageEntity entity) {
        this.emMicro.persist(entity);
    }

    @Override
    public TmpStorageEntity read(String externalKey, String type) {
        TypedQuery<TmpStorageEntity> qry = emMicro.createNamedQuery("findTmpStorageByKeyAndType", TmpStorageEntity.class);
        qry.setParameter("externalKey", externalKey);
        qry.setParameter("type", type);
        List<TmpStorageEntity> resultList = qry.setMaxResults(1).getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    @Override
    public List<TmpStorageEntity> readList(String externalKey, String type) {
        TypedQuery<TmpStorageEntity> qry = emMicro.createNamedQuery("findTmpStorageByKeyAndType", TmpStorageEntity.class);
        qry.setParameter("externalKey", externalKey);
        qry.setParameter("type", type);
        return qry.getResultList();
    }

    @Override
    public void delete(String externalKey, String type) {
        Query qry = emMicro.createNamedQuery("deleteTmpStorageByKeyAndType");
        qry.setParameter("externalKey", externalKey);
        qry.setParameter("type", type);
        qry.executeUpdate();
    }

    @Override
    public void delete(String externalKey) {
        Query qry = emMicro.createNamedQuery("deleteTmpStorageByKey");
        qry.setParameter("externalKey", externalKey);
        qry.executeUpdate();
    }

    @Override
    public void cleanExpired() {
        Query qry = emMicro.createNamedQuery("deleteTmpStorageExpired");
        qry.executeUpdate();
    }

    @Override
    public boolean exists(String externalKey, String type) {
        Query qry = emMicro.createNamedQuery("countTmpStorageByKeyAndType");
        qry.setParameter("externalKey", externalKey);
        qry.setParameter("type", type);
        List resultList = qry.getResultList();

        List lst = qry.getResultList();
        if (lst.size() > 0) {
            Object ares = lst.get(0);
            if (ares == null)
                return false;
            if (ares instanceof Number)
                return (((Number) ares).intValue() > 0);
            else
                return false;
        } else
            return false;
    }
}
