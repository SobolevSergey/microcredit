package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

/**
 * Документы
 */
public class DocumentEntity extends ExtendedBaseEntity implements Serializable {

   
    /**
	 * 
	 */
	private static final long serialVersionUID = 7101030151985820737L;

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    protected Integer txVersion = 0;

    /**
     * серия
     */
    private String series;

    /**
     * номер
     */
    private String number;

    /**
     * дата документа
     */
    private Date docdate;

    /**
     * дата внесения изменений
     */
    private Date dateChange;

    /**
     * дата окончания документа
     */
    private Date docenddate;

    /**
     * выдавшая организация
     */
    private String docorg;

    /**
     * код организации
     */
    private String docorgcode;

    /**
     * запись активна или нет
     */
    private Integer isactive;

    /**
     * комментарий
     */
    private String comment;

    /**
     * была ли загружена
     */
    private Boolean isUploaded;

    /**
     * вид документа по справочнику
     */
    private ReferenceEntity documenttypeId;

    /**
     * причина окончания по справочнику
     */
    private ReferenceEntity reasonendId;

    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;

    /**
     * заявка
     */
    private CreditRequestEntity creditRequestId;

    /**
     * партнер
     */
    private PartnersEntity partnersId;

    /**
     * страницы документа
     */
    private List<DocumentMediaEntity> docpages = new ArrayList<DocumentMediaEntity>(0);

    @Transient
    private boolean isDirty;

    public DocumentEntity() {
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        if (!Utils.equalsNull(this.series, series)) {
            this.series = series;
            isDirty = true;
        }
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if (!Utils.equalsNull(this.number, number)) {
            this.number = number;
            isDirty = true;
        }
    }

    public Date getDocdate() {
        return docdate;
    }

    public void setDocdate(Date docdate) {
        if (docdate == null && this.docdate == null) {
            return;
        }

        if (docdate == null || this.docdate == null) {
            this.docdate = docdate;
            isDirty = true;
        }

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(docdate);

        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(this.docdate);

        if (calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR) ||
                calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH) ||
                calendar1.get(Calendar.DAY_OF_MONTH) != calendar2.get(Calendar.DAY_OF_MONTH)) {
            this.docdate = docdate;
            isDirty = true;
        }
    }

    public Date getDocenddate() {
        return docenddate;
    }

    public void setDocenddate(Date docenddate) {
        if (docenddate == null && this.docenddate == null) {
            return;
        }

        if (docenddate == null || this.docenddate == null) {
            this.docenddate = docenddate;
            isDirty = true;
        }

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(docenddate);

        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(this.docenddate);

        if (calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR) ||
                calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH) ||
                calendar1.get(Calendar.DAY_OF_MONTH) != calendar2.get(Calendar.DAY_OF_MONTH)) {
            this.docenddate = docenddate;
            isDirty = true;
        }
        this.docenddate = docenddate;
    }

    public String getDocorg() {
        return docorg;
    }

    public void setDocorg(String docorg) {
        if (!Utils.equalsNull(this.docorg, docorg)) {
            this.docorg = docorg;
            isDirty = true;
        }
    }

    public String getDocorgcode() {
        return docorgcode;
    }

    public void setDocorgcode(String docorgcode) {
        if (!Utils.equalsNull(this.docorgcode, docorgcode)) {
            this.docorgcode = docorgcode;
            isDirty = true;
        }
    }

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }

    public Boolean getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(Boolean isUploaded) {
        this.isUploaded = isUploaded;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        if (!Utils.equalsNull(this.comment, comment)) {
            this.comment = comment;
            isDirty = true;
        }
    }

    public ReferenceEntity getDocumenttypeId() {
        return documenttypeId;
    }

    public void setDocumenttypeId(ReferenceEntity documenttypeId) {
        this.documenttypeId = documenttypeId;
    }

    public ReferenceEntity getReasonendId() {
        return reasonendId;
    }

    public void setReasonendId(ReferenceEntity reasonendId) {
        this.reasonendId = reasonendId;
    }

    public List<DocumentMediaEntity> getdocpages() {
        return docpages;
    }

    public void setdocpages(List<DocumentMediaEntity> docpages) {
        this.docpages = docpages;
    }

    public PeopleMainEntity getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        this.peopleMainId = peopleMainId;
    }

    public CreditRequestEntity getCreditRequestId() {
        return creditRequestId;
    }

    public void setCreditRequestId(CreditRequestEntity creditRequestId) {
        this.creditRequestId = creditRequestId;
    }

    public PartnersEntity getPartnersId() {
        return partnersId;
    }


    public void setPartnersId(PartnersEntity partnersId) {
        this.partnersId = partnersId;
    }

    public Date getDateChange() {
        return dateChange;
    }

    public void setDateChange(Date dateChange) {
        this.dateChange = dateChange;
    }

    /**
     * паспортные данные строкой
     * @return
     */
    public String getDescription() {
    	String ss="";
    	if (StringUtils.isNotEmpty(series)){
    		ss+=series.trim();
    	}
    	if (StringUtils.isNotEmpty(number)){
    		ss+=", номер " + number.trim();
    	}
    	if (docdate!=null){
    		ss+=", дата выдачи " + sdf.format(docdate);
    	}
    	if (StringUtils.isNotEmpty(docorg)){
    		ss+=", выдавшая организация " + docorg.trim();
    	}
        return ss;
    }

    public String getDescriptionFull() {
       return "паспорт РФ, серия " + getDescription();
    }

    public String getDescriptionFullWithCode() {
        String dcode = (StringUtils.isNotEmpty(docorgcode)?", код подразделения " + docorgcode:"");
        return "паспорт РФ, серия " + getDescription()+dcode;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public DocumentEntity clean() {
        this.isDirty = false;
        return this;
    }

    public DocumentEntity archive() {
        this.isactive = 0;
        return this;
    }

    @Override
    public String toString() {
        return "документ серия " + series.trim() + ", номер " + number.trim() + " дата выдачи " + sdf.format(docdate);
    }

    @Override
    public Boolean equalsContent(BaseEntity other) {
        DocumentEntity ent = (DocumentEntity) other;

        return Utils.equalsNull(comment, ent.getComment()) &&
                Utils.equalsNull(docdate != null ? docdate.getTime() : null,
                        ent.getDocdate() != null ? ent.getDocdate().getTime() : null) &&
                Utils.equalsNull(docenddate != null ? docenddate.getTime() : null,
                        ent.getDocenddate() != null ? ent.getDocenddate().getTime() : null) 
                && Utils.equalsNull(docorg, ent.getDocorg()) 
                && Utils.equalsNull(docorgcode, ent.getDocorgcode())
                && Utils.equalsNull(number, StringUtils.isNotEmpty(ent.getNumber())?Convertor.fromMask(ent.getNumber()):null) 
                && Utils.equalsNull(series, StringUtils.isNotEmpty(ent.getSeries())?Convertor.fromMask(ent.getSeries()):null);
    }
}
