package ru.simplgroupp.contact.protocol.v2;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by aro on 11.09.2014.
 * пробный класс, удалю потом
 */
public class CommonRequest {
    protected static DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();

    private static final Logger logger = Logger.getLogger(CommonRequest.class.getName());

    protected DocumentBuilder icBuilder;

    private String obj_class;
    private String point_code;
    private String user_id;
    private String int_soft_id;
    private String server_ip;
    private String client_ip;
    private String lang;
    private String action;

    public String getObj_class() {
        return obj_class;
    }

    public void setObj_class(String obj_class) {
        this.obj_class = obj_class;
    }

    public String getPoint_code() {
        return point_code;
    }

    public void setPoint_code(String point_code) {
        this.point_code = point_code;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getInt_soft_id() {
        return int_soft_id;
    }

    public void setInt_soft_id(String int_soft_id) {
        this.int_soft_id = int_soft_id;
    }

    public String getServer_ip() {
        return server_ip;
    }

    public void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Document toXml(){
        if(icBuilder==null){
            try {
                icBuilder = icFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                logger.severe("Ошибка создания билдера xml документов для запроса на AppServ.");
                return null;
            }
        }
        try {
            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElement("REQUEST");
            doc.appendChild(mainRootElement);

            // append attributes to root element
            setAttr(mainRootElement, "obj_class",obj_class);
            setAttr(mainRootElement, "point_code",point_code);
            setAttr(mainRootElement, "user_id",user_id);
            setAttr(mainRootElement, "int_soft_id",int_soft_id);
            setAttr(mainRootElement, "server_ip",server_ip);
            setAttr(mainRootElement, "client_ip",client_ip);
            setAttr(mainRootElement, "lang",lang);
            setAttr(mainRootElement, "action", action);
            return doc;
        } catch (DOMException e) {
            e.printStackTrace();
            logger.severe("Ошибка создания общего xml документа для запроса на AppServ.");
            return null;
        }
    }

    protected void setAttr(Element element,String name,String value){
        if(element!=null && StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(value)){
            element.setAttribute(name,value);
        }
    }

    protected void init(Map<String,String> data){
        this.setObj_class(data.get("obj_class"));
        this.setPoint_code(data.get("point_code"));
        this.setUser_id(data.get("user_id"));
        this.setInt_soft_id(data.get("int_soft_id"));

        this.setAction(data.get("action"));
        this.setClient_ip(data.get("action"));
        this.setAction(data.get("action"));
        this.setAction(data.get("action"));
    }

}
