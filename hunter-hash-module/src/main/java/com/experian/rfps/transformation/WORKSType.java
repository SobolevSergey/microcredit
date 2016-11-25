package com.experian.rfps.transformation;

import com.altova.xml.Node;
import com.altova.xml.XmlException;
import com.experian.rfps.transformation.STEPType;
import org.w3c.dom.Document;

public class WORKSType extends Node {
   public WORKSType(WORKSType node) {
      super((Node)node);
   }

   public WORKSType(org.w3c.dom.Node node) {
      super(node);
   }

   public WORKSType(Document doc) {
      super(doc);
   }

   public WORKSType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      for(org.w3c.dom.Node tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "STEP"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "STEP", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new STEPType(tmpNode)).adjustPrefix();
      }

   }

   public static int getSTEPMinCount() {
      return 0;
   }

   public static int getSTEPMaxCount() {
      return Integer.MAX_VALUE;
   }

   public int getSTEPCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "STEP");
   }

   public boolean hasSTEP() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "STEP");
   }

   public STEPType newSTEP() {
      return new STEPType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "STEP"));
   }

   public STEPType getSTEPAt(int index) throws Exception {
      return new STEPType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "STEP", index)));
   }

   public org.w3c.dom.Node getStartingSTEPCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "STEP");
   }

   public org.w3c.dom.Node getAdvancedSTEPCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "STEP", curNode);
   }

   public STEPType getSTEPValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new STEPType(this.dereference(curNode));
      }
   }

   public STEPType getSTEP() throws Exception {
      return this.getSTEPAt(0);
   }

   public void removeSTEPAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "STEP", index);
   }

   public void removeSTEP() {
      while(this.hasSTEP()) {
         this.removeSTEPAt(0);
      }

   }

   public void addSTEP(STEPType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "STEP", value);
   }

   public void insertSTEPAt(STEPType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "STEP", index, value);
   }

   public void replaceSTEPAt(STEPType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "STEP", index, value);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
