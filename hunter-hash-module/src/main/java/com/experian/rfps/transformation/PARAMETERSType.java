package com.experian.rfps.transformation;

import com.altova.xml.Node;
import com.altova.xml.XmlException;
import com.experian.rfps.transformation.PARAMETERType;
import org.w3c.dom.Document;

public class PARAMETERSType extends Node {
   public PARAMETERSType(PARAMETERSType node) {
      super((Node)node);
   }

   public PARAMETERSType(org.w3c.dom.Node node) {
      super(node);
   }

   public PARAMETERSType(Document doc) {
      super(doc);
   }

   public PARAMETERSType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      for(org.w3c.dom.Node tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETER"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETER", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new PARAMETERType(tmpNode)).adjustPrefix();
      }

   }

   public static int getPARAMETERMinCount() {
      return 1;
   }

   public static int getPARAMETERMaxCount() {
      return Integer.MAX_VALUE;
   }

   public int getPARAMETERCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETER");
   }

   public boolean hasPARAMETER() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETER");
   }

   public PARAMETERType newPARAMETER() {
      return new PARAMETERType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "PARAMETER"));
   }

   public PARAMETERType getPARAMETERAt(int index) throws Exception {
      return new PARAMETERType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETER", index)));
   }

   public org.w3c.dom.Node getStartingPARAMETERCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETER");
   }

   public org.w3c.dom.Node getAdvancedPARAMETERCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETER", curNode);
   }

   public PARAMETERType getPARAMETERValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new PARAMETERType(this.dereference(curNode));
      }
   }

   public PARAMETERType getPARAMETER() throws Exception {
      return this.getPARAMETERAt(0);
   }

   public void removePARAMETERAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETER", index);
   }

   public void removePARAMETER() {
      while(this.hasPARAMETER()) {
         this.removePARAMETERAt(0);
      }

   }

   public void addPARAMETER(PARAMETERType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "PARAMETER", value);
   }

   public void insertPARAMETERAt(PARAMETERType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "PARAMETER", index, value);
   }

   public void replacePARAMETERAt(PARAMETERType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "PARAMETER", index, value);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
