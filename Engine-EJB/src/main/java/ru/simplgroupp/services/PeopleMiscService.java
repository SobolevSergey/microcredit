package ru.simplgroupp.services;

import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeopleMiscEntity;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;
import java.util.Date;

/**
 * People misc service
 */
@Local
public interface PeopleMiscService {

    PeopleMiscEntity findActiveByPeople(Integer peopleId);

    PeopleMiscEntity createPeopleMisc(PeopleMainEntity peopleId, Integer maritalStatus, int childrenCount,
                                      boolean hasCar);

    PeopleMiscEntity updatePeopleMisc(PeopleMiscEntity peopleMisc, Integer maritalStatus, int childrenCount,
                                      boolean hasCar);

    PeopleMiscEntity savePeopleMisc(Integer peopleMainId, Integer maritalStatus, int childrenCount, boolean hasCar)
            throws ObjectNotFoundException;

    PeopleMiscEntity saveAndArchivePeopleMisc(Integer peopleId, Integer maritalStatus, int childrenCount,
                                              boolean hasCar) throws ObjectNotFoundException;

    PeopleMiscEntity updatePeopleMiscAddressData(PeopleMiscEntity peopleMisc, Date registrationDate, Date residentialDate,
                                                 Integer realtyCode) throws ObjectNotFoundException;

    PeopleMiscEntity savePeopleMiscAddressData(Integer peopleId, Date registrationDate, Date residentialDate,
                                                 Integer realtyCode) throws ObjectNotFoundException;

    PeopleMiscEntity saveAndArchivePeopleMiscAddressData(Integer peopleId, Date registrationDate, Date residentialDate,
                                             Integer realtyCode);
}
