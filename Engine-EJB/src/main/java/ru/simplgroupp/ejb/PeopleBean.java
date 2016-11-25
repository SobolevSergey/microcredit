package ru.simplgroupp.ejb;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.simplgroupp.dao.interfaces.*;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.PeopleException;
import ru.simplgroupp.fias.ejb.IFIASService;
import ru.simplgroupp.fias.persistence.AddrObj;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.interfaces.service.BusinessEventSender;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.*;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.transfer.kzegovdata.Address;
import ru.simplgroupp.util.*;

import javax.ejb.*;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(PeopleBeanLocal.class)
public class PeopleBean implements PeopleBeanLocal {

    @PersistenceContext(unitName="MicroPU")
    protected EntityManager emMicro;

    @EJB
    EventLogService eventLog;

    @EJB
    ReferenceBooksLocal refBooks;

    @EJB
    UsersBeanLocal userBean;

    @EJB
    UsersDAO userDAO;

    @EJB
    IFIASService fiasServ;

    @EJB
    RulesBeanLocal rulesBean;

    @EJB
    KassaBeanLocal kassa;

    @EJB
    AddressBeanLocal addressBean;

    @EJB
    PeopleDAO peopleDAO;

    @EJB
    CreditDAO creditDAO;

    @EJB
    CreditRequestDAO creditRequestDAO;

    @EJB
    MailBeanLocal mailBean;

    @EJB
    BusinessEventSender eventSender;

    @EJB
    AccountDAO accountDAO;

    @EJB
    DocumentsDAO documentDAO;

    @EJB
    OfficialDocumentsDAO officialDocumentDAO;
    
    @EJB
    ProductDAO productDAO;

    @EJB
    ActionProcessorBeanLocal actionProcessor;

    @EJB
    RulesBeanLocal rulesBeanLocal;

    @EJB
    MailBeanLocal mailBeanLocal;

    @Inject Logger logger;
    
    private static HashMap<String, Object> peopleSortMapping = new HashMap<String, Object>(4);

    static {
    	  peopleSortMapping.put("id", "p.id");
    }

