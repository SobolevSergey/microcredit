package ru.simplgroupp.persistence;

import javax.persistence.PrePersist;
import java.io.Serializable;
import java.util.Date;

/**
 * Ответ на вопрос идентификации
 */
public class IdentityAnswerEntity implements Serializable {
    private static final long serialVersionUID = 8014806581154373719L;

    protected Integer txVersion = 0;

    private Integer id;

    /**
     * вариант ответа, в нем хранится флаг правильный вариант или нет
     */
    private IdentityOptionEntity option;

    /**
     * дата когда был создан
     */
    private Date createdAt;


    public IdentityAnswerEntity() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public IdentityOptionEntity getOption() {
        return option;
    }

    public void setOption(IdentityOptionEntity option) {
        this.option = option;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdentityAnswerEntity that = (IdentityAnswerEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(option != null ? !option.equals(that.option) : that.option != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (option != null ? option.hashCode() : 0);
        return result;
    }
}