package ru.simplgroupp.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.simplgroupp.persistence.DocumentEntity;

import java.util.List;

/**
 * Document repository
 */
public interface DocumentRepository extends CrudRepository<DocumentEntity, Integer> {

    @Query("SELECT d FROM DocumentEntity d " +
            "WHERE d.peopleMainId.id = :peopleId AND documenttypeId.codeinteger =:documentType " +
            "AND partnersId.id = :partnerId AND isactive = 1")
    List<DocumentEntity> findActiveByPeople(@Param("peopleId") Integer peopleId, @Param("documentType") Integer
            documentType, @Param("partnerId") Integer partnerId, Pageable pageable);
}
