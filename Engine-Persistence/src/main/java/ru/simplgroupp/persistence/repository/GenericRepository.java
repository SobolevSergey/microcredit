package ru.simplgroupp.persistence.repository;

import java.io.Serializable;

/**
 * Generic repository
 */
public interface GenericRepository {

    <T> T findOne(Class<T> clazz, Serializable id);
}
