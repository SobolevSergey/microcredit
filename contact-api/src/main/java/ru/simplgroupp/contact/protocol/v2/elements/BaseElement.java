package ru.simplgroupp.contact.protocol.v2.elements;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.simplgroupp.contact.util.DocUtils;

import java.util.Map;

/**
 * Created by aro on 12.09.2014.
 */
public class BaseElement {
    private Map<String,String> attrMap;
    private String innerText;
    private String rootName;


    public BaseElement(String rootName,String innerText,Map<String,String> attrMap) {
        this.attrMap = attrMap;
        this.innerText = innerText;
        this.rootName = rootName;
    }

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public Map<String, String> getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map<String, String> attrMap) {
        this.attrMap = attrMap;
    }

    public String getInnerText() {
        return innerText;
    }

    public void setInnerText(String innerText) {
        this.innerText = innerText;
    }

    public Element toElement(Document doc){
        Element root = doc.createElement(rootName);
        if(attrMap!=null){
            for(String key: attrMap.keySet()){
                DocUtils.setAttr(root, key, attrMap.get(key));
            }
        }
        root.appendChild(doc.createTextNode(innerText));
        return root;
    }

}
