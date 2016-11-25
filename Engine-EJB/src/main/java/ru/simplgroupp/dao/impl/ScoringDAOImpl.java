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
import org.apache.commons.lang3.StringUtils;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.ScoringDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PartnersEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.ScoringEntity;
import ru.simplgroupp.toolkit.common.DateRange;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ScoringDAOImpl implements ScoringDAO{

	@PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
    
    @EJB
    PeopleDAO peopleDAO;
	
	@EJB
	CreditRequestDAO creditRequestDAO;
	
	@EJB
    ReferenceBooksLocal refBooks;
	
	@Override
	public List<ScoringEntity> findScorings(Integer creditRequestId, Integer peopleMainId,
			Integer partnerId, DateRange scoringDate) {
		String hql="from ru.simplgroupp.persistence.ScoringEntity where (1=1)";
		if (creditRequestId!=null){
			hql=hql+" and creditRequestId.id=:creditRequestId ";
		}
		if (peopleMainId!=null){
			hql=hql+" and peopleMainId.id=:peopleMainId ";
		}
		if (partnerId!=null){
			hql=hql+" and partnersId.id=:partnerId ";
		}
		if (scoringDate != null && scoringDate.getFrom() != null) {
		   	hql+= " and (scoringDate > :scoringDateFrom)";
		}
		if (scoringDate != null && scoringDate.getTo() != null) {
		   	hql+= " and (scoringDate <= :scoringDateTo)";
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
	    if (scoringDate != null && scoringDate.getFrom() != null) {
	       	qry.setParameter("scoringDateFrom", DateUtils.addDays(scoringDate.getFrom(),1));
	    }
	    if (scoringDate != null && scoringDate.getTo() != null) {
	       	qry.setParameter("scoringDateTo", scoringDate.getTo());
	    }
	    return qry.getResultList();
	}
	
	@Override
	public void saveScoring(Integer creditRequestId, Integer peopleMainId,
			Integer partnerId, Integer modelId, String modelCode,
			Double score,String error,Double scoreRisk) {
		ScoringEntity scor=new ScoringEntity();
		scor.setScore(score);
		PartnersEntity partner=refBooks.getPartnerEntity(partnerId);
		scor.setPartnersId(partner);
		scor.setScoringDate(new Date());
		CreditRequestEntity creditRequest=creditRequestDAO.getCreditRequestEntity(creditRequestId);
		scor.setCreditRequestId(creditRequest);
		if (modelId!=null){
		    scor.setScoreModelId(refBooks.getScoreModel(modelId));
		}
		if (StringUtils.isNotEmpty(modelCode)&&refBooks.getModelByCode(partner, modelCode)!=null){
			scor.setScoreModelId(refBooks.getModelByCode(partner, modelCode).getEntity());
		}
		scor.setTextError(error);
		scor.setScoreRisk(scoreRisk);
		PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleMainId);
		scor.setPeopleMainId(peopleMain);
		scor=emMicro.merge(scor);
		emMicro.persist(scor);
		
	}

	@Override
	public ScoringEntity getScoringEntity(Integer scoringId) {
		return emMicro.find(ScoringEntity.class,scoringId);
	}
	
}
