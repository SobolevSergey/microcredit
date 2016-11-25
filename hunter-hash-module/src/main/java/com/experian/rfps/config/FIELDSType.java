package com.experian.rfps.config;

import com.altova.xml.Node;
import com.altova.xml.XmlException;
import com.experian.rfps.config.FIELDType;
import org.w3c.dom.Document;

public class FIELDSType extends Node {
   public FIELDSType(FIELDSType node) {
      super((Node)node);
   }

   public FIELDSType(org.w3c.dom.Node node) {
      super(node);
   }

   public FIELDSType(Document doc) {
      super(doc);
   }

   public FIELDSType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      for(org.w3c.dom.Node tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "FIELD"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "FIELD", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new FIELDType(tmpNode)).adjustPrefix();
      }

   }

   public static int getFIELDMinCount() {
      return 0;
   }

   public static int getFIELDMaxCount() {
      return Integer.MAX_VALUE;
   }

   public int getFIELDCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "FIELD");
   }

   public boolean hasFIELD() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "FIELD");
   }

   public FIELDType newFIELD() {
      return new FIELDType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "FIELD"));
   }

   public FIELDType getFIELDAt(int index) throws Exception {
      return new FIELDType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "FIELD", index)));
   }

   public org.w3c.dom.Node getStartingFIELDCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "FIELD");
   }

   public org.w3c.dom.Node getAdvancedFIELDCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "FIELD", curNode);
   }

   public FIELDType getFIELDValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new FIELDType(this.dereference(curNode));
      }
   }

   public FIELDType getFIELD() throws Exception {
      return this.getFIELDAt(0);
   }

   public void removeFIELDAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "FIELD", index);
   }

   public void removeFIELD() {
      while(this.hasFIELD()) {
         this.removeFIELDAt(0);
      }

   }

   public void addFIELD(FIELDType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "FIELD", value);
   }

   public void insertFIELDAt(FIELDType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "FIELD", index, value);
   }

   public void replaceFIELDAt(FIELDType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "FIELD", index, value);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
