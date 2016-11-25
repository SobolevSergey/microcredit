package com.altova.types;

import com.altova.types.SchemaType;
import java.math.BigDecimal;
import java.math.BigInteger;

public interface SchemaTypeNumber extends SchemaType {
   int NUMERIC_VALUE_INT = 1;
   int NUMERIC_VALUE_LONG = 2;
   int NUMERIC_VALUE_BIGINTEGER = 3;
   int NUMERIC_VALUE_FLOAT = 4;
   int NUMERIC_VALUE_DOUBLE = 5;
   int NUMERIC_VALUE_BIGDECIMAL = 6;

   int numericType();

   int intValue();

   long longValue();

   BigInteger bigIntegerValue();

   float floatValue();

   double doubleValue();

   BigDecimal bigDecimalValue();
}
