package ru.simplgroupp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlUtils {

	final static Logger log = LoggerFactory.getLogger(HtmlUtils.class.getName());
	
	public static String makeHtmlFromText(String charset,String text){
		StringBuilder sb=new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<meta http-equiv='Content-Type' content='text/html; charset="+charset+"'>");
		sb.append("<meta http-equiv='Content-Language' content='ru'>");
		sb.append("</head>");
		return sb.toString()+text+"</html>";
	}
}
