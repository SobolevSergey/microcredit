package com.experian.rfps.rules;

import com.experian.rfps.rules.AbstractRule;
import com.experian.rfps.rules.IRuleNormalization;
import com.experian.rfps.rules.Replace;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class MultipleReplace extends AbstractRule implements IRuleNormalization {
   private static final Logger log = Logger.getLogger(MultipleReplace.class);
   private Map<String, Replace> replaces = new TreeMap();
   private String regex;
   private String replacement = "";

   public boolean prepare(Map parameters) {
      HashMap params = new HashMap();
      List set = asSortedList(parameters.keySet());
      Collections.sort(set);
      Iterator iter = set.iterator();

      while(iter.hasNext()) {
         String key = (String)iter.next();
         String value = (String)parameters.get(key);
         if(key.indexOf("WORD") != -1) {
            key = (String)iter.next();
            this.replacement = (String)parameters.get(key);
            params.put("REGEX", value);
            params.put("REPLACEMENT", this.replacement);
            Replace replace = new Replace();
            if(!replace.prepare(params)) {
               return this.isPrepared = false;
            }

            this.replaces.put(key, replace);
         }
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

   public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
      ArrayList list = new ArrayList(c);
      Collections.sort(list);
      return list;
   }
}
