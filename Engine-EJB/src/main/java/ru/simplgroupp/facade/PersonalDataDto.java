package ru.simplgroupp.facade;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import ru.simplgroupp.validators.Age;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * Personal data DTO
 */
public class PersonalDataDto implements Serializable {

    private static final long serialVersionUID = -3241746620925212285L;

    private Integer personalId;

    @NotBlank
    @Pattern(regexp = "[А-Яа-яЁё-]*", message = "{ru.simplgroupp.validators.RuOnly.message}")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "[А-Яа-яЁё-]*", message = "{ru.simplgroupp.validators.RuOnly.message}")
    private String firstName;

    @Pattern(regexp = "[А-Яа-яЁё-]*", message = "{ru.simplgroupp.validators.RuOnly.message}")
    private String middleName;

    private Integer emailId;

    @NotBlank
    @Email
    private String email;

    private Integer phoneId;

    @NotBlank
    private String phone;

    @NotNull
    @Age(min = 18, max = 100)
    private Date birthDate;

    @NotBlank
    @Pattern(regexp = "[.\\-\\$ А-Яа-яЁё0-9]*", message = "{ru.simplgroupp.validators.RuOnly.message}")
    private String birthPlace;

    @NotNull
    private Integer genderCode;

    private Integer documentId;

    @NotBlank
    @Pattern(regexp = "[0-9]{4}", message = "{ru.simplgroupp.validators.PassportSerial.message}")
    private String passportSerial;

    @NotBlank
    @Pattern(regexp = "[0-9]{6}", message = "{ru.simplgroupp.validators.PassportNumber.message}")
    private String passportNumber;

    @NotNull
    @Past
    private Date passportIssueDate;

    @NotBlank
    private String passportDepartmentCode;

    @NotBlank
    @Pattern(regexp = "[А-Яа-яЁё0-9-]*", message = "{ru.simplgroupp.validators.RuOnly.message}")
    private String passportIssueOrganization;

    public Integer getPersonalId() {
        return personalId;
    }

    public void setPersonalId(Integer personalId) {
        this.personalId = personalId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public Integer getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(Integer genderCode) {
        this.genderCode = genderCode;
    }

    public String getPassportSerial() {
        return passportSerial;
    }

    public void setPassportSerial(String passportSerial) {
        this.passportSerial = passportSerial;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Date getPassportIssueDate() {
        return passportIssueDate;
    }

    public void setPassportIssueDate(Date passportIssueDate) {
        this.passportIssueDate = passportIssueDate;
    }

    public String getPassportDepartmentCode() {
        return passportDepartmentCode;
    }

    public void setPassportDepartmentCode(String passportDepartmentCode) {
        this.passportDepartmentCode = passportDepartmentCode;
    }

    public String getPassportIssueOrganization() {
        return passportIssueOrganization;
    }

    public void setPassportIssueOrganization(String passportIssueOrganization) {
        this.passportIssueOrganization = passportIssueOrganization;
    }

    public Integer getEmailId() {
        return emailId;
    }

    public void setEmailId(Integer emailId) {
        this.emailId = emailId;
    }

    public Integer getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Integer phoneId) {
        this.phoneId = phoneId;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public void clear() {
        lastName = null;
        firstName = null;
        middleName = null;
        email = null;
        phone = null;
        birthDate = null;
        birthPlace = null;
        genderCode = null;
        passportSerial = null;
        passportNumber = null;
        passportIssueDate = null;
        passportDepartmentCode = null;
        passportIssueOrganization = null;
    }
}
