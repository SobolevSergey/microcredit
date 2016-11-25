package com.experian.rfps.rules;

import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.IRuleNormalization;
import com.experian.rfps.rules.Replace;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class DeleteWord extends AbstractRule implements IRuleNormalization {
   private static final Logger log = Logger.getLogger(DeleteWord.class);
   private Map<String, Replace> replaces = new TreeMap();
   private String regex;
   private String replacement = "";

   public boolean prepare(Map parameters) {
      HashMap params = new HashMap();
      params.put("REPLACEMENT", this.replacement);
      Set set = parameters.keySet();
      Iterator iter = set.iterator();

      while(iter.hasNext()) {
         String key = (String)iter.next();
         String value = (String)parameters.get(key);
         params.put("REGEX", value);
         Replace replace = new Replace();
         if(!replace.prepare(params)) {
            return this.isPrepared = false;
         }

         this.replaces.put(key, replace);
      }

      return this.isPrepared = true;
   }

   public Object transform(Object inputObject, Map record) {
      if(this.isPrepared) {
         Set set = this.replaces.keySet();

         Replace replace;
         for(Iterator iter = set.iterator(); iter.hasNext(); inputObject = replace.transform(inputObject, (Map)null)) {
            replace = (Replace)this.replaces.get((String)iter.next());
         }
      }

      return inputObject;
   }
}
