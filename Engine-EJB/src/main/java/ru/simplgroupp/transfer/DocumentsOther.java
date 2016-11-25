package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.DocumentOtherEntity;
import ru.simplgroupp.toolkit.common.Initializable;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

/**
 * Created by cobalt on 16.09.15.
 */
public class DocumentsOther extends BaseTransfer<DocumentOtherEntity> implements Serializable, Initializable {
    private static final long serialVersionUID = -3683596475287957073L;

    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }
    static {
        try {
            wrapConstructor = DocumentsOther.class.getConstructor(DocumentOtherEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    public enum Options {
        INIT_DOCPAGES;
    }

    protected Reference documentType;
    protected PeopleMain peopleMain;
    protected Partner partner;

    public DocumentsOther() {
        super();
        entity = new DocumentOtherEntity();
    }

    public DocumentsOther(DocumentOtherEntity entity) {
        super(entity);
        internalCreate(true);
    }

    public DocumentsOther(DocumentOtherEntity entity, boolean isRoot) {
        super(entity);
        internalCreate(isRoot);
    }

    private void internalCreate(boolean isRoot) {
        documentType = new Reference(entity.getDocumenttypeId());
        if (isRoot) {
            peopleMain=new PeopleMain(entity.getPeopleMainId());
        }
        partner=new Partner(entity.getPartnersId());
    }

    @Override
    public void init(Set options) {
    }

    public Integer getId() {
        return entity.getId();
    }

    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }

    public String getSeries() {
        return entity.getSeries();
    }

    @XmlElement
    public void setSeries(String series) {
        entity.setSeries(series);
    }

    public String getNumber() {
        return entity.getNumber();
    }

    @XmlElement
    public void setNumber(String number) {
        entity.setNumber(number);
    }

    public Date getDocDate() {
        return entity.getDocdate();
    }

    @XmlElement
    public void setDocDate(Date docdate) {
        entity.setDocdate(docdate);
    }

    public Date getDocEndDate() {
        return entity.getDocenddate();
    }

    @XmlElement
    public void setDocEndDate(Date docenddate) {
        entity.setDocenddate(docenddate);
    }

    public String getDocOrg() {
        return entity.getDocorg();
    }

    @XmlElement
    public void setDocOrg(String docorg) {
        entity.setDocorg(docorg);
    }

    public Integer getIsActive() {
        return entity.getIsactive();
    }

    @XmlElement
    public void setIsActive(Integer isactive) {
        entity.setIsactive(isactive);
    }

    public String getComment() {
        return entity.getComment();
    }

    @XmlElement
    public void setComment(String comment) {
        entity.setComment(comment);
    }


    public Reference getDocumentType() {
        return documentType;
    }

    @XmlElement
    public void setDocumentType(Reference documentType) {
        this.documentType = documentType;
    }

    public PeopleMain getPeopleMain() {
        return peopleMain;
    }


    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain=peopleMain;
    }

    public Partner getPartner() {
        return partner;
    }

    @XmlElement
    public void setPartner(Partner partner) {
        this.partner=partner;
    }
}
