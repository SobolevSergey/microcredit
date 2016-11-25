package ru.simplgroupp.dao.impl;

import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ru.simplgroupp.dao.interfaces.OfficialDocumentsDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.OfficialDocumentsEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.OfficialDocuments;
import ru.simplgroupp.transfer.RefHeader;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class OfficialDocumentsDAOImpl implements OfficialDocumentsDAO{

	@PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

	@EJB
	ReferenceBooksLocal refBook;
	
	@EJB
	PeopleDAO peopleDAO;
	
	@Override
	public OfficialDocumentsEntity getOfficialDocumentEntity(Integer id) {
		return emMicro.find(OfficialDocumentsEntity.class, id);
	}

	@Override
	public OfficialDocuments getOfficialDocument(Integer id, Set options) {
		OfficialDocumentsEntity officialDocument=getOfficialDocumentEntity(id);
		if (officialDocument!=null){
			OfficialDocuments document=new OfficialDocuments(officialDocument);
			document.init(options);
			return document;
		}
		return null;
	}

	@Override
	public OfficialDocumentsEntity findOfficialDocumentDraft(Integer peopleMainId, Integer typeId) {
		List<OfficialDocumentsEntity> lst = JPAUtils.getResultList(emMicro, "findOfficialDocumentDraft", Utils.mapOf(
    			"peopleMainId", peopleMainId,
    			"typeId", typeId,
    			"isactive",ActiveStatus.DRAFT
    			));
		
		return (OfficialDocumentsEntity) Utils.firstOrNull(lst);
	}

	@Override
	public void deleteOfficialDocument(int id) {
		Query qry = emMicro.createNamedQuery("deleteOfficialDocument");
		qry.setParameter("id", id);
	    qry.executeUpdate();	
		
	}	
	
	 @Override
	 public OfficialDocumentsEntity saveOfficialDocument(OfficialDocumentsEntity document) {
		 
		ReferenceEntity docType=refBook.findByCodeIntegerEntity(RefHeader.OFFICIAL_DOCUMENT_TYPE, document.getDocumentTypeId().getCodeinteger()); 
		document.setDocumentTypeId(docType);
		OfficialDocumentsEntity ent = emMicro.merge(document);
	    emMicro.persist(ent);
	    return ent;
	 }
	 
	 @Override
	 public OfficialDocuments saveOfficialDocument(OfficialDocuments document) {    	
		OfficialDocumentsEntity ent = saveOfficialDocument(document.getEntity());
	   	return new OfficialDocuments(ent);
	 }

	@Override
	public OfficialDocumentsEntity findOfficialDocumentByAnotherId(
			Integer anotherId, Integer peopleMainId, Integer typeId) {
		List<OfficialDocumentsEntity> lst = JPAUtils.getResultList(emMicro, "findOfficialDocumentWithAnotherId", Utils.mapOf(
    			"peopleMainId", peopleMainId,
    			"typeId", typeId,
    			"anotherId",anotherId
    			));
		
		return (OfficialDocumentsEntity) Utils.firstOrNull(lst);
	}

	@Override
	public OfficialDocumentsEntity findOfficialDocumentCreditRequestDraft(
			Integer peopleMainId, Integer creditRequestId, Integer typeId) {
		List<OfficialDocumentsEntity> lst = JPAUtils.getResultList(emMicro, "findOfficialDocumentCreditRequestDraft", Utils.mapOf(
    			"peopleMainId", peopleMainId,
    			"typeId", typeId,
    			"creditRequestId", creditRequestId,
    			"isactive",ActiveStatus.DRAFT
    			));
		
		return (OfficialDocumentsEntity) Utils.firstOrNull(lst);
	}

	@Override
	public OfficialDocumentsEntity findOfficialDocumentCreditDraft(
			Integer peopleMainId, Integer creditId, Integer typeId) {
		List<OfficialDocumentsEntity> lst = JPAUtils.getResultList(emMicro, "findOfficialDocumentCreditDraft", Utils.mapOf(
    			"peopleMainId", peopleMainId,
    			"typeId", typeId,
    			"creditId", creditId,
    			"isactive",ActiveStatus.DRAFT
    			));
		
		return (OfficialDocumentsEntity) Utils.firstOrNull(lst);
	}    
}
