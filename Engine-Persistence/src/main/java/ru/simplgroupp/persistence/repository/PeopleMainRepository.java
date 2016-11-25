package ru.simplgroupp.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import ru.simplgroupp.persistence.PeopleMainEntity;

/**
 * People main repository
 */
public interface PeopleMainRepository extends CrudRepository<PeopleMainEntity, Integer> {

}
