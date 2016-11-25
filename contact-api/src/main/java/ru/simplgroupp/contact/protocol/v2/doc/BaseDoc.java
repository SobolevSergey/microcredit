package ru.simplgroupp.contact.protocol.v2.doc;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ru.simplgroupp.contact.util.DocUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by aro on 11.09.2014.
 */
public class BaseDoc {
    //attributes, just constants
    public static final String OBJ_CLASS = "OBJECT_CLASS";
    public static final String POINT_CODE = "POINT_CODE";
    public static final String USER_ID = "USER_ID";
    public static final String INT_SOFT_ID = "INT_SOFT_ID";
    public static final String SERVER_IP = "SERVER_IP";
    public static final String CLIENT_IP = "CLIENT_IP";
    public static final String LANG = "Lang";
    public static final String ACTION = "ACTION";
    public static final String VERSION = "Version";
    public static final String TYPE_VERSION = "TYPE_VERSION";
    public static final String PORTION = "PORTION";
    public static final String PACK = "Pack";
    public static final String BOOK_ID = "BOOK_ID";
    public static final String PPCODE = "PPCode";
    public static final String CODE = "Code";
    public static final String STAMP = "Stamp";
    public static final String ENCRYPTION = "Encryption";
    public static final String PART = "PART";
    public static final String ALGO = "ALGO";
    public static final String CURR_ID = "CURR_ID";
    public static final String DEBET = "DEBET";

    public static final String EXPECTSIGNED = "ExpectSigned";
    public static final String EXPECTSIGNED_UPPER = "EXPECTSIGNED";
    public static final String VERSION_UPPER = "VERSION";
    public static final String PACK_UPPER = "PACK";
    public static final String DOC_ID = "DOC_ID";
    public static final String INOUT = "INOUT";
    public static final String CorrespondentID = "CorrespondentID";


    public static final String DTU_VERSION_VALUE = "1";
    public static final String PACK_VALUE = "ZLIB"; //for java programmers
    public static final String LANG_VALUE = "ru";
    public static final String CODE_VALUE = "BASE64";
    public static final String PLAIN_ENCRYPTION_VALUE = "PLAIN";
//    public static final String PPCODE_VALUE = "KSOR";
//    public static final String PPCODE_VALUE = "GJPI";
    public static final String PPCODE_VALUE = "ONTR";
    public static final String TEST_PPCODE_VALUE = "AFUZ";
    //public static final String TEST_POINT_CODE_VALUE = "AFRM";
    public static final String ENCODING_VALUE = "US-ASCII";
    public static final String ENCODING_VALUE_WIN1251 = "WINDOWS-1251";
    public static final String ENCODING_VALUE_UTF8 = "UTF-8";

    public static final String ECR3410_ALGO_VALUE = "ECR3410";
    public static final String TYPE_VERSION_VALUE = "I";
    public static final String INT_SOFT_ID_VALUE = "6704EDCC-9B92-4174-85C1-827183F55AD3";//"0017A175-9BD6-487C-AB30-6BF570BCD68E";
    public static final String NO_EXPECTSIGNED_VALUE = "No";
    public static final String NO_EXPECTSIGNED_VALUE_UPPER = "NO";
    public static final String IN_INOUT_VALUE = "I";
    public static final String OUT_INOUT_VALUE = "O";
    public static final String CORRESPONDENT_ID_VALUE = "2";
    public static final String PICKUP_POINT_BEZADR_VALUE = "CDPA";
    public static final String CURR_ID_VALUE = "0";



    public static final String APP_SERV_ROOTNAME = "REQUEST";
    public static final String DTU_ROOTNAME = "CDTRN";
    public static final String DATA = "DATA";
    public static final String SIGNATURE = "SIGNATURE";
    public static final String SIGNATURES = "SIGNATURES";



    public static final String trnSendPoint = "trnSendPoint";
    public static final String trnPickupPoint = "trnPickupPoint";
    public static final String trnService = "trnService";
    public static final String trnClAmount = "trnClAmount";
    public static final String trnClCurrency = "trnClCurrency";
    public static final String trnFeesClient = "trnFeesClient";
    public static final String trnFeesClientLocal = "trnFeesClientLocal";
    public static final String sName = "sName";
    public static final String sLastName = "sLastName";


