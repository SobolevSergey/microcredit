package ru.simplgroupp.contact.protocol.v2;

import org.apache.commons.codec.binary.Base64;
import ru.simplgroupp.contact.protocol.v2.doc.BaseDoc;
import ru.simplgroupp.contact.protocol.v2.doc.DTUDoc;
import ru.simplgroupp.contact.protocol.v2.elements.BaseElement;
import ru.simplgroupp.contact.util.AUtils;
import ru.simplgroupp.contact.util.ZlibUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by aro on 11.09.2014.
 */
public class BaseAppServAssembler implements IObject,IAppServAssembler {
    private static final Logger logger = Logger.getLogger(BaseAppServAssembler.class.getName());
    //no need to create a signature for this class
    //Немного теории.
    // AppServ - это запрос в виде простой xml структуры. Сама суть нашего запроса к контакту. Не зашифрован, не сжат,не кодирован.
    //DTU- документ транспортного уровня - это уже то как AppServ будет передан Контакту.AppServ тут сжат,иногда зашифрован,подписан ЭЦП
    //Тут же передается сама подпись,методы сжатия,шифрования
    //Потом только этот DTU передается в качестве параметра inXML в soap запрос к серверу Контакта
    //Для каждого веб-сервиса(метода) класса TAbonentObject нужно 2 метода: 1-просто создает AppServ; 2-создает DTU с этим AppServ внутри
    //Например веб-сервис  GET_CHANGES - получить справочники, для полного получения есть методы:
    // createGetChangesFullAppServ и createDTUWithoutSignature

    //Abonent object methods
    public static final String TABONENT_OBJECT_CLASS = "TAbonentObject";
    public static final String GET_CHANGES_METHOD = "GET_CHANGES";
    public static final String PING_METHOD = "Ping";



    //Money object methods
    public static final String TMONEY_ORDER_OBJECT_CLASS = "TMoneyOrderObject";
    public static final String NEW_AND_SEND_OUTGOING_METHOD = "NewAndSendOutgoing";
    public static final String GET_STATE_OUTGOING_METHOD = "GetState";
    public static final String REQ_FOR_CANCEL_METHOD = "ReqForCancel";
    public static final String RETURN_OUTGOING_METHOD = "ReturnOutgoing";
    public static final String GET_METHOD = "Get";

    //Clearing object methods
    public static final String TCLEARING_OBJECT_CLASS = "TClearingObject";
    public static final String CHECK_VALID_REST_CORR_METHOD = "CheckValidRestCorr";


    public String createDTUWithoutSignature(byte[] appServInBytes,String ppCode){
        //1.подписывать не надо
        //2.сжать запрос ZLIB
        byte[] packedAppServ = null;
        try {
            packedAppServ = ZlibUtils.compress(appServInBytes);
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe("Ошибка сжатия AppServ");
            return null;
        }
        Map<String,String> dataMap = new HashMap<String, String>();
        dataMap.put(BaseDoc.PACK,BaseDoc.PACK_VALUE);
        dataMap.put(BaseDoc.ENCRYPTION,BaseDoc.PLAIN_ENCRYPTION_VALUE);

        byte[] inBytes = Base64.encodeBase64(packedAppServ);
        String inString = null;
        try {
            inString = new String(inBytes,BaseDoc.ENCODING_VALUE_WIN1251);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //inString = new String(inBytes);

        BaseElement baseElement = new BaseElement(BaseDoc.DATA,inString,dataMap);
        //BaseElement baseElement = new BaseElement(BaseDoc.DATA, new String(packedAppServ),dataMap);


        //create DTU doc
        DTUDoc dtuDoc = new DTUDoc();
        dtuDoc.init(BaseDoc.DTU_ROOTNAME,null,null,null,baseElement,ppCode);
        return dtuDoc.generateString();
    }

    public String createDTUWithSignature(byte[] appServInBytes,byte[] sig,String ppCode){
        //1.подпись
        BaseElement signature = AUtils.createSignatureElement(sig);
        List<BaseElement> signatures = null;
        if(signature!=null){
            signatures = new ArrayList<BaseElement>();
            signatures.add(signature);
        }

        //2.сжать запрос ZLIB
        byte[] packedAppServ = null;
        try {
            packedAppServ = ZlibUtils.compress(appServInBytes);
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe("Ошибка сжатия AppServ");
            return null;
        }
        Map<String,String> dataMap = new HashMap<String, String>();
        dataMap.put(BaseDoc.PACK,BaseDoc.PACK_VALUE);
        dataMap.put(BaseDoc.ENCRYPTION,BaseDoc.PLAIN_ENCRYPTION_VALUE);

        byte[] inBytes = Base64.encodeBase64(packedAppServ);
        String inString = null;
        try {
            inString = new String(inBytes,BaseDoc.ENCODING_VALUE_WIN1251);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BaseElement baseElement = new BaseElement(BaseDoc.DATA,inString,dataMap);

        //create DTU doc
        DTUDoc dtuDoc = new DTUDoc();
        dtuDoc.init(BaseDoc.DTU_ROOTNAME,null,null,signatures,baseElement,ppCode);
        return dtuDoc.generateString();
    }


    @Override
    public byte[] createAppServ(Map<String, String> data,boolean sign,Map<String, String> childrenData) {
        data.put(BaseDoc.INT_SOFT_ID,BaseDoc.INT_SOFT_ID_VALUE);
        if(!sign){
            data.put(BaseDoc.EXPECTSIGNED,BaseDoc.NO_EXPECTSIGNED_VALUE);
        }
        return null;
    }

    @Override
    public String getMethodName() {
        return null;
    }
}
