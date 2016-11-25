package ru.simplgroupp.facade.impl

import ru.simplgroupp.facade.AdditionalDataDto
import ru.simplgroupp.facade.EmploymentDataDto
import ru.simplgroupp.facade.PersonalDataDto
import ru.simplgroupp.facade.RegistrationAddressDataDto
import ru.simplgroupp.facade.ResidentialAddressDataDto
import ru.simplgroupp.facade.SpouseFacade
import ru.simplgroupp.interfaces.PeopleBeanLocal
import ru.simplgroupp.interfaces.ReferenceBooksLocal
import ru.simplgroupp.interfaces.UsersBeanLocal
import ru.simplgroupp.persistence.AddressEntity
import ru.simplgroupp.persistence.DocumentEntity
import ru.simplgroupp.persistence.EmploymentEntity
import ru.simplgroupp.persistence.PeopleContactEntity
import ru.simplgroupp.persistence.PeopleMainEntity
import ru.simplgroupp.persistence.PeopleMiscEntity
import ru.simplgroupp.persistence.PeoplePersonalEntity
import ru.simplgroupp.persistence.ReferenceEntity
import ru.simplgroupp.persistence.SpouseEntity
import ru.simplgroupp.services.AddressService
import ru.simplgroupp.services.DocumentService
import ru.simplgroupp.services.EmploymentService
import ru.simplgroupp.services.PeopleContactService
import ru.simplgroupp.services.PeopleMiscService
import ru.simplgroupp.services.PeoplePersonalService
import ru.simplgroupp.services.PeopleService
import ru.simplgroupp.services.SpouseService
import ru.simplgroupp.transfer.PeopleContact
import spock.lang.Specification

/**
 * Specification for @{link PeopleFacadeImpl}*/
class PeopleFacadeImplSpec extends Specification {

    PeopleService peopleService = Mock(PeopleService)

    PeoplePersonalService peoplePersonalService = Mock(PeoplePersonalService)

    DocumentService documentService = Mock(DocumentService)

    PeopleContactService peopleContactService = Mock(PeopleContactService)

    PeopleMiscService peopleMiscService = Mock(PeopleMiscService)

    SpouseService spouseService = Mock(SpouseService)

    EmploymentService employmentService = Mock(EmploymentService)

    AddressService addressService = Mock(AddressService)

    SpouseFacade spouseFacade = Mock(SpouseFacade)

    PeopleBeanLocal peopleBean = Mock(PeopleBeanLocal)

    UsersBeanLocal usersBean = Mock(UsersBeanLocal)

    ReferenceBooksLocal referenceBooks = Mock(ReferenceBooksLocal)

    PeopleFacadeImpl peopleFacade = new PeopleFacadeImpl(
            peopleService: peopleService,
            peoplePersonalService: peoplePersonalService,
            documentService: documentService,
            peopleContactService: peopleContactService,
            peopleMiscService: peopleMiscService,
            spouseService: spouseService,
            employmentService: employmentService,
            addressService: addressService,
            spouseFacade: spouseFacade,
            peopleBean: peopleBean,
            usersBean: usersBean,
            referenceBooks: referenceBooks
    )

