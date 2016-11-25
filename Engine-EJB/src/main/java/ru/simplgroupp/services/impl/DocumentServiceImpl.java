package ru.simplgroupp.services.impl;

import org.springframework.data.domain.PageRequest;
import ru.simplgroupp.exception.KassaRuntimeException;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.repository.DocumentRepository;
import ru.simplgroupp.persistence.repository.PartnerRepository;
import ru.simplgroupp.persistence.repository.PeopleMainRepository;
import ru.simplgroupp.services.DocumentService;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.Documents;
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
 * Document service implementation
 */
@Singleton
@TransactionManagement
public class DocumentServiceImpl implements DocumentService {

    @Inject
    private PartnerRepository partnerRepository;

    @Inject
    private DocumentRepository documentRepository;

    @Inject
    private PeopleMainRepository peopleMainRepository;

    @Inject
    private ReferenceBooksLocal referenceBooks;

    @Override
    public DocumentEntity findById(Integer documentId) {
        return documentRepository.findOne(documentId);
    }

    @Override
    public DocumentEntity findActivePassportByPeople(Integer peopleId) {
        List<DocumentEntity> documents = documentRepository.findActiveByPeople(peopleId, Documents.PASSPORT_RF,
                Partner.CLIENT, new PageRequest(0, 1));
        return documents.size() > 0 ? documents.get(0) : null;
    }

    @Override
    @TransactionAttribute
    public DocumentEntity createDocument(PeopleMainEntity peopleMain, String serial, String number,
                                         String departmentCode, Date issueDate, String issueOrganization) {
        PartnersEntity partner = partnerRepository.findOne(Partner.CLIENT);

        DocumentEntity document = new DocumentEntity();
        document.setPeopleMainId(peopleMain);
        document.setPartnersId(partner);
        document.setIsUploaded(false);
        document.setDateChange(new Date());
        document.setIsactive(ActiveStatus.ACTIVE);
        document.setDocumenttypeId(
                referenceBooks.findByCodeIntegerEntity(RefHeader.DOC_VER_TYPE, Documents.PASSPORT_RF));

        fillDocument(document, serial, number, departmentCode, issueDate, issueOrganization);
        document.clean();

        return documentRepository.save(document);
    }

    @Override
    @TransactionAttribute
    public DocumentEntity updateDocument(DocumentEntity document, String serial, String number, String departmentCode,
                                         Date issueDate, String issueOrganization) {
        fillDocument(document, serial, number, departmentCode, issueDate, issueOrganization);
        return document;
    }

    @Override
    @TransactionAttribute
    public DocumentEntity saveDocument(Integer peopleId, String passportSerial, String passportNumber,
                                       String passportDepartmentCode, Date passportIssueDate,
                                       String passportIssueOrganization) throws ObjectNotFoundException {
        PeopleMainEntity peopleMain = peopleMainRepository.findOne(peopleId);
        if (peopleMain == null) {
            throw new ObjectNotFoundException("People with id " + peopleId + " not found");
        }

        DocumentEntity document = findActivePassportByPeople(peopleId);
        if (document != null) {
            document = updateDocument(document, passportSerial, passportNumber, passportDepartmentCode,
                    passportIssueDate, passportIssueOrganization);

            return documentRepository.save(document.clean());
        } else {
            return createDocument(peopleMain, passportSerial, passportNumber, passportDepartmentCode, passportIssueDate,
                    passportIssueOrganization);
        }
    }

    @Override
    @TransactionAttribute
    public DocumentEntity saveAndArchiveDocument(Integer peopleId, String passportSerial, String passportNumber,
                                                 String passportDepartmentCode, Date passportIssueDate,
                                                 String passportIssueOrganization) throws ObjectNotFoundException {
        PeopleMainEntity peopleMain = peopleMainRepository.findOne(peopleId);
        if (peopleMain == null) {
            throw new ObjectNotFoundException("People with id " + peopleId + " not found");
        }

        DocumentEntity document = findActivePassportByPeople(peopleId);
        if (document != null) {
            document = updateDocument(document, passportSerial, passportNumber, passportDepartmentCode,
                    passportIssueDate, passportIssueOrganization);
            if (document.isDirty()) {
                try {
                    DocumentEntity documentArchive = (DocumentEntity) document.clone();
                    documentRepository.save(documentArchive.archive());
                } catch (CloneNotSupportedException e) {
                    throw new KassaRuntimeException("Error cloning PeoplePersonalEntity with id " + document.getId());
                }
            }

            return document;
        } else {
            return createDocument(peopleMain, passportSerial, passportNumber, passportDepartmentCode, passportIssueDate,
                    passportIssueOrganization);
        }
    }

    private void fillDocument(DocumentEntity document, String serial, String number, String departmentCode,
                              Date issueDate, String issueOrganization) {
        document.setSeries(Convertor.fromMask(serial));
        document.setNumber(number);
        document.setDocorgcode(departmentCode);
        document.setDocdate(issueDate);
        document.setDocorg(Convertor.toLimitString(issueOrganization, 255));
    }
}
