package com.altova.types;

import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeNumber;
import com.altova.types.StringParseException;
import com.altova.types.TypesIncompatibleException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SchemaInt implements SchemaTypeNumber {
   protected int value;
   protected boolean isempty;
   protected boolean isnull;

   public SchemaInt() {
      this.setEmpty();
   }

   public SchemaInt(SchemaInt newvalue) {
      this.value = newvalue.value;
      this.isempty = newvalue.isempty;
      this.isnull = newvalue.isnull;
   }

   public SchemaInt(int newvalue) {
      this.setValue(newvalue);
   }

   public SchemaInt(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaInt(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaInt(SchemaTypeNumber newvalue) {
      this.assign(newvalue);
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int newvalue) {
      this.value = newvalue;
      this.isempty = false;
      this.isnull = false;
   }

   public void parse(String newvalue) {
      if(newvalue == null) {
         this.setNull();
      } else if(newvalue.length() == 0) {
         this.setEmpty();
      } else {
         try {
            this.value = Integer.parseInt(newvalue);
            this.isempty = false;
            this.isnull = false;
         } catch (NumberFormatException var3) {
            throw new StringParseException(var3);
         }
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

            this.value = ((SchemaTypeNumber)newvalue).intValue();
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
      return !(obj instanceof SchemaInt)?false:this.value == ((SchemaInt)obj).value;
   }

   public Object clone() {
      return new SchemaInt(this);
   }

   public String toString() {
      return !this.isempty && !this.isnull?Integer.toString(this.value):"";
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
      return this.compareTo((SchemaInt)obj);
   }

   public int compareTo(SchemaInt obj) {
      return (new Integer(this.value)).compareTo(new Integer(obj.value));
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
