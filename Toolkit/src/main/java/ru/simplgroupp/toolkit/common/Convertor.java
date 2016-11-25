package ru.simplgroupp.toolkit.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

public class Convertor {

	private static Logger logger = Logger.getLogger(Convertor.class.getName());
	
	private static MessageFactory msgFactory = null;
	private static TransformerFactory transFactory;
	private static DatatypeFactory dataTypeFactory;
	private static HashMap<String,String> escH = new HashMap<String,String>(66);
	
	public static final String TYPE_SHORT_STRING = "C";
	public static final String TYPE_LONG_STRING = "T";
	public static final String TYPE_INTEGER = "N";
	public static final String TYPE_DOUBLE = "F";
	public static final String TYPE_DATE = "D";
	
	static {
		
		try {

			transFactory = TransformerFactory.newInstance();
			dataTypeFactory = DatatypeFactory.newInstance();
			escH.put("&#1040;", "А");
			escH.put("&#1041;", "Б");
			escH.put("&#1042;", "В");
			escH.put("&#1043;", "Г");
			escH.put("&#1044;", "Д");
			escH.put("&#1045;", "Е");
			escH.put("&#1046;", "Ж");
			escH.put("&#1047;", "З");
			escH.put("&#1048;", "И");
			escH.put("&#1049;", "Й");
			escH.put("&#1050;", "К");
			escH.put("&#1051;", "Л");
			escH.put("&#1052;", "М");
			escH.put("&#1053;", "Н");
			escH.put("&#1054;", "О");
			escH.put("&#1055;", "П");
			escH.put("&#1056;", "Р");
			escH.put("&#1057;", "С");
            escH.put("&#1058;", "Т");
            escH.put("&#1059;", "У");
            escH.put("&#1060;", "Ф");
            escH.put("&#1061;", "Х");
            escH.put("&#1062;", "Ц");
            escH.put("&#1063;", "Ч");
            escH.put("&#1064;", "Ш");
            escH.put("&#1065;", "Щ");
            escH.put("&#1066;", "Ъ");
            escH.put("&#1067;", "Ы");
            escH.put("&#1068;", "Ь");
            escH.put("&#1069;", "Э");
            escH.put("&#1070;", "Ю");
            escH.put("&#1071;", "Я");
            escH.put("&#1090;", "т");
            escH.put("&#1088;", "р");
            escH.put("&#1086;", "о");
            escH.put("&#1080;", "и");
            escH.put("&#1072;", "а");
            escH.put("&#1073;", "б");
            escH.put("&#1074;", "в");
            escH.put("&#1075;", "г");
            escH.put("&#1076;", "д");
            escH.put("&#1077;", "е");
            escH.put("&#1078;", "ж");
            escH.put("&#1079;", "з");
            escH.put("&#1083;", "л");
            escH.put("&#1081;", "й");
            escH.put("&#1082;", "к");
            escH.put("&#1084;", "м");
            escH.put("&#1085;", "н");
            escH.put("&#1087;", "п");
            escH.put("&#1089;", "с");
            escH.put("&#1091;", "у");
            escH.put("&#1092;", "ф");
            escH.put("&#1093;", "х");
            escH.put("&#1094;", "ц");
            escH.put("&#1095;", "ч");
            escH.put("&#1096;", "ш");
            escH.put("&#1097;", "щ");
            escH.put("&#1098;", "ъ");
            escH.put("&#1099;", "ы");
            escH.put("&#1100;", "ь");
            escH.put("&#1101;", "э");
            escH.put("&#1102;", "ю");
            escH.put("&#1103;", "я");
            
		    } catch (Exception e) {
		    	logger.log(Level.SEVERE,"Convertor - ошибка при инициализации статических переменных", e);
		}
		
		try {
			msgFactory = MessageFactory.newInstance();
		} catch (SOAPException e) {
			logger.severe("Ошибка при инициализации message factory");
		}
	}
	
