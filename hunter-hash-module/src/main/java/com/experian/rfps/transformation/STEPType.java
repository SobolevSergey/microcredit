package com.experian.rfps.transformation;

import com.altova.types.SchemaByte;
import com.altova.types.SchemaString;
import com.altova.xml.Node;
import com.altova.xml.XmlException;
import org.w3c.dom.Document;

public class STEPType extends Node {
   public STEPType(STEPType node) {
      super((Node)node);
   }

   public STEPType(org.w3c.dom.Node node) {
      super(node);
   }

   public STEPType(Document doc) {
      super(doc);
   }

   public STEPType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      org.w3c.dom.Node tmpNode;
      for(tmpNode = this.getDomFirstChild(0, (String)null, "ORDER"); tmpNode != null; tmpNode = this.getDomNextChild(0, (String)null, "ORDER", tmpNode)) {
         internalAdjustPrefix(tmpNode, false);
      }

      for(tmpNode = this.getDomFirstChild(0, (String)null, "RULE"); tmpNode != null; tmpNode = this.getDomNextChild(0, (String)null, "RULE", tmpNode)) {
         internalAdjustPrefix(tmpNode, false);
      }

   }

   public static int getORDERMinCount() {
      return 1;
   }

   public static int getORDERMaxCount() {
      return 1;
   }

   public int getORDERCount() {
      return this.getDomChildCount(0, (String)null, "ORDER");
   }

   public boolean hasORDER() {
      return this.hasDomChild(0, (String)null, "ORDER");
   }

   public SchemaByte newORDER() {
      return new SchemaByte();
   }

   public SchemaByte getORDERAt(int index) throws Exception {
      return new SchemaByte(getDomNodeValue(this.dereference(this.getDomChildAt(0, (String)null, "ORDER", index))));
   }

   public org.w3c.dom.Node getStartingORDERCursor() throws Exception {
      return this.getDomFirstChild(0, (String)null, "ORDER");
   }

   public org.w3c.dom.Node getAdvancedORDERCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(0, (String)null, "ORDER", curNode);
   }

   public SchemaByte getORDERValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaByte(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaByte getORDER() throws Exception {
      return this.getORDERAt(0);
   }

   public void removeORDERAt(int index) {
      this.removeDomChildAt(0, (String)null, "ORDER", index);
   }

   public void removeORDER() {
      while(this.hasORDER()) {
         this.removeORDERAt(0);
      }

   }

   public void addORDER(SchemaByte value) {
      if(!value.isNull()) {
         this.appendDomChild(0, (String)null, "ORDER", value.toString());
      }

   }

   public void addORDER(String value) throws Exception {
      this.addORDER(new SchemaByte(value));
   }

   public void insertORDERAt(SchemaByte value, int index) {
      this.insertDomChildAt(0, (String)null, "ORDER", index, value.toString());
   }

   public void insertORDERAt(String value, int index) throws Exception {
      this.insertORDERAt(new SchemaByte(value), index);
   }

   public void replaceORDERAt(SchemaByte value, int index) {
      this.replaceDomChildAt(0, (String)null, "ORDER", index, value.toString());
   }

   public void replaceORDERAt(String value, int index) throws Exception {
      this.replaceORDERAt(new SchemaByte(value), index);
   }

   public static int getRULEMinCount() {
      return 1;
   }

   public static int getRULEMaxCount() {
      return 1;
   }

   public int getRULECount() {
      return this.getDomChildCount(0, (String)null, "RULE");
   }

   public boolean hasRULE() {
      return this.hasDomChild(0, (String)null, "RULE");
   }

   public SchemaString newRULE() {
      return new SchemaString();
   }

   public SchemaString getRULEAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(0, (String)null, "RULE", index))));
   }

   public org.w3c.dom.Node getStartingRULECursor() throws Exception {
      return this.getDomFirstChild(0, (String)null, "RULE");
   }

   public org.w3c.dom.Node getAdvancedRULECursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(0, (String)null, "RULE", curNode);
   }

   public SchemaString getRULEValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getRULE() throws Exception {
      return this.getRULEAt(0);
   }

   public void removeRULEAt(int index) {
      this.removeDomChildAt(0, (String)null, "RULE", index);
   }

   public void removeRULE() {
      while(this.hasRULE()) {
         this.removeRULEAt(0);
      }

   }

   public void addRULE(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(0, (String)null, "RULE", value.toString());
      }

   }

   public void addRULE(String value) throws Exception {
      this.addRULE(new SchemaString(value));
   }

   public void insertRULEAt(SchemaString value, int index) {
      this.insertDomChildAt(0, (String)null, "RULE", index, value.toString());
   }

   public void insertRULEAt(String value, int index) throws Exception {
      this.insertRULEAt(new SchemaString(value), index);
   }

   public void replaceRULEAt(SchemaString value, int index) {
      this.replaceDomChildAt(0, (String)null, "RULE", index, value.toString());
   }

   public void replaceRULEAt(String value, int index) throws Exception {
      this.replaceRULEAt(new SchemaString(value), index);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
