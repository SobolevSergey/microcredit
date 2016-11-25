package com.experian.rfps.transformation;

import com.altova.types.SchemaString;
import com.altova.xml.Node;
import com.altova.xml.XmlException;
import org.w3c.dom.Document;

public class XSDType extends Node {
   public XSDType(XSDType node) {
      super((Node)node);
   }

   public XSDType(org.w3c.dom.Node node) {
      super(node);
   }

   public XSDType(Document doc) {
      super(doc);
   }

   public XSDType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      org.w3c.dom.Node tmpNode;
      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUT"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUT", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUT"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUT", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

   }

   public static int getOUTPUTMinCount() {
      return 1;
   }

   public static int getOUTPUTMaxCount() {
      return 1;
   }

   public int getOUTPUTCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUT");
   }

   public boolean hasOUTPUT() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUT");
   }

   public SchemaString newOUTPUT() {
      return new SchemaString();
   }

   public SchemaString getOUTPUTAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUT", index))));
   }

   public org.w3c.dom.Node getStartingOUTPUTCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUT");
   }

   public org.w3c.dom.Node getAdvancedOUTPUTCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUT", curNode);
   }

   public SchemaString getOUTPUTValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getOUTPUT() throws Exception {
      return this.getOUTPUTAt(0);
   }

   public void removeOUTPUTAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUT", index);
   }

   public void removeOUTPUT() {
      while(this.hasOUTPUT()) {
         this.removeOUTPUTAt(0);
      }

   }

   public void addOUTPUT(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUT", value.toString());
      }

   }

   public void addOUTPUT(String value) throws Exception {
      this.addOUTPUT(new SchemaString(value));
   }

   public void insertOUTPUTAt(SchemaString value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUT", index, value.toString());
   }

   public void insertOUTPUTAt(String value, int index) throws Exception {
      this.insertOUTPUTAt(new SchemaString(value), index);
   }

   public void replaceOUTPUTAt(SchemaString value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUT", index, value.toString());
   }

   public void replaceOUTPUTAt(String value, int index) throws Exception {
      this.replaceOUTPUTAt(new SchemaString(value), index);
   }

   public static int getINPUTMinCount() {
      return 1;
   }

   public static int getINPUTMaxCount() {
      return 1;
   }

   public int getINPUTCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "INPUT");
   }

   public boolean hasINPUT() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUT");
   }

   public SchemaString newINPUT() {
      return new SchemaString();
   }

   public SchemaString getINPUTAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "INPUT", index))));
   }

   public org.w3c.dom.Node getStartingINPUTCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUT");
   }

   public org.w3c.dom.Node getAdvancedINPUTCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUT", curNode);
   }

   public SchemaString getINPUTValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getINPUT() throws Exception {
      return this.getINPUTAt(0);
   }

   public void removeINPUTAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "INPUT", index);
   }

   public void removeINPUT() {
      while(this.hasINPUT()) {
         this.removeINPUTAt(0);
      }

   }

   public void addINPUT(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUT", value.toString());
      }

   }

   public void addINPUT(String value) throws Exception {
      this.addINPUT(new SchemaString(value));
   }

   public void insertINPUTAt(SchemaString value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "INPUT", index, value.toString());
   }

   public void insertINPUTAt(String value, int index) throws Exception {
      this.insertINPUTAt(new SchemaString(value), index);
   }

   public void replaceINPUTAt(SchemaString value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "INPUT", index, value.toString());
   }

   public void replaceINPUTAt(String value, int index) throws Exception {
      this.replaceINPUTAt(new SchemaString(value), index);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
