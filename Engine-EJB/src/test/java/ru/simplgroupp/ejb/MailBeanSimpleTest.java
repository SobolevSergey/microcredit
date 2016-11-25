package ru.simplgroupp.ejb;

import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.mail.MailBean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MailBeanSimpleTest {

    private MailBean bean;

    @Before
    public void setUp() throws Exception {
        bean = new MailBean();
    }


    @Test
    public void doSendSmsV2() {
        boolean res = false;
        //Igor phone = 79164169442
        //Elena phone = 79059610016


        try {
            String theText = readFile("C:/sms.txt","windows-1251");
            res = bean.sendSMSV2("79872896935",theText);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(res){
            System.out.print("+++++++++ All is good!");
        }else{
            System.out.print("--------  All is bad!");
        }
    }

    public static String readFile(String path, String encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

  
}
