package ru.simplgroupp.services;

import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;
import java.util.Date;

/**
 * Document service
 */
@Local
public interface DocumentService {

    DocumentEntity findById(Integer documentId);

    DocumentEntity findActivePassportByPeople(Integer peopleId);

    /**
     * Создать документ
     *
     * @param peopleMain        владелец документа
     * @param serial            серия
     * @param number            номер
     * @param departmentCode    код подразделения
     * @param issueDate         дата выдачи
     * @param issueOrganization кем выдан
     * @return документ
     */
    DocumentEntity createDocument(PeopleMainEntity peopleMain, String serial, String number, String departmentCode,
                                  Date issueDate, String issueOrganization);

    DocumentEntity updateDocument(DocumentEntity document, String serial, String number, String departmentCode,
                                  Date issueDate, String issueOrganization);

    DocumentEntity saveDocument(Integer peopleId, String passportSerial, String passportNumber,
                                String passportDepartmentCode, Date passportIssueDate, String passportIssueOrganization)
            throws ObjectNotFoundException;

    DocumentEntity saveAndArchiveDocument(Integer peopleId, String passportSerial, String passportNumber,
                                          String passportDepartmentCode, Date passportIssueDate,
                                          String PassportIssueOrganization) throws ObjectNotFoundException;
}
