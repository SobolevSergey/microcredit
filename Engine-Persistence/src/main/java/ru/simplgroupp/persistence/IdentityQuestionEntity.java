package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Вариант ответа на вопрос идентификации, сам ответ хранится в другой таблице
 */
public class IdentityQuestionEntity implements Serializable {
    private static final long serialVersionUID = 359353545509053972L;

    protected Integer txVersion = 0;

    private Integer id;

    /**
     * текст варианта
     */
    private String value;

    /**
     * вопрос
     */
    private IdentityTemplateEntity template;

    /**
     * кредит по которому задавали вопрос
     */
    private CreditEntity credit;

    private CreditRequestEntity creditRequest;

    private PeopleMainEntity peopleMain;

    private List<IdentityOptionEntity> options = new ArrayList<>();


    public IdentityQuestionEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CreditEntity getCredit() {
        return credit;
    }

    public void setCredit(CreditEntity credit) {
        this.credit = credit;
    }

    public IdentityTemplateEntity getTemplate() {
        return template;
    }

    public void setTemplate(IdentityTemplateEntity template) {
        this.template = template;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<IdentityOptionEntity> getOptions() {
        return options;
    }

    public void setOptions(List<IdentityOptionEntity> options) {
        this.options = options;
    }

    public PeopleMainEntity getPeopleMain() {
        return peopleMain;
    }

    public void setPeopleMain(PeopleMainEntity peopleMain) {
        this.peopleMain = peopleMain;
    }

    public CreditRequestEntity getCreditRequest() {
        return creditRequest;
    }

    public void setCreditRequest(CreditRequestEntity creditRequest) {
        this.creditRequest = creditRequest;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdentityQuestionEntity that = (IdentityQuestionEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return creditRequest.equals(that.creditRequest);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + creditRequest.hashCode();
        return result;
    }
}
