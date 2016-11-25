package com.experian.rfps.config;

import com.altova.types.SchemaInteger;
import com.altova.xml.XmlException;

public class MAXROWSType extends SchemaInteger {
   public MAXROWSType() {
   }

   public MAXROWSType(String newValue) {
      super(newValue);
      this.validate();
   }

   public MAXROWSType(SchemaInteger newValue) {
      super(newValue);
      this.validate();
   }

   public void validate() {
      if(this.compareTo(this.getMaxInclusive()) > 0) {
         throw new XmlException("Value of MAXROWS is out of range.");
      } else if(this.compareTo(this.getMinInclusive()) < 0) {
         throw new XmlException("Value of MAXROWS is out of range.");
      }
   }

   public SchemaInteger getMaxInclusive() {
      return new SchemaInteger("10000");
   }

   public SchemaInteger getMinInclusive() {
      return new SchemaInteger("1");
   }
}
