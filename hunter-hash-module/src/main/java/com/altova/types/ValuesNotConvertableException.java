package com.altova.types;

import com.altova.types.SchemaType;
import com.altova.types.SchemaTypeException;

public class ValuesNotConvertableException extends SchemaTypeException {
   protected SchemaType object1;
   protected SchemaType object2;

   public ValuesNotConvertableException(SchemaType newobj1, SchemaType newobj2) {
      super("Value \'" + newobj1.toString() + "\' could not be converted to type " + newobj2.getClass().getName());
      this.object1 = newobj1;
      this.object2 = newobj2;
   }

   public ValuesNotConvertableException(Exception other) {
      super(other);
   }
}
