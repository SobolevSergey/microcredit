package ru.simplgroupp.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;

import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleContactEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.RegionsEntity;
import ru.simplgroupp.persistence.repository.GenericRepository;
import ru.simplgroupp.persistence.repository.PartnerRepository;
import ru.simplgroupp.persistence.repository.PeopleContactRepository;
import ru.simplgroupp.persistence.repository.PeopleMainRepository;
import ru.simplgroupp.services.DefInfoClientService;
import ru.simplgroupp.services.DefInfoClientServiceException;
import ru.simplgroupp.services.DefInfoRest;
import ru.simplgroupp.services.PeopleContactService;
import ru.simplgroupp.transfer.*;

import javax.ejb.Asynchronous;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;

import java.util.Date;
import java.util.List;

/**
 * People contact service implementation
 */
@Singleton
@TransactionManagement
public class PeopleContactServiceImpl implements PeopleContactService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private ReferenceBooksLocal referenceBooks;

    @Inject
    private EventLogService eventLogService;

    @Inject
    private PartnerRepository partnerRepository;

    @Inject
    private PeopleContactRepository peopleContactRepository;

    @Inject
    private PeopleMainRepository peopleMainRepository;

    @Inject
    private DefInfoClientService defInfoClientService;

    @Inject
    private GenericRepository genericRepository;

    @Override
    public PeopleContactEntity findActiveEmailByPeople(Integer peopleId) {
        List<PeopleContactEntity> list = peopleContactRepository.findActiveByPeople(peopleId,
                PeopleContact.CONTACT_EMAIL, Partner.CLIENT, new PageRequest(0, 1));
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public PeopleContactEntity findActivePhoneByPeople(Integer peopleId) {
        List<PeopleContactEntity> list = peopleContactRepository.findActiveByPeople(peopleId,
                PeopleContact.CONTACT_CELL_PHONE, Partner.CLIENT, new PageRequest(0, 1));
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public PeopleContactEntity findActiveContactByPeople(Integer peopleId, int type) {
        List<PeopleContactEntity> list = peopleContactRepository.findActiveByPeople(peopleId, type, Partner.CLIENT,
                new PageRequest(0, 1));
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    @TransactionAttribute
    public PeopleContactEntity createCellPhone(PeopleMainEntity peopleMain, String phone) {
        PeopleContactEntity contact = createContact(peopleMain, phone, PeopleContact.CONTACT_CELL_PHONE, false);
        contact.setValue(phone);
        peopleContactRepository.save(contact);
        return contact;
    }

    @Override
    public PeopleContactEntity updateCellPhone(PeopleContactEntity contact, String phone) {
        contact.setValue(phone);
        return peopleContactRepository.save(contact);
    }

    @Override
    @TransactionAttribute
    public PeopleContactEntity createEmail(PeopleMainEntity peopleMain, String email) {
        PeopleContactEntity contact = createContact(peopleMain, email, PeopleContact.CONTACT_EMAIL, false);
        peopleContactRepository.save(contact);

        return contact;
    }

    @Override
    public PeopleContactEntity updateEmail(PeopleContactEntity contact, String email) {
        contact.setValue(email);
        return peopleContactRepository.save(contact);
    }

    @Override
    public PeopleContactEntity saveEmail(Integer peopleId, String email) throws ObjectNotFoundException {
        PeopleMainEntity peopleMain = getPeople(peopleId);

        PeopleContactEntity emailContact = findActiveEmailByPeople(peopleId);
        if (emailContact != null) {
            return updateEmail(emailContact, email);
        } else {
            return createEmail(peopleMain, email);
        }
    }

    @Override
    public PeopleContactEntity saveContact(Integer peopleId, int type, String value, boolean available)
            throws ObjectNotFoundException {
        PeopleMainEntity peopleMain = getPeople(peopleId);

        PeopleContactEntity contact = findActiveContactByPeople(peopleId, type);
        if (contact != null) {
            contact.setValue(value);
            contact.setAvailable(available);
            return peopleContactRepository.save(contact);
        } else {
            return createContact(peopleMain, value, type, available);
        }
    }

    @Override
    public PeopleContactEntity saveCellPhone(Integer peopleId, String phone) throws ObjectNotFoundException {
        PeopleMainEntity peopleMain = getPeople(peopleId);

        PeopleContactEntity phoneContact = findActivePhoneByPeople(peopleId);
        if (phoneContact != null) {
            return updateCellPhone(phoneContact, phone);
        } else {
            return createCellPhone(peopleMain, phone);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void save(PeopleContactEntity contact) {
        peopleContactRepository.save(contact);
    }

    @Asynchronous
    public void loadDefInfo(PeopleContactEntity phoneContact, String phone) {
        if (phoneContact == null) {
            throw new IllegalArgumentException("Contact is null");
        }

        if (StringUtils.isBlank(phone)) {
            throw new IllegalArgumentException("Phone is blank");
        }

        String phoneParam = phone.length() > 10 ? phone.substring(1, 11) : phone;
        try {
            DefInfoRest defInfo = defInfoClientService.getInfo(phoneParam);
            RegionsEntity region = referenceBooks.findRegionByName(defInfo.getRegion());
            phoneContact.setRegionShort(region);
            phoneContact.setOperator(defInfo.getOperator());
            save(phoneContact);

            try {
                eventLogService.saveLog(EventType.INFO, EventCode.REQUEST_OUTER_DB,
                        "Обращение к внешней БД телефонных номеров, номер " + phone, null, new Date(),
                        phoneContact.getCreditRequestId() == null ? null : genericRepository.findOne(
                                CreditRequestEntity.class, phoneContact.getCreditRequestId()),
                        genericRepository.findOne(PeopleMainEntity.class, phoneContact.getPeopleMainId().getId()),
                        null);
            } catch (Exception e) {
                logger.warn("Не удалось записать лог, ошибка " + e.getMessage());
            }
        } catch (DefInfoClientServiceException e) {
            logger.warn("Can't load DEF info. Cause: " + e.getMessage());
        }
    }

    @Override
    public void deleteContact(Integer peopleId, int type) {
        if (PeopleContact.CONTACT_CELL_PHONE == type || PeopleContact.CONTACT_EMAIL == type) {
            throw new IllegalArgumentException("Do not allow delete cell phone or email");
        }
        PeopleContactEntity contact = findActiveContactByPeople(peopleId, type);
        if (contact != null) {
            peopleContactRepository.delete(contact);
        }
    }

    @Override
    public void archiveContact(Integer peopleId, int type) {
        if (PeopleContact.CONTACT_CELL_PHONE == type || PeopleContact.CONTACT_EMAIL == type) {
            throw new IllegalArgumentException("Do not allow archive cell phone or email");
        }
        PeopleContactEntity contact = findActiveContactByPeople(peopleId, type);
        peopleContactRepository.save(contact.archive());
    }

    private PeopleContactEntity createContact(PeopleMainEntity peopleMain, String value, Integer type,
                                              boolean available) {
        PartnersEntity partner = partnerRepository.findOne(Partner.CLIENT);
        Reference contactType = referenceBooks.getContactType(type);

        PeopleContactEntity contact = new PeopleContactEntity();
        contact.setPeopleMainId(peopleMain);
        contact.setContactId(contactType.getEntity());
        contact.setPartnersId(partner);
        contact.setDateactual(new Date());
        contact.setAvailable(available);
        contact.setIsactive(ActiveStatus.ACTIVE);
        contact.setValue(value);
        return contact;
    }

    private PeopleMainEntity getPeople(Integer peopleId) throws ObjectNotFoundException {
        PeopleMainEntity peopleMain = peopleMainRepository.findOne(peopleId);
        if (peopleMain == null) {
            throw new ObjectNotFoundException("People with id " + peopleId + " not found");
        }
        return peopleMain;
    }
}
