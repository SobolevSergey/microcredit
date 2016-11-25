package ru.simplgroupp.dao.impl;

import ru.simplgroupp.dao.interfaces.DocumentOtherDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.DocumentOtherEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.DocumentsOther;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

/**
 * Created by cobalt on 16.09.15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DocumentOtherDAOImpl implements DocumentOtherDAO {

    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;

    @EJB
    ReferenceBooksLocal refBook;

    @EJB
    PeopleDAO peopleDAO;

    @Override
    public DocumentOtherEntity getDocument(Integer id) {
        return emMicro.find(DocumentOtherEntity.class,id);
    }

    @Override
    public DocumentsOther getDocument(Integer id, Set options) {
        DocumentOtherEntity documentEntity=getDocument(id);
        if (documentEntity!=null){
            DocumentsOther document=new DocumentsOther(documentEntity);
            document.init(options);
            return document;
        }
        return null;
    }

    @Override
    public List<DocumentOtherEntity> findPeopleOtherDocuments(Integer id, Integer type) {
        return JPAUtils.getResultList(emMicro, "findPeopleOtherDocuments", Utils.mapOf("peopleId", id, "type", type));
    }

    @Override
    public void deleteDocument(Integer id) {
        emMicro.createNamedQuery("deleteOtherDocument").setParameter("id",id).executeUpdate();
    }

    @Override
    public DocumentOtherEntity saveDocument(DocumentOtherEntity entity) {
        return emMicro.merge(entity);
    }
}
