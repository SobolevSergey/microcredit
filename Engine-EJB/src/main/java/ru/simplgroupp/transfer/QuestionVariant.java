package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.QuestionVariantEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudOccasionEntity;
import ru.simplgroupp.persistence.verificatorquestion.QuestionVariantStatusEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.antifraud.AntifraudOccasion;
import ru.simplgroupp.transfer.verificatorquestion.QuestionVariantStatus;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QuestionVariant extends BaseTransfer<QuestionVariantEntity> implements Initializable, Serializable, Identifiable {
    private static final long serialVersionUID = 4483649054560844399L;

    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = QuestionVariant.class.getConstructor(QuestionVariantEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    private Question question;

    private List<QuestionVariantStatus> statuses;

    private List<AntifraudOccasion> occasions;


    public QuestionVariant() {
        super();
        entity = new QuestionVariantEntity();
        statuses = new ArrayList<>();
        occasions = new ArrayList<>();
    }

    public QuestionVariant(QuestionVariantEntity value) {
        super(value);

        if (entity.getQuestionId() != null) {
            question = new Question(entity.getQuestionId());
        }
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public Integer getId() {
        return entity.getId();
    }

    @Override
    public void init(Set options) {
        if (options != null && options.contains(Options.INIT_STATUSES)) {
            // статусы ответа
            statuses = new ArrayList<>();

            Utils.initCollection(entity.getStatuses(), options);
            for (QuestionVariantStatusEntity statusEntity : entity.getStatuses()) {
                QuestionVariantStatus status = new QuestionVariantStatus(statusEntity);
                status.init(options);
                statuses.add(status);
            }
        } else if (options != null && options.contains(Options.INIT_ANTIFRAUD_OCCASIONS)) {
            // антимошеннические случаи
            occasions = new ArrayList<>();

            Utils.initCollection(entity.getOccasions(), options);
            for (AntifraudOccasionEntity occasionEntity : entity.getOccasions()) {
                AntifraudOccasion occasion = new AntifraudOccasion(occasionEntity);
                occasion.init(options);
                occasions.add(occasion);
            }
        }
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnswerText() {
        return entity.getAnswerText();
    }

    public void setAnswerText(String answerText) {
        entity.setAnswerText(answerText);
    }

    public String getAnswerValue() {
        return entity.getAnswerValue();
    }

    public void setAnswerValue(String answerValue) {
        entity.setAnswerValue(answerValue);
    }

    public Double getScore() {
        return entity.getScore();
    }

    public void setScore(Double score) {
        entity.setScore(score);
    }

    public Boolean getDefaultAnswer() {
        return entity.getDefaultAnswer();
    }

    public void setDefaultAnswer(Boolean defaultAnswer) {
        entity.setDefaultAnswer(defaultAnswer);
    }

    public List<QuestionVariantStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<QuestionVariantStatus> statuses) {
        this.statuses = statuses;
    }

    public List<AntifraudOccasion> getOccasions() {
        return occasions;
    }

    public void setOccasions(List<AntifraudOccasion> occasions) {
        this.occasions = occasions;
    }

    public enum Options {
        INIT_STATUSES,
        INIT_ANTIFRAUD_OCCASIONS
    }
}
