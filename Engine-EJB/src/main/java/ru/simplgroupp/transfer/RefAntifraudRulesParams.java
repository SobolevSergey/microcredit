package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.antifraud.RefAntifraudRulesParamsEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

public class RefAntifraudRulesParams extends BaseTransfer<RefAntifraudRulesParamsEntity> implements Serializable, Initializable, Identifiable {
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = AntifraudFieldType.class.getConstructor(RefAntifraudRulesParamsEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    public RefAntifraudRulesParams() {
        super();
        entity = new RefAntifraudRulesParamsEntity();
    }

    public RefAntifraudRulesParams(RefAntifraudRulesParamsEntity entity) {
        super(entity);
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
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

    public String getCondition() {
        return entity.getCondition();
    }

    public void setCondition(String condition) {
        entity.setCondition(condition);
    }

    public String getDescription() {
        return entity.getDescription();
    }

    public void setDescription(String description) {
        entity.setDescription(description);
    }

    public String getDataType() {
        return entity.getDataType();
    }

    public void setDataType(String dataType) {
        entity.setDataType(dataType);
    }

    public String getValueString() {
        return entity.getValueString();
    }

    public void setValueString(String valueString) {
        entity.setValueString(valueString);
    }

    public Integer getValueInteger() {
        return entity.getValueInteger();
    }

    public void setValueInteger(Integer valueInteger) {
        entity.setValueInteger(valueInteger);
    }

    public Double getValueFloat() {
        return entity.getValueFloat();
    }

    public void setValueFloat(Double valueFloat) {
        entity.setValueFloat(valueFloat);
    }

    public Date getValueDate() {
        return entity.getValueDate();
    }

    public void setValueDate(Date valueDate) {
        entity.setValueDate(valueDate);
    }

//    public Integer getForRequest() {
//        return entity.getForRequest();
//    }
//
//    public void setForRequest(Integer forRequest) {
//        entity.setForRequest(forRequest);
//    }

    public String getGroupCondition() {
        return entity.getGroupCondition();
    }

    public void setGroupCondition(String groupCondition) {
        entity.setGroupCondition(groupCondition);
    }

    public Integer getGroupIndex() {
        return entity.getGroupIndex();
    }

    public void setGroupIndex(Integer groupIndex) {
        entity.setGroupIndex(groupIndex);
    }

    public boolean isForRequest() {
        return Utils.booleanFromNumber(entity.getForRequest());
    }

    public void setForRequest(boolean forRequest) {
        entity.setForRequest(forRequest ? 1 : 0);
    }
}
