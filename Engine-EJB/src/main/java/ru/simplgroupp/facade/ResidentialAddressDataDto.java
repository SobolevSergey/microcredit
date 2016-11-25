package ru.simplgroupp.facade;

import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.transfer.FiasAddress;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;

/**
 * Residential address data DTO
 */
public class ResidentialAddressDataDto implements Serializable {

    private static final long serialVersionUID = 494419142514805049L;

    private FiasAddress address;

    private String phone;

    private boolean phoneAvailable;

    @Past
    private Date residentialDate;

    @NotNull
    private Integer realtyCode;

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

    public void setPhoneAvailable(boolean phoneAvailable) {
        this.phoneAvailable = phoneAvailable;
    }

    public boolean isPhoneAvailable() {
        return phoneAvailable;
    }

    public void clear() {
        address = new ru.simplgroupp.transfer.FiasAddress(new AddressEntity());
        phone = null;
        phoneAvailable = false;
        realtyCode = null;
        residentialDate = null;
    }

    public void setResidentialDate(Date residentialDate) {
        this.residentialDate = residentialDate;
    }

    public Date getResidentialDate() {
        return residentialDate;
    }

    public void setRealtyCode(Integer realtyCode) {
        this.realtyCode = realtyCode;
    }

    public Integer getRealtyCode() {
        return realtyCode;
    }
}