    public static final String sSurName = "sSurName";
    public static final String sCountry = "sCountry";
    public static final String sZipCode = "sZipCode";
    public static final String sRegion = "sRegion";
    public static final String sCity = "sCity";
    public static final String sAddress = "sAddress";
    public static final String sPhone = "sPhone";
    public static final String sIDtype = "sIDtype";
    public static final String sIDtype_Value = "PASSPORT";
    public static final String sIDnumber = "sIDnumber";
    public static final String sIDdate = "sIDdate";
    public static final String sIDwhom = "sIDwhom";
    public static final String sIDexpireDate = "sIDexpireDate";
    public static final String bName = "bName";
    public static final String bLastName = "bLastName";
    public static final String bSurName = "bSurName";

    public static final String sResident = "sResident";
    public static final String sResidentC = "sResidentC";
    public static final String sBirthday = "sBirthday";
    public static final String sBirthPlace = "sBirthPlace";
    public static final String trnCurrency = "trnCurrency";
    public static final String trnDate = "trnDate";
    public static final String trnReference = "trnReference";
    public static final String trnAmount = "trnAmount";
    public static final String trnAdditionalInfo = "trnAdditionalInfo";
    public static final String bBirthday = "bBirthday";
    public static final String bCountry = "bCountry";
    public static final String bZipCode = "bZipCode";
    public static final String bRegion = "bRegion";
    public static final String bCity = "bCity";
    public static final String bAddress = "bAddress";
    public static final String bPhone = "bPhone";
    public static final String bIDtype = "bIDtype";
    public static final String bIDnumber = "bIDnumber";

    public static final String bIDdate = "bIDdate";
    public static final String bIDwhom = "bIDwhom";
    public static final String bIDexpireDate = "bIDexpireDate";
    public static final String bResident = "bResident";
    public static final String trnRate = "trnRate";
    public static final String trnFeesClientCurr = "trnFeesClientCurr";
    public static final String sCountryC = "sCountryC";
    public static final String bCountryC = "bCountryC";



    protected static DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();

    private static final Logger logger = Logger.getLogger(BaseDoc.class.getName());

    protected DocumentBuilder icBuilder;
    protected Document doc;

    protected Map<String,String> attrMap;
    protected Map<String,String> childrenMap;
    protected Element rootElement;
    protected String rootName;


    /**
     * Simple version,can be overridden
     */
    protected void toXml(){
        try {
            //add attrs
            DocUtils.addAttrs(rootElement, attrMap);
            //add children
            DocUtils.addSimpleChildren(rootElement,childrenMap,doc);
            doc.appendChild(rootElement);
        } catch (DOMException e) {
            e.printStackTrace();
            logger.severe("Ошибка создания общего xml документа для запроса на AppServ.");
        }
    }

    /**
     * Simple version,can be overridden
     */
    public void init(String rootName,Map<String,String> attrMAp,Map<String,String> childrenMap){
        this.rootName=rootName;
        this.attrMap=attrMAp;
        this.childrenMap=childrenMap;
    }

    protected ByteArrayOutputStream transformToXml(){
        try {
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty("encoding", ENCODING_VALUE_WIN1251);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            StreamResult streamResult=new StreamResult(bos);
            DOMSource source = new DOMSource(doc);

            transformer.transform(source, streamResult);
            return bos;
        } catch (TransformerException e) {
            e.printStackTrace();
            logger.severe("Ошибка преобразования Dom документа в байты.");
            return null;
        }
    }
    protected byte[] toBytes(){
        ByteArrayOutputStream bos = transformToXml();
        if(bos!=null){
            return bos.toByteArray();
        }
        return null;
    }


    public String toString(){
        ByteArrayOutputStream bos = transformToXml();
        if(bos!=null){
            return bos.toString();
        }
        return null;
    }
    private void process(){
        try {
            icBuilder = icFactory.newDocumentBuilder();
            doc = icBuilder.newDocument();
            rootElement = doc.createElement(rootName);
            toXml();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            logger.severe("Ошибка создания билдера xml документов для запроса на AppServ.");
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Ошибка генерации XML.");
        }
    }

    public byte[] generateBytes(){
         process();
         return toBytes();
     }

    public String generateString(){
        process();
        return toString();
    }

}
