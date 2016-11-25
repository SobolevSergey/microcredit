package ru.simplgroupp.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.simplgroupp.persistence.PeopleContactEntity;

import java.util.List;

/**
 * People contact repository
 */
public interface PeopleContactRepository extends CrudRepository<PeopleContactEntity, Integer> {

    @Query("SELECT c FROM PeopleContactEntity c WHERE peopleMainId.id = :peopleId AND contactId.codeinteger =:typeId " +
            "AND partnersId.id = :partnerId AND c.isactive = 1")
    List<PeopleContactEntity> findActiveByPeople(@Param("peopleId") Integer peopleId, @Param("typeId") int typeId,
                                                 @Param("partnerId") Integer partnerId, Pageable pageable);
}
