package ru.simplgroupp.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.time.DateUtils;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.UploadingDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.service.UploadingService;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.UploadingEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.CreditStatus;
import ru.simplgroupp.transfer.UploadStatus;
import ru.simplgroupp.transfer.Uploading;
import ru.simplgroupp.util.DatesUtils;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UploadingDAOImpl implements UploadingDAO{

	@PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    
    @EJB
	CreditRequestDAO creditRequestDAO;
	
	@EJB
    ReferenceBooksLocal refBooks;
	
	@EJB
	UploadingService uploadingService;
	
	@Override
	public UploadingEntity getUploadingEntity(Integer id) {
		return emMicro.find(UploadingEntity.class, id);
	}
	
	@Override
	public UploadingEntity newUploading(UploadingEntity uploading,
			Integer partnersId, Integer uploadTypeId, String name, String info,
			Date dateUploading, Integer statusId, Date responseDate,
			String report, Integer recordsAll, Integer recordsCorrect,
			Integer recordsError,boolean isCreated) throws KassaException{
		
		UploadingEntity upl=null;
		if (uploading==null){
		     upl=new UploadingEntity();
		     upl.setPartnersId(refBooks.getPartnerEntity(partnersId));
		} else {
			 upl=uploading;
		}
		
		upl.setUploadType(uploadTypeId);
		//номер, если только создаем файл
		if (isCreated){
		    Integer uplId=uploadingService.findMaxUploadId(partnersId,uploadTypeId);
		    upl.setUploadId(uplId);
		}
		upl.setInfo(info);
		upl.setName(name);
		upl.setStatus(statusId);
		upl.setReport(report);
		upl.setResponseDate(responseDate);
		upl.setRecordsAll(recordsAll);
		upl.setRecordsCorrect(recordsCorrect);
		upl.setRecordsCorrect(recordsCorrect);
		
		upl=saveUpload(upl);
		return upl;
	
	}
	
	@Override
	public void updateAfterUpload(Date sendingDate) {
		// заявки с измененным статусом
		String sql="update ru.simplgroupp.persistence.CreditRequestEntity set isUploaded=true where dateStatus<=:dt ";
		Query qry = emMicro.createQuery(sql);
		qry.setParameter("dt", sendingDate);
		qry.executeUpdate();	
		//человек
	    sql="update ru.simplgroupp.persistence.PeoplePersonalEntity set isUploaded=true where isactive=1 and peopleMainId.id in (select peopleMainId.id from ru.simplgroupp.persistence.CreditRequestEntity where isUploaded=true)";
		qry = emMicro.createQuery(sql);
		qry.executeUpdate();
		//документ
		sql="update ru.simplgroupp.persistence.DocumentEntity set isUploaded=true where isactive=1 and peopleMainId.id in (select peopleMainId.id from ru.simplgroupp.persistence.CreditRequestEntity where isUploaded=true)";
		qry = emMicro.createQuery(sql);
		qry.executeUpdate();
		//кредит
		sql="update ru.simplgroupp.persistence.CreditEntity set isUploaded=true where isactive=1 and creditRequestId.id in (select id from ru.simplgroupp.persistence.CreditRequestEntity where isUploaded=true)";
		qry = emMicro.createQuery(sql);
		qry.executeUpdate();
	}
	
	@Override
	public UploadingEntity getLastUploading(Integer partnerId, Integer type) {
		String sql=" from ru.simplgroupp.persistence.UploadingEntity  where partnersId.id=:partnerId ";
		if (type!=null){
			sql+=" and uploadType=:type ";
		}
		sql+=" order by dateUploading desc ";
		Query qry = emMicro.createQuery(sql);
		qry.setParameter("partnerId", partnerId);
		if (type!=null){
			qry.setParameter("type", type);
		}
		List<UploadingEntity> lp=qry.getResultList();
		return (UploadingEntity) Utils.firstOrNull(lp);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UploadingEntity saveUpload(UploadingEntity upl) throws KassaException {
		upl=emMicro.merge(upl);
		emMicro.persist(upl);
		return upl;
	}

	@Override
	public List<CreditRequestEntity> getListForUpload(DateRange sendingDate,Integer creditRequestOnly) {
		
		// заявки с измененным статусом
		String sql="from ru.simplgroupp.persistence.CreditRequestEntity  where id in (select id from ru.simplgroupp.persistence.CreditRequestEntity where statusId.id<>:status $statuses$ and $credit$ ( (dateStatus between :dtFrom and :dtTo) $creditdate$))";
		if (creditRequestOnly==UploadStatus.UPLOAD_CREDITREQUEST){
			//только заявки
			sql=sql.replace("$credit$", "");
			sql=sql.replace("$creditdate$", "");
			sql=sql.replace("$statuses$", " and (statusId.id in (:statuses)) ");
		} else if(creditRequestOnly==UploadStatus.UPLOAD_CREDIT){
			//только кредиты
			sql=sql.replace("$statuses$", "");
			sql=sql.replace("$credit$", " acceptedcreditId is not null  and acceptedcreditId.isactive=:isactive and acceptedcreditId.creditStatusId.codeinteger<>:creditStatus and ");
			sql=sql.replace("$creditdate$", " or (acceptedcreditId.dateStatus between :dtFrom and :dtTo)");
		} else if (creditRequestOnly==UploadStatus.UPLOAD_CREDIT_CREDITREQUEST){
			//заявки и кредиты вместе
			sql=sql.replace("$credit$", " acceptedcreditId is null and ");
			sql=sql.replace("$creditdate$", "");
			sql=sql.replace("$statuses$", " and (statusId.id in (:statuses)) ");
		}
		if (creditRequestOnly==UploadStatus.UPLOAD_CREDIT_CREDITREQUEST){
			//заявки и кредиты вместе
			sql+="  or id in (select id from ru.simplgroupp.persistence.CreditRequestEntity where acceptedcreditId is not null  and acceptedcreditId.isactive=:isactive and acceptedcreditId.creditStatusId.codeinteger<>:creditStatus and  (acceptedcreditId.dateStatus between :dtFrom and :dtTo))";
		}
		sql+=" order by id";
		Query qry = emMicro.createQuery(sql);
		qry.setParameter("status", CreditStatus.IN_PROCESS);
		qry.setParameter("dtFrom", sendingDate.getFrom());
		qry.setParameter("dtTo", sendingDate.getTo());
		if (creditRequestOnly!=UploadStatus.UPLOAD_CREDITREQUEST){
		    qry.setParameter("isactive", ActiveStatus.ACTIVE);
		    qry.setParameter("creditStatus", BaseCredit.CREDIT_CANCELLED);
		}
		if (creditRequestOnly!=UploadStatus.UPLOAD_CREDIT){
		    qry.setParameter("statuses", Arrays.asList(CreditStatus.CLIENT_REFUSE, 
		    		CreditStatus.FILLED,CreditStatus.CLOSED,CreditStatus.REJECTED,CreditStatus.DECISION));
		   /* qry.setParameter("statuses", Arrays.asList(CreditStatus.CLIENT_REFUSE, 
		    		CreditStatus.CLOSED,CreditStatus.REJECTED,CreditStatus.DECISION));*/
		}
		return qry.getResultList();
	}
	
	@Override
	public List<CreditRequestEntity> getListForUpload(DateRange sendingDate,
			List<Integer> creditIds) {
		
		String sql="from ru.simplgroupp.persistence.CreditRequestEntity  where  acceptedcreditId is not null  and acceptedcreditId.isactive=:isactive and acceptedcreditId.id in (:creditIds) order by id";
		return JPAUtils.getResultListFromSql(emMicro, sql, Utils.mapOf("isactive", ActiveStatus.ACTIVE,
				"creditIds", creditIds));
	}
	
	@Override
	public List<CreditRequestEntity> getListForUploadAll(Integer creditOnly) {
		String sql="from ru.simplgroupp.persistence.CreditRequestEntity  where statusId.id<>:status ";
		if (creditOnly==UploadStatus.UPLOAD_CREDIT){
			sql+=" and acceptedcreditId is not null  and acceptedcreditId.isactive=:isactive";
		}
		Query qry = emMicro.createQuery(sql);
		qry.setParameter("status", CreditStatus.IN_PROCESS);
		if (creditOnly==UploadStatus.UPLOAD_CREDIT){
		    qry.setParameter("isactive", ActiveStatus.ACTIVE);
		}
		
		return qry.getResultList();
	}
	
	@Override
	public List<CreditRequestEntity> getListForUploadDaily(Date sendingDate) {
   		String sql=" from ru.simplgroupp.persistence.CreditRequestEntity  where date_part('day',dateStatus)=:day and date_part('month',dateStatus)=:month and date_part('year',dateStatus)=:year and acceptedcreditId is not null and acceptedcreditId.isactive=:isactive";
		return JPAUtils.getResultListFromSql(emMicro, sql, Utils.mapOf("day", DatesUtils.getDay(sendingDate),
				"month", DatesUtils.getMonth(sendingDate),
				"year", DatesUtils.getYear(sendingDate),
				"isactive", ActiveStatus.ACTIVE));
		
	}
	
	@Override
	public List<Uploading> listUploading(int nFirst, int nCount, Set options,
			DateRange uploadingDate, DateRange responseDate, Integer partnerId,
			Integer uploadId, Integer status,Integer typeId) {
		String sql=" from ru.simplgroupp.persistence.UploadingEntity  where (1=1)";
		
		if (uploadingDate != null && uploadingDate.getFrom() != null) {
	        sql+= " and (dateUploading >= :uploadingDateFrom) ";
	    }
		if (uploadingDate != null && uploadingDate.getTo() != null) {
	        sql+= " and (dateUploading < :uploadingDateTo) ";
	    }
		if (responseDate != null && responseDate.getFrom() != null) {
	        sql+= " and (responseDate >= :responseDateFrom) ";
	    }
		if (responseDate != null && responseDate.getTo() != null) {
	        sql+= " and (responseDate < :responseDateTo) ";
	    }
		if (partnerId!=null&&partnerId>0){
			sql+=" and partnersId.id=:partnerId ";
		}
		if (uploadId!=null&&uploadId>0){
			sql+=" and uploadId=:uploadId ";
		}
		if (typeId!=null&&typeId>0){
			sql+=" and uploadType=:typeId ";
		}
		if (status!=null){
			sql+=" and status=:status ";
		}
		Query qry=emMicro.createQuery(sql);
		if (status!=null){
			qry.setParameter("status", status);
		}
		if (partnerId!=null&&partnerId>0){
			qry.setParameter("partnerId", partnerId);
		}
		if (uploadId!=null&&uploadId>0){
			qry.setParameter("uploadId", uploadId);
		}
		if (typeId!=null&&typeId>0){
			qry.setParameter("typeId", typeId);
		}
		if (uploadingDate != null && uploadingDate.getFrom() != null) {
	        qry.setParameter("uploadingDateFrom", uploadingDate.getFrom());
	    }
		if (uploadingDate != null && uploadingDate.getTo() != null) {
			 qry.setParameter("uploadingDateTo", DateUtils.addDays(uploadingDate.getTo(),1));
	    }
		if (responseDate != null && responseDate.getFrom() != null) {
			qry.setParameter("responseDateFrom", responseDate.getFrom());
	    }
		if (responseDate != null && responseDate.getTo() != null) {
			qry.setParameter("responseDateTo", DateUtils.addDays(responseDate.getTo(),1));
	    }
		
		if (nFirst >= 0)
	         qry.setFirstResult(nFirst);
	    if (nCount > 0)
	         qry.setMaxResults(nCount);
	        
		List<UploadingEntity> lst=qry.getResultList();
		    
		if (lst.size() > 0) {
			List<Uploading> lstReq = new ArrayList<Uploading>(lst.size());
            for (UploadingEntity req : lst) {
                Uploading reqNew = new Uploading(req);
                lstReq.add(reqNew);
                if (options != null && options.size() > 0) {
                	reqNew.init(options);
                }
            }
            return lstReq;	
		} else {
			return new ArrayList<Uploading>(0);
		}
	
	}
}
