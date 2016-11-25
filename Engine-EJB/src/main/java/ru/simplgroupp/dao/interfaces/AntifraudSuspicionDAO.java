package ru.simplgroupp.dao.interfaces;

import ru.simplgroupp.persistence.antifraud.AntifraudSuspicionEntity;
import ru.simplgroupp.persistence.antifraud.RefAntifraudRulesEntity;
import ru.simplgroupp.persistence.antifraud.RefHunterRuleEntity;
import ru.simplgroupp.transfer.antifraud.AntifraudSuspicion;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Local
public interface AntifraudSuspicionDAO {
    /**
     * возвращает сущность
     *
     * @param id id сущности в бд
     * @return сущность
     */
    AntifraudSuspicionEntity getEntity(int id);

    /**
     * возвращает трансферный объект подозрения
     *
     * @param id      id в бд
     * @param options какие коллекции инициализируем
     * @return трансферный объект
     */
    AntifraudSuspicion getSuspicion(int id, Set options);

    /**
     * генерирует сущность на основе параметров
     * нужно для того что бы не инициировать кучу зависимостей в других классах
     * где нам надо создать и сохранить подозрение
     *
     * @param creditRequestId id кредитной заявки
     * @param peopleId        id человека которого проверяем
     * @param partnerId       id партнера (система)
     * @param rule            внутреннее антимошенническое правило
     * @param secondPeopleId  id второго человека если он будет найден
     * @param fraud           мошенничество ли это (инициализируется всегда false)
     * @param comment         комментарий
     * @param score           баллы
     * @param createdAt       когда создано
     * @param hunterCode      код из хантера, если мы эту запись получили
     * @return сущность готовую к сохранению
     */
    AntifraudSuspicionEntity buildSuspicion(int creditRequestId, int peopleId, int partnerId, RefAntifraudRulesEntity rule,
                                            Integer secondPeopleId, Boolean fraud, String comment,
                                            Double score, Date createdAt,String hunterCode);

    /**
     * генерирует сущность на основе параметров
     * нужно для того что бы не инициировать кучу зависимостей в других классах
     * где нам надо создать и сохранить подозрение
     *
     * @param creditRequestId id кредитной заявки
     * @param peopleId        id человека которого проверяем
     * @param partnerId       id партнера (система)
     * @param rule            внутреннее антимошенническое правило
     * @param secondPeopleId  id второго человека если он будет найден
     * @param fraud           мошенничество ли это (инициализируется всегда false)
     * @param comment         комментарий
     * @param score           баллы
     * @param createdAt       когда создано
     * @return сущность готовую к сохранению
     */
    AntifraudSuspicionEntity buildSuspicion(int creditRequestId, int peopleId, int partnerId, RefAntifraudRulesEntity rule,
                                            Integer secondPeopleId, Boolean fraud, String comment,
                                            Double score, Date createdAt);
    /**
     * сохраняет сущность
     *
     * @param entity сущность
     * @return возвращает сохраненную сущность с id
     */
    AntifraudSuspicionEntity saveSuspicion(AntifraudSuspicionEntity entity);

    /**
     * найти подозрения по параметрам
     *
     * @param peopleId       id человека котрого проверяли
     * @param partnerId      id партнера
     * @param ruleId         id правила срабатываения
     * @param secondPeopleId id второго человека
     * @param fraud          мошшеничество ли это
     * @return список сущностей
     */
    List<AntifraudSuspicion> find(Integer creditRequestId, Integer peopleId, Integer partnerId,
                                  Integer ruleId, Integer secondPeopleId, Boolean fraud, Set options);

    /**
     * Найти правило Хантера по коду
     *
     * @param code код правила Хантера
     * @return сущность если правило найдено иначе null
     */
    RefHunterRuleEntity findHunterRule(String code);

    /**
     * удаляет сущность
     *
     * @param id id сущности в бд
     */
    void delete(int id);
}