	/**
	 * преобразует объект в дату
	 * @param dateObject - объект
	 * @param format - формат даты
	 */
    public static Date toDate(Object dateObject,String format) {
    	if (dateObject==null){
    		return null;
    	}
    	if (dateObject instanceof Date ) {
    		return (Date) dateObject;
    	}
    	
    	if (StringUtils.isEmpty(dateObject.toString())){
    		return null;
    	}
        Date date = new Date();
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse( dateObject.toString() );
        } catch (ParseException pe) {
        	logger.log(Level.SEVERE, "Convertor error ошибка преобразования даты "+dateObject, pe);
            return null;
        }
        return date;
    }

	/**
	 * преобразует Дату в в строку
	 * @param date - дата
	 * @param format - формат даты
	 */
	public static String dateToString(Date date,String format) {
		if (date==null){
			return null;
		}

		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
    
    /**
     * преобразует объект в double
     * @param doubleObject - объект
     * @param format - формат double
     */
    public static Double toDouble(Object doubleObject, String format) {
    	if (doubleObject==null){
    		return null;
    	}
    	if (StringUtils.isEmpty(doubleObject.toString())){
    		return null;
    	}
    	Double value = null;
        try {
            DecimalFormat dfFormat = new DecimalFormat(format);
            dfFormat.setMaximumFractionDigits(5);
            dfFormat.getDecimalFormatSymbols().setDecimalSeparator('.');
            value = new Double( dfFormat.parse(doubleObject.toString()).doubleValue() );
        } catch (Exception ex) {
        	logger.log(Level.SEVERE, "Convertor error ошибка преобразования double "+doubleObject, ex);
        	 return null;
        }
        return value;
    }
    
    public static BigDecimal toMoney(Object doubleObject) {
    	if (doubleObject==null){
    		return null;
    	}
    	
    	if (doubleObject instanceof BigDecimal) {
    		return (BigDecimal) doubleObject;
    	}
    	
    	if (doubleObject instanceof Number) {
    		return new BigDecimal( ((Number) doubleObject).doubleValue() );
    	}
    	
    	if (StringUtils.isEmpty(doubleObject.toString())){
    		return null;
    	}
    	Double value = null;
    	try { 
    		value=Double.parseDouble(doubleObject.toString());
    	} catch (Exception ex) {
    		logger.log(Level.SEVERE, "Convertor error ошибка преобразования double "+doubleObject, ex);
       	   return null;
       }
    	return new BigDecimal(value);    	
    }
    
    /**
     * преобразует объект в Double 
     * @param doubleObject - объект
     */
    public static Double toDouble(Object doubleObject) {
    	if (doubleObject==null){
    		return null;
    	}
    	
    	if (doubleObject instanceof Number) {
    		return ((Number) doubleObject).doubleValue();
    	}
    	
    	if (StringUtils.isEmpty(doubleObject.toString())){
    		return null;
    	}
    	Double value = null;
    	try { 
    		value=Double.parseDouble(doubleObject.toString());
    	} catch (Exception ex) {
    		logger.log(Level.SEVERE, "Convertor error ошибка преобразования double "+doubleObject, ex);
       	   return null;
       }
    	return value;
    }
    
    /**
     * преобразует объект в Float 
     * @param floatObject - объект
     */
    public static Float toFloat(Object floatObject) {
    	if (floatObject==null){
    		return null;
    	}
    	
    	if (floatObject instanceof Number) {
    		return ((Number) floatObject).floatValue();
    	}
    	
    	if (StringUtils.isEmpty(floatObject.toString())){
    		return null;
    	}
    	Float value = null;
    	try { 
    		value=Float.parseFloat(floatObject.toString());
    	} catch (Exception ex) {
    		logger.log(Level.SEVERE, "Convertor error ошибка преобразования double "+floatObject, ex);
       	   return null;
       }
    	return value;
    }
    
    /**
     * преобразует объект в Integer
     * @param intObject - объект
     */
    public static Integer toInteger(Object intObject) {
    	if (intObject==null){
    		return null;
    	}
    	
    	if (intObject instanceof Number) {
    		return ((Number) intObject).intValue();
    	}
    	
    	if (StringUtils.isEmpty(intObject.toString())){
    		return null;
    	}
        Integer value = null;
        try {
            value = Integer.parseInt(intObject.toString());
        } catch (Exception ex) {
        	 logger.log(Level.SEVERE, "Convertor error ошибка преобразования Integer "+intObject, ex);
        	 return null;
        }
        return value;
    }
  
    public static String toStringSafe(String[] source) {
    	String sres = "";
    	for (String ss: source) {
    		if (StringUtils.isEmpty(ss)) {
    			continue;
    		}
    		if (sres.length() > 0) {
    			sres = sres + ",";
    		}
    		sres = sres + ss.trim();
    	}
    	return sres;
    }
    
    /**
     * преобразует объект в строку
     * @param strObject - объект
     */
    public static String toString(Object strObject) {
    	if (strObject == null) {
    		return null;
    	} else {
    		return strObject.toString();
    	}
    }

    /**
     * преобразует объект в Boolean
     * @param boolObject - объект
     */
    public static Boolean toBoolean(Object boolObject) {
    	if (boolObject==null){
    		return null;
    	}
    	if (StringUtils.isEmpty(boolObject.toString())){
    		return null;
    	}
        Boolean value = null;
        try {
            value = Boolean.parseBoolean(boolObject.toString());
        } catch (Exception ex) {
        	 logger.log(Level.SEVERE, "Convertor error ошибка преобразования Boolean "+boolObject, ex);
        	 return null;
        }
        return value;
    }

    
    /**
     * преобразование в long
     * @param longObject - объект
     * @return
     */
    public static Long toLong(Object longObject) {
    	if (longObject==null){
    		return null;
    	}
    	if (StringUtils.isEmpty(longObject.toString())){
    		return null;
    	}
        Long value = null;
        try {
            value = Long.parseLong(longObject.toString());
        } catch (Exception ex) {
        	 logger.log(Level.SEVERE, "Convertor error ошибка преобразования Long "+longObject, ex);
        	 return null;
        }
        return value;
    }
    
    /**
     * преобразует строку в md5
     * @param password - строка
     */
    public static String toDigest(String password) throws Exception  {
    	  MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] digest = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(String.format("%02x", b & 0xff));
			}
         return  sb.toString();
    }
    
    /**
     * преобразует строку в md5, в определенной кодировке
     * 
     * @param password - строка
     * @param charset - кодировка
     */
    public static String toDigest(String password,String charset) throws Exception {
    	  MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes(charset));
			byte[] digest = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(String.format("%02x", b & 0xff));
			}
         return  sb.toString();
    }
    
    /**
     * преобразует soapMessage в строку
     * 
     * @param msg - сообщение
     */
    public static String soapToString(SOAPMessage msg) throws Exception {
		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		msg.writeTo(byteArrayOS);
		return new String(byteArrayOS.toByteArray());
    }
    
    /**
     * преобразует soapMessage в строку
     * 
     * @param msg - сообщение
     * @param charset - кодировка
     */
    public static String soapToString(SOAPMessage msg,String charset) throws Exception {
    	if (msg!=null){
		    ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		    msg.writeTo(byteArrayOS);
		    return new String(byteArrayOS.toByteArray(),charset);
    	}
    	return "";
    }
    
    /**
     * преобразует xml в строку 
     */
    public static String xmlToString(Node source) {	
		try {
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(source), new StreamResult(writer));
			String output = writer.getBuffer().toString();
			return output;
		} catch (TransformerException ex) {
			logger.log(Level.SEVERE, "Convertor error ошибка преобразования xml в строку ", ex);
			return null;
		}
	}
    
    /**
     * преобразует дату в строковое представление
     * 1 января 2010 г.
     * 
     * @param dt - дата
     */
	public static String toWriteDate(Date dt) {
		Calendar c1=Calendar.getInstance();
		c1.setTime(dt);
    	String st=new Integer(c1.get(Calendar.DAY_OF_MONTH)).toString()+" ";
		Integer m=c1.get(Calendar.MONTH);
    	switch (m){
    	case 0: st+="января";
    	     break;
    	case 1: st+="февраля";
	         break; 
    	case 2: st+="марта";
	         break;
    	case 3: st+="апреля";
	         break;
    	case 4: st+="мая";
	         break;
    	case 5: st+="июня";
	         break;   
    	case 6: st+="июля";
	         break;  
    	case 7: st+="августа";
	         break; 
    	case 8: st+="сентября";
             break;
    	case 9: st+="октября";
             break;
    	case 10: st+="ноября";
             break;
    	case 11: st+="декабря";
             break;     
    	    	}
    	st+=" " +new Integer(c1.get(Calendar.YEAR)).toString()+" г.";
    	return st;
    }
	
	/**
	 * убирает из строки лишние символы (при работе с масками) 
	 * 
	 * @param value - строка
	 */
	public static String fromMask(String value)	{
		if (StringUtils.isEmpty(value)){
			return "";
		}
		return value.replace("+","").replace("(", "").replace("-", "").replace(")", "").replace(" ", "");
	}
	
	/**
	 * преобразует строку в объект
	 * 
	 * @param type - вид преобразования
	 * @param strValue - строка
	 * @return
	 */
    public static Object toValue(String type, String strValue) {
    	if (StringUtils.isEmpty(strValue)){
    		return null;
    	}
    	if (TYPE_SHORT_STRING.equalsIgnoreCase(type)) {
    		return strValue;
    	} else if (TYPE_LONG_STRING.equalsIgnoreCase(type)) {
    		return toInteger(strValue);
    	} else if (TYPE_INTEGER.equalsIgnoreCase(type)) {
    		return toInteger(strValue);
    	} else if (TYPE_DOUBLE.equalsIgnoreCase(type)) {
    		return toDouble(strValue);
    	} else if (TYPE_DATE.equalsIgnoreCase(type)) {
    		return toDate(strValue, "yyyy-MM-dd");
    	} else {
    		return null;
    	}
    }
    
    /**
     * переводит дату в грегорианский календарь
     * @param isDateTime - если true, то дата и время, если false, то только дата
     * @param adate - дата, которую переводим
      */
    public static XMLGregorianCalendar dateToGreg(Date adate,Boolean isDateTime) {
		
    	GregorianCalendar calG = new GregorianCalendar(); 
		
		calG.setTime(adate);
		XMLGregorianCalendar dat=null;
		try {
			if (isDateTime) {
				dat=DatatypeFactory.newInstance().newXMLGregorianCalendar(calG);
			} else {
			    dat = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(calG.get(Calendar.YEAR), calG.get(Calendar.MONTH)+1, calG.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);
			}
		} catch (DatatypeConfigurationException e) {
			logger.log(Level.SEVERE, "Convertor error не удалось перевести дату в грегорианский календарь ", e);
			return null;
		}
		
		return dat;
	}

    /**
     * возвращает правильный decimal формат 
     * @return
     */
    public static String toDecimalString(String pattern,Double value){
    	if (value==null) {
    		return "0.00";
    	}
    	Locale locale  = new Locale("en", "UK");
    	NumberFormat nf = NumberFormat.getNumberInstance(locale);
    	DecimalFormat df = (DecimalFormat)nf;
    	df.applyPattern(pattern);
    	return df.format(value);
    }
    
    /**
     * возвращает строку определенной длины, если есть ограничения
     * 
     * @param str - исходная строка
     * @param sz - необходимая длина 
     */
    public static String toLimitString(String str,int sz){
    	if (StringUtils.isEmpty(str)){
    		return str;
    	}
    	if (str.length()>=sz){
    		return str.substring(0,sz-1);
    	}
    	else {
    		return str;
    	}
    }
    
    /**
     * преобразует строку в Soap Message
     * @param message - строка
      */
    public static SOAPMessage stringToSOAP(String message) {
        
    	InputStream input = new ByteArrayInputStream(message.getBytes());
    	try {
	    	try {
	    		SOAPMessage msg = msgFactory.createMessage(null, input);
	    		return msg;
			} finally {
	    		input.close();
	    	}
    	} catch (Exception e) {
    		logger.severe("Не удалось перевести строку в SOAP "+e.getMessage());
    	}
    	return null;
    }		
    
    /**
     * переводит строку в escape-последовательность
     */
    public static String toEscape(String S) {
       StringBuffer B = new StringBuffer();
       int CP;
       for(int i = 0; i < S.length(); i++)
       {
         CP = S.codePointAt(i);
         if (CP <= 255)
           if (((CP >= 48) & (CP <= 57)) | ((CP >= 65) & (CP <= 90)) |
              ((CP >= 97) & (CP <= 122)))
             B.append(S.charAt(i));
           else
             B.append("%"+Integer.toHexString(CP));
         else
         { 
           B.append("%26%23"+Integer.toString(S.codePointAt(i))+"%3B");
         }  
       }
       return B.toString();
    }  

    /**
     * преобразуем из html escape если заменены только русские буквы
     * 
     * @param str - строка для преобразования
     */
    public static String fromEscapeReplace(String str){
    	for (Map.Entry<String, String> e:escH.entrySet()) {
    		str=str.replace(e.getKey(), e.getValue());
    	}
    	return str;
    }

    /**
     * преобразуем из html escape если заменены только русские буквы посимвольно
     * 
     * @param Esc - строка для преобразования 
     */
    public static String fromEscapeHtml(String Esc) throws Exception {
      StringBuffer B = new StringBuffer();
      int i = 0;
      while (i < Esc.length()) {
        if (Esc.charAt(i) != '&') {  
        	  B.append(Esc.charAt(i));
              i++;
        }
        else {
        	if (i+2 < Esc.length() && Esc.substring(i+1, i+2).equals("#")) {
        		String subst=Esc.substring(i+2, i+6);
        		B.appendCodePoint(Integer.decode(subst));
        		i=i+7;
        	}
        }
      }
      return B.toString();
   }
    
    /**
     * преобразуем из закодированного escape если заменены только русские буквы посимвольно
     * 
     * @param Esc - строка для преобразования  
     */
    public static String fromEscape(String Esc) throws Exception {
      StringBuffer B = new StringBuffer();
      //String S = Esc;
      boolean H_E_Not_Pass;
      int i = 0;
      int j;
      while (i < Esc.length()) {
        if (Esc.charAt(i) != '%') {  
          B.append(Esc.charAt(i));
          i++;
        }
        else {
          H_E_Not_Pass = true;
          if (i+5 < Esc.length() && Esc.substring(i+1, i+6).equals("26%23"))
          {
            for (j=i+6; j < Esc.length() && Esc.charAt(j)>= '0' && Esc.charAt(j) <= '9'; j++);
            if ((j > i+6) && (Esc.substring(j, j+3).equalsIgnoreCase("%3b")))
            { 
              B.appendCodePoint(Integer.decode(Esc.substring(i+6, j)));
              H_E_Not_Pass = false;
              i = j+3;
            }  
            else
              throw new Exception("Wrong html escape sequence");
          }
          if (H_E_Not_Pass)
          {
            for (j=i+1; j < Esc.length() && (j < i+3) && ((Esc.charAt(j)>= '0' && Esc.charAt(j) <= '9') || (Esc.substring(j, j+1).compareToIgnoreCase("a") >= 0  && Esc.substring(j, j+1).compareToIgnoreCase("f") <= 0)); j++);
            if ((j > i+1) && (j <= i+3))
            {
              B.appendCodePoint(Integer.valueOf(Esc.substring(i+1, j), 16));
              i = j;
            }
            else
              throw new Exception("Wrong hex escape sequence");
          }
        }  
      }
      return B.toString();
    } 

