package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;

/**
 * Контакты человека - телефоны, email, id в соц.сетях
 */
public class PeopleContactEntity extends ExtendedBaseEntity implements Serializable {

    private static final long serialVersionUID = -1401215663734278697L;

    protected Integer txVersion = 0;

    /**
     * контакт
     */
    private String value;

    /**
     * активный или нет
     */
    private Integer isactive;

    /**
     * дата актуальности
     */
    private Date dateactual;

    /**
     * нельзя звонить
     */
    private Boolean available;

    /**
     * вид контакта
     */
    private ReferenceEntity contactId;

    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;

    /**
     * источник информации (партнер)
     */
    private PartnersEntity partnersId;

    /**
     * Заявка
     */
    private CreditRequestEntity creditRequestId;

    /**
     * оператор связи
     */
    private String operator;

    /**
     * регион текстом
     */
    private String region;

    /**
     * код региона
     */
    private RegionsEntity regionShort;

    /**
     * список сообщений
     */
    private List<MessagesEntity> messages;

    public PeopleContactEntity() {
    }

    public ReferenceEntity getContactId() {
        return contactId;
    }

    public void setContactId(ReferenceEntity contactId) {
        this.contactId = contactId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }

    public Date getDateactual() {
        return dateactual;
    }

    public void setDateactual(Date dateactual) {
        this.dateactual = dateactual;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public RegionsEntity getRegionShort() {
        return regionShort;
    }

    public void setRegionShort(RegionsEntity regionShort) {
        this.regionShort = regionShort;
    }

    public List<MessagesEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesEntity> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return this.value;
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

    public PeopleContactEntity archive() {
        this.isactive = 0;
        return this;
    }

    public String getDescription(){
    	String ss="";
    	if (contactId!=null){
    		ss+=this.contactId.getName();
    	}
    	ss+=", "+this.value;
    	return ss;
    }
    
    @Override
    public Boolean equalsContent(BaseEntity other) {
        PeopleContactEntity ent = (PeopleContactEntity) other;
        return Utils.equalsNull(value, StringUtils.isNotEmpty(ent.getValue())?Convertor.fromMask(ent.getValue()):null);
    }
}
