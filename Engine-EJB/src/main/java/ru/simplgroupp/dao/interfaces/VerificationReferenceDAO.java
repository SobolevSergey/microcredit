package ru.simplgroupp.dao.interfaces;

import ru.simplgroupp.persistence.VerificationReferenceEntity;
import ru.simplgroupp.transfer.VerificationReference;

import javax.ejb.Local;
import java.util.List;
import java.util.Set;

/**
 * Verification reference DAO
 */
@Local
public interface VerificationReferenceDAO {

    /**
     * Find verification references for rata
     *
     * @param entityName the entity name
     * @return the list of verification references
     */
    List<VerificationReferenceEntity> findForRate(String entityName);

    /**
     * Возвращает все объекты
     *
     * @return список объектов
     */
    List<VerificationReference> findAllReference(Set options);

    /**
     * Сохраняет справочник
     *
     * @param entity сущность
     * @return сохраненную сущность
     */
    VerificationReferenceEntity saveReference(VerificationReferenceEntity entity);

    /**
     * Возвращает сущность справочника по id
     *
     * @param id id в бд
     * @return сущность справочника
     */
    VerificationReferenceEntity getReferenceEntity(int id);

    /**
     * 
     * Возвращает сущность справочника по id
     *
     * @param id id в бд
     * @param options - что инициализируем
     * @return сущность справочника
     */
    VerificationReference getReference(int id, Set options);
    
    /**
     * удаляет запись по id
     * @param id - id в справочнике
     * @return
     */
    boolean removeReference(int id);
}
