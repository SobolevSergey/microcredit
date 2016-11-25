package ru.simplgroupp.transfer;

import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.IntegerRange;
import ru.simplgroupp.toolkit.common.MoneyRange;
import ru.simplgroupp.toolkit.common.SortCriteria;

import java.io.Serializable;
import java.util.Set;

/**
 * Фильтр для запроса списков кредитных заявок
 */
public class CreditRequestFilter implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1099354561679023945L;
    /**
     * ID заявки
     */
    private Integer requestID;
    /**
     * первый номер
     */
    private Integer nFirst;
    /**
     * на страницу
     */
    private Integer nCount;
    /**
     * сортировка
     */
    private SortCriteria[] sorting;
    /**
     * что инициализируем
     */
    private Set options;
    /**
     * id человека
     */
    private Integer peopleMainId;
    /**
     * причина отказа
     */
    private Integer rejectReasonId;
    /**
     * Детальная причина отказа
     */
    private Integer refuseReasonId;
    /**
     * статус заявки
     */
    private Integer statusId;
    /**
     * одобрена или нет
     */
    private Integer accepted;
    /**
     * дата подачи
     */
    private DateRange date;
    /**
     * дней кредита
     */
    private IntegerRange days;
    /**
     * сумма кредита
     */
    private MoneyRange sum;
    /**
     * закрыта
     */
    private Boolean isover;
    /**
     * дата окончания кредита
     */
    private DateRange creditDateEnd;
    /**
     * фамилия
     */
    private String peopleSurname;
    /**
     * имя
     */
    private String peopleName;
    /**
     * отчество
     */
    private String peopleMidname;
    /**
     * вид документа
     */
    private Integer docTypeId;
    /**
     * серия документа
     */
    private String docSeries;
    /**
     * номер документа
     */
    private String docNomer;
    /**
     * email
     */
    private String peopleEmail;
    /**
     * мобильный телефон
     */
    private String peoplePhone;
    /**
     * снилс
     */
    private String peopleSNILS;
    /**
     * инн
     */
    private String peopleINN;
    /**
     * номер заявки
     */
    private String uniqueNomer;
    /**
     * способ подачи заявки
     */
    private Integer wayId;
    /**
     * Способ принятия решения
     */
    private Integer decisionWayID;
    /**
     * подразделение где выдана заявка
     */
    private Integer workplaceID;
    private String taskId;
    private String taskDefKey;

    /**
     * Есть ли выданный кредит
     */
    private Integer acceptedcredit_id;
    /**
     * Просрочен ли кредит
     */
    private Integer isOverdue;
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
    /**
     * Любой тип телефона
     */
    private String peopleAnyPhone;
    /**
     * Первый или повторные кредиты
     */
    private Boolean isFirstCredit;

    public Integer getRequestID() {
        return requestID;
    }

    public void setRequestID(Integer requestID) {
        this.requestID = requestID;
        if (requestID != null) cleared = false;
    }

    public Integer getAccepted() {
        return accepted;
    }

    public void setAccepted(Integer accepted) {
        this.accepted = accepted;
        if (accepted != null) cleared = false;
    }

    public DateRange getCreditDateEnd() {
        return creditDateEnd;
    }

    public void setCreditDateEnd(DateRange creditDateEnd) {
        this.creditDateEnd = creditDateEnd;
        if (creditDateEnd != null && (creditDateEnd.getTo() != null || creditDateEnd.getFrom() != null)) cleared = false;
    }

    public DateRange getDate() {
        return date;
    }

    public void setDate(DateRange date) {
        this.date = date;
        if (date != null && (date.getTo() != null || date.getFrom() != null)) cleared = false;
    }

    public IntegerRange getDays() {
        return days;
    }

    public void setDays(IntegerRange days) {
        this.days = days;
        if (days != null && (days.getTo() != null || days.getFrom() != null)) cleared = false;
    }

    public String getDocNomer() {
        return docNomer;
    }

    public void setDocNomer(String docNomer) {
        this.docNomer = docNomer;
        if (docNomer != null) cleared = false;
    }

    public String getDocSeries() {
        return docSeries;
    }

    public void setDocSeries(String docSeries) {
        this.docSeries = docSeries;
        if (docSeries != null) cleared = false;
    }

    public Integer getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(Integer docTypeId) {
        this.docTypeId = docTypeId;
        if (docTypeId != null) cleared = false;
    }

    public Boolean getIsover() {
        return isover;
    }

    public void setIsover(Boolean isover) {
        this.isover = isover;
        if (isover != null) cleared = false;
    }

    public Integer getnCount() {
        return nCount;
    }

    public void setnCount(Integer nCount) {
        this.nCount = nCount;
    }

    public Integer getnFirst() {
        return nFirst;
    }

    public void setnFirst(Integer nFirst) {
        this.nFirst = nFirst;
    }

    public Set getOptions() {
        return options;
    }

    public void setOptions(Set options) {
        this.options = options;
    }

    public String getPeopleEmail() {
        return peopleEmail;
    }

    public void setPeopleEmail(String peopleEmail) {
        this.peopleEmail = peopleEmail;
        if (peopleEmail != null) cleared = false;
    }

    public String getPeopleINN() {
        return peopleINN;
    }

    public void setPeopleINN(String peopleINN) {
        this.peopleINN = peopleINN;
        if (peopleINN != null) cleared = false;
    }

    public Integer getPeopleMainId() {
        return peopleMainId;
    }

    public void setPeopleMainId(Integer peopleMainId) {
        this.peopleMainId = peopleMainId;
        if (peopleMainId != null) cleared = false;
    }

    public String getPeopleMidname() {
        return peopleMidname;
    }

    public void setPeopleMidname(String peopleMidname) {
        this.peopleMidname = peopleMidname;
        if (peopleMidname != null) cleared = false;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
        if (peopleName != null) cleared = false;
    }

    public String getPeoplePhone() {
        return peoplePhone;
    }

    public void setPeoplePhone(String peoplePhone) {
        this.peoplePhone = peoplePhone;
        if (peoplePhone != null) cleared = false;
    }

    public String getPeopleSNILS() {
        return peopleSNILS;
    }

    public void setPeopleSNILS(String peopleSNILS) {
        this.peopleSNILS = peopleSNILS;
        if (peopleSNILS != null) cleared = false;
    }

    public String getPeopleSurname() {
        return peopleSurname;
    }

    public void setPeopleSurname(String peopleSurname) {
        this.peopleSurname = peopleSurname;
        if (peopleSurname != null) cleared = false;
    }

    public Integer getRejectReasonId() {
        return rejectReasonId;
    }

    public void setRejectReasonId(Integer rejectReasonId) {
        this.rejectReasonId = rejectReasonId;
        if (rejectReasonId != null) cleared = false;
    }

    public Integer getRefuseReasonId() {
        return refuseReasonId;
    }

    public void setRefuseReasonId(Integer refuseReasonId) {
        this.refuseReasonId = refuseReasonId;
        if (refuseReasonId != null) cleared = false;
    }

    public SortCriteria[] getSorting() {
        return sorting;
    }

    public void setSorting(SortCriteria[] sorting) {
        this.sorting = sorting;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
        if (statusId != null) cleared = false;
    }

    public MoneyRange getSum() {
        return sum;
    }

    public void setSum(MoneyRange sum) {
        this.sum = sum;
        if (sum != null && (sum.getFrom() != null || sum.getTo() != null)) cleared = false;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
        if (taskId != null) cleared = false;
    }

    public String getUniqueNomer() {
        return uniqueNomer;
    }

    public void setUniqueNomer(String uniqueNomer) {
        this.uniqueNomer = uniqueNomer;
        if (uniqueNomer != null) cleared = false;
    }

    public Integer getWayId() {
        return wayId;
    }

    public void setWayId(Integer wayId) {
        this.wayId = wayId;
        if (wayId != null) cleared = false;
    }

    public Integer getDecisionWayID() {
        return decisionWayID;
    }

    public void setDecisionWayID(Integer decisionWayID) {
        this.decisionWayID = decisionWayID;
        if (decisionWayID != null) cleared = false;
    }

    public Integer getWorkplaceID() {
        return workplaceID;
    }

    public void setWorkplaceID(Integer workplaceID) {
        this.workplaceID = workplaceID;
        if (workplaceID != null) cleared = false;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
        if (taskDefKey != null) cleared = false;
    }

    public Integer getAcceptedcredit_id() {
        return acceptedcredit_id;
    }

    public void setAcceptedcredit_id(Integer acceptedcredit_id) {
        this.acceptedcredit_id = acceptedcredit_id;
        if (acceptedcredit_id != null) cleared = false;
    }

    public Integer getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(Integer isOverdue) {
        this.isOverdue = isOverdue;
        if (isOverdue != null) cleared = false;
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

    public String getPeopleAnyPhone() {
        return peopleAnyPhone;
    }

    public void setPeopleAnyPhone(String peopleAnyPhone) {
        this.peopleAnyPhone = peopleAnyPhone;
        if (peopleAnyPhone != null) cleared = false;
    }

    public Boolean getFirstCredit() {
        return isFirstCredit;
    }

    public void setFirstCredit(Boolean firstCredit) {
        isFirstCredit = firstCredit;
    }
}
