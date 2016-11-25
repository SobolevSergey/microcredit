package com.altova.types;

import com.altova.types.SchemaString;
import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SchemaBoolean implements SchemaTypeNumber {
   protected boolean value;
   protected boolean isempty;
   protected boolean isnull;

   public SchemaBoolean() {
      this.setEmpty();
   }

   public SchemaBoolean(SchemaBoolean newvalue) {
      this.value = newvalue.value;
      this.isempty = newvalue.isempty;
      this.isnull = newvalue.isnull;
   }

   public SchemaBoolean(boolean newvalue) {
      this.setValue(newvalue);
   }

   public SchemaBoolean(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaBoolean(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaBoolean(SchemaTypeNumber newvalue) {
      this.assign(newvalue);
   }

   public boolean getValue() {
      return this.value;
   }

   public void setValue(boolean newvalue) {
      this.isnull = false;
      this.isempty = false;
      this.value = newvalue;
   }

   public void parse(String newvalue) {
      if(newvalue == null) {
         this.setNull();
      } else if(newvalue.length() == 0) {
         this.setEmpty();
      } else {
         this.setValue((new SchemaString(newvalue)).booleanValue());
      }

   }

   public void assign(SchemaType newvalue) {
      if(newvalue != null && !newvalue.isNull()) {
         if(newvalue.isEmpty()) {
            this.setEmpty();
         } else {
            this.parse(newvalue.toString());
         }
      } else {
         this.setNull();
      }

   }

   public void setNull() {
      this.isnull = true;
      this.isempty = true;
      this.value = false;
   }

   public void setEmpty() {
      this.isnull = false;
      this.isempty = true;
      this.value = false;
   }

   public int hashCode() {
      return this.value?1231:1237;
   }

   public boolean equals(Object obj) {
      return !(obj instanceof SchemaBoolean)?false:this.value == ((SchemaBoolean)obj).value;
   }

   public Object clone() {
      return new SchemaBoolean(this);
   }

   public String toString() {
      return !this.isempty && !this.isnull?(this.value?"true":"false"):"";
   }

   public int length() {
      return 1;
   }

   public boolean booleanValue() {
      return this.value;
   }

   public boolean isEmpty() {
      return this.isempty;
   }

   public boolean isNull() {
      return this.isnull;
   }

   public int compareTo(Object obj) {
      return this.compareTo((SchemaBoolean)obj);
   }

   public int compareTo(SchemaBoolean obj) {
      return this.value == obj.value?0:(!this.value?-1:1);
   }

   public int numericType() {
      return 1;
   }

   public void setValue(int newvalue) {
      this.isnull = false;
      this.isempty = false;
      this.value = newvalue != 0;
   }

   public void setValue(long newvalue) {
      this.isnull = false;
      this.isempty = false;
      this.value = newvalue != 0L;
   }

   public void setValue(BigInteger newvalue) {
      this.isnull = false;
      this.isempty = false;
      this.value = newvalue.compareTo(BigInteger.valueOf(0L)) != 0;
   }

   public void setValue(float newvalue) {
      this.isnull = false;
      this.isempty = false;
      this.value = newvalue != 0.0F;
   }

   public void setValue(double newvalue) {
      this.isnull = false;
      this.isempty = false;
      this.value = newvalue != 0.0D;
   }

   public void setValue(BigDecimal newvalue) {
      this.isnull = false;
      this.isempty = false;
      this.value = newvalue.compareTo(BigDecimal.valueOf(0L)) != 0;
   }

   public int intValue() {
      return this.value?1:0;
   }

   public long longValue() {
      return this.value?1L:0L;
   }

   public BigInteger bigIntegerValue() {
      return this.value?BigInteger.valueOf(1L):BigInteger.valueOf(0L);
   }

   public float floatValue() {
      return this.value?1.0F:0.0F;
   }

   public double doubleValue() {
      return this.value?1.0D:0.0D;
   }

   public BigDecimal bigDecimalValue() {
      return this.value?BigDecimal.valueOf(1L):BigDecimal.valueOf(0L);
   }
}
