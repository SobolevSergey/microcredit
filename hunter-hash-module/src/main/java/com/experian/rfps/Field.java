package com.experian.rfps;

import com.experian.rfps.Domain;

public class Field {
   private String name;
   private Domain type;
   private int order;
   private String value;

   public Field(String name, Domain type, int order) {
      this.name = name;
      this.type = type;
      this.order = order;
      this.value = null;
   }

   public Field(String name, int order, String value) {
      this.name = name;
      this.type = null;
      this.order = order;
      this.value = value;
   }

   public String toString() {
      return this.getClass() + "(name=" + this.name + " type=" + this.type.getName() + " order=" + this.order + " value" + this.value;
   }

   public String getName() {
      return this.name;
   }

   public int getOrder() {
      return this.order;
   }

   public Domain getType() {
      return this.type;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }
}
