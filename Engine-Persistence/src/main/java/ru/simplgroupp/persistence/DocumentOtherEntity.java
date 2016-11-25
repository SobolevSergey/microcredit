package ru.simplgroupp.persistence;

import org.apache.commons.lang.StringUtils;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;

import javax.persistence.Transient;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by cobalt on 16.09.15.
 */
public class DocumentOtherEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 5829706011279030063L;
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
     * дата окончания документа
     */
    private Date docenddate;

    /**
     * выдавшая организация
     */
    private String docorg;

    /**
     * запись активна или нет
     */
    private Integer isactive;

    /**
     * комментарий
     */
    private String comment;

    /**
     * вид документа по справочнику
     */
    private ReferenceEntity documenttypeId;

    /**
     * человек
     */
    private PeopleMainEntity peopleMainId;

    /**
     * партнер
     */
    private PartnersEntity partnersId;

    @Transient
    private boolean isDirty;

    public DocumentOtherEntity(){

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

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
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

    public boolean isDirty() {
        return isDirty;
    }

    public DocumentOtherEntity clean() {
        this.isDirty = false;
        return this;
    }

    public DocumentOtherEntity archive() {
        this.isactive = 0;
        return this;
    }

    @Override
    public String toString() {
        return "документ серия " + series.trim() + ", номер " + number.trim() + " дата выдачи " + docdate.toString();
    }

    @Override
    public Boolean equalsContent(BaseEntity other) {
        DocumentOtherEntity ent = (DocumentOtherEntity) other;

        return Utils.equalsNull(comment, ent.getComment()) &&
                Utils.equalsNull(docdate != null ? docdate.getTime() : null,
                        ent.getDocdate() != null ? ent.getDocdate().getTime() : null) &&
                Utils.equalsNull(docenddate != null ? docenddate.getTime() : null,
                        ent.getDocenddate() != null ? ent.getDocenddate().getTime() : null)
                && Utils.equalsNull(docorg, ent.getDocorg())
                && Utils.equalsNull(number, StringUtils.isNotEmpty(ent.getNumber())? Convertor.fromMask(ent.getNumber()):null)
                && Utils.equalsNull(series, StringUtils.isNotEmpty(ent.getSeries())?Convertor.fromMask(ent.getSeries()):null);
    }

}
