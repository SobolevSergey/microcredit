package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.RefBonusEntity;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

public class RefBonus extends BaseTransfer<RefBonusEntity> implements Serializable, Initializable {
    private static final long serialVersionUID = 1L;

    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = RefBonus.class.getConstructor(RefBonusEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }


    public RefBonus() {
        super();
        entity = new RefBonusEntity();
    }

    public RefBonus(RefBonusEntity value) {
        super(value);

    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
        entity.getName();

    }

    public Integer getId() {
        return entity.getId();
    }

    @XmlElement
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

    public Double getAmount() {
        return entity.getAmount();
    }

    @XmlElement
    public void setAmount(Double amount) {
        entity.setAmount(amount);
    }

    public Boolean getIsactive() {
        return Utils.triStateToBoolean(entity.getIsactive());
    }

    @XmlElement
    public void setIsactive(Boolean isactive) {
        entity.setIsactive(Utils.booleanToTriState(isactive));
    }

    public Integer getCodeinteger() {
        return entity.getCodeinteger();
    }

    @XmlElement
    public void setCodeinteger(Integer codeinteger) {
        entity.setCodeinteger(codeinteger);
    }

    public String getCode() {
        return entity.getCode();
    }

    @XmlElement
    public void setCode(String code) {
        entity.setCode(code);
    }

    public Double getRate() {
        return entity.getRate();
    }

    public void setRate(Double rate) {
        entity.setRate(rate);
    }
}
