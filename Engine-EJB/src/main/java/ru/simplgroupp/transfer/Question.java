package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.QuestionEntity;
import ru.simplgroupp.persistence.QuestionVariantEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Question extends BaseTransfer<QuestionEntity> implements Initializable, Serializable, Identifiable {
    public static final int ANSWER_SINGLE = 1;
    public static final int ANSWER_MULTI = 2;
    public static final int ANSWER_NUMERIC = 3;
    public static final int ANSWER_MONEY = 4;
    public static final int ANSWER_STRING = 5;
    public static final int ANSWER_DATE = 6;
    public static final int CHECKING_QUESTION = 1;
    public static final int VERIFIER_QUESTION = 2;


    private static final long serialVersionUID = 7810598827886172345L;

    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = Question.class.getConstructor(QuestionEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    private Products product;

    private Reference type;

    private List<QuestionVariant> options;


    public Question() {
        super();
        entity = new QuestionEntity();
        options = new ArrayList<>();
    }

    public Question(QuestionEntity value) {
        super(value);

        if (entity.getProductId() != null) {
            product = new Products(entity.getProductId());
        }

        if (entity.getType() != null) {
            type = new Reference(entity.getType());
        }
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
        // варианты ответов
        if (options != null && options.contains(Options.INIT_OPTIONS)) {
            this.options = new ArrayList<>();

            Utils.initCollection(entity.getVariants(), options);
            for (QuestionVariantEntity optionEntity : entity.getVariants()) {
                QuestionVariant option = new QuestionVariant(optionEntity);
                option.init(options);
                this.options.add(option);
            }
        }
    }

    @Override
    public Integer getId() {
        return entity.getId();
    }

    public String getQuestionCode() {
        return entity.getQuestionCode();
    }

    public void setQuestionCode(String questionCode) {
        entity.setQuestionCode(questionCode);
    }

    public String getQuestionText() {
        return entity.getQuestionText();
    }

    public void setQuestionText(String questionText) {
        entity.setQuestionText(questionText);
    }

    public Integer getAnswerType() {
        return entity.getAnswerType();
    }

    public void setAnswerType(Integer answerType) {
        entity.setAnswerType(answerType);
    }

    public boolean getHasVariants() {
        if (options != null && options.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public String getAnswerTypeName() {
        switch (entity.getAnswerType()) {
            case ANSWER_SINGLE:
                return "один вариант";
            case ANSWER_MULTI:
                return "несколько вариантов";
            case ANSWER_NUMERIC:
                return "число";
            case ANSWER_MONEY:
                return "деньги";
            case ANSWER_STRING:
                return "строка";
            case ANSWER_DATE:
                return "дата";
        }
        return null;
    }

    public List<QuestionVariant> getOptions() {
        return options;
    }

    public void setOptions(List<QuestionVariant> options) {
        this.options = options;
    }

    public Reference getType() {
        return type;
    }

    public void setType(Reference type) {
        this.type = type;
    }

    public enum Options {
        INIT_OPTIONS
    }
}
