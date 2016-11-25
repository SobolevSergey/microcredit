package ru.simplgroupp.facade.impl;

import org.apache.commons.lang3.StringUtils;
import ru.simplgroupp.facade.AdditionalDataDto;
import ru.simplgroupp.facade.EmploymentDataDto;
import ru.simplgroupp.facade.PeopleFacade;
import ru.simplgroupp.facade.PersonalDataDto;
import ru.simplgroupp.facade.RegistrationAddressDataDto;
import ru.simplgroupp.facade.ResidentialAddressDataDto;
import ru.simplgroupp.facade.SpouseFacade;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.UsersBeanLocal;
import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.EmploymentEntity;
import ru.simplgroupp.persistence.PeopleContactEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeopleMiscEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.persistence.SpouseEntity;
import ru.simplgroupp.services.AddressService;
import ru.simplgroupp.services.DocumentService;
import ru.simplgroupp.services.EmploymentService;
import ru.simplgroupp.services.PeopleContactService;
import ru.simplgroupp.services.PeopleMiscService;
import ru.simplgroupp.services.PeoplePersonalService;
import ru.simplgroupp.services.PeopleService;
import ru.simplgroupp.services.SpouseService;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.FiasAddress;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.PeopleContact;
import ru.simplgroupp.transfer.Spouse;

import javax.ejb.ObjectNotFoundException;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import java.util.Date;

/**
 * People facade implementation
 */
@Singleton
@TransactionManagement
public class PeopleFacadeImpl implements PeopleFacade {

    @Inject
    private PeopleService peopleService;

    @Inject
    private PeoplePersonalService peoplePersonalService;

    @Inject
    private DocumentService documentService;

    @Inject
    private PeopleContactService peopleContactService;

    @Inject
    private PeopleMiscService peopleMiscService;

    @Inject
    private SpouseService spouseService;

    @Inject
    private EmploymentService employmentService;

    @Inject
    private AddressService addressService;

    @Inject
    private SpouseFacade spouseFacade;

    @Inject
    private PeopleBeanLocal peopleBean;

    @Inject
    private UsersBeanLocal usersBean;

    @Inject
    private ReferenceBooksLocal referenceBooks;

    @Override
    public PersonalDataDto getPersonalData(Integer peopleId) {
        PersonalDataDto personalData = new PersonalDataDto();

        PeoplePersonalEntity peoplePersonal = peoplePersonalService.findActiveByPeople(peopleId);
        if (peoplePersonal != null) {
            personalData.setPersonalId(peoplePersonal.getId());
            personalData.setLastName(peoplePersonal.getSurname());
            personalData.setFirstName(peoplePersonal.getName());
            personalData.setMiddleName(peoplePersonal.getMidname());
            personalData.setBirthDate(peoplePersonal.getBirthdate());
            personalData.setBirthPlace(peoplePersonal.getBirthplace());
            personalData.setGenderCode(peoplePersonal.getGender().getCodeinteger());
        }

        DocumentEntity passport = documentService.findActivePassportByPeople(peopleId);
        if (passport != null) {
            personalData.setDocumentId(passport.getId());
            personalData.setPassportSerial(passport.getSeries());
            personalData.setPassportNumber(passport.getNumber());
            personalData.setPassportDepartmentCode(passport.getDocorgcode());
            personalData.setPassportIssueDate(passport.getDocdate());
            personalData.setPassportIssueOrganization(passport.getDocorg());
        }

        PeopleContactEntity email = peopleContactService.findActiveEmailByPeople(peopleId);
        if (email != null) {
            personalData.setEmailId(email.getId());
            personalData.setEmail(email.getValue());
        }
        PeopleContactEntity phone = peopleContactService.findActivePhoneByPeople(peopleId);
        if (phone != null) {
            personalData.setPhoneId(phone.getId());
            personalData.setPhone(phone.getValue());
        }

        return personalData;
    }

