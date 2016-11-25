package ru.simplgroupp.interfaces;

import ru.simplgroupp.fias.persistence.AddrObj;
import ru.simplgroupp.persistence.AddressEntity;

import javax.ejb.Local;

@Local
public interface AddressBeanLocal {

    /**
     * пишем данные региона из фиас в адрес
     *
     * @param param guid региона
     * @param aadr  адрес
     */
    void setNameForRegion(String param, AddressEntity aadr);

    /**
     * пишем название для любого адресного объекта
     *
     * @param param guid передаваемого объекта
     */

    String setNameFromAddrObj(String param);

    /**
     * пишем ext для любого адресного объекта
     *
     * @param param guid передаваемого объекта
     */
    String setExtFromAddrObj(String param);

    /**
     * заполняем адрес данными из фиас
     *
     * @param address           адрес
     * @param fiasAddressObject объект фиас
     */
    void fillAddress(AddressEntity address, AddrObj fiasAddressObject);

    /**
     * сохраняем адрес
     *
     * @param address адрес
     * @return
     */
    public AddressEntity saveAddressEntity(AddressEntity address);
    
    /**
     * ищем последний элемент фиас, по которому можно восстановть цепочку
     * 
     * @param address- адрес
     * @return
     */
    String findLastFiasGuid(AddressEntity address);
    /**
     * возвращаем адрес по id
     * 
     * @param id - id адреса
     * @return
     */
    AddressEntity getAddress(Integer id);

}
