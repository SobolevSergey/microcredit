package com.altova.types;

import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeNumber;
import com.altova.types.TypesIncompatibleException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SchemaByte implements SchemaTypeNumber {
   protected byte value;
   protected boolean isempty;
   protected boolean isnull;

   public SchemaByte() {
      this.setEmpty();
   }

   public SchemaByte(SchemaByte newvalue) {
      this.value = newvalue.value;
      this.isempty = newvalue.isempty;
      this.isnull = newvalue.isnull;
   }

   public SchemaByte(int newvalue) {
      this.setValue(newvalue);
   }

   public SchemaByte(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaByte(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaByte(SchemaTypeNumber newvalue) {
      this.assign(newvalue);
   }

   public byte getValue() {
      return this.value;
   }

   public void setValue(int newvalue) {
      this.value = (byte)newvalue;
      this.isempty = false;
      this.isnull = false;
   }

   public void parse(String newvalue) {
      if(newvalue == null) {
         this.setNull();
      } else if(newvalue.length() == 0) {
         this.setEmpty();
      } else {
         this.isempty = false;
         this.isnull = false;
         this.value = Byte.parseByte(newvalue);
      }

   }

   public void assign(SchemaType newvalue) {
      if(newvalue != null && !newvalue.isNull()) {
         if(newvalue.isEmpty()) {
            this.setEmpty();
         } else {
            this.isnull = false;
            this.isempty = false;
            if(!(newvalue instanceof SchemaTypeNumber)) {
               throw new TypesIncompatibleException(newvalue, this);
            }

            this.value = (byte)((SchemaTypeNumber)newvalue).intValue();
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
      return !(obj instanceof SchemaByte)?false:this.value == ((SchemaByte)obj).value;
   }

   public Object clone() {
      return new SchemaByte(this);
   }

   public String toString() {
      return !this.isempty && !this.isnull?Byte.toString(this.value):"";
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
      return this.compareTo((SchemaByte)obj);
   }

   public int compareTo(SchemaByte obj) {
      return (new Byte(this.value)).compareTo(new Byte(obj.value));
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
