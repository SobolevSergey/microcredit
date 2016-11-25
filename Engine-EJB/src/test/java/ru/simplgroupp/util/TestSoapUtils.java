package ru.simplgroupp.util;

import junit.framework.Assert;
import org.junit.Test;

import javax.xml.soap.SOAPMessage;

/**
 * Created by aro on 13.09.2014.
 */
public class TestSoapUtils {

    @Test
    public void testcreateSOAPRequest() throws Exception
    {
        String dtu = "<?xml version=\"1.0\" encoding=\"US-ASCII\"?><CDTRN Version=\"1\" PPCode=\"TZGX\" Code=\"BASE64\" Lang=\"ru\" Stamp=\"2010-08-20T17:21:24+04:00\"><DATA Pack=\"ZLIB\"Encryption=\"PLAIN\">eNo9zssKwjAURdG54D9c7txHH4JCo9QYJCqmNlHUSdE2aLWmYIvVvzc4cHYG68AOJu9HAS/9rPLSEHS6fQRt0jLLzYVgk5usbKqO4w4chKo+mexUlEYT/OgKJ2Not4KYbbZMKhDTBaMqoatQSoIqPFtnanG+6bRGCKniYk2QXnV6R4gEX1srZszS</DATA> \n" +
                "<SIGNATURES> <SIGNATURE ALGO=\"ECR3410\">MIAGCSqGSIb3DQEHAqCAMIIBNAIBATEOMAwGCisGAQQBrVkBAgEwgAYJKoZIhvcNAQcBAAAxggEOMIIBCgIBATCBmzCBjTELMAkGA1UEBhMC</SIGNATURE> \n" +
                "</SIGNATURES> \n" +
                "</CDTRN> ";
        System.out.print("Request SOAP Message:");
        SOAPMessage soapMessage = SoapUtils.createSOAPRequest(dtu);
        soapMessage.writeTo(System.out);
        System.out.println();
        Assert.assertNotNull(soapMessage);

    }
}
