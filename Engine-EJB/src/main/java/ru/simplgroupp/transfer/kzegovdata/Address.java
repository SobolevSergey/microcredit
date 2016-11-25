package ru.simplgroupp.transfer.kzegovdata;

import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.transfer.BaseTransfer;
import ru.simplgroupp.transfer.FiasAddress;

import javax.xml.bind.annotation.XmlElement;
import java.lang.reflect.Constructor;

/**
 * Адрес Адресного регистра, содержащий параметры конечной части адреса
 * Портал Открытые данные Электронного правительства Республики Казахстан
 * http://data.egov.kz
 */
public class Address extends FiasAddress {

    private static final long serialVersionUID = 8930083635659702983L;

	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    }
    static {
    	try {
			wrapConstructor = Address.class.getConstructor(AddressEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }

	public Address() {
        super();
        entity = new AddressEntity();
    }

    public Address(AddressEntity entity) {
        super(entity);
    }

    public Long getKzegovdataAts() {
        return entity.getKzegovdataAts();
    }

    @XmlElement
    public void setKzegovdataAts(Long kzegovdataAts) {
        entity.setKzegovdataAts(kzegovdataAts);
    }

    public Long getKzegovdataGeonims() {
        return entity.getKzegovdataGeonims();
    }

    @XmlElement
    public void setKzegovdataGeonims(Long kzegovdataGeonims) {
        entity.setKzegovdataGeonims(kzegovdataGeonims);
    }

    public String getKzegovdataCato() {
        return entity.getKzegovdataCato();
    }

    @XmlElement
    public void setKzegovdataCato(String kzegovdataCato) {
        entity.setKzegovdataCato(kzegovdataCato);
    }

    public String getAddressTextToStreet() {
        return entity.getAddressTextToStreet();
    }

    @XmlElement
    public void setAddressTextToStreet(String addressTextToStreet) {
        entity.setAddressTextToStreet(addressTextToStreet);
    }
}
