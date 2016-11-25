package ru.simplgroupp.interfaces;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.transfer.IdentityQuestion;

import java.util.List;
import java.util.Set;

/**
 * Бин для работы с вопросами идентификации пользователя
 */
public interface IdentityQuestionLocal {
    /**
     * возвращает список всех вопросов для подстановки данных (не фейковые)
     *
     * @param fake   фейковый ли вопрос
     * @param active активный ли вопрос
     * @return список шаблонов
     */
    List<IdentityTemplateEntity> findTemplates(Boolean fake, Boolean active);

    /**
     * возвращает вопросы по разным критериям
     *
     * @param creditRequestId id запроса
     * @param creditId        id кредита
     * @param peopleMainId    id человека
     * @return список трансферных объектов
     */
    List<IdentityQuestion> findQuestions(Integer creditRequestId, Integer creditId, Integer peopleMainId, Set options);

    /**
     * возвращает список кредитов от кредитных бюро (partners_id не 1 и не 6) для кредитного запроса
     *
     * @param creditRequestEntity запрос кредита
     * @return список кредитов
     */
    List<CreditEntity> getCreditEntitiesFromKb(CreditRequestEntity creditRequestEntity);

    /**
     * удаляет вопросы по creditRequest
     *
     * @param creditRequestId кредитный запрос
     * @return если true то запрос выполнился успешно
     */
    boolean removeQuestionsByCreditRequestId(int creditRequestId);

    /**
     * возвращает шаблон по id из бд
     *
     * @param id id из бд
     * @return сущность или null
     */
    IdentityTemplateEntity getTemplate(int id);

    /**
     * возвращает вариант ответа из бд
     *
     * @param id id варанта ответа
     * @return вариант ответа
     */
    IdentityOptionEntity getOption(int id);

    /**
     * есть ли у данного лица уже сгенерированные вопросы
     *
     * @param peopleMainId id человека
     * @return true если есть, иначе false
     */
    boolean hasQuestions(int peopleMainId);

    /**
     * генерирует вопросы и сразу же сохраняет их, главный метод для использования
     *
     * @param creditRequestId id кредитного запроса к которому привязываются вопросы
     * @param questionsAmount общее количество вопросов
     * @param answersAmount   количество вариантов ответов
     * @param useFake         использовать ли фейковые вопросы
     * @return возвращает список вопросов, иначе возвращает null если вопросы уже были заданы данному лицу
     */
    List<IdentityQuestionEntity> getAndSaveQuestions(int creditRequestId,
                                                     int questionsAmount, int answersAmount, boolean useFake);

    /**
     * генерирует вопросы, но не сохраняет их
     *
     * @param creditRequestId id кредитного запроса к которому привязываются вопросы
     * @param questionsAmount общее количество вопросов
     * @param answersAmount   количество вариантов ответов
     * @param useFake         использовать ли фейковые вопросы
     * @return возвращает список вопросов
     */
    List<IdentityQuestionEntity> getQuestions(int creditRequestId, int questionsAmount, int answersAmount, boolean useFake);

    /**
     * генерирует список вопросов для списка кредитов
     *
     * @param creditEntities список кредитов
     * @param questions      количество вопросов по кредиту
     * @param answers        количество вариантов ответов по кредиту
     * @return возвращает список уникальных вопросов
     */
    List<IdentityQuestionEntity> generateRealQuestions(List<CreditEntity> creditEntities, int questions, int answers);

    /**
     * генерирует фейковые вопросы вместе с вариантами ответов
     *
     * @param peopleMain      лицо
     * @param creditRequest   кредитный запрос к которому привяжем вопрос
     * @param questionsAmount количество вопросов
     * @param answersAmount   количество вариантов ответов
     * @return список вопросов
     */
    List<IdentityQuestionEntity> generateFakeQuestions(PeopleMainEntity peopleMain,
                                                       CreditRequestEntity creditRequest,
                                                       int questionsAmount, int answersAmount);

    /**
     * заполнить шаблон вопроса
     *
     * @param creditEntity кредит
     * @param template     вопрос
     * @return возвращает заполненный вопрос
     */
    IdentityQuestionEntity buildQuestion(IdentityQuestionEntity question,
                                         CreditEntity creditEntity,
                                         IdentityTemplateEntity template) throws KassaException;

    /**
     * генерирует фейковый вопрос
     *
     * @param template шаблон
     * @return вопрос
     * @throws KassaException бросает исключение если вопрос нельзя сгенерировать
     */
    IdentityQuestionEntity buildFakeQuestion(IdentityTemplateEntity template) throws KassaException;

    /**
     * возвращает вопрос для ответа да или нет
     *
     * @param question вопрос
     * @param credits  список кредитов
     * @param template шаблон
     * @return вопрос с вариантами ответов
     * @throws KassaException
     */
    IdentityQuestionEntity buildBooleanQuestion(IdentityQuestionEntity question,
                                                List<CreditEntity> credits,
                                                IdentityTemplateEntity template) throws KassaException;

