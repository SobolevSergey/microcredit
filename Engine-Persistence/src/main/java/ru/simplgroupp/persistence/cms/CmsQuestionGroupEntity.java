package ru.simplgroupp.persistence.cms;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Parfenov
 * Date: 18.08.2015
 * Time: 0:43
 *
 * категории вопрос
 */
public class CmsQuestionGroupEntity implements Serializable {

    /**
     * индентификатов
     */
    private Integer id;

    /**
     * наименование
     */
    private String name;

    /**
     * порядок отображения
     */
    private Integer orderInd;

    /**
     * показыват на главное странице
     */
    private Integer onMainShow;
    /**
     * показывать в фуутер
      */
    private Integer onFooterShow;
    /**
     * дата создания
     */
    private Date createDate;
    /**
     * дата последео изменения
     */
    private Date dateChange;

    private String projectName;


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


    public Integer getOrderInd() {
        return orderInd;
    }


    public void setOrderInd(Integer orderInd) {
        this.orderInd = orderInd;
    }


    public Integer getOnMainShow() {
        return onMainShow;
    }


    public void setOnMainShow(Integer onMainShow) {
        this.onMainShow = onMainShow;
    }


    public Integer getOnFooterShow() {
        return onFooterShow;
    }


    public void setOnFooterShow(Integer onFooterShow) {
        this.onFooterShow = onFooterShow;
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


    public String getProjectName() {
        return projectName;
    }


    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
