package com.altova.types;

import com.altova.types.SchemaString;
import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeBinary;
import com.altova.types.TypesIncompatibleException;

public abstract class SchemaBinaryBase implements SchemaTypeBinary {
   protected byte[] value;
   protected boolean isempty;
   protected boolean isnull;

   public SchemaBinaryBase() {
      this.setEmpty();
   }

   public boolean equals(Object obj) {
      if(!(obj instanceof SchemaBinaryBase)) {
         return false;
      } else {
         SchemaBinaryBase bin = (SchemaBinaryBase)obj;
         if(this.value.length != bin.value.length) {
            return false;
         } else {
            for(int i = 0; i < this.value.length; ++i) {
               if(this.value[i] != bin.value[i]) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public void assign(SchemaType newvalue) {
      if(newvalue != null && !newvalue.isNull()) {
         if(newvalue.isEmpty()) {
            this.setEmpty();
         } else if(newvalue instanceof SchemaBinaryBase) {
            this.value = ((SchemaBinaryBase)newvalue).value;
            this.isempty = ((SchemaBinaryBase)newvalue).isempty;
         } else {
            if(!(newvalue instanceof SchemaString)) {
               throw new TypesIncompatibleException(newvalue, this);
            }

            this.parse(newvalue.toString());
         }
      } else {
         this.setNull();
      }

   }

   public byte[] getValue() {
      return this.value;
   }

   public void setValue(byte[] newvalue) {
      this.value = newvalue;
      this.isnull = newvalue == null;
      this.isempty = newvalue.length == 0;
   }

   public abstract void parse(String var1);

   public void setNull() {
      this.isnull = true;
      this.isempty = true;
      this.value = null;
   }

   public void setEmpty() {
      this.isnull = false;
      this.isempty = true;
      this.value = null;
   }

   public int hashCode() {
      return this.value == null?0:this.value[0];
   }

   public int length() {
      return this.value.length;
   }

   public boolean booleanValue() {
      return true;
   }

   public boolean isEmpty() {
      return this.isempty;
   }

   public boolean isNull() {
      return this.isnull;
   }

   public int compareTo(Object obj) {
      return this.compareTo((SchemaBinaryBase)obj);
   }

   public int compareTo(SchemaBinaryBase obj) {
      if(this.value.length != obj.value.length) {
         return (new Integer(this.value.length)).compareTo(new Integer(obj.value.length));
      } else {
         for(int i = 0; i < this.value.length; ++i) {
            if(this.value[i] != obj.value[i]) {
               return (new Integer(this.value[i])).compareTo(new Integer(obj.value[i]));
            }
         }

         return 0;
      }
   }
}
