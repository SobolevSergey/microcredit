package ru.simplgroupp.services;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.CreditEntity;

import javax.ejb.Local;
import javax.ejb.ObjectNotFoundException;

/**
 * Credit service
 */
@Local
public interface CreditService {

    /**
     * Cравнивает любые два кредита из разных источников
     *
     * @param creditId1 первый кредит
     * @param creditId2 второй кредит
     * @return true если точно совпали тип, валюта, просроченный статус, и в пределах небольшой погрешности дата начала, сумма
     * @throws ObjectNotFoundException
     * @throws KassaException
     */
    boolean isMatch(Integer creditId1, Integer creditId2) throws ObjectNotFoundException, KassaException;

    /**
     * Cравнивает любые два кредита из разных источников
     *
     * @param credit1 первый кредит
     * @param credit2 второй кредит
     * @return true если точно совпали тип, валюта, просроченный статус, и в пределах небольшой погрешности дата начала, сумма
     * @throws ObjectNotFoundException
     * @throws KassaException
     */
    boolean isMatch(CreditEntity credit1, CreditEntity credit2) throws ObjectNotFoundException, KassaException;

    /**
     * считает оценку соответствия кредита, заполненного клиентом в анкете, кредиту, полученному из кредитного бюро
     *
     * @param creditId1 кредит, заполненный клиентом
     * @param creditId2 кредит, полученный из кредитного бюро
     * @return оценка соответствия
     * @throws ObjectNotFoundException
     * @throws KassaException
     */
    double creditRating(Integer creditId1, Integer creditId2) throws ObjectNotFoundException, KassaException;
    
    /**
     * считает оценку соответствия кредита, заполненного клиентом в анкете, кредиту, полученному из кредитного бюро
     *
     * @param credit1 кредит, заполненный клиентом
     * @param credit2 кредит, полученный из кредитного бюро
     * @return оценка соответствия
     * @throws ObjectNotFoundException
     * @throws KassaException
     */
    double creditRating(CreditEntity credit1, CreditEntity credit2) throws ObjectNotFoundException, KassaException;
}
