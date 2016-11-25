package ru.simplgroupp.services;

import ru.simplgroupp.persistence.PeopleMainEntity;

import javax.ejb.Local;

/**
 * People service
 */
@Local
public interface PeopleService {

    PeopleMainEntity findPeople(Integer peopleId);

    PeopleMainEntity createPeople();
}
