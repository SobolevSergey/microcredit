package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.QuestionAnswerEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

public class QuestionAnswer extends BaseTransfer<QuestionAnswerEntity> implements Initializable, Serializable, Identifiable {
    public static final int ANSWER_STATUS_NOT_ASKED = 0;
    public static final int ANSWER_STATUS_DENIAL = 1;
    public static final int ANSWER_STATUS_ANSWERED = 2;

    private static final long serialVersionUID = 6316024175272707534L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = QuestionAnswer.class.getConstructor(QuestionAnswerEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    private Boolean answerDenied;

    private CreditRequest creditRequest;

    private Question question;

    private Users user;

    private PeopleMain peopleMainId;


    public QuestionAnswer() {
        super();
        entity = new QuestionAnswerEntity();
    }

    public QuestionAnswer(QuestionAnswerEntity value) {
        super(value);

        if (entity.getCreditRequestId() != null) {
            creditRequest = new CreditRequest(entity.getCreditRequestId());
        }

        if (entity.getQuestionId() != null) {
            question = new Question(entity.getQuestionId());
        }

        if (entity.getUser() != null) {
            user = new Users(entity.getUser());
        }

        answerDenied = (entity.getAnswerStatus() == ANSWER_STATUS_DENIAL);
        if (entity.getPeopleMainId() != null) {
            peopleMainId = new PeopleMain(entity.getPeopleMainId());
        }
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
        if (options == null) {
            return;
        }

        if (options.contains(Question.Options.INIT_OPTIONS)) {
            if (question != null) {
                question.init(options);
            }
        }
    }

    @Override
    public Object getId() {
        return entity.getId();
    }

    public Integer getAnswerStatus() {
        return entity.getAnswerStatus();
    }

    public void setAnswerStatus(Integer answerStatus) {
        entity.setAnswerStatus(answerStatus);
    }

    public Long getAnswerValueNumber() {
        return entity.getAnswerValueNumber();
    }

    public void setAnswerValueNumber(Long answerValueNumber) {
        entity.setAnswerValueNumber(answerValueNumber);
    }

    public Double getAnswerValueMoney() {
        return entity.getAnswerValueMoney();
    }

    public void setAnswerValueMoney(Double answerValueMoney) {
        entity.setAnswerValueMoney(answerValueMoney);
    }

    public String getAnswerValueString() {
        return entity.getAnswerValueString();
    }

    public void setAnswerValueString(String answerValueString) {
        entity.setAnswerValueString(answerValueString);
    }

    public String getComment() {
        return entity.getComment();
    }

    public void setComment(String comment) {
        entity.setComment(comment);
    }

    public Date getAnswerValueDate() {
        return entity.getAnswerValueDate();
    }

    public void setAnswerValueDate(Date answerValueDate) {
        entity.setAnswerValueDate(answerValueDate);
    }

    public Integer getAnswerValueRef() {
        return entity.getAnswerValueRef();
    }

    public void setAnswerValueRef(Integer answerValueRef) {
        entity.setAnswerValueRef(answerValueRef);
    }

    public CreditRequest getCreditRequest() {
        return creditRequest;
    }

    public void setCreditRequest(CreditRequest creditRequest) {
        this.creditRequest = creditRequest;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public PeopleMain getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMain peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public boolean isAnswerDenied() {
        return answerDenied;
    }

    public void setAnswerDenied(boolean answerDenied) {
        this.answerDenied = answerDenied;
    }

    public Object getAnswerValue() {
        if (entity.getAnswerStatus() != ANSWER_STATUS_ANSWERED) {
            return null;
        }
        Object ans = getAnswerValueDate();
        if (ans != null) {
            return ans;
        }
        ans = getAnswerValueMoney();
        if (ans != null) {
            return ans;
        }
        ans = getAnswerValueNumber();
        if (ans != null) {
            return ans;
        }
        ans = getAnswerValueRef();
        if (ans != null) {
            return ans;
        }
        ans = getAnswerValueString();
        return ans;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return entity.getCreatedAt();
    }

    public void setCreatedAt(Date createdAt) {
        entity.setCreatedAt(createdAt);
    }

    public Date getUpdatedAt() {
        return entity.getUpdatedAt();
    }

    public void setUpdatedAt(Date updatedAt) {
        entity.setUpdatedAt(updatedAt);
    }

    public boolean isAnsweredByValue() {
        boolean answered = false;

        if (question.getAnswerType() == Question.ANSWER_NUMERIC) {
            if (getAnswerValueNumber() != null) {
                answered = true;
            }
        } else if (question.getAnswerType() == Question.ANSWER_STRING) {
            if (!getAnswerValueString().isEmpty()) {
                answered = true;
            }
        } else if (question.getAnswerType() == Question.ANSWER_MONEY) {
            if (getAnswerValueMoney() != 0) {
                answered = true;
            }
        } else if (question.getAnswerType() == Question.ANSWER_DATE) {
            if (getAnswerValueDate() != null) {
                answered = true;
            }
        } else if (question.getAnswerType() == Question.ANSWER_SINGLE) {
            if (getAnswerValueRef() != null) {
                answered = true;
            }
        }


        return answered;
    }

    public enum Options {
    }
}
