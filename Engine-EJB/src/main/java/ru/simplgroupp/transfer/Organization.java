package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.AccountEntity;
import ru.simplgroupp.persistence.OrganizationEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Organization extends BaseTransfer<OrganizationEntity> implements Serializable, Initializable, Identifiable {


    public static final int OWN_ORGANIZATION = 1;
    //типы организаций
    public static final int CREDIT_ORGANIZATION = 1;
    public static final int CREDIT_BUREAU = 2;
    public static final int COLLECTOR_ORGANIZATION = 3;
    public static final int COURT_ORGANIZATION = 4;
    //вид организации мфо - для выгрузки
    public static final int MFO_TYPE = 1;
    /**
     *
     */
    private static final long serialVersionUID = -7366560220387913156L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = Organization.class.getConstructor(OrganizationEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    protected List<Account> accounts;
    protected Partner partner;
    protected Reference orgType;


    public Organization() {
        super();
        entity = new OrganizationEntity();
    }

    public Organization(OrganizationEntity entity) {
        super(entity);
        accounts = new ArrayList<Account>(0);
        if (entity.getPartnersId() != null) {
            partner = new Partner(entity.getPartnersId());
        }
        if (entity.getOrgTypeId() != null) {
            orgType = new Reference(entity.getOrgTypeId());
        }
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
        if (options != null && options.contains(Options.INIT_ACCOUNT)) {
            Utils.initCollection(entity.getAccounts(), options);
            for (AccountEntity acc1 : entity.getAccounts()) {
                Account acc = new Account(acc1);
                acc.init(options);
                accounts.add(acc);
            }
        }

    }

    public Integer getId() {
        return entity.getId();
    }

    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }

    public String getInn() {
        return entity.getInn();
    }

    @XmlElement
    public void setInn(String inn) {
        entity.setInn(inn);
    }

    public String getOgrn() {
        return entity.getOgrn();
    }

    @XmlElement
    public void setOgrn(String ogrn) {
        entity.setOgrn(ogrn);
    }

    public String getName() {
        return entity.getName();
    }

    @XmlElement
    public void setName(String name) {
        entity.setName(name);
    }

    public Boolean getIsCreditOrg() {
        return entity.getIsCreditOrg();
    }

    @XmlElement
    public void setIsCreditOrg(Boolean isCreditOrg) {
        entity.setIsCreditOrg(isCreditOrg);
    }

    public String getPhone() {
        return entity.getPhone();
    }

    @XmlElement
    public void setPhone(String phone) {
        entity.setPhone(phone);
    }

    public String getFreePhone() {
        return entity.getFreePhone();
    }

    @XmlElement
    public void setFreePhone(String freePhone) {
        entity.setFreePhone(freePhone);
    }

    public String getEmail() {
        return entity.getEmail();
    }

    @XmlElement
    public void setEmail(String email) {
        entity.setEmail(email);
    }

    public String getKpp() {
        return entity.getKpp();
    }

    @XmlElement
    public void setKpp(String kpp) {
        entity.setKpp(kpp);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Reference getOrgType() {
        return orgType;
    }

    @XmlElement
    public void setOrgType(Reference orgType) {
        this.orgType = orgType;
    }

    public Partner getPartner() {
        return partner;
    }

    @XmlElement
    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Integer getIsActive() {
        return entity.getIsactive();
    }

    @XmlElement
    public void setIsActive(Integer isactive) {
        entity.setIsactive(getIsActive());
    }

    public Date getDatabeg() {
        return entity.getDatabeg();
    }

    @XmlElement
    public void setDatabeg(Date databeg) {
        entity.setDatabeg(databeg);
    }

    public Date getDataend() {
        return entity.getDataend();
    }

    @XmlElement
    public void setDataend(Date dataend) {
        entity.setDataend(dataend);
    }

    public int getHasAccounts() {
        return entity.getHasAccounts();
    }

    @Override
    public Boolean equalsContent(OrganizationEntity entity) {
        return Utils.equalsNull(this.getName(), entity.getName())
                && Utils.equalsNull(this.getInn(), entity.getInn())
                && Utils.equalsNull(this.getOgrn(), entity.getOgrn())
                && Utils.equalsNull(this.getKpp(), entity.getKpp())
                && Utils.equalsNull(this.getEmail(), entity.getEmail())
                && Utils.equalsNull(this.getPhone(), entity.getPhone())
                && Utils.equalsNull(this.getFreePhone(), entity.getFreePhone())
                && Utils.equalsNull(this.getNotificationPhone(), entity.getNotificationPhone())
                && Utils.equalsNull(this.getOrgType().getId(), entity.getOrgTypeId().getId());
    }

    public String getNotificationPhone() {
        return entity.getNotificationPhone();
    }

    public void setNotificationPhone(String notificationPhone) {
        entity.setNotificationPhone(notificationPhone);
    }

    public enum Options {
        INIT_ACCOUNT;
    }
}
