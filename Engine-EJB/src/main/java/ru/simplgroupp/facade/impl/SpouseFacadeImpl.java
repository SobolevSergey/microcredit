package ru.simplgroupp.facade.impl;

import ru.simplgroupp.facade.SpouseFacade;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.SpouseEntity;
import ru.simplgroupp.services.PeopleContactService;
import ru.simplgroupp.services.PeoplePersonalService;
import ru.simplgroupp.services.PeopleService;
import ru.simplgroupp.services.SpouseService;

import javax.ejb.ObjectNotFoundException;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import java.util.Date;

/**
 * Spouse facade implementation
 */
@Singleton
@TransactionManagement
public class SpouseFacadeImpl implements SpouseFacade {

    @Inject
    private SpouseService spouseService;

    @Inject
    private PeoplePersonalService peoplePersonalService;

    @Inject
    private PeopleService peopleService;

    @Inject
    private PeopleContactService peopleContactService;

    @Override
    public SpouseEntity createSpouse(PeopleMainEntity peopleMain, String lastName, String firstName, String middleName,
                                     Date birthDate, String phone, Integer occupationId, Date marriageDate) {

        PeopleMainEntity spousePeople = peopleService.createPeople();
        peoplePersonalService.createPeoplePersonal(spousePeople,
                lastName, firstName, middleName, birthDate, null, null);
        peopleContactService.createCellPhone(spousePeople, phone);

        SpouseEntity spouse = spouseService.createSpouse(peopleMain, spousePeople, occupationId, marriageDate);

        return spouse;
    }

    @Override
    public SpouseEntity updateSpouse(SpouseEntity spouse, String lastName, String firstName, String middleName,
                                     Date birthDate, String phone, Integer occupationId, Date marriageDate)
            throws ObjectNotFoundException {
        spouseService.updateSpouse(spouse, occupationId, marriageDate);
        peoplePersonalService.savePeoplePersonal(spouse.getPeopleMainSpouseId().getId(), lastName, firstName,
                middleName, birthDate, null, null);
        peopleContactService.saveCellPhone(spouse.getPeopleMainSpouseId().getId(), phone);

        return spouse;
    }

    @Override
    public SpouseEntity saveSpouse(Integer peopleId, String lastName, String firstName, String middleName,
                                   Date birthDate, String phone, Integer occupationId, Date marriageDate)
            throws ObjectNotFoundException {
        PeopleMainEntity peopleMain = peopleService.findPeople(peopleId);
        if (peopleMain == null) {
            throw new ObjectNotFoundException("People with id " + peopleId + " not found");
        }

        SpouseEntity spouse = spouseService.findActiveByPeople(peopleId);

        if (spouse != null) {
            return updateSpouse(spouse, lastName, firstName, middleName, birthDate, phone, occupationId, marriageDate);
        } else {
            return createSpouse(peopleMain, lastName, firstName, middleName, birthDate, phone, occupationId, marriageDate);
        }
    }
}
