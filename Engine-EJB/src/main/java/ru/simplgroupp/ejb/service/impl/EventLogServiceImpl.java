package ru.simplgroupp.ejb.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import ru.simplgroupp.dao.interfaces.UsersDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.EventLog;
import ru.simplgroupp.transfer.EventType;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.util.ErrorKeys;

/**
 * работа с логами, события системы
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class EventLogServiceImpl implements EventLogService{
	
	@EJB
	UsersDAO userDAO;
	
	@EJB
	ReferenceBooksLocal refBook;
	
	@Inject Logger log;
	
	@PersistenceContext(unitName = "MicroPU")
	protected EntityManager emMicro;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EventLog findLatestByCreditId(int eventCode, int creditId) {
		Query qry = emMicro.createNamedQuery("findLatestByCredit");
		qry.setParameter("creditId",creditId);
		qry.setParameter("eventCode", eventCode);
		qry.setMaxResults(1);
		List<EventLogEntity> lst=qry.getResultList();
		if (lst.size() == 0) {
			return null;
		} else {
			return new EventLog(lst.get(0));
		}				
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EventLog findLatestByCreditRequestId(int eventCode, int crequestId) {
		EventLogEntity eventLog= (EventLogEntity) JPAUtils.getSingleResult(emMicro, "findLatestByCreditRequest", Utils.mapOf("crequestId",crequestId,
				"eventCode", eventCode));
		if (eventLog==null) {
			return null;
		} else {
			return new EventLog(eventLog);
		}				
	}	
	 
	@Override
	public List<EventLog> listLogs( int nFirst, int nCount,Set options,Integer userId,Integer roleId,Integer usertypeId,
			  String surname, String crequestNumber, Integer crequestId,
			  Integer eventCode,String description, String os,String browser, 
			  String geoPlace,DateRange dateEvent, Integer creditId, String ipaddress) {
		String hql = "select c from ru.simplgroupp.persistence.EventLogEntity as c where (1=1)";
		if (crequestId!=null)
			  hql=hql+" and (c.creditRequestId.id = :crequestId)";
		if (creditId!=null) {
			  hql=hql+" and (c.creditId.id = :creditId)";
		}
		if (usertypeId!=null)
			  hql=hql+" and (c.userId.usertype.id = :usertypeId)";
		if (userId!=null)
			  hql=hql+" and (c.userId.id = :userId)";
		if (StringUtils.isNotEmpty(crequestNumber))
			  hql=hql+" and (c.creditRequestId.uniquenomer = :crequestNumber)";
		if (StringUtils.isNotEmpty(description))
			  hql=hql+" and (upper(c.description) like :description)";
		if (StringUtils.isNotEmpty(os))
			  hql=hql+" and (upper(c.os) like :os)";
		if (StringUtils.isNotEmpty(browser))
			  hql=hql+" and (upper(c.browser) like :browser)";
		if (StringUtils.isNotEmpty(geoPlace))
			  hql=hql+" and (upper(c.geoPlace) like :geoPlace)";
		if (eventCode!=null) {
			if (eventCode>0) {
			   hql=hql+" and (c.eventcodeid.id = :eventCode)";
			}
		}
		if (dateEvent!=null&&dateEvent.getFrom()!=null){
			 hql = hql + " and (c.eventtime >= :datefrom)";
		}
		if (dateEvent!=null&&dateEvent.getTo()!=null){
			 hql = hql + " and (c.eventtime < :dateto)";
		}
		if (StringUtils.isNotEmpty(surname)) {
            hql = hql + " and ( (select count(*) from c.userId.peopleMainId.peoplepersonal as p where (upper(p.surname) like :surname) ) > 0 )";
        }
		if (roleId!=null) {
			if (roleId>0) {
              hql = hql + " and ( (select count(*) from c.userId.roles as r where (r.id=:roleId) ) > 0 )";
			}
        }
		if (StringUtils.isNotEmpty(ipaddress)) {
			hql = hql + " and (c.ipaddress = :ipaddress) ";
		}
		
		hql+="order by c.eventtime desc";
		Query qry = emMicro.createQuery(hql);
		if (crequestId!=null) {
			qry.setParameter("crequestId",crequestId);
		}
		if (creditId!=null) {
			qry.setParameter("creditId",creditId);
		}
		if (userId!=null) {
			qry.setParameter("userId",userId);
		}
		if (usertypeId!=null) {
			qry.setParameter("usertypeId",usertypeId);
		}
		if (StringUtils.isNotEmpty(crequestNumber)) {
			qry.setParameter("crequestNumber",crequestNumber);
		}
		if (StringUtils.isNotEmpty(description)) {
			qry.setParameter("description", "%"+description.toUpperCase()+"%");
		}
		if (StringUtils.isNotEmpty(os)) {
			qry.setParameter("os", "%"+os.toUpperCase()+"%");
		}
		if (StringUtils.isNotEmpty(browser)) {
			qry.setParameter("browser", "%"+browser.toUpperCase()+"%");
		}
		if (StringUtils.isNotEmpty(geoPlace)) {
			qry.setParameter("geoPlace", "%"+geoPlace.toUpperCase()+"%");
		}
		if ((dateEvent != null) && (dateEvent.getFrom() != null)) {
            qry.setParameter("datefrom", dateEvent.getFrom(),TemporalType.DATE);
        }
        if ((dateEvent != null) && (dateEvent.getTo() != null)) {
            qry.setParameter("dateto", DateUtils.addDays(dateEvent.getTo(),1),TemporalType.DATE);
        }
        if (StringUtils.isNotEmpty(surname)) {
        	qry.setParameter("surname", "%"+surname.toUpperCase()+"%");
        }
        if (roleId!=null) {
        	if (roleId>0) {
        	  qry.setParameter("roleId", roleId);
        	}
        }
        if (eventCode!=null){
        	if (eventCode>0) {
        	  qry.setParameter("eventCode", eventCode);
        	}
        }
        if (StringUtils.isNotEmpty(ipaddress)) {
        	qry.setParameter("ipaddress", ipaddress);
        }
        
        if (nFirst >= 0)
			qry.setFirstResult(nFirst);
		if (nCount > 0)
			qry.setMaxResults(nCount);
		
        List<EventLogEntity> lst=qry.getResultList();
              
        if (lst.size() > 0) {
            List<EventLog> lst1 = new ArrayList<EventLog>(lst.size());
            for (EventLogEntity ev : lst) {
                EventLog evt = new EventLog(ev);
                lst1.add(evt);
                if (options != null && options.size() > 0) {
                    evt.init(options);
                }
            }
            return lst1;
        } else
            return new ArrayList<EventLog>(0);
       
	}

	@Override
	public int countLogs(Integer userId,Integer roleId,Integer usertypeId,
			  String surname, String crequestNumber, Integer crequestId,
			  Integer eventCode,String description, String os,String browser, 
			  String geoPlace,DateRange dateEvent, Integer creditId, String ipaddress) {
		
		List<EventLog> lst=listLogs(0,0,null,userId,roleId,usertypeId,surname,crequestNumber,crequestId,
				eventCode,description,os,browser,geoPlace,dateEvent,creditId,ipaddress);
		return lst.size();
	
	}

	
	@Override
	public List<EventLog> listLogsTime( int nFirst, int nCount,Set options,Integer userId,Integer roleId,Integer usertypeId,
			  String surname, String crequestNumber, Integer crequestId,
			  Integer eventCode,String description, String os,String browser, 
			  String geoPlace,DateRange dateEvent, Integer creditId, String ipaddress) {
		String hql = "select c from ru.simplgroupp.persistence.EventLogEntity as c where (1=1)";
		if (crequestId!=null)
			  hql=hql+" and (c.creditRequestId.id = :crequestId)";
		if (creditId!=null) {
			  hql=hql+" and (c.creditId.id = :creditId)";
		}
		if (usertypeId!=null)
			  hql=hql+" and (c.userId.usertype.id = :usertypeId)";
		if (userId!=null)
			  hql=hql+" and (c.userId.id = :userId)";
		if (StringUtils.isNotEmpty(crequestNumber))
			  hql=hql+" and (c.creditRequestId.uniquenomer = :crequestNumber)";
		if (StringUtils.isNotEmpty(description))
			  hql=hql+" and (upper(c.description) like :description)";
		if (StringUtils.isNotEmpty(os))
			  hql=hql+" and (upper(c.os) like :os)";
		if (StringUtils.isNotEmpty(browser))
			  hql=hql+" and (upper(c.browser) like :browser)";
		if (StringUtils.isNotEmpty(geoPlace))
			  hql=hql+" and (upper(c.geoPlace) like :geoPlace)";
		if (eventCode!=null) {
			if (eventCode>0) {
			   hql=hql+" and (c.eventcodeid.id = :eventCode)";
			}
		}
		if (dateEvent!=null&&dateEvent.getFrom()!=null){
			 hql = hql + " and (c.eventtime >= :datefrom)";
		}
		if (dateEvent!=null&&dateEvent.getTo()!=null){
			 hql = hql + " and (c.eventtime <= :dateto)";
		}
		if (StringUtils.isNotEmpty(surname)) {
            hql = hql + " and ( (select count(*) from c.userId.peopleMainId.peoplepersonal as p where (upper(p.surname) like :surname) ) > 0 )";
        }
		if (roleId!=null) {
			if (roleId>0) {
              hql = hql + " and ( (select count(*) from c.userId.roles as r where (r.id=:roleId) ) > 0 )";
			}
        }
		if (ipaddress != null) {
			hql = hql + " and (c.ipaddress = :ipaddress)";
		}
		
		Query qry = emMicro.createQuery(hql);
		if (crequestId!=null)
			qry.setParameter("crequestId",crequestId);
		if (creditId!=null) {
			qry.setParameter("creditId",creditId);
		}
		if (userId!=null)
			qry.setParameter("userId",userId);
		if (usertypeId!=null)
			qry.setParameter("usertypeId",usertypeId);
		if (StringUtils.isNotEmpty(crequestNumber))
			qry.setParameter("crequestNumber",crequestNumber);
		if (StringUtils.isNotEmpty(description))
			qry.setParameter("description", "%"+description.toUpperCase()+"%");
		if (StringUtils.isNotEmpty(os))
			qry.setParameter("os", "%"+os.toUpperCase()+"%");
		if (StringUtils.isNotEmpty(browser))
			qry.setParameter("browser", "%"+browser.toUpperCase()+"%");
		if (StringUtils.isNotEmpty(geoPlace))
			qry.setParameter("geoPlace", "%"+geoPlace.toUpperCase()+"%");
		if ((dateEvent != null) && (dateEvent.getFrom() != null)) {
            qry.setParameter("datefrom", dateEvent.getFrom());
        }
        if ((dateEvent != null) && (dateEvent.getTo() != null)) {
            qry.setParameter("dateto", dateEvent.getTo());
        }
        if (StringUtils.isNotEmpty(surname)) {
        	qry.setParameter("surname", "%"+surname.toUpperCase()+"%");
        }
        if (roleId!=null) {
        	if (roleId>0) {
        	  qry.setParameter("roleId", roleId);
        	}
        }
        if (eventCode!=null){
        	if (eventCode>0) {
        	  qry.setParameter("eventCode", eventCode);
        	}
        }
        if (ipaddress != null) {
        	qry.setParameter("ipaddress", ipaddress);
        }
        
        if (nFirst >= 0)
			qry.setFirstResult(nFirst);
		if (nCount > 0)
			qry.setMaxResults(nCount);
		
        List<EventLogEntity> lst=qry.getResultList();
              
        if (lst.size() > 0) {
            List<EventLog> lst1 = new ArrayList<EventLog>(lst.size());
            for (EventLogEntity ev : lst) {
                EventLog evt = new EventLog(ev);
                lst1.add(evt);
                if (options != null && options.size() > 0) {
                    evt.init(options);
                }
            }
            return lst1;
        } else
            return new ArrayList<EventLog>(0);
       
	}
	
	@Override
	public int countLogsTime(Integer userId,Integer roleId,Integer usertypeId,
			  String surname, String crequestNumber, Integer crequestId,
			  Integer eventCode,String description, String os,String browser, 
			  String geoPlace,DateRange dateEvent, Integer creditId, String ipaddress) {
		
		List<EventLog> lst=listLogsTime(0,0,null,userId,roleId,usertypeId,surname,crequestNumber,crequestId,
				eventCode,description,os,browser,geoPlace,dateEvent,creditId,ipaddress);
		return lst.size();
	}
	
	 @Override
	 @TransactionAttribute(TransactionAttributeType.REQUIRED)
	 public void saveLog(String ipAddress, int evTypeId,
	                     int eventTypeCode, String description,
	                     CreditRequestEntity cre, PeopleMainEntity pplmain, Integer userId, CreditEntity credit,
	                     String browser,String userAgent,String os,String geoPlace,Date date) throws KassaException {
		 EventTypeEntity evType = getEventTypeEntity(evTypeId);
		 EventCodeEntity evCode = getEventCodeEntity(eventTypeCode);
		 UsersEntity usr = null;
		 if (userId != null) {
			 usr= userDAO.getUsersEntity(userId);
		 }
		 saveLog(ipAddress, evType, evCode, description, cre, pplmain, usr, credit, browser, userAgent, os, geoPlace, date);
	 }
	 
	 @Override
	 @TransactionAttribute(TransactionAttributeType.REQUIRED)
	 public void saveLog(int evTypeId,
	                     int eventTypeCode, String description,
	                     Integer userId, Date date,
	                     CreditRequestEntity cre, PeopleMainEntity pplmain, CreditEntity credit) throws KassaException {
		 saveLog(null, evTypeId, eventTypeCode, description, cre, pplmain, userId, credit, null, null, null, null, date);		 
	 }	 
	
	 @Override
	 @TransactionAttribute(TransactionAttributeType.REQUIRED)
	 public void saveLog(String ipAddress, EventTypeEntity evType,
	                     EventCodeEntity evCode, String description,
	                     CreditRequestEntity cre, PeopleMainEntity pplmain, UsersEntity usr, CreditEntity credit,
	                     String browser,String userAgent,String os,String geoPlace,Date date) throws KassaException {

		 saveLog(ipAddress, evType, evCode, description,cre, pplmain, usr, credit,
                 browser,userAgent,os,geoPlace,date, Partner.SYSTEM);

	 }
	 
	 @Override
	 @TransactionAttribute(TransactionAttributeType.REQUIRED)
	 public void saveLog(String ipAddress, EventTypeEntity evType,
	                     EventCodeEntity evCode, String description,
	                     CreditRequestEntity cre, PeopleMainEntity pplmain, UsersEntity usr, CreditEntity credit,
	                     String browser,String userAgent,String os,String geoPlace,Date date,
	                     Integer partnerId) throws KassaException {

	        EventLogEntity log = new EventLogEntity();
	        log.setEventtypeid(evType);
	        log.setEventcodeid(evCode);
	        log.setEventtime(date);
	        log.setIpaddress(ipAddress);
	        log.setDescription(description);
	        log.setBrowser(browser);
	        log.setGeoPlace(geoPlace);
	        log.setUserAgent(userAgent);
	        log.setOs(os);
	        if (cre != null) {
	            log.setCreditRequestId(cre);
	        }
	        if (credit != null) {
	            log.setCreditId(credit);
	        }
	        if (pplmain != null) {
	            log.setPeopleMainId(pplmain);
	        }
	        if (usr != null) {
	            log.setUserId(usr);
	        }
	        if (partnerId!=null){
	        	log.setPartnersId(refBook.getPartnerEntity(partnerId));
	        }
	        emMicro.merge(log);

	 }
	 
	 @Override
	 @TransactionAttribute(TransactionAttributeType.REQUIRED)
	 public void saveLog(String ipAddress, EventTypeEntity evType,
	                     EventCodeEntity evCode, String description,
	                     CreditRequestEntity cre, PeopleMainEntity pplmain, UsersEntity usr, CreditEntity credit,
	                     String browser,String userAgent,String os,String geoPlace) throws KassaException {

		 saveLog(ipAddress,evType,evCode,description,cre,pplmain,usr,credit,
         browser,userAgent,os,geoPlace,new Date());     

	 }
	 
	 @Override
	 public EventLogEntity findLog(Integer creditRequestId,Integer peopleMainId, Integer eventId,Integer creditId) {
			  String hql = "from ru.simplgroupp.persistence.EventLogEntity as c where (1=1)";
			  if (creditRequestId!=null)
				  hql=hql+" and (c.creditRequestId.id = :creditRequestId)";
			  if (peopleMainId!=null)
				  hql=hql+" and (c.peopleMainId.id = :peopleMainId)";
			  if (eventId!=null)
				  hql=hql+" and (c.eventcodeid.id = :eventId)";
			  if (creditId!=null)
				  hql=hql+" and (c.creditId.id = :creditId)";
			  
		      Query qry = emMicro.createQuery(hql);
		      if (peopleMainId!=null)
		        qry.setParameter("peopleMainId", peopleMainId);
		      if (creditRequestId!=null)
		        qry.setParameter("creditRequestId", creditRequestId);
		      if (eventId!=null)
		        qry.setParameter("eventId", eventId);
		      if (creditId!=null)
			        qry.setParameter("creditId", creditId);
		     List<EventLogEntity> lst=qry.getResultList();
		     return (EventLogEntity) Utils.firstOrNull(lst);
	 }

	@Override
	public EventLog getEventLog(Integer eventLogId, Set options) {
		EventLogEntity evLog=getEventLogEntity(eventLogId);
		if (evLog!=null){
			EventLog eventLog=new EventLog(evLog);
			eventLog.init(options);
			return eventLog;
		}
		return null;
	}

	@Override
    public List<EventType> getEventTypes() {
        String sql = "from ru.simplgroupp.persistence.EventTypeEntity";
        Query qry = emMicro.createQuery(sql);

        List<EventTypeEntity> lstEvt = qry.getResultList();
        List<EventType> lstRes = new ArrayList<EventType>(lstEvt.size());
        for (EventTypeEntity ent : lstEvt) {
            lstRes.add(new EventType(ent));
        }
        return lstRes;
    }

    @Override
    public EventType getEventType(int id) {
        EventTypeEntity ent = getEventTypeEntity(id);
        if (ent == null) {
            return null;
        } else {
            return new EventType(ent);
        }
    }

    @Override
    public List<EventCode> getEventCodes() {
        String sql = "from ru.simplgroupp.persistence.EventCodeEntity order by name";
        Query qry = emMicro.createQuery(sql);

        List<EventCodeEntity> lstEvc = qry.getResultList();
        List<EventCode> lstRes = new ArrayList<EventCode>(lstEvc.size());
        for (EventCodeEntity ent : lstEvc) {
            lstRes.add(new EventCode(ent));
        }
        return lstRes;
    }

    @Override
    public EventCode getEventCode(int id) {
        EventCodeEntity ent = getEventCodeEntity(id);
        if (ent == null) {
            return null;
        } else {
            return new EventCode(ent);
        }
    }
    
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addEventCode(String name) {
		EventCodeEntity res=new EventCodeEntity();
		res.setName(name);
		emMicro.merge(res);
				
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveEventCode(List<EventCode> lst){
		if (lst.size()>0 && lst!=null)
			for (EventCode st:lst){
				EventCodeEntity rre=getEventCodeEntity(st.getId());
				rre.setName(st.getName());
				rre.setId(st.getId());
				emMicro.merge(rre);
			}
	}

	@Override
	public EventCodeEntity getEventCodeEntity(int id) {
		return emMicro.find(EventCodeEntity.class,id);
	}

	@Override
	public EventTypeEntity getEventTypeEntity(int id) {
		return emMicro.find(EventTypeEntity.class, id);
	}

	@Override
	public EventLogEntity getEventLogEntity(Integer eventLogId) {
		return emMicro.find(EventLogEntity.class, eventLogId);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveLogEx(int evTypeId, int eventTypeCode, String description,
			Integer userId, Date date, CreditRequestEntity cre,
			PeopleMainEntity pplmain, CreditEntity credit)
			throws ActionException {
		try {
			saveLog(evTypeId,eventTypeCode,	description,userId,date,cre,pplmain,credit);
		} catch (KassaException e) {
			log.severe("Не удалось сохранить лог "+e);
			throw new ActionException(ErrorKeys.CANT_WRITE_LOG,"Не удалось записать лог",Type.TECH, ResultType.NON_FATAL,e);
		} 
		
	}

}
