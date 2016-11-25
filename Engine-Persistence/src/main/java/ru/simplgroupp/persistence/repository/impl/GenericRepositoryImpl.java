package ru.simplgroupp.persistence.repository.impl;

import ru.simplgroupp.persistence.repository.GenericRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Generic repository implementation
 */
@Named
public class GenericRepositoryImpl implements GenericRepository {

    @Inject
    private EntityManager entityManager;

    @Override
    public <T> T findOne(Class<T> clazz, Serializable id) {
        return entityManager.find(clazz, id);
    }
}
