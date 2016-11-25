package ru.simplgroupp.transfer.creditrequestwizard;

import ru.simplgroupp.marshaller.DateAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * Данные шага 1
 */
public class Step1Data {

    private String lastName;

    private String firstName;

    private String middleName;

    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date birthday;

    private String phone;

    private String dopphone1;

    private String dopphone2;

    private String phoneCode;

    private String phoneHash;

    private String email;

    private String emailCode;

    private String emailHash;

    private boolean hasVk;

    private boolean hasMoyMir;

    private boolean hasFacebook;

    private boolean hasOdnoklassniki;

    private boolean agreement;

    private boolean treaty;

    private boolean personalData;

    private boolean isConfirmed;
    

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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getPhoneHash() {
        return phoneHash;
    }

    public void setPhoneHash(String phoneHash) {
        this.phoneHash = phoneHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    public String getEmailHash() {
        return emailHash;
    }

    public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
    }

    public boolean isAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
    }

    public boolean isHasVk() {
        return hasVk;
    }

    public void setHasVk(boolean hasVk) {
        this.hasVk = hasVk;
    }

    public boolean isHasMoyMir() {
        return hasMoyMir;
    }

    public void setHasMoyMir(boolean hasMoyMir) {
        this.hasMoyMir = hasMoyMir;
    }

    public boolean isHasFacebook() {
        return hasFacebook;
    }

    public void setHasFacebook(boolean hasFacebook) {
        this.hasFacebook = hasFacebook;
    }

    public boolean isHasOdnoklassniki() {
        return hasOdnoklassniki;
    }

    public void setHasOdnoklassniki(boolean hasOdnoklassniki) {
        this.hasOdnoklassniki = hasOdnoklassniki;
    }

    public boolean isPersonalData() {
        return personalData;
    }

    public void setPersonalData(boolean personalData) {
        this.personalData = personalData;
    }

    public boolean isTreaty() {
        return treaty;
    }

    public void setTreaty(boolean treaty) {
        this.treaty = treaty;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public String getDopphone1() {
        return dopphone1;
    }

    public void setDopphone1(String dopphone1) {
        this.dopphone1 = dopphone1;
    }

    public String getDopphone2() {
        return dopphone2;
    }

    public void setDopphone2(String dopphone2) {
        this.dopphone2 = dopphone2;
    }
}