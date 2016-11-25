package ru.simplgroupp.dao.impl;


import ru.simplgroupp.dao.interfaces.VerificationConfigDAO;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.VerificationConfigEntity;
import ru.simplgroupp.persistence.VerificationReferenceEntity;
import ru.simplgroupp.toolkit.common.Utils;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Verification config DAO implementation
 */
@Singleton
@TransactionManagement
public class VerificationConfigDAOImpl implements VerificationConfigDAO {

    private static final String FIND_FOR_MATCHING = "findVerificationConfigForMatching";

    private static final String FIND_FOR_RATING = "findVerificationConfigForRating";

    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

    @Override
    public VerificationConfigEntity findForMatching(VerificationReferenceEntity verificationReference) {
        return  (VerificationConfigEntity) JPAUtils.getSingleResult(emMicro, FIND_FOR_MATCHING, 
        		Utils.mapOf("verificationReference", verificationReference)); 
    }

    @Override
    public VerificationConfigEntity findForRaiting(VerificationReferenceEntity verificationReference, double diff) {
    	return (VerificationConfigEntity) JPAUtils.getSingleResult(emMicro,FIND_FOR_RATING,
    			Utils.mapOf("verificationReference", verificationReference,
    					"diff",diff)); 
    }
}
