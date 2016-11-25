package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Вопросы верификатора
 */
public class QuestionEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -3964273619682906903L;
    protected Integer txVersion = 0;

    /**
     * Код вопроса
     */
    private String questionCode;

    /**
     * Текст вопроса
     */
    private String questionText;

    /**
     * вид ответа
     */
    private Integer answerType;

    /**
     * продукт
     */
    private ProductsEntity productId;

    /**
     * варианты ответа
     */
    private List<QuestionVariantEntity> variants = new ArrayList<>();

    /**
     * тип вопроса
     */
    private ReferenceEntity type;


    public QuestionEntity() {
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Integer getAnswerType() {
        return answerType;
    }

    public void setAnswerType(Integer answerType) {
        this.answerType = answerType;
    }

    public List<QuestionVariantEntity> getVariants() {
        return variants;
    }

    public void setVariants(List<QuestionVariantEntity> variants) {
        this.variants = variants;
    }

    public ProductsEntity getProductId() {
        return productId;
    }

    public void setProductId(ProductsEntity productId) {
        this.productId = productId;
    }

    public ReferenceEntity getType() {
        return type;
    }

    public void setType(ReferenceEntity type) {
        this.type = type;
    }
}
