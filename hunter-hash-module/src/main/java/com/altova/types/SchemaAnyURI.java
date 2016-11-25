package com.altova.types;

import com.altova.types.SchemaString;
import com.altova.types.SchemaType;
import com.altova.types.StringParseException;
import com.altova.types.TypesIncompatibleException;
import java.net.URI;

public class SchemaAnyURI implements SchemaType {
   protected URI value;
   protected boolean isempty;
   protected boolean isnull;

   public SchemaAnyURI() {
      this.setEmpty();
   }

   public SchemaAnyURI(SchemaAnyURI newvalue) {
      this.value = newvalue.value;
      this.isempty = newvalue.isempty;
      this.isnull = newvalue.isnull;
   }

   public SchemaAnyURI(URI newvalue) {
      this.setValue(newvalue);
   }

   public SchemaAnyURI(String newvalue) {
      this.parse(newvalue);
   }

   public SchemaAnyURI(SchemaType newvalue) {
      this.assign(newvalue);
   }

   public URI getValue() {
      return this.value;
   }

   public void setValue(URI newvalue) {
      if(newvalue == null) {
         this.isempty = true;
         this.isnull = true;
      } else {
         this.isnull = false;
         this.isempty = false;
         this.value = newvalue;
      }

   }

   public void parse(String newvalue) {
      if(newvalue == null) {
         this.setNull();
      } else if(newvalue.length() == 0) {
         this.setEmpty();
      } else {
         this.setNull();

         try {
            this.value = URI.create(newvalue);
         } catch (IllegalArgumentException var3) {
            throw new StringParseException(var3);
         }

         this.isnull = false;
         this.isempty = false;
      }

   }

   public void assign(SchemaType newvalue) {
      if(newvalue != null && !newvalue.isNull()) {
         if(newvalue.isEmpty()) {
            this.setEmpty();
         } else if(newvalue instanceof SchemaString) {
            this.parse(newvalue.toString());
         } else {
            if(!(newvalue instanceof SchemaAnyURI)) {
               throw new TypesIncompatibleException(newvalue, this);
            }

            this.setValue(((SchemaAnyURI)newvalue).value);
         }
      } else {
         this.setNull();
      }

   }

   public void setNull() {
      this.isnull = true;
      this.isempty = true;
      this.value = null;
   }

   public void setEmpty() {
      this.parse("http://www.altova.com/language_select.html");
   }

   public int hashCode() {
      return this.value.hashCode();
   }

   public boolean equals(Object obj) {
      return !(obj instanceof SchemaAnyURI)?false:this.value.equals(((SchemaAnyURI)obj).value);
   }

   public Object clone() {
      return new SchemaAnyURI(this);
   }

   public String toString() {
      return !this.isempty && !this.isnull && this.value != null?this.value.toString():"";
   }

   public int length() {
      return this.value.toString().length();
   }

   public boolean booleanValue() {
      return this.value != null && this.value.toString().length() != 0;
   }

   public boolean isEmpty() {
      return this.isempty;
   }

   public boolean isNull() {
      return this.isnull;
   }

   public int compareTo(Object obj) {
      return this.compareTo((SchemaAnyURI)obj);
   }

   public int compareTo(SchemaAnyURI obj) {
      return this.toString().compareTo(obj.toString());
   }
}
