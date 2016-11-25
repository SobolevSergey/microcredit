package ru.simplgroupp.contact.protocol.v2.tclearingobject;

import ru.simplgroupp.contact.protocol.v2.BaseAppServAssembler;
import ru.simplgroupp.contact.protocol.v2.IAppServAssembler;
import ru.simplgroupp.contact.protocol.v2.doc.BaseDoc;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by aro on 09.12.2014.
 */
public class CheckValidRestCorrMethodAssembler extends BaseAppServAssembler implements IAppServAssembler {
    private static final Logger logger = Logger.getLogger(CheckValidRestCorrMethodAssembler.class.getName());

    //<REQUEST OBJECT_CLASS="TClearingObject" ACTION="CheckValidRestCorr" DEBET="30302810000000000316" CURR_ID="0" POINT_CODE="TSTF"/>

    @Override
    public byte[] createAppServ(Map<String, String> attrData,boolean sign,Map<String, String> childrenData) {
        Map<String,String> attrMap = new HashMap<String, String>();
        super.createAppServ(attrMap,sign,childrenData);
        attrMap.put(BaseDoc.OBJ_CLASS, TCLEARING_OBJECT_CLASS);
        attrMap.put(BaseDoc.ACTION, getMethodName());
        attrMap.put(BaseDoc.POINT_CODE, attrData.get(BaseDoc.POINT_CODE));
        attrMap.put(BaseDoc.CURR_ID,BaseDoc.CURR_ID_VALUE);
        attrMap.put(BaseDoc.DEBET,attrData.get(BaseDoc.DEBET));


        BaseDoc baseDoc = new BaseDoc();
        baseDoc.init(BaseDoc.APP_SERV_ROOTNAME, attrMap, null);
        return baseDoc.generateBytes();
    }

    @Override
    public String getMethodName() {
        return BaseAppServAssembler.CHECK_VALID_REST_CORR_METHOD;
    }

}
