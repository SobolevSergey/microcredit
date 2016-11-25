package ru.simplgroupp.persistence.cms;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Parfenov
 * Date: 18.08.2015
 * Time: 0:47
 *
 * Вопрос ответ
 */
public class CmsQuestionsEntity implements Serializable {
    /**
     * индентификаторв
     */
    private Integer id;
    /**
     * категория
     */
    private CmsQuestionGroupEntity group;
    /**
     * текст вопроса
     */
    private String question;
    /**
     * текст овтета
     */
    private String answer;

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
    /**
     * порядок отображения
     */
    private Integer orderInd;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public CmsQuestionGroupEntity getGroup() {
        return group;
    }


    public void setGroup(CmsQuestionGroupEntity group) {
        this.group = group;
    }


    public String getQuestion() {
        return question;
    }


    public void setQuestion(String question) {
        this.question = question;
    }


    public String getAnswer() {
        return answer;
    }


    public void setAnswer(String answer) {
        this.answer = answer;
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


    public Integer getOrderInd() {
        return orderInd;
    }


    public void setOrderInd(Integer orderInd) {
        this.orderInd = orderInd;
    }
}
