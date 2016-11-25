package com.altova.types;

import java.io.Serializable;

public interface SchemaType extends Cloneable, Comparable, Serializable {
   void parse(String var1);

   void setNull();

   void setEmpty();

   boolean booleanValue();

   boolean isEmpty();

   boolean isNull();

   String toString();

   int length();
}
