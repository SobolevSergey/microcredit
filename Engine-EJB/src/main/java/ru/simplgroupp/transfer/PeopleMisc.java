package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.PeopleMiscEntity;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.util.DatesUtils;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

public class PeopleMisc extends BaseTransfer<PeopleMiscEntity> implements Serializable, Initializable {

   
    /**
	 * 
	 */
	private static final long serialVersionUID = 5823726032450310581L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = PeopleMisc.class.getConstructor(PeopleMiscEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    public static final String REALTY_UNKNOWN="9";
    
    protected Reference marriage;
    protected Reference realty;
    protected PeopleMain peopleMain;
    protected CreditRequest creditRequest;
    protected Partner partner;
    private Reference regDateId;
    private Reference realtyDateId;


    public PeopleMisc() {
        super();
        entity = new PeopleMiscEntity();
    }

    public PeopleMisc(PeopleMiscEntity value) {
        super(value);
        if (entity.getMarriageId() != null) {
            marriage = new Reference(entity.getMarriageId());
        }
        if (entity.getRealtyId() != null) {
            realty = new Reference(entity.getRealtyId());
        }
        peopleMain = new PeopleMain(entity.getPeopleMainId());
        partner = new Partner(entity.getPartnersId());

        if (entity.getCreditRequestId() != null) {
            creditRequest = new CreditRequest(entity.getCreditRequestId());
        }

        if (entity.getRegDateId() != null) {
            regDateId = new Reference(entity.getRegDateId());
        }
        
        if (entity.getRealtyDateId() != null) {
        	setRealtyDateId(new Reference(entity.getRealtyDateId()));
        }
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    public Integer getId() {
        return entity.getId();
    }

    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }

    public Reference getMarriage() {
        return marriage;
    }

    @XmlElement
    public void setMarriage(Reference marriage) {
        this.marriage = marriage;
    }

    public Boolean getCar() {
        return entity.getCar();
    }

    @XmlElement
    public void setCar(Boolean car) {
        entity.setCar(car);
    }

    public Boolean getDriverLicense() {
        return entity.getDriverLicense();
    }

    @XmlElement
    public void setDriverLicense(Boolean driverLicense) {
        entity.setDriverLicense(driverLicense);
    }

    public Reference getRealty() {
        return realty;
    }

    @XmlElement
    public void setRealty(Reference realty) {
        this.realty = realty;
    }

    public PeopleMain getPeopleMain() {
        return peopleMain;
    }

    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain = peopleMain;
    }

    public CreditRequest getCreditRequest() {
        return creditRequest;
    }

    public void setCreditRequest(CreditRequest creditRequest) {
        this.creditRequest = creditRequest;
    }

    public Partner getPartner() {
        return partner;
    }

    @XmlElement
    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Integer getChildren() {
        return entity.getChildren();
    }

    @XmlElement
    public void setChildren(Integer children) {
        entity.setChildren(children);
    }

    public Integer getUnderage() {
        return entity.getUnderage();
    }

    @XmlElement
    public void setUnderage(Integer underage) {
        entity.setUnderage(underage);
    }

    public Short getDependants() {
        return entity.getDependants();
    }

    @XmlElement
    public void setDependants(Short dependants) {
        entity.setDependants(dependants);
    }

    public Integer getIsActive() {
        return entity.getIsactive();
    }

    @XmlElement
    public void setIsActive(Integer isactive) {
        entity.setIsactive(isactive);
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

    public Date getRealtyDate() {
        return entity.getRealtyDate();
    }

    @XmlElement
    public void setRealtyDate(Date realtyDate) {
        entity.setRealtyDate(realtyDate);
    }

    public Date getRegDate() {
        return entity.getRegDate();
    }

    @XmlElement
    public void setRegDate(Date regDate) {
        entity.setRegDate(regDate);
    }

    public Reference getRegDateId() {
        return regDateId;
    }

    public void setRegDateId(Reference regDateId) {
        this.regDateId = regDateId;
    }

	public Reference getRealtyDateId() {
		return realtyDateId;
	}

	public void setRealtyDateId(Reference realtyDateId) {
		this.realtyDateId = realtyDateId;
	}
	
    @Override
    public void init(Set options) {
        // TODO Auto-generated method stub

    }

    @Override
    public Boolean equalsContent(PeopleMiscEntity entity) {
        return Utils.equalsNull(this.getCar(), entity.getCar())
                && Utils.equalsNull(this.getChildren(), entity.getChildren())
                && Utils.equalsNull(this.getDriverLicense(), entity.getDriverLicense())
                && Utils.equalsNull(this.getDependants(), entity.getDependants())
                && Utils.equalsNull(
                    this.getMarriage() != null ? this.getMarriage().getId() : null,
                    entity.getMarriageId() != null ? entity.getMarriageId().getId() : null)
                && DatesUtils.isSameDay(this.getRealtyDate(), entity.getRealtyDate())
                && DatesUtils.isSameDay(this.getRegDate(), entity.getRegDate())
                && Utils.equalsNull(
                    this.getRealty() != null ? this.getRealty().getId() : null,
                    entity.getRealtyId() != null ? entity.getRealtyId().getId() : null)
                && Utils.equalsNull(
                    this.getRegDateId() != null ? this.getRegDateId().getId() : null,
                    entity.getRegDateId() != null ? entity.getRegDateId().getId() : null)
                 && Utils.equalsNull(
                    this.getRealtyDateId() != null ? this.getRealtyDateId().getId() : null,
                    entity.getRealtyDateId() != null ? entity.getRealtyDateId().getId() : null);
    }

}

