package com.experian.rfps.config;

import com.altova.types.SchemaString;
import com.altova.xml.XmlException;

public class CREATIONType extends SchemaString {
   public static final int EDAY = 0;
   public static final int EMONTH = 1;
   public static final int EQUARTER = 2;
   public static final int EYEAR = 3;
   public static String[] sEnumValues = new String[]{"DAY", "MONTH", "QUARTER", "YEAR"};

   public CREATIONType() {
   }

   public CREATIONType(String newValue) {
      super(newValue);
      this.validate();
   }

   public CREATIONType(SchemaString newValue) {
      super(newValue);
      this.validate();
   }

   public static int getEnumerationCount() {
      return sEnumValues.length;
   }

   public static String getEnumerationValue(int index) {
      return sEnumValues[index];
   }

   public static boolean isValidEnumerationValue(String val) {
      for(int i = 0; i < sEnumValues.length; ++i) {
         if(val.equals(sEnumValues[i])) {
            return true;
         }
      }

      return false;
   }

   public void validate() {
      if(!isValidEnumerationValue(this.toString())) {
         throw new XmlException("Value of CREATION is invalid.");
      }
   }
}
