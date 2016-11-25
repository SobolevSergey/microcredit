package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * Вопрос идентификации на основе данных от кредитных бюро
 */
public class IdentityTemplateEntity implements Serializable {
    private static final long serialVersionUID = -9184709077818800230L;

    protected Integer txVersion = 0;

    private Integer id;

    /**
     * текст вопроса (шаблон)
     */
    private String value;

    /**
     * тип ответа, может быть: дата (D), сумма денег (M), тип (ReferenceEntity) (T)
     */
    private String answerType;

    /**
     * поле в CreditEntity по которому будем строить варианты ответов
     */
    private String answerField;

    /**
     * оценка вопроса, сколько очков пользователь получит при правильном ответе
     */
    private Integer points;

    /**
     * если варианты должны быть типы, то это ссылка по которой можно найти другие варианты
     */
    private RefHeaderEntity optionType;

    /**
     * вопрос доступен если кредит имеет подобный тип кредита
     */
    private ReferenceEntity forCreditType;

    /**
     * использовать ли данный вопрос для фейка
     */
    private Boolean fake;

    /**
     * активный ли шаблон вопроса
     */
    private Boolean active;


    public IdentityTemplateEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getAnswerField() {
        return answerField;
    }

    public void setAnswerField(String answerField) {
        this.answerField = answerField;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RefHeaderEntity getOptionType() {
        return optionType;
    }

    public void setOptionType(RefHeaderEntity optionType) {
        this.optionType = optionType;
    }

    public ReferenceEntity getForCreditType() {
        return forCreditType;
    }

    public void setForCreditType(ReferenceEntity forCreditType) {
        this.forCreditType = forCreditType;
    }

    public Boolean getFake() {
        return fake;
    }

    public void setFake(Boolean fake) {
        this.fake = fake;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdentityTemplateEntity that = (IdentityTemplateEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(value != null ? !value.equals(that.value) : that.value != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