/**
 * переводит строку иВаН в вид Иван
 * @param str - строка
 * @return
 */
@SuppressWarnings("deprecation")
public static String toRightString(String str){
	  if (StringUtils.isEmpty(str)) {
		  return "";
	  }
	  return StringUtils.capitalise(str.toLowerCase());
  }


/**
 * возвращает да или нет, если в поле числовое значение 1 или 0
 * @param state - числовое значение 1 или 0
 * @return
 */
public static String fromIntToYesNo(Integer state){
	if (state==null||state==0){
		return "нет";
	} else {
		return "да";
	}
}

/**
 * возвращает дату 
 * @param timestamp - unix timestamp
 * @return
 */
public static Date fromUnixTimestampToDate(Object timestamp){
	Long lt = Convertor.toLong(timestamp);
	if (lt == null){
		return null;
	} else {
		Date d = new Date();
		d.setTime(lt*1000);
		return d;
	}
  }

 /**
 * возвращает начало числового ранга
 * @param range - числовой ранг
 * @return
 */
  public static Integer integerRangeFrom(Object range){
	  if (!(range instanceof Range)){
		  return 0;
	  } else {
		  return Convertor.toInteger(((Range) range).getFrom());
	  }
  }
  
  /**
   * возвращает окончание числового ранга
   * @param range - числовой ранг
   * @return
   */
  public static Integer integerRangeTo(Object range){
	  if (!(range instanceof Range)){
		  return 0;
	  } else {
		  return Convertor.toInteger(((Range) range).getTo());
	  }
  }
  
  /**
   * возвращает начало  ранга дат
   * @param range -  ранг дат
   * @return
   */
  public static Date dateRangeFrom(Object range){
	  if (!(range instanceof Range)){
		  return new Date();
	  } else {
		  return (Date) (((Range) range).getFrom());
	  }
  }
  
  /**
   * возвращает окончание ранга дат
   * @param range -  ранг дат
   * @return
   */
  public static Date dateRangeTo(Object range){
	  if (!(range instanceof Range)){
		  return new Date();
	  } else {
		  return (Date) (((Range) range).getTo());
	  }
  }
  
  /**
   * конвертируем строку вида 1, 2, 3 в массив целых чисел
   * @param source - строка
   * @return
   */
  public static List<Integer> stringToIntegersList(String source){
	    if (StringUtils.isEmpty(source)){
	    	return new ArrayList<Integer>(0);
	    }
	    String[] sarr = source.split(",");
	    List<Integer> lstIds = new ArrayList<Integer>(sarr.length);
		for (String ss: sarr) {
			if (! StringUtils.isBlank(ss)) {
				try {
					lstIds.add(Convertor.toInteger(ss.trim()));
				} catch (Exception ex) {
					logger.severe("Не удалось добавить номер "+ss+", ошибка "+ex);
				}
			}
		}
		return lstIds;
  }

    /**
     * конверитирует телефонный номер вида 79993334455 в +7 (999) 333-44-55
     *
     * @param s телефонный номер
     * @return отформатированный номер либо null
     */
	public static String toPhoneFormat(String s) {
        Pattern p = Pattern.compile("(\\d{1})(\\d{3})(\\d{3})(\\d{2})(\\d{2})");
        Matcher m = p.matcher(s);

        String phoneNumber = null;
        if (m.find()) {
            if (m.groupCount() >= 5) {
                String firstGroup = m.group(1);
                String secondGroup = m.group(2);
                String thirdGroup = m.group(3);
                String fourthGroup = m.group(4);
                String fifthGroup = m.group(5);

                phoneNumber = "+" + firstGroup + " (" + secondGroup + ") " + thirdGroup + "-" + fourthGroup + "-" + fifthGroup;
            }
        }

        return phoneNumber;
    }
}
