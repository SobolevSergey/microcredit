package com.altova.types;

import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeNumber;
import com.altova.types.StringParseException;
import com.altova.types.TypesIncompatibleException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SchemaLong implements SchemaTypeNumber {
   protected long value;
   protected boolean isempty;
   protected boolean isnull;

   public SchemaLong() {
      this.setEmpty();
   }

   public SchemaLong(SchemaLong newvalue) {
      this.value = newvalue.value;
      this.isempty = newvalue.isempty;
      this.isnull = newvalue.isnull;
   }

   public SchemaLong(long newvalue) {
      this.setValue(newvalue);
   }

   public SchemaLong(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaLong(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaLong(SchemaTypeNumber newvalue) {
      this.assign(newvalue);
   }

   public long getValue() {
      return this.value;
   }

   public void setValue(long newvalue) {
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
            this.value = Long.parseLong(newvalue);
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

            this.value = ((SchemaTypeNumber)newvalue).longValue();
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
      this.value = 0L;
   }

   public void setEmpty() {
      this.isnull = false;
      this.isempty = true;
      this.value = 0L;
   }

   public int hashCode() {
      return (int)this.value;
   }

   public boolean equals(Object obj) {
      return !(obj instanceof SchemaLong)?false:this.value == ((SchemaLong)obj).value;
   }

   public Object clone() {
      return new SchemaLong(this);
   }

   public String toString() {
      return !this.isempty && !this.isnull?Long.toString(this.value):"";
   }

   public int length() {
      return this.toString().length();
   }

   public boolean booleanValue() {
      return this.value != 0L;
   }

   public boolean isEmpty() {
      return this.isempty;
   }

   public boolean isNull() {
      return this.isnull;
   }

   public int compareTo(Object obj) {
      return this.compareTo((SchemaLong)obj);
   }

   public int compareTo(SchemaLong obj) {
      return (new Long(this.value)).compareTo(new Long(obj.value));
   }

   public int numericType() {
      return 2;
   }

   public int intValue() {
      return (int)this.value;
   }

   public long longValue() {
      return this.value;
   }

   public BigInteger bigIntegerValue() {
      return BigInteger.valueOf(this.value);
   }

   public float floatValue() {
      return (float)this.value;
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public BigDecimal bigDecimalValue() {
      return BigDecimal.valueOf(this.value);
   }
}
