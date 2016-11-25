package ru.simplgroupp.dao.interfaces;

import ru.simplgroupp.persistence.TmpStorageEntity;

import javax.ejb.Local;
import java.io.Serializable;
import java.util.List;

@Local
public interface TmpStorageDAO extends Serializable {
    void persist(TmpStorageEntity entity);

    TmpStorageEntity read(String externalKey, String type);

    List<TmpStorageEntity> readList(String externalKey, String type);

    void delete(String externalKey, String type);

    void delete(String externalKey);

    void cleanExpired();

    boolean exists(String externalKey, String type);
}