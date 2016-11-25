package com.experian.rfps.transformation;

import com.altova.types.SchemaString;

public class VERSIONType extends SchemaString {
   public static String[] sPatternValues = new String[]{"\\d\\.\\d\\.\\d\\d\\d"};

   public VERSIONType() {
   }

   public VERSIONType(String newValue) {
      super(newValue);
      this.validate();
   }

   public VERSIONType(SchemaString newValue) {
      super(newValue);
      this.validate();
   }

   public static int getPatternCount() {
      return sPatternValues.length;
   }

   public static String getPatternValue(int index) {
      return sPatternValues[index];
   }

   public void validate() {
   }
}
