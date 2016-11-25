package ru.simplgroupp.transfer;

import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.MoneyRange;
import ru.simplgroupp.toolkit.common.SortCriteria;

import java.io.Serializable;
import java.util.Set;

public class CreditFilter implements Serializable {
    private Integer firstRow;
    private Integer rows;
    private SortCriteria[] sorting;
    /**
     * номер
     */
    private String nomer;
    private DateRange databeg;
    private DateRange dataend;
    private DateRange dataendfact;
    private MoneyRange creditsum;
    /**
     * Фамилия
     */
    private String surname;
    /**
     * статус кредита
     */
    private Integer statusId;
    /**
     * завершен ли кредит
     */
    private Boolean isOver;
    /**
     * рабочее состояние
     */
    private Boolean isActive;
    /**
     * выдан своей организацией
     */
    private Boolean isSameorg;
    //по умолчанию показываем кредиты только свои
    private Integer partnerId = Partner.SYSTEM;
    /**
     * вид кредита
     */
    private Integer typeId;
    /**
     * ставка в день
     */
    private MoneyRange creditStake;
    /**
     * Человек
     */
    private Integer peopleMainId;
    /**
     * Платежная система
     */
    private Integer accountTypeId;
    private Integer workPlaceId;
    /**
     * Параметры инициализации
     */
    private Set options;
    /**
     * Установлено хоть одно значение в фильтре
     */
    private Boolean cleared = true;
    /**
     * Оператор колл центра
     */
    private Boolean isCallCenterOnly = false;
    /**
     * ID кредитного менеджера
     */
    private Integer creditManagerID;

    public Integer getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(Integer firstRow) {
        this.firstRow = firstRow;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public SortCriteria[] getSorting() {
        return sorting;
    }

    public void setSorting(SortCriteria[] sorting) {
        this.sorting = sorting;
    }

    public MoneyRange getCreditStake() {
        return creditStake;
    }

    public void setCreditStake(MoneyRange creditStake) {
        this.creditStake = creditStake;
        if (creditStake != null && (creditStake.getTo() != null || creditStake.getFrom() != null)) cleared = false;
    }

    public MoneyRange getCreditsum() {
        return creditsum;
    }

    public void setCreditsum(MoneyRange creditsum) {
        this.creditsum = creditsum;
        if (creditsum != null && (creditsum.getTo() != null || creditsum.getFrom() != null)) cleared = false;
    }

    public DateRange getDatabeg() {
        return databeg;
    }

    public void setDatabeg(DateRange databeg) {
        this.databeg = databeg;
        if (databeg != null && (databeg.getTo() != null || databeg.getFrom() != null)) cleared = false;
    }

    public DateRange getDataend() {
        return dataend;
    }

    public void setDataend(DateRange dataend) {
        this.dataend = dataend;
        if (dataend != null && (dataend.getTo() != null || dataend.getFrom() != null)) cleared = false;
    }

    public DateRange getDataendfact() {
        return dataendfact;
    }

    public void setDataendfact(DateRange dataendfact) {
        this.dataendfact = dataendfact;
        if (dataendfact != null && (dataendfact.getTo() != null || dataendfact.getFrom() != null)) cleared = false;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
        if (isActive != null) cleared = false;
    }

    public Boolean getIsSameorg() {
        return isSameorg;
    }

    public void setIsSameorg(Boolean isSameorg) {
        this.isSameorg = isSameorg;
        if (isSameorg != null) cleared = false;
    }

    public Boolean getIsOver() {
        return isOver;
    }

    public void setIsOver(Boolean isOver) {
        this.isOver = isOver;
        if (isOver != null) cleared = false;
    }

    public String getNomer() {
        return nomer;
    }

    public void setNomer(String nomer) {
        this.nomer = nomer;
        if (nomer != null) cleared = false;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
        if (statusId != null) cleared = false;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        if (surname != null) cleared = false;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
        if (typeId != null) cleared = false;
    }

    public Integer getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(Integer accountTypeId) {
        this.accountTypeId = accountTypeId;
        if (accountTypeId != null) cleared = false;
    }

    public Integer getWorkPlaceId() {
        return workPlaceId;
    }

    public void setWorkPlaceId(Integer workPlaceId) {
        this.workPlaceId = workPlaceId;
        if (workPlaceId != null) cleared = false;
    }

    public Set getOptions() {
        return options;
    }

    public void setOptions(Set options) {
        this.options = options;
    }

    public Integer getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(Integer peopleMainId) {
        this.peopleMainId = peopleMainId;
        if (peopleMainId != null) cleared = false;
    }

    public Boolean getCleared() {
        return cleared;
    }

    public void setCleared(Boolean cleared) {
        this.cleared = cleared;
    }

    public Boolean isCallCenterOnly() {
        return isCallCenterOnly;
    }

    public void setCallCenterOnly(Boolean callCenterOnly) {
        isCallCenterOnly = callCenterOnly;
    }

    public Integer getCreditManagerID() {
        return creditManagerID;
    }

    public void setCreditManagerID(Integer creditManagerID) {
        this.creditManagerID = creditManagerID;
        if (creditManagerID != null) cleared = false;
    }
}
