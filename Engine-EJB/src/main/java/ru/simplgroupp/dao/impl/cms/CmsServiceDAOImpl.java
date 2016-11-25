package ru.simplgroupp.dao.impl.cms;

import ru.simplgroupp.dao.interfaces.cms.CmsServiceDAO;
import ru.simplgroupp.persistence.cms.CmsDocumentsEntity;
import ru.simplgroupp.persistence.cms.CmsImagesEntity;
import ru.simplgroupp.persistence.cms.CmsNewsEntity;
import ru.simplgroupp.persistence.cms.CmsPageEntity;
import ru.simplgroupp.persistence.cms.CmsQuestionGroupEntity;
import ru.simplgroupp.persistence.cms.CmsQuestionsEntity;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CmsServiceDAOImpl implements CmsServiceDAO {

    private static final Logger logger = Logger.getLogger(CmsServiceDAOImpl.class.getName());

    @PersistenceContext(unitName = "MicroPU")
    private EntityManager emMicro;


    @Override
    public CmsPageEntity getCmsPageByType(String type, String projectName) {
        Query qry = emMicro.createNamedQuery("getCmsPageByType");
        qry.setParameter("type", type);
        qry.setParameter("projectName", projectName);
        List<CmsPageEntity> lstc = qry.getResultList();
        if (lstc.size() == 1) {
            return lstc.get(0);
        } else {
            return null;
        }
    }


    @Override
    public CmsPageEntity save(CmsPageEntity cmsPageEntity) {
        if (cmsPageEntity.getId() != null) {
            emMicro.merge(cmsPageEntity);
        } else {
            emMicro.persist(cmsPageEntity);
        }
        return cmsPageEntity;
    }

    @Override
    public CmsImagesEntity saveImage(CmsImagesEntity cmsImagesEntity){
        emMicro.persist(cmsImagesEntity);

        return cmsImagesEntity;
    }

    @Override
    public List<CmsImagesEntity> getCmsImageByPageId(Integer pageId) {
        Query qry = emMicro.createNamedQuery("getCmsImageByPageId");
        qry.setParameter("pageId", pageId);
        List<CmsImagesEntity> lstc = qry.getResultList();
            return lstc;
    }
    @Override
    public List<CmsNewsEntity> getCmsNewsEntityPagi(String projectName, Integer limit, Integer offset) {
        Query qry = emMicro.createNamedQuery("cmsNewsFindAll");
        qry.setParameter("projectName", projectName);
        qry.setMaxResults(limit);
        qry.setFirstResult(offset);

        List<CmsNewsEntity> lstc = qry.getResultList();
        return lstc;
    }    
    @Override
    public List<CmsQuestionsEntity> getCmsQuestionPagi(String projectName, Integer limit, Integer offset) {
        Query qry = emMicro.createNamedQuery("cmsQuestionByProjectname");
        qry.setParameter("projectName", projectName);
        qry.setMaxResults(limit);
        qry.setFirstResult(offset);
        List<CmsQuestionsEntity> lstc = qry.getResultList();
        return lstc;
    }
    @Override
    public List<CmsNewsEntity> getCmsNewsEntityAll(String projectName) {
        Query qry = emMicro.createNamedQuery("cmsNewsFindAll");
        qry.setParameter("projectName", projectName);
        List<CmsNewsEntity> lstc = qry.getResultList();
        return lstc;
    }
    @Override
    public List<CmsQuestionGroupEntity> getCmsQuestionGroupEntityAll(String projectName) {
        Query qry = emMicro.createNamedQuery("cmsQuestionGroupFindAll");
        qry.setParameter("projectName", projectName);
        List<CmsQuestionGroupEntity> lstc = qry.getResultList();
        return lstc;
    }
    @Override
    public List<CmsDocumentsEntity> getCmsDocumentEntityAll(String projectName) {
        return getCmsDocumentEntityAll(projectName,false);
    }
    @Override
    public List<CmsDocumentsEntity> getCmsDocumentEntityAll(String projectName, Boolean isArchive) {
        Query qry;
        if(isArchive) qry = emMicro.createNamedQuery("cmsDocumentFindArchive");
        else qry = emMicro.createNamedQuery("cmsDocumentFindAll");
        qry.setParameter("projectName", projectName);
        List<CmsDocumentsEntity> lstc = qry.getResultList();
        return lstc;
    }
    @Override
    public List<CmsQuestionsEntity> getCmsQuestionByGroupId(Integer groupId) {
        Query qry = emMicro.createNamedQuery("cmsQuestionByGroup");
        qry.setParameter("groupId", groupId);
        List<CmsQuestionsEntity> lstc = qry.getResultList();
        return lstc;
    }
   
    @Override
    public Integer countCmsNews(String projectName) {
        Query qry = emMicro.createNamedQuery("countNewsProject");
        qry.setParameter("projectName", projectName);
        BigInteger lstc = (BigInteger) qry.getSingleResult();
        return lstc.intValue();
    }

    @Override
    public Integer countCmsQuestions(String projectName) {
        Query qry = emMicro.createNamedQuery("countQuestionProject");
        qry.setParameter("projectName", projectName);
        BigInteger lstc = (BigInteger) qry.getSingleResult();
        return lstc.intValue();
    }    

    @Override
    public void removeImage(Integer id) {
        Query qry = emMicro.createNamedQuery("removeImage");
        qry.setParameter("id", id);
        qry.executeUpdate();
    }
    @Override
    public void removePreviewImagePerOneTransaction(Integer newsId) {
        CmsNewsEntity newsEntity = (CmsNewsEntity) findById(CmsNewsEntity.class,newsId);
        if (newsEntity != null) {
            if (newsEntity.getPreviewImgId() != null) {
                Integer previewImageId = newsEntity.getPreviewImgId();
                Query qry = emMicro.createNamedQuery("clearPreviewImgNews");
                qry.setParameter("id", newsId);
                qry.executeUpdate();
                newsEntity.setPreviewImgId(null);
                emMicro.merge(newsEntity);
                qry = emMicro.createNamedQuery("removeImage");
                qry.setParameter("id", previewImageId);
                qry.executeUpdate();
            }
        }

    }
    @Override
    public void removeImagePerOneTransaction(Integer newsId) {
        CmsNewsEntity newsEntity = (CmsNewsEntity) findById(CmsNewsEntity.class,newsId);
        if (newsEntity != null) {
            if (newsEntity.getImgId() != null) {
                Integer imgId = newsEntity.getImgId();
                Query qry = emMicro.createNamedQuery("clearImgNews");
                qry.setParameter("id", newsId);
                qry.executeUpdate();
                newsEntity.setImgId(null);
                emMicro.merge(newsEntity);
                qry = emMicro.createNamedQuery("removeImage");
                qry.setParameter("id", imgId);
                qry.executeUpdate();
            }
        }

    }

    @Override
    public <T extends Serializable> Object findById(Class<T> t, Integer id){
        return (T) emMicro.find(t, id);
    }

    @Override
    public <T extends Serializable> List findAll(Class<T> t){
        CriteriaBuilder cb = emMicro.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(t);
        Root rootEntry = cq.from(t);
        CriteriaQuery all = cq.select(rootEntry);
        TypedQuery allQuery = emMicro.createQuery(all);
        return (List) allQuery.getResultList();
    }

    @Override
    public <T extends Serializable> void edit(T t){
        emMicro.merge(t);
    }

    @Override
    public <T extends Serializable> void create(T t){
        emMicro.persist(t);
    }
    @Override
    public <T extends Serializable> void remove(T t){
        emMicro.remove(emMicro.merge(t));
    }    
}
