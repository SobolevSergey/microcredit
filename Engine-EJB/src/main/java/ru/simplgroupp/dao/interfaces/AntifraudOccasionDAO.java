package ru.simplgroupp.dao.interfaces;

import ru.simplgroupp.persistence.QuestionEntity;
import ru.simplgroupp.persistence.QuestionVariantEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudOccasionEntity;
import ru.simplgroupp.transfer.antifraud.AntifraudOccasion;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Local
public interface AntifraudOccasionDAO {
    /**
     * возвращает сущность
     *
     * @param id id сущности в бд
     * @return сущность
     */
    AntifraudOccasionEntity getEntity(int id);

    /**
     * возвращает трансферный объект
     *
     * @param id      id объекта
     * @param options какие коллекции инициализируем
     * @return объект или null
     */
    AntifraudOccasion getMain(int id, Set options);

    /**
     * генерируем сущность
     *
     * @param userId           id пользователя который создал эту сущность
     * @param creditRequestId  id кредитного запроса
     * @param peopleId         id человека
     * @param hunterObjectCode строковый код в системе правил Национальный хантер
     * @param statusCode       код статуса мошенничества
     * @param createdAt        когда создано (если null задается автоматически как текущее время)
     * @param updatedAt        когда обновлено (если null задается автоматически как текущее время)
     * @param comment          комментарий
     * @return возвращает не сохраненную сущность
     */
    AntifraudOccasionEntity buildEntity(Integer userId, int creditRequestId, int peopleId, String hunterObjectCode,
                                        int statusCode, Date createdAt, Date updatedAt, String comment);

    /**
     * генерируем сущность
     *
     * @param userId                id пользователя который создал эту сущность
     * @param creditRequestId       id кредитного запроса
     * @param peopleId              id человека
     * @param hunterObjectCode      строковый код в системе правил Национальный хантер
     * @param statusCode            код статуса мошенничества
     * @param createdAt             когда создано (если null задается автоматически как текущее время)
     * @param updatedAt             когда обновлено (если null задается автоматически как текущее время)
     * @param comment               комментарий
     * @param questionVariantEntity вариант ответа по которому создана запись
     * @return возвращает не сохраненную сущность
     */
    AntifraudOccasionEntity buildEntity(Integer userId, int creditRequestId, int peopleId, String hunterObjectCode,
                                        int statusCode, Date createdAt, Date updatedAt, String comment,
                                        QuestionVariantEntity questionVariantEntity);

    /**
     * сохраняет сущность
     *
     * @param entity сущность
     * @return возвращает сохраненную сущность
     */
    AntifraudOccasionEntity save(AntifraudOccasionEntity entity);

    /**
     * поиск сущностей по параметрам
     *
     * @param creditRequestId  id кредитного запроса
     * @param peopleId         id человека
     * @param hunterObjectCode строковый код в системе правил Национальный хантер
     * @param options          какие коллекции инициализируем
     * @param isActive         активна ли запись
     * @return возвращает список трансферных объектов
     */
    List<AntifraudOccasion> find(Integer creditRequestId, Integer peopleId, String hunterObjectCode,
                                 Integer statusCode, Integer isActive, Set options);

    /**
     * удалить сущность по id
     *
     * @param id id сущности в бд
     */
    void delete(int id);

    /**
     * сохранение сущности с проверкой возможно ли сохранение (наличие других случаев для текущего кредитного запроса)
     *
     * @param creditRequestId  id кредитного запроса
     * @param peopleMainId     id человека
     * @param hunterObjectCode код сущности Хантера
     * @param comment          комментарий
     */
    void saveWithCheck(int creditRequestId, int peopleMainId, String hunterObjectCode, String comment);

    /**
     * сохранение сущности с проверкой возможно ли сохранение (наличие других случаев для текущего кредитного запроса)
     *
     * @param userId           id пользователя который создал эту сущность
     * @param creditRequestId  id кредитного запроса
     * @param peopleId         id человека
     * @param hunterObjectCode строковый код в системе правил Национальный хантер
     * @param statusCode       код статуса мошенничества
     * @param createdAt        когда создано (если null задается автоматически как текущее время)
     * @param updatedAt        когда обновлено (если null задается автоматически как текущее время)
     * @param comment          комментарий
     */
    void saveWithCheck(Integer userId, int creditRequestId, int peopleId, String hunterObjectCode,
                       int statusCode, Date createdAt, Date updatedAt, String comment);

    /**
     * сохранение сущности с проверкой возможно ли сохранение (наличие других случаев для текущего кредитного запроса)
     *
     * @param userId                id пользователя который создал эту сущность
     * @param creditRequestId       id кредитного запроса
     * @param peopleId              id человека
     * @param hunterObjectCode      строковый код в системе правил Национальный хантер
     * @param statusCode            код статуса мошенничества
     * @param createdAt             когда создано (если null задается автоматически как текущее время)
     * @param updatedAt             когда обновлено (если null задается автоматически как текущее время)
     * @param comment               комментарий
     * @param questionVariantEntity вариант ответа по которому создана запись
     */
    void saveWithCheck(Integer userId, int creditRequestId, int peopleId, String hunterObjectCode,
                       int statusCode, Date createdAt, Date updatedAt, String comment,
                       QuestionVariantEntity questionVariantEntity);

    /**
     * возвращает антимошеннические случаи по вопросу
     *
     * @param questionEntity варант ответа
     * @param options        инициализация
     */
    List<AntifraudOccasion> getByQuestion(QuestionEntity questionEntity, Set options);
}
