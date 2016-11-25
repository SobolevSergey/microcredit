package com.experian.rfps.rules;

import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.IRuleNormalization;
import com.experian.rfps.rules.VerifyDigit;
import java.util.Map;

public class SPINValidator extends AbstractRule implements IRuleNormalization {
   public boolean prepare(Map parameters) {
      return true;
   }

   public boolean validation(Object inputObject, Map record) {
      return VerifyDigit.checkSPIN((String)inputObject);
   }

   public Object transform(Object inputObject, Map record) {
      return !this.validation(inputObject, record)?"DUMMY":inputObject;
   }
}
