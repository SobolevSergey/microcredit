package com.experian.rfps.rules;

import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.IRuleValidation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.log4j.Logger;

public class DateValidator extends AbstractRule implements IRuleValidation {
   private static final Logger log = Logger.getLogger(DateValidator.class);
   public static final String PARAM_FORMAT = "FORMAT";
   SimpleDateFormat fd;

   public boolean prepare(Map parameters) {
      String format = (String)parameters.get("FORMAT");
      if(format != null) {
         try {
            this.fd = new SimpleDateFormat(format);
            this.isPrepared = true;
         } catch (IllegalArgumentException var4) {
            log.error(var4.getMessage());
         }
      }

      return this.isPrepared;
   }

   public boolean validation(Object inputObject, Map record) {
      boolean result = false;
      if(this.isPrepared) {
         try {
            this.fd.parse((String)inputObject);
            result = true;
         } catch (ParseException var5) {
            log.error(var5.getMessage());
         }
      }

      return result;
   }
}
