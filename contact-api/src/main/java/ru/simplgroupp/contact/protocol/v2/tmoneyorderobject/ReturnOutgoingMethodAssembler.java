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
public class ReturnOutgoingMethodAssembler extends BaseAppServAssembler implements IAppServAssembler {
    private static final Logger logger = Logger.getLogger(ReturnOutgoingMethodAssembler.class.getName());

    @Override
    public byte[] createAppServ(Map<String, String> attrData,boolean sign,Map<String, String> childrenData) {
        Map<String,String> attrMap = new HashMap<String, String>();
        super.createAppServ(attrMap,sign,childrenData);
        attrMap.put(BaseDoc.OBJ_CLASS, TMONEY_ORDER_OBJECT_CLASS);
        attrMap.put(BaseDoc.ACTION, getMethodName());
        attrMap.put(BaseDoc.DOC_ID, attrData.get(BaseDoc.DOC_ID));
        attrMap.put(BaseDoc.POINT_CODE, attrData.get(BaseDoc.POINT_CODE));
        attrMap.put(BaseDoc.USER_ID,"1");
        attrMap.put(BaseDoc.LANG,BaseDoc.LANG_VALUE);

        BaseDoc baseDoc = new BaseDoc();
        baseDoc.init(BaseDoc.APP_SERV_ROOTNAME, attrMap, null);
        return baseDoc.generateBytes();
    }

    @Override
    public String getMethodName() {
        return BaseAppServAssembler.RETURN_OUTGOING_METHOD;
    }

}


