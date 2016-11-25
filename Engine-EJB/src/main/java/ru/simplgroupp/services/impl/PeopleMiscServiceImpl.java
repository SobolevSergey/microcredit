package ru.simplgroupp.services.impl;

import org.springframework.data.domain.PageRequest;
import ru.simplgroupp.exception.KassaRuntimeException;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeopleMiscEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.persistence.repository.PartnerRepository;
import ru.simplgroupp.persistence.repository.PeopleMainRepository;
import ru.simplgroupp.persistence.repository.PeopleMiscRepository;
import ru.simplgroupp.services.PeopleMiscService;
import ru.simplgroupp.transfer.ActiveStatus;
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
 * People misc service implementation
 */
@Singleton
@TransactionManagement
public class PeopleMiscServiceImpl implements PeopleMiscService {

    @Inject
    private PeopleMiscRepository peopleMiscRepository;

    @Inject
    private ReferenceBooksLocal referenceBooks;

    @Inject
    private PartnerRepository partnerRepository;

    @Inject
    private PeopleMainRepository peopleMainRepository;

    @Override
    public PeopleMiscEntity findActiveByPeople(Integer peopleId) {
        List<PeopleMiscEntity> list = peopleMiscRepository.findActiveByPeople(peopleId, Partner.CLIENT,
                new PageRequest(0, 1));
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    @TransactionAttribute
    public PeopleMiscEntity createPeopleMisc(PeopleMainEntity peopleMain, Integer maritalStatusCode, int childrenCount,
                                             boolean hasCar) {
        PartnersEntity partner = partnerRepository.findOne(Partner.CLIENT);
        ReferenceEntity maritalStatus = referenceBooks.findByCodeIntegerEntity(RefHeader.MARITAL_STATUS_SYSTEM,
                maritalStatusCode);

        PeopleMiscEntity peopleMisc = new PeopleMiscEntity();
        peopleMisc.setPeopleMainId(peopleMain);
        peopleMisc.setPartnersId(partner);
        peopleMisc.setIsactive(ActiveStatus.ACTIVE);

        peopleMisc.setMarriageId(maritalStatus);
        peopleMisc.setChildren(childrenCount);
        peopleMisc.setCar(hasCar);
        peopleMisc.clean();

        return peopleMiscRepository.save(peopleMisc);
    }

    @Override
    @TransactionAttribute
    public PeopleMiscEntity updatePeopleMisc(PeopleMiscEntity peopleMisc, Integer maritalStatusCode, int childrenCount,
                                             boolean hasCar) {

        peopleMisc.setMarriageId(referenceBooks.findByCodeIntegerEntity(RefHeader.MARITAL_STATUS_SYSTEM, maritalStatusCode));
        peopleMisc.setChildren(childrenCount);
        peopleMisc.setCar(hasCar);

        return peopleMiscRepository.save(peopleMisc);
    }

    @Override
    @TransactionAttribute
    public PeopleMiscEntity savePeopleMisc(Integer peopleId, Integer maritalStatus, int childrenCount, boolean hasCar)
            throws ObjectNotFoundException {
        PeopleMainEntity peopleMain = peopleMainRepository.findOne(peopleId);
        if (peopleMain == null) {
            throw new ObjectNotFoundException("People with id " + peopleId + " not found");
        }

        PeopleMiscEntity peopleMisc = findActiveByPeople(peopleId);
        if (peopleMisc != null) {
            peopleMisc = updatePeopleMisc(peopleMisc, maritalStatus, childrenCount, hasCar);
            return peopleMiscRepository.save(peopleMisc.clean());
        } else {
            return createPeopleMisc(peopleMain, maritalStatus, childrenCount, hasCar);
        }
    }

    @Override
    @TransactionAttribute
    public PeopleMiscEntity saveAndArchivePeopleMisc(Integer peopleId, Integer maritalStatus, int childrenCount,
                                                     boolean hasCar) throws ObjectNotFoundException {
        PeopleMainEntity peopleMain = peopleMainRepository.findOne(peopleId);
        if (peopleMain == null) {
            throw new ObjectNotFoundException("People with id " + peopleId + " not found");
        }

        PeopleMiscEntity peopleMisc = findActiveByPeople(peopleId);
        if (peopleMisc != null) {
            peopleMisc = updatePeopleMisc(peopleMisc, maritalStatus, childrenCount, hasCar);
            if (peopleMisc.isDirty()) {
                peopleMisc.setDatabeg(new Date());
                peopleMiscRepository.save(peopleMisc.clean());

                try {
                    PeopleMiscEntity peopleMiscArchive = (PeopleMiscEntity) peopleMisc.clone();
                    peopleMiscRepository.save(peopleMiscArchive.archive());
                } catch (CloneNotSupportedException e) {
                    throw new KassaRuntimeException(
                            "Error cloning PeoplePersonalEntity with id " + peopleMisc.getId());
                }
            }
            return peopleMisc;
        } else {
            return createPeopleMisc(peopleMain, maritalStatus, childrenCount, hasCar);
        }
    }

    @Override
    @TransactionAttribute
    public PeopleMiscEntity updatePeopleMiscAddressData(PeopleMiscEntity peopleMisc, Date registrationDate, Date residentialDate,
                                                        Integer realtyCode) throws ObjectNotFoundException {
        peopleMisc.setRealtyDate(registrationDate);
        peopleMisc.setRegDate(residentialDate);
        peopleMisc.setRealtyId(referenceBooks.findByCodeIntegerEntity(RefHeader.REALTY_TYPE, realtyCode));

        return peopleMiscRepository.save(peopleMisc);
    }

    @Override
    @TransactionAttribute
    public PeopleMiscEntity savePeopleMiscAddressData(Integer peopleId, Date registrationDate, Date residentialDate,
                                                      Integer realtyCode) throws ObjectNotFoundException {
        PeopleMainEntity peopleMain = peopleMainRepository.findOne(peopleId);
        if (peopleMain == null) {
            throw new ObjectNotFoundException("People with id " + peopleId + " not found");
        }

        PeopleMiscEntity peopleMisc = findActiveByPeople(peopleId);
        peopleMisc = updatePeopleMiscAddressData(peopleMisc, registrationDate, residentialDate, realtyCode);
        return peopleMiscRepository.save(peopleMisc.clean());
    }

    @Override
    public PeopleMiscEntity saveAndArchivePeopleMiscAddressData(Integer peopleId, Date registrationDate,
                                                                Date residentialDate, Integer realtyCode) {
        return null;
    }
}
