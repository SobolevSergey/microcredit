package ru.simplgroupp.dao.interfaces.cms;

import ru.simplgroupp.persistence.cms.CmsDocumentsEntity;
import ru.simplgroupp.persistence.cms.CmsImagesEntity;
import ru.simplgroupp.persistence.cms.CmsNewsEntity;
import ru.simplgroupp.persistence.cms.CmsPageEntity;
import ru.simplgroupp.persistence.cms.CmsQuestionGroupEntity;
import ru.simplgroupp.persistence.cms.CmsQuestionsEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PARFENOV on 11.06.2015.
 */
public interface CmsServiceDAO {

    CmsPageEntity getCmsPageByType(String type, String projectName);

    CmsPageEntity save(CmsPageEntity cmsPageEntity);

    CmsImagesEntity saveImage(CmsImagesEntity cmsImagesEntity);

    List<CmsImagesEntity> getCmsImageByPageId(Integer pageId);

    List<CmsNewsEntity> getCmsNewsEntityPagi(String projectName, Integer limit, Integer offset);

    List<CmsNewsEntity> getCmsNewsEntityAll(String projectName);

    List<CmsQuestionGroupEntity> getCmsQuestionGroupEntityAll(String projectName);

    List<CmsDocumentsEntity> getCmsDocumentEntityAll(String projectName);

    List<CmsDocumentsEntity> getCmsDocumentEntityAll(String projectName, Boolean isArchive);

    List<CmsQuestionsEntity> getCmsQuestionByGroupId(Integer groupId);

    List<CmsQuestionsEntity> getCmsQuestionPagi(String projectName, Integer limit, Integer offset);

    Integer countCmsNews(String projectName);

    Integer countCmsQuestions(String projectName);    

    void removeImage(Integer id);

    void removePreviewImagePerOneTransaction(Integer newsId);

    void removeImagePerOneTransaction(Integer newsId);

    <T extends Serializable> Object findById(Class<T> t, Integer id);

    <T extends Serializable> List findAll(Class<T> t);

    <T extends Serializable> void edit(T t);

    <T extends Serializable> void create(T t);

    <T extends Serializable> void remove(T t);    
}
