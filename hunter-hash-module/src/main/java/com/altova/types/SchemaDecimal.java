package com.altova.types;

import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeNumber;
import com.altova.types.StringParseException;
import com.altova.types.TypesIncompatibleException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SchemaDecimal implements SchemaTypeNumber {
   protected BigDecimal value;
   protected boolean isempty;
   protected boolean isnull;

   public SchemaDecimal() {
      this.setEmpty();
   }

   public SchemaDecimal(SchemaDecimal newvalue) {
      this.value = newvalue.value;
      this.isempty = newvalue.isempty;
      this.isnull = newvalue.isnull;
   }

   public SchemaDecimal(BigDecimal newvalue) {
      this.setValue(newvalue);
   }

   public SchemaDecimal(double newvalue) {
      this.setValue(newvalue);
   }

   public SchemaDecimal(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaDecimal(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaDecimal(SchemaTypeNumber newvalue) {
      this.assign(newvalue);
   }

   public BigDecimal getValue() {
      return this.value;
   }

   public void setValue(BigDecimal newvalue) {
      if(newvalue == null) {
         this.setNull();
      } else {
         this.value = newvalue;
         this.isempty = false;
         this.isnull = false;
      }

   }

   public void setValue(double newvalue) {
      this.value = new BigDecimal(newvalue);
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
            this.value = new BigDecimal(newvalue);
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

            this.value = ((SchemaTypeNumber)newvalue).bigDecimalValue();
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
      this.value = BigDecimal.valueOf(0L);
   }

   public void setEmpty() {
      this.isnull = false;
      this.isempty = true;
      this.value = BigDecimal.valueOf(0L);
   }

   public int hashCode() {
      return this.value.hashCode();
   }

   public boolean equals(Object obj) {
      return !(obj instanceof SchemaDecimal)?false:this.value.compareTo(((SchemaDecimal)obj).value) == 0;
   }

   public Object clone() {
      return new SchemaDecimal(this);
   }

   public String toString() {
      if(!this.isempty && !this.isnull) {
         String result = this.value.toString();
         int decimalpos = result.indexOf(46);
         if(result.length() > 0 && decimalpos > 0) {
            int nPos = result.length();

            for(nPos = result.length() - 1; nPos > decimalpos && result.charAt(nPos) == 48; --nPos) {
               ;
            }

            if(result.charAt(nPos) == 46) {
               --nPos;
            }

            return result.substring(0, nPos + 1);
         } else {
            return result;
         }
      } else {
         return "";
      }
   }

   public int length() {
      return this.toString().length();
   }

   public boolean booleanValue() {
      return this.value.compareTo(new BigDecimal(0)) != 0;
   }

   public boolean isEmpty() {
      return this.isempty;
   }

   public boolean isNull() {
      return this.isnull;
   }

   public int compareTo(Object obj) {
      return this.compareTo((SchemaDecimal)obj);
   }

   public int compareTo(SchemaDecimal obj) {
      return this.value.compareTo(obj.value);
   }

   public void reduceScale() {
      if(this.value.scale() > 0) {
         String sScaled = this.value.toString();
         int nPos = sScaled.length() - 1;

         int nReduceScale;
         for(nReduceScale = 0; nPos >= 0 && sScaled.substring(nPos, nPos + 1).equals("0"); ++nReduceScale) {
            --nPos;
         }

         this.value = this.value.setScale(this.value.scale() - nReduceScale);
      }
   }

   public int numericType() {
      return 6;
   }

   public int intValue() {
      return this.value.intValue();
   }

   public long longValue() {
      return this.value.longValue();
   }

   public BigInteger bigIntegerValue() {
      return this.value.toBigInteger();
   }

   public float floatValue() {
      return this.value.floatValue();
   }

   public double doubleValue() {
      return this.value.doubleValue();
   }

   public BigDecimal bigDecimalValue() {
      return this.value;
   }
}
