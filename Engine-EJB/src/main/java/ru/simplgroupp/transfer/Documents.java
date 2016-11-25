package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.DocumentMediaEntity;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

public class Documents extends BaseTransfer<DocumentEntity> implements Serializable, Initializable {
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6759597408208786528L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Documents.class.getConstructor(DocumentEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	public static final int PASSPORT_RF = 21;
	public static final String PASSPORT_RF_TEXT = "Паспорт гражданина Российской Федерации";
	public static final int PASSPORT_RF_EQUIFAX_RS = 1;
	public static final String PASSPORT_RF_OKB="01";
		
	public static final int VALID=1;
	public static final int INVALID=9;
	
	public enum Options {
		INIT_DOCPAGES;
	}

	protected Reference documentType;
	protected Reference reasonEnd;
	protected List<DocumentMedia> docpages;
	protected PeopleMain peopleMain;
	protected CreditRequest creditRequest;
	protected Partner partner;
	
	public Documents() {
		super();
		entity = new DocumentEntity();
	}

	public Documents(DocumentEntity entity) {
		super(entity);
		if (entity.getReasonendId() != null) {
			reasonEnd = new Reference(entity.getReasonendId());
		}
		internalCreate(true);
	}
	
	public Documents(DocumentEntity entity, boolean isRoot) {
		super(entity);
		internalCreate(isRoot);
	}	
	
	private void internalCreate(boolean isRoot) {
		documentType = new Reference(entity.getDocumenttypeId());
		if (isRoot) {
			peopleMain=new PeopleMain(entity.getPeopleMainId());
		}
		if ( isRoot && entity.getCreditRequestId()!=null) {
			creditRequest=new CreditRequest(entity.getCreditRequestId());
		}
        partner=new Partner(entity.getPartnersId());
		docpages = new ArrayList<DocumentMedia>(0);		
	}

	@Override
	public void init(Set options) {
		if (options.contains(Options.INIT_DOCPAGES)) {
			Utils.initCollection(entity.getdocpages(), options);
			for (DocumentMediaEntity medEnt: entity.getdocpages()) {
				DocumentMedia media = new DocumentMedia(medEnt);
				media.init(options);
				docpages.add(media);
			}
			Collections.sort(docpages, new DocumentMedia.PageNumberComparator());
		}
		
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

    public Date getDateChange() {
        return entity.getDateChange();
    }

    @XmlElement
    public void setDateChange(Date dateChange) {
    	entity.setDateChange(dateChange);;
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

    public String getDocOrgCode() {
        return entity.getDocorgcode();
    }

    @XmlElement
    public void setDocOrgCode(String docorgcode) {
    	entity.setDocorgcode(docorgcode);
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

    public Boolean getIsUploaded() {
        return entity.getIsUploaded();
    }

    public void setIsUploaded(Boolean isUploaded) {
        entity.setIsUploaded(isUploaded);
    }
    
    public Reference getDocumentType() {
        return documentType;
    }

    @XmlElement
    public void setDocumentType(Reference documentType) {
        this.documentType = documentType;
    }

    public Reference getReasonEnd() {
        return reasonEnd;
    }

    @XmlElement
    public void setReasonEnd(Reference reasonEnd) {
        this.reasonEnd = reasonEnd;
    }
    
    public List<DocumentMedia> getDocPages() {
    	return docpages;
    }	
    
    public PeopleMain getPeopleMain() {
        return peopleMain;
    }


    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain=peopleMain;
    }
    
    public CreditRequest getCreditRequest() {
        return creditRequest;
    }


    public void setCreditRequest(CreditRequest creditRequest) {
        this.creditRequest=creditRequest;
    }
    
    
    public Partner getPartner() {
        return partner;
    }

    @XmlElement
    public void setPartner(Partner partner) {
        this.partner=partner;
    }
    
    public String getDescription() {
    	return entity.getDescription();
    }
    
    public String getDescriptionFull() {
    	return entity.getDescriptionFull();
    }
    
    public String getDescriptionFullWithCode() {
    	return entity.getDescriptionFullWithCode();
    }
    
    public DocumentMedia getSinglePage() {
    	return docpages.get(0);
    }
    
      
}
