package com.experian.rfps.rules;

import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.CharValidator;
import com.experian.rfps.rules.IRuleNormalization;
import com.experian.rfps.rules.VerifyDigit;
import java.util.Map;

public class PersonalTaxIDValidator extends AbstractRule implements IRuleNormalization {
   CharValidator formatValidator = new CharValidator();

   public boolean prepare(Map parameters) {
      parameters.put(CharValidator.PARAM_MASK, "\\d{12,12}");
      this.isPrepared = this.formatValidator.prepare(parameters);
      return this.isPrepared;
   }

   public boolean validation(Object inputObject, Map record) {
      boolean result = false;
      if(this.isPrepared && this.formatValidator.validation(inputObject, record)) {
         result = VerifyDigit.checkTax((String)inputObject, 11) && VerifyDigit.checkTax((String)inputObject, 12);
      }

      return result;
   }

   public Object transform(Object inputObject, Map record) {
      return !this.validation(inputObject, record)?"DUMMY":((String)inputObject).replaceAll("^0+$", "DUMMY");
   }
}
