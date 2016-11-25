package ru.simplgroupp.util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;

import ru.simplgroupp.toolkit.common.Convertor;

public class TestRemoteDB {

	@Before
	public void setUp() throws Exception {
	}

	//@Test
	public void testPhone() throws Exception{
		String rmessage="/phonenumbers/api/phonenumber/check/9059610016";
		Map<String,String> rparams=new HashMap<String,String>();
		String string="adminUser:admin123";
		byte[] byt=Base64.encodeBase64(string.getBytes());		
		String encString=new String(byt);
		rparams.put("Authorization", "Basic "+encString);
		byte[] response = HTTPUtils.sendHttp("GET", "http://stand.asap.digital"+rmessage, null, rparams, null);
		
		if (response!=null){
			String answer=new String(response);
			 JSONParser parser = new JSONParser();
			 JSONObject jsonAnswer = (JSONObject) parser.parse(answer);
			
			if (jsonAnswer!=null){
			
				 Integer nstatus=Convertor.toInteger(jsonAnswer.get("status"));
				 if (nstatus==1){
					 JSONObject result= (JSONObject) jsonAnswer.get("result");
					 if (result!=null){
						 String region=(String) result.get("region");
						 System.out.println(region);
					 }
				 } else if (nstatus==2){
					
					 String emessage=(String) jsonAnswer.get("error");
					 System.out.println(emessage);
				 }
			
			}
		}
	}

	@Test
	public void testPassport() throws Exception{
		String rmessage="/passinfo/rest/passcheck?passpnum=2202855342";
		//String rmessage="/passinfo/rest/passcheck?passpnum=3201363851";
		//String rmessage="/passinfo/rest/passcheck?passpnum=3204888533";
		Map<String,String> rparams=new HashMap<String,String>();
		String string="adminUser:admin123";
		byte[] byt=Base64.encodeBase64(string.getBytes());		
		String encString=new String(byt);
		rparams.put("Authorization", "Basic "+encString);
		byte[] response = HTTPUtils.sendHttp("GET", "http://localhost:8080"+rmessage, null, rparams, null);
		
		if (response!=null){
			String answer=new String(response);
			 JSONParser parser = new JSONParser();
			 JSONObject jsonAnswer = (JSONObject) parser.parse(answer);
			
			if (jsonAnswer!=null){
			
				 Integer nstatus=Convertor.toInteger(jsonAnswer.get("code"));
				 if (nstatus==1){
					 
						 System.out.println("Нашли недействительный паспорт");
					
				 } else if (nstatus==2){
					
					 String emessage=(String) jsonAnswer.get("description");
					 System.out.println(emessage);
				 } else {
					 System.out.println("Действительный паспорт");
				 }
			
			}
		}
	}
	
	//@Test
	public void testTerrorist() throws Exception{
		
		String rmessage="surname="+URLEncoder.encode("Ященко",XmlUtils.ENCODING_UTF)+"&name="+URLEncoder.encode("Артем",XmlUtils.ENCODING_UTF)+"&midname="+URLEncoder.encode("Александрович",XmlUtils.ENCODING_UTF)+"&birthday=28.06.1990";
		Map<String,String> rparams=new HashMap<String,String>();
		String string="admin:admin";
		byte[] byt=Base64.encodeBase64(string.getBytes());		
		String encString=new String(byt);
		rparams.put("Authorization", "Basic "+encString);
		byte[] response = HTTPUtils.sendHttp("POST", "http://localhost:8080/terrorist/rest/check?",rmessage.getBytes(), rparams, null);
		
		if (response!=null){
			String answer=new String(response);
			 JSONParser parser = new JSONParser();
			 JSONObject jsonAnswer = (JSONObject) parser.parse(answer);
			
			if (jsonAnswer!=null){
			     if (jsonAnswer.get("exist")!=null){
				   Boolean nstatus=Convertor.toBoolean(jsonAnswer.get("exist"));
				   if (nstatus){
					 JSONObject result= (JSONObject) jsonAnswer.get("result");
					 if (result!=null){
						 String dbeg=(String) result.get("databeg");
						 System.out.println(dbeg);
						 Boolean active=Convertor.toBoolean("isActive");
						 System.out.println(active);
					 }
				   } else {
					
					 System.out.println("Не найдено");
				   }
			     } else {
			    	 //error
			    	 String emessage=(String) jsonAnswer.get("error");
			    	 System.out.println(emessage);
			     }
			
			}
		}
	}
}
