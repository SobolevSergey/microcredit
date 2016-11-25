package ru.simplgroupp.dao.impl;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ru.simplgroupp.dao.interfaces.DocumentsDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.DocumentMediaEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.DocumentMedia;
import ru.simplgroupp.transfer.Documents;
import ru.simplgroupp.transfer.RefHeader;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DocumentsDAOImpl implements DocumentsDAO{

	@PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

	@EJB
	ReferenceBooksLocal refBook;
	
	@EJB
	PeopleDAO peopleDAO;
	
	@Inject Logger log;
	
	@Override
	public DocumentEntity getDocument(Integer id) {
		return emMicro.find(DocumentEntity.class, id);
	}

	@Override
	public Documents getDocument(Integer id, Set options) {
		DocumentEntity documentEntity=getDocument(id);
		if (documentEntity!=null){
			Documents document=new Documents(documentEntity);
			document.init(options);
			return document;
		}
		return null;
	}

	@Override
	public DocumentMedia getDocumentMedia(Integer id, Set options) {
		DocumentMediaEntity documentMediaEntity=getDocumentMedia(id);
		if (documentMediaEntity!=null){
			DocumentMedia documentMedia=new DocumentMedia(documentMediaEntity);
			documentMedia.init(options);
			return documentMedia;
		}
		return null;
	}

	@Override
	public Integer findMaxDocPageNumber(Integer documentId) {
		List<Integer> lst = JPAUtils.getResultList(emMicro, "maxDocPageNumber", Utils.mapOf(
    			"documentId", documentId
    			));
		if (lst.size()>0&&lst.get(0)!=null){
			return lst.get(0)+1;
		}
		return 1;
	}

	@Override
	public DocumentMediaEntity saveDocumentPage(DocumentMediaEntity docMedia,Integer peopleMainId,
			Integer scanTypeId,Integer documentId, 	Integer pageNumber,	
			String mimeType, String mediaPath, byte[] mediaData) {
		
			DocumentMediaEntity documentMedia=null;
			//если это новый скан
			if (docMedia==null){
				documentMedia=new DocumentMediaEntity();
			    if (documentId!=null){
				    DocumentEntity document=getDocument(documentId);
				    documentMedia.setDocumentId(document);
			    }
		        if (scanTypeId!=null){
		    	    documentMedia.setScanTypeId(refBook.findByCodeIntegerEntity(RefHeader.DOCUMENT_SCAN_TYPE, scanTypeId));
		        }
		        if (peopleMainId!=null){
		    	    documentMedia.setPeopleMainId(peopleDAO.getPeopleMainEntity(peopleMainId));
		        }
			} else {
				documentMedia=docMedia;
			}
			//если не передали номер страницы, посчитаем сами
			if (pageNumber==null){
				if (documentId!=null){
				    documentMedia.setPagenumber(findMaxDocPageNumber(documentId));
				} else {
					documentMedia.setPagenumber(1);
				}
			} else {
				documentMedia.setPagenumber(pageNumber);
			}
			documentMedia.setMimetype(mimeType);
			documentMedia.setMediapath(mediaPath);
			documentMedia.setMediadata(mediaData);
			emMicro.persist(documentMedia);
			return documentMedia;
		
	}

	@Override
	public DocumentMediaEntity getDocumentMedia(Integer id) {
		return emMicro.find(DocumentMediaEntity.class, id);
	}

	@Override
	public void deleteDocumentMedia(Integer id) {
		Query qry = emMicro.createNamedQuery("deleteDocPage");
		qry.setParameter("id", id);
	    qry.executeUpdate();	
		
	}

	@Override
	public void deleteDocumentMediaAll(Integer documentId) {
		Query qry = emMicro.createNamedQuery("deleteAllDocPages");
		qry.setParameter("documentId", documentId);
	    qry.executeUpdate();		
		
	}

	@Override
	public DocumentMedia addDocumentMedia(DocumentMedia docMedia,Integer peopleMainId) {
		DocumentMediaEntity documentMedia=null;
		//если это не новый документ
		if (docMedia.getId()!=null){
			documentMedia=getDocumentMedia(docMedia.getId());
		}
		try {
		    DocumentMediaEntity newMedia=saveDocumentPage(documentMedia,peopleMainId,
				refBook.referenceCodeIntegerOrNull(docMedia.getScanType()),
				docMedia.getDocument()==null?null:docMedia.getDocument().getId(),
				docMedia.getPageNumber(),docMedia.getMimeType(), 
				docMedia.getMediaPath(), docMedia.getMediaData());
		    return new DocumentMedia(newMedia);
		} catch (Exception e){
			log.severe("Не удалось сохранить скан страницы, ошибка "+e);
			return null;
		}
		
	}

	@Override
	public DocumentEntity saveDocument(DocumentEntity document) {
		return emMicro.merge(document);
	}

	@Override
	public DocumentEntity saveDocumentValidity(DocumentEntity document,
			Integer validity) {
		document.setReasonendId(refBook.findByCodeIntegerEntity(RefHeader.DOC_END_TYPE,validity));
		return saveDocument(document);
	}

	

}