    @Override
    public AdditionalDataDto getAdditionalData(Integer peopleId) {
        AdditionalDataDto additionalData = new AdditionalDataDto();

        PeopleMiscEntity peopleMisc = peopleMiscService.findActiveByPeople(peopleId);
        if (peopleMisc != null) {
            if (peopleMisc.getMarriageId() != null) {
                additionalData.setMaritalStatus(peopleMisc.getMarriageId().getCodeinteger());
            }
            additionalData.setChildrenCount(peopleMisc.getChildren());
            additionalData.setHasCar(peopleMisc.getCar());
        }

        SpouseEntity spouse = spouseService.findActiveByPeople(peopleId);

        if (spouse != null) {
            if (spouse.getTypeworkId() != null) {
                additionalData.setOccupationCode(spouse.getTypeworkId().getCodeinteger());
            }
            additionalData.setMarriageDate(spouse.getDatabeg());

            PeopleMainEntity mate = spouse.getPeopleMainSpouseId();
            if (mate != null) {
                PeoplePersonalEntity matePersonal = peoplePersonalService.findActiveByPeople(mate.getId());

                additionalData.setLastName(matePersonal.getSurname());
                additionalData.setFirstName(matePersonal.getName());
                additionalData.setMiddleName(matePersonal.getMidname());
                additionalData.setBirthDate(matePersonal.getBirthdate());

                PeopleContactEntity phone = peopleContactService.findActivePhoneByPeople(mate.getId());
                if (phone != null) {
                    additionalData.setPhone(phone.getValue());
                }
            }
        }

        return additionalData;
    }

    @Override
    public EmploymentDataDto getEmploymentData(Integer peopleId) {
        EmploymentDataDto employmentData = new EmploymentDataDto();

        EmploymentEntity employment = employmentService.findCurrentByPeople(peopleId);
        if (employment != null) {
            if (employment.getTypeworkId() != null) {
                employmentData.setOccupationCode(employment.getTypeworkId().getCodeinteger());
            }
            if (employment.getOccupationId() != null) {
                employmentData.setProfessionCode(employment.getOccupationId().getCodeinteger());
            }
            employmentData.setSalary(employment.getSalary());
            if (employment.getSalaryfreqId() != null) {
                employmentData.setSalaryFrequenceCode(employment.getSalaryfreqId().getCodeinteger());
            }
            if (employment.getEducationId() != null) {
                employmentData.setEducationCode(employment.getEducationId().getCodeinteger());
            }
            employmentData.setAdditionalIncome(employment.getExtsalary());
            if (employment.getExtsalaryId() != null) {
                employmentData.setAdditionalIncomeSourceCode(employment.getExtsalaryId().getCodeinteger());
            }
            employmentData.setEnterEmploymentDate(employment.getExperience());
            employmentData.setEmploymentPlace(employment.getPlaceWork());
            employmentData.setPosition(employment.getOccupation());
            employmentData.setEnterPositionDate(employment.getDatestartwork());
        }

        PeopleContactEntity workPhone = peopleContactService.findActiveContactByPeople(peopleId,
                PeopleContact.CONTACT_WORK_PHONE);
        if (workPhone != null) {
            employmentData.setWorkPhone(workPhone.getValue());
            employmentData.setWorkPhoneAvailable(workPhone.getAvailable() == null ? false : workPhone.getAvailable());
        }

        return employmentData;
    }

    @Override
    public RegistrationAddressDataDto getRegistrationAddressData(Integer peopleId) {
        RegistrationAddressDataDto registrationAddressData = new RegistrationAddressDataDto();

        AddressEntity address = addressService.findActiveRegistrationAddressByPeople(peopleId);
        if (address != null) {
            registrationAddressData.setSameResidentalAddress(address.getIsSame() > 0);
            registrationAddressData.setAddress(new FiasAddress(address));
        }

        PeopleContactEntity phone = peopleContactService.findActiveContactByPeople(peopleId,
                PeopleContact.CONTACT_HOME_REGISTER_PHONE);
        if (phone != null) {
            registrationAddressData.setPhone(phone.getValue());
            registrationAddressData.setPhoneAvailable(phone.getAvailable() == null ? false : phone.getAvailable());
        }

        PeopleMiscEntity peopleMisc = peopleMiscService.findActiveByPeople(peopleId);
        registrationAddressData.setRegistrationDate(peopleMisc.getRealtyDate());
        return registrationAddressData;
    }

