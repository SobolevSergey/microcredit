package ru.simplgroupp.services;

import ru.simplgroupp.persistence.AddressEntity;

import javax.ejb.Local;

/**
 * Address service
 */
@Local
public interface AddressService {

    /**
     * Найти адрес регистрации
     *
     * @param peopleId идентификатор человека
     * @return адрес
     */
    AddressEntity findActiveRegistrationAddressByPeople(Integer peopleId);

    /**
     * Найти адрес проживания
     *
     * @param peopleId идентификатор человека
     * @return адрес
     */
    AddressEntity findActiveResidentialAddressByPeople(Integer peopleId);
}
