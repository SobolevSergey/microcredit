package ru.simplgroupp.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.RequestsDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.RequestStatusEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.RequestStatus;
import ru.simplgroupp.transfer.Requests;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RequestsDAOImpl implements RequestsDAO{

	@PersistenceContext(unitName = "MicroPU")
	private EntityManager emMicro;	
	
	@EJB
	CreditRequestDAO crDAO;
	
	@EJB 
	ReferenceBooksLocal refBooks;
	
	@EJB
	PeopleDAO peopleDAO;
	
	@Override
	public RequestsEntity saveRequest(RequestsEntity req) throws KassaException {
		req=emMicro.merge(req);
		emMicro.persist(req);
		return req;
	}
	
	@Override
	public Integer findMaxRequestNumber() {
		Query qry = emMicro.createNamedQuery("maxRequestNumber");
		List<Number> lp=qry.getResultList();
		if (lp.size()>0){
			
			if (lp.get(0)!=null) {
				return lp.get(0).intValue()+1;
			} else {
				return 1;
			}
		} else {
			return 1;
		}

	}
	
	@Override
    public RequestsEntity getRequest(int id) {
	    return emMicro.find(RequestsEntity.class, new Integer(id));
	}
	
	@Override
	public RequestStatusEntity getRequestStatusEntity(int id) {
	    return emMicro.find(RequestStatusEntity.class, id);
	}

	@Override
	public List<RequestStatus> getRequestStatuses() {
	    String sql = "from ru.simplgroupp.persistence.RequestStatusEntity order by name";
	    Query qry = emMicro.createQuery(sql);

	    List<RequestStatusEntity> lstRqs = qry.getResultList();
	    List<RequestStatus> lstRes = new ArrayList<RequestStatus>(lstRqs.size());
	    for (RequestStatusEntity ent : lstRqs) {
	        lstRes.add(new RequestStatus(ent));
	    }
	    return lstRes;
	}
	
	@Override
	public List<Requests> listKBRequests(int nFirst, int nCount,Integer creditRequestId,
				Integer partnerId, Integer statusId, String requestGuid,
				DateRange requestDate, DateRange responseDate,String creditRequestNumber) {
			
			String hql="from ru.simplgroupp.persistence.RequestsEntity where (1=1)";
			
			if (creditRequestId!=null&&creditRequestId>0){
				hql+=" and creditRequestId.id=:creditRequestId ";
			}
			if (partnerId!=null){
				hql+=" and partnersId.id=:partnerId ";
			}
			if (statusId!=null){
				hql+=" and requeststatus.id=:statusId ";
			}
			if (StringUtils.isNotEmpty(requestGuid)){
				hql+=" and requestguid=:requestGuid ";
			}
			if (StringUtils.isNotEmpty(creditRequestNumber)){
				hql+=" and creditRequestId.uniquenomer=:creditRequestNumber ";
			}
			if (requestDate != null && requestDate.getFrom() != null) {
		        hql+= " and (requestdate >= :requestDateFrom) ";
		    }
		    if (requestDate != null && requestDate.getTo() != null) {
		    	hql+= " and (requestdate < :requestDateTo) ";
		    }
		    if (responseDate != null && responseDate.getFrom() != null) {
		        hql+= " and (responsedate >= :responseDateFrom) ";
		    }
		    if (responseDate != null && responseDate.getTo() != null) {
		    	hql+= " and (responsedate < :responseDateTo) ";
		    }
		    hql+=" order by requestdate desc";
		    Query qry=emMicro.createQuery(hql);
		    
		    if (creditRequestId!=null&&creditRequestId>0){
				qry.setParameter("creditRequestId", creditRequestId);
			}
			if (partnerId!=null){
				qry.setParameter("partnerId", partnerId);
			}
			if (statusId!=null){
				qry.setParameter("statusId", statusId);
			}
			if (StringUtils.isNotEmpty(requestGuid)){
				qry.setParameter("requestGuid", requestGuid);
			}
			if (StringUtils.isNotEmpty(creditRequestNumber)){
				qry.setParameter("creditRequestNumber",creditRequestNumber);
			}
			if (requestDate != null && requestDate.getFrom() != null) {
				qry.setParameter("requestDateFrom", requestDate.getFrom(),TemporalType.DATE);
		    }
		    if (requestDate != null && requestDate.getTo() != null) {
		    	qry.setParameter("requestDateTo", DateUtils.addDays(requestDate.getTo(),1),TemporalType.DATE);
		    }
		    if (responseDate != null && responseDate.getFrom() != null) {
		    	qry.setParameter("responseDateFrom", responseDate.getFrom(),TemporalType.DATE);
		    }
		    if (responseDate != null && responseDate.getTo() != null) {
		    	qry.setParameter("responseDateTo", DateUtils.addDays(responseDate.getTo(),1),TemporalType.DATE);
		    }
		    
		    if (nFirst >= 0)
	            qry.setFirstResult(nFirst);
	        if (nCount > 0)
	            qry.setMaxResults(nCount);
	        
		    List<RequestsEntity> lst=qry.getResultList();
		    
		    if (lst.size() > 0) {
	            List<Requests> lstReq = new ArrayList<Requests>(lst.size());
	            for (RequestsEntity req : lst) {
	                Requests reqNew = new Requests(req);
	                lstReq.add(reqNew);
	            }
	            return lstReq;
	        } else {
	            return new ArrayList<Requests>(0);
	        }
	 }
	
	 @Override
	 public int countKBRequests(Integer creditRequestId, Integer partnerId,
				Integer statusId, String requestGuid, DateRange requestDate,
				DateRange responseDate,String creditRequestNumber) {
		
		 String hql="select count(*) from ru.simplgroupp.persistence.RequestsEntity where (1=1)";
			
			if (creditRequestId!=null&&creditRequestId>0){
				hql+=" and creditRequestId.id=:creditRequestId ";
			}
			if (partnerId!=null){
				hql+=" and partnersId.id=:partnerId ";
			}
			if (statusId!=null){
				hql+=" and requeststatus.id=:statusId ";
			}
			if (StringUtils.isNotEmpty(requestGuid)){
				hql+=" and requestguid=:requestGuid ";
			}
			if (StringUtils.isNotEmpty(creditRequestNumber)){
				hql+=" and creditRequestId.uniquenomer=:creditRequestNumber ";
			}
			if (requestDate != null && requestDate.getFrom() != null) {
		        hql+= " and (requestdate >= :requestDateFrom) ";
		    }
		    if (requestDate != null && requestDate.getTo() != null) {
		    	hql+= " and (requestdate < :requestDateTo) ";
		    }
		    if (responseDate != null && responseDate.getFrom() != null) {
		        hql+= " and (responsedate >= :responseDateFrom) ";
		    }
		    if (responseDate != null && responseDate.getTo() != null) {
		    	hql+= " and (responsedate < :responseDateTo) ";
		    }
		   
		    Query qry=emMicro.createQuery(hql);
		    
		    if (creditRequestId!=null&&creditRequestId>0){
				qry.setParameter("creditRequestId", creditRequestId);
			}
			if (partnerId!=null){
				qry.setParameter("partnerId", partnerId);
			}
			if (statusId!=null){
				qry.setParameter("statusId", statusId);
			}
			if (StringUtils.isNotEmpty(requestGuid)){
				qry.setParameter("requestGuid", requestGuid);
			}
			if (StringUtils.isNotEmpty(creditRequestNumber)){
				qry.setParameter("creditRequestNumber",creditRequestNumber);
			}
			if (requestDate != null && requestDate.getFrom() != null) {
				qry.setParameter("requestDateFrom", requestDate.getFrom(),TemporalType.DATE);
		    }
		    if (requestDate != null && requestDate.getTo() != null) {
		    	qry.setParameter("requestDateTo", DateUtils.addDays(requestDate.getTo(),1),TemporalType.DATE);
		    }
		    if (responseDate != null && responseDate.getFrom() != null) {
		    	qry.setParameter("responseDateFrom", responseDate.getFrom(),TemporalType.DATE);
		    }
		    if (responseDate != null && responseDate.getTo() != null) {
		    	qry.setParameter("responseDateTo", DateUtils.addDays(responseDate.getTo(),1),TemporalType.DATE);
		    }
				    
		    Number nres = (Number) qry.getSingleResult();

	        if (nres==null) {
	        	return 0;
	        } else {
	        	return nres.intValue();
	        }
	
	 }
	 
	@Override
	public void removeRequest(int id) {
	    Query qry = emMicro.createNamedQuery("removeRequest");
	    qry.setParameter("id", id);
	    qry.executeUpdate();
	}
	
	@Override
	public List<RequestsEntity> listPartnersRequests(Integer partnerId) {
		return JPAUtils.getResultList(emMicro, "listPartnersRequests", Utils.mapOf("partnerId", partnerId));
	}
	
	@Override
	public RequestsEntity addRequest(Integer peopleMainId,Integer creditRequestId,
			Integer partnerId, Integer statusId, Date requestDate) {
		RequestsEntity req=new RequestsEntity();
		if (peopleMainId!=null){
			req.setPeopleMainId(peopleDAO.getPeopleMainEntity(peopleMainId));
		}
		if (partnerId!=null){
			req.setPartnersId(refBooks.getPartnerEntity(partnerId));
		}
		if (creditRequestId!=null){
		    req.setCreditRequestId(crDAO.getCreditRequestEntity(creditRequestId));
		}
		req.setRequeststatus(getRequestStatusEntity(statusId));
		req.setRequestdate(requestDate);
		Integer res = findMaxRequestNumber();
		req.setRequestquid(res.toString());
		req.setRequestNumber(res);
		emMicro.persist(req);
		return req;
	}
	
	@Override
	public RequestsEntity getLastPeopleRequestByPartner(Integer peopleMainId,
			Integer partnerId) {
		return (RequestsEntity) JPAUtils.getSingleResult(emMicro, "getLastPeopleRequestByPartner", Utils.mapOf("peopleMainId", peopleMainId,
				"partnerId", partnerId,
				"status", RequestStatus.STATUS_IS_DONE_WITH_SESSION));
		
	}
}
