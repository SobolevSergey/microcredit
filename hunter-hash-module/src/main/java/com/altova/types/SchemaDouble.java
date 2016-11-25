package com.altova.types;

import com.altova.types.SchemaInteger;
import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeNumber;
import com.altova.types.StringParseException;
import com.altova.types.TypesIncompatibleException;
import com.altova.types.ValuesNotConvertableException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SchemaDouble implements SchemaTypeNumber {
   protected double value;
   protected boolean isempty;
   protected boolean isnull;

   public SchemaDouble() {
      this.setEmpty();
   }

   public SchemaDouble(SchemaDouble newvalue) {
      this.value = newvalue.value;
      this.isempty = newvalue.isempty;
      this.isnull = newvalue.isnull;
   }

   public SchemaDouble(double newvalue) {
      this.setValue(newvalue);
   }

   public SchemaDouble(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaDouble(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaDouble(SchemaTypeNumber newvalue) {
      this.assign(newvalue);
   }

   public double getValue() {
      return this.value;
   }

   public void setValue(double newvalue) {
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
            this.value = Double.parseDouble(newvalue);
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

            this.value = ((SchemaTypeNumber)newvalue).doubleValue();
         }
      } else {
         this.setNull();
      }

      this.isempty = false;
   }

   public void setNull() {
      this.isnull = true;
      this.isempty = true;
      this.value = 0.0D;
   }

   public void setEmpty() {
      this.isnull = false;
      this.isempty = true;
      this.value = 0.0D;
   }

   public int hashCode() {
      return (int)Double.doubleToLongBits(this.value);
   }

   public boolean equals(Object obj) {
      return !(obj instanceof SchemaDouble)?false:this.value == ((SchemaDouble)obj).value;
   }

   public Object clone() {
      return new SchemaDouble(this);
   }

   public String toString() {
      if(!this.isempty && !this.isnull) {
         String result = Double.toString(this.value);
         return result.length() > 2 && result.substring(result.length() - 2, result.length()).equals(".0")?result.substring(0, result.length() - 2):result;
      } else {
         return "";
      }
   }

   public int length() {
      return this.toString().length();
   }

   public boolean booleanValue() {
      return this.value != 0.0D && this.value != Double.NaN;
   }

   public boolean isEmpty() {
      return this.isempty;
   }

   public boolean isNull() {
      return this.isnull;
   }

   public int compareTo(Object obj) {
      return this.compareTo((SchemaDouble)obj);
   }

   public int compareTo(SchemaDouble obj) {
      return Double.compare(this.value, obj.value);
   }

   public int numericType() {
      return 5;
   }

   public int intValue() {
      return (int)this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public BigInteger bigIntegerValue() {
      try {
         return new BigInteger(this.toString());
      } catch (NumberFormatException var2) {
         throw new ValuesNotConvertableException(this, new SchemaInteger(0L));
      }
   }

   public float floatValue() {
      return (float)this.value;
   }

   public double doubleValue() {
      return this.value;
   }

   public BigDecimal bigDecimalValue() {
      return new BigDecimal(this.value);
   }
}
