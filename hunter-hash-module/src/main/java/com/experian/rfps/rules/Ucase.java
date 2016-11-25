package com.experian.rfps.rules;

import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.IRuleNormalization;
import java.util.Map;

public class Ucase extends AbstractRule implements IRuleNormalization {
   public Boolean checkParameters(Map parameters) {
      return Boolean.TRUE;
   }

   public Object transform(Object inputObject, Map record) {
      return ((String)inputObject).toUpperCase();
   }

   public boolean prepare(Map parameters) {
      this.isPrepared = true;
      return true;
   }
}
