
package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

import ru.simplgroupp.toolkit.common.Utils;

/**
 * Занятость
 */
public class EmploymentEntity extends ExtendedBaseEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7057640540206791531L;

	protected Integer txVersion = 0;

   /**
     * запись: 0-предыдущая, 1-текущая
     */
    private Integer current;

    /**
     * частота получения зарплаты
     */
    private ReferenceEntity durationId;

    /**
     * вид деятельности
     */
    private ReferenceEntity typeworkId;

    /**
     * профессия
     */
    private ReferenceEntity professionId;

    /**
     * должность
     */
    private ReferenceEntity occupationId;

    /**
     * образование
     */
    private ReferenceEntity educationId;

    /**
     * вид доп.дохода
     */
    private ReferenceEntity extsalaryId;

    /**
     * зарплата
     */
    private Double salary;

    /**
     * дополнительный доход
     */
    private Double extsalary;

    /**
     * дополнительные кредитные обязательства
     */
    private Double extCreditSum;

    /**
     * дата начала рабты в должности
     */
    private Date datestartwork;

    /**
     * место работы
     */
    private String placeWork;

    /**
     * должность текстом
     */
    private String occupation;

    /**
     * дата начала
     */
    private Date databeg;

    /**
     * дата окончания
     */
    private Date dataend;

    /**
     * дата трудового стажа
     */
    private Date experience;

    /**
     * дата следующей зарплаты
     */
    private Date nextSalaryDate;

    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;

    /**
     * партнер
     */
    private PartnersEntity partnersId;

    /**
     * заявка
     */
    private CreditRequestEntity creditRequestId;

    /**
     * не используется
     */
    private ReferenceEntity companystateId;

    private ReferenceEntity companysizeId;

    private ReferenceEntity companyareaId;

    private ReferenceEntity salaryfreqId;

    private String professionText;

    private String companyareaText;

    /**
     * дата трудоустройства из справочника
     */
    private ReferenceEntity dateStartWorkId;


    public EmploymentEntity() {
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public ReferenceEntity getDurationId() {
        return durationId;
    }

    public void setDurationId(ReferenceEntity durationId) {
        this.durationId = durationId;
    }

    public ReferenceEntity getTypeworkId() {
        return typeworkId;
    }

    public void setTypeworkId(ReferenceEntity typeworkId) {
        this.typeworkId = typeworkId;
    }

    public ReferenceEntity getProfessionId() {
        return professionId;
    }

    public void setProfessionId(ReferenceEntity professionId) {
        this.professionId = professionId;
    }

    public String getProfessionText() {
        return professionText;
    }

    public void setProfessionText(String professionText) {
        this.professionText = professionText;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPlaceWork() {
        return placeWork;
    }

    public void setPlaceWork(String placeWork) {
        this.placeWork = placeWork;
    }

    public ReferenceEntity getCompanystateId() {
        return companystateId;
    }

    public void setCompanystateId(ReferenceEntity companystateId) {
        this.companystateId = companystateId;
    }

    public ReferenceEntity getCompanysizeId() {
        return companysizeId;
    }

    public void setCompanysizeId(ReferenceEntity companysizeId) {
        this.companysizeId = companysizeId;
    }

    public ReferenceEntity getCompanyareaId() {
        return companyareaId;
    }

    public void setCompanyareaId(ReferenceEntity companyareaId) {
        this.companyareaId = companyareaId;
    }

    public String getCompanyareaText() {
        return companyareaText;
    }

    public void setCompanyareaText(String companyareaText) {
        this.companyareaText = companyareaText;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Double getExtsalary() {
        return extsalary;
    }

    public void setExtsalary(Double extsalary) {
        this.extsalary = extsalary;
    }

    public ReferenceEntity getSalaryfreqId() {
        return salaryfreqId;
    }

    public void setSalaryfreqId(ReferenceEntity salaryfreqId) {
        this.salaryfreqId = salaryfreqId;
    }

    public String getDescription() {
        return "зарплата " + ((salary == null) ? "" : salary.toString()) + " руб., вид деятельности " + ((typeworkId == null) ? "" : typeworkId.getName()) + ", место работы " + ((placeWork == null) ? "" : placeWork) + ", должность " + ((occupation == null) ? "" : occupation) + ", частота получения зарплаты " + ((salaryfreqId == null) ? "" : salaryfreqId.getName()) + ", образование " + ((educationId == null) ? "" : educationId.getName());
    }

    @Override
    public String toString() {
        return "доход " + salary.toString();
    }


    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public PartnersEntity getPartnersId() {
        return partnersId;
    }

    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }

    public CreditRequestEntity getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequestEntity creditRequestId) {
        this.creditRequestId = creditRequestId;
    }

    public ReferenceEntity getEducationId() {
        return educationId;
    }

    public void setEducationId(ReferenceEntity educationId) {
        this.educationId = educationId;
    }

    public ReferenceEntity getExtsalaryId() {
        return extsalaryId;
    }

    public void setExtsalaryId(ReferenceEntity extsalaryId) {
        this.extsalaryId = extsalaryId;
    }

    public Date getDatabeg() {
        return databeg;
    }

    public void setDatabeg(Date databeg) {
        this.databeg = databeg;
    }

    public Date getDataend() {
        return dataend;
    }

    public void setDataend(Date dataend) {
        this.dataend = dataend;
    }

    public Date getDatestartwork() {
        return datestartwork;
    }

    public void setDatestartwork(Date datestartwork) {
        this.datestartwork = datestartwork;
    }

    public Date getExperience() {
        return experience;
    }

    public void setExperience(Date experience) {
        this.experience = experience;
    }


    public Double getExtCreditSum() {
        return extCreditSum;
    }

    public void setExtCreditSum(Double extCreditSum) {
        this.extCreditSum = extCreditSum;
    }

    public Date getNextSalaryDate() {
        return nextSalaryDate;
    }

    public void setNextSalaryDate(Date nextSalaryDate) {
        this.nextSalaryDate = nextSalaryDate;
    }

    public ReferenceEntity getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(ReferenceEntity occupationId) {
        this.occupationId = occupationId;
    }

    @Override
    public Boolean equalsContent(BaseEntity other) {
        EmploymentEntity ent = (EmploymentEntity) other;
        return Utils.equalsNull(experience != null ? experience.getTime() : null, ent.getExperience() != null ? ent.getExperience().getTime() : null)
                && Utils.equalsNull(datestartwork != null ? datestartwork.getTime() : null, ent.getDatestartwork() != null ? ent.getDatestartwork().getTime() : null)
                && Utils.equalsNull(nextSalaryDate != null ? nextSalaryDate.getTime() : null, ent.getNextSalaryDate() != null ? ent.getNextSalaryDate().getTime() : null)
                && Utils.equalsNull(durationId, ent.getDurationId())
                && Utils.equalsNull(occupationId, ent.getOccupationId())
                && Utils.equalsNull(educationId, ent.getEducationId())
                && Utils.equalsNull(extsalary, ent.getExtsalary())
                && Utils.equalsNull(extCreditSum, ent.getExtCreditSum())
                && Utils.equalsNull(extsalaryId, ent.getExtsalaryId())
                && Utils.equalsNull(occupation, ent.getOccupation())
                && Utils.equalsNull(placeWork, ent.getPlaceWork())
                && Utils.equalsNull(professionId, ent.getProfessionId())
                && Utils.equalsNull(salary, ent.getSalary())
                && Utils.equalsNull(typeworkId, ent.getTypeworkId());

    }

    public ReferenceEntity getDateStartWorkId() {
        return dateStartWorkId;
    }

    public void setDateStartWorkId(ReferenceEntity dateStartWorkId) {
        this.dateStartWorkId = dateStartWorkId;
    }
}
