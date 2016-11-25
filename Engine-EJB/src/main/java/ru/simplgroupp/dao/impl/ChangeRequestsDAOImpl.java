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

import ru.simplgroupp.dao.interfaces.ChangeRequestsDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.UsersDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.ChangeRequestsEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.UsersEntity;
import ru.simplgroupp.transfer.ChangeRequests;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ChangeRequestsDAOImpl implements ChangeRequestsDAO{

	@PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
	
	@EJB
    ReferenceBooksLocal refBooks;
	
	@EJB
	CreditRequestDAO creditRequestDAO;
	
	@EJB
	UsersDAO usersDAO;
	
	@Override
	public List<ChangeRequests> listChangeRequests(Integer creditRequestId,
				Integer isEdited, Date date, Integer userId) {

		String hql="from ru.simplgroupp.persistence.ChangeRequestsEntity where (1=1)";
		if (creditRequestId!=null){
			hql=hql+" and creditRequestId.id=:creditRequestId ";
		}
		if (isEdited!=null){
			hql=hql+" and isEdited=:isEdited ";
		}	
		if (userId!=null){
			hql=hql+" and userId.id=:userId ";
		}	
		if (date!=null){
			hql=hql+" and requestDate=:requestDate ";
		}	
		Query qry=emMicro.createQuery(hql);
		if (creditRequestId !=null) {
			qry.setParameter("creditRequestId", creditRequestId);
		}
		if (isEdited != null) {
			qry.setParameter("isEdited", isEdited);
		}
		if (userId!=null){
			qry.setParameter("userId", userId);
		}
		if (date!=null) {
			qry.setParameter("requestDate", date);
		}
		List<ChangeRequestsEntity> lst = qry.getResultList();
		if (lst.size() > 0) {
		    List<ChangeRequests> lst1 = new ArrayList<ChangeRequests>(lst.size());
		    for (ChangeRequestsEntity cre : lst) {
		        ChangeRequests cr = new ChangeRequests(cre);
		        lst1.add(cr);
		     }
		     return lst1;
		  } else {
		      return new ArrayList<ChangeRequests>(0);
		  }
			
	}

	@Override
	public ChangeRequestsEntity getChangeRequest(Integer id) {
		return emMicro.find(ChangeRequestsEntity.class, id);
	}
	
	@Override
	public ChangeRequests saveChangeRequest(Integer chRequestId, boolean bConfirm) {
		ChangeRequestsEntity creq = getChangeRequest(chRequestId);
		if (creq == null) {
			return null;
		}
		if (bConfirm) {
			creq.setIsEdited(ChangeRequests.EDITED);
		} else {
			creq.setIsEdited(ChangeRequests.NOT_EDITED);
		}
		emMicro.persist(creq);
		return new ChangeRequests(creq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ChangeRequests saveChangeRequest(Integer chRequestId, int creditRequestId,
			int userId, Date reqDate, String description) {
		CreditRequestEntity cre=creditRequestDAO.getCreditRequestEntity(creditRequestId);
		if (cre==null) {
			return null;
		}
		UsersEntity usr=usersDAO.getUsersEntity(userId);
		if (usr==null) {
			return null;
		}
		
		ChangeRequestsEntity creq = null;
		if (chRequestId != null) {
			creq = emMicro.find(ChangeRequestsEntity.class, chRequestId);
			if (creq == null) {
				return null;
			}
		} else {
			creq = new ChangeRequestsEntity();
			creq.setCreditRequestId(cre);
			creq.setRequestDate(reqDate);
			creq.setIsEdited(ChangeRequests.WAIT_EDIT);			
		}
		creq.setUserId(usr);
		creq.setDescription(description);
		
		emMicro.persist(creq);
		
		return new ChangeRequests(creq);
	}

	@Override
	public ChangeRequestsEntity saveChangeRequest(ChangeRequestsEntity changeRequest) {
		return emMicro.merge(changeRequest);
	}
}
