package ru.simplgroupp.transfer;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import ru.simplgroupp.persistence.ReportEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

public class Report extends BaseTransfer<ReportEntity> implements Serializable, Initializable, Identifiable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4355681548818655554L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;
    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
    	return wrapConstructor;
    } 	
    static {
    	try {
			wrapConstructor = Report.class.getConstructor(ReportEntity.class);
		} catch (NoSuchMethodException | SecurityException e) {
			wrapConstructor = null;
		}
    }	
	
	public static final String OFERTA_ID = "oferta.credit";
	public static final String EMAIL_ID = "letter.common";
	public static final String CENTR_EMAIL_ID = "letter.common";
	public static final String CENTR_CODE_EMAIL_ID = "letter.confirm";
	public static final String OFERTA_PROLONG_ID = "oferta.prolong";
	public static final String EMAILOKB_ID = "letter.okb";
	public static final String OFERTA_REFINANCE_ID = "oferta.refinance";
	public static final String LETTER_COLLECTOR = "letter.collector";
	public static final String CONSENT_SIGN = "consent.sign";
	public static final String CONSENT_PERSONAL = "consent.personal";
	
	private ReportType reportType;
	protected Products product;
	
	public Report() {
		super();
		entity = new ReportEntity();
	}
	
	public Report(ReportEntity entity) {
		super(entity);
		if (entity.getReportTypeId()!=null){
			reportType=new ReportType(entity.getReportTypeId());
		}
		if (entity.getProductId()!=null){
			product=new Products(entity.getProductId());
		}
	}
	
	public Integer getId() {
		return entity.getId();
	}
	
	@XmlElement
	public void setId(Integer id) {
		entity.setId(id);
	}
	public String getName() {
		return entity.getName();
	}
	@XmlElement
	public void setName(String name) {
		entity.setName(name);
	}
	public String getCode() {
		return entity.getCode();
	}
	@XmlElement
	public void setCode(String code) {
		entity.setCode(code);
	}
	public String getBody() {
		return entity.getBody();
	}
	@XmlElement
	public void setBody(String body) {
		entity.setBody(body);
	}
	public String getReportExecutor() {
		return entity.getReportExecutor();
	}
	@XmlElement
	public void setReportExecutor(String reportExecutor) {
		entity.setReportExecutor(reportExecutor);
	}
	public String getMimeType() {
		return entity.getMimeType();
	}
	@XmlElement
	public void setMimeType(String mimeType) {
		entity.setMimeType(mimeType);
	}
	
	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	@Override
	public void init(Set options) {
		getName();
		
	}

}