    @Override
    public ResidentialAddressDataDto getResidentialAddressData(Integer peopleId) {
        ResidentialAddressDataDto residentialAddressData = new ResidentialAddressDataDto();

        AddressEntity address = addressService.findActiveResidentialAddressByPeople(peopleId);
        if (address != null) {
            residentialAddressData.setAddress(new FiasAddress(address));
        }

        PeopleContactEntity homePhone = peopleContactService.findActiveContactByPeople(peopleId,
                PeopleContact.CONTACT_HOME_PHONE);
        if (homePhone != null) {
            residentialAddressData.setPhone(homePhone.getValue());
            residentialAddressData.setPhoneAvailable(
                    homePhone.getAvailable() == null ? false : homePhone.getAvailable());
        }

        PeopleMiscEntity peopleMisc = peopleMiscService.findActiveByPeople(peopleId);
        residentialAddressData.setResidentialDate(peopleMisc.getRegDate());
        residentialAddressData.setRealtyCode(peopleMisc.getRealtyId().getCodeinteger());

        return residentialAddressData;
    }

    @Override
    @TransactionAttribute
    public void savePersonalData(Integer peopleId, PersonalDataDto personalData, boolean isNewPeople)
            throws ObjectNotFoundException {
        if (isNewPeople) {
            // Personal data
            peoplePersonalService.savePeoplePersonal(peopleId, personalData.getLastName(), personalData.getFirstName(),
                    personalData.getMiddleName(), personalData.getBirthDate(), personalData.getBirthPlace(),
                    personalData.getGenderCode());

            // Document data
            documentService.saveDocument(peopleId, personalData.getPassportSerial(), personalData.getPassportNumber(),
                    personalData.getPassportDepartmentCode(), personalData.getPassportIssueDate(),
                    Convertor.toLimitString(personalData.getPassportIssueOrganization(), 255));

            // Phone
            PeopleContactEntity phoneContact = peopleContactService.saveCellPhone(peopleId, personalData.getPhone());
            peopleContactService.loadDefInfo(phoneContact, personalData.getPhone());

            // Email
            peopleContactService.saveEmail(peopleId, personalData.getEmail());
        } else {
            // Personal data
            peoplePersonalService.saveAndArchivePeoplePersonal(peopleId, personalData.getLastName(),
                    personalData.getFirstName(), personalData.getMiddleName(), personalData.getBirthDate(),
                    personalData.getBirthPlace(), personalData.getGenderCode());

            // Document data
            documentService.saveAndArchiveDocument(peopleId, personalData.getPassportSerial(),
                    personalData.getPassportNumber(), personalData.getPassportDepartmentCode(),
                    personalData.getPassportIssueDate(),
                    Convertor.toLimitString(personalData.getPassportIssueOrganization(), 255));

            // Phone
            PeopleContactEntity phoneContact = peopleBean.changeContact(peopleId, PeopleContact.CONTACT_CELL_PHONE,
                    personalData.getPhone().toLowerCase(), false);
            peopleContactService.loadDefInfo(phoneContact, personalData.getPhone());

            // Email
            peopleBean.changeContact(peopleId, PeopleContact.CONTACT_EMAIL, personalData.getEmail(), false);
        }
    }

    @Override
    @TransactionAttribute
    public void saveAdditionalData(Integer peopleId, AdditionalDataDto additionalData, boolean isNewPeople)
            throws ObjectNotFoundException {
        if (isNewPeople) {
            peopleMiscService.savePeopleMisc(peopleId, additionalData.getMaritalStatus(),
                    additionalData.getChildrenCount(), additionalData.isHasCar());
        } else {
            peopleMiscService.saveAndArchivePeopleMisc(peopleId, additionalData.getMaritalStatus(),
                    additionalData.getChildrenCount(), additionalData.isHasCar());
        }

        if (additionalData.getMaritalStatus() == 1 || additionalData.getMaritalStatus() == 4) {
            peopleBean.addSpouse(peopleService.findPeople(peopleId), additionalData.getLastName(),
                    additionalData.getFirstName(), additionalData.getMiddleName(), additionalData.getBirthDate(),
                    additionalData.getPhone(), Spouse.CODE_SPOUSE, additionalData.getMarriageDate(),
                    additionalData.getOccupationCode());
        } else {
            peopleBean.closeSpouse(peopleId, new Date());
        }
    }

