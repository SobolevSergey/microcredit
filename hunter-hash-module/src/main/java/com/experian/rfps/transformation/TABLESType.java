package com.experian.rfps.transformation;

import com.altova.xml.Node;
import com.altova.xml.XmlException;
import com.experian.rfps.transformation.TABLEType;
import org.w3c.dom.Document;

public class TABLESType extends Node {
   public TABLESType(TABLESType node) {
      super((Node)node);
   }

   public TABLESType(org.w3c.dom.Node node) {
      super(node);
   }

   public TABLESType(Document doc) {
      super(doc);
   }

   public TABLESType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      for(org.w3c.dom.Node tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "TABLE"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "TABLE", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new TABLEType(tmpNode)).adjustPrefix();
      }

   }

   public static int getTABLEMinCount() {
      return 1;
   }

   public static int getTABLEMaxCount() {
      return Integer.MAX_VALUE;
   }

   public int getTABLECount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "TABLE");
   }

   public boolean hasTABLE() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "TABLE");
   }

   public TABLEType newTABLE() {
      return new TABLEType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "TABLE"));
   }

   public TABLEType getTABLEAt(int index) throws Exception {
      return new TABLEType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "TABLE", index)));
   }

   public org.w3c.dom.Node getStartingTABLECursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "TABLE");
   }

   public org.w3c.dom.Node getAdvancedTABLECursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "TABLE", curNode);
   }

   public TABLEType getTABLEValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new TABLEType(this.dereference(curNode));
      }
   }

   public TABLEType getTABLE() throws Exception {
      return this.getTABLEAt(0);
   }

   public void removeTABLEAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "TABLE", index);
   }

   public void removeTABLE() {
      while(this.hasTABLE()) {
         this.removeTABLEAt(0);
      }

   }

   public void addTABLE(TABLEType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "TABLE", value);
   }

   public void insertTABLEAt(TABLEType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "TABLE", index, value);
   }

   public void replaceTABLEAt(TABLEType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "TABLE", index, value);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