    @Override
    public PeopleMainEntity findPeopleMain( String inn, String snils) {
    	return peopleDAO.findPeopleMain(inn, snils);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<PeopleContactEntity> findPeopleByContact(Integer contactTypeId, String value,
    		Integer peopleMainId, Integer partnerId,Integer isActive,Integer creditRequestId) {
        String hql = "from ru.simplgroupp.persistence.PeopleContactEntity where (1=1)";
        if (contactTypeId != null) {
            hql = hql + " and (contactId.codeinteger = :code)";
        }
        if (! StringUtils.isEmpty(value)) {
            hql = hql + " and (value = :value)";
        }
        if (peopleMainId != null) {
            hql = hql + " and (peopleMainId.id = :peopleMainId)";
        }
        if (creditRequestId != null) {
            hql = hql + " and (creditRequestId.id = :creditRequestId)";
        }
        if (partnerId != null) {
            hql = hql + " and (partnersId.id = :partnerId)";
        }
        if (isActive != null) {
            hql = hql + " and (isactive = :active)";
        }
        hql+=" order by dateactual desc ";
        Query qry = emMicro.createQuery(hql);
        if (contactTypeId != null) {
            qry.setParameter("code", new Integer(contactTypeId));
        }
        if (! StringUtils.isEmpty(value)) {
            qry.setParameter("value", value);
        }

        if (peopleMainId != null) {
            qry.setParameter("peopleMainId", peopleMainId);
        }
        if (creditRequestId != null) {
            qry.setParameter("creditRequestId", creditRequestId);
        }
        if (partnerId != null) {
            qry.setParameter("partnerId", partnerId);
        }
        if (isActive != null) {
            qry.setParameter("active", isActive);
        }
        List<PeopleContactEntity> lst = qry.getResultList();
        return lst;
    }

    @Override
    public PeopleContactEntity findPeopleByContactClient(Integer contactTypeId, String value) {
        List<PeopleContactEntity> lstCon = findPeopleByContact(contactTypeId, value, null, Partner.CLIENT, ActiveStatus.ACTIVE, null);
        return (PeopleContactEntity) Utils.firstOrNull(lstCon);
    }

    @Override
    public PeopleContactEntity findPeopleByContactMan(Integer contactTypeId, Integer peopleId) {
        List<PeopleContactEntity> lstCon = findPeopleByContact(contactTypeId, null, peopleId, Partner.CLIENT, ActiveStatus.ACTIVE, null);
        return (PeopleContactEntity) Utils.firstOrNull(lstCon);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PeopleContactEntity setPeopleContactClient(PeopleMainEntity peopleMain, int contactTypeId, String value,
                                                      Boolean available) {
        PartnersEntity partner = refBooks.getPartnerEntity(Partner.CLIENT);
        return setPeopleContact(peopleMain, partner, contactTypeId, value,available,null,null);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PeopleContactEntity setPeopleContact(PeopleMainEntity peopleMain, PartnersEntity partner, int contactTypeId, String value,
    		Boolean available,Integer creditRequestId,Date date) {
       return setPeopleContact(peopleMain, partner, contactTypeId,value,
       		available,creditRequestId,date,true);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PeopleContactEntity setPeopleContact(PeopleMainEntity peopleMain, PartnersEntity partner, int contactTypeId, String value,
    		Boolean available,Integer creditRequestId,Date date,Boolean needCheck) {
        PeopleContactEntity peopleCon = null;
        List<PeopleContactEntity> lstCon = findPeopleByContact(contactTypeId, null, peopleMain.getId(),
        		partner.getId(),ActiveStatus.ACTIVE,creditRequestId);
        if (lstCon.size() > 0&&partner.getId()==Partner.CLIENT) {
            peopleCon = lstCon.get(0);

        } else {
            Reference contactType = refBooks.getContactType(contactTypeId);
            if (contactType==null){
            	contactType = refBooks.getContactType(PeopleContact.CONTACT_OTHER);
            }
            peopleCon = new PeopleContactEntity();
            peopleCon.setPeopleMainId(peopleMain);
            peopleCon.setContactId(contactType.getEntity());
            peopleCon.setPartnersId(partner);
            if (creditRequestId!=null){
                peopleCon.setCreditRequestId(creditRequestDAO.getCreditRequestEntity(creditRequestId));
            }

        }
        if (date==null){
            peopleCon.setDateactual(new Date());
        } else {
        	peopleCon.setDateactual(date);
        }
        if (contactTypeId==PeopleContact.CONTACT_EMAIL){
        	peopleCon.setValue(value.toLowerCase());
        } else {
            peopleCon.setValue(Convertor.fromMask(value));
        }
        peopleCon.setAvailable(available);
        peopleCon.setIsactive(ActiveStatus.ACTIVE);
        emMicro.persist(peopleCon);
        //если это телефон, надо его проверить по БД телефонов
        if ((contactTypeId==PeopleContact.CONTACT_CELL_PHONE||contactTypeId==PeopleContact.CONTACT_HOME_PHONE
        		||contactTypeId==PeopleContact.CONTACT_HOME_REGISTER_PHONE||contactTypeId==PeopleContact.CONTACT_DOPPHONE1
        		||contactTypeId==PeopleContact.CONTACT_WORK_PHONE||contactTypeId==PeopleContact.CONTACT_OTHER)
        		&&needCheck){

        	PeopleContactEntity peopleContact=null;
        	try {
        		peopleContact=savePhoneInfo(peopleCon);
				eventLog.saveLog(EventType.INFO,EventCode.REQUEST_OUTER_DB,  "Обращение к внешней БД телефонных номеров, номер "+peopleContact.getValue(),
					 null, new Date(),
					 creditRequestId==null?null:creditRequestDAO.getCreditRequestEntity(creditRequestId),
							 peopleDAO.getPeopleMainEntity(peopleMain.getId()), null);
				return peopleContact;
			} catch (Exception e) {
				logger.severe("Не удалось обратиться к внешней БД или записать лог, ошибка "+e);
			}

        }
        return peopleCon;
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<PeoplePersonalEntity> findPeoplePersonal(PeopleMainEntity peopleMain,
    		CreditRequestEntity creditRequest, Integer partnerId, Integer active) {
        String hql = "from ru.simplgroupp.persistence.PeoplePersonalEntity where (1=1)";
        if (peopleMain != null) {
            hql = hql + " and (peopleMainId.id = :peopleMainId)";
        }
        if (creditRequest != null) {
            hql = hql + " and (creditRequestId.id = :creditRequest)";
        }
        if (partnerId != null) {
            hql = hql + " and (partnersId.id = :partner)";
        }
        if (active != null) {
            hql = hql + " and (isactive = :active)";
        }
        hql+=" order by databeg desc ";
        Query qry = emMicro.createQuery(hql);
        if (peopleMain != null) {
            qry.setParameter("peopleMainId", peopleMain.getId());
        }
        if (creditRequest != null) {
            qry.setParameter("creditRequest", creditRequest.getId());
        }
        if (partnerId != null) {
            qry.setParameter("partner", partnerId);
        }
        if (active != null) {
            qry.setParameter("active", active);
        }
        List<PeoplePersonalEntity> lst = qry.getResultList();
        return lst;
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public AddressEntity findAddress(PeopleMainEntity peopleMain,Integer partnerId,
			CreditRequestEntity creditRequest, Integer addrType,Integer active) {

		String hql = "from ru.simplgroupp.persistence.AddressEntity where (addrtype.codeinteger=:addrtype)";
	        if (peopleMain != null) {
	            hql = hql + " and (peopleMainId.id = :peopleMainId)";
	        }
	        if (partnerId != null) {
	        	 hql = hql + " and (partnersId.id = :partner)";
	        }
	        if (creditRequest != null) {
	        	hql = hql + " and (creditRequestId.id = :creditRequest)";
	        }
	        if (active != null) {
	            hql = hql + " and (isactive = :active)";
	        }
	        hql+=" order by databeg desc ";
	        Query qry = emMicro.createQuery(hql);
	        qry.setParameter("addrtype", new Integer(addrType));
	        if (peopleMain != null) {
	            qry.setParameter("peopleMainId", peopleMain.getId());
	        }
	        if (partnerId != null) {
	        	qry.setParameter("partner", partnerId);
	        }
	        if (creditRequest != null) {
	            qry.setParameter("creditRequest", creditRequest.getId());
	        }
	        if (active != null) {
	            qry.setParameter("active", active);
	        }
	        List<AddressEntity> lst=qry.getResultList();
		    return (AddressEntity) Utils.firstOrNull(lst);
	}

	@Override
	public List<AddressEntity> findAddresses(PeopleMainEntity peopleMain,Integer partnerId,
			CreditRequestEntity creditRequest, Integer addrType,Integer active) {

		String hql = "from ru.simplgroupp.persistence.AddressEntity where (1=1)";
		    if (addrType!=null){
		    	hql+=" and (addrtype.codeinteger=:addrtype) ";
		    }
	        if (peopleMain != null) {
	            hql = hql + " and (peopleMainId.id = :peopleMainId)";
	        }
	        if (partnerId != null) {
	        	 hql = hql + " and (partnersId.id = :partner)";
	        }
	        if (creditRequest != null) {
	        	hql = hql + " and (creditRequestId.id = :creditRequest)";
	        }
	        if (active != null) {
	            hql = hql + " and (isactive = :active)";
	        }
	        hql+=" order by databeg desc ";
	        Query qry = emMicro.createQuery(hql);
	        if (addrType!=null){
	            qry.setParameter("addrtype", addrType);
	        }
	        if (peopleMain != null) {
	            qry.setParameter("peopleMainId", peopleMain.getId());
	        }
	        if (partnerId != null) {
	        	qry.setParameter("partner", partnerId);
	        }
	        if (creditRequest != null) {
	            qry.setParameter("creditRequest", creditRequest.getId());
	        }
	        if (active != null) {
	            qry.setParameter("active", active);
	        }
	        return qry.getResultList();
		    
	}

	@Override
	public DocumentEntity findDocument(Integer docTypeId,String serial,String number) {

		String hql = "from ru.simplgroupp.persistence.DocumentEntity where (documenttypeId.codeinteger=:docTypeId) and (partnersId.id = :partner) and (isactive=:isactive) ";
        if (StringUtils.isNotBlank(serial)) {
            hql = hql + " and (series = :serial)";
        }
        if (StringUtils.isNotBlank(number)) {
            hql = hql + " and (number = :number)";
        }

        Query qry = emMicro.createQuery(hql);
        qry.setParameter("docTypeId", docTypeId);
        if (StringUtils.isNotBlank(serial)) {
            qry.setParameter("serial", serial);
        }
        if (StringUtils.isNotBlank(number)) {
            qry.setParameter("number", number);
        }
        qry.setParameter("partner", Partner.CLIENT);
        qry.setParameter("isactive", ActiveStatus.ACTIVE);
        List<DocumentEntity> lst=qry.getResultList();

        return (DocumentEntity) Utils.firstOrNull(lst);
	}

    @Override
    public DocumentOtherEntity findDocumentOther(PeopleMainEntity peopleMain,
                                                 CreditRequestEntity creditRequest, Integer partnerId,
                                                 Integer active, Integer docType) {
        String hql = "from ru.simplgroupp.persistence.DocumentOtherEntity where (documenttypeId.refHeaderId.id=:doctype)";
        if (peopleMain != null) {
            hql = hql + " and (peopleMainId.id = :peopleMainId)";
        }
        if (creditRequest != null) {
            hql = hql + " and (creditRequestId.id = :creditRequest)";
        }
        if (partnerId != null) {
            hql = hql + " and (partnersId.id = :partner)";
        }
        if (active != null) {
            hql = hql + " and (isactive = :active)";
        }
        Query qry = emMicro.createQuery(hql);
        qry.setParameter("doctype", new Integer(docType));
        if (peopleMain != null) {
            qry.setParameter("peopleMainId", peopleMain.getId());
        }
        if (creditRequest != null) {
            qry.setParameter("creditRequest", creditRequest.getId());
        }
        if (partnerId != null) {
            qry.setParameter("partner", partnerId);
        }
        if (active != null) {
            qry.setParameter("active", active);
        }
        List<DocumentOtherEntity> lst = qry.getResultList();
        return (DocumentOtherEntity) Utils.firstOrNull(lst);
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<PeopleMiscEntity> findPeopleMisc(PeopleMainEntity peopleMain,
			CreditRequestEntity creditRequest, Integer partnerId, Integer active) {
		 String hql = "from ru.simplgroupp.persistence.PeopleMiscEntity where (1=1)";
	        if (peopleMain != null) {
	            hql = hql + " and (peopleMainId.id = :peopleMainId)";
	        }
	        if (creditRequest != null) {
	            hql = hql + " and (creditRequestId.id = :creditRequest)";
	        }
	        if (partnerId != null) {
	            hql = hql + " and (partnersId.id = :partner)";
	        }
	        if (active != null) {
	            hql = hql + " and (isactive = :active)";
	        }
	        Query qry = emMicro.createQuery(hql);
	        if (peopleMain != null) {
	            qry.setParameter("peopleMainId", peopleMain.getId());
	        }
	        if (creditRequest != null) {
	            qry.setParameter("creditRequest", creditRequest.getId());
	        }
	        if (partnerId != null) {
	            qry.setParameter("partner", partnerId);
	        }
	        if (active != null) {
	            qry.setParameter("active", active);
	        }
	    List<PeopleMiscEntity> lst=qry.getResultList();

		return lst;
	}

    @Override
    public List<PeopleOtherEntity> findPeopleOther(PeopleMainEntity peopleMain,
                                                  CreditRequestEntity creditRequest, Integer partnerId, Integer active){
        String hql = "from ru.simplgroupp.persistence.PeopleOtherEntity where (1=1)";
        if (peopleMain != null) {
            hql = hql + " and (peopleMainId.id = :peopleMainId)";
        }
        if (creditRequest != null) {
            hql = hql + " and (creditRequestId.id = :creditRequest)";
        }
        if (partnerId != null) {
            hql = hql + " and (partnersId.id = :partner)";
        }
        if (active != null) {
            hql = hql + " and (isactive = :active)";
        }
        Query qry = emMicro.createQuery(hql);
        if (peopleMain != null) {
            qry.setParameter("peopleMainId", peopleMain.getId());
        }
        if (creditRequest != null) {
            qry.setParameter("creditRequest", creditRequest.getId());
        }
        if (partnerId != null) {
            qry.setParameter("partner", partnerId);
        }
        if (active != null) {
            qry.setParameter("active", active);
        }
        List<PeopleOtherEntity> lst=qry.getResultList();

        return lst;
    }

	@Override
	public List<AccountEntity> findAccounts(PeopleMainEntity peopleMain, Integer accountTypeId, Integer isActive) {
        String hql = "from ru.simplgroupp.persistence.AccountEntity where (1=1)";
        if (peopleMain != null) {
            hql = hql + " and (peopleMainId.id = :peopleMainId)";
        }
        if (accountTypeId != null) {
            hql = hql + " and (accountTypeId.codeinteger = :accountTypeId)";
        }
        if (isActive != null) {
            hql = hql + " and (isactive = :isActive)";
        }
        Query qry = emMicro.createQuery(hql);
        if (peopleMain != null) {
            qry.setParameter("peopleMainId", peopleMain.getId());
        }
        if (accountTypeId != null) {
            qry.setParameter("accountTypeId", accountTypeId);
        }
        if (isActive != null) {
            qry.setParameter("isActive", isActive);
        }
        List<AccountEntity> lst = qry.getResultList();
        return lst;
	}

	@Override
	public AccountEntity findAccountActive(PeopleMainEntity peopleMain, Integer accountTypeId) {
	    List<AccountEntity> lst = findAccounts(peopleMain, accountTypeId, ActiveStatus.ACTIVE);
	    return (AccountEntity) Utils.firstOrNull(lst);
	}

	@Override
	public EmploymentEntity findEmployment(PeopleMainEntity peopleMain,CreditRequestEntity creditRequest,
			PartnersEntity partner,	Integer current) {
		String hql = "from ru.simplgroupp.persistence.EmploymentEntity where (1=1)";
        if (peopleMain != null) {
            hql = hql + " and (peopleMainId.id = :peopleMainId)";
        }
        if (creditRequest != null) {
            hql = hql + " and (creditRequestId.id = :creditRequest)";
        }
        if (partner != null) {
            hql = hql + " and (partnersId.id = :partner)";
        }
        if (current != null) {
            hql = hql + " and (current = :current)";
        }
        hql+=" order by databeg desc ";
        Query qry = emMicro.createQuery(hql);
        if (peopleMain != null) {
            qry.setParameter("peopleMainId", peopleMain.getId());
        }
        if (creditRequest != null) {
            qry.setParameter("creditRequest", creditRequest.getId());
        }
        if (partner != null) {
            qry.setParameter("partner", partner.getId());
        }
        if (current != null) {
            qry.setParameter("current", current);
        }
        List<EmploymentEntity> lst = qry.getResultList();
        return (EmploymentEntity) Utils.firstOrNull(lst);
	}


	@Override
	public PeoplePersonalEntity findPeoplePersonalActive(PeopleMainEntity peopleMain) {

	        List<PeoplePersonalEntity> lst = findPeoplePersonal(peopleMain, null, Partner.CLIENT, ActiveStatus.ACTIVE);
	        return (PeoplePersonalEntity) Utils.firstOrNull(lst);
	}

	@Override
	public PeopleMiscEntity findPeopleMiscActive(PeopleMainEntity peopleMain) {

	        List<PeopleMiscEntity> lst=findPeopleMisc(peopleMain, null, Partner.CLIENT, ActiveStatus.ACTIVE);
	        return (PeopleMiscEntity) Utils.firstOrNull(lst);

	}


    @Override
    public PeopleOtherEntity findPeopleOtherActive(PeopleMainEntity peopleMain){
        List<PeopleOtherEntity> lst=findPeopleOther(peopleMain, null, Partner.CLIENT, ActiveStatus.ACTIVE);
        return (PeopleOtherEntity) Utils.firstOrNull(lst);
    }

	@Override
	public String findContactClient(Integer contactTypeId, Integer peopleMainId) {
	    List<PeopleContactEntity> lstCon = findPeopleByContact(contactTypeId, null, peopleMainId, Partner.CLIENT, ActiveStatus.ACTIVE, null);
        if (lstCon.size() > 0) {
            return lstCon.get(0).getValue();
        } else {
            return null;
        }
	}


	@Override
	public PeoplePersonalEntity findPeopleByPersonalData(String surname,
			String name, String midname, Date birthdate) {
		  String hql = "from ru.simplgroupp.persistence.PeoplePersonalEntity where (partnersId.id = :partner) ";

		  if (StringUtils.isNotEmpty(surname)) {
			  hql = hql + " and (upper(surname) like :surname)";
		  }
		  if (StringUtils.isNotEmpty(name)) {
			  hql = hql + " and (upper(name) like :name)";
		  }
		  if (StringUtils.isNotEmpty(midname)) {
			  hql = hql + " and (upper(midname) like :midname)";
		  }
		  if (birthdate != null) {
	          hql = hql + " and (birthdate = :birthdate)";
	      }
		  
		  Query qry = emMicro.createQuery(hql);
	      if (StringUtils.isNotEmpty(surname)) {
	          qry.setParameter("surname", "%"+surname.toUpperCase().trim()+"%");
	      }
	      if (StringUtils.isNotEmpty(name)) {
	          qry.setParameter("name", "%"+name.toUpperCase().trim()+"%");
	      }
	      if (StringUtils.isNotEmpty(midname)) {
	          qry.setParameter("midname", "%"+midname.toUpperCase().trim()+"%");
	      }
	      if (birthdate!=null) {
	          qry.setParameter("birthdate", birthdate);
	      }
	      qry.setParameter("partner", Partner.CLIENT);

          List<PeoplePersonalEntity> lst = qry.getResultList();
	      return (PeoplePersonalEntity) Utils.firstOrNull(lst);
	}

	@Override
	public BlacklistEntity findInBlackList(PeopleMainEntity peopleMain, Date dt,
			Integer reasonId) throws PeopleException {

		String hql = "from ru.simplgroupp.persistence.BlacklistEntity where (1=1)";
        if (peopleMain != null) {
            hql = hql + " and (peopleMainId.id = :peopleMainId)";
        }
        if (reasonId != null) {
        	hql = hql + " and (reasonId.codeinteger = :reasonId)";
        }
        if (dt!=null) {
        	hql = hql + " and (:dt between databeg and dataend)";
        }
        Query qry = emMicro.createQuery(hql);
        if (peopleMain != null) {
            qry.setParameter("peopleMainId", peopleMain.getId());
        }
        if (dt!=null) {
        	qry.setParameter("dt", dt);
        }
        if (reasonId != null) {
        	qry.setParameter("reasonId", reasonId);
        }
       List<BlacklistEntity> lst=qry.getResultList();
       return (BlacklistEntity) Utils.firstOrNull(lst);

	}

	@Override
	public SpouseEntity findSpouseActive(PeopleMainEntity peopleMain) {
		String hql = " from ru.simplgroupp.persistence.SpouseEntity where  (isactive = :active)";
        if (peopleMain != null) {
            hql = hql + " and (peopleMainId.id = :peopleMainId)";
        }
        Query qry = emMicro.createQuery(hql);
        if (peopleMain != null) {
            qry.setParameter("peopleMainId", peopleMain.getId());
        }

       qry.setParameter("active", ActiveStatus.ACTIVE);

       List<SpouseEntity> lst=qry.getResultList();
       return (SpouseEntity) Utils.firstOrNull(lst);

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addSpouse(PeopleMainEntity peopleMain, String surname,
			String name, String midname, Date birthdate, String mobilephone,
			Integer spouseId,Date databeg,Integer typeworkId) {

		//ищем, есть ли партнер у человека
		SpouseEntity sp=findSpouseActive(peopleMain);
		PeopleMainEntity ms=null;
		PeoplePersonalEntity ppl=null;

		//если партнера нет, добавляем нового человека
		if (sp==null) {
			//добавляем записи в таблицы с данными человека
			ms=new PeopleMainEntity();
			emMicro.persist(ms);

			ppl=new PeoplePersonalEntity();
			ppl.setPeopleMainId(ms);
			ppl.setDatabeg(new Date());
			ppl.setPartnersId(refBooks.getPartnerEntity(Partner.CLIENT));

			sp=new SpouseEntity();
			if (databeg!=null){
			    sp.setDatabeg(databeg);
			} else {
				sp.setDatabeg(new Date());
			}
			sp.setPeopleMainId(peopleMain);

		}
		else {
            sp.setDatabeg(databeg);
			ms=sp.getPeopleMainSpouseId();
			ppl=findPeoplePersonalActive(ms);
		}


		ppl.setSurname(surname);
		ppl.setName(name);
		ppl.setMidname(midname);
		ppl.setBirthdate(birthdate);
		ppl.setIsactive(ActiveStatus.ACTIVE);
		ppl=emMicro.merge(ppl);
		emMicro.persist(ppl);

		sp.setPeopleMainSpouseId(ms);
		sp.setIsactive(ActiveStatus.ACTIVE);
		sp.setSpouseId(refBooks.getSpouseType(spouseId).getEntity());
		if (typeworkId!=null) {
			sp.setTypeworkId(refBooks.getEmployType(typeworkId).getEntity());
		}
		emMicro.merge(sp);

		if (StringUtils.isNotEmpty(mobilephone)){
	      setPeopleContactClient(ms, PeopleContact.CONTACT_CELL_PHONE, mobilephone,false);
		}

	}

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changePersonalData(PeoplePersonal pper, Integer peopleMainId, int partnerId, Date dateChange) {

	    PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleMainId);

        PeoplePersonalEntity pplper = findPeoplePersonalActive(pMain);

        //есть старая запись
        if (pplper!=null){
          //проверяем ПД
          if (pper.equalsContent(pplper)) {
        	  return;
          }

          //поставим в предыдущую запись недействительность
          pplper.setIsactive(ActiveStatus.ARCHIVE);
          pplper.setDataend(new Date( dateChange.getTime() - 1 ));
          emMicro.merge(pplper);
        }

        //запишем новые данные
        newPeoplePersonal(null, peopleMainId,null, partnerId, pper.getSurname(),pper.getName(),
        		pper.getMidname(), pper.getMaidenname(),pper.getBirthDate(), pper.getBirthPlace(),
    			refBooks.referenceCodeIntegerOrNull(pper.getGender()),dateChange, ActiveStatus.ACTIVE);


	}

    @Override
    @TransactionAttribute
    public PeoplePersonalEntity changePersonalData(Integer peopleId, String lastName, String firstName,
                                                   String middleName, Date birthDate, String birthPlace,
                                                   Integer genderCode) {
        PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleId);

        PeoplePersonalEntity peoplePersonal = findPeoplePersonalActive(pMain);

        //есть старая запись
        if (peoplePersonal != null) {
            //проверяем ПД
            if (Utils.equalsNull(peoplePersonal.getSurname(), lastName) &&
                    Utils.equalsNull(peoplePersonal.getName(), firstName) &&
                    Utils.equalsNull(peoplePersonal.getMidname(), middleName) &&
                    Utils.equalsNull(peoplePersonal.getBirthdate(), birthDate) &&
                    Utils.equalsNull(peoplePersonal.getBirthplace(), birthPlace) &&
                    Utils.equalsNull(
                            peoplePersonal.getGender() != null ? peoplePersonal.getGender().getCodeinteger() : null,
                            genderCode)) {
                return peoplePersonal;
            }

            //поставим в предыдущую запись недействительность
            peoplePersonal.setIsactive(ActiveStatus.ARCHIVE);
            peoplePersonal.setDataend(new Date());
            emMicro.merge(peoplePersonal);
        }

        //запишем новые данные
        return newPeoplePersonal(null, peopleId, null, Partner.CLIENT, lastName, firstName, middleName, null, birthDate,
                birthPlace, genderCode, new Date(), ActiveStatus.ACTIVE);
    }

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeMiscData(PeopleMisc pmisc, Integer peopleId,	int partnerId, Date dateChange) {
		PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleId);

		//проверяем дополнительные данные
        PeopleMiscEntity pplmisc = findPeopleMiscActive(pMain);
        PeopleMiscEntity pplmiscnew =null;

        //если есть старая запись
        if (pplmisc!=null)  {

          if (pmisc.equalsContent(pplmisc)) {
        	  return;
          }

         //если были изменения на других страницах
         if (!DatesUtils.isSameDay(pmisc.getDatabeg(), dateChange)) {
            pplmisc.setIsactive(ActiveStatus.ARCHIVE);
            pplmisc.setDataend(new Date( dateChange.getTime() - 1 ));
            emMicro.merge(pplmisc);
         } else {
            pplmiscnew=pplmisc;
          }
       }

        //запишем новые данные
        newPeopleMisc(pplmiscnew, peopleId, null, partnerId,
                refBooks.referenceCodeIntegerOrNull(pmisc.getMarriage()),
                pmisc.getChildren(), pmisc.getUnderage(),
                refBooks.referenceCodeIntegerOrNull(pmisc.getRealtyDateId()), pmisc.getRealtyDate(),
                refBooks.referenceCodeIntegerOrNull(pmisc.getRegDateId()), pmisc.getRegDate(),
                refBooks.referenceCodeIntegerOrNull(pmisc.getRealty()),
                pmisc.getCar() == null ? false : pmisc.getCar(), dateChange, ActiveStatus.ACTIVE,
                pmisc.getDriverLicense() == null ? false : pmisc.getDriverLicense());

	}

    @Override
    @TransactionAttribute
    public PeopleMiscEntity changeMiscData(Integer peopleId, Integer maritalStatus, int childrenCount, int underage, boolean hasCar) {
        PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleId);

        //проверяем дополнительные данные
        PeopleMiscEntity peopleMisc = findPeopleMiscActive(pMain);

        //если есть старая запись
        if (peopleMisc != null)  {

            if (Utils.equalsNull(
                    peopleMisc.getMarriageId() == null ? null : peopleMisc.getMarriageId().getCodeinteger(), maritalStatus) &&
                    Utils.equalsNull(peopleMisc.getChildren(), childrenCount) &&
                    Utils.equalsNull(peopleMisc.getCar(), hasCar)) {
                return peopleMisc;
            }

            //если были изменения на других страницах
            peopleMisc.setIsactive(ActiveStatus.ARCHIVE);
            peopleMisc.setDataend(new Date());
            emMicro.merge(peopleMisc);
        }

        //запишем новые данные
        return newPeopleMisc(null, peopleId, null, Partner.CLIENT, maritalStatus, childrenCount, underage,
                peopleMisc != null ? peopleMisc.getRealtyDateId().getCodeinteger() : null,
                peopleMisc != null ? peopleMisc.getRealtyDate() : null,
                peopleMisc != null ? peopleMisc.getRegDateId().getCodeinteger() : null,
                peopleMisc != null ? peopleMisc.getRegDate() : null,
                peopleMisc != null ? peopleMisc.getRealtyId().getCodeinteger() : null,
                hasCar, new Date(), ActiveStatus.ACTIVE, false);
    }

    @Override
    public PeopleMiscEntity changeMiscAddressData(Integer peopleId, Integer realtyDateId, Date realtyDate, Integer regDateId,
    		Date regDate, Integer realtyCode) {
        PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleId);

        //проверяем дополнительные данные
        PeopleMiscEntity peopleMisc = findPeopleMiscActive(pMain);

        //если есть старая запись
        if (peopleMisc != null)  {
            if (Utils.equalsNull(
                    peopleMisc.getRegDate() == null ? null : peopleMisc.getRegDate().getTime(),
                    		regDate == null ? null : regDate.getTime()) &&
                Utils.equalsNull(
                            peopleMisc.getRealtyDate() == null ? null : peopleMisc.getRealtyDate().getTime(),
                            realtyDate == null ? null : realtyDate.getTime()) &&
                Utils.equalsNull(
                            peopleMisc.getRealtyId() == null ? null : peopleMisc.getRealtyId().getCodeinteger(), realtyCode)&&
                Utils.equalsNull(
                            peopleMisc.getRealtyDateId() == null ? null : peopleMisc.getRealtyDateId().getCodeinteger(), realtyDateId)&&
                Utils.equalsNull(
                            peopleMisc.getRegDateId() == null ? null : peopleMisc.getRegDateId().getCodeinteger(), regDateId)) 
            {
                return peopleMisc;
            }

            //если были изменения на других страницах
            peopleMisc.setIsactive(ActiveStatus.ARCHIVE);
            peopleMisc.setDataend(new Date());
            emMicro.merge(peopleMisc);
        }

        //запишем новые данные
        return newPeopleMisc(null, peopleId, null, Partner.CLIENT, peopleMisc.getMarriageId().getCodeinteger(),
                peopleMisc.getChildren(), peopleMisc.getUnderage(), realtyDateId, realtyDate, regDateId,
                regDate, realtyCode, peopleMisc.getCar(), new Date(), ActiveStatus.ACTIVE, peopleMisc.getDriverLicense());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void changeOtherData(PeopleOther pmisc, Integer peopleId, int partnerId, Date dateChange) {
        PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleId);

        //проверяем дополнительные данные
        PeopleOtherEntity pplmisc = findPeopleOtherActive(pMain);
        PeopleOtherEntity pplmiscnew = null;

        //если есть старая запись
        if (pplmisc != null) {

            if (pmisc.equalsContent(pplmisc)) {
                return;
            }

            pplmiscnew = pplmisc;
        }

        //запишем новые данные
        newPeopleOther(pplmiscnew, peopleId, null, partnerId,
                pmisc.getPublic_role(),pmisc.getPublic_relative_role(),pmisc.getPublic_relative_fio(),
                pmisc.getBenific_fio(), pmisc.getReclam_accept(), pmisc.getOther(),ActiveStatus.ACTIVE);
    }

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeDocument(Documents dct, Integer peopleId, int partnerId ) {

	    PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleId);

        //проверяем документ
        DocumentEntity dc = findPassportActive(pMain);

        //если ничего не менялось, возвращаемся
        if (dc != null) {
        	if (dc.equalsContent(dct.getEntity())) {
        		return;
        	}
            //поставим в предыдущую запись недействительность
        	dc.setIsactive(ActiveStatus.ARCHIVE);
        	emMicro.merge(dc);
        }

        //запишем новые данные
        newDocument(null, peopleId, null, partnerId, dct.getSeries(), dct.getNumber(), dct.getDocDate(),
                dct.getDocOrgCode(), dct.getDocOrg(), ActiveStatus.ACTIVE);

	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void changeDocumentOther(DocumentsOther dct, Integer peopleId, int partnerId) {
        PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleId);

        DocumentOtherEntity dc = findDocumentOtherActive(pMain);

        //если ничего не менялось, возвращаемся
        if (dc != null) {
            if (dc.equalsContent(dct.getEntity())) {
                return;
            }
            //поставим в предыдущую запись недействительность
            dc.setIsactive(ActiveStatus.ARCHIVE);
            emMicro.merge(dc);
        }

        //запишем новые данные
        newOtherDocument(null, refBooks.getReferenceEntity(dct.getDocumentType().getId()), peopleId, partnerId, dct.getNumber(), dct.getDocDate(), ActiveStatus.ACTIVE);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeDocumentOther(Integer peopleId, int partnerId) {
        PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleId);

        DocumentOtherEntity dc = findDocumentOtherActive(pMain);
        if (dc != null) {
            dc.setIsactive(ActiveStatus.ARCHIVE);
            emMicro.merge(dc);
        }
    }

    @Override
    public DocumentEntity changeDocument(Integer peopleId, String passportSerial, String passportNumber,
                                         String passportDepartmentCode, Date passportIssueDate,
                                         String passportIssueOrganization) {
        PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleId);

        //проверяем документ
        DocumentEntity document = findPassportActive(pMain);

        //если ничего не менялось, возвращаемся
        if (document != null) {
            if (Utils.equalsNull(document.getSeries(), StringUtils.isNotEmpty(passportSerial)?Convertor.fromMask(passportSerial):null) &&
                    Utils.equalsNull(document.getNumber(), StringUtils.isNotEmpty(passportNumber)?Convertor.fromMask(passportNumber):null) &&
                    Utils.equalsNull(document.getDocorgcode(), passportDepartmentCode) &&
                    Utils.equalsNull(document.getDocdate(), passportIssueDate) &&
                    Utils.equalsNull(document.getDocorg(), passportIssueOrganization)) {
                return document;
            }
            //поставим в предыдущую запись недействительность
            document.setIsactive(ActiveStatus.ARCHIVE);
            emMicro.merge(document);
        }

        //запишем новые данные
        return newDocument(null, peopleId, null, Partner.CLIENT, passportSerial, passportNumber, passportIssueDate,
                passportDepartmentCode, passportIssueOrganization, ActiveStatus.ACTIVE);
    }

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeContact(PeopleContact pc)	throws PeopleException{

		changeContactActive(pc.getId());
	}

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeContact(PeopleContact pc, Integer peopleId, int partnerId) {
	    PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleId);

		//поставим в предыдущую запись недействительность

		PeopleContactEntity oldcont=findPeopleByContactMan(pc.getContact().getCodeInteger(), peopleId);
		if (oldcont != null  ) {
    	    //если ничего не менялось, возвращаемся
			if (oldcont.equalsContent(pc.getEntity())){
				return;
			}
			
			oldcont.setIsactive(ActiveStatus.ARCHIVE);
			emMicro.merge(oldcont);
						   
		}
		//запишем новые данные

		setPeopleContact(pMain, refBooks.getPartnerEntity(partnerId), pc.getContact().getCodeInteger(),
                pc.getValue() == null ? null : pc.getValue().toLowerCase(), pc.getAvailable(), null, null);

		if (oldcont!=null){
			//если это логин
            Map<String, Object> login_const=rulesBean.getLoginConstants();
            String login_way = (String) login_const.get(SettingsKeys.LOGIN_WAY);
            
            if (login_way.equalsIgnoreCase(SettingsKeys.LOGIN_PHONE)&&pc.getContact().getCodeInteger() == PeopleContact.CONTACT_CELL_PHONE){
            	//меняем логин для пользователя
                if (StringUtils.isNotEmpty(oldcont.getValue())) {
                    try {
						userBean.updateUsername(oldcont.getValue(), Convertor.fromMask(pc.getValue().toLowerCase()));
					} catch (ObjectNotFoundException e) {
						logger.severe("Не найден контакт "+e);
					}
                }
            }
            if (login_way.equalsIgnoreCase(SettingsKeys.LOGIN_EMAIL)&&pc.getContact().getCodeInteger() == PeopleContact.CONTACT_EMAIL) {
                //меняем логин для пользователя
                if (StringUtils.isNotEmpty(oldcont.getValue())) {
                    try {
						userBean.updateUsername(oldcont.getValue(), pc.getValue().toLowerCase());
					} catch (ObjectNotFoundException e) {
						logger.severe("Не найден контакт "+e);
					}
                }
            }
			
		}
	}