    @Override
    @TransactionAttribute
    public void saveEmploymentData(Integer peopleId, EmploymentDataDto employmentData, boolean isNewPeople)
            throws ObjectNotFoundException {

        if (isNewPeople) {
            employmentService.saveEmployment(peopleId, employmentData.getOccupationCode(),
                    employmentData.getEducationCode(), employmentData.getSalary(),
                    employmentData.getEnterEmploymentDate(), employmentData.getAdditionalIncome(),
                    employmentData.getAdditionalIncomeSourceCode(), employmentData.getProfessionCode(),
                    employmentData.getSalaryFrequenceCode(), employmentData.getEmploymentPlace(),
                    employmentData.getPosition(), employmentData.getEnterPositionDate());

            if (StringUtils.isBlank(employmentData.getWorkPhone())) {
                peopleContactService.deleteContact(peopleId, PeopleContact.CONTACT_WORK_PHONE);
            }
        } else {
            peopleBean.changeEmployment(peopleId, employmentData.getEducationCode(), employmentData.getOccupationCode(),
                    employmentData.getProfessionCode(), employmentData.getSalaryFrequenceCode(),
                    employmentData.getAdditionalIncomeSourceCode(), employmentData.getEmploymentPlace(),
                    employmentData.getPosition(), employmentData.getSalary(), employmentData.getAdditionalIncome(),
                    employmentData.getEnterEmploymentDate(), employmentData.getEnterPositionDate());

            if (StringUtils.isBlank(employmentData.getWorkPhone())) {
                peopleContactService.archiveContact(peopleId, PeopleContact.CONTACT_HOME_REGISTER_PHONE);
            }
        }

        // work phone
        if (!StringUtils.isBlank(employmentData.getWorkPhone())) {
            peopleBean.changeContact(peopleId, PeopleContact.CONTACT_WORK_PHONE, employmentData.getWorkPhone(),
                    false);
        }
    }

    @Override
    @TransactionAttribute
    public void saveAddressData(Integer peopleId, RegistrationAddressDataDto registrationAddressData,
                                ResidentialAddressDataDto residentialAddressData, boolean isNewPeople)
            throws ObjectNotFoundException {
        registrationAddressData.getAddress().setAddrType(referenceBooks.getAddressType(FiasAddress.REGISTER_ADDRESS));
        peopleBean.changeAddress(registrationAddressData.getAddress(), peopleId, Partner.CLIENT, new Date());

        residentialAddressData.getAddress().setAddrType(referenceBooks.getAddressType(FiasAddress.RESIDENT_ADDRESS));
        peopleBean.changeAddress(registrationAddressData.getAddress(), peopleId, Partner.CLIENT, new Date());

        if (isNewPeople) {
            peopleMiscService.savePeopleMiscAddressData(peopleId, registrationAddressData.getRegistrationDate(),
                    residentialAddressData.getResidentialDate(), residentialAddressData.getRealtyCode());

            if (StringUtils.isBlank(registrationAddressData.getPhone())) {
                peopleContactService.deleteContact(peopleId, PeopleContact.CONTACT_HOME_REGISTER_PHONE);
            }

            if (StringUtils.isBlank(residentialAddressData.getPhone())) {
                peopleContactService.deleteContact(peopleId, PeopleContact.CONTACT_HOME_PHONE);
            }
        } else {
            peopleMiscService.saveAndArchivePeopleMiscAddressData(peopleId,
                    registrationAddressData.getRegistrationDate(), residentialAddressData.getResidentialDate(),
                    residentialAddressData.getRealtyCode());

            if (StringUtils.isBlank(registrationAddressData.getPhone())) {
                peopleContactService.archiveContact(peopleId, PeopleContact.CONTACT_HOME_REGISTER_PHONE);
            }

            if (StringUtils.isBlank(residentialAddressData.getPhone())) {
                peopleContactService.archiveContact(peopleId, PeopleContact.CONTACT_HOME_PHONE);
            }
        }

        if (!StringUtils.isBlank(registrationAddressData.getPhone())) {
            peopleBean.changeContact(peopleId, PeopleContact.CONTACT_HOME_REGISTER_PHONE,
                    registrationAddressData.getPhone(), !registrationAddressData.isPhoneAvailable());
        }

        if (!StringUtils.isBlank(residentialAddressData.getPhone())) {
            peopleBean.changeContact(peopleId, PeopleContact.CONTACT_HOME_PHONE, residentialAddressData.getPhone(),
                    !residentialAddressData.isPhoneAvailable());
        }
    }
}
