package ru.simplgroupp.services.impl;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.VerificationConfigDAO;
import ru.simplgroupp.dao.interfaces.VerificationReferenceDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.persistence.CreditEntity;
import ru.simplgroupp.persistence.VerificationConfigEntity;
import ru.simplgroupp.persistence.VerificationReferenceEntity;
import ru.simplgroupp.services.CreditService;

import javax.ejb.EJB;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;

import java.util.List;

/**
 * Credit service implementation
 */
@Singleton
@TransactionManagement
public class CreditServiceImpl implements CreditService {

    @EJB
    private CreditDAO creditDAO;

    @EJB
    private VerificationReferenceDAO verificationReferenceDAO;

    @EJB
    private VerificationConfigDAO verificationConfigDAO;

    private static final Logger log = LoggerFactory.getLogger(CreditServiceImpl.class.getName());
    
    @Override
    public boolean isMatch(Integer creditId1, Integer creditId2) throws ObjectNotFoundException, KassaException {
        CreditEntity credit1 = creditDAO.getCreditEntity(creditId1);
        if (credit1 == null) {
            throw new ObjectNotFoundException("Credit with id " + creditId1 + " not found");
        }

        CreditEntity credit2 = creditDAO.getCreditEntity(creditId2);
        if (credit2 == null) {
            throw new ObjectNotFoundException("Credit with id " + creditId2 + " not found");
        }

        return isMatch(credit1,credit2);
    }

    @Override
    public double creditRating(Integer creditId1, Integer creditId2) throws ObjectNotFoundException, KassaException {
        CreditEntity credit1 = creditDAO.getCreditEntity(creditId1);
        if (credit1 == null) {
            throw new ObjectNotFoundException("Credit with id " + creditId1 + " not found");
        }

        CreditEntity credit2 = creditDAO.getCreditEntity(creditId2);
        if (credit2 == null) {
            throw new ObjectNotFoundException("Credit with id " + creditId2 + " not found");
        }

        return creditRating(credit1,credit2);
    }

	@Override
	public boolean isMatch(CreditEntity credit1, CreditEntity credit2)
			throws ObjectNotFoundException, KassaException {
		  List<VerificationReferenceEntity> verificationReferences = verificationReferenceDAO
	                .findForRate(CreditEntity.class.getName());
	        for (VerificationReferenceEntity verificationReference : verificationReferences) {
	            try {
	                Object property1 = PropertyUtils.getProperty(credit1, verificationReference.getFieldName());
	                Object property2 = PropertyUtils.getProperty(credit2, verificationReference.getFieldName());
	                VerificationConfigEntity verificationConfig = verificationConfigDAO
	                        .findForMatching(verificationReference);
	                if(verificationConfig == null) {
	                    throw new ObjectNotFoundException("Verification config not found for verification reference " +
	                            verificationReference.getId());
	                }

	                /* если необязательное поле не заполнено - пропускаем*/
	                if (!verificationReference.isNecessary() && (property1 == null || property2 == null)) {
	                    continue;
	                }
	                boolean isMatch = MatchingHelper.isMatch(property1, property2, verificationConfig.getDiff());
	                if (!isMatch) {
	                    return false;
	                }
	            } catch (Exception e) {
	            	log.error("Can't match property " + CreditEntity.class.getName() + "." +
	                        verificationReference.getFieldName());
	                throw new KassaException("Can't match property " + CreditEntity.class.getName() + "." +
	                        verificationReference.getFieldName(), e);
	            } 
	        }

	        return true;
	}

	@Override
	public double creditRating(CreditEntity credit1, CreditEntity credit2)
			throws ObjectNotFoundException, KassaException {
		 List<VerificationReferenceEntity> verificationReferences = verificationReferenceDAO
	                .findForRate(CreditEntity.class.getName());
	        double rating = 0;
	        for (VerificationReferenceEntity verificationReference : verificationReferences) {
	            try {
	                Object property1 = PropertyUtils.getProperty(credit1, verificationReference.getFieldName());
	                Object property2 = PropertyUtils.getProperty(credit2, verificationReference.getFieldName());

	                double diff = MatchingHelper.diff(property1, property2);
	                VerificationConfigEntity verificationConfig = verificationConfigDAO
	                        .findForRaiting(verificationReference, diff);
	                if (verificationConfig != null) {
	                    rating += verificationConfig.getRating();
	                }
	            } catch (Exception e) {
	            	log.error("Can't rate property " + CreditEntity.class.getName() + "." +
	                        verificationReference.getFieldName());
	                throw new KassaException("Can't rate property " + CreditEntity.class.getName() + "." +
	                        verificationReference.getFieldName(), e);
	            }
	        }

	     return rating;
	}
}
