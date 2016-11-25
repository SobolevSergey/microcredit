package ru.simplgroupp.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import ru.simplgroupp.toolkit.common.Identifiable;

import java.io.Serializable;
import java.util.Date;

/**
 * Ответы на вопросы
 */
public class QuestionAnswerEntity implements Serializable, Identifiable {
    private static final long serialVersionUID = 8669919486684718407L;
    protected Integer txVersion = 0;

    /**
     * вопрос
     */
    private QuestionEntity questionId;

    /**
     * заявка
     */
    private CreditRequestEntity creditRequestId;

    /**
     * Статус вопроса
     */
    private Integer answerStatus;

    /**
     * ответ число
     */
    private Long answerValueNumber;

    /**
     * ответ строка
     */
    private String answerValueString;

    /**
     * ответ дата
     */
    private Date answerValueDate;

    /**
     * ответ сумма
     */
    private Double answerValueMoney;

    /**
     * ответ значение из справочника
     */
    private Integer answerValueRef;

    /**
     * комментарий
     */
    private String comment;

    /**
     * пользователь изменивший ответ
     */
    private UsersEntity user;

    /**
     * дата создания
     */
    private Date createdAt;

    /**
     * дата обновления
     */
    private Date updatedAt;

    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;


    public QuestionEntity getQuestionId() {
        return questionId;
    }

    public void setQuestionId(QuestionEntity questionId) {
        this.questionId = questionId;
    }

    public CreditRequestEntity getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequestEntity creditRequestId) {
        this.creditRequestId = creditRequestId;
    }

    public Integer getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(Integer answerStatus) {
        this.answerStatus = answerStatus;
    }

    public Long getAnswerValueNumber() {
        return answerValueNumber;
    }

    public void setAnswerValueNumber(Long answerValueNumber) {
        this.answerValueNumber = answerValueNumber;
    }

    public Integer getAnswerValueRef() {
        return answerValueRef;
    }

    public void setAnswerValueRef(Integer answerValueRef) {
        this.answerValueRef = answerValueRef;
    }

    public Double getAnswerValueMoney() {
        return answerValueMoney;
    }

    public void setAnswerValueMoney(Double answerValueMoney) {
        this.answerValueMoney = answerValueMoney;
    }

    public String getAnswerValueString() {
        return answerValueString;
    }

    public void setAnswerValueString(String answerValueString) {
        this.answerValueString = answerValueString;
    }

    public Date getAnswerValueDate() {
        return answerValueDate;
    }

    public void setAnswerValueDate(Date answerValueDate) {
        this.answerValueDate = answerValueDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    /**
     * возвращаем значение ответа на вопрос
     *
     * @return
     */
    public Object getAnswerValue() {
        Object res = this.answerValueString;
        if (res == null) {
            res = this.answerValueDate;
        }
        if (res == null) {
            res = this.answerValueMoney;
        }
        if (res == null) {
            res = this.answerValueNumber;
        }
        if (res == null) {
            res = this.answerValueRef;
        }
        return res;
    }

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append((questionId == null) ? 0 : questionId.getId())
                .append((creditRequestId == null) ? 0 : creditRequestId.getId())
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QuestionAnswerEntity) {
            final QuestionAnswerEntity other = (QuestionAnswerEntity) obj;
            return new EqualsBuilder()
                    .append((questionId == null) ? 0 : questionId.getId(), (other.questionId == null) ? 0 : other.questionId.getId())
                    .append((creditRequestId == null) ? 0 : creditRequestId.getId(), (other.creditRequestId == null) ? 0 : other.creditRequestId.getId())
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public Object getId() {
        return new QuestionAnswerPK(questionId.getId(), creditRequestId.getId());
    }
}
