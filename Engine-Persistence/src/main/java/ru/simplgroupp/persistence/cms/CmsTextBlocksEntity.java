package ru.simplgroupp.persistence.cms;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Parfenov
 * Date: 18.08.2015
 * Time: 1:18
 */
public class CmsTextBlocksEntity implements Serializable{

    /**
     * индентификатов
     */
    private Integer id;

    /**
     * наименование
     */
    private String name;
    /**
     * текст
     */
    private String body;
    /**
     * дата создания
     */
    private Date createDate;
    /**
     * дата послденего изменения
     */
    private Date dateChange;
    /**
     * ссылка на картинку
     */
    private Integer imgId;
    /**
     * порядоковый номер отбражаения
     */
    private Integer orderInd;


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


    public Integer getImgId() {
        return imgId;
    }


    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }


    public Integer getOrderInd() {
        return orderInd;
    }


    public void setOrderInd(Integer orderInd) {
        this.orderInd = orderInd;
    }
}
