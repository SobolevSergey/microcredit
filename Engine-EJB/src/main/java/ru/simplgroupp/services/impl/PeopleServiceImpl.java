package ru.simplgroupp.services.impl;

import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.repository.PeopleMainRepository;
import ru.simplgroupp.services.PeopleService;

import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;

/**
 * People service implementation
 */
@Singleton
@TransactionManagement
public class PeopleServiceImpl implements PeopleService {

    @Inject
    private PeopleMainRepository peopleMainRepository;

    @Override
    public PeopleMainEntity findPeople(Integer peopleId) {
        return peopleMainRepository.findOne(peopleId);
    }

    @Override
    @TransactionAttribute
    public PeopleMainEntity createPeople() {
        PeopleMainEntity peopleMain = new PeopleMainEntity();
        peopleMainRepository.save(peopleMain);

        return peopleMain;
    }
}