package ru.simplgroupp.persistence.config;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * CDI config
 */
public class CdiConfig {

    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

    @Produces
    @RequestScoped
    public EntityManager getEntityManagerMicro() {
        return emMicro;
    }
}
