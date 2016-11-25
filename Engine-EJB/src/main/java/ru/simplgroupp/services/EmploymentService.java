package ru.simplgroupp.services;

import ru.simplgroupp.persistence.EmploymentEntity;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;
import java.util.Date;

/**
 * Employment service
 */
@Local
public interface EmploymentService {

    EmploymentEntity findCurrentByPeople(Integer peopleId);

    EmploymentEntity saveEmployment(Integer peopleId, Integer occupationId, Integer educationId, Double salary,
                                    Date enterEmploymentDate, Double additionalIncome, Integer additionalIncomeSourceId,
                                    Integer professionId, Integer salaryFrequenceId, String employmentPlace,
                                    String position, Date enterPositionDate) throws ObjectNotFoundException;
}
