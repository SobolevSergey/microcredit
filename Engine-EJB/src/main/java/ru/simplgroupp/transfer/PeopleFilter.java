package ru.simplgroupp.transfer;

import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.SortCriteria;

import java.io.Serializable;
import java.util.Set;

public class PeopleFilter implements Serializable {
    private Integer firstRow;
    private Integer rows;
    private SortCriteria[] sort;
    private String surname;
    private String name;
    private String midname;
    private DateRange dateBirth;
    private String paspNumber;
    private String paspSeries;
    private String inn;
    private String email;
    private String cellPhone;
    private String snils;
    private Boolean searchFull;
    private Boolean cleared = true;
    private Set options;
    /**
     * Оператор колл центра
     */
    private Boolean isCallCenterOnly = false;

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

    public SortCriteria[] getSort() {
        return sort;
    }

    public void setSort(SortCriteria[] sort) {
        this.sort = sort;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
        if (cellPhone != null) cleared = false;
    }

    public Boolean getCleared() {
        return cleared;
    }

    public void setCleared(Boolean cleared) {
        this.cleared = cleared;
    }

    public DateRange getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(DateRange dateBirth) {
        this.dateBirth = dateBirth;
        if (dateBirth != null && (dateBirth.getTo() != null || dateBirth.getFrom() != null)) cleared = false;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        if (email != null) cleared = false;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
        if (inn != null) cleared = false;
    }

    public String getMidname() {
        return midname;
    }

    public void setMidname(String midname) {
        this.midname = midname;
        if (midname != null) cleared = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (name != null) cleared = false;
    }

    public String getPaspNumber() {
        return paspNumber;
    }

    public void setPaspNumber(String paspNumber) {
        this.paspNumber = paspNumber;
        if (paspNumber != null) cleared = false;
    }

    public String getPaspSeries() {
        return paspSeries;
    }

    public void setPaspSeries(String paspSeries) {
        this.paspSeries = paspSeries;
        if (paspSeries != null) cleared = false;
    }

    public Boolean getSearchFull() {
        return searchFull;
    }

    public void setSearchFull(Boolean searchFull) {
        this.searchFull = searchFull;
        if (searchFull != null) cleared = false;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
        if (snils != null) cleared = false;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        if (surname != null) cleared = false;
    }

    public Set getOptions() {
        return options;
    }

    public void setOptions(Set options) {
        this.options = options;
    }

    public Boolean isCallCenterOnly() {
        return isCallCenterOnly;
    }

    public void setCallCenterOnly(Boolean callCenterOnly) {
        isCallCenterOnly = callCenterOnly;
    }
}
