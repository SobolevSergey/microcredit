package ru.simplgroupp.dao.impl;

import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.DebtDao;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.DebtEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Debt;
import ru.simplgroupp.transfer.RefHeader;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DebtDaoImpl implements DebtDao{
	
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
    public List<DebtEntity> getDebtsWithDecisionDate(Date start, Date end) {
        return JPAUtils.getResultList(emMicro, "findDebtsWithDecisionDebt", Utils.mapOf("start", start, "end", end));
    }
    
    @Override
	public void saveDebt(Debt debt) throws KassaException {
		DebtEntity debtEntity=emMicro.merge(debt.getEntity());
		emMicro.persist(debtEntity);
		
	}
    
    @Override
	public DebtEntity newDebt(Integer creditRequestId,Integer peopleMainId, Integer partnerId,
			Integer creditId,Double amount, Double amountPenalty,String nameDebt,
			String authorityName, Date dateDebt, String debtNumber,
			String comment,String typeDebt,Integer authorityCode,
			Integer authorityId,Date dateDecision,Integer isActive) throws KassaException {
		
		DebtEntity debt=new DebtEntity();
		CreditRequestEntity creditRequest=creditRequestDAO.getCreditRequestEntity(creditRequestId);
		debt.setCreditRequestId(creditRequest);
		PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
		debt.setPeopleMainId(peopleMain);
		PartnersEntity partners=refBooks.getPartnerEntity(partnerId);
		debt.setPartnersId(partners);
		if (creditId!=null){
			CreditEntity credit=creditDAO.getCreditEntity(creditId);
			debt.setCreditId(credit);
		}
		debt.setAmount(amount);
		debt.setAmountPenalty(amountPenalty);
		debt.setAuthorityName(authorityName);
		debt.setNameDebt(nameDebt);
		if (authorityId!=null){
		   debt.setAuthorityId(refBooks.findByCodeIntegerEntity(RefHeader.AUTHORITY_TYPE, authorityId));
		}
		debt.setDateDebt(dateDebt);
		debt.setDebtNumber(debtNumber);
		debt.setComment(comment);
		debt.setTypeDebt(typeDebt);
		debt.setDateDecision(dateDecision);
		debt.setAuthorityCode(authorityCode);
		debt.setIsActive(isActive);
		debt=emMicro.merge(debt);
		emMicro.persist(debt);
		return debt;
	}
    
	@Override
    public List<DebtEntity> listDebts(Integer creditRequestId,
			Integer peopleMainId, Integer partnerId,Integer organizationId,
			Integer authorityId, DateRange dateDebt,DateRange dateDecision) {
		String hql="from ru.simplgroupp.persistence.DebtEntity where (1=1)";
		if (creditRequestId!=null){
			hql=hql+" and creditRequestId.id=:creditRequestId ";
		}
		if (peopleMainId!=null){
			hql=hql+" and peopleMainId.id=:peopleMainId ";
		}
		if (partnerId!=null){
			hql=hql+" and partnersId.id=:partnerId ";
		}
		if (organizationId!=null){
			hql=hql+" and organizationId.id=:organizationId ";
		}
		if (authorityId!=null){
			hql=hql+" and authorityId.codeinteger=:authorityId ";
		}
	    if (dateDebt != null && dateDebt.getFrom() != null) {
		   	hql+= " and (dateDebt >= :dateDebtFrom)";
		}
		if (dateDebt != null && dateDebt.getTo() != null) {
		   	hql+= " and (dateDebt <= :dateDebtTo)";
		}
		if (dateDecision != null && dateDecision.getFrom() != null) {
		   	hql+= " and (dateDecision >= :dateDecisionFrom)";
		}
		if (dateDecision != null && dateDecision.getTo() != null) {
		   	hql+= " and (dateDecision <= :dateDecisionTo)";
		}
		Query qry=emMicro.createQuery(hql);
		if (creditRequestId !=null) {
			qry.setParameter("creditRequestId", creditRequestId);
		}
		if (peopleMainId !=null) {
			qry.setParameter("peopleMainId", peopleMainId);
		}
		if (partnerId !=null) {
			qry.setParameter("partnerId", partnerId);
		}
		if (authorityId !=null) {
			qry.setParameter("authorityId", authorityId);
		}
		if (organizationId !=null) {
			qry.setParameter("organizationId", organizationId);
		}
		if (dateDebt != null && dateDebt.getFrom() != null) {
        	qry.setParameter("dateDebtFrom", dateDebt.getFrom());
        }
        if (dateDebt != null && dateDebt.getTo() != null) {
        	qry.setParameter("dateDebtTo", dateDebt.getTo());
        }
        if (dateDecision != null && dateDecision.getFrom() != null) {
        	qry.setParameter("dateDecisionFrom", dateDecision.getFrom());
        }
        if (dateDecision != null && dateDecision.getTo() != null) {
        	qry.setParameter("dateDecisionTo", dateDecision.getTo());
        }
		List<DebtEntity> lst=qry.getResultList();
		return lst;
	}

	@Override
	public DebtEntity getDebtEntity(Integer debtId) {
		return emMicro.find(DebtEntity.class, debtId);
	}

	@Override
	public DebtEntity findDebt(Integer peopleMainId, Integer partnerId,
			Double amount, Integer authorityId,Date dateDecision) {
		String hql="from ru.simplgroupp.persistence.DebtEntity where peopleMainId.id=:peopleMainId and partnersId.id=:partnerId and amount=:amount ";
		if (dateDecision!=null){
			hql=hql+" and dateDecision=:dateDecision ";
		}
		if (authorityId!=null){
			hql=hql+" and authorityId.codeinteger=:authorityId ";
		}
		Query qry=emMicro.createQuery(hql);
		qry.setParameter("peopleMainId", peopleMainId);
		qry.setParameter("partnerId", partnerId);
		qry.setParameter("amount", amount);
		if (authorityId !=null) {
			qry.setParameter("authorityId", authorityId);
		}
		if (dateDecision!=null){
			qry.setParameter("dateDecision", dateDecision);
		}
		return (DebtEntity) Utils.firstOrNull(qry.getResultList());
	}
}
