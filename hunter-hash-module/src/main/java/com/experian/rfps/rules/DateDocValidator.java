package com.experian.rfps.rules;

import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.IRuleValidation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;

public class DateDocValidator extends AbstractRule implements IRuleValidation {
   private static final Logger log = Logger.getLogger(DateDocValidator.class);
   public static final String PARAM_NODE_DOB = "DOB";
   public static final String PARAM_NODE_YEARS = "YEARS";
   private String xpathDOB = null;
   private int countYears = 0;
   private XPathFactory xpathFactory = XPathFactory.newInstance();
   private XPath xpath;
   private XPathExpression xpathExp;
   private SimpleDateFormat dmf;

   public DateDocValidator() {
      this.xpath = this.xpathFactory.newXPath();
      this.dmf = new SimpleDateFormat("yyyy-MM-dd");
   }

   public boolean prepare(Map parameters) {
      this.xpathDOB = (String)parameters.get("DOB");
      if(this.xpathDOB == null) {
         log.error("The parameter \'DOB\' for a rule " + this.getClass() + "  isn\'t specified");
      } else {
         try {
            this.xpath.setNamespaceContext(this.app.getNameSpaceContext());
            this.xpathExp = this.xpath.compile(this.xpathDOB);
         } catch (XPathExpressionException var5) {
            log.error(var5.getMessage(), var5);
            return false;
         }
      }

      String s = (String)parameters.get("YEARS");

      try {
         this.countYears = Integer.parseInt(s);
      } catch (NumberFormatException var4) {
         log.error("The parameter \'YEARS\' of rule " + this.getClass() + " should be numerical value." + var4.getMessage());
         return false;
      }

      return this.isPrepared = true;
   }

   public boolean validation(Object inputObject, Map record) {
      boolean result = false;

      try {
         String ex = (String)this.xpathExp.evaluate(this.app.currentNode, XPathConstants.STRING);
         if(ex == null || ex.length() == 0) {
            log.warn("It is not found node \'" + this.xpathDOB + "\' for rule " + this.getClass() + " of node " + this.app.currentNode.getNodeName() + " with value \'" + inputObject + "\'");
            ex = this.xpath.evaluate("../../a:DOB", this.app.currentNode);
            log.info(this.app.currentNode.getNodeName() + "=" + ex);
            return result;
         }

         Date dob = this.dmf.parse(ex);
         Date dateDoc = this.dmf.parse((String)inputObject);
         Calendar cal = Calendar.getInstance();
         cal.setTime(dob);
         cal.add(1, this.countYears);
         dob = cal.getTime();
         if(dob.after(dateDoc)) {
            log.error("Document date " + (String)inputObject + " should be more dates of birth " + ex + " plus " + this.countYears + " years.");
            return false;
         }

         result = true;
      } catch (ParseException var8) {
         log.error(var8.getMessage());
      } catch (XPathExpressionException var9) {
         log.error(var9.getMessage());
      }

      return result;
   }
}
