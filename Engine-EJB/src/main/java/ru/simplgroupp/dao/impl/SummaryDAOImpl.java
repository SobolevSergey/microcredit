package ru.simplgroupp.dao.impl;

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

import ru.simplgroupp.dao.interfaces.SummaryDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.RefSummaryEntity;
import ru.simplgroupp.persistence.SummaryEntity;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SummaryDAOImpl implements SummaryDAO{

	@PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
   	
	@EJB
    ReferenceBooksLocal refBooks;
	
	@Override
	public void saveSummary(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain,
			PartnersEntity partner, String value, RefSummaryEntity ref, Integer refid,Date summaryDate) {
		SummaryEntity sume=new SummaryEntity();
		sume.setCreditRequestId(creditRequest);
		sume.setPeopleMainId(peopleMain);
		sume.setPartnersId(partner);
		sume.setValue(value);
		sume.setFieldRef(ref);
		if (refid!=null) {
			sume.setRefid(refid);
		}
		sume.setSummaryDate(summaryDate);
		sume=emMicro.merge(sume);
		emMicro.persist(sume);
		
	}

	@Override
	public SummaryEntity getSummaryEntity(Integer summaryId) {
		return emMicro.find(SummaryEntity.class, summaryId);
	}
	
	@Override
	public List<SummaryEntity> findSummary(PeopleMainEntity pmain,	CreditRequestEntity cre, 
			PartnersEntity partner,	RefSummaryEntity ref) {
		String hql = "from ru.simplgroupp.persistence.SummaryEntity where (1=1)";
        if (pmain != null) {
            hql = hql + " and (peopleMainId.id = :peopleMainId)";
        }
        if (cre != null) {
        	hql = hql + " and (creditRequestId.id = :creditRequest)";
        }
        if (partner != null) {
            hql = hql + " and (partnersId.id = :partner)";
        }
        if (ref != null) {
            hql = hql + " and (fieldRef.id = :ref)";
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
        if (ref != null) {
            qry.setParameter("ref", ref.getId());
        }
        List<SummaryEntity> lst=qry.getResultList();
        if (lst.size()>0){
        	return lst;
        } else {
        	return null;
        }
	}

}
