package ru.simplgroupp.contact.protocol.v2.response.responseparsing;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import ru.simplgroupp.contact.protocol.v2.response.unpacked.BaseResponse;
import ru.simplgroupp.contact.protocol.v2.response.unpacked.CheckRestResponse;

import java.util.logging.Logger;

/**
 * Created by aro on 15.12.2014.
 */
public class CheckRestResponseParser extends ResponseParser {
    private static final Logger logger = Logger.getLogger(CheckRestResponseParser.class.getName());

    //     <RESPONSE SIGN_IT="0" RE="0" AVAIL_REST="1000.00" AVAIL_REST_CURR="1000.00" AVAIL_REST_CURR_CODE="RUR" GLOBAL_VERSION="17.04.2013 15:46:48" GLOBAL_VERSION_SERVER="23.01.2013 16:33:23"/>

    protected void extractAttrs(Node responseNode, BaseResponse response){
        super.extractAttrs(responseNode,response);
        NamedNodeMap atrs = responseNode.getAttributes();
        if(atrs != null){
            CheckRestResponse checkRestResponse = (CheckRestResponse)response;
            Node atr = atrs.getNamedItem("AVAIL_REST");
            if(atr!=null && atr.getNodeValue()!=null) checkRestResponse.setAVAIL_REST(Double.valueOf(atr.getNodeValue()));
            atr = atrs.getNamedItem("AVAIL_REST_CURR");
            if(atr!=null && atr.getNodeValue()!=null) checkRestResponse.setAVAIL_REST_CURR(Double.valueOf(atr.getNodeValue()));
            atr = atrs.getNamedItem("AVAIL_REST_CURR_CODE");
            if(atr!=null && atr.getNodeValue()!=null) checkRestResponse.setAVAIL_REST_CURR_CODE(atr.getNodeValue());
        }

    }

    protected Class getResponseClass(){
        return CheckRestResponse.class;
    }

}
