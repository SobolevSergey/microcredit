package ru.simplgroupp.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.KassaRuntimeException;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.CountryEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.persistence.repository.GenericRepository;
import ru.simplgroupp.persistence.repository.PartnerRepository;
import ru.simplgroupp.persistence.repository.PeopleMainRepository;
import ru.simplgroupp.persistence.repository.PeoplePersonalRepository;
import ru.simplgroupp.services.PeoplePersonalService;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.BaseAddress;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.RefHeader;

import javax.ejb.ObjectNotFoundException;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * People personal service implementation
 */
@Singleton
@TransactionManagement
public class PeoplePersonalServiceImpl implements PeoplePersonalService {

    private Logger logger = LoggerFactory.getLogger(PeoplePersonalServiceImpl.class);

    @Inject
    private PartnerRepository partnerRepository;

    @Inject
    private PeoplePersonalRepository peoplePersonalRepository;

    @Inject
    private PeopleMainRepository peopleMainRepository;

    @Inject
    private GenericRepository genericRepository;

    @Inject
    private ReferenceBooksLocal referenceBooks;

    @Override
    public PeoplePersonalEntity findById(Integer personalId) {
        return peoplePersonalRepository.findOne(personalId);
    }

    @Override
    public PeoplePersonalEntity findActiveByPeople(Integer peopleId) {
        List<PeoplePersonalEntity> list = peoplePersonalRepository.findActiveByPeople(peopleId, Partner.CLIENT,
                new PageRequest(0, 1));

        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    @TransactionAttribute
    public PeoplePersonalEntity createPeoplePersonal(PeopleMainEntity peopleMain, String lastName, String firstName,
                                                     String middleName, Date birthDate, String birthPlace,
                                                     Integer genderId) {
        PartnersEntity partner = partnerRepository.findOne(Partner.CLIENT);
        CountryEntity country = genericRepository.findOne(CountryEntity.class, BaseAddress.COUNTRY_RUSSIA);

        PeoplePersonalEntity peoplePersonal = new PeoplePersonalEntity();
        peoplePersonal.setPeopleMainId(peopleMain);
        peoplePersonal.setPartnersId(partner);
        peoplePersonal.setIsUploaded(false);
        peoplePersonal.setCitizen(country);
        peoplePersonal.setDatabeg(new Date());
        peoplePersonal.setIsactive(ActiveStatus.ACTIVE);

        fillPeoplePersonal(peoplePersonal, lastName, firstName, middleName, birthDate, birthPlace, genderId);
        peoplePersonal.clean();
        return peoplePersonalRepository.save(peoplePersonal);
    }

    @Override
    @TransactionAttribute
    public PeoplePersonalEntity updatePeoplePersonal(PeoplePersonalEntity peoplePersonal, String lastName,
                                                     String firstName, String middleName, Date birthDate,
                                                     String birthPlace, Integer genderId) {
        fillPeoplePersonal(peoplePersonal, lastName, firstName, middleName, birthDate, birthPlace, genderId);
        return peoplePersonalRepository.save(peoplePersonal);
    }

    @Override
    public PeoplePersonalEntity savePeoplePersonal(Integer peopleId, String lastName, String firstName,
                                                   String middleName, Date birthDate, String birthPlace,
                                                   Integer genderId) throws ObjectNotFoundException {
        PeopleMainEntity peopleMain = peopleMainRepository.findOne(peopleId);
        if (peopleMain == null) {
            throw new ObjectNotFoundException("People with id " + peopleId + " not found");
        }

        PeoplePersonalEntity peoplePersonal = findActiveByPeople(peopleId);
        if (peoplePersonal != null) {
            peoplePersonal = updatePeoplePersonal(peoplePersonal, lastName, firstName, middleName, birthDate,
                    birthPlace, genderId);

            return peoplePersonalRepository.save(peoplePersonal.clean());
        } else {
            return createPeoplePersonal(peopleMain, lastName, firstName, middleName, birthDate, birthPlace, genderId);
        }
    }

    @Override
    public PeoplePersonalEntity saveAndArchivePeoplePersonal(Integer peopleId, String lastName, String firstName,
                                                             String middleName, Date birthDate, String birthPlace,
                                                             Integer genderId) throws ObjectNotFoundException {
        PeopleMainEntity peopleMain = peopleMainRepository.findOne(peopleId);
        if (peopleMain == null) {
            throw new ObjectNotFoundException("People with id " + peopleId + " not found");
        }

        PeoplePersonalEntity peoplePersonal = findActiveByPeople(peopleId);
        if (peoplePersonal != null) {
            peoplePersonal = updatePeoplePersonal(peoplePersonal, lastName, firstName, middleName, birthDate,
                    birthPlace, genderId);
            if (peoplePersonal.isDirty()) {
                peoplePersonal.setDatabeg(new Date());
                peoplePersonalRepository.save(peoplePersonal.clean());

                try {
                    PeoplePersonalEntity peoplePersonalArchive = (PeoplePersonalEntity) peoplePersonal.clone();
                    peoplePersonalRepository.save(peoplePersonalArchive.archive());
                } catch (CloneNotSupportedException e) {
                    throw new KassaRuntimeException(
                            "Error cloning PeoplePersonalEntity with id " + peoplePersonal.getId());
                }
            }
            return peoplePersonal;
        } else {
            return createPeoplePersonal(peopleMain, lastName, firstName, middleName, birthDate, birthPlace, genderId);
        }
    }

    private void fillPeoplePersonal(PeoplePersonalEntity peoplePersonal, String lastName, String firstName,
                                    String middleName, Date birthDate, String birthPlace, Integer genderId) {
        peoplePersonal.setSurname(Convertor.toRightString(lastName));
        peoplePersonal.setName(Convertor.toRightString(firstName));
        peoplePersonal.setMidname(Convertor.toRightString(middleName));
        peoplePersonal.setBirthdate(birthDate);
        peoplePersonal.setBirthplace(birthPlace);
        if (genderId != null) {
            peoplePersonal.setGender(referenceBooks.findByCodeIntegerEntity(RefHeader.GENDER_TYPE, genderId));
        }
    }
}
