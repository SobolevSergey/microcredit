package ru.simplgroupp.dao.impl;

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

import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.PhonePaySummaryDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PhonePaySummaryEntity;
import ru.simplgroupp.transfer.RefHeader;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PhonePaySummaryDAOImpl implements PhonePaySummaryDAO {

	@PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    
    @EJB
    PeopleDAO peopleDAO;
	
	@EJB
	CreditRequestDAO creditRequestDAO;
	
	@EJB
	CreditDAO creditDAO;
	
	@EJB
    ReferenceBooksLocal refBooks;
	
	@Override
	public List<PhonePaySummaryEntity> findPhonePaySummary(	PeopleMainEntity pmain, 
			CreditRequestEntity cre, PartnersEntity partner) {
		String hql = "from ru.simplgroupp.persistence.PhonePaySummaryEntity where (1=1)";
        if (pmain != null) {
            hql = hql + " and (peopleMainId.id = :peopleMainId)";
        }
        if (cre != null) {
        	hql = hql + " and (creditRequestId.id = :creditRequest)";
        }
        if (partner != null) {
            hql = hql + " and (partnersId.id = :partner)";
        }

        Query qry = emMicro.createQuery(hql);

        if (pmain != null) {
            qry.setParameter("peopleMainId", pmain.getId());
        }
        if (cre != null) {
            qry.setParameter("creditRequest", cre.getId());
        }
        if (partner != null) {
            qry.setParameter("partner", partner.getId());
        }

        return qry.getResultList();
        
	}

	@Override
	public PhonePaySummaryEntity savePhonePaySummary(Integer creditRequestId,
			Integer peopleMainId, Integer partnerId, Integer paymentType,
			String paymentPeriodType, Integer periodCount) {
		PhonePaySummaryEntity phoneSummary=new PhonePaySummaryEntity();
		if (creditRequestId!=null){
			phoneSummary.setCreditRequestId(creditRequestDAO.getCreditRequestEntity(creditRequestId));
		}
		if (partnerId!=null){
			phoneSummary.setPartnersId(refBooks.getPartnerEntity(partnerId));
		}
		if (peopleMainId!=null){
			phoneSummary.setPeopleMainId(peopleDAO.getPeopleMainEntity(peopleMainId));
		}
		phoneSummary.setPaymentType(refBooks.findByCodeIntegerEntity(RefHeader.PHONE_PAY_DETAIL, paymentType));
		phoneSummary.setPaymentPeriodType(refBooks.findByCodeEntity(RefHeader.PHONE_PAY_SUMMARY_TYPE, paymentPeriodType));
		phoneSummary.setPeriodCount(periodCount);
		emMicro.persist(phoneSummary);
		return phoneSummary;
	}

	@Override
	public PhonePaySummaryEntity getPhonePaySummary(Integer id) {
		return emMicro.find(PhonePaySummaryEntity.class, id);
	}
}