    /**
     * сгенерировать варианты для вопроса
     *
     * @param question вопрос
     * @param amount   количество вариантов ответа
     * @return список вариантов
     */
    List<IdentityOptionEntity> buildOptions(IdentityQuestionEntity question, int amount) throws KassaException;

    /**
     * генерирует варианты ответов для фейковых вопросов
     * варианты выбираются случайно, если дата то диапазон +-30 дней, если сумма то диапазон до 50к,
     * правильный ответ в данных вопросах "ни один из выше перечисленных"
     *
     * @param question вопрос
     * @param amount   количество вариантов ответов
     * @return список вариантов ответов
     */
    List<IdentityOptionEntity> buildFakeOptions(IdentityQuestionEntity question, int amount) throws KassaException;

    /**
     * удаляет шаблон вопроса
     *
     * @param templateId id шаблона
     * @return true если удаление успешно, иначе false
     */
    boolean removeTemplate(int templateId);

    /**
     * сохраняет шаблон вопроса
     *
     * @param template шаблон
     * @return сохраненный шаблон
     */
    IdentityTemplateEntity saveTemplate(IdentityTemplateEntity template);

    /**
     * сохраняет вопрос в бд
     *
     * @param identityQuestionTemplateEntity сущность
     * @return возвращает сохраненную сущность
     */
    IdentityQuestionEntity saveQuestion(IdentityQuestionEntity identityQuestionTemplateEntity);

    /**
     * сохраняет ответ и если все вопросы с ответами сохраняет балл верификации
     *
     * @param optionId id варианта ответа
     * @return ответ
     */
    IdentityAnswerEntity saveAnswerAndCalculate(int optionId);

    /**
     * сохраняет ответ
     *
     * @param optionId id варианта ответа из бд
     * @return ответ
     */
    IdentityAnswerEntity saveAnswer(int optionId);

    /**
     * возвращает ответ на вопрос если он есть
     *
     * @param questionId id вопроса
     * @return ответ, либо null
     */
    IdentityAnswerEntity getAnswerByQuestion(int questionId);

    /**
     * есть ли вопросы без ответа для кредитного запроса
     *
     * @param creditRequestId id запроса кредита
     * @param creditId        id кредита к которому привязаны вопросы
     * @param peopleMainId    id человека для которого задавали вопросы
     * @return true - все вопросы имеют ответ, false - один или несколько вопросов не имеют ответа
     * так же true будет если вопросы вообще не задавались
     */
    boolean isComplete(Integer creditRequestId, Integer creditId, Integer peopleMainId);

    /**
     * есть ли вопросы без ответа для кредитного запроса
     *
     * @param creditRequestId id запроса кредита
     * @return true - все вопросы имеют ответ, false - один или несколько вопросов не имеют ответа
     * так же true будет если вопросы вообще не задавались
     */
    boolean isCompleteForPeople(int creditRequestId);

    /**
     * проверяет корректность ответа на вопрос по id
     *
     * @param questionId id вопроса
     * @return true если ответ верный иначе false, если null значит ответа еще нет
     * @deprecated {@see IdentityQuestion#getCorrect}
     */
    @Deprecated
    Boolean isCorrect(int questionId);

    /**
     * подсчитать балл верификации и сохранить его
     *
     * @param creditRequestId id кредитного запроса
     */
    int calculatePoints(int creditRequestId);

    /**
     * сохрнаить балл верификации, только если ответы были даны на все вопросы
     *
     * @param points сколько баллов
     * @return возвращает true если ок, возвращает false, если не на все вопросы даны ответы
     */
    boolean savePoints(int points, int creditRequestId);

    /**
     * получить значение используя java reflect api
     *
     * @param object объект
     * @param field  поле, можно использовать иерархию вида: partnersId.name
     * @return значение или null
     */
    Object getValueByReflectApi(Object object, String field) throws KassaException;

    /**
     * Возвращает количество верных ответов
     *
     * @param creditRequestId id запроса кредита
     * @param creditId        id кредита к которому привязаны вопросы
     * @param peopleMainId    id человека для которого задавали вопросы
     * @return количество верных ответов, если вопросы не были заданы возвращает 0
     */
    Integer countRightAnswers(Integer creditRequestId, Integer creditId, Integer peopleMainId);

    /**
     * Возвращает количество заданных вопросов
     *
     * @param creditRequestId id запроса кредита
     * @param creditId        id кредита к которому привязаны вопросы
     * @param peopleMainId    id человека для которого задавали вопросы
     * @return количество заданных вопросов
     */
    int countQuestions(Integer creditRequestId, Integer creditId, Integer peopleMainId);

    /**
     * возвращает случайное число в диапазоне
     *
     * @param min минимальное значение (возможно отрицательное)
     * @param max максимальное значение
     * @return случайное число
     */
    int randInt(int min, int max);
}
