package ru.simplgroupp.facade;

import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.transfer.FiasAddress;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;

/**
 * Registration address data DTO
 */
public class RegistrationAddressDataDto implements Serializable {

    private static final long serialVersionUID = -3730174568324945612L;

    private FiasAddress address;

    private String phone;

    private boolean phoneAvailable;

    private boolean sameResidentalAddress;

    @NotNull
    @Past
    private Date registrationDate;

    public FiasAddress getAddress() {
        return address;
    }

    public void setAddress(FiasAddress address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPhoneAvailable() {
        return phoneAvailable;
    }

    public void setPhoneAvailable(boolean phoneAvailable) {
        this.phoneAvailable = phoneAvailable;
    }

    public boolean isSameResidentalAddress() {
        return sameResidentalAddress;
    }

    public void setSameResidentalAddress(boolean sameResidentalAddress) {
        this.sameResidentalAddress = sameResidentalAddress;
    }

    public void clear() {
        address = new ru.simplgroupp.transfer.FiasAddress(new AddressEntity());
        phone = null;
        sameResidentalAddress = true;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }
}
