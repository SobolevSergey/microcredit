package ru.simplgroupp.persistence.cms;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Parfenov
 * Date: 18.08.2015
 * Time: 0:54
 */
public class CmsDocumentsEntity implements Serializable {

    /**
     * идентификатор
     */
    private Integer id;
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
     * дата последео изменения
     */
    private Date dateChange;
    /**
     * ссылка на файл пдф
     */
    private String pdfUrl;
    /**
     * порядоковый номер отбражаения
     */
    private Integer orderInd;

    private String projectName;

    /**
     * Архивная запись или нет
     */
    private Integer isArchive;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
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


    public Date getDateChange() {
        return dateChange;
    }


    public void setDateChange(Date dateChange) {
        this.dateChange = dateChange;
    }


    public String getPdfUrl() {
        return pdfUrl;
    }


    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }


    public Integer getOrderInd() {
        return orderInd;
    }


    public void setOrderInd(Integer orderInd) {
        this.orderInd = orderInd;
    }


    public String getProjectName() {
        return projectName;
    }


    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Integer isArchive) {
        this.isArchive = isArchive;
    }
}
