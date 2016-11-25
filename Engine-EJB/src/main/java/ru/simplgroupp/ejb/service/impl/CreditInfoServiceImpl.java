package ru.simplgroupp.ejb.service.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import ru.simplgroupp.dao.interfaces.ChangeRequestsDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.DebtDao;
import ru.simplgroupp.dao.interfaces.NegativeDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.ScoringDAO;
import ru.simplgroupp.dao.interfaces.SummaryDAO;
import ru.simplgroupp.dao.interfaces.VerificationDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.service.CreditInfoService;
import ru.simplgroupp.persistence.ChangeRequestsEntity;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.DebtEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.RefSummaryEntity;
import ru.simplgroupp.persistence.ScoringEntity;
import ru.simplgroupp.persistence.VerificationEntity;
import ru.simplgroupp.services.CreditService;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.transfer.ChangeRequests;
import ru.simplgroupp.transfer.Debt;
import ru.simplgroupp.transfer.Partner;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CreditInfoServiceImpl implements CreditInfoService{

	@EJB
    ReferenceBooksLocal refBooks;
	
	@EJB
    PeopleDAO peopleDAO;
	
	@EJB
	CreditRequestDAO creditRequestDAO;
	
	@EJB
	CreditService creditService;
	
	@EJB
	DebtDao debtDAO;
	
	@EJB
	VerificationDAO verificationDAO;
	
	@EJB
	SummaryDAO summaryDAO;
	
	@EJB
	NegativeDAO negativeDAO;
	
	@EJB
	ScoringDAO scoringDAO;
	
	@EJB
	ChangeRequestsDAO changeRequestsDAO;
	
	@Inject Logger log;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveSummary(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain,
			PartnersEntity partner, String value, RefSummaryEntity ref, Integer refid) {
		saveSummary(creditRequest, peopleMain,partner, value,ref, refid,new Date());
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveSummary(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain,
			PartnersEntity partner, String value, RefSummaryEntity ref, Integer refid,Date summaryDate) {
		try {
		    summaryDAO.saveSummary(creditRequest, peopleMain, partner, value, ref, refid, summaryDate);
		} catch (Exception e){
			log.log(Level.SEVERE, "Не удалось сохранить суммарную информацию, ошибка "+e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public VerificationEntity saveVerification(CreditRequestEntity creditRequest, PeopleMainEntity peopleMain,
				PartnersEntity partner, Double verScore, Double valScore,
				Integer verifyPersonal,Integer verifyDocument,Integer verifyPhone,Integer verifyAddress,Integer verifyInn) {
		   
		    return verificationDAO.saveVerification(creditRequest, peopleMain, partner, verScore, 
		    		valScore, verifyPersonal, verifyDocument, verifyPhone, verifyAddress, verifyInn);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public DebtEntity newDebt(Integer creditRequestId,Integer peopleMainId, Integer partnerId,
			Integer creditId,Double amount, Double amountPenalty,String nameDebt,
			String authorityName, Date dateDebt, String debtNumber,
			String comment,String typeDebt,Integer authorityCode,
			Integer authorityId,Date dateDecision,Integer isActive) throws KassaException {
	
		return debtDAO.newDebt(creditRequestId, peopleMainId, partnerId, creditId, amount, 
				amountPenalty, nameDebt, authorityName, dateDebt, debtNumber, comment, 
				typeDebt, authorityCode, authorityId, dateDecision, isActive);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveStatusChangeRequest(Integer creditRequestId, Integer isEdited)
			throws KassaException {
		ChangeRequests chReq=findChangeRequestWaitingEdit(creditRequestId);
		if (chReq!=null){
			ChangeRequestsEntity chEnt=changeRequestsDAO.getChangeRequest(chReq.getId());
			chEnt.setIsEdited(isEdited);
			changeRequestsDAO.saveChangeRequest(chEnt);
		}
		
	}

	@Override
	public ChangeRequests findChangeRequestWaitingEdit(Integer creditRequestId) {
	    List<ChangeRequests> lst=changeRequestsDAO.listChangeRequests(creditRequestId,ChangeRequests.WAIT_EDIT,null,null);
		if (lst.size()>0){
			return lst.get(0);
		}
		return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public ChangeRequests saveChangeRequest(Integer chRequestId, boolean bConfirm) {
		return changeRequestsDAO.saveChangeRequest(chRequestId, bConfirm);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ChangeRequests saveChangeRequest(Integer chRequestId, int creditRequestId,
			int userId, Date reqDate, String description) {
	    return changeRequestsDAO.saveChangeRequest(chRequestId, creditRequestId, userId, reqDate, description);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<DebtEntity> listDebts(Integer creditRequestId,
			Integer peopleMainId, Integer partnerId,Integer organizationId,
			Integer authorityId, DateRange dateDebt,DateRange dateDecision) {
		return debtDAO.listDebts(creditRequestId, peopleMainId, partnerId, 
					organizationId, authorityId, dateDebt, dateDecision);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveDebt(Debt debt) throws KassaException {
		debtDAO.saveDebt(debt);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveScoring(Integer creditRequestId, Integer peopleMainId,
			Integer partnerId, Integer modelId, String modelCode,
			Double score,String error,Double scoreRisk) {
			
		scoringDAO.saveScoring(creditRequestId, peopleMainId, partnerId, 
					modelId, modelCode, score, error, scoreRisk);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveNegative(Integer creditRequestId, Integer peopleMainId,
			Integer partnerId, String article, String module,
			Integer yearArticle) {
		negativeDAO.saveNegative(creditRequestId, peopleMainId, partnerId, article, module, yearArticle);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<VerificationEntity> findVerification(Integer creditRequestId, Integer peopleMainId,
			Integer partnerId, DateRange verificationDate) {
	    return verificationDAO.findVerification(creditRequestId, peopleMainId, partnerId, verificationDate);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public VerificationEntity saveVerification(	VerificationEntity verification) {
		return verificationDAO.saveVerification(verification);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public VerificationEntity saveSystemVerification(VerificationEntity verification,
			Integer creditRequestId, Integer peopleMainId,CreditEntity clientCredit, CreditEntity credit) {
        return saveSystemVerification(verification,creditRequestId, peopleMainId,clientCredit, credit,0);
	}
		
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public VerificationEntity saveSystemVerification(VerificationEntity verification,
			Integer creditRequestId, Integer peopleMainId,CreditEntity clientCredit, 
			CreditEntity credit,Integer addMark) {
			
       	double mark=0d;
       	try{
    	    mark=creditService.creditRating(clientCredit, credit);
    		if (addMark!=null&&addMark>0){
    			mark+=addMark;
    		}
    	} catch (Exception e){
    		log.severe("Не удалось рассчитать верификацию по кредиту, ошибка "+e);
    	}
        	
       	//уже есть верификация системы
        if (verification!=null){
              	//балл верификации меньше рассчитанного
            	if (verification.getVerificationScore()<mark){
            		/*verificationDAO.removeVerification(verification.getId());
            		verification=saveVerification(creditRequestDAO.getCreditRequestEntity(creditRequestId), 
                			peopleDAO.getPeopleMainEntity(peopleMainId), 
                			refBooks.getPartnerEntity(Partner.SYSTEM), mark, 0d, 0, 0, 0, 0, 0);*/
            		verification.setVerificationScore(mark);
            		verification=verificationDAO.saveVerification(verification);
            		log.info("Балл верификации меньше рассчитанного "+mark+", пытаемся сохранить данные по заявке "+creditRequestId);
            		
            	}
            } else {
            	log.info("Cохраняем данные верификации первый раз по заявке "+creditRequestId+", балл "+mark);
            	verification=saveVerification(creditRequestDAO.getCreditRequestEntity(creditRequestId), 
            			peopleDAO.getPeopleMainEntity(peopleMainId), 
            			refBooks.getPartnerEntity(Partner.SYSTEM), mark, 0d, 0, 0, 0, 0, 0);
        }
        return verification;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ScoringEntity> findScorings(Integer creditRequestId, Integer peopleMainId,
		Integer partnerId, DateRange scoringDate) {
			
	    return scoringDAO.findScorings(creditRequestId, peopleMainId, partnerId, scoringDate);
	}

	@Override
	public VerificationEntity findOneVerification(Integer creditRequestId,
			Integer peopleMainId, Integer partnerId, DateRange verificationDate) {
		return verificationDAO.findOneVerification(creditRequestId, peopleMainId, partnerId, verificationDate);
	}
}
