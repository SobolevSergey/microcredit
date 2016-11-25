package com.experian.rfps.rules;

import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.CharValidator;
import com.experian.rfps.rules.DateValidator;
import com.experian.rfps.rules.IRuleValidation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocValidation extends AbstractRule implements IRuleValidation {
   public static final String PARAM_FIELDTYPEDOC = "NAMEFIELDTYPEDOC";
   Map mapRulePassport = new HashMap();
   Map mapParameters = new HashMap();
   String fieldTypeDoc = null;
   DateValidator dataValidator = new DateValidator();

   public boolean prepare(Map parameters) {
      Set set = parameters.keySet();
      Iterator iter = set.iterator();

      while(iter.hasNext()) {
         String code = ((String)iter.next()).toUpperCase();
         String value = (String)parameters.get(code);
         if(code.startsWith("CODE")) {
            CharValidator validator = new CharValidator();
            this.mapParameters.put(CharValidator.PARAM_MASK, value);
            if(!validator.prepare(this.mapParameters)) {
               return this.isPrepared;
            }

            this.mapRulePassport.put(code, validator);
         } else if(code.equalsIgnoreCase("NAMEFIELDTYPEDOC")) {
            this.fieldTypeDoc = value;
         }
      }

      this.mapParameters.put("FORMAT", "yyyy.mm.dd");
      if(this.fieldTypeDoc == null) {
         Logger.getLogger(DocValidation.class.getName()).log(Level.SEVERE, (String)null, new Exception("Not found parameter NAMEFIELDTYPEDOC for class " + this.getClass() + "."));
         return this.isPrepared;
      } else {
         return this.isPrepared = this.dataValidator.prepare(parameters);
      }
   }

   public boolean validation(Object inputObject, Map record) {
      if(this.isPrepared) {
         String s = (String)inputObject;
         String dateStr = s.substring(s.length() - 9);
         String docStr = s.substring(0, s.length() - 8);
         if(!this.dataValidator.validation(dateStr, record)) {
            Logger.getLogger(DocValidation.class.getName()).log(Level.SEVERE, (String)null, new Exception("Error validation date " + dateStr + "."));
            return false;
         }

         String codeValue = (String)record.get(this.fieldTypeDoc);
         CharValidator docValidator = (CharValidator)this.mapRulePassport.get(codeValue);
         if(!docValidator.validation(docStr, record)) {
            Logger.getLogger(DocValidation.class.getName()).log(Level.SEVERE, (String)null, new Exception("Error validation document number format " + docStr + "."));
            return false;
         }
      }

      return false;
   }
}
