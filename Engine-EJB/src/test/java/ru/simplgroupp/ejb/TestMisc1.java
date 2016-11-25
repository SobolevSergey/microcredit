package ru.simplgroupp.ejb;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.w3c.dom.Node;

import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.toolkit.common.CheckUtils;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.ZipUtils;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.FTPUtils;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.MoneyString;
import ru.simplgroupp.util.XmlUtils;


public class TestMisc1 {
		
	
	//@Test
	public void testConvert1() {
		Double d=CalcUtils.roundHalfUp(new Double(1234.100), 3);
		System.out.println(d);
		System.out.println(MoneyString.num2str(false,d));
	}
	
	
	
	//@Test
    public void testSha1() throws Exception{
    	//MessageDigest md = MessageDigest.getInstance("SHA-1");
    	String st="Amount=555f70bf-c977-48f0-9e38-308d93ba0e8c&PrivateSecurityKey=07c0ceed-c871-4b9a-b7e8-26413383950a";
    	//md.update(st.getBytes());
    	//byte[] f=md.digest();
    	byte[] f=DigestUtils.sha(st);
    	Long s= 100l;
    	for (int i=0;i<20;i++){
    		s=s+(((int) f[i] & 0xFF)%31);
    		System.out.println("s "+s);
    		System.out.println("f[i] "+f[i]+", i "+i+", f[i]%31 "+Math.abs(f[i])%31);
    		
    	}
    	double a=(double) (s/100.0);
		System.out.println("a "+a);
    }
    
   // @Test
    public void testDate(){
    	System.out.println(DatesUtils.getDay(new Date()));
    	System.out.println(DatesUtils.getMonth(new Date()));
    	System.out.println(DatesUtils.getYear(new Date()));
    }
    
   // @Test
    public void testMd5() throws Exception{
    	
    	File file=new File("c:\\1\\hashtest.txt");
    	
    	String str=FileUtils.readFileToString(file,"Windows-1251");
    	String st=Convertor.toDigest(str,"Windows-1251");
    	System.out.println(st);
    	st=Convertor.toDigest("123456");
    	System.out.println(st);
    	
    }
    
