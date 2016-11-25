package com.experian.rfps.config;

import com.altova.types.SchemaString;
import com.altova.xml.XmlException;

public class Y_N extends SchemaString {
   public static final int EY = 0;
   public static final int EN = 1;
   public static String[] sEnumValues = new String[]{"Y", "N"};

   public Y_N() {
   }

   public Y_N(String newValue) {
      super(newValue);
      this.validate();
   }

   public Y_N(SchemaString newValue) {
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
         throw new XmlException("Value of Y_N is invalid.");
      }
   }
}
