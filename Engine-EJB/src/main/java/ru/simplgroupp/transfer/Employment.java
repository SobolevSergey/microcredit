package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.EmploymentEntity;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.util.DatesUtils;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

public class Employment extends BaseTransfer<EmploymentEntity> implements Initializable, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7466284537489019009L;
	public static final int CURRENT = 1;
    public static final int RECENT = 0;
    
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = Employment.class.getConstructor(EmploymentEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    protected Reference duration;
    protected Reference typeWork;
    protected Reference profession;
    protected Reference occupationId;
    protected Reference companyState;
    protected Reference companySize;
    protected Reference companyArea;
    protected Reference salaryFreq;
    protected Reference education;
    protected Reference extSalaryId;
    protected PeopleMain peopleMain;
    protected CreditRequest creditRequest;
    protected Partner partner;
    private Reference dateStartWorkId;


    public Employment() {
        super();
        entity = new EmploymentEntity();
    }

    public Employment(EmploymentEntity entity) {
        super(entity);
        if (entity.getDurationId() != null) {
            duration = new Reference(entity.getDurationId());
        }
        if (entity.getTypeworkId() != null) {
            typeWork = new Reference(entity.getTypeworkId());
        }
        if (entity.getProfessionId() != null) {
            profession = new Reference(entity.getProfessionId());
        }
        if (entity.getOccupationId() != null) {
            occupationId = new Reference(entity.getOccupationId());
        }
        if (entity.getCompanystateId() != null)
            companyState = new Reference(entity.getCompanystateId());
        if (entity.getCompanysizeId() != null)
            companySize = new Reference(entity.getCompanysizeId());
        if (entity.getCompanyareaId() != null)
            companyArea = new Reference(entity.getCompanyareaId());
        if (entity.getSalaryfreqId() != null) {
            salaryFreq = new Reference(entity.getSalaryfreqId());
        }
        if (entity.getEducationId() != null) {
            education = new Reference(entity.getEducationId());
        }
        if (entity.getExtsalaryId() != null) {
            extSalaryId = new Reference(entity.getExtsalaryId());
        }
        peopleMain = new PeopleMain(entity.getPeopleMainId());
        partner = new Partner(entity.getPartnersId());
        if (entity.getCreditRequestId() != null) {
            creditRequest = new CreditRequest(entity.getCreditRequestId());
        }

        if (entity.getDateStartWorkId() != null) {
            dateStartWorkId = new Reference(entity.getDateStartWorkId());
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

    public Integer getCurrent() {
        return entity.getCurrent();
    }

    @XmlElement
    public void setCurrent(Integer current) {
        entity.setCurrent(current);
    }

    public Reference getDuration() {
        return duration;
    }

    @XmlElement
    public void setDuration(Reference duration) {
        this.duration = duration;
    }

    public Reference getTypeWork() {
        return typeWork;
    }

    @XmlElement
    public void setTypeWork(Reference typeWork) {
        this.typeWork = typeWork;
    }

    public Reference getProfession() {
        return profession;
    }

    @XmlElement
    public void setProfession(Reference profession) {
        this.profession = profession;
    }

    public String getProfessionText() {
        return entity.getProfessionText();
    }

    @XmlElement
    public void setProfessionText(String professionText) {
        entity.setProfessionText(professionText);
    }

    public String getOccupation() {
        return entity.getOccupation();
    }

    @XmlElement
    public void setOccupation(String occupation) {
        entity.setOccupation(occupation);
    }

    public Reference getOccupationId() {
        return occupationId;
    }

    @XmlElement
    public void setOccupationId(Reference occupationId) {
        this.occupationId = occupationId;
    }

    public String getPlaceWork() {
        return entity.getPlaceWork();
    }

    @XmlElement
    public void setPlaceWork(String placeWork) {
        entity.setPlaceWork(placeWork);
    }

    public Reference getCompanyState() {
        return companyState;
    }

    @XmlElement
    public void setCompanyState(Reference companyState) {
        this.companyState = companyState;
    }

    public Reference getCompanySize() {
        return companySize;
    }

    @XmlElement
    public void setCompanySize(Reference companySize) {
        this.companySize = companySize;
    }

    public Reference getCompanyArea() {
        return companyArea;
    }

    @XmlElement
    public void setCompanyArea(Reference companyArea) {
        this.companyArea = companyArea;
    }

    public String getCompanyAreaText() {
        return entity.getCompanyareaText();
    }

    @XmlElement
    public void setCompanyAreaText(String companyareaText) {
        entity.setCompanyareaText(companyareaText);
    }

    public Double getSalary() {
        return entity.getSalary();
    }

    @XmlElement
    public void setSalary(Double salary) {
        entity.setSalary(salary);
    }

    public Double getExtSalary() {
        return entity.getExtsalary();
    }

    @XmlElement
    public void setExtSalary(Double extsalary) {
        entity.setExtsalary(extsalary);
    }

    public Double getExtCreditSum() {
        return entity.getExtCreditSum();
    }

    @XmlElement
    public void setExtCreditSum(Double extCreditSum) {
        entity.setExtCreditSum(extCreditSum);
    }

    public Reference getSalaryFreq() {
        return salaryFreq;
    }

    @XmlElement
    public void setSalaryFreq(Reference salaryFreq) {
        this.salaryFreq = salaryFreq;
    }

    @Override
    public void init(Set options) {
        entity.getSalary();

    }

    public Reference getEducation() {
        return education;
    }

    @XmlElement
    public void setEducation(Reference education) {
        this.education = education;
    }

    public Reference getExtSalaryId() {
        return extSalaryId;
    }

    @XmlElement
    public void setExtSalaryId(Reference extSalaryId) {
        this.extSalaryId = extSalaryId;
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

    public Date getDateStartWork() {
        return entity.getDatestartwork();
    }

    @XmlElement
    public void setDateStartWork(Date datestartwork) {
        entity.setDatestartwork(datestartwork);
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

    public String getDescription() {
        return entity.getDescription();
    }

    public Date getExperience() {
        return entity.getExperience();
    }

    @XmlElement
    public void setExperience(Date experience) {
        entity.setExperience(experience);
    }

    public Date getNextSalaryDate() {
        return entity.getNextSalaryDate();
    }

    @XmlElement
    public void setNextSalaryDate(Date nextSalaryDate) {
        entity.setNextSalaryDate(nextSalaryDate);
        ;
    }

    public Reference getDateStartWorkId() {
        return dateStartWorkId;
    }

    public void setDateStartWorkId(Reference dateStartWorkId) {
        this.dateStartWorkId = dateStartWorkId;
    }

    @Override
    public Boolean equalsContent(EmploymentEntity entity) {
    	     	
    	return Utils.equalsNull(this.getEducation() != null ? this.getEducation().getId() : null, entity.getEducationId() != null ? entity.getEducationId().getId() : null)
                && DatesUtils.isSameDay(this.getNextSalaryDate(), entity.getNextSalaryDate())
                && DatesUtils.isSameDay(this.getDateStartWork(), entity.getDatestartwork())
                && Utils.equalsNull(this.getDuration() != null ? this.getDuration().getId() : null, entity.getDurationId() != null ? entity.getDurationId().getId() : null)
                && Utils.equalsNull(this.getDateStartWorkId() != null ? this.getDateStartWorkId().getId() : null, entity.getDateStartWorkId() != null ? entity.getDateStartWorkId().getId() : null)
                && Utils.equalsNull((this.getExtSalary()!=null&&this.getExtSalary()>0)?this.getExtSalary():null, entity.getExtsalary())
                && Utils.equalsNull((this.getExtCreditSum()!=null&&this.getExtCreditSum()>0)?this.getExtCreditSum():null, entity.getExtCreditSum())
                && Utils.equalsNull(this.getExtSalaryId() != null ? this.getExtSalaryId().getId() : null, entity.getExtsalaryId() != null ? entity.getExtsalaryId().getId() : null)
                && Utils.equalsNull(this.getOccupation(), entity.getOccupation())
                && Utils.equalsNull(this.getPlaceWork(), entity.getPlaceWork())
                && Utils.equalsNull(this.getOccupationId() != null ? this.getOccupationId().getId() : null, entity.getOccupationId() != null ? entity.getOccupationId().getId() : null)
                && Utils.equalsNull(this.getProfession() != null ? this.getProfession().getId() : null, entity.getProfessionId() != null ? entity.getProfessionId().getId() : null)
                && Utils.equalsNull(this.getSalary(), entity.getSalary())
                && Utils.equalsNull(this.getTypeWork() != null ? this.getTypeWork().getId() : null, entity.getTypeworkId() != null ? entity.getTypeworkId().getId() : null);
    }
}
