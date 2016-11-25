package ru.simplgroupp.dao.impl;

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

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.NegativeDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.NegativeEntity;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class NegativeDAOImpl implements NegativeDAO{

	@PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;
   	
	@EJB
    ReferenceBooksLocal refBooks;
	
	@EJB
    PeopleDAO peopleDAO;
	
	@EJB
	CreditRequestDAO creditRequestDAO;
	
	@Override
	public void saveNegative(Integer creditRequestId, Integer peopleMainId,
			Integer partnerId, String article, String module,
			Integer yearArticle) {
		NegativeEntity negative=new NegativeEntity();
		if (creditRequestId!=null){
		    negative.setCreditRequestId(creditRequestDAO.getCreditRequestEntity(creditRequestId));
		}
		if (partnerId!=null){
		    negative.setPartnersId(refBooks.getPartnerEntity(partnerId));
		}
		if (peopleMainId!=null){
		    negative.setPeopleMainId(peopleDAO.getPeopleMainEntity(peopleMainId));
		}
		if (refBooks.findByArticle(article)!=null){
		    negative.setArticleId(refBooks.findByArticle(article).getEntity());
		}
		negative.setModule(module);
		negative.setYearArticle(yearArticle);
		emMicro.persist(negative);
		
	}

	@Override
	public List<NegativeEntity> findNegative(Integer creditRequestId,
			Integer peopleMainId, Integer partnerId, String article,Integer yearArticle) {
		String hql="from ru.simplgroupp.persistence.NegativeEntity where (1=1)";
		if (creditRequestId!=null){
			hql=hql+" and creditRequestId.id=:creditRequestId ";
		}
		if (peopleMainId!=null){
			hql=hql+" and peopleMainId.id=:peopleMainId ";
		}
		if (partnerId!=null){
			hql=hql+" and partnersId.id=:partnerId ";
		}
		if (StringUtils.isNotEmpty(article)) {
		   	hql+= " and articleId.article=:article ";
		}
		if (yearArticle!=null){
			hql=hql+" and yearArticle=:yearArticle ";
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
	  	if (StringUtils.isNotEmpty(article)) { 
	       	qry.setParameter("article", article);
	    }
	  	if (yearArticle!=null){
	  		qry.setParameter("yearArticle", yearArticle);
		}
	    return qry.getResultList();	
	}

	@Override
	public NegativeEntity getNegativeEntity(Integer negativeId) {
		return emMicro.find(NegativeEntity.class, negativeId);
	}
	
	
	
}
