package ru.simplgroupp.persistence.antifraud;

import ru.simplgroupp.persistence.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * правила внутреннего АМ
 */
public class RefAntifraudRulesEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3713694715667349200L;

    protected Integer txVersion = 0;

    private Integer id;
    /**
     * название
     */
    private String name;
    /**
     * название класса
     */
    private String entityName;
    /**
     * alias сущности
     */
    private String entityAlias;
    /**
     * возвращаемая сущность
     */
    private String resultType;
    /**
     * алиас для возвращаемой сущности
     */
    private String resultTypeAlias;
    /**
     * join для возвращаемой сущности
     */
    private String resultJoin;
    /**
     * код
     */
    private String code;
    /**
     * какое кол-во раз ишем
     */
    private Integer times;
    /**
     * активное правило
     */
    private Integer isActive = 1;
    /**
     * проверяем только по этому человеку
     */
    private Integer checkSamePerson;
    /**
     * проверка включает в себя и этого человека
     */
    private Integer includeSamePerson;

    /**
     * дочерние правила
     */
    private List<RefAntifraudRulesEntity> childRulesList;

    /**
     * parrent
     */
    private RefAntifraudRulesEntity parrentRule;

    /**
     * параметры правила
     */
    private List<RefAntifraudRulesParamsEntity> paramList;

    public RefAntifraudRulesEntity() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getCheckSamePerson() {
        return checkSamePerson;
    }

    public void setCheckSamePerson(Integer checkSamePerson) {
        this.checkSamePerson = checkSamePerson;
    }

    public Integer getIncludeSamePerson() {
        return includeSamePerson;
    }

    public void setIncludeSamePerson(Integer includeSamePerson) {
        this.includeSamePerson = includeSamePerson;
    }

    public String getEntityAlias() {
        return entityAlias;
    }

    public void setEntityAlias(String entityAlias) {
        this.entityAlias = entityAlias;
    }

    public String getResultJoin() {
        return resultJoin;
    }

    public void setResultJoin(String resultJoin) {
        this.resultJoin = resultJoin;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultTypeAlias() {
        return resultTypeAlias;
    }

    public void setResultTypeAlias(String resultTypeAlias) {
        this.resultTypeAlias = resultTypeAlias;
    }

    public List<RefAntifraudRulesEntity> getChildRulesList() {
        return childRulesList;
    }

    public void setChildRulesList(List<RefAntifraudRulesEntity> childRulesList) {
        this.childRulesList = childRulesList;
    }

    public RefAntifraudRulesEntity getParrentRule() {
        return parrentRule;
    }

    public void setParrentRule(RefAntifraudRulesEntity parrentRule) {
        this.parrentRule = parrentRule;
    }

    public List<RefAntifraudRulesParamsEntity> getParamList() {
        return paramList;
    }

    public void setParamList(List<RefAntifraudRulesParamsEntity> paramList) {
        this.paramList = paramList;
    }
}
