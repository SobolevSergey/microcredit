package ru.simplgroupp.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class GeoUtils {
		
	final static Logger log = LoggerFactory.getLogger(GeoUtils.class.getName());
	
	/**
	 * возвращает xml-документ с гео-позицией
	 * @param url - урл для обращения
	 * @return
	 * @throws Exception
	 */
	public static final Document geoDescriptionDoc(String url) throws Exception{
		
		Map<String,String> rparams=new HashMap<String,String>();
		rparams.put("Content-Type", "text/html; charset=windows-1251");
	
		 byte[] rmessage= HTTPUtils.sendHttp("GET", url, null, rparams, null);
				
		   if (rmessage!=null) {
				String aanswer= new String(rmessage,XmlUtils.ENCODING_WINDOWS);
				
				if (StringUtils.isNotEmpty(aanswer)){
				    Document doc=XmlUtils.getDocFromString(aanswer);
				    log.info(aanswer);
				    return doc;
		        }
		
		   }
		   return null;
	}
	
	/**
	 * описание гео-позиции
	 * @param doc - xml-документ с гео-локацией
	 * @return
	 * @throws Exception
	 */
	public static final String geoDescription(Document doc) throws Exception{
		 String descr="";
		 if (doc!=null) {
			 if (XmlUtils.isExistNode(doc, "country")){
				 descr+="страна - " +XmlUtils.getNodeValueText(doc.getElementsByTagName("country").item(0))+"; ";
			 }
			 if (XmlUtils.isExistNode(doc, "district")){
				 descr+="округ - " +XmlUtils.getNodeValueText(doc.getElementsByTagName("district").item(0))+"; ";
			 }
			 if (XmlUtils.isExistNode(doc, "region")){
				 descr+="регион - " +XmlUtils.getNodeValueText(doc.getElementsByTagName("region").item(0))+"; ";
			 }
			 if (XmlUtils.isExistNode(doc, "city")){
				 descr+="город - " +XmlUtils.getNodeValueText(doc.getElementsByTagName("city").item(0))+".";
			 }
		  }
		 log.info(descr);
		 return descr;
	}
	
}
