package ru.simplgroupp.ejb;

import ru.simplgroupp.interfaces.AntifraudBeanLocal;
import ru.simplgroupp.persistence.antifraud.AntifraudFieldEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudFieldTypesEntity;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.transfer.AntifraudField;
import ru.simplgroupp.transfer.AntifraudFieldType;
import ru.simplgroupp.transfer.AntifraudPage;

import javax.ejb.*;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(AntifraudBeanLocal.class)
public class AntifraudBean implements AntifraudBeanLocal {

    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

    @Inject Logger logger;

    @Override
    public AntifraudField getAntifraudField(int id, Set options) {

        AntifraudFieldEntity ent = emMicro.find(AntifraudFieldEntity.class, id);

        if (ent == null) {

            return null;

        } else {

            AntifraudField pm = new AntifraudField(ent);
            pm.init(options);
            return pm;
        }
    }

    @Override
    public List<AntifraudField> listAntifraudField(int first, int count, SortCriteria[] sortCriterias,
                                                   Integer creditRequestId) {

        String sql = " from ru.simplgroupp.persistence.antifraud.AntifraudFieldEntity as af" +
                " where af.creditRequestId.id = :creditRequestId";


        Query query = emMicro.createQuery(sql);
        query.setParameter("creditRequestId", creditRequestId);

        if (first > 0)
            query.setFirstResult(first);
        if (count > 0)
            query.setMaxResults(count);

        List<AntifraudFieldEntity> list = query.getResultList();

        if (list.size() > 0) {

            List<AntifraudField> list1 = new ArrayList<>(list.size());

            for (AntifraudFieldEntity antifraudFieldEntity : list) {
                AntifraudField field = new AntifraudField(antifraudFieldEntity);
                list1.add(field);
            }

            return list1;

        } else

            return new ArrayList<>(0);
    }

    @Override
    public int countAntifraudField(Integer creditRequestId) {

        String sql = "select count(*) from ru.simplgroupp.persistence.antifraud.AntifraudFieldEntity af" +
                " where af.creditRequestId.id = :creditRequestId";

        Query query = emMicro.createQuery(sql);
        query.setParameter("creditRequestId", creditRequestId);

        Number res = (Number) query.getSingleResult();
        return res == null ? 0 : res.intValue();
    }

    @Override
    public AntifraudPage getAntifraudPage(int id, Set options) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<AntifraudPage> listAntifraudPage(int first, int count, SortCriteria[] sortCriterias, Integer creditRequestId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int countAntifraudPage(Integer creditRequestId) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<AntifraudFieldType> getAntifraudFieldTypes() {

        String sql = " from ru.simplgroupp.persistence.antifraud.AntifraudFieldTypesEntity order by id";

        Query query = emMicro.createQuery(sql);
        List<AntifraudFieldTypesEntity> list = query.getResultList();

        if (list.size() > 0) {

            List<AntifraudFieldType> list1 = new ArrayList<>(list.size());

            for (AntifraudFieldTypesEntity antifraudFieldTypesEntity : list) {
                AntifraudFieldType type = new AntifraudFieldType(antifraudFieldTypesEntity);
                list1.add(type);
            }

            return list1;

        } else

            return new ArrayList<>(0);
    }

    @Override
    public AntifraudFieldTypesEntity getAntifraudFieldTypesEntityByType(String type) {

        String sql = " from ru.simplgroupp.persistence.antifraud.AntifraudFieldTypesEntity where type = :type";
        Query query = emMicro.createQuery(sql);
        query.setParameter("type", type);
        List<AntifraudFieldTypesEntity> list = query.getResultList();

        return list == null || list.size() != 1 ? null : list.get(0);
    }


    @Override
    public AntifraudFieldTypesEntity getAntifraudFieldTypesEntityById(Integer id) {

        return emMicro.find(AntifraudFieldTypesEntity.class, id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addAntifraudFieldType(String type, String description) {

        AntifraudFieldTypesEntity entity = getAntifraudFieldTypesEntityByType(type);

        if (entity == null) {

            AntifraudFieldTypesEntity newEntity = new AntifraudFieldTypesEntity();
            newEntity.setType(type);
            newEntity.setDescription(description);
            emMicro.persist(newEntity);

        } else {
            logger.severe("Тип поля " + type + " уже существует в базе");
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveAntifraudFieldTypes(List<AntifraudFieldType> antifraudFieldTypeList) {

        if (antifraudFieldTypeList != null) {

            for (AntifraudFieldType antifraudFieldType : antifraudFieldTypeList) {

                AntifraudFieldTypesEntity entity = getAntifraudFieldTypesEntityById(antifraudFieldType.getId());
                entity.setType(antifraudFieldType.getType());
                entity.setDescription(antifraudFieldType.getDescription());
                emMicro.merge(entity);
            }
        }
    }
}
