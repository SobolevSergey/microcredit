package com.altova.types;

import com.altova.types.SchemaBinaryBase;
import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeBinary;

public class SchemaHexBinary extends SchemaBinaryBase {
   protected final char[] FINAL_ENCODE = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   protected final byte[] FINAL_DECODE = new byte[]{(byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1};

   public SchemaHexBinary() {
   }

   public SchemaHexBinary(SchemaHexBinary newvalue) {
      this.value = newvalue.value;
      this.isempty = newvalue.isempty;
      this.isnull = newvalue.isnull;
   }

   public SchemaHexBinary(byte[] newvalue) {
      this.setValue(newvalue);
   }

   public SchemaHexBinary(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaHexBinary(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaHexBinary(SchemaTypeBinary newvalue) {
      this.assign(newvalue);
   }

   public void parse(String newvalue) {
      if(newvalue == null) {
         this.setNull();
      } else if(newvalue.length() == 0) {
         this.setEmpty();
      } else {
         char[] cSrc = newvalue.toCharArray();
         this.value = new byte[cSrc.length >> 1];
         int nSrcIndex = 0;
         int nTarIndex = 0;

         while(nSrcIndex < cSrc.length) {
            byte c = this.FINAL_DECODE[cSrc[nSrcIndex++]];
            if(c != -1) {
               this.value[nTarIndex >> 1] |= (byte)((nTarIndex & 1) == 1?c:c << 4);
               ++nTarIndex;
            }
         }

         this.isnull = false;
         this.isempty = this.value.length == 0;
      }

   }

   public int hashCode() {
      return this.value.hashCode();
   }

   public boolean equals(Object obj) {
      return !(obj instanceof SchemaHexBinary)?false:this.value == ((SchemaHexBinary)obj).value;
   }

   public Object clone() {
      return new SchemaHexBinary(this);
   }

   public String toString() {
      if(!this.isempty && !this.isnull && this.value != null) {
         char[] cResult = new char[this.value.length << 1];

         for(int i = 0; i < this.value.length; ++i) {
            cResult[i << 1] = this.FINAL_ENCODE[this.value[i] >> 4 & 15];
            cResult[(i << 1) + 1] = this.FINAL_ENCODE[this.value[i] & 15];
         }

         return new String(cResult);
      } else {
         return "";
      }
   }

   public int binaryType() {
      return 1;
   }
}
