package com.altova.types;

import com.altova.types.SchemaType;

public interface SchemaTypeBinary extends SchemaType {
   int BINARY_VALUE_UNDEFINED = -1;
   int BINARY_VALUE_BASE64 = 0;
   int BINARY_VALUE_HEX = 1;

   int binaryType();
}
