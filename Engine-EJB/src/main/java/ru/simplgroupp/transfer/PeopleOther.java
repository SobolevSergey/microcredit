package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.PeopleOtherEntity;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * Created by cobalt on 18.09.15.
 */
public class PeopleOther extends BaseTransfer<PeopleOtherEntity> implements Serializable, Initializable {
    private static final long serialVersionUID = -7964413684648115047L;


    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }
    static {
        try {
            wrapConstructor = PeopleOther.class.getConstructor(PeopleOtherEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    protected PeopleMain peopleMain;
    protected CreditRequest creditRequest;
    protected Partner partner;

    public PeopleOther() {
        super();
        entity = new PeopleOtherEntity();
        partner = new Partner(entity.getPartnersId());

        if (entity.getCreditRequestId()!=null){
            creditRequest=new CreditRequest(entity.getCreditRequestId());
        }
    }

    public PeopleOther(PeopleOtherEntity value) {
        super(value);
        peopleMain=new PeopleMain(entity.getPeopleMainId());
    }

    public Integer getId() {
        return entity.getId();
    }

    public CreditRequest getCreditRequest() {
        return creditRequest;
    }

    public void setCreditRequest(CreditRequest creditRequest) {
        this.creditRequest=creditRequest;
    }

    public Partner getPartner() {
        return partner;
    }

    @XmlElement
    public void setPartner(Partner partner) {
        this.partner=partner;
    }

    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }

    public String getPublic_relative_role() {
        return entity.getPublic_relative_role();
    }

    @XmlElement
    public void setPublic_relative_role(String role) {
        this.entity.setPublic_relative_role(role);
    }

    public String getPublic_relative_fio() {
        return entity.getPublic_relative_fio();
    }

    @XmlElement
    public void setPublic_relative_fio(String fio) {
        this.entity.setPublic_relative_fio(fio);
    }

    public String getBenific_fio() {
        return entity.getBenific_fio();
    }

    @XmlElement
    public void setBenific_fio(String fio) {
        this.entity.setBenific_fio(fio);
    }

    public String getPublic_role() {
        return entity.getPublic_role();
    }

    @XmlElement
    public void setPublic_role(String role) {
        this.entity.setPublic_role(role);
    }

    public Boolean getReclam_accept() {
        return entity.getReclam_accept();
    }

    @XmlElement
    public void setReclam_accept(Boolean accept) {
        entity.setReclam_accept(accept);
    }

    public Boolean getOther() {
        return entity.getOther();
    }

    @XmlElement
    public void setOther(Boolean other) {
        entity.setOther(other);
    }

    public PeopleMain getPeopleMain() {
        return peopleMain;
    }


    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain=peopleMain;
    }

    @Override
    public void init(Set options) {
        // TODO Auto-generated method stub

    }

    @Override
    public Boolean equalsContent(PeopleOtherEntity ent){
        return Utils.equalsNull(getOther(), ent.getOther()) && Utils.equalsNull(getReclam_accept(), ent.getReclam_accept())
                && Utils.equalsNull(getPublic_role(), ent.getPublic_role()) && Utils.equalsNull(getPublic_relative_role(), ent.getPublic_relative_role())
                && Utils.equalsNull(getPublic_relative_fio(), ent.getPublic_relative_fio()) && Utils.equalsNull(getBenific_fio(), ent.getBenific_fio());
    }
}
