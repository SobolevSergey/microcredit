package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.AIModelEntity;
import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.persistence.ProductConfigEntity;
import ru.simplgroupp.persistence.ProductMessagesEntity;
import ru.simplgroupp.persistence.ProductRulesEntity;
import ru.simplgroupp.persistence.ProductsEntity;
import ru.simplgroupp.persistence.QuestionEntity;
import ru.simplgroupp.persistence.ReportEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

public class Products extends BaseTransfer<ProductsEntity> implements Serializable, Initializable {

    /**
     *
     */
    private static final long serialVersionUID = 1476232147919473581L;

    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    static {
        try {
            wrapConstructor = Products.class.getConstructor(ProductsEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    public enum Options {

        INIT_CONFIG,
        INIT_RULES,
        INIT_MESSAGES,
        INIT_REPORTS,
        INIT_MODELS,
        INIT_ACTIONS,
        INIT_QUESTIONS;
    }

    protected Reference paymentType;

    protected List<ProductConfig> productConfig;

    protected List<ProductRules> productRules;

    protected List<ProductMessages> productMessages;

    protected List<Report> reports;

    protected List<AIModel> models;

    protected List<BizAction> actions;

    protected List<Question> questions;

    public static final int PRODUCT_PDL = 1;

    public static final String ZAEM_CODE = "R001";

    public Products() {
        super();
        entity = new ProductsEntity();
    }

    public Products(ProductsEntity entity) {
        super(entity);
        if (entity.getPaymentTypeId() != null) {
            paymentType = new Reference(entity.getPaymentTypeId());
        }
        productConfig = new ArrayList<ProductConfig>(0);
        productRules = new ArrayList<ProductRules>(0);
        productMessages = new ArrayList<ProductMessages>(0);
        reports = new ArrayList<Report>(0);
        models = new ArrayList<AIModel>(0);
        actions = new ArrayList<BizAction>(0);
        questions = new ArrayList<Question>(0);
    }

    public Integer getId() {
        return entity.getId();
    }

    public void setId(Integer id) {
        entity.setId(id);
    }

    public String getName() {
        return entity.getName();
    }

    @XmlElement
    public void setName(String name) {
        entity.setName(name);
    }

    public String getDescription() {
        return entity.getDescription();
    }

    public void setDescription(String description) {
        entity.setDescription(description);
    }

    @XmlElement
    public String getCode() {
        return entity.getCode();
    }

    public void setCode(String code) {
        entity.setCode(code);
    }

    public Integer getIsActive() {
        return entity.getIsActive();
    }

    public void setIsActive(Integer isactive) {
        entity.setIsActive(isactive);
    }

    public Integer getIsDefault() {
        return entity.getIsDefault();
    }

    public void setIsDefault(Integer isdefault) {
        entity.setIsDefault(isdefault);
    }

    public String getActiveString() {
        return Convertor.fromIntToYesNo(entity.getIsActive());
    }

    public String getDefaultString() {
        return Convertor.fromIntToYesNo(entity.getIsDefault());
    }

    public Reference getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Reference paymentType) {
        this.paymentType = paymentType;
    }


    public List<ProductRules> getProductRules() {
        return productRules;
    }

    public void setProductRules(List<ProductRules> productRules) {
        this.productRules = productRules;
    }


    public List<ProductMessages> getProductMessages() {
        return productMessages;
    }

    public void setProductMessages(List<ProductMessages> productMessages) {
        this.productMessages = productMessages;
    }


    public List<BizAction> getActions() {
        return actions;
    }

    public void setActions(List<BizAction> actions) {
        this.actions = actions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getHasProductConfig() {
        return entity.getHasProductConfig();
    }

    public int getHasProductRules() {
        return entity.getHasProductRules();
    }

    public int getHasProductMessages() {
        return entity.getHasProductMessages();
    }

    public int getHasQuestions() {
        return entity.getHasQuestions();
    }

    public int getHasModels() {
        return entity.getHasModels();
    }

    public int getHasReports() {
        return entity.getHasReports();
    }

    public int getHasActions() {
        return entity.getHasActions();
    }

    @Override
    public void init(Set options) {
        //конфигурация
        if (options != null && options.contains(Options.INIT_CONFIG)) {
            Utils.initCollection(entity.getProductConfig(), options);
            for (ProductConfigEntity reqEnt : entity.getProductConfig()) {
                ProductConfig req = new ProductConfig(reqEnt);
                req.init(options);
                productConfig.add(req);
            }
        }

        //скрипты
        if (options != null && options.contains(Options.INIT_RULES)) {
            Utils.initCollection(entity.getProductRules(), options);
            for (ProductRulesEntity reqEnt : entity.getProductRules()) {
                ProductRules req = new ProductRules(reqEnt);
                req.init(options);
                productRules.add(req);
            }
        }

        //сообщения
        if (options != null && options.contains(Options.INIT_MESSAGES)) {
            Utils.initCollection(entity.getProductMessages(), options);
            for (ProductMessagesEntity reqEnt : entity.getProductMessages()) {
                ProductMessages req = new ProductMessages(reqEnt);
                req.init(options);
                productMessages.add(req);
            }
        }

        //отчеты
        if (options != null && options.contains(Options.INIT_REPORTS)) {
            Utils.initCollection(entity.getReports(), options);
            for (ReportEntity reqEnt : entity.getReports()) {
                Report req = new Report(reqEnt);
                req.init(options);
                reports.add(req);
            }
        }

        //стратегии
        if (options != null && options.contains(Options.INIT_MODELS)) {
            Utils.initCollection(entity.getModels(), options);
            for (AIModelEntity reqEnt : entity.getModels()) {
                AIModel req = new AIModel(reqEnt);
                req.init(options);
                models.add(req);
            }
        }

        //бизнес-действия
        if (options != null && options.contains(Options.INIT_ACTIONS)) {
            Utils.initCollection(entity.getActions(), options);
            for (BizActionEntity reqEnt : entity.getActions()) {
                BizAction req = new BizAction(reqEnt);
                req.init(options);
                actions.add(req);
            }
        }

        //вопросы
        if (options != null && options.contains(Options.INIT_QUESTIONS)) {
            Utils.initCollection(entity.getQuestions(), options);
            for (QuestionEntity reqEnt : entity.getQuestions()) {
                Question req = new Question(reqEnt);
                req.init(options);
                questions.add(req);
            }
        }
    }

    public List<ProductConfig> getProductConfig() {
        return productConfig;
    }

    public void setProductConfig(List<ProductConfig> productConfig) {
        this.productConfig = productConfig;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public List<AIModel> getModels() {
        return models;
    }

    public void setModels(List<AIModel> models) {
        this.models = models;
    }


}
