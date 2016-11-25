package ru.simplgroupp.util;

import org.dom4j.DocumentException;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import ru.simplgroupp.exception.KassaException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class XmlUtils {

	private static Logger log = Logger.getLogger(XmlUtils.class.getName());
    private static TransformerFactory transformerFactory;
    public static final String ENCODING_WINDOWS = "Windows-1251";
    public static final String ENCODING_UTF = "UTF-8";
    public static final String ENCODING_UTF_16 = "UTF-16";
    public static final String HEADER_XML_WINDOWS="<?xml version='1.0' encoding='windows-1251'?>";
    public static final String HEADER_XML_UTF="<?xml version='1.0' encoding='utf-8'?>";
    
    static {
        try {
            transformerFactory = TransformerFactory.newInstance();
        } catch (Exception e) {
        	log.severe("Не удалось инициализировать Transformer Factory");
            throw new RuntimeException(e);
        }
    }

    /**
     * ищем элемент в родительском элементе
     * @param parent - родительский элемент, в котором ищем
     * @param bRequired - обязательный это элемент или нет
     * @param tagName - название тэга, который ищем
     */
    public static Element findElement(boolean bRequired, Element parent, String tagName)
            throws KassaException {
    	if (parent==null){
    		return null;
    	}
        NodeList ndl = parent.getElementsByTagName(tagName);
        if (ndl == null || ndl.getLength() == 0) {
        	//если это обязательный элемент
            if (bRequired){
                throw new KassaException("Не найден элемент " + tagName);
            } else {
                return null;
            }
        }
        return (Element) ndl.item(0);
    }

    /**
     * ищем элемент в документе
     * @param parent - документ
     * @param bRequired - обязательный это элемент или нет
     * @param tagName - название тэга, который ищем
     */
    public static Element findElement(boolean bRequired, Document parent, String tagName)
            throws KassaException {
    	if (parent==null){
    		return null;
    	}
        NodeList ndl = parent.getElementsByTagName(tagName);
        if (ndl == null || ndl.getLength() == 0) {
        	//если это обязательный элемент
            if (bRequired){
                throw new KassaException("Не найден элемент " + tagName);
            } else {
                return null;
            }
        }
        return (Element) ndl.item(0);
    }

    /**
     * ищет первый дочерний элемент
     * @param parent - узел, в котором ищем
     */
    public static Element firstChildElement(Element parent) {
    	if (parent==null){
    		return null;
    	}
        NodeList lst = parent.getChildNodes();
        for (int i = 0; i <= lst.getLength(); i++) {
            Node nd = lst.item(i);
            if (nd.getNodeType() == Node.ELEMENT_NODE){
                return (Element) nd;
            }
        }
        return null;
    }


    /**
     * ищет и возвращает лист из подузлов  с конкретным именем 
     * @param parent - где ищем
     * @param name - название тэга, который ищем
     */
    public static List<Node> findNodeList(Node parent, String name) {

        ArrayList<Node> nres = new ArrayList<Node>(0);
        if (parent != null) {
            NodeList lst = parent.getChildNodes();
            for (int i = 0; i < lst.getLength(); i++) {
                Node nd = lst.item(i);

                if (name.equalsIgnoreCase(nd.getNodeName())){
                    nres.add(nd);
                }

            }
        }
        return nres;
    }

    /**
     * Ищет, есть ли в документе подузел с именем childname в узле с именем nodename
     * при переборе в цикле npp-итератор, в остальных случаях - 0
     * @param dct - документ
     * @param nodename - название узла
     * @param childname - название дочернего узла
     * @param npp - итератор для цикла, в остальных случаях 0
     */
    public static boolean isExistChildInNode(Document dct, String nodename, String childname, Integer npp) {
    	Boolean bl = false;
    	//нет документа
    	if (dct==null){
    		return bl;
    	}
    	//нет хотя бы одного узла
    	NodeList ndl = dct.getElementsByTagName(nodename);
    	if (ndl==null||ndl.getLength()==0){
    		return bl;
    	}
    	
        Node nd = ndl.item(npp);
        //не найдена нода с соотв.номером
        if (nd == null){
            return bl;
        }
        
        NodeList ndlch = nd.getChildNodes();
        if (!(ndlch == null || ndlch.getLength() == 0)) {
            for (int i = 0; i < ndlch.getLength(); i++) {
                Node ndch = ndlch.item(i);
                if (ndch.getNodeName().equalsIgnoreCase(childname)) {
                    bl = true;
                    break;
                }
            }
        }
        return bl;
    }

    /**
     * Ищет, есть ли в передаваемом узле подузел с именем childname
     * при переборе в цикле npp-итератор, в остальных случаях - 0
     * @param dct - узел, в котором ищем
     * @param childname - имя дочернего узла
     * @param npp - итератор для цикла, в остальных случаях 0
     */
    public static boolean isExistChildInNode(Node dct, String childname, Integer npp) {
        Boolean bl = false;
        //нет узла
    	if (dct==null){
    		return bl;
    	}
        int cnt=0;
        NodeList ndlch = dct.getChildNodes();
        if (!(ndlch == null || ndlch.getLength() == 0)) {
            for (int i = 0; i < ndlch.getLength(); i++) {
                Node ndch = ndlch.item(i);
                if (ndch.getNodeName().equalsIgnoreCase(childname)) {
                	//если номер соответствует итератору
                	if (cnt==npp){
                        bl = true;
                        break;
                	} else {
                		cnt++;
                	}
                }
            }
        }
        return bl;
    }

    /**
     * Ищет в документе подузел с именем childname в узле с именем nodename
     * при переборе в цикле npp-итератор, в остальных случаях - 0,
     * возвращает подузел, либо null
     * @param dct - документ
     * @param nodename - название узла
     * @param childname - название дочернего узла
     * @param npp - итератор для цикла, в остальных случаях 0
     */
    public static Node findChildInNode(Document dct, String nodename, String childname, Integer npp) {
    	//нет документа
    	if (dct==null){
    		return null;
    	}
    	NodeList ndl = dct.getElementsByTagName(nodename);
    	if (ndl==null){
    		return null;
    	}
        Node nd = ndl.item(npp);
        if (nd==null){
        	return null;
        }
        Node nr = null;
        NodeList ndlch = nd.getChildNodes();
        int cnt=0;
        if (!(ndlch == null || ndlch.getLength() == 0)) {
            for (int i = 0; i < ndlch.getLength(); i++) {
                Node ndch = ndlch.item(i);
                if (ndch.getNodeName().equalsIgnoreCase(childname)) {
                	//если номер соответствует итератору
                	if (cnt==npp){
                        nr = ndch;
                        break;
                	} else {
                		cnt++;
                	}
                }
            }
        }
        return nr;
    }

    /**
     * Ищет в заданном узле подузел с именем childname
     * при переборе в цикле npp-итератор, в остальных случаях - 0,
     * возвращает подузел, либо null
     * @param dct - узел, в котором ищем
     * @param childname - имя дочернего узла
     * @param npp - итератор для цикла, в остальных случаях 0
     */
    public static Node findChildInNode(Node dct, String childname, Integer npp) {

    	if (dct==null){
    		return null;
    	}
    	
        Node nr = null;
        NodeList ndlch = dct.getChildNodes();
        int cnt=0;
        if (!(ndlch == null || ndlch.getLength() == 0)) {
            for (int i = 0; i < ndlch.getLength(); i++) {
                Node ndch = ndlch.item(i);
                if (ndch.getNodeName().equalsIgnoreCase(childname)) {
                	//если номер соответствует итератору
                	if (cnt==npp){
                        nr = ndch;
                        break;
                	} else {
                		cnt++;
                	}
                }
            }
        }
        return nr;
    }


    /**
     * Ищет, есть ли в документе узел с именем nodename
     * @param dct - документ
     * @param nodename - имя узла
     */
    public static boolean isExistNode(Document dct, String nodename) {
    	//нет документа
    	if (dct==null){
    		return false;
    	}

    	NodeList ndl = dct.getElementsByTagName(nodename);
        if (ndl == null || ndl.getLength() == 0) {
            return false;
        } else {
        	return true;
        }
    }

    /**
     * Возвращает значение атрибута из определенного узла
     * @param nd - узел
     * @param attrName - название атрибута
     */
    public static String getAttrubuteValue(Node nd, String attrName) {
        if (nd == null) {
            return "";
        }
        NamedNodeMap attr = nd.getAttributes();
        Node nameAttrib = null;
        if (attr!=null){		
        	nameAttrib = attr.getNamedItem(attrName);
        } else {
        	return "";
        }
        if (nameAttrib == null){
            return "";
        } else {
            return nameAttrib.getNodeValue();
        }
    }

    /**
     * Возвращает узел по значению атрибута
     * @param dc - документ
     * @param nodeName - имя узла
     * @param attrName - имя атрибута
     * @param attrValue - значение атрибута
     */
    public static Node getNodeFromAttributeValue(Document dc, String nodeName, String attrName, String attrValue) {
    	//нет документа
    	if (dc==null){
    		return null;
    	}

    	NodeList ndl = dc.getElementsByTagName(nodeName);
        if (ndl != null&&ndl.getLength()>0) {
            for (int i = 0; i < ndl.getLength(); i++) {
                Node nd = ndl.item(i);
                NamedNodeMap attr = nd.getAttributes();
                if (attr != null) {
                    Node nameAttrib = attr.getNamedItem(attrName);
                    if (nameAttrib != null && nameAttrib.getNodeValue().equals(attrValue)) {
                        return nd;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Возвращает узел по значению атрибута из списка узлов
     * @param ndl - список узлов
     * @param nodeName - имя узла
     * @param attrName - имя атрибута
     * @param attrValue - значение атрибута
     */
    public static Node getNodeFromAttributeValue(List<Node> ndl, String nodeName, String attrName, String attrValue) {
        if (ndl != null) {
            for (Node nd : ndl) {
                NamedNodeMap attr = nd.getAttributes();
                if (attr != null) {
                    Node nameAttrib = attr.getNamedItem(attrName);
                    if (nameAttrib != null && nameAttrib.getNodeValue().equals(attrValue)) {
                        return nd;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Возвращает узел-child по значению атрибута
     * @param node - узел
     * @param nodeName - имя узла
     * @param attrName - имя атрибута
     * @param attrValue - значение атрибута 
     */
    public static Node getNodeFromAttributeValue(Node node, String nodeName, String attrName, String attrValue) {
        if (node != null) {
            NodeList ndl = node.getChildNodes();
            if (ndl != null) {
                for (int i = 0; i < ndl.getLength(); i++) {
                    Node nd = ndl.item(i);
                    NamedNodeMap attr = nd.getAttributes();
                    if (attr != null) {
                        Node nameAttrib = attr.getNamedItem(attrName);
                        if (nameAttrib != null && nameAttrib.getNodeValue().equals(attrValue)){
                            return nd;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * возвращает значение из ноды, либо пустую строку
     * @param nd - узел
     */
    public static String getNodeValueText(Node nd) {
        String st = "";
        if (nd != null){
            st = nd.getTextContent();
        }
        return st;
    }

    /**
     * перекодирует сообщение str из любой кодировки first в любую кодировку second
     * @param str - сообщение
     * @param first - исходная кодировка
     * @param second - кодировка, которую нужно получить
     */
    public static String getRightUncode(String str, String first, String second) throws Exception {
        InputStream in = new ByteArrayInputStream(str.getBytes(first));

        int b;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((b = in.read()) != -1) {
            baos.write(b);
        }

        // Перекодирование байтов в строку с использованием кодировки по умолчанию
        String st = baos.toString(second);

        return st;
    }

    /**
     * возвращает данные из cdata
     * @param node - узел
     */
    public static String getCharacterDataFromNode(Node node) {

      if (node!=null){
        if (node instanceof CharacterData) {
            CharacterData cd = (CharacterData) node;
            return cd.getData();
        }
      }
      return "";
    }

    /**
     * возвращает массив байтов xml, преобразованный путем xsl трансформации
     *
     * @param sxml    - xml строкой
     * @param sxsl    - xsl строкой
     * @param xslRoot - абсолютный путь к шаблону xsl
     */
    public static byte[] xmlByXsl(String sxml, String sxsl, String xslRoot) throws KassaException {
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        try {
            SAXReader xslReader = new SAXReader();
            xslReader.setValidation(false);
            DocumentSource xslStream = new DocumentSource(xslReader.read(new StringReader(sxsl)));

            ErrorTransformCollector ec = new ErrorTransformCollector();
            transformerFactory.setErrorListener(ec);

            Transformer transformer = transformerFactory.newTransformer(xslStream);
            transformer.setParameter("xslRoot", xslRoot);

            SAXReader xmlReader = new SAXReader();
            xmlReader.setValidation(false);

            DocumentSource in = new DocumentSource(xmlReader.read(new StringReader(sxml)));

            ByteArrayOutputStream stm = new ByteArrayOutputStream();
            StreamResult out = new StreamResult(stm);

            transformer.transform(in, out);
            return stm.toByteArray();
        } catch (NullPointerException e) {
        	log.severe("Не удалось подключить трансформер "+e.getMessage());
            throw new KassaException("Не удалось подключить трансформер " + e);
        } catch (TransformerConfigurationException e) {
        	log.severe("Не удалось преобразовать xml используя xsl "+e.getMessage());
            throw new KassaException("Не удалось преобразовать xml используя xsl " + e);
        } catch (TransformerException e) {
        	log.severe("Не удалось преобразовать xml используя xsl "+e.getMessage());
            throw new KassaException("Не удалось преобразовать xml используя xsl " + e);
        } catch (DocumentException e) {
        	log.severe("Не удалось преобразовать xml используя xsl "+e.getMessage());
            throw new KassaException("Не удалось преобразовать xml используя xsl " + e);
        }
    }

    /**
     * преобразует строку в документ, если в исходной строке нет заголовка xml
     * @param respmessage - строка для документа
     * @param header - заголовок
     */
    public static Document getDocFromString(String respmessage, String header) throws KassaException {
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            doc = db.parse(new InputSource(new StringReader(header.trim() + respmessage.trim())));
        } catch (Exception e) {
        	log.severe("Не удалось получит xml документ из строки "+e.getMessage());
            throw new KassaException("Не удалось получит xml документ из строки "+e.getMessage());
        }

        return doc;
    }

    /**
     * преобразует строку в документ
     * @param respmessage - строка для документа
     */
    public static Document getDocFromString(String respmessage) throws KassaException {
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            doc = db.parse(new InputSource(new StringReader(respmessage.substring(respmessage.indexOf("<?")).trim())));
        } catch (Exception e) {
        	log.severe("Не удалось получит xml документ из строки "+e.getMessage());
            throw new KassaException("Не удалось получит xml документ из строки "+e);
        }

        return doc;
    }

    /**
     * возвращает чистую строку xml
     * @param str - строка xml
     * @param hdr - заголовок
     * @param rootel - корневой элемент xml
     */
    public static String getXmlFromRaw(String str, String hdr, String rootel) {
        int n = str.indexOf(hdr);
        int n1 = str.lastIndexOf("</" + rootel + ">");
        String xmlContent = "";
        if (n > 0 && n1 > 0){
            xmlContent = str.substring(n, n1 + ("</" + rootel + ">").length());
        }
        return xmlContent;
    }
    
    /**
     * возвращает xml без заголовка
     * @param str - строка xml
     * @param hdr - заголовок
      */
    public static String getXmlWithoutHeader(String str,String hdr){
    	int n = str.lastIndexOf(hdr);
    	if (n<0) {
    		return str;
    	}
    	int n1=str.lastIndexOf("?>");
    	if (n1<0) {
    		return str;
    	} else {
    		return str.substring(n1+2);
    	}
    }
    
    /**
     * проверяем, заканчивается ли строка на слеш, и если не заканчивается, то добавляем
     * @param source - строка
     */
    public static String checkSlash(String source) {
		if ( source.charAt(source.length()-1) != '/') {
			return source + '/';
		} else {
			return source;
		}
	}

    public static String docToString(Document doc) throws TransformerException {
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);
        writer.flush();
        return writer.toString();
    }

    public static Document createDoc() throws ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.newDocument();
    }
}
