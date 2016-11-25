package ru.simplgroupp.interfaces.service;

import java.util.List;

import javax.ejb.Local;

import ru.simplgroupp.persistence.UserWorkplaceHistoryEntity;

@Local
public interface WorkplaceHistoryService {

	List<UserWorkplaceHistoryEntity> findAll();

    void save(List<UserWorkplaceHistoryEntity> workplaces);

    void save(UserWorkplaceHistoryEntity workplace);
}
