package ru.simplgroupp.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.simplgroupp.persistence.PeopleMiscEntity;

import java.util.List;

/**
 * People misc repository
 */
public interface PeopleMiscRepository extends CrudRepository<PeopleMiscEntity, Integer> {

    @Query("SELECT pm FROM PeopleMiscEntity pm WHERE pm.peopleMainId.id = :peopleId " +
            "AND pm.partnersId.id = :partnerId AND pm.isactive = 1")
    List<PeopleMiscEntity> findActiveByPeople(@Param("peopleId") Integer peopleId,
                                              @Param("partnerId") Integer partnerId, Pageable pageable);
}
