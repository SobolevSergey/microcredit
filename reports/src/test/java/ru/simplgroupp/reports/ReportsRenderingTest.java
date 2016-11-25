package ru.simplgroupp.reports;
import org.apache.openejb.api.LocalClient;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.Before;
import org.junit.Test;
import ru.simplgroupp.reports.configuration.ReportsConfiguration;
import ru.simplgroupp.reports.model.*;
import ru.simplgroupp.reports.wrapper.ReportCompiler;

import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.NamingException;
import static junit.framework.Assert.assertNotNull;

import java.io.IOException;
import java.util.*;
/*
* тестирование заполнения отчетов, отчеты должны быть скомпилированы
* */
@LocalClient
public class ReportsRenderingTest {
    private String reportsDir;
    @Inject
    private ReportsConfiguration reportsConfiguration;
    @Inject
    private ReportCompiler reportCompiler;
    @Before
    public void setUp() throws NamingException, IOException {
        reportsDir = "/home/victor/reports/";
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

        final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));

        Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);

    }
    @Test
    public void testCredits() throws JRException {
        String sourceFileName = reportsDir +"requests.jasper";
        System.out.println(sourceFileName);
        ArrayList<CreditReportModel> reportBeans = new ArrayList<CreditReportModel>();
        CreditReportModel reportBean = new CreditReportModel();
        reportBean.setRejectsNumber(100l);
        reportBean.setRejectsNumberOneCredit(1);
        reportBean.setRejectsNumberTwoPlusCredits(10);
        reportBean.setRejectsPercentTwoPlusCredits(10);
        reportBean.setRejectsPercentOneCredit(10);
        reportBean.setRequestsNumber(10l);
        reportBean.setRequestsNumberOneCredit(10);
        reportBean.setRequestsNumberTwoPlusCredits(10);
        reportBean.setTotalCredits(10l);
        reportBean.setTotalDebt(10.0);
        reportBean.setTotalCreditsOneCredit(10);
        reportBean.setTotalCreditsTwoPlusCredits(10);
        reportBean.setTotalDebtOneCredit(10);
        reportBean.setTotalDebtTwoPlusCredits(10);
        reportBean.setNewClientsPercent("10%");
        reportBean.setAvgPeriod(10.0);
        reportBean.setWeightedAvgRate(10.0);
        reportBean.setAvgTotalDebt(10.0);
        reportBeans.add(reportBean);
        reportBeans.add(reportBean);
        reportBean.setAvgTotalDebtOneCredit(10);
        reportBean.setAvgTotalDebtTwoPlusCredits(20);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportBeans);
        Map parameters = new HashMap();
        JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
        JasperExportManager.exportReportToHtmlFile(jasperPrint,reportsDir+"requests.html");
    }
    @Test
    public void testPayments() throws JRException {
        String sourceFileName = reportsDir +"payments.jasper";
        PaymentReportModel reportBean = new PaymentReportModel();
        reportBean.setMoneyFlow(10.0);
        reportBean.setPayedAmount(100l);
        reportBean.setPayedSum(1000.0);
        reportBean.setPlannedToPayAmount(10000l);
        reportBean.setPlannedToPaySum(10000000.0);
        reportBean.setPlannedToPayAmountOneCredit(12l);
        reportBean.setPlannedToPayAmountTwoPlusCredits(14l);
        reportBean.setPlannedToPaySumCommonDebt(100.0);
        reportBean.setPlannedToPaySumPenalties(100.0);
        reportBean.setPlannedToPayPercents(100.0);
        reportBean.setPayedAmountOneCredit(10);
        reportBean.setPayedAmountTwoPlusCredits(10);
        reportBean.setPayedSumCommonDebt(10);
        reportBean.setPayedSumPercents(10);
        reportBean.setPayedSumPenalties(100.0);
        reportBean.setPayedSumBeforeTerm(1);
        reportBean.setPayedSumInTerm(1);
        reportBean.setPayedSumOverdue1_3(1);
        reportBean.setPayedSumOverdue4_10(2);
        reportBean.setPayedSumOverdue11_30(2);
        reportBean.setPayedSumOverdue31_60(2);
        reportBean.setPayedSumOverdue61_90(2);
        reportBean.setPayedSumOverdue91_plus(2);
        ArrayList<PaymentReportModel> reportBeans = new ArrayList<PaymentReportModel>();
        reportBeans.add(reportBean);
        reportBeans.add(reportBean);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportBeans);
        Map parameters = new HashMap();
        JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
        JasperExportManager.exportReportToHtmlFile(jasperPrint,reportsDir+"payments.html");

    }
    @Test
    public void testProlongations() throws JRException {
        String sourceFileName = reportsDir+"prolongations.jasper";
        ProlongationReportModel reportBean = new ProlongationReportModel();
        reportBean.setCreditAmount(101l);
        reportBean.setCreditAmountOneCredit(10);
        reportBean.setCreditAmountTwoPlusCredits(2);
        reportBean.setCreditAvgSum(102.0);
        reportBean.setCreditAmountOneCredit(1);
        reportBean.setCreditAmountTwoPlusCredits(2);
        reportBean.setProlongationAvgTerm(100.0);
        reportBean.setProlongationAvgTermOneCredit(2);
        reportBean.setProlongationAvgTermTwoPlusCredits(2);
        reportBean.setProlongationPaymentAvgSum(10000.0);
        reportBean.setProlongationsAmount(1000000l);
        reportBean.setProlongationsAmountOneCredit(2);
        reportBean.setProlongationsAmountTwoPlusCredits(2);
        ArrayList<ProlongationReportModel> reportBeans = new ArrayList<ProlongationReportModel>();
        reportBeans.add(reportBean);
        reportBeans.add(reportBean);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportBeans);
        Map parameters = new HashMap();
        JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
        JasperExportManager.exportReportToHtmlFile(jasperPrint,reportsDir+"prolongations.html");

    }
    @Test
    public void testClosed() throws JRException {
        String sourceFileName = reportsDir+"closed.jasper";
        ClosedCreditReportModel reportBean = new ClosedCreditReportModel();
        reportBean.setClosedCreditsAmt(100l);
        reportBean.setPlannedToCloseCreditsAmt(1000l);
        ArrayList<ClosedCreditReportModel> reportBeans = new ArrayList<ClosedCreditReportModel>();
        reportBeans.add(reportBean);
        reportBeans.add(reportBean);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportBeans);
        Map parameters = new HashMap();
        JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
        JasperExportManager.exportReportToHtmlFile(jasperPrint,reportsDir+"closed.html");
    }
    @Test
    public  void testPenalties() throws JRException {
        String sourceFileName = reportsDir+"penalties.jasper";
        CollectorsReportModel reportBean = new CollectorsReportModel();
        reportBean.setCollectorCreditCount(10l);
        reportBean.setCollectorPenaltiesAndPercents(100);
        reportBean.setCollectorsCreditsCommonSumDebt(1000);
        reportBean.setCollectorsCreditsTotalSum(100000);
        reportBean.setCollectorsCollectedCommonSumDebt(20);
        reportBean.setCollectorsCollectedCreditsTotalSum(200);
        reportBean.setCollectorsCollectedPenaltiesAndPercents(2000);
        ArrayList<CollectorsReportModel> reportBeans = new ArrayList<CollectorsReportModel>();
        reportBeans.add(reportBean);
        reportBeans.add(reportBean);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportBeans);
        Map parameters = new HashMap();
        JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
        JasperExportManager.exportReportToHtmlFile(jasperPrint,reportsDir+"penalties.html");
    }
    @Test
    public void testPortfail() throws JRException {
        String sourceFileName =reportsDir+"portfail.jasper";
        PortfelReportModel reportBean = new PortfelReportModel();
        reportBean.setAmount(1l);
        reportBean.setCommonDebt(3.0);
        reportBean.setCommonDebtIncrement(10.14555);
        ArrayList<PortfelReportModel> reportBeans = new ArrayList<PortfelReportModel>();
        reportBeans.add(reportBean);
        reportBeans.add(reportBean);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportBeans);
        Map parameters = new HashMap();
        JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
        JasperExportManager.exportReportToHtmlFile(jasperPrint, reportsDir + "portfail.html");

    }



}
