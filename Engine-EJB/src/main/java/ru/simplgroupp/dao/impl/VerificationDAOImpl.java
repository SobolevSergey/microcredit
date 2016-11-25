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

import org.apache.commons.lang.time.DateUtils;

import ru.simplgroupp.dao.interfaces.VerificationDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.VerificationEntity;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.Verification;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class VerificationDAOImpl implements VerificationDAO{
	
	@PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
   	
	@EJB
    ReferenceBooksLocal refBooks;
	
	@Override
	public List<VerificationEntity> findVerification(Integer creditRequestId, Integer peopleMainId,
			Integer partnerId, DateRange verificationDate) {
		String hql="from ru.simplgroupp.persistence.VerificationEntity where (1=1)";
		if (creditRequestId!=null){
			hql=hql+" and creditRequestId.id=:creditRequestId ";
		}
		if (peopleMainId!=null){
			hql=hql+" and peopleMainId.id=:peopleMainId ";
		}
		if (partnerId!=null){
			hql=hql+" and partnersId.id=:partnerId ";
		}
		if (verificationDate != null && verificationDate.getFrom() != null) {
		   	hql+= " and (verificationDate > :verificationDateFrom)";
		}
		if (verificationDate != null && verificationDate.getTo() != null) {
		   	hql+= " and (verificationDate <= :verificationDateTo)";
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
	    if (verificationDate != null && verificationDate.getFrom() != null) {
	       	qry.setParameter("verificationDateFrom", DateUtils.addDays(verificationDate.getFrom(),1));
	    }
	    if (verificationDate != null && verificationDate.getTo() != null) {
	       	qry.setParameter("verificationDateTo", verificationDate.getTo());
	    }
	    return qry.getResultList();
	}

	@Override
	public VerificationEntity saveVerification(VerificationEntity verification) {
		VerificationEntity verificationNew=emMicro.merge(verification);
		emMicro.persist(verificationNew);
		return verificationNew;
	}

	@Override
	public VerificationEntity saveVerification(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain,
				PartnersEntity partner, Double verScore, Double valScore,
				Integer verifyPersonal,Integer verifyDocument,Integer verifyPhone,Integer verifyAddress,Integer verifyInn) {
			    
		    VerificationEntity ver=new VerificationEntity();
		    ver.setCreditRequestId(creditRequest);
		    ver.setPeopleMainId(peopleMain);
		    ver.setPartnersId(partner);
		    ver.setVerificationDate(new Date());
		    //save validation 
		    if (valScore!=null) {
		    	//если нам вернули баллы
		        ver.setValidationMark(refBooks.getVerificationMarks(valScore, Verification.CODE_VALIDATION).getEntity());
		        ver.setValidationText(refBooks.getVerificationMarks(valScore, Verification.CODE_VALIDATION).getEntity().getName());
		        ver.setValidationScore(valScore);
		    } else {
		    	//если это была просто верификация
		    	Integer i=refBooks.getValidationMarkFromVerify(verifyPersonal, verifyDocument);
		    	ver.setValidationMark(refBooks.getValidationMark(i));
		    	if (i!=null){
		    		ver.setValidationText(refBooks.getValidationMark(i).getName());
		    	}
		    }
		    //save verification
		    if (verScore!=null) {
		    	if (partner.getId()!=Partner.SYSTEM){
		            ver.setVerificationMark(refBooks.getVerificationMarks(verScore, Verification.CODE_VERIFICATION).getEntity());
		            ver.setVerificationText(refBooks.getVerificationMarks(verScore, Verification.CODE_VERIFICATION).getEntity().getName());
		    	} else {
		    		ver.setVerificationText("Произведена идентификация системой");
		    	}
		        ver.setVerificationScore(verScore);
		    }
		    //save other data
		    ver.setVerifyPersonal(verifyPersonal);
		    ver.setVerifyPhone(verifyPhone);
		    ver.setVerifyDocument(verifyDocument);
		    ver.setVerifyAddress(verifyAddress);
		    ver.setVerifyInn(verifyInn);
		    return emMicro.merge(ver);
	}

	@Override
	public VerificationEntity getVerificationEntity(Integer verificationId) {
		return emMicro.find(VerificationEntity.class, verificationId);
	}

	@Override
	public void removeVerification(Integer id) {
		 Query qry = emMicro.createNamedQuery("removeVerification");
		 qry.setParameter("id", id);
	     qry.executeUpdate();	
		
	}

	@Override
	public VerificationEntity findOneVerification(Integer creditRequestId,
			Integer peopleMainId, Integer partnerId, DateRange verificationDate) {
		List<VerificationEntity> lst=findVerification(creditRequestId,peopleMainId, partnerId, verificationDate);
		return (VerificationEntity) Utils.firstOrNull(lst);
	}

}
