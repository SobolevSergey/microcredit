package ru.simplgroupp.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.simplgroupp.persistence.AddressEntity;

/**
 * Address repository
 */
public interface AddressRepository extends CrudRepository<AddressEntity, Integer> {

    @Query("SELECT a FROM AddressEntity a WHERE a.peopleMainId.id = :peopleId AND partnersId.id = :partnerId " +
            "AND a.addrtype.codeinteger = :type AND isactive = 1")
    AddressEntity findActiveByPeople(@Param("peopleId") Integer peopleId, @Param("type") Integer
            type, @Param("partnerId") Integer partnerId);
}
