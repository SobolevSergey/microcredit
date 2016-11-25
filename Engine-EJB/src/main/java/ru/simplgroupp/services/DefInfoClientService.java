package ru.simplgroupp.services;

import javax.ejb.Local;

/**
 * DEF info client service
 */
public interface DefInfoClientService {

    /**
     * Get DEF info by phone
     *
     * @param phone the phone
     * @return DEF info rest
     * @throws DefInfoClientServiceException
     */
    DefInfoRest getInfo(String phone);
}
