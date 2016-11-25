package com.altova.types;

import com.altova.types.SchemaBinaryBase;
import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeBinary;
import java.io.IOException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SchemaBase64Binary extends SchemaBinaryBase {
   public SchemaBase64Binary() {
   }

   public SchemaBase64Binary(SchemaBase64Binary newvalue) {
      this.value = newvalue.value;
      this.isempty = newvalue.isempty;
      this.isnull = newvalue.isnull;
   }

   public SchemaBase64Binary(byte[] newvalue) {
      this.setValue(newvalue);
   }

   public SchemaBase64Binary(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaBase64Binary(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaBase64Binary(SchemaTypeBinary newvalue) {
      this.assign(newvalue);
   }

   public void parse(String newvalue) {
      if(newvalue == null) {
         this.setNull();
      } else if(newvalue.length() == 0) {
         this.setEmpty();
      } else {
         this.setNull();

         try {
            this.value = (new BASE64Decoder()).decodeBuffer(newvalue);
            this.isnull = false;
            this.isempty = this.value.length == 0;
         } catch (IOException var3) {
            this.value = null;
         }
      }

   }

   public int hashCode() {
      return this.value.hashCode();
   }

   public boolean equals(Object obj) {
      return !(obj instanceof SchemaBase64Binary)?false:this.value.equals(((SchemaBase64Binary)obj).value);
   }

   public Object clone() {
      return new SchemaBase64Binary(this);
   }

   public String toString() {
      if(!this.isempty && !this.isnull && this.value != null) {
         String sResult = (new BASE64Encoder()).encode(this.value);
         return sResult.replaceAll("\r", "");
      } else {
         return "";
      }
   }

   public int binaryType() {
      return 0;
   }
}
