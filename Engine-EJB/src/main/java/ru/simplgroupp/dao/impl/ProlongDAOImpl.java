package ru.simplgroupp.dao.impl;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ru.simplgroupp.dao.interfaces.ProlongDAO;
import ru.simplgroupp.persistence.ProlongEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.Prolong;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ProlongDAOImpl implements ProlongDAO {
	
    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    
	@Override
	public ProlongEntity getProlongEntity(int prolongId) {
		return emMicro.find(ProlongEntity.class, prolongId);
	}
	
	@Override
	public Prolong getProlong(int prolongId, Set options) {
		ProlongEntity ent = getProlongEntity(prolongId);
		if (ent == null) {
			return null;
		} 
		Prolong pr = new Prolong(ent);
		pr.init(options);
		return pr;
	}    
    
	@Override
	public void deleteProlong(int id) {
		 Query qry = emMicro.createNamedQuery("deleteProlong");
		 qry.setParameter("id", id);
	     qry.executeUpdate();		
	}
	
	@Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ProlongEntity> findProlong(int creditId, DateRange longDate, Integer isActive) {
		String hql = "from ru.simplgroupp.persistence.ProlongEntity where (creditId.id = :creditId) ";
		if (isActive!=null){
			hql=hql+" and isactive=:isActive ";
		}
		if (longDate!=null && longDate.getFrom()!=null){
			hql = hql + " and (longdate >= :longDateFrom)";
		}
		if (longDate!=null && longDate.getTo()!=null){
			hql = hql + " and (longdate <= :longDateTo)";
		}
		hql+=" order by longDate desc";
		Query qry = emMicro.createQuery(hql);
		qry.setParameter("creditId", creditId);
		if (isActive!=null){
			qry.setParameter("isActive", isActive);
		}
		if (longDate!=null && longDate.getFrom()!=null){
			qry.setParameter("longDateFrom", longDate.getFrom());
		}
		if (longDate!=null && longDate.getTo()!=null){
			qry.setParameter("longDateTo", longDate.getTo());
		}
		List<ProlongEntity> lst=qry.getResultList();
		
		return lst;
	}
	
	@Override
	public ProlongEntity findProlongDraft(int creditId) {
		List<ProlongEntity> lst = findProlong(creditId,null,ActiveStatus.DRAFT);
		return (ProlongEntity) Utils.firstOrNull(lst);
	}	
	
	@Override
	public ProlongEntity saveProlongEntity(ProlongEntity prolong) {
		 ProlongEntity prolongNew = emMicro.merge(prolong);
		 emMicro.persist(prolongNew);    	
		 return prolongNew;
	}
}
