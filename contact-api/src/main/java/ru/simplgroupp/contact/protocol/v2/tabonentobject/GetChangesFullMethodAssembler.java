package ru.simplgroupp.contact.protocol.v2.tabonentobject;

import ru.simplgroupp.contact.protocol.v2.BaseAppServAssembler;
import ru.simplgroupp.contact.protocol.v2.IAppServAssembler;
import ru.simplgroupp.contact.protocol.v2.doc.BaseDoc;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by aro on 11.09.2014.
 */
public class GetChangesFullMethodAssembler extends BaseAppServAssembler implements IAppServAssembler {
    private static final Logger logger = Logger.getLogger(GetChangesFullMethodAssembler.class.getName());

    @Override
    public byte[] createAppServ(Map<String, String> data,boolean sign,Map<String, String> childrenData) {
        Map<String,String> attrMap = new HashMap<String, String>();
        super.createAppServ(attrMap,sign,childrenData);
        attrMap.put(BaseDoc.OBJ_CLASS,TABONENT_OBJECT_CLASS);
        attrMap.put(BaseDoc.ACTION, getMethodName());
        attrMap.put(BaseDoc.POINT_CODE, data.get(BaseDoc.POINT_CODE));
        attrMap.put(BaseDoc.VERSION_UPPER,data.get(BaseDoc.VERSION));
        attrMap.put(BaseDoc.TYPE_VERSION,BaseDoc.TYPE_VERSION_VALUE);
        attrMap.put(BaseDoc.PORTION,"0");
        attrMap.put(BaseDoc.PACK_UPPER,BaseDoc.PACK_VALUE);
        //attrMap.put(BaseDoc.BOOK_ID,"0");

        BaseDoc baseDoc = new BaseDoc();
        baseDoc.init(BaseDoc.APP_SERV_ROOTNAME, attrMap, childrenData);
        return baseDoc.generateBytes();
    }

    @Override
    public String getMethodName() {
        return BaseAppServAssembler.GET_CHANGES_METHOD;
    }

}


