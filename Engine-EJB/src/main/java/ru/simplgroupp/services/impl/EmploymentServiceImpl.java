package ru.simplgroupp.services.impl;

import org.springframework.data.domain.PageRequest;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.EmploymentEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.repository.EmploymentRepository;
import ru.simplgroupp.persistence.repository.PartnerRepository;
import ru.simplgroupp.persistence.repository.PeopleMainRepository;
import ru.simplgroupp.services.EmploymentService;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.RefHeader;

import javax.ejb.ObjectNotFoundException;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Employment service implementation
 */
@Singleton
@TransactionAttribute
public class EmploymentServiceImpl implements EmploymentService {

    @Inject
    private EmploymentRepository employmentRepository;

    @Inject
    private PeopleMainRepository peopleMainRepository;

    @Inject
    private PartnerRepository partnerRepository;

    @Inject
    private ReferenceBooksLocal referenceBooks;

    public EmploymentEntity findCurrentByPeople(Integer peopleId) {
        List<EmploymentEntity> list = employmentRepository.findCurrentByPeople(peopleId, Partner.CLIENT,
                new PageRequest(0, 1));
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public EmploymentEntity saveEmployment(Integer peopleId, Integer occupationId, Integer educationId, Double salary,
                                           Date enterEmploymentDate, Double additionalIncome,
                                           Integer additionalIncomeSourceId, Integer professionId,
                                           Integer salaryFrequenceId, String employmentPlace, String position,
                                           Date enterPositionDate) throws ObjectNotFoundException {
        PeopleMainEntity peopleMain = peopleMainRepository.findOne(peopleId);
        if (peopleMain == null) {
            throw new ObjectNotFoundException("People with id " + peopleId + " not found");
        }

        EmploymentEntity employment = findCurrentByPeople(peopleId);
        if (employment != null) {
            return updateEmployment(employment, occupationId, educationId, salary, enterEmploymentDate,
                    additionalIncome, additionalIncomeSourceId, professionId, salaryFrequenceId, employmentPlace,
                    position, enterPositionDate);
        } else {
            return createEmployment(peopleMain, occupationId, educationId, salary, enterEmploymentDate,
                    additionalIncome, additionalIncomeSourceId, professionId, salaryFrequenceId, employmentPlace,
                    position, enterPositionDate);
        }
    }

    private EmploymentEntity createEmployment(PeopleMainEntity peopleMain, Integer occupationCode,
                                              Integer educationCode, Double salary, Date enterEmploymentDate,
                                              Double additionalIncome, Integer additionalIncomeSourceId,
                                              Integer professionCode, Integer salaryFrequenceCode,
                                              String employmentPlace, String position, Date enterPositionDate) {
        PartnersEntity partner = partnerRepository.findOne(Partner.CLIENT);

        EmploymentEntity employment = new EmploymentEntity();
        employment.setPeopleMainId(peopleMain);
        employment.setPartnersId(partner);
        employment.setDatabeg(new Date());
        employment.setCurrent(1);

        employment.setEducationId(referenceBooks.findByCodeIntegerEntity(RefHeader.EDUCATION_TYPE, educationCode));
        employment.setTypeworkId(referenceBooks.findByCodeIntegerEntity(RefHeader.EMPLOY_TYPE, occupationCode));
        employment.setOccupationId(referenceBooks.findByCodeIntegerEntity(RefHeader.PROFESSION_TYPE, professionCode));
        employment.setDurationId(
                referenceBooks.findByCodeIntegerEntity(RefHeader.INCOME_FREQ_TYPE, salaryFrequenceCode));
        if (additionalIncomeSourceId != null) {
            employment.setExtsalaryId(
                    referenceBooks.findByCodeIntegerEntity(RefHeader.EXT_SALARY_TYPE, additionalIncomeSourceId));
        }
        employment.setPlaceWork(employmentPlace);
        employment.setOccupation(position);
        employment.setSalary(salary);
        employment.setExtsalary(additionalIncome);
        employment.setExperience(enterEmploymentDate);
        employment.setDatestartwork(enterPositionDate);

        return employmentRepository.save(employment);
    }

    private EmploymentEntity updateEmployment(EmploymentEntity employment, Integer occupationCode,
                                              Integer educationCode, Double salary, Date enterEmploymentDate,
                                              Double additionalIncome, Integer additionalIncomeSourceId,
                                              Integer professionCode, Integer salaryFrequenceCode,
                                              String employmentPlace, String position, Date enterPositionDate) {
        employment.setEducationId(referenceBooks.findByCodeIntegerEntity(RefHeader.EDUCATION_TYPE, educationCode));
        employment.setTypeworkId(referenceBooks.findByCodeIntegerEntity(RefHeader.EMPLOY_TYPE, occupationCode));
        employment.setOccupationId(referenceBooks.findByCodeIntegerEntity(RefHeader.PROFESSION_TYPE, professionCode));
        employment.setDurationId(
                referenceBooks.findByCodeIntegerEntity(RefHeader.INCOME_FREQ_TYPE, salaryFrequenceCode));
        if (additionalIncomeSourceId != null) {
            employment.setExtsalaryId(
                    referenceBooks.findByCodeIntegerEntity(RefHeader.EXT_SALARY_TYPE, additionalIncomeSourceId));
        }
        employment.setPlaceWork(employmentPlace);
        employment.setOccupation(position);
        employment.setSalary(salary);
        employment.setExtsalary(additionalIncome);
        employment.setExperience(enterEmploymentDate);
        employment.setDatestartwork(enterPositionDate);

        return employmentRepository.save(employment);
    }

}
