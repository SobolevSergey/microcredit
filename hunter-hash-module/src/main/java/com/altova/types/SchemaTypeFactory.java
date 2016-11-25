package com.altova.types;

import com.altova.types.SchemaBoolean;
import com.altova.types.SchemaByte;
import com.altova.types.SchemaDate;
import com.altova.types.SchemaDateTime;
import com.altova.types.SchemaDecimal;
import com.altova.types.SchemaDuration;
import com.altova.types.SchemaInt;
import com.altova.types.SchemaInteger;
import com.altova.types.SchemaString;
import com.altova.types.SchemaTime;
import com.altova.types.SchemaType;
import com.altova.types.StringParseException;
import java.math.BigDecimal;

public class SchemaTypeFactory {
   public static SchemaType createInstanceByString(String value) {
      if(value == null) {
         SchemaString e5 = new SchemaString();
         e5.setNull();
         return e5;
      } else if(value.length() == 0) {
         return new SchemaString();
      } else if(value.compareToIgnoreCase("false") == 0) {
         return new SchemaBoolean(false);
      } else if(value.compareToIgnoreCase("true") == 0) {
         return new SchemaBoolean(true);
      } else {
         try {
            SchemaDateTime e4 = new SchemaDateTime(value);
            return e4;
         } catch (StringParseException var6) {
            try {
               SchemaDuration e3 = new SchemaDuration(value);
               return e3;
            } catch (StringParseException var5) {
               try {
                  SchemaDate e2 = new SchemaDate(value);
                  return e2;
               } catch (StringParseException var4) {
                  try {
                     SchemaTime e1 = new SchemaTime(value);
                     return e1;
                  } catch (StringParseException var3) {
                     try {
                        BigDecimal e = new BigDecimal(value);
                        return (SchemaType)(e.scale() <= 0?(e.compareTo(new BigDecimal(Integer.MAX_VALUE)) <= 0 && e.compareTo(new BigDecimal(Integer.MIN_VALUE)) >= 0?new SchemaInt(e.intValue()):new SchemaInteger(e.toBigInteger())):new SchemaDecimal(e));
                     } catch (NumberFormatException var2) {
                        return new SchemaString(value);
                     }
                  }
               }
            }
         }
      }
   }

   public static SchemaType createInstanceByObject(Object value) {
      return (SchemaType)(value instanceof Boolean?new SchemaBoolean(((Boolean)value).booleanValue()):(value instanceof Byte?new SchemaByte(((Byte)value).byteValue()):new SchemaString(value.toString())));
   }
}
