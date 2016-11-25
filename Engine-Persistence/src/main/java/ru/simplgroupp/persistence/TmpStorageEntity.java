package ru.simplgroupp.persistence;

import java.util.Date;

/**
 * Временное хранилище данных. Данные юудут удалены по прошествии {@link #getMaxAge()} от {@link #getCdate()}.
 */
public class TmpStorageEntity {
    private static final long serialVersionUID = 1L;

    protected Integer txVersion = 0;
    private Integer id;

    /**
     * Ключь
     */
    private String externalKey;

    /**
     * Тип
     */
    private String type;

    /**
     * Дата создния
     */
    private Date cdate;

    /**
     * Максиальный возраст в секундах
     */
    private Integer maxAge;

    /**
     * Данные
     */
    private byte[] data;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;

        if (other == this) return true;

        if (! (other.getClass() ==  getClass()))
            return false;

        TmpStorageEntity cast = (TmpStorageEntity) other;

        if (this.id == null) return false;

        if (cast.getId() == null) return false;

        if (this.id.intValue() != cast.getId().intValue())
            return false;

        return true;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getTxVersion() {
        return txVersion;
    }

    public void setTxVersion(Integer txVersion) {
        this.txVersion = txVersion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(String externalKey) {
        this.externalKey = externalKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
