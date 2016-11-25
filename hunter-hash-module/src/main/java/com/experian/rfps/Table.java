package com.experian.rfps;

import com.experian.rfps.Field;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Table {
   private String name;
   private Map<Integer, Field> fields = new TreeMap();
   private Map indexName = new HashMap();
   private Table additionFields;

   public Table(String name) {
      this.fields.clear();
      this.indexName.clear();
      this.name = name;
   }

   public void addField(int order, Field field) throws Exception {
      Integer orderList = new Integer(order);
      if(this.fields.containsKey(orderList)) {
         throw new Exception("Order number " + order + " duplicated. Field " + field.getName() + " of table " + this.name + ".");
      } else if(this.indexName.containsKey(field.getName())) {
         throw new Exception("Duplicated name of field " + field.getName() + " of table " + this.name + ".");
      } else {
         this.fields.put(orderList, field);
         this.indexName.put(field.getName(), field);
      }
   }

   public int getSize() {
      return this.fields.size();
   }

   public Field getField(int order) {
      return (Field)this.fields.get(new Integer(order));
   }

   public Field getField(String name) {
      return (Field)this.indexName.get(name);
   }

   public String getName() {
      return this.name;
   }

   public Table getAdditionFields() {
      return this.additionFields;
   }

   public void setAdditionFields(Table additionFields) {
      this.additionFields = additionFields;
   }

   public void writeSql(PrintWriter outstr) throws Exception {
      outstr.println("CREATE TABLE HUNTER(");

      int col;
      Field f;
      String msg;
      for(col = 0; col < this.fields.size(); ++col) {
         f = this.getField(col + 1);
         if(f == null) {
            msg = "Not found column number " + (col + 1) + "  of Table " + this.getName();
            throw new Exception(msg);
         }

         outstr.print(f.getName());
         outstr.print(" VARCHAR(50) DEFAULT NULL");
         if(col + 1 != this.fields.size()) {
            outstr.println(",");
         } else {
            outstr.println(");");
         }
      }

      outstr.println("INSERT INTO HUNTER(");

      for(col = 0; col < this.fields.size(); ++col) {
         f = this.getField(col + 1);
         if(f == null) {
            msg = "Not found column number " + (col + 1) + "  of Table " + this.getName();
            throw new Exception(msg);
         }

         outstr.print(f.getName());
         if(col + 1 != this.fields.size()) {
            outstr.println(",");
         } else {
            outstr.println(")");
         }
      }

      outstr.println(" VALUES(");

      for(col = 0; col < this.fields.size(); ++col) {
         f = this.getField(col + 1);
         if(f == null) {
            msg = "Not found column number " + (col + 1) + "  of Table " + this.getName();
            throw new Exception(msg);
         }

         outstr.print("\"" + f.getName() + "\"");
         if(col + 1 != this.fields.size()) {
            outstr.println(",");
         } else {
            outstr.println(");");
         }
      }

      outstr.flush();
   }

   public void writeCSV(PrintWriter outstr) throws Exception {
      int col;
      Field f;
      String msg;
      for(col = 0; col < this.fields.size(); ++col) {
         f = this.getField(col + 1);
         if(f == null) {
            msg = "Not found column number " + (col + 1) + "  of Table " + this.getName();
            throw new Exception(msg);
         }

         outstr.print(f.getName());
         if(col + 1 != this.fields.size()) {
            outstr.print(",");
         }
      }

      outstr.println();

      for(col = 0; col < this.fields.size(); ++col) {
         f = this.getField(col + 1);
         if(f == null) {
            msg = "Not found column number " + (col + 1) + "  of Table " + this.getName();
            throw new Exception(msg);
         }

         outstr.print("\"" + f.getName() + "\"");
         if(col + 1 != this.fields.size()) {
            outstr.print(",");
         }
      }

      outstr.flush();
   }
}
