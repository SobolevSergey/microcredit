package com.experian.rfps.rules;

import com.experian.rfps.Transformation;
import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.Hash;
import com.experian.rfps.rules.IRuleNormalization;
import java.util.Map;
import org.apache.log4j.Logger;

public class HashNormalizator extends AbstractRule implements IRuleNormalization {
   private static final Logger log = Logger.getLogger(HashNormalizator.class);
   private Hash hashObject = new Hash();

   public boolean prepare(Map parameters) {
      return this.isPrepared = this.hashObject.prepare(parameters);
   }

   public Object transform(Object inputObject, Map record) {
      return !Transformation.excludeHash?this.hashObject.encode((String)inputObject, record):inputObject;
   }
}
