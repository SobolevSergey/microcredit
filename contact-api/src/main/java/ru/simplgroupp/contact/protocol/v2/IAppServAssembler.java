package ru.simplgroupp.contact.protocol.v2;

import java.util.Map;

/**
 * Created by aro on 16.09.2014.
 */
public interface IAppServAssembler {
    byte[] createAppServ(Map<String,String> data,boolean sign,Map<String, String> childrenData);
    String getMethodName();
    String createDTUWithSignature(byte[] appServInBytes,byte[] sig,String ppCode);
    String createDTUWithoutSignature(byte[] appServInBytes,String ppCode);
}
