package ru.simplgroupp.ejb;

import java.util.Arrays;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import junit.framework.Assert;

import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.dao.interfaces.UploadingDAO;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.MailBeanLocal;
import ru.simplgroupp.interfaces.ScoringEquifaxBeanLocal;
import ru.simplgroupp.interfaces.ScoringNBKIBeanLocal;
import ru.simplgroupp.interfaces.ScoringOkbCaisBeanLocal;
import ru.simplgroupp.interfaces.ScoringOkbIdvBeanLocal;
import ru.simplgroupp.interfaces.ScoringRsBeanLocal;
import ru.simplgroupp.interfaces.ScoringSkbBeanLocal;
import ru.simplgroupp.services.PaymentService;
import ru.simplgroupp.interfaces.service.UploadingService;
import ru.simplgroupp.persistence.UploadingEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.UploadStatus;
import ru.simplgroupp.util.DatesUtils;


@LocalClient
public class TestUpload {

	@EJB
    KassaBeanLocal kassa;

    @EJB
    PaymentService paymentService;

    @EJB
    ScoringEquifaxBeanLocal equifax;

    @EJB
    ScoringRsBeanLocal rs;
    
    @EJB
    ScoringNBKIBeanLocal nbki;
    
    @EJB
    ScoringSkbBeanLocal skb;
    
    @EJB
    ScoringOkbIdvBeanLocal okbIdv;
    
    @EJB
    ScoringOkbCaisBeanLocal okbCais;
    
    @EJB
    MailBeanLocal mail;
    
    @EJB
    UploadingService upload;
    
    @EJB
    UploadingDAO uploadingDAO;
    
    @Before
    public void setUp() throws Exception {
    	System.setProperty("openejb.validation.output.level","VERBOSE");
		final Properties p = new Properties();
        p.load(this.getClass().getResourceAsStream("/test.properties"));
	
        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);
  
