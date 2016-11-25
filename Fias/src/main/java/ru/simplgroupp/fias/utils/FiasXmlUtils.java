package ru.simplgroupp.fias.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import ru.simplgroupp.fias.exception.FiasException;

/**
 * Утилиты для работы с документами XML.
 * @author CVB
 */
public class FiasXmlUtils {

  /**
   * Преобразует строку XML в документ DOM
   *
   * @param xmlText - строка для документа
   * @return Document
   * @throws ru.simplgroupp.fias.exception.FiasException
   */
  public static Document getDocFromString(String xmlText) throws FiasException {
    Document doc = null;
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      doc = db.parse(new InputSource(new StringReader(xmlText.substring(xmlText.indexOf("<?")).trim())));
    } catch (Exception e) {
      throw new FiasException(FiasException.INCORRECT_FORMAT, "Не удалось получить XML документ из строки.", e);
    }
    return doc;
  }
  
  /**
   * Преобразует файл XML в документ DOM
   * @param file Файл XML
   * @return
   * @throws FiasException 
   */
  public static Document getDocFromFile(File file) throws FiasException {
    String xml;
    try {
      xml = FileUtils.readFileToString(file, "UTF-8");
    } catch (IOException ex) {
      throw new FiasException(FiasException.FILE_SYSTEM_EXCEPTION, "Ошибка при чтении файла XML.", ex);
    }
    return getDocFromString(xml);
  }

  /**
   * Возвращает значение атрибута из определенного узла
   *
   * @param node - исследуемый узел
   * @param attrName - название атрибута
   * @return String
   */
  public static String getAttrubuteValue(Node node, String attrName) {
    String result = null;
    if (node != null) {
      NamedNodeMap attrs = node.getAttributes();
      if (attrs != null) {
        Node attrNode = attrs.getNamedItem(attrName);
        if (attrNode != null) {
          result = attrNode.getNodeValue();
        }
      }
    }
    if (StringUtils.isBlank(result)) result = null;
    return result;
  }

  public static String getStringAttr(Node node, String attrName, boolean required) throws FiasException {
    String result = getAttrubuteValue(node, attrName);
    return processStringAttr(attrName, result, required);
  }
  
  public static String getStringAttr(Attributes attrs, String attrName, boolean required) throws FiasException {
    String result = attrs.getValue(attrName);
    return processStringAttr(attrName, result, required);
  }
  
  public static Integer getIntegerAttr(Node node, String attrName, boolean required) throws FiasException {
    String strValue = getAttrubuteValue(node, attrName);
    return processIntegerAttr(attrName, strValue, required);
  }
  
  public static Integer getIntegerAttr(Attributes attrs, String attrName, boolean required) throws FiasException {
    String strValue = attrs.getValue(attrName);
    return processIntegerAttr(attrName, strValue, required);
  }
  
  public static Date getDateAttr(Node node, String attrName, boolean required) throws FiasException {
    String strValue = getAttrubuteValue(node, attrName);
    return processDateAttr(attrName, strValue, required);
  }
  
  public static Date getDateAttr(Attributes attrs, String attrName, boolean required) throws FiasException {
    String strValue = attrs.getValue(attrName);
    return processDateAttr(attrName, strValue, required);
  }
  
  public static Node getChildNode(Node parent, String childName) {
    Node result = null;
    if (parent != null) {
      NodeList nl = parent.getChildNodes();
      if (nl != null)
        for (int i = 0; i < nl.getLength(); i++)
          if (nl.item(i).getNodeName().equalsIgnoreCase(childName)) {
            result = nl.item(i);
            break;
          }
    }
    return result;
  }
  
  public static String getChildNodeValue(Node parent, String childName, boolean required) throws FiasException {
    String result = null;
    Node node = getChildNode(parent, childName);
    if (node != null) result = node.getTextContent();
    if (required && StringUtils.isBlank(result))
      throw new FiasException(FiasException.INCORRECT_FORMAT, "Не определено значение элемента " + childName + ".");
    return result;
  }
  
  /**
   * Выполняет SAX-парсинг файла XML
   * @param file Файл XML
   * @param handler Обработчик
   * @throws FiasException 
   */
  public static void processXmlFile(File file, DefaultHandler handler) throws FiasException {
    XMLReader xmlReader;
    try {
      SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
      xmlReader = saxParser.getXMLReader();
      xmlReader.setContentHandler(handler);
    } catch (ParserConfigurationException | SAXException e) {
      throw new FiasException(FiasException.XML_EXCEPTION, "Ошибка при инициализации парсера документа XML.", e);
    }
    try {
      xmlReader.parse(new InputSource(new FileInputStream(file)));
    } catch (IOException e) {
      throw new FiasException(FiasException.FILE_SYSTEM_EXCEPTION, "Ошибка при чтении файла XML.", e);
    } catch (SAXException e) {
      Exception ex = e.getException();
      if (ex != null && ex instanceof FiasException)
        throw (FiasException) ex;
      throw new FiasException(FiasException.XML_EXCEPTION, "Ошибка при декодировании файла XML.", e);
    }
  }
  
  private static String processStringAttr(String name, String value, boolean required) throws FiasException {
    if (required && StringUtils.isBlank(value))
      throw new FiasException(FiasException.VALUE_REQUIRED, "Отсутствует значение обязательного атрибута " + name + ".");
    return value;
  }
  
  private static Integer processIntegerAttr(String name, String value, boolean required) throws FiasException {
    Integer result = null;
    if (StringUtils.isNotBlank(value))
      try {
        result = Integer.parseInt(value);
      } catch (Exception e) {
        throw new FiasException(FiasException.INCORRECT_FORMAT, "Некорректный формат целочисленного атрибута " + name + ".", e);
      }
    if (required && result == null)
      throw new FiasException(FiasException.VALUE_REQUIRED, "Отсутствует значение обязательного атрибута " + name + ".");
    return result;
  }
  
  private static Date processDateAttr(String name, String value, boolean required) throws FiasException {
    Date result = null;
    if (StringUtils.isNotBlank(value))
      try {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        result = dateFormat.parse(value);
      } catch (ParseException e) {
        throw new FiasException(FiasException.INCORRECT_FORMAT, "Некорректный формат атрибута " + name + ".", e);
      }
    if (required && result == null)
      throw new FiasException(FiasException.VALUE_REQUIRED, "Отсутствует значение обязательного атрибута " + name + ".");
    return result;
  }
  
}
