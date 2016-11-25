package ru.simplgroupp.facade;

import ru.simplgroupp.validators.Age;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * Additional data DTO
 */
public class AdditionalDataDto implements Serializable {

    private static final long serialVersionUID = -1504175114782013294L;

    @NotNull
    private Integer maritalStatus;

    @Pattern(regexp = "[А-Яа-яЁё-]*", message = "{ru.simplgroupp.validators.RuOnly.message}")
    private String lastName;

    @Pattern(regexp = "[А-Яа-яЁё-]*", message = "{ru.simplgroupp.validators.RuOnly.message}")
    private String firstName;

    @Pattern(regexp = "[А-Яа-яЁё-]*", message = "{ru.simplgroupp.validators.RuOnly.message}")
    private String middleName;

    @Age(min = 18, max = 100)
    private Date birthDate;

    private String phone;

    @Past
    private Date marriageDate;

    private Integer occupationCode;

    private int childrenCount = 0;

    private boolean hasCar = false;

    public Integer getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getMarriageDate() {
        return marriageDate;
    }

    public void setMarriageDate(Date marriageDate) {
        this.marriageDate = marriageDate;
    }

    public Integer getOccupationCode() {
        return occupationCode;
    }

    public void setOccupationCode(Integer occupationCode) {
        this.occupationCode = occupationCode;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

    public boolean isHasCar() {
        return hasCar;
    }

    public void setHasCar(boolean hasCar) {
        this.hasCar = hasCar;
    }

    public void clear() {
        maritalStatus = null;
        lastName = null;
        firstName = null;
        middleName = null;
        birthDate = null;
        phone = null;
        marriageDate = null;
        occupationCode = null;
        childrenCount = 0;
        hasCar = false;
    }
}
