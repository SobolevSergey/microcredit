package ru.simplgroupp.facade;

import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.SpouseEntity;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;
import java.util.Date;

/**
 * Spouse facade
 */
@Local
public interface SpouseFacade {

    SpouseEntity createSpouse(PeopleMainEntity peopleMain, String lastName, String firstName, String middleName,
                              Date birthDate, String phone, Integer occupationId, Date marriageDate);

    SpouseEntity updateSpouse(SpouseEntity spouse, String lastName, String firstName, String middleName,
                              Date birthDate, String phone, Integer occupationId, Date marriageDate) throws ObjectNotFoundException;

    SpouseEntity saveSpouse(Integer peopleId, String lastName, String firstName, String middleName, Date birthDate,
                            String phone, Integer occupationId, Date marriageDate) throws ObjectNotFoundException;
}
