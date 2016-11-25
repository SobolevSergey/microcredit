package ru.simplgroupp.contact.protocol.v2.doc;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import ru.simplgroupp.contact.protocol.v2.elements.BaseElement;
import ru.simplgroupp.contact.util.AUtils;
import ru.simplgroupp.contact.util.DocUtils;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by aro on 11.09.2014.
 */
public class DTUDoc extends BaseDoc {
    private static final Logger logger = Logger.getLogger(DTUDoc.class.getName());

    private List<BaseElement> signatures;
    private BaseElement dataElement;
    private String ppCode;


    @Override
    protected void toXml() {
        try {

            //add static attrs
            rootElement.setAttribute(BaseDoc.VERSION,BaseDoc.DTU_VERSION_VALUE);
            rootElement.setAttribute(BaseDoc.CODE,BaseDoc.CODE_VALUE);
            rootElement.setAttribute(BaseDoc.LANG,BaseDoc.LANG_VALUE);
            rootElement.setAttribute(BaseDoc.STAMP, AUtils.createStamp());
            rootElement.setAttribute(BaseDoc.PPCODE, ppCode);
            //rootElement.setAttribute(BaseDoc.CorrespondentID,BaseDoc.CORRESPONDENT_ID_VALUE);


            //add dynamic attrs
            DocUtils.addAttrs(rootElement, attrMap);

            //add signatures
            if(signatures!=null && signatures.size()>0){
                Element sigs = doc.createElement("SIGNATURES");
                for(BaseElement signatureElement : signatures){
                    Element sig = signatureElement.toElement(doc);
                    if(sig!=null){
                        sigs.appendChild(sig);
                    }
                }
                rootElement.appendChild(sigs);
            }
            //add Data element
            if(dataElement!=null){
                rootElement.appendChild(dataElement.toElement(doc));
            }


            doc.appendChild(rootElement);

        } catch (DOMException e) {
            e.printStackTrace();
            logger.severe("Ошибка создания общего xml документа для запроса на AppServ.");
        }

    }

    public void init(String rootName,Map<String, String> attrMAp,
                     Map<String, String> childrenMap,
                     List<BaseElement> signatures,
                     BaseElement dataElement,
                     String ppCode
    ) {
        super.init(rootName,attrMAp, childrenMap);
        this.signatures=signatures;
        this.dataElement=dataElement;
        this.ppCode=ppCode;
    }
}