    def 'should get personal data'() {
        given:
        Integer peopleId = 1;

        ReferenceEntity gender = new ReferenceEntity(codeinteger: 1)
        PeoplePersonalEntity peoplePersonal = new PeoplePersonalEntity(
                id: 1,
                surname: 'LAST_NAME',
                name: 'FIRST_NAME',
                midname: 'MIDDLE_NAME',
                birthdate: new Date(),
                birthplace: 'BIRTH_PLACE',
                gender: gender
        )

        DocumentEntity document = new DocumentEntity(
                id: 2,
                series: 'SERIAL',
                number: 'NUMBER',
                docorgcode: 'DEPARTMENT_CODE',
                docdate: new Date(),
                docorg: 'ISSUE_ORGANIZATION'
        );

        PeopleContactEntity email = new PeopleContactEntity(
                id: 3,
                value: 'EMAIL'
        )

        PeopleContactEntity phone = new PeopleContactEntity(
                id: 4,
                value: 'PHONE'
        )

        when: 'get personal data'
        PersonalDataDto personalData = peopleFacade.getPersonalData(peopleId)

        then:
        peoplePersonalService.findActiveByPeople(peopleId) >> peoplePersonal
        documentService.findActivePassportByPeople(peopleId) >> document
        peopleContactService.findActiveEmailByPeople(peopleId) >> email
        peopleContactService.findActivePhoneByPeople(peopleId) >> phone

        and: 'personal data is correct'
        personalData.personalId == peoplePersonal.id
        personalData.lastName == peoplePersonal.surname
        personalData.firstName == peoplePersonal.name
        personalData.middleName == peoplePersonal.midname
        personalData.birthDate == peoplePersonal.birthdate
        personalData.birthPlace == peoplePersonal.birthplace
        personalData.genderCode == peoplePersonal.gender.getCodeinteger()

        and: 'document data is correct'
        personalData.documentId == document.id
        personalData.passportSerial == document.series;
        personalData.passportNumber == document.number;
        personalData.passportDepartmentCode == document.docorgcode
        personalData.passportIssueDate == document.docdate
        personalData.passportIssueOrganization == document.docorg

        and: 'email is correct'
        personalData.emailId == email.id
        personalData.email == email.value

        and: 'phone is correct'
        personalData.phoneId == phone.id
        personalData.phone == phone.value
    }

    def 'should get additional data'() {
        given:
        Integer peopleId = 1;

        PeopleMiscEntity peopleMisc = new PeopleMiscEntity(
                marriageId: new ReferenceEntity(codeinteger: 2),
                children: 3,
                car: true
        )

        SpouseEntity spouse = new SpouseEntity(
                peopleMainSpouseId: new PeopleMainEntity(
                        id: 33,
                ),
                typeworkId: new ReferenceEntity(codeinteger: 44),
                databeg: new Date()
        )

        PeoplePersonalEntity matePersonal = new PeoplePersonalEntity(
                surname: 'LAST_NAME',
                name: 'FIRST_NAME',
                midname: 'MIDDLE_NAME',
                birthdate: new Date()
        )

        PeopleContactEntity phone = new PeopleContactEntity(
                id: 4,
                value: 'PHONE'
        )

        when: 'get additional data'
        AdditionalDataDto additionalData = peopleFacade.getAdditionalData(peopleId)

        then:
        peopleMiscService.findActiveByPeople(peopleId) >> peopleMisc
        spouseService.findActiveByPeople(peopleId) >> spouse
        peoplePersonalService.findActiveByPeople(spouse.getPeopleMainSpouseId().getId()) >> matePersonal
        peopleContactService.findActivePhoneByPeople(spouse.getPeopleMainSpouseId().getId()) >> phone

        and: 'misc data is correct'
        additionalData.maritalStatus == peopleMisc.marriageId.codeinteger
        additionalData.childrenCount == peopleMisc.children
        additionalData.hasCar == peopleMisc.car

        and: 'spouse data is correct'
        additionalData.occupationCode == spouse.typeworkId.codeinteger
        additionalData.marriageDate == spouse.getDatabeg()

        and: 'mate data is correct'
        additionalData.lastName == matePersonal.surname
        additionalData.firstName == matePersonal.name
        additionalData.middleName == matePersonal.midname
        additionalData.birthDate == matePersonal.birthdate

        and: 'phone is correct'
        additionalData.phone == phone.value
    }


