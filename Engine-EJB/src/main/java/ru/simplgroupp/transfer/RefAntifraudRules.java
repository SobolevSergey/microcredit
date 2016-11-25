package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.antifraud.RefAntifraudRulesEntity;
import ru.simplgroupp.persistence.antifraud.RefAntifraudRulesParamsEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RefAntifraudRules extends BaseTransfer<RefAntifraudRulesEntity> implements Serializable, Initializable, Identifiable {
    private static final long serialVersionUID = 2407885801065763272L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = AntifraudFieldType.class.getConstructor(RefAntifraudRulesEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    private List<RefAntifraudRulesParams> paramList = new ArrayList<>();


    public RefAntifraudRules() {
        super();
        entity = new RefAntifraudRulesEntity();
    }

    public RefAntifraudRules(RefAntifraudRulesEntity entity) {
        super(entity);
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
        if (options != null && options.contains(Options.INIT_RULES_PARAMS)) {
            Utils.initCollection(entity.getParamList(), options);
            for (RefAntifraudRulesParamsEntity param : entity.getParamList()) {
                RefAntifraudRulesParams transferParam = new RefAntifraudRulesParams(param);
                transferParam.init(options);
                paramList.add(transferParam);
            }
        }
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

    public void setName(String name) {
        entity.setName(name);
    }

    public String getEntityName() {
        return entity.getEntityName();
    }

    public void setEntityName(String entityName) {
        entity.setEntityName(entityName);
    }

    public String getEntityAlias() {
        return entity.getEntityAlias();
    }

    public void setEntityAlias(String entityAlias) {
        entity.setEntityAlias(entityAlias);
    }

    public String getResultType() {
        return entity.getResultType();
    }

    public void setResultType(String resultType) {
        entity.setResultType(resultType);
    }

    public String getResultTypeAlias() {
        return entity.getResultTypeAlias();
    }

    public void setResultTypeAlias(String resultTypeAlias) {
        entity.setResultTypeAlias(resultTypeAlias);
    }

    public String getResultJoin() {
        return entity.getResultJoin();
    }

    public void setResultJoin(String resultJoin) {
        entity.setResultJoin(resultJoin);
    }

    public String getCode() {
        return entity.getCode();
    }

    public void setCode(String code) {
        entity.setCode(code);
    }

    public Integer getTimes() {
        return entity.getTimes();
    }

    public void setTimes(Integer times) {
        entity.setTimes(times);
    }

    public boolean getIsActive() {
        return Utils.booleanFromNumber(entity.getIsActive());
    }

    public void setIsActive(boolean isActive) {
        entity.setIsActive(isActive ? 1 : 0);
    }

    public boolean getCheckSamePerson() {
        return Utils.booleanFromNumber(entity.getCheckSamePerson());
    }

    public void setCheckSamePerson(boolean checkSamePerson) {
        entity.setCheckSamePerson(checkSamePerson ? 1 : 0);
    }

    public boolean getIncludeSamePerson() {
        return Utils.booleanFromNumber(entity.getIncludeSamePerson());
    }

    public void setIncludeSamePerson(boolean includeSamePerson) {
        entity.setIncludeSamePerson(includeSamePerson ? 1 : 0);
    }

    public RefAntifraudRules getParrentRule() {
        return new RefAntifraudRules(entity.getParrentRule());
    }

    public void setParrentRule(RefAntifraudRules parrentRule) {
        entity.setParrentRule(parrentRule.getEntity());
    }

    public List<RefAntifraudRulesParams> getParamList() {
        return paramList;
    }

    public void setParamList(List<RefAntifraudRulesParams> paramList) {
        this.paramList = paramList;
    }

    public enum Options {
        INIT_RULES_PARAMS
    }
}
