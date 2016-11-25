package ru.simplgroupp.persistence;

import java.io.Serializable;

/**
 * Составной ключ для ответов на вопросы
 */
public class QuestionAnswerPK implements Serializable {
    private static final long serialVersionUID = -3479982550914631170L;

    protected Integer questionId;

    protected Integer creditRequestId;


    public QuestionAnswerPK(int qid, int crid) {
        super();
        questionId = qid;
        creditRequestId = crid;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(Integer creditRequestId) {
        this.creditRequestId = creditRequestId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QuestionAnswerPK) {
            QuestionAnswerPK other = (QuestionAnswerPK) obj;
            return (questionId == other.questionId && creditRequestId == other.creditRequestId);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = questionId != null ? questionId.hashCode() : 0;
        result = 31 * result + (creditRequestId != null ? creditRequestId.hashCode() : 0);
        return result;
    }
}
