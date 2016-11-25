package ru.simplgroupp.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.simplgroupp.persistence.PeoplePersonalEntity;

import java.util.List;

/**
 * People personal repository
 */
public interface PeoplePersonalRepository extends CrudRepository<PeoplePersonalEntity, Integer> {

    @Query("SELECT pp FROM PeoplePersonalEntity pp WHERE pp.peopleMainId.id = :peopleId " +
            "AND pp.partnersId.id = :partnerId AND pp.isactive = 1")
    List<PeoplePersonalEntity> findActiveByPeople(@Param("peopleId") Integer peopleId, @Param("partnerId") Integer
            partnerId, Pageable pageable);
}
