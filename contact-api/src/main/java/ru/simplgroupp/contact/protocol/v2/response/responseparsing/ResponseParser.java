package ru.simplgroupp.contact.protocol.v2.response.responseparsing;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import ru.simplgroupp.contact.protocol.v2.response.CdtrnCXR;
import ru.simplgroupp.contact.protocol.v2.response.unpacked.BaseResponse;
import ru.simplgroupp.contact.protocol.v2.response.unpacked.ResponseUnp;
import ru.simplgroupp.contact.util.AUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by aro on 15.12.2014.
 */
public class ResponseParser{
    private static final Logger logger = Logger.getLogger(ResponseParser.class.getName());

    protected BaseResponse parseXml(CdtrnCXR cdtrn,Class theClass){
        BaseResponse response = null;
        if(cdtrn!=null && cdtrn.getData() !=null && cdtrn.getData().getResponseAsXml()!=null){
            try {
                org.w3c.dom.Node responseNode = extractResponse(cdtrn);
                JAXBContext jaxbContext = JAXBContext.newInstance(theClass);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                response = (BaseResponse) jaxbUnmarshaller.unmarshal(responseNode);

                responseNode.getAttributes().getNamedItem("RE").getNodeValue();
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }else{
            logger.severe("Пустой xml,ошибка!");
        }
        return response;
    }

    protected org.w3c.dom.Node extractResponse(CdtrnCXR cdtrn){
        org.w3c.dom.Node responseNode =null;
        if(cdtrn!=null && cdtrn.getData() !=null && cdtrn.getData().getResponseAsXml()!=null){
            String xml = cdtrn.getData().getResponseAsXml();
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputStream inputSource = new ByteArrayInputStream(xml.getBytes(AUtils.CHARSET_1251));
                org.w3c.dom.Document d = db.parse(inputSource);

                responseNode = d.getElementsByTagName("RESPONSE").item(0);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                logger.log( Level.SEVERE, "Не парсится респонс", e);
            } catch (SAXException e) {
                e.printStackTrace();
                logger.log( Level.SEVERE, "Не парсится респонс", e);
            } catch (IOException e) {
                e.printStackTrace();
                logger.log(Level.SEVERE, "Не парсится респонс", e);
            }
        }else{
            logger.severe("Пустой xml,ошибка!");
        }
        return responseNode;
    }

    protected void extractAttrs(org.w3c.dom.Node responseNode, BaseResponse response){
        NamedNodeMap atrs = responseNode.getAttributes();
        if(atrs != null){
            Node atr = atrs.getNamedItem("ID");
            if(atr!=null) response.setID(atr.getNodeValue());
            atr = atrs.getNamedItem("ERR_TEXT");
            if(atr!=null) response.setERR_TEXT(atr.getNodeValue());
            atr = atrs.getNamedItem("FULL");
            if(atr!=null) response.setFULL(atr.getNodeValue());
            atr = atrs.getNamedItem("GLOBAL_VERSION");
            if(atr!=null) response.setGLOBAL_VERSION(atr.getNodeValue());
            atr = atrs.getNamedItem("GLOBAL_VERSION_SERVER");
            if(atr!=null) response.setGLOBAL_VERSION_SERVER(atr.getNodeValue());
            atr = atrs.getNamedItem("RE");
            if(atr!=null) response.setRE(atr.getNodeValue());
            atr = atrs.getNamedItem("SIGN_IT");
            if(atr!=null) response.setSIGN_IT(atr.getNodeValue());
            atr = atrs.getNamedItem("VERSION");
            if(atr!=null) response.setVERSION(atr.getNodeValue());
            atr = atrs.getNamedItem("STATE");
            if(atr!=null) response.setSTATE(atr.getNodeValue());
        }

    }

    public BaseResponse parseResponse(CdtrnCXR cdtrn){
        BaseResponse response = this.parseXml(cdtrn,this.getResponseClass());
        Node node = this.extractResponse(cdtrn);
        this.extractAttrs(node,response);
        return response;
    }

    protected Class getResponseClass(){
        return ResponseUnp.class;
    }
}
