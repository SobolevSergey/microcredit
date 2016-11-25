package com.experian.rfps.config;

import com.altova.types.SchemaString;
import com.altova.xml.XmlException;

public class STATUSType extends SchemaString {
   public static final int ESTART = 0;
   public static final int ESTOP = 1;
   public static final int EWORKING = 2;
   public static String[] sEnumValues = new String[]{"START", "STOP", "WORKING"};

   public STATUSType() {
   }

   public STATUSType(String newValue) {
      super(newValue);
      this.validate();
   }

   public STATUSType(SchemaString newValue) {
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
         throw new XmlException("Value of STATUS is invalid.");
      }
   }
}
