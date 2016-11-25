package ru.simplgroupp.interfaces.service;

import ru.simplgroupp.persistence.WorkplaceEntity;

import java.util.List;

/**
 * Workplace service
 */
public interface WorkplaceService {

    List<WorkplaceEntity> findAll();

    void save(List<WorkplaceEntity> workplaces);

    void save(WorkplaceEntity workplace);
}
