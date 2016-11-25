package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.BlacklistEntity;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.util.DatesUtils;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

public class BlackList extends BaseTransfer<BlacklistEntity> implements Serializable, Initializable {
    public static final int REASON_TERRORISM = 1;
    public static final int REASON_CREDIT_OVERDUE = 2;
    public static final int REASON_FRAUD = 3;
    public static final int REASON_BANKRUPTCY = 4;
    public static final int REASON_NEGATIVE = 5;
    public static final int REASON_OTHER = 6;

    private static final long serialVersionUID = 1L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = BlackList.class.getConstructor(BlacklistEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    protected Reference reason;


    public BlackList() {
        super();
        entity = new BlacklistEntity();
    }

    public BlackList(BlacklistEntity entity) {
        super(entity);
        reason = new Reference(entity.getReasonId());
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
        entity.getDatabeg();
    }

    public Integer getId() {
        return entity.getId();
    }

    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }

    public Date getDataBeg() {
        return entity.getDatabeg();
    }

    @XmlElement
    public void setDataBeg(Date databeg) {
        entity.setDatabeg(databeg);
    }

    public Date getDataEnd() {
        return entity.getDataend();
    }

    @XmlElement
    public void setDataEnd(Date dataend) {
        entity.setDataend(dataend);
    }

    public String getComment() {
        return entity.getComment();
    }

    @XmlElement
    public void setComment(String comment) {
        entity.setComment(comment);
    }

    public Reference getReason() {
        return reason;
    }

    @XmlElement
    public void setReason(Reference reason) {
        this.reason = reason;
    }

    public boolean isActive() {
        return DatesUtils.daysDiffAny(getDataEnd(), new Date()) >= 0;
    }
}
