package com.altova.types;

import com.altova.types.SchemaDate;
import com.altova.types.SchemaDateTime;
import com.altova.types.SchemaDecimal;
import com.altova.types.SchemaDouble;
import com.altova.types.SchemaDuration;
import com.altova.types.SchemaFloat;
import com.altova.types.SchemaInt;
import com.altova.types.SchemaInteger;
import com.altova.types.SchemaLong;
import com.altova.types.SchemaTime;
import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeCalendar;
import com.altova.types.SchemaTypeNumber;
import com.altova.types.StringParseException;
import com.altova.types.ValuesNotConvertableException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SchemaString implements SchemaTypeNumber, SchemaTypeCalendar {
   protected String value;
   protected boolean isempty;
   protected boolean isnull;

   public SchemaString() {
      this.setEmpty();
   }

   public SchemaString(SchemaString newvalue) {
      this.value = newvalue.value;
      this.isempty = newvalue.isempty;
      this.isnull = newvalue.isnull;
   }

   public SchemaString(String newvalue) {
      this.setValue(newvalue);
   }

   public SchemaString(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public SchemaString(SchemaTypeNumber newvalue) {
      this.assign(newvalue);
   }

   public SchemaString(SchemaTypeCalendar newvalue) {
      this.assign(newvalue);
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String newvalue) {
      if(newvalue == null) {
         this.isempty = true;
         this.value = "";
      } else {
         this.value = newvalue;
         this.isempty = this.value.length() == 0;
      }
   }

   public void parse(String newvalue) {
      if(newvalue == null) {
         this.setNull();
      } else if(newvalue.length() == 0) {
         this.setEmpty();
      } else {
         this.setValue(newvalue);
      }

   }

   public void assign(SchemaType newvalue) {
      if(newvalue != null && !newvalue.isNull()) {
         if(newvalue.isEmpty()) {
            this.setEmpty();
         } else {
            this.value = newvalue.toString();
            this.isempty = this.value.length() == 0;
            this.isnull = false;
         }
      } else {
         this.setNull();
      }

   }

   public void setNull() {
      this.isnull = true;
      this.isempty = true;
      this.value = "";
   }

   public void setEmpty() {
      this.isnull = false;
      this.isempty = true;
      this.value = "";
   }

   public int hashCode() {
      return this.value.hashCode();
   }

   public boolean equals(Object obj) {
      return !(obj instanceof SchemaString)?false:this.value.equals(((SchemaString)obj).value);
   }

   public Object clone() {
      if(this.isnull) {
         SchemaString result = new SchemaString();
         result.setNull();
         return result;
      } else {
         return this.isempty?new SchemaString():new SchemaString(new String(this.value));
      }
   }

   public String toString() {
      return !this.isempty && !this.isnull?this.value:"";
   }

   public int length() {
      return this.value.length();
   }

   public boolean booleanValue() {
      return this.value != null && this.value.length() != 0 && this.value.compareToIgnoreCase("false") != 0?(this.isValueNumeric()?this.bigDecimalValue().compareTo(BigDecimal.valueOf(0L)) != 0:true):false;
   }

   public boolean isEmpty() {
      return this.isempty;
   }

   public boolean isNull() {
      return this.isnull;
   }

   public int compareTo(Object obj) {
      return this.compareTo((SchemaString)obj);
   }

   public int compareTo(SchemaString obj) {
      return this.value.compareTo(obj.value);
   }

   public boolean isValueNumeric() {
      try {
         new BigDecimal(this.value);
         return true;
      } catch (NumberFormatException var2) {
         return false;
      }
   }

   public int numericType() {
      return 6;
   }

   public int intValue() {
      if(!this.isnull && !this.isempty && this.value.length() != 0) {
         try {
            return Integer.parseInt(this.value);
         } catch (NumberFormatException var2) {
            throw new ValuesNotConvertableException(this, new SchemaInt(0));
         }
      } else {
         return 0;
      }
   }

   public long longValue() {
      if(!this.isnull && !this.isempty && this.value.length() != 0) {
         try {
            return Long.parseLong(this.value);
         } catch (NumberFormatException var2) {
            throw new ValuesNotConvertableException(this, new SchemaLong(0L));
         }
      } else {
         return 0L;
      }
   }

   public BigInteger bigIntegerValue() {
      if(!this.isnull && !this.isempty && this.value.length() != 0) {
         try {
            return new BigInteger(this.value);
         } catch (NumberFormatException var2) {
            throw new ValuesNotConvertableException(this, new SchemaInteger(0L));
         }
      } else {
         return new BigInteger("0");
      }
   }

   public float floatValue() {
      if(!this.isnull && !this.isempty && this.value.length() != 0) {
         try {
            return Float.parseFloat(this.value);
         } catch (NumberFormatException var2) {
            throw new ValuesNotConvertableException(this, new SchemaFloat(0.0F));
         }
      } else {
         return 0.0F;
      }
   }

   public double doubleValue() {
      if(!this.isnull && !this.isempty && this.value.length() != 0) {
         try {
            return Double.parseDouble(this.value);
         } catch (NumberFormatException var2) {
            throw new ValuesNotConvertableException(this, new SchemaDouble(0.0D));
         }
      } else {
         return 0.0D;
      }
   }

   public BigDecimal bigDecimalValue() {
      if(!this.isnull && !this.isempty && this.value.length() != 0) {
         try {
            return new BigDecimal(this.value);
         } catch (NumberFormatException var2) {
            throw new ValuesNotConvertableException(this, new SchemaDecimal(0.0D));
         }
      } else {
         return new BigDecimal(0);
      }
   }

   public int calendarType() {
      return -1;
   }

   public SchemaDuration durationValue() {
      try {
         return new SchemaDuration(this.value);
      } catch (StringParseException var2) {
         throw new ValuesNotConvertableException(this, new SchemaDuration("PT"));
      }
   }

   public SchemaDateTime dateTimeValue() {
      try {
         return new SchemaDateTime(this.value);
      } catch (StringParseException var2) {
         throw new ValuesNotConvertableException(this, new SchemaDateTime("2003-08-06T00:00:00"));
      }
   }

   public SchemaDate dateValue() {
      try {
         return new SchemaDate(this.value);
      } catch (StringParseException var2) {
         throw new ValuesNotConvertableException(this, new SchemaDate("2003-08-06"));
      }
   }

   public SchemaTime timeValue() {
      try {
         return new SchemaTime(this.value);
      } catch (StringParseException var2) {
         throw new ValuesNotConvertableException(this, new SchemaTime("00:00:00"));
      }
   }
}
