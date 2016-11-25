package ru.simplgroupp.interfaces.service;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.transfer.creditrequestwizard.Step0Data;
import ru.simplgroupp.transfer.creditrequestwizard.Step1Data;
import ru.simplgroupp.transfer.creditrequestwizard.Step6Data;

import java.util.Map;

/**
 * Service for credit request wizard
 */
public interface CreditRequestWizardService {

    /**
     * Данные для шага 0
     *
     * @param creditRequestId - credit request id
     * @param isFirstTime - is entered in first time
     */
    Step0Data step0(Integer creditRequestId, boolean isFirstTime);

    /**
     * сохраняем шаг 0, возвращаем id заявки 
     *
     * @param creditRequestId идентификатор заявки на кредит
     * @param step0 данные шага 0
     */
    Integer saveStep0(Integer creditRequestId, Step0Data step0) throws KassaException;

    /**
     * возвращаем параметры для возвращения на шаг 1
     *
     * @param creditRequestId - id заявки
     */
    Step1Data step1(Integer creditRequestId);


    /**
     * сохраняем шаг 1
     *
     * @param creditRequestId идентификатор заявки на кредит
     * @param step1 данные шага 1
     */
    void saveStep1(Integer creditRequestId, Step1Data step1) throws KassaException;

    /**
     * возвращаем параметры для возвращения на шаг 6
     *
     * @param creditRequestId - id заявки
     */
    Step6Data step6(Integer creditRequestId);

    /**
     * сохраняем 5 шаг, возвращаем id заявки
     *
     * @param params - описание в вики
     */
    Integer saveStep5(Map<String, Object> params) throws KassaException;

    /**
     * сохраняем шаг 6, возвращаем счет человека
     * 
     * @param creditRequestId идентификатор заявки на кредит
     * @param step6 данные шага 6
     */
    AccountEntity saveStep6(Integer creditRequestId, Step6Data step6) throws KassaException;
}
