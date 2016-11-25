package ru.simplgroupp.contact.protocol.v2.tmoneyorderobject;

import ru.simplgroupp.contact.protocol.v2.BaseAppServAssembler;
import ru.simplgroupp.contact.protocol.v2.IAppServAssembler;
import ru.simplgroupp.contact.protocol.v2.doc.BaseDoc;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by aro on 11.09.2014.
 */
public class NewAndSendOutgoingMethodAssembler extends BaseAppServAssembler implements IAppServAssembler {
    private static final Logger logger = Logger.getLogger(NewAndSendOutgoingMethodAssembler.class.getName());

    @Override
    public byte[] createAppServ(Map<String, String> attrData,boolean sign,Map<String, String> childrenData) {
        Map<String,String> attrMap = new HashMap<String, String>();
        super.createAppServ(attrMap,sign,childrenData);
        attrMap.put(BaseDoc.OBJ_CLASS, TMONEY_ORDER_OBJECT_CLASS);
        attrMap.put(BaseDoc.ACTION, getMethodName());
        attrMap.put(BaseDoc.POINT_CODE, attrData.get(BaseDoc.POINT_CODE));
        attrMap.put(BaseDoc.USER_ID,"1");
        attrMap.put(BaseDoc.LANG,BaseDoc.LANG_VALUE);

        Map<String,String> childrenMap = new HashMap<String, String>();
        attrMap.put(BaseDoc.POINT_CODE, attrData.get(BaseDoc.POINT_CODE));



        BaseDoc baseDoc = new BaseDoc();
        baseDoc.init(BaseDoc.APP_SERV_ROOTNAME, attrMap, childrenData);
        return baseDoc.generateBytes();
    }

    @Override
    public String getMethodName() {
        return BaseAppServAssembler.NEW_AND_SEND_OUTGOING_METHOD;
    }

}


