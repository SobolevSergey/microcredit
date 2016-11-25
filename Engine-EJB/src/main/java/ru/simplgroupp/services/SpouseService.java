package ru.simplgroupp.services;

import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.SpouseEntity;

import javax.ejb.Local;
import java.util.Date;

/**
 * Spouse service
 */
@Local
public interface SpouseService {

    SpouseEntity findActiveByPeople(Integer peopleId);

    SpouseEntity createSpouse(PeopleMainEntity peopleMain, PeopleMainEntity spousePeople, Integer occupationId, Date marriageDate);

    SpouseEntity updateSpouse(SpouseEntity spouse, Integer occupationId, Date marriageDate);
}
