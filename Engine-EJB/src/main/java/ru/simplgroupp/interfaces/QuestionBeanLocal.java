package ru.simplgroupp.interfaces;

import ru.simplgroupp.persistence.QuestionAnswerEntity;
import ru.simplgroupp.persistence.QuestionEntity;
import ru.simplgroupp.persistence.QuestionVariantEntity;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.transfer.Question;
import ru.simplgroupp.transfer.QuestionAnswer;
import ru.simplgroupp.transfer.QuestionVariant;

import java.util.List;
import java.util.Set;

/**
 * EJB для работы с вопросами верификатору
 */
public interface QuestionBeanLocal {
    /**
     * список вопросов
     *
     * @param nFirst          - первая запись
     * @param nCount          - всего записей
     * @param sorting         - сортировка
     * @param options         - что инициализируем
     * @param questionCodeTpl - код вопроса
     * @param questionTextTpl - текст вопроса
     * @param answerType      - вид ответа
     * @param productId       - id продукта
     * @return
     */
    List<Question> listQuestion(int nFirst, int nCount, SortCriteria[] sorting,
                                Set options, String questionCodeTpl, String questionTextTpl,
                                Integer answerType, Integer productId);

    /**
     * посчитать вопросы
     *
     * @param questionCodeTpl - код вопроса
     * @param questionTextTpl - текст вопроса
     * @param answerType      - вид ответа
     * @param productId       - id продукта
     * @return
     */
    int countQuestion(String questionCodeTpl, String questionTextTpl,
                      Integer answerType, Integer productId);

    /**
     * ищем вопрос по коду
     *
     * @param code - код вопроса
     */
    QuestionEntity findByCode(String code);

    /**
     * ищем вопрос по коду и продукту
     *
     * @param code      - код вопроса
     * @param productId - id продукта
     */
    QuestionEntity findByCode(String code, Integer productId);

    /**
     * создать вопрос
     *
     * @param code - код вопроса
     * @return
     */
    QuestionEntity createQuestion(String code);

    /**
     * создать вопрос
     *
     * @param code      - код вопроса
     * @param productId - id продукта
     * @return
     */
    QuestionEntity createQuestion(String code, Integer productId);

    /**
     * удалить вопрос
     *
     * @param id - id вопроса
     */
    void removeQuestion(int id);

    /**
     * сохранить вопрос
     *
     * @param source - вопрос
     * @return
     */
    QuestionEntity saveQuestion(QuestionEntity source);

    /**
     * сохраняет вопрос и варианты ответов, метод для трансферного класса
     *
     * @param question траснферный класс
     * @return сохраненный вопрос
     */
    QuestionEntity saveQuestion(Question question);

    /**
     * возвращает вопрос по id
     *
     * @param id      - id вопроса
     * @param options - что инициализируем
     * @return
     */
    Question getQuestion(int id, Set options);

    /**
     * возвращает вопрос по id
     *
     * @param id - id вопроса
     * @return
     */
    QuestionEntity getQuestion(int id);

    /**
     * удалить вариант вопроса
     *
     * @param ent - вариант вопросаа
     */
    void removeQuestionVariants(QuestionEntity ent);

    /**
     * удалить вариант вопроса
     *
     * @param id - varId вопроса
     */
    void removeQuestionVariant(int varId);

    /**
     * добавляем ответ на вопрос
     *
     * @param creditRequestId
     * @param key             - название вопроса
     */
    QuestionAnswerEntity addQA(int creditRequestId, String key);

    /**
     * удаляем ответ на вопрос
     */
    void removeQA(QuestionAnswerEntity ent);

    /**
     * ищем ответ с вопросом
     *
     * @param creditRequestId
     * @param key
     */
    QuestionAnswerEntity findQA(int creditRequestId, String key);

    /**
     * посчитать ответы на вопрос
     *
     * @param creditRequestId - id заявки
     * @param statuses        - статусы
     * @return
     */
    int countQuestionAnswer(Integer creditRequestId, int[] statuses);

    /**
     * список ответов на вопрос
     *
     * @param nFirst          - первая запись
     * @param nCount          - всего записей
     * @param sorting         - сортировка
     * @param options         - что инициализируем
     * @param creditRequestId - id заявки
     * @param statuses        - статусы
     * @return
     */
    List<QuestionAnswer> listQuestionAnswer(int nFirst, int nCount,
                                            SortCriteria[] sorting, Set options, Integer creditRequestId,
                                            int[] statuses);

    /**
     * сохраняем отказ отвечать на вопрос
     *
     * @param creditRequestId
     * @param key
     */
    void saveQARefuse(int creditRequestId, String key);

    /**
     * добавляем лист незаполненных вопросов по заявке
     *
     * @param creditRequestId id кредитного запроса
     */
    List<QuestionAnswer> addQAList(int creditRequestId, List<String> questionCodes);

    /**
     * добавляем лист незаполненных вопросов по заявке
     *
     * @param creditRequestId id кредитного запроса
     * @param options         инициализация
     */
    List<QuestionAnswer> addQAList(int creditRequestId, List<String> questionCodes, Set options);

    /**
     * ищем id варианта вопроса
     *
     * @param questionId - id вопроса
     * @param value      - значение
     * @param text       - текст
     */
    Integer findQuestionVariantId(int questionId, String value, String text);

    /**
     * список вариантов ответа на вопрос
     *
     * @param questionId - id вопроса
     */
    List<QuestionVariant> getQuestionVariants(int questionId);

    /**
     * ищем текстовое значение варианта по id
     */
    String findQuestionVariantText(int id);

    /**
     * сохраняем ответ на вопрос
     *
     * @param creditRequestId - id заявки
     * @param key             - код вопроса
     * @param answer          - ответ
     */
    void saveQA(int creditRequestId, String key, Object answer);

    /**
     * сохраняем ответ на вопрос
     *
     * @param creditRequestId - id заявки
     * @param key             - код вопроса
     * @param answer          - ответ
     * @param comment         - комментарий к ответу
     */
    void saveQA(int creditRequestId, String key, Object answer, String comment);

    /**
     * добавляем фейковый ответ на вопрос
     *
     * @param creditRequestId - id заявки
     * @param key             - код вопроса
     */
    QuestionAnswerEntity addQAFake(int creditRequestId, String key);

    /**
     * возвращает вариант ответа по id
     *
     * @param id - id варианта ответа
     * @return
     */
    QuestionVariantEntity getQuestionVariant(int id);

    /**
     * сохранить ответ на вопрос
     *
     * @param questionAnswer трансферный объект
     */
    QuestionAnswer saveQuestionAnswer(QuestionAnswer questionAnswer);

    /**
     * сохранить ответ на вопрос
     *
     * @param entity сущность
     */
    QuestionAnswerEntity saveQuestionAnswerEntity(QuestionAnswerEntity entity);

    /**
     * сохраняем антимошеннические случаи по ответу
     *
     * @param questionAnswer что-то вроде ответа
     */
    void saveAntifraudOccasions(QuestionAnswer questionAnswer);

    /**
     * ищет вопрос-ответы по параметрам
     *
     * @param nFirst          первая запись
     * @param nCount          количество записей
     * @param creditRequestId заявка
     * @param userId          пользователь выставивший
     * @param peopleMainId    человек
     * @param statuses        статусы ответа
     * @param types           типы вопросов
     * @param options         инициализация
     */
    List<QuestionAnswer> listQuestionAnswer(int nFirst, int nCount, Integer creditRequestId, Integer userId,
                                            Integer peopleMainId, int[] statuses, int[] types, Set options);
}
