package com.experian.rfps;

import com.experian.rfps.Rule;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Domain {
   private Map<Integer, Rule> stepList = new TreeMap();
   private String name;

   public Domain(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void addRule(int order, Rule rule) throws Exception {
      Integer ordList = new Integer(order);
      if(this.stepList.containsKey(ordList)) {
         throw new Exception("Order number " + order + " duplicated for rule " + rule.getName() + " of domain " + this.name + ".");
      } else {
         this.stepList.put(ordList, rule);
      }
   }

   public Object makeWork(Object InputObject) throws Exception {
      Object result = null;
      Object value = InputObject;
      Iterator iter = this.stepList.keySet().iterator();

      while(iter.hasNext()) {
         Integer order = (Integer)iter.next();
         Rule rule = (Rule)this.stepList.get(order);
         result = rule.invoke(value);
         if(rule.getType() == 2) {
            if(!((Boolean)result).booleanValue()) {
               throw new Exception(this.ruleFailString(rule));
            }
         } else {
            value = result;
         }
      }

      return result;
   }

   private String ruleFailString(Rule rule) {
      return "Validation of rule " + rule.getName() + " is false" + (rule.getDescription() == ""?"":" (" + rule.getDescription() + ")") + ".";
   }

   public Object makeWork(Object InputObject, int rules) throws Exception {
      Object result = null;
      Object value = InputObject;
      Iterator iter = this.stepList.keySet().iterator();

      while(iter.hasNext()) {
         Integer order = (Integer)iter.next();
         Rule rule = (Rule)this.stepList.get(order);
         if((rule.getType() & rules) != 0) {
            Object operValue = rule.invoke(value);
            if(rule.getType() == 2) {
               if(!((Boolean)operValue).booleanValue()) {
                  throw new Exception(this.ruleFailString(rule));
               }
            } else {
               value = operValue;
               result = operValue;
            }
         }
      }

      return result;
   }
}
