package com.experian.rfps.config;

import com.altova.types.SchemaString;
import com.altova.xml.XmlException;

public class MODEType extends SchemaString {
   public static final int ESERVICE = 0;
   public static final int ECALLED = 1;
   public static String[] sEnumValues = new String[]{"SERVICE", "CALLED"};

   public MODEType() {
   }

   public MODEType(String newValue) {
      super(newValue);
      this.validate();
   }

   public MODEType(SchemaString newValue) {
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
         throw new XmlException("Value of MODE is invalid.");
      }
   }
}
