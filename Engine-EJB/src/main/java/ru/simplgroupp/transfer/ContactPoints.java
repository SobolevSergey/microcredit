package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import ru.simplgroupp.persistence.ContactPointsEntity;
import ru.simplgroupp.toolkit.common.Initializable;

public class ContactPoints extends BaseTransfer<ContactPointsEntity> implements Serializable, Initializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = ContactPoints.class.getConstructor(ContactPointsEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	

	//платеж физ.лиц в адрес юр. лиц
	public static final int FROM_CONTACT=14;
	//платеж юр.лиц в адрес физ.лиц
	public static final int TO_CONTACT=41;
	//платеж физ.лиц в адрес физ.лиц
	public static final int FL_TO_CONTACT=2;
	
	public ContactPoints() {
		super();
	
	}

	public ContactPoints(ContactPointsEntity entity) {
		super(entity);
	
	}

	 public Integer getId() {
	    return entity.getId();
	 }

	 public void setId(Integer id) {
	    entity.setId(id);
	 }
	    
	 public Integer getVersion() {
	    return entity.getVersion();
	  }

	 public void setVersion(Integer version) {
	    entity.setVersion(version);
	 } 
	
	 public Integer getParentId() {
	    return entity.getParentId();
	 }

	 public void setParentId(Integer parentId) {
	    entity.setParentId(parentId);
	 } 
	 
	 public Integer getErased() {
	    return entity.getErased();
	 }

	 public void setErased(Integer erased) {
	        entity.setErased(erased);
	 } 
	
	public String getPpCode(){
		return entity.getPpCode();
	}
	
	public void setPpCode(String ppCode){
		entity.setPpCode(ppCode);
	}
	
	public String getBic(){
		return entity.getBic();
	}
	
	public void setBic(String bic){
		entity.setBic(bic);
	}
	
	public String getName(){
		return entity.getName();
	}
	
	public void setName(String name){
		entity.setName(name);
	}
	
	public String getCityHead(){
		return entity.getCityHead();
	}
	
	public void setCityHead(String cityHead){
		entity.setCityHead(cityHead);
	}
	
	public String getAddress1(){
		return entity.getAddress1();
	}
	
	public void setAddress1(String address1){
		entity.setAddress1(address1);
	}
	
	public String getAddress2(){
		return entity.getAddress2();
	}
	
	public void setAddress2(String address2){
		entity.setAddress2(address2);
	}
	
	public String getAddress3(){
		return entity.getAddress3();
	}
	
	public void setAddress3(String address3){
		entity.setAddress3(address3);
	}
	
	public String getAddress4(){
		return entity.getAddress4();
	}
	
	public void setAddress4(String address4){
		entity.setAddress4(address4);
	}
	
	public String getPhone(){
		return entity.getPhone();
	}
	
	public void setPhone(String phone){
		entity.setPhone(phone);
	}
	
	public String getNameRus(){
		return entity.getNameRus();
	}
	
	public void setNameRus(String nameRus){
		entity.setNameRus(nameRus);
	}
	
	public String getCityLat(){
		return entity.getCityLat();
	}
	
	public void setCityLat(String cityLat){
		entity.setCityLat(cityLat);
	}
	
	public String getAddrLat(){
		return entity.getAddrLat();
	}
	
	public void setAddrLat(String addrLat){
		entity.setAddrLat(addrLat);
	}
	
	 public Integer getCountry() {
		return entity.getCountry();
	 }

	 public void setCountry(Integer country) {
		 entity.setCountry(country);
	 } 
	
	 public Integer getDeleted() {
		 return entity.getDeleted();
	 }

	 public void setDeleted(Integer deleted) {
		  entity.setDeleted(deleted);
	 } 
	
	 public Integer getContact() {
		 return entity.getContact();
	 }

	 public void setContact(Integer contact) {
		 entity.setContact(contact);
	 }  
	 
	 public Integer getRegion() {
		 return entity.getRegion();
	 }

	 public void setRegion(Integer region) {
		  entity.setRegion(region);
	 } 
	 
	 public Integer getCanRevoke() {
		 return entity.getCanRevoke();
	 }

	 public void setCanRevoke(Integer canRevoke) {
		  entity.setCanRevoke(canRevoke);
	 }
	 
	 public Integer getCanChange() {
		 return entity.getCanChange();
	 }

	 public void setCanChange(Integer canChange) {
		  entity.setCanChange(canChange);
	 } 
	 
	 public Integer getGetMoney() {
		 return entity.getGetMoney();
	 }

	 public void setGetMoney(Integer getMoney) {
		  entity.setGetMoney(getMoney);
	 } 
	 
	 public String getSendCurr(){
		return entity.getSendCurr();
	 }
		
	 public void setSendCurr(String sendCurr){
		entity.setSendCurr(sendCurr);
	 } 
	 
	 public String getRecCurr(){
		return entity.getRecCurr();
	 }
			
	 public void setRecCurr(String recCurr){
		entity.setRecCurr(recCurr);
	 } 
	 
	 public String getAttrGrps(){
		return entity.getAttrGrps();
	 }
			
	 public void setAttrGrps(String attrGrps){
		entity.setAttrGrps(attrGrps);
	 } 
	 
	 public Integer getCityId() {
		 return entity.getCityId();
	 }

	 public void setCityId(Integer cityId) {
		 entity.setCityId(cityId);
	 } 
	 
	 public Integer getLogoId() {
		 return entity.getLogoId();
	 }

	 public void setLogoId(Integer logoId) {
		 entity.setLogoId(logoId);
	 }
	 
	 public Integer getScenId() {
		 return entity.getScenId();
	 }

	 public void setScenId(Integer scenId) {
		 entity.setScenId(scenId);
	 }
	 
	 public Integer getUniqueTrn() {
		 return entity.getUniqueTrn();
	 }

	 public void setUniqueTrn(Integer uniqueTrn) {
		 entity.setUniqueTrn(uniqueTrn);
	 }
	 
	 public Integer getMetro() {
		 return entity.getMetro();
	 }

	 public void setMetro(Integer metro) {
		 entity.setMetro(metro);
	 }
	 
	public String getDescription(){
		return entity.getDescription();
	}
	
	@Override
	public void init(Set options) {
		entity.getAddress1();
		
	}

}
