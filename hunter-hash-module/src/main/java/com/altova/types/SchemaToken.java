package com.altova.types;

import com.altova.types.SchemaNormalizedString;

public class SchemaToken extends SchemaNormalizedString {
   public SchemaToken() {
   }

   public SchemaToken(String newvalue) {
      super(newvalue);
   }

   public SchemaToken(SchemaToken newvalue) {
      super((SchemaNormalizedString)newvalue);
   }
}
