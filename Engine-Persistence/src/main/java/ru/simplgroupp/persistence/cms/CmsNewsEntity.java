package ru.simplgroupp.persistence.cms;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Parfenov
 * Date: 18.08.2015
 * Time: 0:51
 *
 * новость
 */
public class CmsNewsEntity implements Serializable {

    /**
     * идентификатор
     */
    private Integer id;

    /**
     * метатэги странички
     */
    private String description;
    private String title;
    private String keywords;
    /**
     * наименвоание
     */
    private String name;
    /**
     * текст новости
     */
    private String body;
    /**
     * дата создания
     */
    private Date createDate;
    /**
     * ссылка на картинку предпросмотра
     */
    private Integer previewImgId;
    /**
     * ссылка на картинку новости
     */
    private Integer imgId;
    /**
     * дата послденего изменения
     */
    private Date dateChange;

    private String projectName;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
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


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getBody() {
        return body;
    }


    public void setBody(String body) {
        this.body = body;
    }


    public Date getCreateDate() {
        return createDate;
    }


    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    public Integer getPreviewImgId() {
        return previewImgId;
    }


    public void setPreviewImgId(Integer previewImgId) {
        this.previewImgId = previewImgId;
    }


    public Integer getImgId() {
        return imgId;
    }


    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }


    public Date getDateChange() {
        return dateChange;
    }


    public void setDateChange(Date dateChange) {
        this.dateChange = dateChange;
    }


    public String getProjectName() {
        return projectName;
    }


    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
