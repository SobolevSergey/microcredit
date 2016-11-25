package ru.simplgroupp.persistence.cms;

import java.io.Serializable;
import java.util.List;

/**
 * CMS для страниц
 */
public class CmsPageEntity implements Serializable{

    /**
     * идентификатор
     */
    private Integer id;
    /**
     * тип, уникальный
     * для кажой страничкий своей тип
     */
    private String type;
    /**
     * метатэги странички
     */
    private String description;
    private String title;
    private String keywords;
    /**
     * Изменяемый контент странички в формате json
     */
    private String body;

    /**
     * лист картинок для страницы
     */
    private List<CmsImagesEntity> images;

    /**
     * названием проекта
     * для мульти поддержки нескольких проектов
     */
    private String projectName;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getKeywords() {
        return keywords;
    }


    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }


    public String getBody() {
        return body;
    }


    public void setBody(String body) {
        this.body = body;
    }


    public List<CmsImagesEntity> getImages() {
        return images;
    }


    public void setImages(List<CmsImagesEntity> images) {
        this.images = images;
    }


    public String getProjectName() {
        return projectName;
    }


    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CmsPageEntity that = (CmsPageEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (keywords != null ? !keywords.equals(that.keywords) : that.keywords != null) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (images != null ? !images.equals(that.images) : that.images != null) return false;
        return !(projectName != null ? !projectName.equals(that.projectName) : that.projectName != null);

    }


    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (keywords != null ? keywords.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        result = 31 * result + (projectName != null ? projectName.hashCode() : 0);
        return result;
    }
}
