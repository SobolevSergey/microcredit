package com.experian.rfps.config;

import com.altova.types.SchemaString;
import com.altova.xml.Node;
import com.altova.xml.XmlException;
import org.w3c.dom.Document;

public class XMLType extends Node {
   public XMLType(XMLType node) {
      super((Node)node);
   }

   public XMLType(org.w3c.dom.Node node) {
      super(node);
   }

   public XMLType(Document doc) {
      super(doc);
   }

   public XMLType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      for(org.w3c.dom.Node tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "TRANSFORMATION"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "TRANSFORMATION", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

   }

   public static int getTRANSFORMATIONMinCount() {
      return 1;
   }

   public static int getTRANSFORMATIONMaxCount() {
      return 1;
   }

   public int getTRANSFORMATIONCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "TRANSFORMATION");
   }

   public boolean hasTRANSFORMATION() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "TRANSFORMATION");
   }

   public SchemaString newTRANSFORMATION() {
      return new SchemaString();
   }

   public SchemaString getTRANSFORMATIONAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "TRANSFORMATION", index))));
   }

   public org.w3c.dom.Node getStartingTRANSFORMATIONCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "TRANSFORMATION");
   }

   public org.w3c.dom.Node getAdvancedTRANSFORMATIONCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "TRANSFORMATION", curNode);
   }

   public SchemaString getTRANSFORMATIONValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getTRANSFORMATION() throws Exception {
      return this.getTRANSFORMATIONAt(0);
   }

   public void removeTRANSFORMATIONAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "TRANSFORMATION", index);
   }

   public void removeTRANSFORMATION() {
      while(this.hasTRANSFORMATION()) {
         this.removeTRANSFORMATIONAt(0);
      }

   }

   public void addTRANSFORMATION(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "TRANSFORMATION", value.toString());
      }

   }

   public void addTRANSFORMATION(String value) throws Exception {
      this.addTRANSFORMATION(new SchemaString(value));
   }

   public void insertTRANSFORMATIONAt(SchemaString value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "TRANSFORMATION", index, value.toString());
   }

   public void insertTRANSFORMATIONAt(String value, int index) throws Exception {
      this.insertTRANSFORMATIONAt(new SchemaString(value), index);
   }

   public void replaceTRANSFORMATIONAt(SchemaString value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "TRANSFORMATION", index, value.toString());
   }

   public void replaceTRANSFORMATIONAt(String value, int index) throws Exception {
      this.replaceTRANSFORMATIONAt(new SchemaString(value), index);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