        kassa = (KassaBeanLocal) context.lookup("java:global/Engine-EJB/KassaBean!ru.simplgroupp.interfaces.KassaBeanLocal");		
        equifax= (ScoringEquifaxBeanLocal) context.lookup("java:global/Engine-EJB/ScoringEquifaxBean!ru.simplgroupp.interfaces.ScoringEquifaxBeanLocal");		
        mail = (MailBeanLocal) context.lookup("java:global/Engine-EJB/MailBean!ru.simplgroupp.interfaces.MailBeanLocal");
        rs= (ScoringRsBeanLocal) context.lookup("java:global/Engine-EJB/ScoringRsBean!ru.simplgroupp.interfaces.ScoringRsBeanLocal");
        nbki= (ScoringNBKIBeanLocal) context.lookup("java:global/Engine-EJB/ScoringNBKIBean!ru.simplgroupp.interfaces.ScoringNBKIBeanLocal");
        skb= (ScoringSkbBeanLocal) context.lookup("java:global/Engine-EJB/ScoringSkbBean!ru.simplgroupp.interfaces.ScoringSkbBeanLocal");
        okbIdv= (ScoringOkbIdvBeanLocal) context.lookup("java:global/Engine-EJB/ScoringOkbIdvBean!ru.simplgroupp.interfaces.ScoringOkbIdvBeanLocal");
        okbCais= (ScoringOkbCaisBeanLocal) context.lookup("java:global/Engine-EJB/ScoringOkbCaisBean!ru.simplgroupp.interfaces.ScoringOkbCaisBeanLocal");
        upload = (UploadingService) context.lookup("java:global/Engine-EJB/UploadingServiceImpl!ru.simplgroupp.interfaces.service.UploadingService");
    }
    
	//@Test
	public void testDB() {
		 Assert.assertNotNull(kassa);
	}
	
	//@Test
	public void testEquifaxDownloadCorrection() throws Exception{
		
		UploadingEntity upl=upload.getLastUploading(Partner.EQUIFAX,UploadStatus.UPLOAD_CREDIT_CORRECTION);
		equifax.uploadHistory(upl,Convertor.toDate("23.02.2015", DatesUtils.FORMAT_ddMMYYYY),false);
	}
	
	//@Test
	public void testEquifaxCreateFileCorrection() throws Exception{
		
		UploadingEntity upl=equifax.createCorrectionFileForUpload(DatesUtils.makeDate(2015, 10, 2), 37116, false);
		if (upl!=null) {
	    	System.out.println(upl.getInfo());
	    }
	}
	
	//@Test
    public void testEquifaxCreateFileCredit() throws Exception
    {
		UploadingEntity upl=equifax.createFileForUpload(Convertor.toDate("22.06.2015", DatesUtils.FORMAT_ddMMYYYY), false);
		if (upl!=null) {
	    	System.out.println(upl.getInfo());
	    }
	
    }
    
	//@Test
    public void testEquifaxCreateFileCreditRequest() throws Exception
    {
		UploadingEntity upl=equifax.createFileForUploadCreditRequest(Convertor.toDate("22.06.2015", DatesUtils.FORMAT_ddMMYYYY), false);
		if (upl!=null) {
	    	System.out.println(upl.getInfo());
	    }
	
    }
	
	//@Test
    public void testEquifaxDownload() throws Exception
    {
    	UploadingEntity upl=uploadingDAO.getUploadingEntity(997554);
	//	UploadingEntity upl=upload.getLastUploading(Partner.EQUIFAX,UploadStatus.UPLOAD_CREDIT);
		equifax.uploadHistory(upl,null,true);
	
    }

   //@Test
    public void testNbkiCreateFile() throws Exception {
   
   	  UploadingEntity upl=nbki.createFileForUpload(null, false,Arrays.asList(37116,54167));
   	  if (upl!=null) {
    	  System.out.println(upl.getInfo());
      }
    	
    }
    
  //  @Test
    public void testNbkiDownload() throws Exception {
    	   
      UploadingEntity upl=uploadingDAO.getUploadingEntity(1001742);
      nbki.uploadHistory(upl, null, true);
      	
    }
    
	//@Test
    public void testEquifaxUploadStatus() throws Exception {
    	//UploadingEntity upl=upload.getLastUploading(Partner.EQUIFAX);
    	UploadingEntity upl=uploadingDAO.getUploadingEntity(323070);
		equifax.checkUploadStatus(upl,true);
    }
    
	
	//@Test
    public void testEquifaxDownloadCR() throws Exception
    {
	//	UploadingEntity upl=upload.getLastUploading(Partner.EQUIFAX,UploadStatus.UPLOAD_CREDITREQUEST);
		UploadingEntity upl=uploadingDAO.getUploadingEntity(259256);
		equifax.uploadHistory(upl,null,true);
    }
	
  //@Test
    public void testEquifaxDownloadDelete() throws Exception
    {
		
		UploadingEntity upl=upload.getLastUploading(Partner.EQUIFAX,UploadStatus.UPLOAD_CREDIT_DELETE);
		equifax.uploadHistory(upl,null,true);
    }
    
    //@Test
    public void testEquifaxCreateFileDelete() throws Exception
    {
		UploadingEntity upl=equifax.createDeleteFileForUpload(Convertor.toDate("10.02.2015", DatesUtils.FORMAT_ddMMYYYY),12345, false);
		if (upl!=null) {
	    	System.out.println(upl.getInfo());
	    }
    }
    
   // @Test
    public void testRSCreateFileDelete() throws Exception
    {
		UploadingEntity upl=rs.createDeleteFileForUpload(DatesUtils.makeDate(2015, 10, 9),40376, false);
		if (upl!=null) {
	    	System.out.println(upl.getInfo());
	    }
    }
    
   //@Test
    public void testRSDownload() throws Exception {
   
   	 	//UploadingEntity upl=upload.getLastUploading(Partner.RSTANDARD);
    	UploadingEntity upl=uploadingDAO.getUploadingEntity(46327);
    	rs.uploadHistory(upl,null,false);
    //	upl=uploadingDAO.getUploadingEntity(42824);
    //	rs.uploadHistory(upl,null,false);
   
	   
    }
    
    //@Test
    public void testRSCreateFile() throws Exception {
   
   	  UploadingEntity upl=rs.createFileForUpload(null, false);
   	  if (upl!=null) {   
    	  System.out.println(upl.getInfo());
      }
    }
    
  //  @Test
    public void testRSCreateSingleFile() throws Exception {
    	   
     	  UploadingEntity upl=rs.createFileForUpload(null, false,Arrays.asList(44480));
     	  if (upl!=null) {
      	    System.out.println(upl.getInfo());
        }
    }
    
    //@Test
    public void testRSUploadStatus() throws Exception {
   
    	UploadingEntity upl=upload.getLastUploading(Partner.RSTANDARD);
    	rs.checkUploadStatus(upl,true);
    }
    
  //  @Test
    public void testSkbDownload() throws Exception {
   	
    	UploadingEntity upl=uploadingDAO.getUploadingEntity(1422741);
    	skb.uploadHistory(upl,Convertor.toDate("11.08.2015", DatesUtils.FORMAT_ddMMYYYY),true);
   
	}
    
    //@Test
    public void testSkbCreateFile() throws Exception {
   
   	 UploadingEntity upl=skb.createFileForUpload(Convertor.toDate("16.03.2015", DatesUtils.FORMAT_ddMMYYYY), false);
    	
    	if (upl!=null) {
    	  System.out.println(upl.getInfo());
    	}
   
	}
    
  // @Test
    public void testSkbCheckStatus() throws Exception {
    	UploadingEntity uploading=uploadingDAO.getUploadingEntity(500000);
    	skb.checkUploadStatus(uploading,true);
    }
    
    @Test
    public void testUploadOkb() throws Exception{
    	
    //	UploadingEntity upl=upload.getLastUploading(Partner.OKB_CAIS);
	    UploadingEntity upl=uploadingDAO.getUploadingEntity(42251);
    	okbCais.uploadHistory(upl,null, true);
    	//mail.sendAttachment("Отчет о тестовой выгрузке данных", "", "bureauoperator@bki-okb.ru", "c:/4/cais_report.zip");
    }
    
    
  //  @Test
    public void testOkbCreateFile() throws Exception{
    	//UploadingEntity upl=okbCais.createFileForUpload(null, false,Arrays.asList(57700,58352,42975,44480,57914));
    	UploadingEntity upl=okbCais.createFileForUpload(null, false,Arrays.asList(24968,33601));	
    	if (upl!=null) {
      	  System.out.println(upl.getInfo());
      	}
    	
    }
    
   // @Test
    public void findError(){
    	UploadingEntity upl=uploadingDAO.getUploadingEntity(484479);
    	String st=upl.getInfo().substring(57800, 58100);
    	System.out.println(st);
    	st=upl.getInfo().substring(89539, 89800);
    	System.out.println(st);
    }
   
}
