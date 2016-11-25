
package ru.simplgroupp.idv;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.simplgroupp.idv package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ApplicantDataIncomeFamily_QNAME = new QName("http://experian.com/bureau/hosted/nbsm", "Income_Family");
    private final static QName _ApplicantDataIncomeTotal_QNAME = new QName("http://experian.com/bureau/hosted/nbsm", "Income_Total");
    private final static QName _ApplicantDataCreditHistory_QNAME = new QName("http://experian.com/bureau/hosted/nbsm", "CreditHistory");
    private final static QName _ApplicantDataJob_QNAME = new QName("http://experian.com/bureau/hosted/nbsm", "Job");
    private final static QName _ApplicantDataIssueDateEntrepreneur_QNAME = new QName("http://experian.com/bureau/hosted/nbsm", "Issue_date_Entrepreneur");
    private final static QName _ProductParamsDataProductParamsNumberOfCardGrades_QNAME = new QName("http://experian.com/bureau/hosted/nbsm", "Product_Params_Number_of_Card_Grades");
    private final static QName _ProductParamsDataProductParamsTermStep_QNAME = new QName("http://experian.com/bureau/hosted/nbsm", "Product_Params_Term_Step");
    private final static QName _ReqBusinesInfoApplicationRequestedCardGrade_QNAME = new QName("http://experian.com/bureau/hosted/nbsm", "Application_Requested_Card_Grade");
    private final static QName _ReqBusinesInfoCollaterialValueAmount_QNAME = new QName("http://experian.com/bureau/hosted/nbsm", "Collaterial_Value_Amount");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.simplgroupp.idv
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IDVProcessResponse }
     * 
     */
    public IDVProcessResponse createIDVProcessResponse() {
        return new IDVProcessResponse();
    }

    /**
     * Create an instance of {@link IDVResponseRoot }
     * 
     */
    public IDVResponseRoot createIDVResponseRoot() {
        return new IDVResponseRoot();
    }

    /**
     * Create an instance of {@link HSMSProcessResponse }
     * 
     */
    public HSMSProcessResponse createHSMSProcessResponse() {
        return new HSMSProcessResponse();
    }

    /**
     * Create an instance of {@link ResponseRoot }
     * 
     */
    public ResponseRoot createResponseRoot() {
        return new ResponseRoot();
    }

    /**
     * Create an instance of {@link IDVProcess }
     * 
     */
    public IDVProcess createIDVProcess() {
        return new IDVProcess();
    }

    /**
     * Create an instance of {@link IDVRequestRoot }
     * 
     */
    public IDVRequestRoot createIDVRequestRoot() {
        return new IDVRequestRoot();
    }

    /**
     * Create an instance of {@link HSMSProcess }
     * 
     */
    public HSMSProcess createHSMSProcess() {
        return new HSMSProcess();
    }

    /**
     * Create an instance of {@link RequestRoot }
     * 
     */
    public RequestRoot createRequestRoot() {
        return new RequestRoot();
    }

    /**
     * Create an instance of {@link IDVApplicantCH }
     * 
     */
    public IDVApplicantCH createIDVApplicantCH() {
        return new IDVApplicantCH();
    }

    /**
     * Create an instance of {@link ApplicantCH }
     * 
     */
    public ApplicantCH createApplicantCH() {
        return new ApplicantCH();
    }

    /**
     * Create an instance of {@link ApplicantAddress }
     * 
     */
    public ApplicantAddress createApplicantAddress() {
        return new ApplicantAddress();
    }

    /**
     * Create an instance of {@link RespTechnicalInfo }
     * 
     */
    public RespTechnicalInfo createRespTechnicalInfo() {
        return new RespTechnicalInfo();
    }

    /**
     * Create an instance of {@link ApplicantDoc }
     * 
     */
    public ApplicantDoc createApplicantDoc() {
        return new ApplicantDoc();
    }

    /**
     * Create an instance of {@link ApplicantJob }
     * 
     */
    public ApplicantJob createApplicantJob() {
        return new ApplicantJob();
    }

    /**
     * Create an instance of {@link IDVRespBusinesInfo }
     * 
     */
    public IDVRespBusinesInfo createIDVRespBusinesInfo() {
        return new IDVRespBusinesInfo();
    }

    /**
     * Create an instance of {@link ProductParamsData }
     * 
     */
    public ProductParamsData createProductParamsData() {
        return new ProductParamsData();
    }

    /**
     * Create an instance of {@link ApplicantData }
     * 
     */
    public ApplicantData createApplicantData() {
        return new ApplicantData();
    }

    /**
     * Create an instance of {@link RespBusinesInfo }
     * 
     */
    public RespBusinesInfo createRespBusinesInfo() {
        return new RespBusinesInfo();
    }

    /**
     * Create an instance of {@link ApplicantVehicle }
     * 
     */
    public ApplicantVehicle createApplicantVehicle() {
        return new ApplicantVehicle();
    }

    /**
     * Create an instance of {@link ReqBusinesInfo }
     * 
     */
    public ReqBusinesInfo createReqBusinesInfo() {
        return new ReqBusinesInfo();
    }

    /**
     * Create an instance of {@link IDVReqBusinesInfo }
     * 
     */
    public IDVReqBusinesInfo createIDVReqBusinesInfo() {
        return new IDVReqBusinesInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://experian.com/bureau/hosted/nbsm", name = "Income_Family", scope = ApplicantData.class)
    public JAXBElement<BigDecimal> createApplicantDataIncomeFamily(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ApplicantDataIncomeFamily_QNAME, BigDecimal.class, ApplicantData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://experian.com/bureau/hosted/nbsm", name = "Income_Total", scope = ApplicantData.class)
    public JAXBElement<BigDecimal> createApplicantDataIncomeTotal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ApplicantDataIncomeTotal_QNAME, BigDecimal.class, ApplicantData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApplicantCH }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://experian.com/bureau/hosted/nbsm", name = "CreditHistory", scope = ApplicantData.class)
    public JAXBElement<ApplicantCH> createApplicantDataCreditHistory(ApplicantCH value) {
        return new JAXBElement<ApplicantCH>(_ApplicantDataCreditHistory_QNAME, ApplicantCH.class, ApplicantData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApplicantJob }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://experian.com/bureau/hosted/nbsm", name = "Job", scope = ApplicantData.class)
    public JAXBElement<ApplicantJob> createApplicantDataJob(ApplicantJob value) {
        return new JAXBElement<ApplicantJob>(_ApplicantDataJob_QNAME, ApplicantJob.class, ApplicantData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://experian.com/bureau/hosted/nbsm", name = "Issue_date_Entrepreneur", scope = ApplicantData.class)
    public JAXBElement<XMLGregorianCalendar> createApplicantDataIssueDateEntrepreneur(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ApplicantDataIssueDateEntrepreneur_QNAME, XMLGregorianCalendar.class, ApplicantData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://experian.com/bureau/hosted/nbsm", name = "Product_Params_Number_of_Card_Grades", scope = ProductParamsData.class)
    public JAXBElement<Integer> createProductParamsDataProductParamsNumberOfCardGrades(Integer value) {
        return new JAXBElement<Integer>(_ProductParamsDataProductParamsNumberOfCardGrades_QNAME, Integer.class, ProductParamsData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://experian.com/bureau/hosted/nbsm", name = "Product_Params_Term_Step", scope = ProductParamsData.class)
    public JAXBElement<Integer> createProductParamsDataProductParamsTermStep(Integer value) {
        return new JAXBElement<Integer>(_ProductParamsDataProductParamsTermStep_QNAME, Integer.class, ProductParamsData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://experian.com/bureau/hosted/nbsm", name = "Application_Requested_Card_Grade", scope = ReqBusinesInfo.class)
    public JAXBElement<Integer> createReqBusinesInfoApplicationRequestedCardGrade(Integer value) {
        return new JAXBElement<Integer>(_ReqBusinesInfoApplicationRequestedCardGrade_QNAME, Integer.class, ReqBusinesInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://experian.com/bureau/hosted/nbsm", name = "Collaterial_Value_Amount", scope = ReqBusinesInfo.class)
    public JAXBElement<BigDecimal> createReqBusinesInfoCollaterialValueAmount(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ReqBusinesInfoCollaterialValueAmount_QNAME, BigDecimal.class, ReqBusinesInfo.class, value);
    }

}
