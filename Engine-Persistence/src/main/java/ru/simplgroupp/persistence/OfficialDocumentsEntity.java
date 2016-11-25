package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.toolkit.common.Utils;

public class OfficialDocumentsEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7279379791351001443L;

	protected Integer txVersion = 0;

    /**
     * номер
     */
    private String docNumber;

    /**
     * дата документа
     */
    private Date docDate;

    /**
     * дата внесения изменений
     */
    private Date dateChange;

    /**
     * код смс
     */
    private String smsCode;

    /**
     * запись активна или нет
     */
    private Integer isActive;

    /**
     * текст документа
     */
    private String docText;

    /**
     * вид документа по справочнику
     */
    private ReferenceEntity documentTypeId;

    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;

    /**
     * id из таблицы, к чему относится данный документ (продление, рефинансирование)
     */
    private Integer anotherId;
    /**
	 * заявка
	 */
	private CreditRequestEntity creditRequestId;
	/**
     * кредит
     */
    private CreditEntity creditId;
    
    public OfficialDocumentsEntity(){
    	
    }

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public Date getDocDate() {
		return docDate;
	}

	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}

	public Date getDateChange() {
		return dateChange;
	}

	public void setDateChange(Date dateChange) {
		this.dateChange = dateChange;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getDocText() {
		return docText;
	}

	public void setDocText(String docText) {
		this.docText = docText;
	}

	public ReferenceEntity getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(ReferenceEntity documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public PeopleMainEntity getPeopleMainId() {
		return peopleMainId;
	}

	public void setPeopleMainId(PeopleMainEntity peopleMainId) {
		this.peopleMainId = peopleMainId;
	}

	public Integer getAnotherId() {
		return anotherId;
	}

	public void setAnotherId(Integer anotherId) {
		this.anotherId = anotherId;
	}

	public boolean isOfertaSigned(){
		return (StringUtils.isNotEmpty(this.smsCode));
	}
	
	
	public CreditRequestEntity getCreditRequestId() {
		return creditRequestId;
	}

	public void setCreditRequestId(CreditRequestEntity creditRequestId) {
		this.creditRequestId = creditRequestId;
	}

	public CreditEntity getCreditId() {
		return creditId;
	}

	public void setCreditId(CreditEntity creditId) {
		this.creditId = creditId;
	}

	@Override
	public Boolean equalsContent(BaseEntity other) {
		 OfficialDocumentsEntity doc=(OfficialDocumentsEntity) other;
		 return Utils.equalsNull(docText, doc.getDocText()) &&
	            Utils.equalsNull(docDate != null ? docDate.getTime() : null,
	                        doc.getDocDate() != null ? doc.getDocDate().getTime() : null) &&
	            Utils.equalsNull(smsCode, doc.getSmsCode()) &&
	            Utils.equalsNull(docNumber, doc.getDocNumber()) &&
	            Utils.equalsNull(documentTypeId!=null?documentTypeId.getId():null,
	            		doc.getDocumentTypeId()!=null?doc.getDocumentTypeId().getId():null);
	}
}