    @Override
    @TransactionAttribute
    public PeopleContactEntity changeContact(Integer peopleId, Integer type, String value, boolean available)
            throws ObjectNotFoundException {
        PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleId);

        PeopleContactEntity contact = findPeopleByContactMan(type, peopleId);

        //поставим в предыдущую запись недействительность

        if (contact != null) {
            //если ничего не менялось, возвращаемся
            if (Utils.equalsNull(contact.getValue(), StringUtils.isNotEmpty(value)?Convertor.fromMask(value):null) 
            		&& Utils.equalsNull(contact.getAvailable(), available)) {
                return contact;
            }

            contact.setIsactive(ActiveStatus.ARCHIVE);
            emMicro.merge(contact);
        }
        //запишем новые данные

        PeopleContactEntity newContact=setPeopleContact(pMain, refBooks.getPartnerEntity(Partner.CLIENT), type,
                value == null ? null : value.toLowerCase(), available, null, new Date());
        if (contact!=null){
        	  //если это логин
            Map<String, Object> login_const=rulesBean.getLoginConstants();
            String login_way = (String) login_const.get(SettingsKeys.LOGIN_WAY);
            
            if (login_way.equalsIgnoreCase(SettingsKeys.LOGIN_PHONE)&&type == PeopleContact.CONTACT_CELL_PHONE){
            	//меняем логин для пользователя
                if (StringUtils.isNotEmpty(contact.getValue())) {
                    userBean.updateUsername(contact.getValue(), Convertor.fromMask(value.toLowerCase()));
                }
            }
            if (login_way.equalsIgnoreCase(SettingsKeys.LOGIN_EMAIL)&&type == PeopleContact.CONTACT_EMAIL) {
                //меняем логин для пользователя
                if (StringUtils.isNotEmpty(contact.getValue())) {
                    userBean.updateUsername(contact.getValue(), value.toLowerCase());
                }
            }
        }
        return newContact;
    }

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changePeopleData(PeoplePersonal pper, PeopleMisc pmisc, Documents activePassportRF, PeopleMain pmain, int partnerId, Date dateChange)
			throws PeopleException {
		if (pper != null) {
			changePersonalData(pper, pmain.getId(), partnerId, dateChange);
		}
		if (pmisc != null) {
			changeMiscData(pmisc, pmain.getId(), partnerId, dateChange);
		}
		if (activePassportRF != null) {
			changeDocument( activePassportRF, pmain.getId(), partnerId);
		}

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeSpouse(Spouse sp, PeopleMain pmain, int partnerId, Date dateChange) 	throws PeopleException {

		 int id=pmain.getId();
	     PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(id);

	     //проверяем партнера
	     if (sp!=null) {
			addSpouse(pMain, Convertor.toRightString(pmain.getSpouseActive().getPeopleMainSpouse().getPeoplePersonalActive().getSurname()), Convertor.toRightString(pmain.getSpouseActive().getPeopleMainSpouse().getPeoplePersonalActive().getName()), Convertor.toRightString(pmain.getSpouseActive().getPeopleMainSpouse().getPeoplePersonalActive().getMidname()), pmain.getSpouseActive().getPeopleMainSpouse().getPeoplePersonalActive().getBirthDate(), pmain.getSpouseActive().getPeopleMainSpouse().getCellPhone().getValue(), Spouse.CODE_SPOUSE, dateChange, pmain.getSpouseActive().getTypeWork().getCodeInteger());
	      }

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeEmployment(Employment empl, Integer peopleId, Date dt) {
       PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleId);
       //проверяем занятость
       EmploymentEntity emp = findEmployment(pMain, null, refBooks.getPartnerEntity(Partner.CLIENT), Employment.CURRENT);

       //если ничего не менялось, возвращаемся
       if (emp != null) {

    	 if (empl.equalsContent(emp)){
    		 return;
    	 }
    	 //поставим в предыдущую запись недействительность
    	 emp.setCurrent(Employment.RECENT);
         emp.setDataend( new Date( dt.getTime() - 1 ));
         emMicro.merge(emp);

       }

        //запишем новые данные
        newEmployment(null, peopleId,null,Partner.CLIENT,empl.getPlaceWork(), empl.getOccupation(),
        		refBooks.referenceCodeIntegerOrNull(empl.getEducation()),
        		refBooks.referenceCodeIntegerOrNull(empl.getTypeWork()),
        		refBooks.referenceCodeIntegerOrNull(empl.getDuration()),
        		refBooks.referenceCodeIntegerOrNull(empl.getProfession()),
        		refBooks.referenceCodeIntegerOrNull(empl.getExtSalaryId()),
        		empl.getSalary(), empl.getExtSalary(),
        		empl.getExperience(),
                refBooks.referenceCodeIntegerOrNull(empl.getDateStartWorkId()),
                empl.getDateStartWork(), Employment.CURRENT,
        		dt,empl.getExtCreditSum(),empl.getNextSalaryDate(),
        		refBooks.referenceCodeIntegerOrNull(empl.getOccupationId()));

   }

    @Override
    @TransactionAttribute
    public EmploymentEntity changeEmployment(Integer peopleId, Integer educationId, Integer occupationId,
                                             Integer professionId, Integer salaryFrequenceId,
                                             Integer additionalIncomeSourceId, String employmentPlace, String position,
                                             Double salary, Double additionalIncome, Date enterEmploymentDate,
                                             Date enterPositionDate) {
        PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleId);
        //проверяем занятость
        EmploymentEntity employment = findEmployment(pMain, null, refBooks.getPartnerEntity(Partner.CLIENT),
                Employment.CURRENT);

        //если ничего не менялось, возвращаемся
        if (employment != null) {

            if (Utils.equalsNull(employment.getEducationId() == null ? null : employment.getEducationId(),
                    educationId) &&
                    Utils.equalsNull(employment.getTypeworkId() == null ? null : employment.getTypeworkId(),
                            occupationId) &&
                    Utils.equalsNull(employment.getProfessionId() == null ? null : employment.getProfessionId(),
                            professionId) &&
                    Utils.equalsNull(employment.getDurationId() == null ? null : employment.getDurationId(),
                            salaryFrequenceId) &&
                    Utils.equalsNull(employment.getExtsalaryId() == null ? null : employment.getExtsalaryId(),
                            additionalIncomeSourceId) &&
                    Utils.equalsNull(employment.getPlaceWork(), employmentPlace) &&
                    Utils.equalsNull(employment.getOccupation(), position) &&
                    Utils.equalsNull(employment.getSalary(), salary) &&
                    Utils.equalsNull(employment.getExtsalary(), additionalIncome) &&
                    Utils.equalsNull(employment.getExperience(), enterEmploymentDate) &&
                    Utils.equalsNull(employment.getDatestartwork(), enterPositionDate)) {
                return employment;
            }
            //поставим в предыдущую запись недействительность
            employment.setCurrent(Employment.RECENT);
            employment.setDataend(new Date());
            emMicro.merge(employment);
        }

        //запишем новые данные
        return newEmployment(null, peopleId, null, Partner.CLIENT, employmentPlace, position, educationId, occupationId,
                salaryFrequenceId, professionId, additionalIncomeSourceId, salary, additionalIncome,
                enterEmploymentDate, null, enterPositionDate, Employment.CURRENT, new Date(), null, null, null);
    }

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeAddress(FiasAddress adr,	Integer peopleId, int partnerId, Date dt) {
	        PeopleMainEntity pMain = emMicro.find(PeopleMainEntity.class, peopleId);

	        //меняем адрес
	        AddressEntity addrreg = findAddressActive(pMain, adr.getAddrtype().getCodeInteger());

	        //если ничего не менялось, возвращаемся
	        if (addrreg != null) {
	        	if (addrreg.equalsContent(adr.getEntity()) && Utils.equalsNull(addrreg.getIsSame(), adr.getEntity().getIsSame()) )
	        		return;

	        	//поставим недействительность в старый адрес
	        	addrreg.setIsactive(ActiveStatus.ARCHIVE);
		        addrreg.setDataend(new Date( dt.getTime() - 1 ));
		        emMicro.merge(addrreg);
	        }

	        //запишем новые данные
	        String aobj=addressBean.findLastFiasGuid(adr.getEntity());
	        AddrObj addrObj=fiasServ.findAddressObject(aobj);
            if (addrObj!=null) {
              aobj= addrObj.getAOGUID();
            }
	        AddressEntity address = newAddressFias(null, peopleId,null,partnerId,adr.getAddrtype().getCodeInteger(),null,dt,
                    null, null,ActiveStatus.ACTIVE,aobj,adr.getHouse(),adr.getCorpus(),adr.getBuilding(),adr.getFlat());

	        address.setIsSame(Utils.booleanToTriState(adr.isIsSame()));

            emMicro.merge(address);

	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void changeAddressKzEgovData(Address address, Integer peopleId, int partnerId, Date dt) {

        PeopleMainEntity pMain = emMicro.find(PeopleMainEntity.class, peopleId);

        //меняем адрес
        AddressEntity addressActive = findAddressActive(pMain, address.getAddrtype().getCodeInteger());

        //если ничего не менялось, возвращаемся
        if (addressActive != null) {
            if (addressActive.equalsContent(address.getEntity()) && Utils.equalsNull(addressActive.getIsSame(), address.getEntity().getIsSame()) )
                return;

            //поставим недействительность в старый адрес
            addressActive.setIsactive(ActiveStatus.ARCHIVE);
            addressActive.setDataend(new Date(dt.getTime() - 1));
            emMicro.merge(addressActive);
        }

        //запишем новые данные
        newAddressKzEgovData(
                null,
                peopleId,
                null,
                partnerId,
                address.getAddrtype().getCodeInteger(),
                null,
                dt,
                null,
                ActiveStatus.ACTIVE,
                address.getKzegovdataCato(),
                address.getKzegovdataAts(),
                address.getKzegovdataGeonims(),
                address.getAddressTextToStreet(),
                address.getStreetName(),
                address.getHouse(),
                address.getCorpus(),
                address.getBuilding(),
                address.getFlat(),
                Utils.booleanToTriState(address.isIsSame())
        );

    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Account addAccount(Account accountInput, Integer peopleId) {
        //добавляем новый счет

		AccountEntity account =newAccount(null, peopleId, new Date(),
                accountInput.getIsWork(), accountInput.getContest(), accountInput.getAccountNumber(),
                refBooks.referenceCodeIntegerOrNull(accountInput.getAccountType()),
                accountInput.getBik(), accountInput.getCorrAccountNumber(), accountInput.getCardNumber(),
                accountInput.getCardNumberMasked(), accountInput.getPayonlineCardHash(),
                accountInput.getCardName(), accountInput.getCardMonthEnd(),
                accountInput.getCardYearEnd(), ActiveStatus.ACTIVE);

        return new Account(account);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public AccountEntity newAccount(AccountEntity account,Integer peopleMainId,Date date,
			Integer isWork,Boolean contest,String accountNumber,Integer accountTypeId,
			String bik,String corAccountNumber,String cardNumber,String cardNumberMasked,
			String payonlineHash,String cardName,Integer cardMonthEnd,Integer cardYearEnd,
			Integer isActive) {
		AccountEntity newAccount=null;
		//если это новая запись
		if (account==null){
			newAccount=new AccountEntity();
			if (peopleMainId!=null){
			  PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
			  newAccount.setPeopleMainId(peopleMain);
			}
			newAccount.setDateAdd(date);
		} else {
			newAccount=account;
		}
		newAccount.setIsWork(isWork);
        newAccount.setContest(contest);
        newAccount.setAccountnumber(Convertor.fromMask(accountNumber));
        if (accountTypeId!=null){
        	newAccount.setAccountTypeId(refBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, accountTypeId));
        }

        newAccount.setBik(bik);
        if (StringUtils.isNotEmpty(bik)){
            BankEntity bk = refBooks.getBank(bik);
            if (bk != null) {
                newAccount.setBankname(bk.getName());
                newAccount.setCorraccountnumber(bk.getCorAccount());
            } else {
                newAccount.setCorraccountnumber(corAccountNumber);
            }
        }
        newAccount.setCardnumber(cardNumber);
        if (StringUtils.isNotEmpty(cardNumber)){
        	if(cardNumber.length()>12){
        		newAccount.setCardNumberMasked("************" + cardNumber.substring(12));
        	} else {
                newAccount.setCardNumberMasked(cardNumberMasked);
        	}
        }
        newAccount.setPayonlineCardHash(payonlineHash);
        newAccount.setCardname(cardName);
        newAccount.setCardmonthend(cardMonthEnd);
        newAccount.setCardyearend(cardYearEnd);
        newAccount.setIsactive(ActiveStatus.ACTIVE);

        emMicro.persist(newAccount);
		return newAccount;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeAccountActive(Integer accountId) {
		AccountEntity acc=accountDAO.getAccountEntity(accountId);
		if (acc!=null){
		  acc.setIsactive(ActiveStatus.ARCHIVE);
		  emMicro.merge(acc);
		}

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeContactActive(Integer contactId) throws PeopleException {
		PeopleContactEntity cont=peopleDAO.getPeopleContactEntity(contactId);
		if (cont!=null)	{
		  cont.setIsactive(ActiveStatus.ARCHIVE);
		  emMicro.merge(cont);
	    }

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Account changeAccount(Account account, Integer peopleId) {
        PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(peopleId);

        AccountEntity oldAccount = findAccountActive(pMain, account.getAccountType().getCodeInteger());

        if (oldAccount != null) {
            if (oldAccount.equalsContent(account.getEntity())) {
                return new Account(oldAccount);
            } else {
                //поставим в предыдущую запись недействительность
                if (account.getId() != null) {
                    changeAccountActive(account.getId());
                }

            }
        }

        //запишем новые данные
        return addAccount(account, pMain.getId());
    }

	@Override
	public AccountEntity findLastAccountActive(PeopleMainEntity peopleMain) {
		  String hql = "from ru.simplgroupp.persistence.AccountEntity where (isactive = :isActive) and (peopleMainId.id = :peopleMainId) order by dateAdd desc";

	        Query qry = emMicro.createQuery(hql);
	        qry.setParameter("peopleMainId", peopleMain.getId());

	        qry.setParameter("isActive", ActiveStatus.ACTIVE);

	        List<AccountEntity> lst = qry.getResultList();
	        return (AccountEntity) Utils.firstOrNull(lst);

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void closeSpouse(PeopleMainEntity peopleMain,Date dt) {

		SpouseEntity sp=findSpouseActive(peopleMain);

		if (sp!=null) {
			sp.setDataend(dt);
			sp.setIsactive(ActiveStatus.ARCHIVE);
			emMicro.merge(sp);

		}

	}

    @Override
    @TransactionAttribute
    public void closeSpouse(Integer peopleId, Date date) {
        PeopleMainEntity people = peopleDAO.getPeopleMainEntity(peopleId);
        closeSpouse(people, date);
    }

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void closeAddress(AddressEntity addr, Date dt) {
		//поставим недействительность в старый адрес
    	addr.setIsactive(ActiveStatus.ARCHIVE);
        addr.setDataend(new Date( dt.getTime() - 1 ));
        emMicro.merge(addr);

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void savePeopleContact(PeopleMain pmain, String userId,Integer contactId) throws PeopleException {
		int id=pmain.getId();
		PeopleMainEntity pMain = peopleDAO.getPeopleMainEntity(id);
		setPeopleContact(pMain, refBooks.getPartnerEntity(Partner.CLIENT), contactId, userId, false, null, null);
	}


	@Override
	public DocumentEntity findPassportActive(PeopleMainEntity pmain) {
		return findDocument(pmain,null,Partner.CLIENT,ActiveStatus.ACTIVE, Documents.PASSPORT_RF);
	}

	@Override
	public AddressEntity findAddressActive(PeopleMainEntity pmain,
			Integer typeId) {
		return findAddress(pmain, Partner.CLIENT, null, typeId, ActiveStatus.ACTIVE);
	}

    @Override
    public DocumentOtherEntity findDocumentOtherActive(PeopleMainEntity pmain){
        return findDocumentOther(pmain, null, Partner.CLIENT, ActiveStatus.ACTIVE, RefHeader.PENSDOC_TYPES);
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public BlacklistEntity saveBlackList(PeopleMainEntity pmain, Integer reasonId,
			Date date, String comment) throws PeopleException {

        BlacklistEntity bl=new BlacklistEntity();
        bl.setPeopleMainId(pmain);
        bl.setDatabeg(date);
        bl.setDataend(DateUtils.addYears(date, 100));
        bl.setComment(comment);
        bl.setReasonId(refBooks.getBlackListReasonType(reasonId).getEntity());
        emMicro.persist(bl);

        try {
            eventLog.saveLog(EventType.INFO, EventCode.BLACKLIST_ADD,
                    "Добавление человека в черный список", null, new Date(), null, pmain, null);
        } catch (Exception e) {
            logger.severe("Не удалось сохранить лог при добавлении человека в черный список "+ e.getMessage());
        }

        return bl;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeFromBlackList(PeopleMainEntity pmain, Date date)
			throws PeopleException {
		BlacklistEntity bl=findInBlackList(pmain,date,null);
		if (bl!=null) {
			bl.setDataend(DateUtils.addDays(date, -1));
			emMicro.merge(bl);

            try {
                eventLog.saveLog(EventType.INFO, EventCode.BLACKLIST_REMOVE,
                        "Удаление человека из черного списка", null, new Date(), null, pmain, null);
            } catch (Exception e) {
                logger.severe("Не удалось сохранить лог при удалении человека из черного списка "+ e.getMessage());
            }
        }
	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public DocumentEntity findDocument(PeopleMainEntity peopleMain,
                                       CreditRequestEntity creditRequest, Integer partnerId,
                                       Integer active, Integer docType) {
        List<DocumentEntity> lst=findDocuments(peopleMain, creditRequest, partnerId,
                active,docType);
        return (DocumentEntity) Utils.firstOrNull(lst);

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<DocumentEntity> findDocuments(PeopleMainEntity peopleMain,
                                       CreditRequestEntity creditRequest, Integer partnerId,
                                       Integer active, Integer docType) {
    	return findDocuments(peopleMain, creditRequest, partnerId,
                active,docType,false);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<DocumentEntity> findDocuments(PeopleMainEntity peopleMain,
                                       CreditRequestEntity creditRequest, Integer partnerId,
                                       Integer active, Integer docType,Boolean excludeClient) {
        String hql = "from ru.simplgroupp.persistence.DocumentEntity where (documenttypeId.codeinteger=:doctype)";
        if (peopleMain != null) {
            hql = hql + " and (peopleMainId.id = :peopleMainId)";
        }
        if (creditRequest != null) {
            hql = hql + " and (creditRequestId.id = :creditRequest)";
        }
        if (partnerId != null) {
            hql = hql + " and (partnersId.id = :partner)";
        }
        if (active != null) {
            hql = hql + " and (isactive = :active)";
        }
        if (excludeClient) {
            hql = hql + " and (partnersId.id <> :partnerClient)";
        }
        hql+=" order by dateChange desc ";
        Query qry = emMicro.createQuery(hql);
        qry.setParameter("doctype", new Integer(docType));
        if (peopleMain != null) {
            qry.setParameter("peopleMainId", peopleMain.getId());
        }
        if (creditRequest != null) {
            qry.setParameter("creditRequest", creditRequest.getId());
        }
        if (partnerId != null) {
            qry.setParameter("partner", partnerId);
        }
        if (excludeClient) {
        	qry.setParameter("partnerClient", Partner.CLIENT);
        }
        if (active != null) {
            qry.setParameter("active", active);
        }
        return qry.getResultList();
    }

    @Override
    public int countPeopleMain(PeopleFilter filter) {
        String sql = "select count(*) from ru.simplgroupp.persistence.PeopleMainEntity as p where p.id in (select peopleMainId.id from ru.simplgroupp.persistence.CreditEntity where partnersId.id=:pid)";
        //снилс и инн
        if (StringUtils.isNotEmpty(filter.getSnils()))
            sql=sql+" and (p.snils=:snils)";
        if (StringUtils.isNotEmpty(filter.getInn()))
            sql=sql+" and (p.inn=:inn)";

        //ФИО и дата рождения
        if (StringUtils.isNotEmpty(filter.getSurname()) || StringUtils.isNotEmpty(filter.getName()) || StringUtils.isNotEmpty(filter.getMidname()) || (filter.getDateBirth() != null && filter.getDateBirth().getFrom() != null) || (filter.getDateBirth() != null && filter.getDateBirth().getTo() != null)) {

            sql = sql + " and ( (select count(*) from p.id.peoplepersonal as e where [$WHERE_PEOPLE$] ) > 0 )";

            String swhere = "(1=1)";
            if (StringUtils.isNotEmpty(filter.getSurname())) {
                swhere = swhere + " and (upper(e.surname) like :surname)";
            }
            if (StringUtils.isNotEmpty(filter.getName())) {
                swhere = swhere + " and (upper(e.name) like :name)";
            }
            if (StringUtils.isNotEmpty(filter.getMidname())) {
                swhere = swhere + " and (upper(e.midname) like :midname)";
            }
            if (filter.getDateBirth() != null && filter.getDateBirth().getFrom() != null)
                swhere=swhere+" and (e.birthdate>=:datefrom)";
            if (filter.getDateBirth() != null && filter.getDateBirth().getTo() != null)
                swhere=swhere+" and (e.birthdate<:dateto)";
            sql = sql.replace("[$WHERE_PEOPLE$]", swhere);
        }
        //ищем по документам
        if (StringUtils.isNotEmpty(filter.getPaspSeries())||StringUtils.isNotEmpty(filter.getPaspNumber())){
            sql = sql + " and ( (select count(*) from p.id.documents as e where [$WHERE_DOC$] ) > 0 )";
            String swhere = "(1=1)";
            if (StringUtils.isNotEmpty(filter.getPaspSeries())) {
                swhere = swhere + " and e.series=:series";
            }
            if (StringUtils.isNotEmpty(filter.getPaspNumber())) {
                swhere = swhere + " and e.number=:number";
            }
            sql = sql.replace("[$WHERE_DOC$]", swhere);
        }

        //ищем по контактам
        if (StringUtils.isNotEmpty(filter.getEmail())||StringUtils.isNotEmpty(filter.getCellPhone())){
            sql = sql + " and ( (select count(*) from p.id.peoplecontact as e where [$WHERE_CONTACT$] ) > 0 )";
            String swhere = "(1=1)";
            if (StringUtils.isNotEmpty(filter.getEmail())) {
                swhere = swhere + " and e.value=:email";
            }
            if (StringUtils.isNotEmpty(filter.getCellPhone())) {
                swhere = swhere + " and e.value=:phone";
            }
            sql = sql.replace("[$WHERE_CONTACT$]", swhere);
        }

        Query qry = emMicro.createQuery(sql);
        qry.setParameter("pid", Partner.SYSTEM);
        if (StringUtils.isNotEmpty(filter.getInn()))
            qry.setParameter("inn", filter.getInn());
        if (StringUtils.isNotEmpty(filter.getSnils()))
            qry.setParameter("snils", filter.getSnils());
        if (StringUtils.isNotEmpty(filter.getSurname())) {
            String q = filter.isCallCenterOnly() ? filter.getSurname().trim().toUpperCase() : "%" + filter.getSurname().trim().toUpperCase() + "%";
            qry.setParameter("surname", q);
        }
        if (StringUtils.isNotEmpty(filter.getName())) {
            String q = filter.isCallCenterOnly() ? filter.getName().trim().toUpperCase() : "%" + filter.getName().trim().toUpperCase() + "%";
            qry.setParameter("name", q);
        }
        if (StringUtils.isNotEmpty(filter.getMidname())) {
            String q = filter.isCallCenterOnly() ? filter.getMidname().trim().toUpperCase() : "%" + filter.getMidname().trim().toUpperCase() + "%";
            qry.setParameter("midname", q);
        }
        if (filter.getDateBirth() != null && filter.getDateBirth().getTo() != null) {
            qry.setParameter("dateto", DateUtils.addDays(filter.getDateBirth().getTo(),1),TemporalType.DATE);
        }
        if (filter.getDateBirth() != null && filter.getDateBirth().getFrom() != null) {
            qry.setParameter("datefrom", filter.getDateBirth().getFrom(),TemporalType.DATE);
        }
        if (StringUtils.isNotEmpty(filter.getPaspSeries())) {
            qry.setParameter("series", filter.getPaspSeries());
        }
        if (StringUtils.isNotEmpty(filter.getPaspNumber())) {
            qry.setParameter("number", filter.getPaspNumber());
        }
        if (StringUtils.isNotEmpty(filter.getEmail())) {
            qry.setParameter("email", filter.getEmail());
        }
        if (StringUtils.isNotEmpty(filter.getCellPhone())) {
            qry.setParameter("phone", filter.getCellPhone());
        }

        Number res = (Number) qry.getSingleResult();
        if (res == null)
            return 0;
        else
            return res.intValue();
    }
    
    @Override
    @Deprecated
	public int countPeopleMain(DateRange dateBirth,
			String surname, String name, String midname, String paspNumber,
			String paspSeries, String inn, String snils,String email,String phone) {
        PeopleFilter filter = new PeopleFilter();
        filter.setDateBirth(dateBirth);
        filter.setSurname(surname);
        filter.setName(name);
        filter.setMidname(midname);
        filter.setPaspNumber(paspNumber);
        filter.setPaspSeries(paspSeries);
        filter.setInn(inn);
        filter.setSnils(snils);
        filter.setEmail(email);
        filter.setCellPhone(phone);

        return countPeopleMain(filter);
	}

    @Override
    public List<PeopleMain> listPeopleMain(PeopleFilter filter) {
        String sql = " from ru.simplgroupp.persistence.PeopleMainEntity as p where p.id in (select peopleMainId.id from ru.simplgroupp.persistence.CreditEntity where partnersId.id=:pid)";
        //снилс и инн
        if (StringUtils.isNotEmpty(filter.getSnils()))
            sql = sql + " and (p.snils=:snils)";
        if (StringUtils.isNotEmpty(filter.getInn()))
            sql = sql + " and (p.inn=:inn)";

        //ФИО и дата рождения
        if (StringUtils.isNotEmpty(filter.getSurname()) || StringUtils.isNotEmpty(filter.getName()) || StringUtils.isNotEmpty(filter.getMidname()) || (filter.getDateBirth() != null && filter.getDateBirth().getFrom() != null) || (filter.getDateBirth() != null && filter.getDateBirth().getTo() != null)) {

            sql = sql + " and ( (select count(*) from p.id.peoplepersonal as e where [$WHERE_PEOPLE$] ) > 0 )";

            String swhere = "(1=1)";
            if (StringUtils.isNotEmpty(filter.getSurname())) {
                swhere = swhere + " and (upper(e.surname) like :surname)";
            }
            if (StringUtils.isNotEmpty(filter.getName())) {
                swhere = swhere + " and (upper(e.name) like :name)";
            }
            if (StringUtils.isNotEmpty(filter.getMidname())) {
                swhere = swhere + " and (upper(e.midname) like :midname)";
            }
            if (filter.getDateBirth() != null && filter.getDateBirth().getFrom() != null) {
                swhere = swhere + " and (e.birthdate>=:datefrom)";
            }
            if (filter.getDateBirth() != null && filter.getDateBirth().getTo() != null) {
                swhere = swhere + " and (e.birthdate<:dateto)";
            }
            sql = sql.replace("[$WHERE_PEOPLE$]", swhere);
        }

        //ищем по документам
        if (StringUtils.isNotEmpty(filter.getPaspSeries()) || StringUtils.isNotEmpty(filter.getPaspNumber())) {
            sql = sql + " and ( (select count(*) from p.id.documents as e where [$WHERE_DOC$] ) > 0 )";
            String swhere = "(1=1)";
            if (StringUtils.isNotEmpty(filter.getPaspSeries())) {
                swhere = swhere + " and e.series=:series";
            }
            if (StringUtils.isNotEmpty(filter.getPaspNumber())) {
                swhere = swhere + " and e.number=:number";
            }
            sql = sql.replace("[$WHERE_DOC$]", swhere);
        }

        //ищем по контактам
        if (StringUtils.isNotEmpty(filter.getEmail()) || StringUtils.isNotEmpty(filter.getCellPhone())) {
            sql = sql + " and ( (select count(*) from p.id.peoplecontact as e where [$WHERE_CONTACT$] ) > 0 )";
            String swhere = "(1=1)";
            if (StringUtils.isNotEmpty(filter.getEmail())) {
                swhere = swhere + " and e.value=:email";
            }
            if (StringUtils.isNotEmpty(filter.getCellPhone())) {
                swhere = swhere + " and e.value=:phone";
            }
            sql = sql.replace("[$WHERE_CONTACT$]", swhere);
        }

        sql = sql + SearchUtil.sortToString(peopleSortMapping, filter.getSort());
        String sortSelect = SearchUtil.sortSelectToString(peopleSortMapping, filter.getSort());
        sql = sql.replace("[$SELECT_SORTING$]", sortSelect);

        Query qry = emMicro.createQuery(sql);
        qry.setParameter("pid", Partner.SYSTEM);
        if (StringUtils.isNotEmpty(filter.getInn()))
            qry.setParameter("inn", filter.getInn());
        if (StringUtils.isNotEmpty(filter.getSnils()))
            qry.setParameter("snils", filter.getSnils());
        if (StringUtils.isNotEmpty(filter.getSurname())) {
            String q = filter.isCallCenterOnly() ? filter.getSurname().trim().toUpperCase() : "%" + filter.getSurname().trim().toUpperCase() + "%";
            qry.setParameter("surname", q);
        }
        if (StringUtils.isNotEmpty(filter.getName())) {
            String q = filter.isCallCenterOnly() ? filter.getName().trim().toUpperCase() : "%" + filter.getName().trim().toUpperCase() + "%";
            qry.setParameter("name", q);
        }
        if (StringUtils.isNotEmpty(filter.getMidname())) {
            String q = filter.isCallCenterOnly() ? filter.getMidname().trim().toUpperCase() : "%" + filter.getMidname().trim().toUpperCase() + "%";
            qry.setParameter("midname", q);
        }
        if (filter.getDateBirth() != null && filter.getDateBirth().getTo() != null) {
            qry.setParameter("dateto", DateUtils.addDays(filter.getDateBirth().getTo(), 1), TemporalType.DATE);
        }
        if (filter.getDateBirth() != null && filter.getDateBirth().getFrom() != null) {
            qry.setParameter("datefrom", filter.getDateBirth().getFrom(), TemporalType.DATE);
        }

        if (StringUtils.isNotEmpty(filter.getPaspSeries())) {
            qry.setParameter("series", filter.getPaspSeries());
        }
        if (StringUtils.isNotEmpty(filter.getPaspNumber())) {
            qry.setParameter("number", filter.getPaspNumber());
        }

        if (StringUtils.isNotEmpty(filter.getEmail())) {
            qry.setParameter("email", filter.getEmail());
        }
        if (StringUtils.isNotEmpty(filter.getCellPhone())) {
            qry.setParameter("phone", filter.getCellPhone());
        }
        if (filter.getFirstRow() >= 0)
            qry.setFirstResult(filter.getFirstRow());
        if (filter.getRows() > 0)
            qry.setMaxResults(filter.getRows());

        List<PeopleMainEntity> lst = null;
        if (filter.getSort() == null || filter.getSort().length == 0) {
            lst = qry.getResultList();
        } else {
            List<Object[]> lstSource = qry.getResultList();
            lst = new ArrayList<PeopleMainEntity>(lstSource.size());
            SearchUtil.extractColumn(lstSource, 0, lst);
            lstSource = null;
        }

        if (lst.size() > 0) {
            List<PeopleMain> lst1 = new ArrayList<PeopleMain>(lst.size());
            for (PeopleMainEntity pm : lst) {
                PeopleMain pmain = new PeopleMain(pm);
                lst1.add(pmain);
                if (filter.getOptions() != null && filter.getOptions().size() > 0) {
                    pmain.init(filter.getOptions());
                }
            }
            return lst1;
        } else {
            return new ArrayList<PeopleMain>(0);
        }
    }

	@Override
    @Deprecated
	public List<PeopleMain> listPeopleMain(int nFirst, int nCount,
			SortCriteria[] sorting, Set options, DateRange dateBirth,
			String surname, String name, String midname, String paspNumber,
			String paspSeries, String inn, String snils,String email,String phone) {
        PeopleFilter filter = new PeopleFilter();
        filter.setFirstRow(nFirst);
        filter.setRows(nCount);
        filter.setSort(sorting);
        filter.setOptions(options);
        filter.setDateBirth(dateBirth);
        filter.setSurname(surname);
        filter.setName(name);
        filter.setMidname(midname);
        filter.setPaspNumber(paspNumber);
        filter.setPaspSeries(paspSeries);
        filter.setInn(inn);
        filter.setSnils(snils);
        filter.setEmail(email);
        filter.setCellPhone(phone);

        return listPeopleMain(filter);
    }

	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void savePeopleBehavior(Integer ccRequestid, Integer peopleMainId, String parameterId,
			String parameterValue, Object parameterValueLong, Date parameterValueDate,
			Integer partnerId, Date date) {

		PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
		if (peopleMain!=null){
			PeopleBehaviorEntity pbe = new PeopleBehaviorEntity();
			CreditRequestEntity cre = creditRequestDAO.getCreditRequestEntity(ccRequestid);
			pbe.setCreditRequestId(cre);
    	    pbe.setPeopleMainId(peopleMain);
    	    pbe.setDateactual(date);
    	    pbe.setParamvaluedate(parameterValueDate);
    	    pbe.setParameterId(refBooks.getBehaviorGAType(parameterId).getEntity());
    	    if (StringUtils.isNotEmpty(parameterValue)){
    	        pbe.setParamvalue(parameterValue);
    	    }
    	    if (parameterValueLong!=null){
    	    	pbe.setParamvaluelong(Convertor.toLong(parameterValueLong));
    	    }
    	    pbe.setPartnersId(refBooks.getPartner(partnerId).getEntity());
    	    pbe.setWeboject("/main");
    	    emMicro.persist(pbe);
		}

	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PeopleBonus addBonus(Integer peopleMainID, Integer bonusCode, Integer operationCode) throws PeopleException {
        RefBonus refb = refBooks.getBonusByCodeInteger(bonusCode);
        if (refb != null) {
            Double amount = refb.getAmount();
            return addBonus(peopleMainID, bonusCode, operationCode, new Date(), amount, null, null);
        }
        return null;
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PeopleBonus addBonus(int peopleMainId, int bonusCode, int operarionCode, Date dateBonus, Double amount,
                                Integer relatedCreditId, Integer relatedPeopleMainId) throws PeopleException {
        //ищем настройку, активны ли бонусы в системе
		Integer isactive = Convertor.toInteger(rulesBean.getBonusConstants().get(CreditCalculatorBeanLocal.BONUS_ISACTIVE));
        if (isactive == 0) {
            return null;
        }

        //ищем бонусы в справочнике по коду
        RefBonus refb = refBooks.getBonusByCodeInteger(bonusCode);
        if (!refb.getIsactive()) {
            return null;
        }

        ReferenceEntity referenceOperation = refBooks.findByCodeIntegerEntity(RefHeader.PEOPLE_SUM_OPERATION, operarionCode);
        PeopleMainEntity peopleMain = peopleDAO.getPeopleMainEntity(peopleMainId);

        PeopleMainEntity relatedPeopleMain = null;
        if (relatedPeopleMainId != null) {
            relatedPeopleMain = peopleDAO.getPeopleMainEntity(relatedPeopleMainId);
        }

        CreditEntity relatedCredit = null;
        if (relatedCreditId != null) {
            relatedCredit = creditDAO.getCreditEntity(relatedCreditId);
        }


        Double overallBonusRate = Convertor.toDouble(rulesBean.getBonusConstants().get(CreditCalculatorBeanLocal.BONUS_RATE));

        // округляем бонусы в большую сторону, и их количество после корректирования в соответствии с курсом
        Integer roundedAmount = Convertor.toInteger(CalcUtils.roundUp(amount, 0));
        Integer ratedAmount = Convertor.toInteger(CalcUtils.roundUp(roundedAmount * overallBonusRate, 0));

        logger.info("Добавление бонусов: " + roundedAmount + " (округленное количество) * "
                + overallBonusRate + " (курс) = "
                + ratedAmount +" (в итоге округленное)");

        if (ratedAmount == 0) {
            logger.info("Количество бонусов = " + ratedAmount + "; добавление записи произведено не будет");
            return null;
        }

        PeopleBonusEntity pbe = peopleDAO.addBonus(peopleMain, refb.getEntity(), referenceOperation,
                roundedAmount.doubleValue(), ratedAmount.doubleValue(), dateBonus, relatedCredit,
                relatedPeopleMain);
        PeopleBonus bon = new PeopleBonus(pbe);

        //ищем шаблон письма
        ProductMessagesEntity message = productDAO.findMessageByName(ProductKeys.EMAIL_ADD_BONUS);
        if (message != null) {
            //высылаем письмо
            actionProcessor.sendEmailCommon(peopleMainId, message.getSubject(), message.getBody(), (Object[]) null);
        }

        eventSender.fireEvent(bon, EventCode.BONUS_ADDED);
        return bon;
	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeBonus(Integer peopleMainID, Integer bonusCode) {
        // проверяем активны ли бонусы
        Integer isactive = Convertor.toInteger(rulesBean.getBonusConstants().get(CreditCalculatorBeanLocal.BONUS_ISACTIVE));
        if(isactive == 0){
            return;
        }
        // удаляем бонус
        peopleDAO.removeBonus(peopleMainID, bonusCode);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<PeopleBonusEntity> findCreditPayBonus(Integer credit_id, Date paydate){
        return peopleDAO.findCreditPayBonus(credit_id,paydate, refBooks.findByCodeIntegerEntity(RefHeader.PEOPLE_SUM_OPERATION, BaseCredit.OPERATION_OUT).getId());
    }

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean saveFriend(Integer peopleMainId, String name, String surname,
			String email,String phone,Boolean available,Integer forBonus) throws KassaException {

		PeopleFriend pf = null;

        Map<String, Object> loginConst = rulesBean.getLoginConstants();
        String loginWay = (String) loginConst.get(SettingsKeys.LOGIN_WAY);
        //если приглашаем за бонусы, надо проверить, нет ли уже такого приглашенного человека
        if (loginWay.equalsIgnoreCase(SettingsKeys.LOGIN_PHONE)) {
            // ищем по телефону
            if (StringUtils.isNotEmpty(phone)) {
                pf = findCalledFriendByPhone(phone);
            }
        } else {
            // ищем по email
            if (StringUtils.isNotEmpty(email)) {
                pf = findCalledFriend(email.toLowerCase());
            }
        }

		if(pf == null)
		{
			PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
			if (peopleMain!=null){
				PeopleFriendEntity friend = new PeopleFriendEntity();
				friend.setDateactual(new Date());
                if (email != null) {
                    friend.setEmail(email.toLowerCase());
                }
				friend.setName(Convertor.toRightString(name));
				friend.setSurname(Convertor.toRightString(surname));
				friend.setPeopleMainId(peopleMain);
				friend.setPhone(Convertor.fromMask(phone));
				friend.setAvailable(available);
				friend.setForBonus(forBonus);
	    	    emMicro.persist(friend);
	    	    logger.info("Сохранили друга "+email+" для бонусов="+forBonus);
	    	    // если пригласили друга, можем получить за это бонусы
	    	    if (forBonus == PeopleFriend.FOR_BONUS) {
                    ProductMessagesEntity message;
                    if (loginWay.equalsIgnoreCase(SettingsKeys.LOGIN_PHONE)) {
                        // отправляем смс
                        message = productDAO.findMessageByName(ProductKeys.SMS_INVITE_FRIEND);
                        if (message != null) {
                            logger.info("Отправляем смс по номеру: " + phone);
                            mailBean.sendSMSV2(phone, message.getBody());
                        }
                    } else {
                        message = productDAO.findMessageByName(ProductKeys.EMAIL_INVITE_FRIEND);
                        if (message != null) {
                            logger.info("Высылаем письмо на адрес " + email);
                            //высылаем письмо
                            actionProcessor.sendEmailCommon(null, email, message.getSubject(), message.getBody(), (Object[]) null);
                        }
                    }
                    eventSender.fireEvent(new PeopleMain(peopleMain), EventCode.SEND_CALL_TO_FRIEND);
	    	    }
			}
			return true;
		}
		return false;

	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PeoplePersonalEntity newPeoplePersonal(PeoplePersonalEntity people,Integer peopleMainId,
			Integer creditRequestId, Integer partnerId, String surname,
			String name, String midname, String maidenname,Date birthdate, String birthplace,
			Integer gender,Date databeg, Integer active) {

		PeoplePersonalEntity peoplePersonal=null;
		//если это новая запись
		if (people==null){
		    peoplePersonal=	new PeoplePersonalEntity();
		    if (partnerId!=null){
				PartnersEntity partner=refBooks.getPartnerEntity(partnerId);
			    peoplePersonal.setPartnersId(partner);
			    //если данные заносит клиент, нужны доп.поля
			    if (partnerId==Partner.CLIENT){
			    	peoplePersonal.setIsUploaded(false);
			    	peoplePersonal.setCitizen(refBooks.getCountryEntity(BaseAddress.COUNTRY_RUSSIA));
			    }
			}
		    if (peopleMainId!=null){
				PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
			    peoplePersonal.setPeopleMainId(peopleMain);
			}
		    if (creditRequestId!=null){
				CreditRequestEntity creditRequest=creditRequestDAO.getCreditRequestEntity(creditRequestId);
			    peoplePersonal.setCreditRequestId(creditRequest);
			}
		    peoplePersonal.setDatabeg(databeg);
		} else {
			peoplePersonal=people;
		}

		peoplePersonal.setSurname(Convertor.toRightString(surname));
		peoplePersonal.setName(Convertor.toRightString(name));
		peoplePersonal.setMidname(Convertor.toRightString(midname));
		peoplePersonal.setMaidenname(Convertor.toRightString(maidenname));
		peoplePersonal.setBirthdate(birthdate);
		peoplePersonal.setBirthplace(StringUtils.isEmpty(birthplace)?birthplace:birthplace.trim());
		peoplePersonal.setIsactive(active);
		if (gender!=null){
			peoplePersonal.setGender(refBooks.findByCodeIntegerEntity(RefHeader.GENDER_TYPE, gender));
		}

		emMicro.persist(peoplePersonal);

		return peoplePersonal;

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public DocumentEntity newDocument(DocumentEntity passport,Integer peopleMainId, Integer creditRequestId,
			Integer partnerId, String series, String number, Date docdate,
			String docorgcode, String docorg, Integer active) {


		DocumentEntity document=null;
		//если это новый документ
		if (passport==null){
			document=new DocumentEntity();
		    if (creditRequestId!=null){
			    CreditRequestEntity creditRequest=creditRequestDAO.getCreditRequestEntity(creditRequestId);
		        document.setCreditRequestId(creditRequest);
		    }
		    if (peopleMainId!=null){
			    PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
		        document.setPeopleMainId(peopleMain);
		    }
		    if (partnerId!=null){
			    PartnersEntity partner=refBooks.getPartnerEntity(partnerId);
		        document.setPartnersId(partner);
		        //если данные заносит клиент, нужны доп.поля
		        if (partnerId==Partner.CLIENT){
		    	    document.setIsUploaded(false);
		    	    document.setDateChange(new Date());
		        }
		    }
		    document.setDocumenttypeId(refBooks.findByCodeIntegerEntity(RefHeader.DOC_VER_TYPE, Documents.PASSPORT_RF));
		} else {
			document=passport;
		}

		document.setDocdate(docdate);
		document.setSeries(Convertor.fromMask(series));
		document.setNumber(number);
		document.setDocorg(Convertor.toLimitString(StringUtils.isEmpty(docorg)?docorg:docorg.trim(), 255));
		document.setDocorgcode(docorgcode);

		document.setIsactive(active);

		emMicro.persist(document);

		return document;
	}

    @Override
    public DocumentOtherEntity newOtherDocument(DocumentOtherEntity document, ReferenceEntity type, Integer peopleMainId, Integer partnerId, String number, Date docdate, Integer active) {
        DocumentOtherEntity doc=null;
        //если это новый документ
        if (document==null){
            doc=new DocumentOtherEntity();
            if (peopleMainId!=null){
                PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
                doc.setPeopleMainId(peopleMain);
            }
            if (partnerId!=null){
                PartnersEntity partner=refBooks.getPartnerEntity(partnerId);
                doc.setPartnersId(partner);
            }
            doc.setDocumenttypeId(type);
        } else {
            doc=document;
        }

        doc.setDocdate(docdate);
        doc.setNumber(number);
        doc.setIsactive(active);
        emMicro.persist(doc);

        return doc;
    }


    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public AddressEntity newAddressFias(AddressEntity addr, Integer peopleMainId,Integer creditRequestId,Integer partnerId,
			Integer addressTypeId,String addressText,Date databeg,Date dataend,String country,
			Integer active,String fiasGuid,String house,String corpus,String building,String flat) {

		  AddressEntity address=null;
		  //если это новый адрес
		  if (addr==null){
			  address= new AddressEntity();
			  if (partnerId!=null){
				  PartnersEntity partner=refBooks.getPartnerEntity(partnerId);
				  address.setPartnersId(partner);
			  }
			  if (peopleMainId!=null){
				  PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
				  address.setPeopleMainId(peopleMain);
			  }
			  address.setAddrtype(refBooks.findByCodeIntegerEntity(RefHeader.ADDRESS_TYPE, addressTypeId));
		      if (creditRequestId!=null){
				  CreditRequestEntity creditRequest=creditRequestDAO.getCreditRequestEntity(creditRequestId);
				  address.setCreditRequestId(creditRequest);
			  }
		  } else {
			  address=addr;
		  }

	      if (StringUtils.isNotEmpty(fiasGuid)){
	    	  AddrObj addressObject = fiasServ.findAddressObjectByAoguid(fiasGuid);
	          addressBean.fillAddress(address, addressObject);
	      }

	      address.setHouse(Convertor.toLimitString(house, 10));
	      address.setCorpus(Convertor.toLimitString(corpus, 10));
	      address.setBuilding(Convertor.toLimitString(building, 10));
	      address.setFlat(Convertor.toLimitString(flat, 10));

	      if (StringUtils.isEmpty(addressText)){
	    	  address.setAddressText(address.getDescription());
	      } else {
	    	  address.setAddressText(Convertor.toLimitString(addressText,500));
	      }

	      address.setIsactive(active);
	      address.setDatabeg(databeg);
	      address.setDataend(dataend);

	      if (StringUtils.isNotEmpty(country)){
	    	  address.setCountry(refBooks.getCountryEntity(country.toUpperCase()));
	      } else {
	    	  address.setCountry(refBooks.getCountryEntity(BaseAddress.COUNTRY_RUSSIA));
	      }

	      emMicro.persist(address);
	      return address;

	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public AddressEntity newAddressFias(AddressEntity addr, Integer peopleMainId, Integer creditRequestId, Integer partnerId,
                                        Integer addressTypeId, String addressText, Date databeg, Date dataend, String country,
                                        Integer active, String fiasGuid, String house, String corpus, String building,
                                        String flat, Integer isSame) {

        AddressEntity address = null;
        //если это новый адрес
        if (addr == null) {
            address = new AddressEntity();
            if (partnerId != null) {
                PartnersEntity partner = refBooks.getPartnerEntity(partnerId);
                address.setPartnersId(partner);
            }
            if (peopleMainId != null) {
                PeopleMainEntity peopleMain = peopleDAO.getPeopleMainEntity(peopleMainId);
                address.setPeopleMainId(peopleMain);
            }
            address.setAddrtype(refBooks.findByCodeIntegerEntity(RefHeader.ADDRESS_TYPE, addressTypeId));
            if (creditRequestId != null) {
                CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequestEntity(creditRequestId);
                address.setCreditRequestId(creditRequest);
            }
        } else {
            address = addr;
        }

        if (StringUtils.isNotEmpty(fiasGuid)) {
            AddrObj addressObject = fiasServ.findAddressObjectByAoguid(fiasGuid);
            addressBean.fillAddress(address, addressObject);
        }

        address.setHouse(Convertor.toLimitString(house, 10));
        address.setCorpus(Convertor.toLimitString(corpus, 10));
        address.setBuilding(Convertor.toLimitString(building, 10));
        address.setFlat(Convertor.toLimitString(flat, 10));

        if (StringUtils.isEmpty(addressText)) {
            address.setAddressText(address.getDescription());
        } else {
            address.setAddressText(Convertor.toLimitString(addressText, 500));
        }

        address.setIsactive(active);
        address.setDatabeg(databeg);
        address.setDataend(dataend);
        address.setIsSame(isSame);

        if (StringUtils.isNotEmpty(country)) {
            address.setCountry(refBooks.getCountryEntity(country.toUpperCase()));
        } else {
            address.setCountry(refBooks.getCountryEntity(BaseAddress.COUNTRY_RUSSIA));
        }

        emMicro.persist(address);
        return address;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public AddressEntity newAddressKzEgovData(
            AddressEntity currentAddress,
            Integer peopleMainId,
            Integer creditRequestId,
            Integer partnerId,
            Integer addressTypeId,
            String addressText,
            Date databeg,
            Date dataend,
            Integer active,
            String kzegovdataCato,
            Long kzegovdataAts,
            Long kzegovdataGeonims,
            String addressTextToStreet,
            String streetText,
            String house,
            String corpus,
            String building,
            String flat,
            Integer isSame
    ) {

        AddressEntity newAddress = null;
        //если это новый адрес
        if (currentAddress == null) {
            newAddress = new AddressEntity();
            if (partnerId != null) {
                PartnersEntity partner = refBooks.getPartnerEntity(partnerId);
                newAddress.setPartnersId(partner);
            }
            if (peopleMainId != null) {
                PeopleMainEntity peopleMain = peopleDAO.getPeopleMainEntity(peopleMainId);
                newAddress.setPeopleMainId(peopleMain);
            }
            newAddress.setAddrtype(refBooks.findByCodeIntegerEntity(RefHeader.ADDRESS_TYPE, addressTypeId));
            if (creditRequestId != null) {
                CreditRequestEntity creditRequest = creditRequestDAO.getCreditRequestEntity(creditRequestId);
                newAddress.setCreditRequestId(creditRequest);
            }
        } else {
            newAddress = currentAddress;
        }

        newAddress.setStreetName(Convertor.toLimitString(streetText, 200));
        newAddress.setKzegovdataCato(Convertor.toLimitString(kzegovdataCato, 18));
        newAddress.setAddressTextToStreet(Convertor.toLimitString(addressTextToStreet, 500));
        newAddress.setKzegovdataAts(kzegovdataAts);
        newAddress.setKzegovdataGeonims(kzegovdataGeonims);
        newAddress.setHouse(Convertor.toLimitString(house, 10));
        newAddress.setCorpus(Convertor.toLimitString(corpus, 10));
        newAddress.setBuilding(Convertor.toLimitString(building, 10));
        newAddress.setFlat(Convertor.toLimitString(flat, 10));

        if (!StringUtils.isEmpty(addressText)) {
            newAddress.setAddressText(Convertor.toLimitString(addressText, 500));
        }

        newAddress.setIsactive(active);
        newAddress.setDatabeg(databeg);
        newAddress.setDataend(dataend);
        newAddress.setIsSame(isSame);

        newAddress.setCountry(refBooks.getCountryEntity(BaseAddress.COUNTRY_KAZAKHSTAN));

        emMicro.persist(newAddress);
        return newAddress;
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void newPeopleIncapacity(Integer peopleMainId,
			Integer creditRequestId, Integer partnerId, Date incapacityDate,
			Integer incapacityId) throws PeopleException {
		PeopleIncapacityEntity incapacity=new PeopleIncapacityEntity();
		if (partnerId!=null){
			  PartnersEntity partner=refBooks.getPartnerEntity(partnerId);
			  incapacity.setPartnersId(partner);
		}
		if (peopleMainId!=null){
			  PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
			  incapacity.setPeopleMainId(peopleMain);
		}
		incapacity.setIncapacityDate(incapacityDate);
		if (creditRequestId!=null){
			  CreditRequestEntity creditRequest=creditRequestDAO.getCreditRequestEntity(creditRequestId);
			  incapacity.setCreditRequestId(creditRequest);
		}
		incapacity.setIncapacityId(refBooks.findByCodeIntegerEntity(RefHeader.INCAPACITY_TYPE,incapacityId));
		emMicro.persist(incapacity);
	}

    @Override
    public PeopleFriend findCalledFriendByPhone(String phone) {
        Query qry = emMicro.createNamedQuery("findCalledFriendByPhone");
        qry.setParameter("phone", phone);
        qry.setMaxResults(1);
        List<PeopleFriendEntity> lst = qry.getResultList();
        if (lst.size() == 0) {
            return null;
        } else {
            return new PeopleFriend(lst.get(0));
        }
    }

	@Override
	public PeopleFriend findCalledFriend(String friendEmail) {
		 Query qry = emMicro.createNamedQuery("findCalledFriendByEmail");
	     qry.setParameter("email", friendEmail);
	     qry.setMaxResults(1);
	     List<PeopleFriendEntity> lst=qry.getResultList();
	     if (lst.size() == 0) {
	         return null;
	     } else {
	         return new PeopleFriend(lst.get(0));
	     }
	}

	@Override
	public List<PeopleIncapacityEntity> findPeopleIncapacity(
			Integer peopleMainId, Integer creditRequestId, Integer partnerId) {

		String hql = "from ru.simplgroupp.persistence.PeopleIncapacityEntity where (1=1)";

	    if (peopleMainId != null) {
	        hql = hql + " and (peopleMainId.id = :peopleMainId)";
	    }
	    if (creditRequestId != null) {
	        hql = hql + " and (creditRequestId.id = :creditRequestId)";
	    }
	    if (partnerId != null) {
	        hql = hql + " and (partnersId.id = :partnerId)";
	    }
	    Query qry = emMicro.createQuery(hql);
	    if (peopleMainId != null) {
	        qry.setParameter("peopleMainId", peopleMainId);
	    }
	    if (creditRequestId != null) {
	        qry.setParameter("creditRequestId", creditRequestId);
	    }
	    if (partnerId != null) {
	        qry.setParameter("partnerId", partnerId);
	    }
	    List<PeopleIncapacityEntity> lst=qry.getResultList();
		return lst;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PeopleContactEntity savePhoneInfo(PeopleContactEntity peopleContact)
			throws PeopleException {
		if (peopleContact!=null){
			//урл и логин-пароль для БД телефонов
        	Map<String,Object> params=rulesBean.getSiteConstants();
            String phoneSite = (String) params.get(SettingsKeys.PHONE_DB_URL);
            String phoneLogin=(String) params.get(SettingsKeys.PHONE_DB_URL_LOGIN);
            String phonePassword=(String) params.get(SettingsKeys.PHONE_DB_URL_PASSWORD);
            //если в БД есть url и логин с паролем
           	if (StringUtils.isNotEmpty(phoneSite)&&StringUtils.isNotEmpty(phoneLogin)&&StringUtils.isNotEmpty(phonePassword)){
           		logger.info("Url "+phoneSite+peopleContact.getValue().substring(1,11));
           		//посылаем запрос, получаем ответ
           	    byte[] response=null;
           	    Map<String,String> rparams=new HashMap<String,String>();
			    rparams.put("Authorization", "Basic "+HTTPUtils.getBasicAuthorizationString(phoneLogin, phonePassword));
				try {
					response = HTTPUtils.sendHttp("GET", phoneSite+peopleContact.getValue().substring(1,11), null, rparams, null);
				} catch (Exception e) {
					logger.severe("Не удалось получить ответ от сайта "+phoneSite+", ошибка "+e);
					return peopleContact;
					
				}
        		//если есть ответ
        		if (response!=null){
        			String answer=new String(response);
        			 JSONParser parser = new JSONParser();
        			 JSONObject jsonAnswer=null;
        			 //пытаемся его разобрать
					 try {
						jsonAnswer = (JSONObject) parser.parse(answer);
					 } catch (ParseException e) {
						logger.severe("Не удалось разобрать ответ из БД телефонов "+answer);
						return peopleContact;
						
					 }

					 //если удалось разобрать ответ
        			 if (jsonAnswer!=null){
        			     //проверяем статус ответа
        				 Integer nstatus=Convertor.toInteger(jsonAnswer.get("status"));
        				 //телефон найден
        				 if (nstatus==1){
        					 JSONObject result= (JSONObject) jsonAnswer.get("result");
        					 if (result!=null){
        						 String region=JsonUtils.getStringValue(result,"region");
        						 RegionsEntity regionShort=refBooks.findRegionByName(region);
        	            		 peopleContact.setRegionShort(regionShort);
        	            		 peopleContact.setRegion(region);
        	            		 String operator=JsonUtils.getStringValue(result,"operator");
        	            		 peopleContact.setOperator(operator);
        	            		 emMicro.merge(peopleContact);
        	            		 logger.info("Region "+region+", operator "+operator);
        					 }
        				 } else if (nstatus==2){
        					 //если была ошибка
        					 String emessage=(String) jsonAnswer.get("error");
        					 logger.severe("Сервис вернул ошибку "+emessage);
     						
        				 }

        			 }//end если удалось разобрать ответ
        		}//end если есть ответ

           	}//end если есть url
		}//end если контакт не null
		return peopleContact;
	}

	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public BlacklistEntity saveTerrorist(PeoplePersonalEntity people) throws PeopleException{
		 if (people!=null){
			PeopleMainEntity pmain=peopleDAO.getPeopleMainEntity(people.getPeopleMainId().getId());

			//урл для БД террористов
	       	Map<String,Object> params=rulesBean.getSiteConstants();
	        String terrSite = (String) params.get(SettingsKeys.TERRORIST_DB_URL);
	        String terrLogin=(String) params.get(SettingsKeys.TERRORIST_DB_URL_LOGIN);
	        String terrPassword=(String) params.get(SettingsKeys.TERRORIST_DB_URL_PASSWORD);
	        //если в БД есть url и логин с паролем
           	if (StringUtils.isNotEmpty(terrSite)&&StringUtils.isNotEmpty(terrLogin)&&StringUtils.isNotEmpty(terrPassword)){
           		logger.info("Данные "+people.getFullName());
           	    byte[] response=null;
			    try {
			    	Map<String,String> rparams=new HashMap<String,String>();
				    rparams.put("Authorization", "Basic "+HTTPUtils.getBasicAuthorizationString(terrLogin, terrPassword));
			    	String rmessage="surname="+URLEncoder.encode(people.getSurname(),XmlUtils.ENCODING_UTF)+"&name="+URLEncoder.encode(people.getName(),XmlUtils.ENCODING_UTF)+"&midname="+URLEncoder.encode(people.getMidname(),XmlUtils.ENCODING_UTF)
			    			+"&birthday="+DatesUtils.DATE_FORMAT_ddMMYYYY.format(people.getBirthdate());
			    	logger.info(terrSite + rmessage);
			    	response = HTTPUtils.sendHttp("POST", terrSite,rmessage.getBytes(), rparams, null);
			    } catch (Exception e) {
				    logger.severe("Не удалось получить ответ от сайта "+terrSite+", ошибка "+e);
				 
			    }

			    if (response!=null){
					String answer=new String(response);
					 JSONParser parser = new JSONParser();
					 JSONObject jsonAnswer = null;
					 //пытаемся его разобрать
					 try {
						jsonAnswer = (JSONObject) parser.parse(answer);
					 } catch (ParseException e) {
						logger.severe("Не удалось разобрать ответ из БД террористов "+answer);
						
					 }
					 if (jsonAnswer!=null){
					     if (jsonAnswer.get("exist")!=null){
						   Boolean nstatus=Convertor.toBoolean(jsonAnswer.get("exist"));
						   if (nstatus){
							 JSONObject result= (JSONObject) jsonAnswer.get("result");
							 if (result!=null){
								 String dbeg=(String) result.get("databeg");
								 String dend=(String) result.get("dataend");
								 Boolean active=Convertor.toBoolean(result.get("isActive"));
								 BlacklistEntity blackOld= findInBlackList(pmain, new Date(), BlackList.REASON_TERRORISM);
								 //если человек нашелся в нашем черном списке
								 if (blackOld!=null){
									 //и в БД террористов он тоже есть
									 if (active){
									     return blackOld;
									 } else {
										 //если нет, то убираем у нас
										 removeFromBlackList(pmain, Convertor.toDate(dend, DatesUtils.FORMAT_ddMMYYYY));
									 }
								 } else {
									 //в БД террористов он есть
									 if (active){
										 BlacklistEntity blackNew=saveBlackList(pmain, BlackList.REASON_TERRORISM, Convertor.toDate(dbeg, DatesUtils.FORMAT_ddMMYYYY),null);
									     return blackNew;
									 }
								 }//end есть в нашем черном списке

							 }//end есть результат
						   } //end нашелся террорист
					     } else {
					    	 //error
					    	 String emessage=(String) jsonAnswer.get("error");
					    	 logger.severe("Сервис вернул ошибку"+emessage);
					    	
					     }//end exist не null

					}//end answer не null
				}//end response не null

           	}//не пустые сайт, логин и пароль
		 }//people не null
		 return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveUserBonusProperties(Integer peopleMainId, Integer bonuspay) {

		PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
		if (peopleMain!=null){
			if(peopleDAO.updatePeopleBonusProperties(peopleMainId, bonuspay) == 0){
				UserPropertiesEntity props = new UserPropertiesEntity();
				props.setActionDate(new Date());
				props.setPayByBonus(bonuspay);
				props.setPeopleMainId(peopleMain);

				emMicro.persist(props);
			}

		}

	}

	@Override
	public UserPropertiesEntity findUserProperties(Integer peopleMainId) {
		Query qry = emMicro.createNamedQuery("findUserProperties");
	    qry.setParameter("peopleMainId",peopleMainId);
	    List<UserPropertiesEntity> lst=qry.getResultList();
	    return (UserPropertiesEntity) Utils.firstOrNull(lst);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PeopleMiscEntity newPeopleMisc(PeopleMiscEntity peopleMisc,
			Integer peopleMainId, Integer creditRequestId, Integer partnerId,
			Integer marriageTypeId, Integer children, Integer underage, Integer realtyDateId, Date realtyDate,
            Integer regDateId, Date regDate, Integer groundTypeId, boolean car,Date databeg,
			Integer active,boolean driverLicense) {

		PeopleMiscEntity misc=null;
		//если это новая запись
		if (peopleMisc==null){
			misc=new PeopleMiscEntity();
			if (partnerId!=null){
				PartnersEntity partner=refBooks.getPartnerEntity(partnerId);
			    misc.setPartnersId(partner);
			}
			if (peopleMainId!=null){
				PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
			    misc.setPeopleMainId(peopleMain);
			}
			if (creditRequestId!=null){
				CreditRequestEntity creditRequest=creditRequestDAO.getCreditRequestEntity(creditRequestId);
			    misc.setCreditRequestId(creditRequest);
			}
			misc.setDatabeg(databeg);
		} else {
			misc=peopleMisc;
		}
		if (marriageTypeId!=null){
			misc.setMarriageId(refBooks.findByCodeIntegerEntity(RefHeader.MARITAL_STATUS_SYSTEM, marriageTypeId));
		}
		misc.setRealtyDate(realtyDate);

		if (regDate!=null){
		    misc.setRegDate(regDate);
		} else {
			misc.setRegDate(misc.getRealtyDate());
		}
		if (groundTypeId!=null){
			misc.setRealtyId(refBooks.findByCodeIntegerEntity(RefHeader.REALTY_TYPE,groundTypeId));
		}
        if (regDateId != null) {
            misc.setRegDateId(refBooks.findByCodeIntegerEntity(RefHeader.TIME_RANGE, regDateId));
        }
        
        if (realtyDateId != null) {
            misc.setRealtyDateId(refBooks.findByCodeIntegerEntity(RefHeader.TIME_RANGE, realtyDateId));
        }

		misc.setCar(car);
		misc.setDriverLicense(driverLicense);
		misc.setChildren(children);
        misc.setUnderage(underage);
		misc.setIsactive(active);
		emMicro.persist(misc);
		return misc;
	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PeopleOtherEntity newPeopleOther(PeopleOtherEntity peopleOther,Integer peopleMainId, Integer creditRequestId, Integer partnerId,
                                            String public_role,String  public_relative_role, String public_relative_fio, String benific_fio,
                                            boolean reclam_accept, boolean other, Integer active){
        PeopleOtherEntity oth = null;
        //если это новая запись
        if (peopleOther==null){
            oth=new PeopleOtherEntity();
            if (peopleMainId!=null){
                PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
                oth.setPeopleMainId(peopleMain);
            }
            if (partnerId!=null){
                PartnersEntity partner=refBooks.getPartnerEntity(partnerId);
                oth.setPartnersId(partner);
            }
            if (creditRequestId!=null){
                CreditRequestEntity creditRequest=creditRequestDAO.getCreditRequestEntity(creditRequestId);
                oth.setCreditRequestId(creditRequest);
            }
        } else {
            oth=peopleOther;
        }
        oth.setOther(other);
        oth.setPublic_role(public_role);
        oth.setPublic_relative_fio(public_relative_fio);
        oth.setPublic_relative_role(public_relative_role);
        oth.setBenific_fio(benific_fio);
        oth.setReclam_accept(reclam_accept);
        oth.setIsactive(active);
        emMicro.persist(oth);
        return oth;
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public EmploymentEntity newEmployment(EmploymentEntity employ,
			Integer peopleMainId, Integer creditRequestId, Integer partnerId,
			String placeWork, String occupation, Integer educationId,
			Integer typeWorkId, Integer durationId, Integer professionId,
			Integer extSalaryId, Double salary, Double extSalary,
			Date experience, Integer dateStartWorkId, Date dateStartWork, Integer current,
			Date databeg,Double extCreditSum,Date nextSalaryDate,
			Integer occupationId) {
		EmploymentEntity newEmploy=null;
		//если это новая запись
		if (employ==null){
			newEmploy=new EmploymentEntity();
			if (partnerId!=null){
				PartnersEntity partner=refBooks.getPartnerEntity(partnerId);
			    newEmploy.setPartnersId(partner);
			}
			if (peopleMainId!=null){
				PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
			    newEmploy.setPeopleMainId(peopleMain);
			}
			if (creditRequestId!=null){
				CreditRequestEntity creditRequest=creditRequestDAO.getCreditRequestEntity(creditRequestId);
			    newEmploy.setCreditRequestId(creditRequest);
			}
			newEmploy.setDatabeg(databeg);
		} else {
			newEmploy=employ;
		}

		if (educationId!=null){
			newEmploy.setEducationId(refBooks.findByCodeIntegerEntity(RefHeader.EDUCATION_TYPE, educationId));
		}
		if (typeWorkId!=null){
			newEmploy.setTypeworkId(refBooks.findByCodeIntegerEntity(RefHeader.EMPLOY_TYPE, typeWorkId));
		}
		if (occupationId!=null){
			newEmploy.setOccupationId(refBooks.findByCodeIntegerEntity(RefHeader.PROFESSION_TYPE, occupationId));
		}
		if (durationId!=null){
			newEmploy.setDurationId(refBooks.findByCodeIntegerEntity(RefHeader.INCOME_FREQ_TYPE, durationId));
		} else {
			newEmploy.setDurationId(null);
		}
		if (professionId!=null){
			newEmploy.setProfessionId(refBooks.findByCodeIntegerEntity(RefHeader.ORGANIZATION_BUSINESS, professionId));
		} else {
			newEmploy.setProfessionId(null);
		}
		if (extSalaryId!=null){
			newEmploy.setExtsalaryId(refBooks.findByCodeIntegerEntity(RefHeader.EXT_SALARY_TYPE, extSalaryId));
		} else {
			newEmploy.setExtsalaryId(null);
		}
        if (dateStartWorkId != null){
            newEmploy.setDateStartWorkId(refBooks.findByCodeIntegerEntity(RefHeader.TIME_RANGE, dateStartWorkId));
        } else {
            newEmploy.setDateStartWorkId(null);
        }

		newEmploy.setPlaceWork(placeWork);
	    newEmploy.setOccupation(occupation);

		newEmploy.setSalary(salary);
		newEmploy.setExtsalary(extSalary);
		newEmploy.setExtCreditSum(extCreditSum);
		newEmploy.setExperience(experience);
		newEmploy.setDatestartwork(dateStartWork);
		newEmploy.setNextSalaryDate(nextSalaryDate);
		newEmploy.setCurrent(current);
		emMicro.persist(newEmploy);
		return newEmploy;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PeoplePersonal addPeoplePersonal(PeoplePersonal people,Integer peopleMainId,Date date) throws PeopleException {

		PeoplePersonalEntity peoplePersonal =null;
		//если это не новая запись, надо ее найти в БД
		if (people.getId()!=null){
			peoplePersonal=peopleDAO.getPeoplePersonalEntity(people.getId());
		}
		try {
			PeoplePersonalEntity newPeople=newPeoplePersonal(peoplePersonal,peopleMainId,
					null, Partner.CLIENT, people.getSurname(),people.getName(), people.getMidname(),
					people.getMaidenname(),people.getBirthDate(), people.getBirthPlace(),
					refBooks.referenceCodeIntegerOrNull(people.getGender()),
					date, ActiveStatus.ACTIVE);
			return new PeoplePersonal(newPeople);
		} catch (Exception e) {
			logger.severe("Не удалось сохранить данные по человеку "+peopleMainId+",ошибка "+e);
			throw new PeopleException("Не удалось сохранить данные по человеку "+peopleMainId+",ошибка "+e);
		}

	}

	@Override
	public PeoplePersonal initPeoplePersonal(PeopleMain peopleMain) {
		PeopleMainEntity people=peopleDAO.getPeopleMainEntity(peopleMain.getId());
		PeoplePersonalEntity peoplePersonal = findPeoplePersonalActive(people);
		if (peoplePersonal==null){
			peoplePersonal=new PeoplePersonalEntity();
			peoplePersonal.setPeopleMainId(people);
			peoplePersonal.setIsactive(ActiveStatus.ACTIVE);
			peoplePersonal.setPartnersId(refBooks.getPartnerEntity(Partner.CLIENT));
		}
		return new PeoplePersonal(peoplePersonal);
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Documents addDocument(Documents passport, Integer peopleMainId)
			throws PeopleException {
		DocumentEntity document=null;
		//если это не новая запись, надо ее найти в БД
		if (passport.getId()!=null){
			document=documentDAO.getDocument(passport.getId());
		}
		try {
			DocumentEntity newDocument=newDocument(document,peopleMainId, null,
					Partner.CLIENT, passport.getSeries(), passport.getNumber(), passport.getDocDate(),
					passport.getDocOrgCode(), passport.getDocOrg(), ActiveStatus.ACTIVE);
			return new Documents(newDocument);
		} catch (Exception e) {
			logger.severe("Не удалось сохранить данные паспорта по человеку "+peopleMainId+",ошибка "+e);
			throw new PeopleException("Не удалось сохранить данные паспорта по человеку "+peopleMainId+",ошибка "+e);
		}

	}

	@Override
	public Documents initDocument(PeopleMain peopleMain) {
		PeopleMainEntity people=peopleDAO.getPeopleMainEntity(peopleMain.getId());
		DocumentEntity passport=findPassportActive(people);
		if (passport==null){
			passport=new DocumentEntity();
			passport.setPeopleMainId(people);
			passport.setIsactive(ActiveStatus.ACTIVE);
			passport.setPartnersId(refBooks.getPartnerEntity(Partner.CLIENT));
			passport.setDocumenttypeId(refBooks.findByCodeIntegerEntity(RefHeader.DOC_VER_TYPE, Documents.PASSPORT_RF));
		}
		return new Documents(passport);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PeopleMisc addPeopleMisc(PeopleMisc misc, Integer peopleMainId,
			Date date) throws PeopleException {
		PeopleMiscEntity peopleMisc=null;
		//если это не новая запись, надо ее найти в БД
		if (misc.getId()!=null){
			peopleMisc=peopleDAO.getPeopleMisc(misc.getId());
		}
		try {
			PeopleMiscEntity newMisc=newPeopleMisc(peopleMisc,peopleMainId, null,Partner.CLIENT,
					refBooks.referenceCodeIntegerOrNull(misc.getMarriage()),
					misc.getChildren(), misc.getUnderage(),
                    refBooks.referenceCodeIntegerOrNull(misc.getRealtyDateId()), 
                    misc.getRealtyDate(),
                    refBooks.referenceCodeIntegerOrNull(misc.getRegDateId()),
                    misc.getRegDate(),
					refBooks.referenceCodeIntegerOrNull(misc.getRealty()),
					misc.getCar(),date,ActiveStatus.ACTIVE,misc.getDriverLicense());
			return new PeopleMisc(newMisc);
		} catch (Exception e) {
			logger.severe("Не удалось сохранить доп. данные по человеку "+peopleMainId+",ошибка "+e);
			throw new PeopleException("Не удалось сохранить доп. данные по человеку "+peopleMainId+",ошибка "+e);
		}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PeopleOther addPeopleOther(PeopleOther other, Integer peopleMainId,
                                    Date date) throws PeopleException {
        PeopleOtherEntity peopleOther=null;
        //если это не новая запись, надо ее найти в БД
        if (other.getId()!=null){
            peopleOther=peopleDAO.getPeopleOther(other.getId());
        }
        try {
            PeopleOtherEntity newOther=newPeopleOther(peopleOther, peopleMainId, null,Partner.CLIENT, other.getPublic_role(), other.getPublic_relative_role(),
                    other.getPublic_relative_fio(), other.getBenific_fio(), other.getReclam_accept(), other.getOther(), ActiveStatus.ACTIVE);
            return new PeopleOther(newOther);
        } catch (Exception e) {
            logger.severe("Не удалось сохранить другие доп. данные по человеку "+peopleMainId+",ошибка "+e);
            throw new PeopleException("Не удалось сохранить доп. данные по человеку "+peopleMainId+",ошибка "+e);
        }
    }

	@Override
	public PeopleMisc initPeopleMisc(PeopleMain peopleMain) {
		PeopleMainEntity people=peopleDAO.getPeopleMainEntity(peopleMain.getId());
		PeopleMiscEntity misc=findPeopleMiscActive(people);
		if (misc==null){
			misc=new PeopleMiscEntity();
			misc.setPeopleMainId(people);
		}
		return new PeopleMisc(misc);
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Employment addEmployment(Employment employ, Integer peopleMainId,
			Date date) throws PeopleException{
		EmploymentEntity employment=null;
		if (employ.getId()!=null){
			employment=peopleDAO.getEmployment(employ.getId());
		}
		try{
			EmploymentEntity newEmploy=newEmployment(employment,peopleMainId,null,Partner.CLIENT,
					employ.getPlaceWork(), employ.getOccupation(),
					refBooks.referenceCodeIntegerOrNull(employ.getEducation()),
					refBooks.referenceCodeIntegerOrNull(employ.getTypeWork()),
					refBooks.referenceCodeIntegerOrNull(employ.getDuration()),
					refBooks.referenceCodeIntegerOrNull(employ.getProfession()),
					refBooks.referenceCodeIntegerOrNull(employ.getExtSalaryId()),
					employ.getSalary(), employ.getExtSalary(),employ.getExperience(),
                    refBooks.referenceCodeIntegerOrNull(employ.getDateStartWorkId()),
					employ.getDateStartWork(), Employment.CURRENT,date,employ.getExtCreditSum(),
					employ.getNextSalaryDate(),refBooks.referenceCodeIntegerOrNull(employ.getOccupationId()));
			return new Employment(newEmploy);
		} catch (Exception e) {
			logger.severe("Не удалось сохранить данные занятости по человеку "+peopleMainId+",ошибка "+e);
			throw new PeopleException("Не удалось сохранить данные занятости по человеку "+peopleMainId+",ошибка "+e);
		}
	}

	@Override
	public Employment initEmployment(PeopleMain peopleMain) {
		PeopleMainEntity people=peopleDAO.getPeopleMainEntity(peopleMain.getId());
		EmploymentEntity employ = findEmployment(people, null, refBooks.getPartnerEntity(Partner.CLIENT), Employment.CURRENT);
		if (employ==null){
			employ=new EmploymentEntity();
			employ.setPeopleMainId(people);
		}
		return new Employment(employ);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PeopleContact addPeopleContact(PeopleContact contact,
			Integer peopleMainId) throws PeopleException {
		PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
		PeopleContactEntity peopleContact=setPeopleContactClient(peopleMain, refBooks.referenceCodeIntegerOrNull(contact.getContact()),
				contact.getValue(), contact.getAvailable());
		return new PeopleContact(peopleContact);
	}

	@Override
	public PeopleContact initPeopleContact(PeopleMain peopleMain,Integer contactTypeId) {
		PeopleMainEntity people=peopleDAO.getPeopleMainEntity(peopleMain.getId());
		PeopleContactEntity contact=findPeopleByContactMan(contactTypeId,peopleMain.getId());
		if (contact==null){
			contact=new PeopleContactEntity();
			contact.setPeopleMainId(people);
			contact.setPartnersId(refBooks.getPartnerEntity(Partner.CLIENT));
			contact.setIsactive(ActiveStatus.ACTIVE);
			contact.setAvailable(false);
			contact.setContactId(refBooks.findByCodeIntegerEntity(RefHeader.CONTACT_TYPE, contactTypeId));
		}
		return new PeopleContact(contact);
	}

	@Override
	public FiasAddress initAddress(PeopleMain peopleMain, Integer addressTypeId) {
		PeopleMainEntity people=peopleDAO.getPeopleMainEntity(peopleMain.getId());
		AddressEntity address=findAddressActive(people,addressTypeId);
		if (address==null){
			address=new AddressEntity();
			address.setAddrtype(refBooks.findByCodeIntegerEntity(RefHeader.ADDRESS_TYPE, addressTypeId));
			address.setPeopleMainId(people);
			address.setIsactive(ActiveStatus.ACTIVE);
			address.setPartnersId(refBooks.getPartnerEntity(Partner.CLIENT));
		}
		return new FiasAddress(address);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public FiasAddress addAddressFias(FiasAddress address,
			Integer peopleMainId, String fiasGuid,Date date) throws PeopleException {
		AddressEntity addressEntity=null;
		if (address.getId()!=null){
			addressEntity=addressBean.getAddress(address.getId());
		}
		try {
			AddressEntity newAddress=newAddressFias(addressEntity,peopleMainId,null,Partner.CLIENT,
			    refBooks.referenceCodeIntegerOrNull(address.getAddrtype()),null,date,null,null,
			    ActiveStatus.ACTIVE,fiasGuid,address.getHouse(),address.getCorpus(),address.getBuilding(),address.getFlat());
			return new FiasAddress(newAddress);
		} catch (Exception e) {
			logger.severe("Не удалось сохранить данные адреса по человеку "+peopleMainId+",ошибка "+e);
			throw new PeopleException("Не удалось сохранить данные адреса по человеку "+peopleMainId+",ошибка "+e);
		}

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PeopleMainEntity savePeopleMain(PeopleMainEntity peopleMain,String inn, String snils) throws PeopleException {
		 PeopleMainEntity peopleMainEntity=null;
		 if (peopleMain!=null){
			 peopleMainEntity=peopleMain;
		 } else {
			 peopleMainEntity=new PeopleMainEntity();
		 }
		 int cnt = 0;
	     if (StringUtils.isNotBlank(inn)) {
	        if (CheckUtils.CheckInn(inn)) {
	            PeopleMainEntity ppmain = findPeopleMain(inn, null);
	            if (ppmain != null) {
	                cnt = kassa.findManWithPrivateCabinet(ppmain);
	                //этот человек уже был
	                if (cnt != 0) {
	                   logger.info("человек с инн "+inn+" уже есть "+ppmain.getId());
	                }
	            } else {
                    // ставим ИНН человеку
                    peopleMainEntity.setInn(inn);
	            }
	        }//end if inn correct
	    }

	    if (StringUtils.isNotBlank(snils)) {
	        if (CheckUtils.CheckSnils(Convertor.fromMask(snils))) {
	            PeopleMainEntity ppmain = findPeopleMain(null, Convertor.fromMask(snils));
	            if (ppmain != null) {
	                cnt = kassa.findManWithPrivateCabinet(ppmain);
	                //этот человек уже был
	                if (cnt != 0) {
	                   logger.info("человек со снилс "+snils+" уже есть "+ppmain.getId());
	                }
	            } else {
                    // устанаваливаем СНИЛС человеку
                    peopleMainEntity.setSnils(Convertor.fromMask(snils));
	            }
	        }//end if snils correct

	    }

	    peopleMainEntity=emMicro.merge(peopleMainEntity);
	    emMicro.persist(peopleMainEntity);
		return peopleMainEntity;
	}

	@Override
	public boolean phoneClientExists(Integer peopleMainId, String value) {
		List<PeopleContactEntity> lst=findPeopleByContact(null, value, peopleMainId, Partner.CLIENT,null,null);
		if (lst.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean phoneExists(Integer peopleMainId, String value) {
		List<PeopleContactEntity> lst=findPeopleByContact(null, value, peopleMainId, null,null,null);
		if (lst.size()>0){
			return true;
		}
		return false;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CallBackEntity newCallBack(String surname, String name, String phone,
                                      String email, Date dateRequest,String message,Integer type) throws PeopleException {
		CallBackEntity callBack=new CallBackEntity();
		callBack.setSurname(Convertor.toRightString(surname));
		callBack.setName(Convertor.toRightString(name));
		callBack.setPhone(Convertor.fromMask(phone));
		callBack.setEmail(email);
		callBack.setMessage(message);
		callBack.setIsActive(ActiveStatus.DRAFT);
		if (dateRequest==null){
			callBack.setDateRequest(new Date());
		} else {
			callBack.setDateRequest(dateRequest);
		}
		if (type!=null){
			callBack.setTypeId(refBooks.findByCodeIntegerEntity(RefHeader.CALLBACK_CLIENT_TYPE, type));
		}
		emMicro.persist(callBack);
		return callBack;
	}

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CallBack addCallBack(CallBack callBack) throws PeopleException {
		CallBackEntity newCallBack = newCallBack(callBack.getSurname(), callBack.getName(),
                callBack.getPhone(), callBack.getEmail(), null, callBack.getMessage(),
                refBooks.referenceCodeIntegerOrNull(callBack.getType()));

        Map<String, Object> params = rulesBeanLocal.getCallbackConstants();
        Boolean sendNotification = Utils.booleanFromNumber(params.get(RulesKeys.CALLBACK_ENABLED));
        if (sendNotification) {
            String email = Convertor.toString(params.get(RulesKeys.CALLBACK_EMAIL));
            mailBeanLocal.send("Обратная связь",
                    callBack.getSurname() + " " + callBack.getName() + "\n" +
                            "Телефон: " + callBack.getPhone() + "\n" +
                            "Email: " + callBack.getEmail(),
                    email
            );
        }

		return new CallBack(newCallBack);
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CallBack saveCallBackWithUser(CallBack callBack, Integer userId) {
		CallBackEntity oldCallBack=peopleDAO.getCallBackEntity(callBack.getId());
		oldCallBack.setIsActive(ActiveStatus.ACTIVE);
		UsersEntity user=userDAO.getUsersEntity(userId);
		oldCallBack.setUserId(user);
		oldCallBack.setDateCall(new Date());
		emMicro.merge(oldCallBack);
		try {
		  eventLog.saveLog(EventType.INFO, EventCode.CALLBACK_TAKEN,
				  "Взята в работу заявка на обратную связь по человеку "+callBack.getSurname()+" телефон "+callBack.getPhone(),
				  userId,new Date(), null, null, null);
		} catch (Exception e){
			logger.severe("Не удалось сохранить лог по взятию в работу заявки на обратный звонок "+callBack.getId()+" пользователем "+userId);
		}
		return new CallBack(oldCallBack);
	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<CallBack> listCallBack(int nFirst, int nCount, String surname,
                                       String name, String midname, DateRange dateRequest,
                                       DateRange dateCall, Integer isActive, Set options) {
        String sql = " from ru.simplgroupp.persistence.CallBackEntity c where (1=1) ";
        if (StringUtils.isNotEmpty(surname)) {
            sql+= " and ((select count(*) from c.peopleMainId.peoplepersonal as p where (upper(p.surname) like :surname))>0 )";
        }
        if (StringUtils.isNotEmpty(name)) {
            sql+= " and ((select count(*) from c.peopleMainId.peoplepersonal as p where (upper(p.name) like :name))>0 )";
        }
        if (StringUtils.isNotEmpty(midname)) {
            sql+= " and ((select count(*) from c.peopleMainId.peoplepersonal as p where (upper(p.midname) like :midname))>0 )";
        }
        if (isActive!=null){
            sql+=" and c.isActive=:isActive ";
        }
        if (dateRequest != null && dateRequest.getFrom() != null){
            sql+=" and (c.dateRequest>=:datefrom) ";
        }
        if (dateRequest != null && dateRequest.getTo() != null){
            sql+=" and (c.dateRequest<:dateto) ";
        }
        if (dateCall != null && dateCall.getFrom() != null){
            sql+=" and (c.dateCall>=:datecallfrom) ";
        }
        if (dateCall != null && dateCall.getTo() != null){
            sql+=" and (c.dateCall<:datecallto) ";
        }
        sql+=" order by c.dateRequest desc";
        Query qry = emMicro.createQuery(sql);
        if (StringUtils.isNotEmpty(surname)){
            qry.setParameter("surname","%"+surname.toUpperCase()+"%" );
        }
        if (StringUtils.isNotEmpty(name)){
            qry.setParameter("name", "%"+name+"%");
        }
        if (StringUtils.isNotEmpty(midname)){
            qry.setParameter("midname", "%"+midname+"%");
        }
        if (isActive!=null){
            qry.setParameter("isActive", isActive);
        }
        if (dateRequest != null && dateRequest.getFrom() != null){
            qry.setParameter("datefrom", dateRequest.getFrom(),TemporalType.DATE);
        }
        if (dateRequest != null && dateRequest.getTo() != null){
            qry.setParameter("dateto", DateUtils.addDays(dateRequest.getTo(),1),TemporalType.DATE);
        }
        if (dateCall != null && dateCall.getFrom() != null){
            qry.setParameter("datecallfrom", dateCall.getFrom(),TemporalType.DATE);
        }
        if (dateCall != null && dateCall.getTo() != null){
            qry.setParameter("datecallto", DateUtils.addDays(dateCall.getTo(),1),TemporalType.DATE);
        }

        if (nFirst >= 0){
            qry.setFirstResult(nFirst);
        }
        if (nCount > 0){
            qry.setMaxResults(nCount);
        }
        List<CallBackEntity> lst=qry.getResultList();
        if (lst.size()>0){
            List<CallBack> lst1=new ArrayList<CallBack>(lst.size());
            for (CallBackEntity call:lst) {
                CallBack callBack = new CallBack(call);
                callBack.init(options);
                lst1.add(callBack);
            }
            return lst1;
        }
        return new ArrayList<CallBack>(0);
    }

	@Override
	public List<CallBack> listCallBack(int nFirst, int nCount,
			SortCriteria[] sorting, String surname, String userName,
			String phone, String email, DateRange dateRequest,
			DateRange dateCall, Integer isActive,Integer type) {
		String sql = " from ru.simplgroupp.persistence.CallBackEntity where (1=1) ";
		if (StringUtils.isNotEmpty(surname)){
			sql+=" and upper(surname) like :surname ";
		}
		if (StringUtils.isNotEmpty(phone)){
			sql+=" and phone=:phone ";
		}
		if (StringUtils.isNotEmpty(email)){
			sql+=" and email=:email ";
		}
		if (isActive!=null){
			sql+=" and isActive=:isActive ";
		}
		if (dateRequest != null && dateRequest.getFrom() != null){
    		sql+=" and (dateRequest>=:datefrom) ";
    	}
    	if (dateRequest != null && dateRequest.getTo() != null){
    		sql+=" and (dateRequest<:dateto) ";
    	}
    	if (dateCall != null && dateCall.getFrom() != null){
    		sql+=" and (dateCall>=:datecallfrom) ";
    	}
    	if (dateCall != null && dateCall.getTo() != null){
    		sql+=" and (dateCall<:datecallto) ";
    	}
    	if (type!=null&&type>0){
    		sql+=" and typeId.codeinteger=:typeId ";
    	}
    	sql+=" order by dateRequest desc";
    	Query qry = emMicro.createQuery(sql);
    	if (StringUtils.isNotEmpty(surname)){
			qry.setParameter("surname","%"+surname.toUpperCase()+"%" );
		}
		if (StringUtils.isNotEmpty(phone)){
			qry.setParameter("phone", phone);
		}
		if (StringUtils.isNotEmpty(email)){
			qry.setParameter("email", email);
		}
		if (isActive!=null){
			qry.setParameter("isActive", isActive);
		}
		if (dateRequest != null && dateRequest.getFrom() != null){
			qry.setParameter("datefrom", dateRequest.getFrom(),TemporalType.DATE);
    	}
    	if (dateRequest != null && dateRequest.getTo() != null){
    		 qry.setParameter("dateto", DateUtils.addDays(dateRequest.getTo(),1),TemporalType.DATE);
    	}
    	if (dateCall != null && dateCall.getFrom() != null){
    		qry.setParameter("datecallfrom", dateCall.getFrom(),TemporalType.DATE);
    	}
    	if (dateCall != null && dateCall.getTo() != null){
    		 qry.setParameter("datecallto", DateUtils.addDays(dateCall.getTo(),1),TemporalType.DATE);
    	}
    	if (type!=null&&type>0){
			qry.setParameter("typeId", type);
		}

    	if (nFirst >= 0){
			qry.setFirstResult(nFirst);
    	}
		if (nCount > 0){
			qry.setMaxResults(nCount);
		}
		List<CallBackEntity> lst=qry.getResultList();
		if (lst.size()>0){
			List<CallBack> lst1=new ArrayList<CallBack>(lst.size());
			for (CallBackEntity call:lst) {
	    		CallBack callBack = new CallBack(call);
	    		lst1.add(callBack);
	    	}
			return lst1;
		}
		return new ArrayList<CallBack>(0);
	}

	@Override
	public int countCallBack(String surname, String userName, String phone,
			String email, DateRange dateRequest, DateRange dateCall,
			Integer isActive,Integer type) {
		return listCallBack(0,0,null,surname,userName,phone,email,dateRequest,dateCall,isActive,type).size();
	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int countCallBack(String surname, String name,
                             String midname, DateRange dateRequest, DateRange dateCall,
                             Integer isActive, Set options) {
        return listCallBack(0,0,surname,name,midname,dateRequest,dateCall,isActive, options).size();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void changeCallBackStatus(CallBack callBack, Integer status) {
        CallBackEntity entity = peopleDAO.getCallBackEntity(callBack.getId());
        entity.setIsActive(status);
        entity = emMicro.merge(entity);
        emMicro.persist(entity);
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean updateFriend(Integer friendId, String name, String surname, String email,
                                String phone, Boolean available) {

        PeopleFriendEntity peopleFriendEntity = peopleDAO.getPeopleFriend(friendId);

        if (peopleFriendEntity == null) {
            return false;
        }

        peopleFriendEntity.setName(name);
        peopleFriendEntity.setSurname(surname);
        peopleFriendEntity.setEmail(email);
        peopleFriendEntity.setPhone(phone);
        peopleFriendEntity.setAvailable(available);
        emMicro.merge(peopleFriendEntity);

        return true;
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public AddressEntity saveAddressDataWithStrings(AddressEntity address,
			String regionCode, String regionName,String areaName, String areaExt,
			String cityName, String cityExt, String placeName, String placeExt,
			String streetName, String streetExt) {
		if (address!=null){
			if (StringUtils.isNotEmpty(regionCode)){
				address.setRegionShort(refBooks.getRegions(regionCode));
			}
			if (StringUtils.isNotEmpty(regionName)){
			    address.setRegionName(regionName);
			} else if (address.getRegionShort()!=null){
				address.setRegionName(address.getRegionShort().getName());
			}
			address.setAreaName(areaName);
			address.setAreaExt(areaExt);
			address.setCityName(cityName);
			address.setCityExt(cityExt);
			address.setPlaceName(placeName);
			address.setPlaceExt(placeExt);
			address.setStreetName(streetName);
			address.setStreetExt(streetExt);
			address.setAddressText("");
			address.setAddressText(address.getDescription());
			emMicro.persist(address);
			return address;
		}
		return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public OfficialDocumentsEntity newOfficialDocument(
			OfficialDocumentsEntity document, Integer peopleMainId,
			Integer creditRequestId, Integer creditId,
			String docNumber, Date docDate, String smsCode, String docText,
			Integer active, Integer anotherId,Integer typeId) {
		if (document==null){
			document=new OfficialDocumentsEntity();
		    if (peopleMainId!=null){
			    PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
			    document.setPeopleMainId(peopleMain);
			}
		    if (creditId!=null){
		    	CreditEntity credit=creditDAO.getCreditEntity(creditId);
		    	document.setCreditId(credit);
		    }
		    if (creditRequestId!=null){
		    	CreditRequestEntity creditRequest=creditRequestDAO.getCreditRequestEntity(creditRequestId);
		    	document.setCreditRequestId(creditRequest);
		    }
		}
		document.setAnotherId(anotherId);
		document.setIsActive(active);
		document.setDocumentTypeId(refBooks.findByCodeIntegerEntity(RefHeader.OFFICIAL_DOCUMENT_TYPE, typeId));
		document.setDocNumber(docNumber);
		document.setDocDate(docDate);
		document.setSmsCode(smsCode);
		document.setDocText(docText);
		document.setDateChange(new Date());
		emMicro.persist(document);
		return document;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeOfficialDocument(OfficialDocuments document,
			Integer peopleId,Integer creditRequestId,Integer creditId,Date dateChange) {

		    OfficialDocumentsEntity ofDocument=officialDocumentDAO.getOfficialDocumentEntity(document.getId());

	        //если ничего не менялось, возвращаемся
	        if (ofDocument != null) {
	        	if (document.equalsContent(ofDocument)) {
	        		return;
	        	}
	            //поставим в предыдущую запись недействительность
	        	ofDocument.setIsActive(ActiveStatus.ARCHIVE);
	        	emMicro.merge(ofDocument);
	        }

	        //запишем новые данные
	        newOfficialDocument(null, peopleId,creditRequestId,creditId,
	    			document.getDocNumber(), dateChange, document.getSmsCode(), document.getDocText(),
	    			ActiveStatus.ACTIVE, document.getAnotherId(),refBooks.referenceCodeIntegerOrNull(document.getDocumentType()));

	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public OfficialDocuments initOfficialDocument(PeopleMain peopleMain,
			Integer creditRequestId,Integer creditId,Integer typeId) {

		 PeopleMainEntity people=peopleDAO.getPeopleMainEntity(peopleMain.getId());
		 OfficialDocumentsEntity document=null;
		 if (creditRequestId==null&&creditId==null){
			 document=officialDocumentDAO.findOfficialDocumentDraft(peopleMain.getId(), typeId);
		 } else if (creditRequestId!=null&&creditId==null){
			 document=officialDocumentDAO.findOfficialDocumentCreditRequestDraft(peopleMain.getId(), creditRequestId, typeId);
		 } else if (creditRequestId==null&&creditId!=null){
			 document=officialDocumentDAO.findOfficialDocumentCreditDraft(peopleMain.getId(), creditId, typeId);
		 }
	     if (document==null){

			document=new OfficialDocumentsEntity();
			document.setPeopleMainId(people);
			if (creditRequestId!=null){
				document.setCreditRequestId(creditRequestDAO.getCreditRequestEntity(creditRequestId));
			}
			if (creditId!=null){
				document.setCreditId(creditDAO.getCreditEntity(creditId));
			}
			document.setDocumentTypeId(refBooks.findByCodeIntegerEntity(RefHeader.OFFICIAL_DOCUMENT_TYPE, typeId));
			document.setDocDate(new Date());
			document.setIsActive(ActiveStatus.DRAFT);
		}
		return new OfficialDocuments(document);
	}

	@Override
	public Account initAccount(PeopleMain peopleMain, Integer accountType) {
		PeopleMainEntity people=peopleDAO.getPeopleMainEntity(peopleMain.getId());
		AccountEntity account=findAccountActive(people,accountType);
		if (account==null){
			account=new AccountEntity();
			account.setAccountTypeId(refBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, accountType));
			account.setPeopleMainId(people);
			account.setIsactive(ActiveStatus.ACTIVE);
			account.setDateAdd(new Date());
		}
		return new Account(account);
	}

	@Override
	public AddressEntity findLastArchiveAddress(Integer peopleMainId,Integer addrType) {
		return findAddress(peopleDAO.getPeopleMainEntity(peopleMainId), Partner.CLIENT, null, addrType, ActiveStatus.ARCHIVE);
	}

	@Override
	public EmploymentEntity findLastArchiveEmployment(Integer peopleMainId) {
		return findEmployment(peopleDAO.getPeopleMainEntity(peopleMainId),null,
				refBooks.getPartnerEntity(Partner.CLIENT),Employment.RECENT);
	}

	@Override
	public DocumentEntity findLastArchivePassport(Integer peopleMainId) {
		return findDocument(peopleDAO.getPeopleMainEntity(peopleMainId),null,Partner.CLIENT,ActiveStatus.ARCHIVE, Documents.PASSPORT_RF);
	}

	@Override
	public PeopleContactEntity findLastArchiveContact(Integer peopleMainId,
			Integer contactType) {
		  List<PeopleContactEntity> lstCon = findPeopleByContact(contactType, null, peopleMainId, Partner.CLIENT, ActiveStatus.ARCHIVE, null);
	      return (PeopleContactEntity) Utils.firstOrNull(lstCon);
	}

	@Override
	public PeoplePersonalEntity findLastArchivePersonalData(Integer peopleMainId) {
		  List<PeoplePersonalEntity> lst = findPeoplePersonal(peopleDAO.getPeopleMainEntity(peopleMainId),
				  null, Partner.CLIENT, ActiveStatus.ARCHIVE);
	      return (PeoplePersonalEntity) Utils.firstOrNull(lst);
	}

	@Override
	public Integer findContactTypeForLogin() {
		 Map<String, Object> login_const = rulesBean.getLoginConstants();
	     String login_way = (String) login_const.get(SettingsKeys.LOGIN_WAY);
	     Integer login_number = PeopleContact.CONTACT_EMAIL;
	     if (login_way.equalsIgnoreCase(SettingsKeys.LOGIN_PHONE)) {
	         login_number = PeopleContact.CONTACT_CELL_PHONE;
	     }
	     return login_number;
	}

    @Override
    public PeopleMainEntity findPeopleByPhone(String phone) {
        Query qry = emMicro.createNamedQuery("findPeopleByPhone");
        qry.setParameter("phone", phone);
        qry.setMaxResults(1);
        List<PeopleContactEntity> lst = qry.getResultList();
        if (lst.size() == 0) {
            return null;
        } else {
            return lst.get(0).getPeopleMainId();
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PeopleMain savePeopleIin(PeopleMainEntity peopleMain, String iin) {
        PeopleMainEntity entity;
        if (peopleMain != null) {
            entity = peopleMain;
        } else {
            entity = new PeopleMainEntity();
        }
        if (StringUtils.isNotEmpty(iin)) {
            if (CheckUtils.checkIin(iin)) {
                entity.setIin(iin);
            }
        }
        entity = emMicro.merge(entity);
        emMicro.persist(entity);

        return new PeopleMain(entity);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<PeoplePersonal> getPeoplePersonalByRoleIDAndEventCodeID(Integer eventCode, Integer roleID) {
        List<Integer> peopleMainIDs = peopleDAO.getPeopleMainIDsByRoleIDAndEventCodeID(eventCode, roleID);
        List<PeoplePersonalEntity> list = peopleDAO.getPeoplePersonalByPeopleMainIDs(peopleMainIDs);
        List<PeoplePersonal> result = new ArrayList<PeoplePersonal>();
        for (PeoplePersonalEntity entity : list) {
            result.add(new PeoplePersonal(entity));
        }
        return result;
    }
}
