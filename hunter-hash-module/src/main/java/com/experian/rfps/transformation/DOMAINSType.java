package com.experian.rfps.transformation;

import com.altova.xml.Node;
import com.altova.xml.XmlException;
import com.experian.rfps.transformation.DOMAINType;
import org.w3c.dom.Document;

public class DOMAINSType extends Node {
   public DOMAINSType(DOMAINSType node) {
      super((Node)node);
   }

   public DOMAINSType(org.w3c.dom.Node node) {
      super(node);
   }

   public DOMAINSType(Document doc) {
      super(doc);
   }

   public DOMAINSType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      for(org.w3c.dom.Node tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "DOMAIN"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "DOMAIN", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new DOMAINType(tmpNode)).adjustPrefix();
      }

   }

   public static int getDOMAINMinCount() {
      return 1;
   }

   public static int getDOMAINMaxCount() {
      return Integer.MAX_VALUE;
   }

   public int getDOMAINCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "DOMAIN");
   }

   public boolean hasDOMAIN() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "DOMAIN");
   }

   public DOMAINType newDOMAIN() {
      return new DOMAINType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "DOMAIN"));
   }

   public DOMAINType getDOMAINAt(int index) throws Exception {
      return new DOMAINType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "DOMAIN", index)));
   }

   public org.w3c.dom.Node getStartingDOMAINCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "DOMAIN");
   }

   public org.w3c.dom.Node getAdvancedDOMAINCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "DOMAIN", curNode);
   }

   public DOMAINType getDOMAINValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new DOMAINType(this.dereference(curNode));
      }
   }

   public DOMAINType getDOMAIN() throws Exception {
      return this.getDOMAINAt(0);
   }

   public void removeDOMAINAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "DOMAIN", index);
   }

   public void removeDOMAIN() {
      while(this.hasDOMAIN()) {
         this.removeDOMAINAt(0);
      }

   }

   public void addDOMAIN(DOMAINType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "DOMAIN", value);
   }

   public void insertDOMAINAt(DOMAINType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "DOMAIN", index, value);
   }

   public void replaceDOMAINAt(DOMAINType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "DOMAIN", index, value);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