    //@Test
    public void testXmlByXsl(){
        String sxml="";
        try {
            sxml = FileUtils.readFileToString(new File("c:\\Users\\helen1\\xsl\\ver1234.xml"));
            sxml=XmlUtils.getXmlWithoutHeader(sxml, "<?xml");
            sxml="<?xml version='1.0' encoding='UTF-8'?>"+"<?xml-stylesheet type='text/xsl' href='xslt/response.xsl'?>"+sxml;
        } catch (IOException e) {
            e.printStackTrace();
        }
        sxml=sxml.substring(sxml.indexOf("<?"));
        
        String sxsl="";
        try {
            sxsl = FileUtils.readFileToString(new File("c:\\Users\\helen1\\xsl\\response.xsl"));
        } catch (IOException e) {
            e.printStackTrace();
        }



        String xslRoot="c:\\Users\\helen1\\xsl\\";
        byte[] b=null;
        try {
            b=XmlUtils.xmlByXsl(sxml, sxsl, xslRoot);
        } catch (KassaException e) {
            e.printStackTrace();
        }

        if (b!=null)
        {
            String str=new String(b);
            //System.out.println(str);
            try {
                FileUtils.writeStringToFile(new File(FileUtils.getTempDirectoryPath()+"123.html"), str);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    //@Test 
  	public void uploadFtp() throws Exception{
    	
  		Boolean b=FTPUtils.UploadFtp("chess", "chess2015", "62.231.164.246", "/misc", "111.zip", "c:\\Users\\helen1\\111.zip", 21,true);
  		System.out.print(b);
  	}
  	
  	//@Test
  	public void testDates(){
  		int i=DatesUtils.daysDiffAny(new Date(), Convertor.toDate("10.12.2014", "dd.MM.yyyy"));
  		System.out.println(i);
  		i=DatesUtils.daysDiff(new Date(), Convertor.toDate("10.12.2014", "dd.MM.yyyy"));
  		System.out.println(i);
  		i=DatesUtils.daysDiffAny(new Date(), Convertor.toDate("16.12.2014", "dd.MM.yyyy"));
  		System.out.println(i);
  		i=DatesUtils.daysDiff(new Date(), Convertor.toDate("16.12.2014", "dd.MM.yyyy"));
  		System.out.println(i);
  	}
  	
  	//@Test
  	public void runApp(){
  		Runtime r = Runtime.getRuntime();
  		Process p = null;
  		try {
  		  //p = r.exec("notepad");
  		  //Base64	
          p=r.exec("c:\\Users\\helen1\\cryptcp\\cryptcp.exe -encr -f c:\\Users\\helen1\\cryptcp\\etest.cer -1 -nochain -norev  c:\\Users\\helen1\\cryptcp\\1.txt c:\\Users\\helen1\\cryptcp\\1.pem");
  		  //Der
          //p=r.exec("c:\\Users\\helen1\\cryptcp\\cryptcp.exe -encr -f c:\\Users\\helen1\\cryptcp\\etest.cer -1 -nochain -norev -der c:\\Users\\helen1\\cryptcp\\1.txt c:\\Users\\helen1\\cryptcp\\1.pem");
          p.waitFor();
  		} catch (Exception e) {
  		  System.out.println(e);
  		}
  		System.out.println("После завершения работы и код возврата - " + p.exitValue());
  		try {
    		  //p = r.exec("notepad");
    		  //Base64	
            //p=r.exec("c:\\Users\\helen1\\cryptcp\\cryptcp.exe -encr -f c:\\Users\\helen1\\cryptcp\\etest.cer -1 -nochain -norev  c:\\Users\\helen1\\cryptcp\\1.txt c:\\Users\\helen1\\cryptcp\\1.pem");
    		  //Der
            p=r.exec("c:\\Users\\helen1\\cryptcp\\cryptcp.exe -encr -f c:\\Users\\helen1\\cryptcp\\etest.cer -1 -nochain -norev -der c:\\Users\\helen1\\cryptcp\\1.txt c:\\Users\\helen1\\cryptcp\\1der.pem");
            p.waitFor();
    		} catch (Exception e) {
    		  System.out.println(e);
    		}
    		System.out.println("После завершения работы и код возврата - " + p.exitValue());  		 
  	}
  	
  	//@Test
  	public void runApp1(){
  		ProcessBuilder processBuilder = new ProcessBuilder("cryptcp.exe -encr -f etest.cer -1 -nochain -norev -der 1.txt 1.pem");
  		processBuilder.directory(new File("c:\\Users\\helen1\\cryptcp\\"));
  		File log = new File("c:\\log.pb");
  		processBuilder.redirectErrorStream(true);
  		processBuilder.redirectOutput(Redirect.appendTo(log));
  		Process process =null;
  		try {
			process =processBuilder.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
  		try {
			processBuilder.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		System.out.println("Завершение работы "+process.exitValue());
  	}
  	
  	//@Test
  	public void testDir() throws Exception{
  		System.out.println(FileUtils.getTempDirectoryPath());
  		System.out.println(FileUtils.getUserDirectoryPath());
  		File file=new File(FileUtils.getTempDirectoryPath()+"address.csv");
  		byte[] bs=ZipUtils.ZipSingleFile(file);
  		System.out.println(FileUtils.getUserDirectoryPath());
  	}
  	
  
    
   // @Test
    public void testCheck(){
    	boolean b=CheckUtils.CheckInn("771920723177");
    	System.out.println("is correct "+b);
    	Assert.assertTrue(b);
    }
    
    //@Test
    public void testEscapeChar() throws Exception
    {
        String st=Convertor.fromEscapeHtml("<a n='line2'>1 &#1052;&#1072;&#1077;&#1074;&#1082;&#1080;</a>");
        System.out.println(st);

    }
    

    // @Test
    public void testAttribute() throws Exception
    {
        StringBuilder sb=new StringBuilder();
        sb.append("<?xml version='1.0' encoding='windows-1251'?>");
        sb.append("<s><a n='accountClass'>1</a><a n='reason'>01</a>");
        sb.append("<a n='applicationNumber'>Credit12345</a><a n='applicationDate'>20100219</a></s>");

        String st=sb.toString();
        org.w3c.dom.Document doc=XmlUtils.getDocFromString(st);
        if (doc!=null)
        {
            Node nd=XmlUtils.getNodeFromAttributeValue(doc, "a","n", "reason");
            String st1=XmlUtils.getNodeValueText(nd);
            System.out.print(st1);
        }
    }

   // @Test
    public void testDecodeBase64() throws Exception{
    	byte[] answerr=FileUtils.readFileToByteArray(new File("c:\\2\\123.pem"));
    	String st=new String(Base64.decodeBase64(answerr));
    	System.out.println(st);
    }
    
    @Test
    public void testConnection() throws Exception{
    	boolean conn = HTTPUtils.isConnected("http://stand.asap.digital/");
    	System.out.println(conn);
    }
    
}
