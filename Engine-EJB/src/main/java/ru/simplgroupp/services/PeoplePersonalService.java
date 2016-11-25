package ru.simplgroupp.services;

import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;
import java.util.Date;

/**
 * People personal service
 */
@Local
public interface PeoplePersonalService {

    PeoplePersonalEntity findById(Integer personalId);

    PeoplePersonalEntity findActiveByPeople(Integer peopleId);

    PeoplePersonalEntity createPeoplePersonal(PeopleMainEntity peopleMain, String lastName, String firstName,
                                              String middleName, Date birthDate, String birthPlace, Integer genderId);

    PeoplePersonalEntity updatePeoplePersonal(PeoplePersonalEntity peoplePersonal, String lastName, String firstName,
                                              String middleName, Date birthDate, String birthPlace, Integer genderId);

    PeoplePersonalEntity savePeoplePersonal(Integer peopleId, String lastName, String firstName,
                                            String middleName, Date birthDate, String birthPlace, Integer genderId) throws ObjectNotFoundException;

    PeoplePersonalEntity saveAndArchivePeoplePersonal(Integer peopleId, String lastName, String firstName, String middleName, Date birthDate,
                              String birthPlace, Integer genderId) throws ObjectNotFoundException;
}
