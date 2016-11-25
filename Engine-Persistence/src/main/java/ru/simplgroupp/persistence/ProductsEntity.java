package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * кредитные продукты
 */
public class ProductsEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 2440837604240970149L;

    protected Integer txVersion = 0;

     /**
     * название продукта
     */
    private String name;

    /**
     * код продукта
     */
    private String code;

    /**
     * описание продукта
     */
    private String description;

    /**
     * как платим по продукту - id справочника 33
     */
    private ReferenceEntity paymentTypeId;

    /**
     * активность
     */
    private Integer isActive;

    /**
     * используем по умолчанию
     */
    private Integer isDefault;

    /**
     * конфигурации продукта
     */
    private List<ProductConfigEntity> productConfig = new ArrayList<>(0);

    /**
     * конфигурации продукта
     */
    private List<ProductRulesEntity> productRules = new ArrayList<>(0);

    /**
     * сообщения продукта
     */
    private List<ProductMessagesEntity> productMessages = new ArrayList<>(0);

    /**
     * отчеты
     */
    private List<ReportEntity> reports = new ArrayList<>(0);

    /**
     * стратегии
     */
    private List<AIModelEntity> models = new ArrayList<>(0);

    /**
     * действия
     */
    private List<BizActionEntity> actions = new ArrayList<>(0);

    /**
     * вопросы
     */
    private List<QuestionEntity> questions = new ArrayList<>(0);

    public ProductsEntity() {

    }

   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ReferenceEntity getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(ReferenceEntity paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public List<ProductConfigEntity> getProductConfig() {
        return productConfig;
    }

    public void setProductConfig(List<ProductConfigEntity> productConfig) {
        this.productConfig = productConfig;
    }

    public List<ProductRulesEntity> getProductRules() {
        return productRules;
    }

    public void setProductRules(List<ProductRulesEntity> productRules) {
        this.productRules = productRules;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public List<ProductMessagesEntity> getProductMessages() {
        return productMessages;
    }

    public void setProductMessages(List<ProductMessagesEntity> productMessages) {
        this.productMessages = productMessages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ReportEntity> getReports() {
        return reports;
    }

    public void setReports(List<ReportEntity> reports) {
        this.reports = reports;
    }

    public List<AIModelEntity> getModels() {
        return models;
    }

    public void setModels(List<AIModelEntity> models) {
        this.models = models;
    }


    public List<BizActionEntity> getActions() {
        return actions;
    }

    public void setActions(List<BizActionEntity> actions) {
        this.actions = actions;
    }

    public List<QuestionEntity> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionEntity> questions) {
        this.questions = questions;
    }

    public int getHasReports() {
        if (getReports().size() > 0) {
            return getReports().size();
        }
        return 0;
    }

    public int getHasModels() {
        if (getModels().size() > 0) {
            return getModels().size();
        }
        return 0;
    }

    public int getHasActions() {
        if (getActions().size() > 0) {
            return getActions().size();
        }
        return 0;
    }

    public int getHasQuestions() {
        if (getQuestions().size() > 0) {
            return getQuestions().size();
        }
        return 0;
    }

    public int getHasProductConfig() {
        if (getProductConfig().size() > 0) {
            return getProductConfig().size();
        }
        return 0;
    }

    public int getHasProductRules() {
        if (getProductRules().size() > 0) {
            return getProductRules().size();
        }
        return 0;
    }

    public int getHasProductMessages() {
        if (getProductMessages().size() > 0) {
            return getProductMessages().size();
        }
        return 0;
    }

}
