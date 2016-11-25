package com.experian.rfps.transformation;

import com.altova.xml.Node;
import com.altova.xml.XmlException;
import com.experian.rfps.transformation.DICTIONARYType;
import org.w3c.dom.Document;

public class DICTIONARIESType extends Node {
   public DICTIONARIESType(DICTIONARIESType node) {
      super((Node)node);
   }

   public DICTIONARIESType(org.w3c.dom.Node node) {
      super(node);
   }

   public DICTIONARIESType(Document doc) {
      super(doc);
   }

   public DICTIONARIESType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      for(org.w3c.dom.Node tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARY"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARY", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new DICTIONARYType(tmpNode)).adjustPrefix();
      }

   }

   public static int getDICTIONARYMinCount() {
      return 0;
   }

   public static int getDICTIONARYMaxCount() {
      return Integer.MAX_VALUE;
   }

   public int getDICTIONARYCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARY");
   }

   public boolean hasDICTIONARY() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARY");
   }

   public DICTIONARYType newDICTIONARY() {
      return new DICTIONARYType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "DICTIONARY"));
   }

   public DICTIONARYType getDICTIONARYAt(int index) throws Exception {
      return new DICTIONARYType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARY", index)));
   }

   public org.w3c.dom.Node getStartingDICTIONARYCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARY");
   }

   public org.w3c.dom.Node getAdvancedDICTIONARYCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARY", curNode);
   }

   public DICTIONARYType getDICTIONARYValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new DICTIONARYType(this.dereference(curNode));
      }
   }

   public DICTIONARYType getDICTIONARY() throws Exception {
      return this.getDICTIONARYAt(0);
   }

   public void removeDICTIONARYAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARY", index);
   }

   public void removeDICTIONARY() {
      while(this.hasDICTIONARY()) {
         this.removeDICTIONARYAt(0);
      }

   }

   public void addDICTIONARY(DICTIONARYType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "DICTIONARY", value);
   }

   public void insertDICTIONARYAt(DICTIONARYType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "DICTIONARY", index, value);
   }

   public void replaceDICTIONARYAt(DICTIONARYType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "DICTIONARY", index, value);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
