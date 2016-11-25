package ru.simplgroupp.interfaces;

import ru.simplgroupp.persistence.CallsEntity;

import java.util.Date;

/**
 * Бин для работы с работы со звонками
 */
public interface CallsLocal {
    /**
     * сохряняет произведенный звонок
     *
     * @param startCall дата начала звонка
     * @param endCall   дата окончания
     * @param status    статус
     * @param phone     номер клиента (при входящем или исходящем)
     * @param incoming  если true значит входящий, иначе исходящий
     * @param uuid      идентификатор оператора в oktell
     * @return возвращает сущность или null
     */
    CallsEntity saveCall(Date startCall, Date endCall, Integer status, String phone, boolean incoming, String uuid);
}
