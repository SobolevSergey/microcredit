package ru.simplgroupp.webapp.controller;

import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.interfaces.QuestionBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.ReportBeanLocal;
import ru.simplgroupp.interfaces.UsersBeanLocal;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.interfaces.service.OrganizationService;
import ru.simplgroupp.interfaces.service.RequestsService;
import ru.simplgroupp.interfaces.service.WorkplaceService;
import ru.simplgroupp.persistence.WorkplaceEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.*;
import ru.simplgroupp.webapp.util.JSFUtils;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReferenceController extends AbstractController implements Serializable {
    private static final long serialVersionUID = -624684282956275635L;

    @EJB
    protected ReferenceBooksLocal refBook;

    @EJB
    protected QuestionBeanLocal questBean;

    @EJB
    protected UsersBeanLocal userBean;

    @EJB
    EventLogService eventLog;

    @EJB
    ReportBeanLocal reportBean;

    @EJB
    RequestsService requestsService;

    @EJB
    OrganizationService orgService;

    @EJB
    ProductDAO productDAO;

    @EJB
    WorkplaceService workplaceService;

    public String getRefText(Integer qid) {
        return questBean.findQuestionVariantText(qid);
    }

    public List<SelectItem> getEventCodes() {
        List<EventCode> lst = eventLog.getEventCodes();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);
        for (EventCode status : lst) {
            si = new SelectItem();
            si.setValue(status.getId());
            si.setLabel(status.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getReportTypes() {
        List<ReportType> lst = reportBean.getReportTypes();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);
        for (ReportType report : lst) {
            si = new SelectItem();
            si.setValue(report.getId());
            si.setLabel(report.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getReportTypesRef() {
        List<ReportType> lst = reportBean.getReportTypes();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());

        for (ReportType report : lst) {
            SelectItem si = new SelectItem();
            si.setValue(report);
            si.setLabel(report.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getCreditRequestStatuses() {
        List<CreditStatus> lst = refBook.getCreditRequestStatuses();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());

        for (CreditStatus status : lst) {
            SelectItem si = new SelectItem();
            si.setValue(status.getId());
            si.setLabel(status.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getCreditRequestStatusesObj() {
        List<CreditStatus> lst = refBook.getCreditRequestStatuses();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());

        for (CreditStatus status : lst) {
            SelectItem si = new SelectItem();
            si.setValue(status);
            si.setLabel(status.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getRequestStatuses() {
        List<RequestStatus> lst = requestsService.getRequestStatuses();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);
        for (RequestStatus status : lst) {
            si = new SelectItem();
            si.setValue(status.getId());
            si.setLabel(status.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getRoles() {
        List<Roles> lst = userBean.getRoles();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);
        for (Roles role : lst) {
            si = new SelectItem();
            si.setValue(role.getId());
            si.setLabel(role.getRealName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getRolesRef() {
        List<Roles> lst = userBean.getRoles();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());

        for (Roles role : lst) {
            SelectItem si = new SelectItem();
            si.setValue(role);
            si.setLabel(role.getRealName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getOrganizations() {
        List<Organization> organizations = orgService.findOrganizations();

        ArrayList<SelectItem> items = new ArrayList<SelectItem>(organizations.size());

        for (Organization organization : organizations) {
            items.add(new SelectItem(organization.getId(), organization.getName()));
        }
        return items;
    }

    public List<SelectItem> getOrganizationsRefNull() {
        List<Organization> lst = orgService.findOrganizations();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());

        for (Organization org : lst) {
            SelectItem si = new SelectItem();
            si.setValue(org);
            si.setLabel(org.getName());
            lstCon.add(si);
        }

        SelectItem si = new SelectItem();
        Organization nref = new Organization();
        nref.setId(JSFUtils.NULL_INT_VALUE);
        nref.setName(JSFUtils.NULL_LABEL);
        si.setValue(nref);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(0, si);

        return lstCon;
    }

    public List<SelectItem> getUserTypes() {
        List<UserType> lst = userBean.getUserTypes();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);
        for (UserType utype : lst) {
            si = new SelectItem();
            si.setValue(utype.getId());
            si.setLabel(utype.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getCreditOrganizations() {
        List<Organization> lst = orgService.getCreditOrganizations();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());

        for (Organization org : lst) {
            SelectItem si = new SelectItem();
            si.setValue(org.getId());
            si.setLabel(org.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getProducts() {
        List<Products> lst = productDAO.getProductsActive(null);

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());

        for (Products prod : lst) {
            SelectItem si = new SelectItem();
            si.setValue(prod);
            si.setLabel(prod.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getProductsId() {
        List<Products> lst = productDAO.getProductsActive(null);

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());

        for (Products prod : lst) {
            SelectItem si = new SelectItem();
            si.setValue(prod.getId());
            si.setLabel(prod.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getCreditOrganizationsNull() {
        List<Organization> lst = orgService.getCreditOrganizations();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);
        for (Organization org : lst) {
            si = new SelectItem();
            si.setValue(org.getId());
            si.setLabel(org.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getRefHeadersSystemNull() {
        List<RefHeader> lst = refBook.listRefHeaders(Partner.SYSTEM, "", true);

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);
        for (RefHeader rh : lst) {
            si = new SelectItem();
            si.setValue(rh.getId());
            si.setLabel(rh.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getBanksRef() {
        List<Bank> lst = refBook.getBanksList();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());

        for (Bank rh : lst) {
            SelectItem si = new SelectItem();
            si.setValue(rh);
            si.setLabel(rh.getName());
            lstCon.add(si);
        }
        return lstCon;
    }


    public List<SelectItem> getContactPointsNull(String city) {
        List<ContactPoints> lst = refBook.getContactPoints(city);
        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);
        for (ContactPoints cp : lst) {
            si = new SelectItem();
            si.setValue(cp.getId());
            si.setLabel(cp.getAddress1());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getBanksNull() {
        List<Bank> lst = refBook.getBanksList();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);
        for (Bank rh : lst) {
            si = new SelectItem();
            si.setValue(rh.getBik());
            si.setLabel(rh.getBik() + ", " + rh.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getRefHeadersNull() {
        List<RefHeader> lst = refBook.listRefHeaders(null, "", true);

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);
        for (RefHeader rh : lst) {
            si = new SelectItem();
            si.setValue(rh.getId());
            si.setLabel(rh.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getPartners() {
        List<Partner> lst = refBook.getPartners();
        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        for (Partner par : lst) {
            SelectItem si = new SelectItem();
            si.setValue(par.getId());
            si.setLabel(par.getRealName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getPartnersNull() {
        List<Partner> lst = refBook.getPartners();
        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);
        for (Partner par : lst) {
            si = new SelectItem();
            si.setValue(par.getId());
            si.setLabel(par.getRealName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getPartnersForPaymentNull() {
        List<Partner> lst = refBook.getPartnersForPayment();
        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);
        for (Partner par : lst) {
            si = new SelectItem();
            si.setValue(par.getId());
            si.setLabel(par.getRealName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getPartnersForCreditHistoryNull() {
        List<Partner> lst = refBook.getPartnersForCreditHistory();
        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);
        for (Partner par : lst) {
            si = new SelectItem();
            si.setValue(par.getId());
            si.setLabel(par.getRealName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getPartnersForScoringNull() {
        List<Partner> lst = refBook.getPartnersForScoring();
        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);
        for (Partner par : lst) {
            si = new SelectItem();
            si.setValue(par.getId());
            si.setLabel(par.getRealName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getMonthesNumeric() {
        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(12);
        for (int i = 1; i <= 12; i++) {
            SelectItem si = new SelectItem();
            si.setValue(new Integer(i));
            si.setLabel(String.valueOf(i));
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getCreditRequestStatusesNull() {
        List<CreditStatus> lst = refBook.getCreditRequestStatuses();

        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(lst.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);

        for (CreditStatus status : lst) {
            si = new SelectItem();
            si.setValue(status.getId());
            si.setLabel(status.getName());
            lstCon.add(si);
        }
        return lstCon;
    }

    public List<SelectItem> getDecisionWaysNull() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.getDecisionWayTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getRejectReasons() {
        return JSFUtils.referenceToSelect(refBook.getRefusalReasonTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getRefuseReasons() {

        List<RefuseReason> source = refBook.getRefuseReasonsForDecision();
        ArrayList<SelectItem> lstRes = new ArrayList<SelectItem>(source.size());
        for (RefuseReason ref : source) {
            SelectItem si = new SelectItem();
            si.setValue(ref.getId());
            si.setLabel(ref.getName());
            lstRes.add(si);
        }

        return lstRes;
    }

    public List<SelectItem> getRefuseReasonsAll() {

        List<RefuseReason> source = refBook.getRefuseReasons();
        ArrayList<SelectItem> lstRes = new ArrayList<SelectItem>(source.size());
        for (RefuseReason ref : source) {
            SelectItem si = new SelectItem();
            si.setValue(ref.getId());
            si.setLabel(ref.getName());
            lstRes.add(si);
        }

        return lstRes;
    }

    public List<SelectItem> getRefuseReasonsAllNull() {

        List<RefuseReason> source = refBook.getRefuseReasons();
        ArrayList<SelectItem> lstRes = new ArrayList<SelectItem>(source.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstRes.add(si);
        for (RefuseReason ref : source) {
            si = new SelectItem();
            si.setValue(ref.getId());
            si.setLabel(ref.getName());
            lstRes.add(si);
        }

        return lstRes;
    }

    public List<SelectItem> getRejectReasonsType() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.getRefuseReasonStateTypes());
    }

    public List<SelectItem> getRejectReasonsTypes() {
        return JSFUtils.referenceToSelect(refBook.getRefuseReasonStateTypes());
    }

    public List<SelectItem> getRejectReasonsTypesRef() {
        return JSFUtils.referenceToSelectRef(refBook.getRefuseReasonStateTypes());
    }

    public List<SelectItem> getRejectReasonsTypeNull() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.getRefuseReasonStateTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getRejectReasonsCode() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.getRefusalReasonTypes());
    }

    public List<SelectItem> getGendersRef() {
        return JSFUtils.referenceToSelectRef(refBook.getGenders());
    }

    public List<SelectItem> getGenders() {
        return JSFUtils.referenceToSelect(refBook.getGenders());
    }

    public List<SelectItem> getGenderCodes() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.getGenders());
    }

    public List<SelectItem> getGendersNull() {
        return JSFUtils.referenceToSelect(refBook.getGenders(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getMarriageTypesRef() {
        return JSFUtils.referenceToSelectRef(refBook.getMarriageTypes());
    }

    public List<SelectItem> getMarriageTypes() {
        return JSFUtils.referenceToSelect(refBook.getMarriageTypes());
    }

    public List<SelectItem> getMarriageTypeCodes() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.getMarriageTypes());
    }

    public List<SelectItem> getMarriageTypesNull() {
        return JSFUtils.referenceToSelect(refBook.getMarriageTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getMarriageTypesRefNull() {
        return JSFUtils.referenceToSelectRef(refBook.getMarriageTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getRealtyTypesRef() {
        return JSFUtils.referenceToSelectRef(refBook.getRealtyTypes());
    }

    public List<SelectItem> getRealtyTypes() {
        return JSFUtils.referenceToSelect(refBook.getRealtyTypes());
    }

    public List<SelectItem> getRealtyTypesCodeInteger() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.getRealtyTypes());
    }

    public List<SelectItem> getRealtyTypesNull() {
        return JSFUtils.referenceToSelect(refBook.getRealtyTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getRealtyTypesRefNull() {
        return JSFUtils.referenceToSelectRef(refBook.getRealtyTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getRealtyDateRef() {
        return JSFUtils.referenceToSelectRef(refBook.getTimeRanges());
    }

    public List<SelectItem> getRealtyDateNull() {
        return JSFUtils.referenceToSelectRef(refBook.getTimeRanges(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getBlackListReasonTypesNull() {
        return JSFUtils.referenceToSelect(refBook.getBlackListReasonTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getBlackListSourceTypesNull() {
        return JSFUtils.referenceToSelect(refBook.listReference(RefHeader.SOURCE_BLACKLIST_TYPES), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getBlackListReasonTypesCodeInteger() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.getBlackListReasonTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getBlackListSourceTypesCodeInteger() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.listReference(RefHeader.SOURCE_BLACKLIST_TYPES), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getTypeWorks() {
        return JSFUtils.referenceToSelect(refBook.getEmployTypes());

    }

    public List<SelectItem> getTypeWorkCodes() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.getEmployTypes());
    }

    public List<SelectItem> getApplicationWays() {
        return JSFUtils.referenceToSelect(refBook.getApplicationWays());

    }

    public List<SelectItem> getTypeWorksNull() {
        return JSFUtils.referenceToSelect(refBook.getEmployTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getTypeWorksRef() {
        return JSFUtils.referenceToSelectRef(refBook.getEmployTypes());

    }

    public List<SelectItem> getTypeWorksRefNull() {
        return JSFUtils.referenceToSelectRef(refBook.getEmployTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getExtSalaries() {
        return JSFUtils.referenceToSelect(refBook.getExtSalaryTypes());

    }

    public List<SelectItem> getExtSalariesNull() {
        return JSFUtils.referenceToSelect(refBook.getExtSalaryTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getExtSalariesRef() {
        return JSFUtils.referenceToSelectRef(refBook.getExtSalaryTypes());

    }

    public List<SelectItem> getExtSalariesRefNull() {
        return JSFUtils.referenceToSelectRef(refBook.getExtSalaryTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getEducationTypes() {
        return JSFUtils.referenceToSelect(refBook.getEducationTypes());
    }

    public List<SelectItem> getEducationTypeCodes() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.getEducationTypes());
    }

    public List<SelectItem> getEducationTypesNull() {
        return JSFUtils.referenceToSelect(refBook.getEducationTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getEducationTypesRef() {
        return JSFUtils.referenceToSelectRef(refBook.getEducationTypes());

    }

    public List<SelectItem> getEducationTypesRefNull() {
        return JSFUtils.referenceToSelectRef(refBook.getEducationTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getCreditStatuses() {
        return JSFUtils.referenceToSelect(refBook.getCreditStatusTypes());

    }

    public List<SelectItem> getCreditStatusesNull() {
        return JSFUtils.referenceToSelect(refBook.getCreditStatusTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getCreditFreqPaymentTypes() {
        return JSFUtils.referenceToSelect(refBook.getCreditFreqPaymentTypes());

    }

    public List<SelectItem> getCreditFreqPaymentTypesNull() {
        return JSFUtils.referenceToSelect(refBook.getCreditFreqPaymentTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getCreditTypes() {
        return JSFUtils.referenceToSelect(refBook.getCreditTypes());
    }

    public List<SelectItem> getCreditTypesRef() {
        return JSFUtils.referenceToSelectRef(refBook.getCreditTypes());
    }

    public List<SelectItem> getCreditTypesRefNull() {
        return JSFUtils.referenceToSelectRef(refBook.getCreditTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getCreditPurposeRef() {
        return JSFUtils.referenceToSelectRef(refBook.getCreditPurposeTypes());
    }

    public List<SelectItem> getCreditPurposeRefNull() {
        return JSFUtils.referenceToSelectRef(refBook.getCreditPurposeTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getCurrency() {
        return JSFUtils.referenceToSelect(refBook.getCurrencyTypes());
    }

    public List<SelectItem> getCurrencyRef() {
        return JSFUtils.referenceToSelectRef(refBook.getCurrencyTypes());
    }

    public List<SelectItem> getCurrencyRefNull() {
        return JSFUtils.referenceToSelectRef(refBook.getCurrencyTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getOverdueTypesRef() {
        return JSFUtils.referenceToSelectRef(refBook.getCreditOverdueTypes());
    }

    public List<SelectItem> getOverdueTypes() {
        return JSFUtils.referenceToSelect(refBook.getCreditOverdueTypes());
    }

    public List<SelectItem> getOverdueTypesRefNull() {
        return JSFUtils.referenceToSelectRef(refBook.getCreditOverdueTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getCreditTypesNull() {
        return JSFUtils.referenceToSelect(refBook.getCreditTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getProfessionTypes() {
        return JSFUtils.referenceToSelect(refBook.getProfessionTypes());

    }

    public List<SelectItem> getProfessionTypesNull() {
        return JSFUtils.referenceToSelect(refBook.getProfessionTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getProfessionTypesRef() {
        return JSFUtils.referenceToSelectRef(refBook.getProfessionTypes());

    }

    public List<SelectItem> getOrganizationTypes() {
        return JSFUtils.referenceToSelect(refBook.getOrganizationTypes());
    }

    public List<SelectItem> getOrganizationTypeCodes() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.getOrganizationTypes());
    }

    public List<SelectItem> getOrganizationTypesNull() {
        return JSFUtils.referenceToSelect(refBook.getOrganizationTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getOrganizationByTypes() {
        return JSFUtils.referenceToSelect(refBook.getOrganizationByTypes());

    }

    public List<SelectItem> getOrganizationByTypesNull() {
        return JSFUtils.referenceToSelect(refBook.getOrganizationByTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getOrganizationTypesRef() {
        return JSFUtils.referenceToSelectRef(refBook.getOrganizationTypes());

    }

    public List<SelectItem> getOrganizationTypesRefNull() {
        return JSFUtils.referenceToSelectRef(refBook.getOrganizationTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getSalaryFreqTypes() {
        return JSFUtils.referenceToSelect(refBook.getIncomeFreqTypes());
    }

    public List<SelectItem> getSalaryFreqTypeCodes() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.getIncomeFreqTypes());
    }

    public List<SelectItem> getSalaryFreqTypesNull() {
        return JSFUtils.referenceToSelect(refBook.getIncomeFreqTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getSalaryFreqTypesRef() {
        return JSFUtils.referenceToSelectRef(refBook.getIncomeFreqTypes());

    }

    public List<SelectItem> getSalaryFreqTypesRefNull() {
        return JSFUtils.referenceToSelectRef(refBook.getIncomeFreqTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getAccountTypes() {
        return JSFUtils.referenceToSelect(refBook.getAccountTypes());

    }

    public List<SelectItem> getAccountTypesCodeInteger() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.getAccountTypes());

    }

    public List<SelectItem> getAccountTypesNull() {
        return JSFUtils.referenceToSelect(refBook.getAccountTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getAccountTypesRef() {
        return JSFUtils.referenceToSelectRef(refBook.getAccountTypes());
    }

    public List<SelectItem> getAccountTypesRefNull() {
        return JSFUtils.referenceToSelectRef(refBook.getAccountTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getYesNoNull() {
        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(3);

        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);

        si = new SelectItem();
        si.setValue(1);
        si.setLabel("да");
        lstCon.add(si);

        si = new SelectItem();
        si.setValue(0);
        si.setLabel("нет");
        lstCon.add(si);

        return lstCon;
    }

    public List<SelectItem> getYesNo() {
        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(3);

        SelectItem si = new SelectItem();
        si.setValue(1);
        si.setLabel("да");
        lstCon.add(si);

        si = new SelectItem();
        si.setValue(0);
        si.setLabel("нет");
        lstCon.add(si);

        return lstCon;
    }

    public List<SelectItem> getYesNoBooleanNull() {
        ArrayList<SelectItem> lstCon = new ArrayList<SelectItem>(3);

        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstCon.add(si);

        si = new SelectItem();
        si.setValue(true);
        si.setLabel("да");
        lstCon.add(si);

        si = new SelectItem();
        si.setValue(false);
        si.setLabel("нет");
        lstCon.add(si);

        return lstCon;
    }

    public String getSelectItemName(List<SelectItem> lst, int code) {
        String st = "неизвестно";
        for (int i = 0; i < lst.size(); i++)
            if (Convertor.toInteger(lst.get(i).getValue()) == code) {
                st = lst.get(i).getLabel();
                break;
            }
        return st;
    }

    public List<SelectItem> getRefAccounts(List<Account> source) {
        ArrayList<SelectItem> lstRes = new ArrayList<SelectItem>(source.size());
        for (Account ref : source) {
            SelectItem si = new SelectItem();
            si.setValue(ref);
            si.setLabel(ref.getDescription());
            lstRes.add(si);
        }
        return lstRes;
    }

    public List<SelectItem> getWorkplaces() {
        List<WorkplaceEntity> source = workplaceService.findAll();
        ArrayList<SelectItem> lstRes = new ArrayList<SelectItem>(source.size());
        SelectItem si = new SelectItem();
        si.setValue(JSFUtils.NULL_INT_VALUE);
        si.setLabel(JSFUtils.NULL_LABEL);
        lstRes.add(si);
        for (WorkplaceEntity ref : source) {
            si = new SelectItem();
            si.setValue(ref.getId());
            si.setLabel(ref.getName());
            lstRes.add(si);
        }
        return lstRes;
    }

    public List<SelectItem> getPaymentTypesRefNull() {
        return JSFUtils.referenceToSelect(refBook.getPaymentTypes(), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getCallbackTypeNull() {
        return JSFUtils.referenceToSelectCodeInteger(refBook.listReference(RefHeader.CALLBACK_CLIENT_TYPE), JSFUtils.NULL_INT_VALUE, JSFUtils.NULL_LABEL);
    }

    public List<SelectItem> getPensdoctypes() {
        return JSFUtils.referenceToSelectRef(refBook.getPensDocTypes());
    }

    public List<SelectItem> getAntifraudEntities() {
        return JSFUtils.referenceToSelectRef(refBook.getHunterObjects());
    }

    public List<SelectItem> getAntifraudStatuses() {
        return JSFUtils.referenceToSelectRef(refBook.getAntifraudStatuses());
    }

    public List<SelectItem> getVerifierQuestionTypes() {
        return JSFUtils.referenceToSelectRef(refBook.getVerifierQuestionTypes());
    }
}
