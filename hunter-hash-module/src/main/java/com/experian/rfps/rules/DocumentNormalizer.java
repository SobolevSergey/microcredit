package com.experian.rfps.rules;

import com.experian.rfps.Rule;
import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.IRuleNormalization;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;

public class DocumentNormalizer extends AbstractRule implements IRuleNormalization {
   private static final Logger log = Logger.getLogger(DocumentNormalizer.class);
   public static final String PARAM_TYPEDOC = "TYPEDOC";
   Map mapRulePassport = new HashMap();
   String fieldTypeDoc = null;
   Pattern p = Pattern.compile("CODE\\d+");
   private XPathFactory xpathFactory = XPathFactory.newInstance();
   private XPath xpath;
   private XPathExpression xpathExp;

   public DocumentNormalizer() {
      this.xpath = this.xpathFactory.newXPath();
   }

   public boolean prepare(Map parameters) {
      Set set = parameters.keySet();
      new HashMap();
      this.isPrepared = false;
      Iterator msg = set.iterator();

      while(msg.hasNext()) {
         String code = ((String)msg.next()).toUpperCase();
         String value = (String)parameters.get(code);
         if(code.startsWith("CODE")) {
            if(!this.p.matcher(code).matches()) {
               log.error("Incorrect writing the parameter name:" + code + ". A correct format: CODEdd");
               return this.isPrepared;
            }

            Rule ex = this.app.getRule(value);
            String msg1;
            if(ex == null) {
               msg1 = "Not found rule " + value + " of Parameter " + code + " of rule " + this.getClass();
               log.error(msg1);
               return this.isPrepared;
            }

            if(ex.getType() != 1) {
               msg1 = "The rule \'" + value + "\' of parameter \'" + code + "\' of rule " + this.getClass() + " should be normalization.";
               log.error(msg1);
               return this.isPrepared;
            }

            this.mapRulePassport.put(code, ex);
         } else if(code.equalsIgnoreCase("TYPEDOC")) {
            try {
               this.fieldTypeDoc = value;
               this.xpath.setNamespaceContext(this.app.getNameSpaceContext());
               this.xpathExp = this.xpath.compile(this.fieldTypeDoc);
            } catch (XPathExpressionException var9) {
               log.error(var9.getMessage());
               return this.isPrepared;
            }
         }
      }

      if(this.fieldTypeDoc == null) {
         String msg2 = "Not found parameter TYPEDOC for Rule " + this.getClass() + ".";
         log.error(msg2);
      }

      return this.isPrepared = true;
   }

   public Object transform(Object inputObject, Map record) {
      Object resvalue = inputObject;
      if(this.isPrepared) {
         String codeType = null;

         try {
            codeType = (String)this.xpathExp.evaluate(this.app.currentNode, XPathConstants.STRING);
         } catch (XPathExpressionException var9) {
            log.error(var9.getMessage());
            return inputObject;
         }

         if((codeType == null || codeType.length() == 0) && !"MA_PAS".equals(this.app.currentNode.getParentNode().getNodeName()) && !"A_PAS".equals(this.app.currentNode.getParentNode().getNodeName())) {
            log.warn("Not found node \'" + this.fieldTypeDoc + "\' for rule " + this.getClass() + " of node " + this.app.currentNode.getNodeName() + " with value \'" + inputObject + "\'");
            return inputObject;
         }

         if("MA_PAS".equals(this.app.currentNode.getParentNode().getNodeName()) || "A_PAS".equals(this.app.currentNode.getParentNode().getNodeName())) {
            codeType = "5";
         }

         String codeMaska = "CODE" + codeType;
         Rule rule = (Rule)this.mapRulePassport.get(codeMaska);
         if(rule == null) {
            if(!"CODE99".equals(codeMaska)) {
               log.warn("Not found \'" + codeMaska + "\' for rule " + this.getClass() + " of node " + this.app.currentNode.getNodeName() + " with value \'" + inputObject + "\'");
            }

            return inputObject;
         }

         try {
            resvalue = rule.invoke(inputObject);
         } catch (Exception var8) {
            log.error(var8.getMessage());
         }
      }

      return resvalue;
   }
}
