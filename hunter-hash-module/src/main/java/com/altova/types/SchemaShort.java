package com.altova.types;

import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeNumber;
import com.altova.types.TypesIncompatibleException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SchemaShort implements SchemaTypeNumber {
   protected short value;
   protected boolean isempty;
   protected boolean isnull;

   public SchemaShort() {
      this.setEmpty();
   }

   public SchemaShort(SchemaShort newvalue) {
      this.value = newvalue.value;
      this.isempty = newvalue.isempty;
      this.isnull = newvalue.isnull;
   }

   public SchemaShort(short newvalue) {
      this.setValue(newvalue);
   }

   public SchemaShort(int newvalue) {
      this.setValue((short)newvalue);
   }

   public SchemaShort(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaShort(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaShort(SchemaTypeNumber newvalue) {
      this.assign(newvalue);
   }

   public short getValue() {
      return this.value;
   }

   public void setValue(short newvalue) {
      this.value = newvalue;
      this.isempty = false;
   }

   public void parse(String newvalue) {
      if(newvalue == null) {
         this.setNull();
      } else if(newvalue.length() == 0) {
         this.setEmpty();
      } else {
         this.value = Short.parseShort(newvalue);
         this.isempty = false;
         this.isnull = false;
      }

   }

   public void assign(SchemaType newvalue) {
      if(newvalue != null && !newvalue.isNull()) {
         if(newvalue.isEmpty()) {
            this.setEmpty();
         } else {
            if(!(newvalue instanceof SchemaTypeNumber)) {
               throw new TypesIncompatibleException(newvalue, this);
            }

            this.value = (short)((SchemaTypeNumber)newvalue).intValue();
            this.isempty = false;
            this.isnull = false;
         }
      } else {
         this.setNull();
      }

   }

   public void setNull() {
      this.isnull = true;
      this.isempty = true;
      this.value = 0;
   }

   public void setEmpty() {
      this.isnull = false;
      this.isempty = true;
      this.value = 0;
   }

   public int hashCode() {
      return this.value;
   }

   public boolean equals(Object obj) {
      return !(obj instanceof SchemaShort)?false:this.value == ((SchemaShort)obj).value;
   }

   public Object clone() {
      return new SchemaShort(this);
   }

   public String toString() {
      return !this.isempty && !this.isnull?Short.toString(this.value):"";
   }

   public int length() {
      return this.toString().length();
   }

   public boolean booleanValue() {
      return this.value != 0;
   }

   public boolean isEmpty() {
      return this.isempty;
   }

   public boolean isNull() {
      return this.isnull;
   }

   public int compareTo(Object obj) {
      return this.compareTo((SchemaShort)obj);
   }

   public int compareTo(SchemaShort obj) {
      return (new Short(this.value)).compareTo(new Short(obj.value));
   }

   public int numericType() {
      return 1;
   }

   public int intValue() {
      return this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public BigInteger bigIntegerValue() {
      return BigInteger.valueOf((long)this.value);
   }

   public float floatValue() {
      return (float)this.value;
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public BigDecimal bigDecimalValue() {
      return BigDecimal.valueOf((long)this.value);
   }
}
