package com.altova.types;

import com.altova.types.SchemaDecimal;
import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeNumber;
import com.altova.types.StringParseException;
import com.altova.types.TypesIncompatibleException;
import com.altova.types.ValuesNotConvertableException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SchemaInteger implements SchemaTypeNumber {
   protected BigInteger value;
   protected boolean isempty;
   protected boolean isnull;

   public SchemaInteger() {
      this.setEmpty();
   }

   public SchemaInteger(SchemaInteger newvalue) {
      this.value = newvalue.value;
      this.isempty = newvalue.isempty;
      this.isnull = newvalue.isnull;
   }

   public SchemaInteger(BigInteger newvalue) {
      this.setValue(newvalue);
   }

   public SchemaInteger(long newvalue) {
      this.setValue(newvalue);
   }

   public SchemaInteger(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaInteger(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaInteger(SchemaTypeNumber newvalue) {
      this.assign(newvalue);
   }

   public BigInteger getValue() {
      return this.value;
   }

   public void setValue(BigInteger newvalue) {
      if(newvalue == null) {
         this.isempty = true;
         this.isnull = true;
         this.value = BigInteger.valueOf(0L);
      } else {
         this.value = newvalue;
         this.isempty = false;
         this.isnull = false;
      }
   }

   public void setValue(long newvalue) {
      this.value = BigInteger.valueOf(newvalue);
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
            this.value = new BigInteger(newvalue);
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

            this.value = ((SchemaTypeNumber)newvalue).bigIntegerValue();
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
      this.value = BigInteger.valueOf(0L);
   }

   public void setEmpty() {
      this.isnull = false;
      this.isempty = true;
      this.value = BigInteger.valueOf(0L);
   }

   public int hashCode() {
      return this.value.hashCode();
   }

   public boolean equals(Object obj) {
      return !(obj instanceof SchemaInteger)?false:this.value.equals(((SchemaInteger)obj).value);
   }

   public Object clone() {
      return new SchemaInteger(this);
   }

   public String toString() {
      return !this.isempty && !this.isnull?this.value.toString():"";
   }

   public int length() {
      return this.toString().length();
   }

   public boolean booleanValue() {
      return this.value.compareTo(BigInteger.valueOf(0L)) != 0;
   }

   public boolean isEmpty() {
      return this.isempty;
   }

   public boolean isNull() {
      return this.isnull;
   }

   public int compareTo(Object obj) {
      return this.compareTo((SchemaInteger)obj);
   }

   public int compareTo(SchemaInteger obj) {
      return this.value.compareTo(obj.value);
   }

   public int numericType() {
      return 3;
   }

   public int intValue() {
      return this.value.intValue();
   }

   public long longValue() {
      return this.value.longValue();
   }

   public BigInteger bigIntegerValue() {
      return this.value;
   }

   public float floatValue() {
      return this.value.floatValue();
   }

   public double doubleValue() {
      return this.value.doubleValue();
   }

   public BigDecimal bigDecimalValue() {
      try {
         return new BigDecimal(this.value.toString());
      } catch (NumberFormatException var2) {
         throw new ValuesNotConvertableException(this, new SchemaDecimal(0.0D));
      }
   }
}
