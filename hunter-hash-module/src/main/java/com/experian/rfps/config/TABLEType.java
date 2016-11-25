package com.experian.rfps.config;

import com.altova.types.SchemaString;
import com.altova.xml.Node;
import com.altova.xml.XmlException;
import com.experian.rfps.config.FIELDSType;
import org.w3c.dom.Document;

public class TABLEType extends Node {
   public TABLEType(TABLEType node) {
      super((Node)node);
   }

   public TABLEType(org.w3c.dom.Node node) {
      super(node);
   }

   public TABLEType(Document doc) {
      super(doc);
   }

   public TABLEType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      org.w3c.dom.Node tmpNode;
      for(tmpNode = this.getDomFirstChild(0, (String)null, "NAME"); tmpNode != null; tmpNode = this.getDomNextChild(0, (String)null, "NAME", tmpNode)) {
         internalAdjustPrefix(tmpNode, false);
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "FIELDS"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "FIELDS", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new FIELDSType(tmpNode)).adjustPrefix();
      }

   }

   public static int getNAMEMinCount() {
      return 1;
   }

   public static int getNAMEMaxCount() {
      return 1;
   }

   public int getNAMECount() {
      return this.getDomChildCount(0, (String)null, "NAME");
   }

   public boolean hasNAME() {
      return this.hasDomChild(0, (String)null, "NAME");
   }

   public SchemaString newNAME() {
      return new SchemaString();
   }

   public SchemaString getNAMEAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(0, (String)null, "NAME", index))));
   }

   public org.w3c.dom.Node getStartingNAMECursor() throws Exception {
      return this.getDomFirstChild(0, (String)null, "NAME");
   }

   public org.w3c.dom.Node getAdvancedNAMECursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(0, (String)null, "NAME", curNode);
   }

   public SchemaString getNAMEValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getNAME() throws Exception {
      return this.getNAMEAt(0);
   }

   public void removeNAMEAt(int index) {
      this.removeDomChildAt(0, (String)null, "NAME", index);
   }

   public void removeNAME() {
      while(this.hasNAME()) {
         this.removeNAMEAt(0);
      }

   }

   public void addNAME(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(0, (String)null, "NAME", value.toString());
      }

   }

   public void addNAME(String value) throws Exception {
      this.addNAME(new SchemaString(value));
   }

   public void insertNAMEAt(SchemaString value, int index) {
      this.insertDomChildAt(0, (String)null, "NAME", index, value.toString());
   }

   public void insertNAMEAt(String value, int index) throws Exception {
      this.insertNAMEAt(new SchemaString(value), index);
   }

   public void replaceNAMEAt(SchemaString value, int index) {
      this.replaceDomChildAt(0, (String)null, "NAME", index, value.toString());
   }

   public void replaceNAMEAt(String value, int index) throws Exception {
      this.replaceNAMEAt(new SchemaString(value), index);
   }

   public static int getFIELDSMinCount() {
      return 1;
   }

   public static int getFIELDSMaxCount() {
      return 1;
   }

   public int getFIELDSCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "FIELDS");
   }

   public boolean hasFIELDS() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "FIELDS");
   }

   public FIELDSType newFIELDS() {
      return new FIELDSType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "FIELDS"));
   }

   public FIELDSType getFIELDSAt(int index) throws Exception {
      return new FIELDSType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "FIELDS", index)));
   }

   public org.w3c.dom.Node getStartingFIELDSCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "FIELDS");
   }

   public org.w3c.dom.Node getAdvancedFIELDSCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "FIELDS", curNode);
   }

   public FIELDSType getFIELDSValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new FIELDSType(this.dereference(curNode));
      }
   }

   public FIELDSType getFIELDS() throws Exception {
      return this.getFIELDSAt(0);
   }

   public void removeFIELDSAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "FIELDS", index);
   }

   public void removeFIELDS() {
      while(this.hasFIELDS()) {
         this.removeFIELDSAt(0);
      }

   }

   public void addFIELDS(FIELDSType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "FIELDS", value);
   }

   public void insertFIELDSAt(FIELDSType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "FIELDS", index, value);
   }

   public void replaceFIELDSAt(FIELDSType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "FIELDS", index, value);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