    def 'should get employment data'() {
        given:
        Integer peopleId = 1;

        EmploymentEntity employment = new EmploymentEntity(
                typeworkId: new ReferenceEntity(codeinteger: 12),
                occupationId: new ReferenceEntity(codeinteger: 13),
                salary: 201,
                salaryfreqId: new ReferenceEntity(codeinteger: 14),
                educationId: new ReferenceEntity(codeinteger: 15),
                extsalary: 603,
                extsalaryId: new ReferenceEntity(codeinteger: 16),
                experience: new Date(),
                placeWork: 'EMPLOYMENT_PLACE',
                occupation: 'POSITION',
                datestartwork: new Date()


        )

        PeopleContactEntity workPhone = new PeopleContactEntity(
                id: 4,
                value: 'PHONE'
        )

        when: 'get employment data'
        EmploymentDataDto employmentData = peopleFacade.getEmploymentData(peopleId)

        then:
        employmentService.findCurrentByPeople(peopleId) >> employment
        peopleContactService.findActiveContactByPeople(peopleId, PeopleContact.CONTACT_WORK_PHONE) >> workPhone

        and: 'employment data is correct'
        employmentData.occupationCode == employment.typeworkId.codeinteger
        employmentData.professionCode == employment.occupationId.codeinteger
        employmentData.salary == employment.salary
        employmentData.salaryFrequenceCode == employment.salaryfreqId.codeinteger
        employmentData.educationCode == employment.educationId.codeinteger
        employmentData.additionalIncome == employment.extsalary
        employmentData.additionalIncomeSourceCode == employment.extsalaryId.codeinteger
        employmentData.enterEmploymentDate == employment.experience
        employmentData.employmentPlace == employment.placeWork
        employmentData.position == employment.occupation
        employmentData.enterPositionDate == employment.datestartwork

        and: 'work phone is correct'
        employmentData.workPhone == workPhone.value
    }

    def 'should get registration address data'() {
        given:
        Integer peopleId = 1;
        PeopleMiscEntity peopleMisc = new PeopleMiscEntity(
                realtyDate: new Date()
        )

        AddressEntity address = new AddressEntity(isSame: 0)

        PeopleContactEntity phone = new PeopleContactEntity(
                id: 4,
                value: 'PHONE',
                available: true
        )

        when: 'get registration address data'
        RegistrationAddressDataDto registrationAddressData = peopleFacade.getRegistrationAddressData(peopleId)

        then:
        addressService.findActiveRegistrationAddressByPeople(peopleId) >> address
        peopleContactService.findActiveContactByPeople(peopleId, PeopleContact.CONTACT_HOME_REGISTER_PHONE) >> phone
        peopleMiscService.findActiveByPeople(peopleId) >> peopleMisc

        and: 'address data is correct'
        registrationAddressData.address.entity == address
        !registrationAddressData.isSameResidentalAddress()

        and: 'phone is correct'
        registrationAddressData.phone == phone.value
        registrationAddressData.phoneAvailable == phone.available

        and: 'registration date is correct'
        registrationAddressData.registrationDate == peopleMisc.realtyDate
    }

    def 'should get residential address data'() {
        given:
        Integer peopleId = 1;
        PeopleMiscEntity peopleMisc = new PeopleMiscEntity(
                regDate: new Date(),
                realtyId: new ReferenceEntity(codeinteger: 3)
        )

        AddressEntity address = new AddressEntity()

        PeopleContactEntity phone = new PeopleContactEntity(
                id: 4,
                value: 'PHONE',
                available: true
        )

        when: 'get residential address data'
        ResidentialAddressDataDto residentialAddressData = peopleFacade.getResidentialAddressData(peopleId)

        then:
        addressService.findActiveResidentialAddressByPeople(peopleId) >> address
        peopleContactService.findActiveContactByPeople(peopleId, PeopleContact.CONTACT_HOME_PHONE) >> phone
        peopleMiscService.findActiveByPeople(peopleId) >> peopleMisc

        and: 'address data is correct'
        residentialAddressData.address.entity == address

        and: 'phone is correct'
        residentialAddressData.phone == phone.value
        residentialAddressData.phoneAvailable == phone.available

        and: 'registration date and realty code is correct'
        residentialAddressData.residentialDate == peopleMisc.regDate
        residentialAddressData.realtyCode == peopleMisc.realtyId.codeinteger
    }
}
