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

import ru.simplgroupp.dao.interfaces.CreditOfferDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.UsersDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.CreditOfferEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditOffer;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.RefHeader;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CreditOfferDAOImpl implements CreditOfferDAO {

	@PersistenceContext(unitName = "MicroPU")
	private EntityManager emMicro;

	@EJB
	PeopleDAO peopleDAO;
	
	@EJB
	ReferenceBooksLocal refBook;
	
	@EJB
	UsersDAO userDAO;
	
	@EJB
	CreditRequestDAO crDAO;
	
	@Override
	public CreditOfferEntity getCreditOfferEntity(int id) {
		return emMicro.find(CreditOfferEntity.class, id);
	}

	@Override
	public CreditOfferEntity saveCreditOffer(CreditOfferEntity creditOffer) {
		CreditOfferEntity offer=emMicro.merge(creditOffer);
		emMicro.persist(offer);
		return offer;
	}

	@Override
	public void deleteCreditOffer(int id) {
		Query qry = emMicro.createNamedQuery("deleteCreditOffer");
		qry.setParameter("id", id);
	    qry.executeUpdate();		
	}

	@Override
	public CreditOfferEntity newCreditOffer(Integer creditRequestId,
			Integer peopleMainId, Integer userId, Integer offerWayId,
			Date offerTime, Integer creditDays, Double creditSum,
			Double creditStake, String comment) {
		if (creditRequestId!=null&&peopleMainId!=null&&offerWayId!=null){
		    CreditOfferEntity creditOffer=new CreditOfferEntity();
		    creditOffer.setComment(comment);
		    creditOffer.setCreditDays(creditDays);
		    creditOffer.setCreditStake(creditStake);
		    creditOffer.setCreditSum(creditSum);
		    if (offerTime==null){
		    	creditOffer.setOfferTime(new Date());
		    } else {
		        creditOffer.setOfferTime(offerTime);
		    }
		    if (userId!=null){
		    	creditOffer.setUserId(userDAO.getUsersEntity(userId));
		    }
		    creditOffer.setCreditRequestId(crDAO.getCreditRequestEntity(creditRequestId));
		    creditOffer.setPeopleMainId(peopleDAO.getPeopleMainEntity(peopleMainId));
		    creditOffer.setOfferWayId(refBook.findByCodeIntegerEntity(RefHeader.EXECUTION_WAY,offerWayId));
		    emMicro.persist(creditOffer);
		    return creditOffer;
		}
		return null;
	}

	@Override
	public CreditOfferEntity acceptOffer(int id) {
		CreditOfferEntity offer=getCreditOfferEntity(id);
		offer.setAccepted(CreditRequest.ACCEPTED);
		offer=saveCreditOffer(offer);
		return offer;
	}

	@Override
	public CreditOfferEntity declineOffer(int id) {
		CreditOfferEntity offer=getCreditOfferEntity(id);
		offer.setAccepted(CreditRequest.NOT_ACCEPTED);
		offer=saveCreditOffer(offer);
		return offer;
	}

	@Override
	public List<CreditOfferEntity> findCreditOfferByCreditRequest(
			Integer creditRequestId) {
		return JPAUtils.getResultList(emMicro, "findCreditOfferByCreditRequest", 
				Utils.mapOf("creditRequestId",creditRequestId));
	}

	@Override
	public List<CreditOffer> getCreditOfferByCreditRequest(Integer creditRequestID) {
		List<CreditOfferEntity> offerEntities = findCreditOfferByCreditRequest(creditRequestID);
		List<CreditOffer> result = new ArrayList<>();
		for (CreditOfferEntity entity : offerEntities) {
			CreditOffer offer = new CreditOffer(entity);
			result.add(offer);
		}
		return result;
	}

	@Override
	public void declineAllOffer(Integer creditRequestID) {
		Query qry = emMicro.createNamedQuery("declineAllCreditOffers");
		qry.setParameter("creditRequestID", creditRequestID);
		qry.executeUpdate();
	}
}
