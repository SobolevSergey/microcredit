package ru.simplgroupp.facade;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * Employment data DTO
 */
public class EmploymentDataDto implements Serializable {

    private static final long serialVersionUID = -3666164983308755882L;

    @NotNull
    private Integer occupationCode;

    private Integer professionCode;

    private Integer salaryFrequenceCode;

    @NotNull
    private Integer educationCode;

    @NotNull
    private Double salary;

    private Double additionalIncome;

    private Integer additionalIncomeSourceCode;

    private Date enterEmploymentDate;

    @Pattern(regexp = "[.\\- А-Яа-яЁё0-9-]*", message = "{ru.simplgroupp.validators.RuOnly.message}")
    private String employmentPlace;

    @Pattern(regexp = "[.\\- А-Яа-яЁё0-9-]*", message = "{ru.simplgroupp.validators.RuOnly.message}")
    private String position;

    private Date enterPositionDate;

    private String workPhone;

    private boolean workPhoneAvailable = false;

    public Integer getOccupationCode() {
        return occupationCode;
    }

    public void setOccupationCode(Integer occupationCode) {
        this.occupationCode = occupationCode;
    }

    public Integer getProfessionCode() {
        return professionCode;
    }

    public void setProfessionCode(Integer professionCode) {
        this.professionCode = professionCode;
    }

    public Integer getSalaryFrequenceCode() {
        return salaryFrequenceCode;
    }

    public void setSalaryFrequenceCode(Integer salaryFrequenceCode) {
        this.salaryFrequenceCode = salaryFrequenceCode;
    }

    public Integer getEducationCode() {
        return educationCode;
    }

    public void setEducationCode(Integer educationCode) {
        this.educationCode = educationCode;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Double getAdditionalIncome() {
        return additionalIncome;
    }

    public void setAdditionalIncome(Double additionalIncome) {
        this.additionalIncome = additionalIncome;
    }

    public Integer getAdditionalIncomeSourceCode() {
        return additionalIncomeSourceCode;
    }

    public void setAdditionalIncomeSourceCode(Integer additionalIncomeSourceCode) {
        this.additionalIncomeSourceCode = additionalIncomeSourceCode;
    }

    public Date getEnterEmploymentDate() {
        return enterEmploymentDate;
    }

    public void setEnterEmploymentDate(Date enterEmploymentDate) {
        this.enterEmploymentDate = enterEmploymentDate;
    }

    public String getEmploymentPlace() {
        return employmentPlace;
    }

    public void setEmploymentPlace(String employmentPlace) {
        this.employmentPlace = employmentPlace;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getEnterPositionDate() {
        return enterPositionDate;
    }

    public void setEnterPositionDate(Date enterPositionDate) {
        this.enterPositionDate = enterPositionDate;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public boolean isWorkPhoneAvailable() {
        return workPhoneAvailable;
    }

    public void setWorkPhoneAvailable(boolean workPhoneAvailable) {
        this.workPhoneAvailable = workPhoneAvailable;
    }

    public boolean isWorking() {
        if (occupationCode == null) {
            return false;
        }

        return 0 == occupationCode
                || 1 == occupationCode
                || 3 == occupationCode
                || 5 == occupationCode
                || 9 == occupationCode;
    }

    public void clear() {
        occupationCode = null;
        professionCode = null;
        salaryFrequenceCode = null;
        educationCode = null;
        salary = null;
        additionalIncome = null;
        additionalIncomeSourceCode = null;
        enterEmploymentDate = null;
        employmentPlace = null;
        position = null;
        enterPositionDate = null;
        this.workPhone = null;
        this.workPhoneAvailable = false;
    }
}
