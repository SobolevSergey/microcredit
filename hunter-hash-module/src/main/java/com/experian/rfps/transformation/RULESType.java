package com.experian.rfps.transformation;

import com.altova.xml.Node;
import com.altova.xml.XmlException;
import com.experian.rfps.transformation.RULEType;
import org.w3c.dom.Document;

public class RULESType extends Node {
   public RULESType(RULESType node) {
      super((Node)node);
   }

   public RULESType(org.w3c.dom.Node node) {
      super(node);
   }

   public RULESType(Document doc) {
      super(doc);
   }

   public RULESType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      for(org.w3c.dom.Node tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "RULE"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "RULE", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new RULEType(tmpNode)).adjustPrefix();
      }

   }

   public static int getRULEMinCount() {
      return 0;
   }

   public static int getRULEMaxCount() {
      return Integer.MAX_VALUE;
   }

   public int getRULECount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "RULE");
   }

   public boolean hasRULE() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "RULE");
   }

   public RULEType newRULE() {
      return new RULEType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "RULE"));
   }

   public RULEType getRULEAt(int index) throws Exception {
      return new RULEType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "RULE", index)));
   }

   public org.w3c.dom.Node getStartingRULECursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "RULE");
   }

   public org.w3c.dom.Node getAdvancedRULECursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "RULE", curNode);
   }

   public RULEType getRULEValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new RULEType(this.dereference(curNode));
      }
   }

   public RULEType getRULE() throws Exception {
      return this.getRULEAt(0);
   }

   public void removeRULEAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "RULE", index);
   }

   public void removeRULE() {
      while(this.hasRULE()) {
         this.removeRULEAt(0);
      }

   }

   public void addRULE(RULEType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "RULE", value);
   }

   public void insertRULEAt(RULEType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "RULE", index, value);
   }

   public void replaceRULEAt(RULEType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "RULE", index, value);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
