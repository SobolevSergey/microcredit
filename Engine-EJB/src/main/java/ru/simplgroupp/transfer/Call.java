package ru.simplgroupp.transfer;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import ru.simplgroupp.persistence.CallsEntity;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.util.DatesUtils;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

public class Call extends BaseTransfer<CallsEntity> implements Serializable, Initializable {
    private static final long serialVersionUID = 5111126638826781180L;

    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = Call.class.getConstructor(CallsEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    protected PeopleMain peopleMain;

    protected Users user;

    protected Reference status;

    private Integer hours;

    private Integer minutes;

    private Integer seconds;


    public Call() {
        super();
        entity = new CallsEntity();
    }

    public Call(CallsEntity entity) {
        super(entity);
        peopleMain = new PeopleMain(entity.getPeopleMainId());
        user = new Users(entity.getUserId());

        calculateLength(entity.getCalldataend().getTime() - entity.getCalldatabeg().getTime());

        if (entity.getStatusId() != null) {
            status = new Reference(entity.getStatusId());
        }
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
        if (options != null && options.contains(Options.INIT_USER)) {
            user = new Users(entity.getUserId());
            user.init(options);
        }
    }

    public void calculateLength(long length) {
        Double time = (double) length / 1000;

        Double hours = time / 60 / 60;
        if (hours.intValue() > 0) {
            this.hours = hours.intValue();
            time -= hours.intValue() * 60 * 60;
        }

        Double minutes = time / 60;
        if (minutes.intValue() > 0) {
            this.minutes = minutes.intValue();

            time -= minutes.intValue() * 60;
        }

        if (time.intValue() > 0) {
            this.seconds = time.intValue();
        }

        if (this.hours == null && this.minutes == null && this.seconds == null) {
            this.seconds = 0;
        }
    }

    public Integer getId() {
        return entity.getId();
    }

    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }

    public Date getCallDataBeg() {
        return entity.getCalldatabeg();
    }

    @XmlElement
    public void setCallDataBeg(Date calldatabeg) {
        entity.setCalldatabeg(calldatabeg);
    }

    public Date getCallDataEnd() {
        return entity.getCalldataend();
    }

    @XmlElement
    public void setCallDataEnd(Date calldataend) {
        entity.setCalldataend(calldataend);
    }

    public byte[] getCallData() {
        return entity.getCalldata();
    }

    @XmlElement
    public void setCallData(byte[] calldata) {
        entity.setCalldata(calldata);
    }

    public String getComment() {
        return entity.getComment();
    }

    @XmlElement
    public void setComment(String comment) {
        entity.setComment(comment);
    }

    public String getPhone() {
        return entity.getPhone();
    }

    @XmlElement
    public void setPhone(String phone) {
        entity.setPhone(phone);
    }

    public Integer getIsClientCall() {
        return entity.getIsClientCall();
    }

    @XmlElement
    public void setIsClientCall(Integer isClientCall) {
        entity.setIsClientCall(isClientCall);
    }

    public PeopleMain getPeopleMain() {
        return peopleMain;
    }

    @XmlElement
    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain = peopleMain;
    }

    public Reference getStatus() {
        return status;
    }

    @XmlElement
    public void setStatus(Reference status) {
        this.status = status;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Boolean isIncoming() {
        return entity.isIncoming();
    }

    public void setIncoming(Boolean incoming) {
        entity.setIncoming(incoming);
    }

    public Integer getCallLengthHours() {
        return hours;
    }

    public Integer getCallLengthMinutes() {
        return minutes;
    }

    public Integer getCallLengthSeconds() {
        return seconds;
    }

    public enum Options {
        INIT_USER;
    }
}
