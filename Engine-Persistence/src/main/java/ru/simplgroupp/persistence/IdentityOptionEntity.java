package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * Вариант ответа на вопрос идентификации
 */
public class IdentityOptionEntity implements Serializable {
    private static final long serialVersionUID = 3675550111431756338L;

    protected Integer txVersion = 0;

    private Integer id;

    /**
     * текст варианта
     */
    private String value;

    /**
     * верный этот ответ на вопрос или нет
     */
    private Boolean correct = false;

    /**
     * вопрос
     */
    private IdentityQuestionEntity question;

    /**
     * ответ, если он есть, иначе null
     */
    private IdentityAnswerEntity answer;


    public IdentityOptionEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public IdentityQuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(IdentityQuestionEntity question) {
        this.question = question;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public IdentityAnswerEntity getAnswer() {
        return answer;
    }

    public void setAnswer(IdentityAnswerEntity answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdentityOptionEntity that = (IdentityOptionEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(value != null ? !value.equals(that.value) : that.value != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
