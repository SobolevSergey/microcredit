package ru.simplgroupp.dao.interfaces;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.CallResultEntity;
import ru.simplgroupp.persistence.CallsEntity;
import ru.simplgroupp.transfer.Call;
import ru.simplgroupp.transfer.CallResult;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Local
public interface CallsDAO {

    /**
     * возвращает звонок по id
     *
     * @param id - id звонка
     * @return
     */
    public CallsEntity getCallsEntity(Integer id);

    /**
     * возвращает результат звонка по id
     *
     * @param id - id звонка
     * @return
     */
    public CallResultEntity getCallResult(Integer id);

    /**
     * удаляет звонок по id
     *
     * @param id - id звонка
     * @return
     */
    public void removeCall(Integer id);

    /**
     * сохраняем звонок
     *
     * @param call - звонок
     * @return
     */
    public CallsEntity saveCallsEntity(CallsEntity call);

    /**
     * новый звонок
     *
     * @param peopleMainId - человек, кому звонили
     * @param userId       - пользователь, сотрудник
     * @param statusId     - статус звонка
     * @param databeg      - дата начала звонка
     * @param dataend      - дата окончания звонка
     * @param phone        - телефон
     * @param isClientCall - звонил ли клиент
     * @param comment      - комментарии
     * @param callData     - сам разговор
     * @return
     */
    public CallsEntity newCall(Integer peopleMainId, Integer userId, Integer statusId, Date databeg,
                               Date dataend, String phone, Integer isClientCall, boolean incoming,
                               String comment, byte[] callData) throws KassaException;

    /**
     * результат звонка
     *
     * @param peopleMainId    - id клиента
     * @param userId          - id оператора
     * @param resultTypeId    - id результата
     * @param dateCall        - дата звонка
     * @param datePromise     - дата обещания
     * @param dateNextContact - дата следующего контакта
     * @param comment         - комментарии
     * @return
     */
    public CallResultEntity newCallResult(Integer peopleMainId, Integer userId, Integer resultTypeId,
                                          Date dateCall, Date datePromise, Date dateNextContact, String comment) throws KassaException;

    /**
     * Метод достает результаты обзвона по человеку (кому был сделан звонок) и по пользователю (кто звонил)
     *
     * @param peopleMainID кому звонили
     * @param userID       кто звонил
     */
    List<CallResult> getCallResultsByPeopleAndUserID(Integer peopleMainID, Integer userID);

    /**
     * Метод достает результаты обзвона по человеку (кому был сделан звонок)
     *
     * @param peopleMainID кому звонили
     */
    List<CallResult> getCallResultsByPeople(Integer peopleMainID);

    /**
     * Метод достает звонки
     *
     * @param peopleMainID кому звонили
     * @param userID       кто звонил
     * @return список звонков
     */
    List<Call> getCallsByPeopleAndUserID(Integer peopleMainID, Integer userID);

    /**
     * возвращает список звонков для человека
     *
     * @param peopleMainId id человека
     * @return список совершенных звонков
     */
    List<Call> getCallsByPeople(Integer peopleMainId, Set options);
}
