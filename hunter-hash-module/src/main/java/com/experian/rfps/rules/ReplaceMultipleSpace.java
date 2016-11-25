package com.experian.rfps.rules;

import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.IRuleNormalization;
import java.util.Map;
import org.apache.log4j.Logger;

public class ReplaceMultipleSpace extends AbstractRule implements IRuleNormalization {
   public static final String PARAM_REGEX = "REGEX";
   public static final String PARAM_REPLACEMENT = "REPLACEMENT";
   private String regex;
   private String replacement;
   private static final Logger log = Logger.getLogger(ReplaceMultipleSpace.class);

   public Object transform(Object inputObject, Map record) {
      return this.isPrepared?((String)inputObject).replaceAll("\\s+", " "):inputObject;
   }

   public boolean prepare(Map parameters) {
      this.isPrepared = true;
      return true;
   }
}
