package ru.simplgroupp.facade;

import javax.ejb.ObjectNotFoundException;

/**
 * People facade
 */
public interface PeopleFacade {

    PersonalDataDto getPersonalData(Integer peopleId);

    AdditionalDataDto getAdditionalData(Integer peopleId);

    EmploymentDataDto getEmploymentData(Integer peopleId);

    RegistrationAddressDataDto getRegistrationAddressData(Integer peopleId);

    ResidentialAddressDataDto getResidentialAddressData(Integer peopleId);

    void savePersonalData(Integer peopleId, PersonalDataDto personalData, boolean isNewPeople)
            throws ObjectNotFoundException;

    void saveAdditionalData(Integer peopleId, AdditionalDataDto additionalData, boolean isNewPeople)
            throws ObjectNotFoundException;

    void saveEmploymentData(Integer peopleId, EmploymentDataDto employmentDataDto, boolean isNewPeople)
            throws ObjectNotFoundException;

    void saveAddressData(Integer peopleId, RegistrationAddressDataDto registrationAddressData,
                         ResidentialAddressDataDto residentialAddressData, boolean isNewPeople)
            throws ObjectNotFoundException;
}
