package ru.simplgroupp.contact.util;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by aro on 11.09.2014.
 */
public class DocUtils {
    private static final Logger logger = Logger.getLogger(DocUtils.class.getName());
    
    public static void addAttrs(Element element,Map<String,String> attrMap){
        // append attributes to root element
        if(attrMap!=null){
            for(String key :attrMap.keySet()){
                setAttr(element, key, attrMap.get(key));
            }
        }
    }

    public static void setAttr(Element element,String name,String value){
        if(element!=null && StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(value)){
            element.setAttribute(name,value);
        }
    }

    public static void addSimpleChildren(Element element, Map<String,String> childrenMap,Document doc){
        // append inner elements to root element
        if(childrenMap!=null){
            for(String key :childrenMap.keySet()){
                if(childrenMap.get(key)!=null){
                    Element el = doc.createElement(key);
                    el.appendChild(doc.createTextNode(childrenMap.get(key)));
                    element.appendChild(el);
                }
            }
        }
    }

}
