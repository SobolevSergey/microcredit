package ru.simplgroupp.services;

import ru.simplgroupp.persistence.PeopleContactEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;

/**
 * People contact service
 */
@Local
public interface PeopleContactService {

    PeopleContactEntity findActiveEmailByPeople(Integer peopleId);

    PeopleContactEntity findActivePhoneByPeople(Integer peopleId);

    PeopleContactEntity findActiveContactByPeople(Integer peopleId, int type);

    PeopleContactEntity createCellPhone(PeopleMainEntity peopleMain, String phone);

    PeopleContactEntity updateCellPhone(PeopleContactEntity peopleMain, String phone);

    PeopleContactEntity saveCellPhone(Integer peopleId, String phone) throws ObjectNotFoundException;

    PeopleContactEntity createEmail(PeopleMainEntity peopleMain, String email);

    PeopleContactEntity updateEmail(PeopleContactEntity peopleMain, String email);

    PeopleContactEntity saveEmail(Integer peopleId, String email) throws ObjectNotFoundException;

    PeopleContactEntity saveContact(Integer peopleId, int type, String value, boolean available)
            throws ObjectNotFoundException;

    void loadDefInfo(PeopleContactEntity phoneContact, String phone);

    void deleteContact(Integer peopleId, int type);

    void archiveContact(Integer peopleId, int type);
}
