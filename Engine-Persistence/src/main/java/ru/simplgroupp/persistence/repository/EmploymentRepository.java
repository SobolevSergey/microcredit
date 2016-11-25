package ru.simplgroupp.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.simplgroupp.persistence.EmploymentEntity;

import java.util.List;

/**
 * Employment repository
 */
public interface EmploymentRepository extends CrudRepository<EmploymentEntity, Integer> {

    @Query("SELECT e FROM EmploymentEntity e WHERE e.peopleMainId.id = :peopleId AND e.partnersId.id = :partnerId " +
            "AND e.current = 1")
    List<EmploymentEntity> findCurrentByPeople(@Param("peopleId") Integer peopleId,
                                               @Param("partnerId") Integer partnerId, Pageable pageable);
}
