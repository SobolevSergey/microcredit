package ru.simplgroupp.persistence;

import ru.simplgroupp.persistence.antifraud.AntifraudOccasionEntity;
import ru.simplgroupp.persistence.verificatorquestion.QuestionVariantStatusEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Варианты ответа (справочник)
 */
public class QuestionVariantEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -8900200716005202584L;
    protected Integer txVersion = 0;

    /**
     * вопрос
     */
    private QuestionEntity questionId;

    /**
     * значение ответа
     */
    private String answerValue;

    /**
     * текст ответа
     */
    private String answerText;

    /**
     * балл
     */
    private Double score;

    /**
     * это стандартный ответ
     */
    private Boolean defaultAnswer = false;

    /**
     * статусы и их сущности которые указаны для вопроса
     */
    private List<QuestionVariantStatusEntity> statuses = new ArrayList<>();

    /**
     * сохраненные сущности когда выбрали данный вариант
     */
    private List<AntifraudOccasionEntity> occasions = new ArrayList<>();


    public QuestionVariantEntity() {
    }

    public QuestionEntity getQuestionId() {
        return questionId;
    }

    public void setQuestionId(QuestionEntity questionId) {
        this.questionId = questionId;
    }

    public String getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(String answerValue) {
        this.answerValue = answerValue;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Boolean getDefaultAnswer() {
        return defaultAnswer;
    }

    public void setDefaultAnswer(Boolean defaultAnswer) {
        this.defaultAnswer = defaultAnswer;
    }

    public List<QuestionVariantStatusEntity> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<QuestionVariantStatusEntity> statuses) {
        this.statuses = statuses;
    }

    public List<AntifraudOccasionEntity> getOccasions() {
        return occasions;
    }

    public void setOccasions(List<AntifraudOccasionEntity> occasions) {
        this.occasions = occasions;
    }
}
