package ru.simplgroupp.dao.interfaces;

import ru.simplgroupp.persistence.IdentityAnswerEntity;
import ru.simplgroupp.persistence.IdentityOptionEntity;
import ru.simplgroupp.persistence.IdentityQuestionEntity;
import ru.simplgroupp.persistence.IdentityTemplateEntity;
import ru.simplgroupp.transfer.IdentityQuestion;

import javax.ejb.Local;
import java.util.List;
import java.util.Set;

@Local
public interface IdentityQuestionDAO {
    /**
     * возвращает шаблон по id из бд
     *
     * @param id id из бд
     * @return сущность или null
     */
    IdentityTemplateEntity getTemplate(int id);

    /**
     * возвращает сущность вопроса по id
     *
     * @param id id вопроса
     * @return сущность или null
     */
    IdentityQuestionEntity getQuestionEntity(int id);

    /**
     * возвращает трансферный класс вопроса по id
     *
     * @param id id вопроса
     * @return сущность или null
     */
    IdentityQuestion getQuestion(int id, Set options);

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
     * сохранить вопрос
     *
     * @param question вопрос
     * @return сохраненный вопрос
     */
    IdentityQuestionEntity saveQuestion(IdentityQuestionEntity question);

    /**
     * сохранить ответ
     *
     * @param optionId идентификатор варианта ответа
     * @return сохраненный ответ
     */
    IdentityAnswerEntity saveAnswer(int optionId);

    /**
     * возвращает вариант ответа по идентификатору
     *
     * @param optionId id варианта
     * @return сохраненный вариант
     */
    IdentityOptionEntity getOption(int optionId);
}
