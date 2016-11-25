package ru.simplgroupp.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.simplgroupp.exception.PeopleException;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.Documents;

public class CheckRemoteDbUtils {

	final static Logger logger = LoggerFactory.getLogger(CheckRemoteDbUtils.class.getName());
	 /**
     * проверяем действительность паспорта
     * 
     * @param passpNumber - серия и номер паспорта
     * @param params - константы для обращения к внешней БД
     * @return
     * @throws PeopleException
     */
	public static Integer checkPaspValidity(String passpNumber,Map<String,Object> params) throws PeopleException {
		if (StringUtils.isNotEmpty(passpNumber)){
			//урл для БД фмс
        	
            String fmsSite = (String) params.get(SettingsKeys.FMS_DB_URL);
            String fmsLogin=(String) params.get(SettingsKeys.FMS_DB_URL_LOGIN);
            String fmsPassword=(String) params.get(SettingsKeys.FMS_DB_URL_PASSWORD);
            //если в БД есть url и логин с паролем
           	if (StringUtils.isNotEmpty(fmsSite)&&StringUtils.isNotEmpty(fmsLogin)&&StringUtils.isNotEmpty(fmsPassword)){
           		logger.info("Url "+fmsSite+passpNumber);

                //посылаем запрос, получаем ответ
       	        byte[] response=null;
			    try {
				    Map<String,String> rparams=new HashMap<String,String>();
				    rparams.put("Authorization", "Basic "+HTTPUtils.getBasicAuthorizationString(fmsLogin, fmsPassword));
				    response = HTTPUtils.sendHttp("GET", fmsSite+passpNumber,null, rparams, null);
			    } catch (Exception e) {
				    logger.error("Не удалось получить ответ от сайта "+fmsSite+", ошибка "+e);
				   
			    }
			    //есть ответ
			    if (response!=null){
        			String answer=new String(response);
        			 JSONParser parser = new JSONParser();
        			 JSONObject jsonAnswer=null;
        			 //пытаемся его разобрать
					 try {
						jsonAnswer = (JSONObject) parser.parse(answer);
					 } catch (ParseException e) {
						logger.error("Не удалось разобрать ответ из БД фмс "+answer);
						
					 }

					 //если удалось разобрать ответ
        			 if (jsonAnswer!=null){
        			     //проверяем статус ответа
        				 Integer nstatus=Convertor.toInteger(jsonAnswer.get("code"));
        				 //если паспорт найден в БД
        				 if (nstatus==1){
        					 return Documents.INVALID;
        				 } else if (nstatus==2){
        					 //если была ошибка
        					 String emessage=(String) jsonAnswer.get("error");
        					 logger.error("Сервис вернул ошибку "+emessage);
     						
        				 } else {
        					 return Documents.VALID;
          				 }
      
        			 }
			    }//end if response is not null
           	}//end if fmsSite is not null
		}//end if passport is not null
		return null;
	}
}
