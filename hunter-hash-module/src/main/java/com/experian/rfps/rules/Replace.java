package com.experian.rfps.rules;

import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.IRuleNormalization;
import java.util.Map;
import org.apache.log4j.Logger;

public class Replace extends AbstractRule implements IRuleNormalization {
   public static final String PARAM_REGEX = "REGEX";
   public static final String PARAM_REPLACEMENT = "REPLACEMENT";
   private String regex;
   private String replacement;
   private static final Logger log = Logger.getLogger(Replace.class);

   public Object transform(Object inputObject, Map record) {
      return this.isPrepared?((String)inputObject).replaceAll(this.regex, this.replacement):inputObject;
   }

   public boolean prepare(Map parameters) {
      this.regex = (String)parameters.get("REGEX");
      this.replacement = (String)parameters.get("REPLACEMENT");
      if(this.regex != null && this.replacement != null) {
         this.isPrepared = true;
      }

      return this.isPrepared;
   }
}
