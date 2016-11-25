package com.experian.rfps.config;

import com.altova.types.SchemaString;
import com.altova.xml.Node;
import com.altova.xml.XmlException;
import org.w3c.dom.Document;

public class OUTPUTFILEType extends Node {
   public OUTPUTFILEType(OUTPUTFILEType node) {
      super((Node)node);
   }

   public OUTPUTFILEType(org.w3c.dom.Node node) {
      super(node);
   }

   public OUTPUTFILEType(Document doc) {
      super(doc);
   }

   public OUTPUTFILEType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      for(org.w3c.dom.Node tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "PATH"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "PATH", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

   }

   public static int getPATHMinCount() {
      return 1;
   }

   public static int getPATHMaxCount() {
      return 1;
   }

   public int getPATHCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "PATH");
   }

   public boolean hasPATH() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "PATH");
   }

   public SchemaString newPATH() {
      return new SchemaString();
   }

   public SchemaString getPATHAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PATH", index))));
   }

   public org.w3c.dom.Node getStartingPATHCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "PATH");
   }

   public org.w3c.dom.Node getAdvancedPATHCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "PATH", curNode);
   }

   public SchemaString getPATHValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getPATH() throws Exception {
      return this.getPATHAt(0);
   }

   public void removePATHAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PATH", index);
   }

   public void removePATH() {
      while(this.hasPATH()) {
         this.removePATHAt(0);
      }

   }

   public void addPATH(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "PATH", value.toString());
      }

   }

   public void addPATH(String value) throws Exception {
      this.addPATH(new SchemaString(value));
   }

   public void insertPATHAt(SchemaString value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PATH", index, value.toString());
   }

   public void insertPATHAt(String value, int index) throws Exception {
      this.insertPATHAt(new SchemaString(value), index);
   }

   public void replacePATHAt(SchemaString value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PATH", index, value.toString());
   }

   public void replacePATHAt(String value, int index) throws Exception {
      this.replacePATHAt(new SchemaString(value), index);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
