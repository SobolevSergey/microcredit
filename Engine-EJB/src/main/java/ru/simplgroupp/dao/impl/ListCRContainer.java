package ru.simplgroupp.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.IntegerRange;
import ru.simplgroupp.toolkit.common.MoneyRange;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Documents;
import ru.simplgroupp.transfer.PeopleContact;
import ru.simplgroupp.util.AbstractListContainer;
import ru.simplgroupp.util.ListContainer;
import ru.simplgroupp.workflow.SignalRef;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ListCRContainer extends AbstractListContainer<CreditRequest> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3766107924238326904L;
	
	@XmlElement
	protected Integer peopleMainId;
	@XmlElement
	protected Integer rejectReasonId;
	@XmlElement
	protected Integer refuseReasonId;
	@XmlElement
	protected Integer statusId;
	@XmlElement
	protected Integer accepted;
	@XmlElement
	protected DateRange date = new DateRange(null, null);
	@XmlElement
	protected IntegerRange days = new IntegerRange(null,null);
	@XmlElement
	protected MoneyRange sum = new MoneyRange(null,null);
	@XmlElement
	protected Integer isover;
	@XmlElement
	protected DateRange creditDateEnd = new DateRange(null, null);
	@XmlElement
	protected String peopleSurname;
	@XmlElement
	protected String peopleName;
	@XmlElement
	protected String peopleMidname;
	@XmlElement
	protected Integer docTypeId;
	@XmlElement
	protected String docSeries;
	@XmlElement
	protected String docNomer;
	@XmlElement
	protected String peopleEmail;
	@XmlElement
	protected String peoplePhone;
	@XmlElement
	protected String peopleSNILS;
	@XmlElement
	protected String peopleINN;
	@XmlElement
	protected String uniqueNomer;
	@XmlElement
	protected String taskDefKey;
	@XmlElement
	protected Integer wayId;
	@XmlElement
	protected Integer decisionWayID;
	@XmlElement
	protected Integer workplace_id;
	@XmlElement
	protected Integer requestID;
	
	private List<Integer> lstCR = null;
	
	@Override
	public String buildHQL(int bForList) {		
		String sql = null, sqlJoin = "";
		
		switch (bForList) {
			case FOR_LIST: sql = "select c [$SELECT_SORTING$] from ru.simplgroupp.persistence.CreditRequestEntity as c [$JOIN$] where (c.peopleMainId is not null)"; break;
			case FOR_COUNT: sql = "select count(c) from ru.simplgroupp.persistence.CreditRequestEntity as c [$JOIN$] where (c.peopleMainId is not null)"; break;
			case FOR_IDS: sql = "select c.id from ru.simplgroupp.persistence.CreditRequestEntity as c [$JOIN$] where (c.peopleMainId is not null)"; break;
		}

		if (prmListId != null) {
			sql = sql + " and (c.id = some (select bi.BusinessObjectId from ru.simplgroupp.persistence.BusinessListItemEntity as bi where bi.list.id = :prmListId)) ";
			return sql.replace("[$JOIN$]", sqlJoin);
		}		
		
        if (peopleMainId != null) {
            sql = sql + " and (c.peopleMainId.id = :peopleMainId)";
        }     
        if (peopleSNILS != null) {
        	sql = sql + " and (c.peopleMainId.snils = :peopleSNILS)";
        }
        if (peopleINN != null) {
        	sql = sql + " and (c.peopleMainId.inn = :peopleINN)";
        }        
        if (statusId != null) {
            sql = sql + " and (c.statusId.id = :statusId)";
        }
        if (wayId != null) {
            sql = sql + " and (c.wayId.codeinteger = :wayId)";
        }
        if (decisionWayID != null) {
            sql = sql + " and (c.decisionWayId.codeinteger = :decisionWayID)";
        }
		if(workplace_id!=null){
			sql += " and (c.workplace.id = :workplace_id)";
		}
		if (requestID != null) {
			sql += " and (c.id = :requestID)";
		}
        if (rejectReasonId != null) {
            sql = sql + " and (c.rejectreasonId.reasonId.codeinteger = :rejectreasonId)";
        }
        if (refuseReasonId != null) {
            sql = sql + " and (c.rejectreasonId.id = :refuseReasonId)";
        }
        if (accepted != null) {
            sql = sql + " and (c.accepted = :accepted)";
        }
        if ((date != null) && (date.getFrom() != null)) {
            sql = sql + " and (c.datecontest >= :dfrom)";
        }
        if ((date != null) && (date.getTo() != null)) {
            sql = sql + " and (c.datecontest < :dto)";
        }
        if ((days != null) && (days.getFrom() != null)) {
            sql = sql + " and (c.creditdays>=:daysfrom)";
        }
        if ((days != null) && (days.getTo() != null)) {
            sql = sql + " and (c.creditdays<=:daysto)";
        }
        if ((sum != null) && (sum.getFrom() != null)) {
            sql = sql + " and (c.creditsum>=:sumfrom)";
        }
        if ((sum != null) && (sum.getTo() != null)) {
            sql = sql + " and (c.creditsum<=:sumto)";
        }
        if (isover != null) {
        	if (isover == 1) {
        		sql = sql + " and (c.acceptedcreditId.creditdataend < :dateNow)";
        	} else {
        		sql = sql + " and (c.acceptedcreditId.creditdataend >= :dateNow)";
        	}
        }
        if (creditDateEnd != null && creditDateEnd.getFrom() != null) {
            sql = sql + " and (c.acceptedcreditId.creditdataend >= :creditDateEndFrom)";
        }
        if (creditDateEnd != null && creditDateEnd.getTo() != null) {
            sql = sql + " and (c.acceptedcreditId.creditdataend < :creditDateEndTo)";
        }
        if (uniqueNomer != null) {
            sql = sql + " and (c.uniquenomer like :uniqueNomer)";
        }
        if (peopleSurname == null && peopleName == null && peopleMidname == null) {
//	    	sql = sql.replace("[$JOIN_PEOPLE$]", "");
        } else {
            sql = sql + " and ( (select count(*) from c.peopleMainId as pm inner join pm.peoplepersonal  as p where [$WHERE_PEOPLE$] ) > 0 )";

            String swhere = "(1=1)";
            if (peopleSurname != null) {
                swhere = swhere + " and (upper(p.surname) like :peopleSurname)";
            }
            if (peopleName != null) {
                swhere = swhere + " and (upper(p.name) like :peopleName)";
            }
            if (peopleMidname != null) {
                swhere = swhere + " and (upper(p.midname) like :peopleMidname)";
            }
            sql = sql.replace("[$WHERE_PEOPLE$]", swhere);
        }

        if (!(docTypeId == null && docSeries == null && docNomer == null)) {
            sql = sql + " and ( (select count(*) from c.peopleMainId as pm1 inner join pm1.documents as d where [$WHERE_DOCS$] ) > 0 )";

            String swhere = "(1=1)";
            if (docTypeId != null) {
                swhere = swhere + " and (d.documenttypeId.codeinteger = :docTypeId)";
            }
            if (docSeries != null) {
                swhere = swhere + " and (d.series = :docSeries)";
            }
            if (docNomer != null) {
                swhere = swhere + " and (d.number = :docNomer)";
            }
            sql = sql.replace("[$WHERE_DOCS$]", swhere);
        }

        if (!((peopleEmail == null) && (peoplePhone == null))) {
            sql = sql + " and ( (select count(*) from c.peopleMainId as pm2 inner join pm2.peoplecontact as t where [$WHERE_CONTACT$] ) > 0 )";

            String swhere = "(1=0)";
            if (peopleEmail != null) {
                swhere = swhere + " or ((t.contactId.codeinteger = :codeConEmail) and (t.value = :peopleEmail))";
            }
            if (peoplePhone != null) {
                swhere = swhere + " or ((t.contactId.codeinteger in (:codeConPhoneHome, :codeConPhoneWork, :codeConPhoneMobile)) and (t.value = :peoplePhone))";
            }

            sql = sql.replace("[$WHERE_CONTACT$]", swhere);
        }
        
	    if (lstCR != null) {
	    	if (lstCR.size() == 0) {
	    		sql = sql + " and (1=0)";
	    	} else {
	    		sql = sql + " and (c.id in (:lstCR))";
	    	}
	    }		

	    return sql.replace("[$JOIN$]", sqlJoin);
	}

	@Override
	public void setHQLParams(Query qry, int bForList) {
		if (prmListId != null) {
			qry.setParameter("prmListId", prmListId);
			return;
		}
		
        if (peopleMainId != null) {
            qry.setParameter("peopleMainId", peopleMainId);
        }
        if (peopleSNILS != null) {
            qry.setParameter("peopleSNILS", peopleSNILS);
        }     
        if (peopleINN != null) {
            qry.setParameter("peopleINN", peopleINN);
        }         
        if (statusId != null) {
            qry.setParameter("statusId", statusId);
        }
        if (wayId != null) {
            qry.setParameter("wayId", wayId);
        }
        if (decisionWayID != null) {
            qry.setParameter("decisionWayID", decisionWayID);
        }
		if(workplace_id!=null){
			qry.setParameter("workplace_id",workplace_id);
		}
		if (requestID != null) {
			qry.setParameter("requestID", requestID);
		}
		if (rejectReasonId != null) {
            qry.setParameter("rejectreasonId", rejectReasonId);
        }
        if (refuseReasonId != null) {
            qry.setParameter("refuseReasonId", refuseReasonId);
        }
        if (accepted != null) {
            qry.setParameter("accepted", accepted);
        }
        if ((date != null) && (date.getFrom() != null)) {
            qry.setParameter("dfrom", date.getFrom(),TemporalType.DATE);
        }
        if ((date != null) && (date.getTo() != null)) {
            qry.setParameter("dto", DateUtils.addDays(date.getTo(),1),TemporalType.DATE);
        }
        if ((days != null) && (days.getFrom() != null)) {
            qry.setParameter("daysfrom", days.getFrom());
        }
        if ((days != null) && (days.getTo() != null)) {
            qry.setParameter("daysto", days.getTo());
        }
        if ((sum != null) && (sum.getFrom() != null)) {
            qry.setParameter("sumfrom", sum.getFrom().doubleValue());
        }
        if ((sum != null) && (sum.getTo() != null)) {
            qry.setParameter("sumto", sum.getTo().doubleValue());
        }
        if (isover != null) {
            qry.setParameter("dateNow", new Date(),TemporalType.DATE);
        }
        if (creditDateEnd != null && creditDateEnd.getFrom() != null) {
            qry.setParameter("creditDateEndFrom", creditDateEnd.getFrom(),TemporalType.DATE);
        }
        if (creditDateEnd != null && creditDateEnd.getTo() != null) {
            qry.setParameter("creditDateEndTo", DateUtils.addDays(creditDateEnd.getTo(),1),TemporalType.DATE);
        }
        if (uniqueNomer != null) {
            qry.setParameter("uniqueNomer", "%"+uniqueNomer+"%");
        }

            if (peopleSurname != null) {
                qry.setParameter("peopleSurname", "%" + peopleSurname.trim().toUpperCase() + "%");
            }
            if (peopleName != null) {
                qry.setParameter("peopleName", "%" + peopleName.trim().toUpperCase() + "%");
            }
            if (peopleMidname != null) {
                qry.setParameter("peopleMidname", "%" + peopleMidname.trim().toUpperCase() + "%");
            }
 
        if (!(docTypeId == null && docSeries == null && docNomer == null)) {
            if (docTypeId != null) {
                qry.setParameter("docTypeId", docTypeId);
            }
            if (docSeries != null) {
                qry.setParameter("docSeries", docSeries);
            }
            if (docNomer != null) {
                qry.setParameter("docNomer", docNomer);
            }
        }

        if (!((peopleEmail == null) && (peoplePhone == null))) {
            if (peopleEmail != null) {
                qry.setParameter("codeConEmail", PeopleContact.CONTACT_EMAIL);
                qry.setParameter("peopleEmail", peopleEmail);
            }
            if (peoplePhone != null) {
                qry.setParameter("codeConPhoneHome", PeopleContact.CONTACT_HOME_PHONE);
                qry.setParameter("codeConPhoneWork", PeopleContact.CONTACT_WORK_PHONE);
                qry.setParameter("codeConPhoneMobile", PeopleContact.CONTACT_CELL_PHONE);
                qry.setParameter("peoplePhone", peoplePhone);
            }
        }
        
        if (lstCR != null && lstCR.size() > 0) {        	
        	qry.setParameter("lstCR", lstCR);
        }

		
	}

	@Override
	public void nullIfEmpty() {
		if (StringUtils.isBlank(peopleSurname)) {
			peopleSurname = null;
		}
		if (StringUtils.isBlank(peopleName)) {
			peopleName = null;
		}	
		if (StringUtils.isBlank(peopleMidname)) {
			peopleMidname = null;
		}		
		if (StringUtils.isBlank(docSeries)) {
			docSeries = null;
		}	
		if (StringUtils.isBlank(docNomer)) {
			docNomer = null;
		}	
		if (StringUtils.isBlank(peopleEmail)) {
			peopleEmail = null;
		}	
		if (StringUtils.isBlank(peoplePhone)) {
			peoplePhone = null;
		}
		if (StringUtils.isBlank(peopleINN)) {
			peopleINN = null;
		}		
		if (StringUtils.isBlank(peopleSNILS)) {
			peopleSNILS = null;
		}		
		if (StringUtils.isBlank(peopleSNILS)) {
			peopleSNILS = null;
		}	
		if (StringUtils.isBlank(uniqueNomer)) {
			uniqueNomer = null;
		}		
		
		if (ListContainer.NULL_INT_VALUE.equals(statusId)) {
			statusId = null;
		}
		if (ListContainer.NULL_INT_VALUE.equals(wayId)) {
			wayId = null;
		}
		if (ListContainer.NULL_INT_VALUE.equals(decisionWayID)) {
			decisionWayID = null;
		}
		if(ListContainer.NULL_INT_VALUE.equals(workplace_id)){
			workplace_id = null;
		}
		if (requestID != null && requestID == 0) {
			requestID = null;
		}
		if (ListContainer.NULL_INT_VALUE.equals(rejectReasonId)) {
			rejectReasonId = null;
		}
		if (ListContainer.NULL_INT_VALUE.equals(refuseReasonId)) {
			refuseReasonId = null;
		}
		if (ListContainer.NULL_INT_VALUE.equals(accepted)) {
			accepted = null;
		}	
		if (ListContainer.NULL_INT_VALUE.equals(isover)) {
			isover = null;
		}	
		if (ListContainer.NULL_INT_VALUE.equals(docTypeId)) {
			docTypeId = null;
		}	
		if (days != null && days.getFrom() != null && days.getFrom().intValue() == 0) {
			days.setFrom(null);
		}
		if (days != null && days.getTo() != null && days.getTo().intValue() == 0) {
			days.setTo(null);
		}
		if (sum != null && sum.getFrom() != null && sum.getFrom().intValue() == 0) {
			sum.setFrom(null);
		}
		if (sum != null && sum.getTo() != null && sum.getTo().intValue() == 0) {
			sum.setTo(null);
		}		
		
	    if ( (docNomer != null ) || (docSeries != null ) ) {
			docTypeId = Documents.PASSPORT_RF;
		}
		
		if (StringUtils.isBlank(taskDefKey)) {
			taskDefKey = null;
		}
		
	}

	public Integer getPeopleMainId() {
		return peopleMainId;
	}

	public void setPeopleMainId(Integer peopleMainId) {
		this.peopleMainId = peopleMainId;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public Integer getRefuseReasonId() {
		return refuseReasonId;
	}

	public void setRefuseReasonId(Integer refuseReasonId) {
		this.refuseReasonId = refuseReasonId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getAccepted() {
		return accepted;
	}

	public void setAccepted(Integer accepted) {
		this.accepted = accepted;
	}

	public DateRange getDate() {
		return date;
	}

	public void setDate(DateRange date) {
		this.date = date;
	}

	public IntegerRange getDays() {
		return days;
	}

	public void setDays(IntegerRange days) {
		this.days = days;
	}

	public MoneyRange getSum() {
		return sum;
	}

	public void setSum(MoneyRange sum) {
		this.sum = sum;
	}

	public Integer getIsover() {
		return isover;
	}

	public void setIsover(Integer isover) {
		this.isover = isover;
	}

	public DateRange getCreditDateEnd() {
		return creditDateEnd;
	}

	public void setCreditDateEnd(DateRange creditDateEnd) {
		this.creditDateEnd = creditDateEnd;
	}

	public String getPeopleSurname() {
		return peopleSurname;
	}

	public void setPeopleSurname(String peopleSurname) {
		this.peopleSurname = peopleSurname;
	}

	public String getPeopleName() {
		return peopleName;
	}

	public void setPeopleName(String peopleName) {
		this.peopleName = peopleName;
	}

	public String getPeopleMidname() {
		return peopleMidname;
	}

	public void setPeopleMidname(String peopleMidname) {
		this.peopleMidname = peopleMidname;
	}

	public Integer getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Integer docTypeId) {
		this.docTypeId = docTypeId;
	}

	public String getDocSeries() {
		return docSeries;
	}

	public void setDocSeries(String docSeries) {
		this.docSeries = docSeries;
	}

	public String getDocNomer() {
		return docNomer;
	}

	public void setDocNomer(String docNomer) {
		this.docNomer = docNomer;
	}

	public String getPeopleEmail() {
		return peopleEmail;
	}

	public void setPeopleEmail(String peopleEmail) {
		this.peopleEmail = peopleEmail;
	}

	public String getPeoplePhone() {
		return peoplePhone;
	}

	public void setPeoplePhone(String peoplePhone) {
		this.peoplePhone = peoplePhone;
	}

	public String getPeopleSNILS() {
		return peopleSNILS;
	}

	public void setPeopleSNILS(String peopleSNILS) {
		this.peopleSNILS = peopleSNILS;
	}

	public String getPeopleINN() {
		return peopleINN;
	}

	public void setPeopleINN(String peopleINN) {
		this.peopleINN = peopleINN;
	}

	public String getUniqueNomer() {
		return uniqueNomer;
	}

	public void setUniqueNomer(String uniqueNomer) {
		this.uniqueNomer = uniqueNomer;
	}

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public Integer getWayId() {
		return wayId;
	}

	public void setWayId(Integer wayId) {
		this.wayId = wayId;
	}

	public Integer getDecisionWayID() {
		return decisionWayID;
	}

	public void setDecisionWayID(Integer decisionWayID) {
		this.decisionWayID = decisionWayID;
	}

	public Integer getWorkplace_id() {
		return workplace_id;
	}

	public void setWorkplace_id(Integer workplace_id) {
		this.workplace_id = workplace_id;
	}

	public Integer getRequestID() {
		return requestID;
	}

	public void setRequestID(Integer requestID) {
		this.requestID = requestID;
	}

	@Override
	public void prepareParams() {
		if ( StringUtils.isBlank(taskDefKey)) {
			lstCR = null;
			return;
		}
		
		if (lstCR != null) {
			return;
		}

		SignalRef ref = SignalRef.valueOf(taskDefKey);
	    lstCR = runServ.getWfEngine().listTasksCR(ref.getName());
	}

	@Override
	protected CreditRequest wrapEntity(Object entity) {
		return new CreditRequest((CreditRequestEntity) entity);
	}

	@Override
	public void clearParams() {
		peopleMainId = null;
		rejectReasonId = null;
		refuseReasonId = null;
		statusId = null;
		accepted = null;
		date = new DateRange(null, null);
		days = new IntegerRange(null,null);
		sum = new MoneyRange(null,null);
		isover = null;
		creditDateEnd = new DateRange(null, null);
		peopleSurname = null;
		peopleName = null;
		peopleMidname = null;
		docTypeId = null;
		docSeries = null;
		docNomer = null;
		peopleEmail = null;
		peoplePhone = null;
		peopleSNILS = null;
		peopleINN = null;
		uniqueNomer = null;
		taskDefKey = null;
		lstCR = null;
		wayId=null;
		decisionWayID=null;
		workplace_id=null;
		requestID=null;
	}

	@Override
	public String generateListName() {
		String sname = "Заявки ";
		sname = sname + "от " + new Date().toString();
		return sname;
	}

	@Override
	public void copyParams(ListContainer<CreditRequest> source) {
		ListCRContainer src = (ListCRContainer) source;
		this.accepted = src.accepted;
		this.creditDateEnd = src.creditDateEnd;
		this.date = src.date;
		this.days = src.days;
		this.docNomer = src.docNomer;
		this.docSeries = src.docSeries;
		this.docTypeId = src.docTypeId;
		this.isover = src.isover;
		this.peopleEmail = src.peopleEmail;
		this.peopleINN = src.peopleINN;
		this.peopleMainId = src.peopleMainId;
		this.peopleMidname = src.peopleMidname;
		this.peopleName = src.peopleName;
		this.peoplePhone = src.peoplePhone;
		this.peopleSNILS = src.peopleSNILS;
		this.peopleSurname = src.peopleSurname;
		this.prmListId = src.prmListId;
		this.rejectReasonId = src.rejectReasonId;
		this.refuseReasonId = src.refuseReasonId;
		this.statusId = src.statusId;
		this.sum = src.sum;
		this.taskDefKey = src.taskDefKey;
		this.uniqueNomer = src.uniqueNomer;
		this.wayId = src.wayId;
		this.decisionWayID = src.decisionWayID;
		this.workplace_id = src.workplace_id;
		this.requestID = src.requestID;
	}
}

