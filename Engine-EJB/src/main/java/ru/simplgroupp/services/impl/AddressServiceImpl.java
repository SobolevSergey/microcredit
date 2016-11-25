package ru.simplgroupp.services.impl;

import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.persistence.repository.AddressRepository;
import ru.simplgroupp.services.AddressService;
import ru.simplgroupp.transfer.FiasAddress;
import ru.simplgroupp.transfer.Partner;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

/**
 * Address service implementation
 */
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AddressServiceImpl implements AddressService {

    @Inject
    private AddressRepository addressRepository;

    @Override
    public AddressEntity findActiveRegistrationAddressByPeople(Integer peopleId) {
        return addressRepository.findActiveByPeople(peopleId, FiasAddress.REGISTER_ADDRESS, Partner.CLIENT);
    }

    @Override
    public AddressEntity findActiveResidentialAddressByPeople(Integer peopleId) {
        return addressRepository.findActiveByPeople(peopleId, FiasAddress.RESIDENT_ADDRESS, Partner.CLIENT);
    }
}
