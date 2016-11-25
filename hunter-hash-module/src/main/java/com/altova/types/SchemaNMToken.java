package com.altova.types;

import com.altova.types.SchemaToken;

public class SchemaNMToken extends SchemaToken {
   public SchemaNMToken() {
   }

   public SchemaNMToken(String newvalue) {
      super(newvalue);
   }

   public SchemaNMToken(SchemaNMToken newvalue) {
      super((SchemaToken)newvalue);
   }
}
