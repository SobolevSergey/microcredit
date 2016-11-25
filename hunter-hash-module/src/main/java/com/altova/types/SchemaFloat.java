package com.altova.types;

import com.altova.types.SchemaInteger;
import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeNumber;
import com.altova.types.StringParseException;
import com.altova.types.TypesIncompatibleException;
import com.altova.types.ValuesNotConvertableException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SchemaFloat implements SchemaTypeNumber {
   protected float value;
   protected boolean isempty;
   protected boolean isnull;

   public SchemaFloat() {
      this.setEmpty();
   }

   public SchemaFloat(SchemaFloat newvalue) {
      this.value = newvalue.value;
      this.isempty = newvalue.isempty;
      this.isnull = newvalue.isnull;
   }

   public SchemaFloat(float newvalue) {
      this.setValue(newvalue);
   }

   public SchemaFloat(double newvalue) {
      this.setValue((float)newvalue);
   }

   public SchemaFloat(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaFloat(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaFloat(SchemaTypeNumber newvalue) {
      this.assign(newvalue);
   }

   public float getValue() {
      return this.value;
   }

   public void setValue(float newvalue) {
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
            this.value = Float.parseFloat(newvalue);
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

            this.value = ((SchemaTypeNumber)newvalue).floatValue();
            this.isempty = false;
         }
      } else {
         this.setNull();
      }

   }

   public void setNull() {
      this.isnull = true;
      this.isempty = true;
      this.value = 0.0F;
   }

   public void setEmpty() {
      this.isnull = false;
      this.isempty = true;
      this.value = 0.0F;
   }

   public int hashCode() {
      return Float.floatToIntBits(this.value);
   }

   public boolean equals(Object obj) {
      return !(obj instanceof SchemaFloat)?false:this.value == ((SchemaFloat)obj).value;
   }

   public Object clone() {
      return new SchemaFloat(this);
   }

   public String toString() {
      if(!this.isempty && !this.isnull) {
         String result = Float.toString(this.value);
         return result.length() > 2 && result.substring(result.length() - 2, result.length()).equals(".0")?result.substring(0, result.length() - 2):result;
      } else {
         return "";
      }
   }

   public int length() {
      return this.toString().length();
   }

   public boolean booleanValue() {
      return this.value != 0.0F && this.value != Float.NaN;
   }

   public boolean isEmpty() {
      return this.isempty;
   }

   public boolean isNull() {
      return this.isnull;
   }

   public int compareTo(Object obj) {
      return this.compareTo((SchemaFloat)obj);
   }

   public int compareTo(SchemaFloat obj) {
      return Float.compare(this.value, obj.value);
   }

   public int numericType() {
      return 4;
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
      return this.value;
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public BigDecimal bigDecimalValue() {
      return new BigDecimal((double)this.value);
   }
}
