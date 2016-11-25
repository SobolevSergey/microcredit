package ru.simplgroupp.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.simplgroupp.persistence.SpouseEntity;

import java.util.List;

/**
 * Spouse repository
 */
public interface SpouseRepository extends CrudRepository<SpouseEntity, Integer> {

    @Query("SELECT s FROM SpouseEntity s WHERE s.peopleMainId.id = :peopleId AND s.isactive = 1")
    List<SpouseEntity> findActiveByPeople(@Param("peopleId") Integer peopleId, Pageable pageable);
}
