package com.experian.rfps.transformation;

import com.altova.types.SchemaString;
import com.altova.xml.Node;
import com.altova.xml.XmlException;
import org.w3c.dom.Document;

public class XSLType extends Node {
   public XSLType(XSLType node) {
      super((Node)node);
   }

   public XSLType(org.w3c.dom.Node node) {
      super(node);
   }

   public XSLType(Document doc) {
      super(doc);
   }

   public XSLType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      org.w3c.dom.Node tmpNode;
      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "CSV_INPUTXML"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "CSV_INPUTXML", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUTXML_OUTPUTXML"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUTXML_OUTPUTXML", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

   }

   public static int getCSV_INPUTXMLMinCount() {
      return 1;
   }

   public static int getCSV_INPUTXMLMaxCount() {
      return 1;
   }

   public int getCSV_INPUTXMLCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "CSV_INPUTXML");
   }

   public boolean hasCSV_INPUTXML() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "CSV_INPUTXML");
   }

   public SchemaString newCSV_INPUTXML() {
      return new SchemaString();
   }

   public SchemaString getCSV_INPUTXMLAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "CSV_INPUTXML", index))));
   }

   public org.w3c.dom.Node getStartingCSV_INPUTXMLCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "CSV_INPUTXML");
   }

   public org.w3c.dom.Node getAdvancedCSV_INPUTXMLCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "CSV_INPUTXML", curNode);
   }

   public SchemaString getCSV_INPUTXMLValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getCSV_INPUTXML() throws Exception {
      return this.getCSV_INPUTXMLAt(0);
   }

   public void removeCSV_INPUTXMLAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "CSV_INPUTXML", index);
   }

   public void removeCSV_INPUTXML() {
      while(this.hasCSV_INPUTXML()) {
         this.removeCSV_INPUTXMLAt(0);
      }

   }

   public void addCSV_INPUTXML(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "CSV_INPUTXML", value.toString());
      }

   }

   public void addCSV_INPUTXML(String value) throws Exception {
      this.addCSV_INPUTXML(new SchemaString(value));
   }

   public void insertCSV_INPUTXMLAt(SchemaString value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "CSV_INPUTXML", index, value.toString());
   }

   public void insertCSV_INPUTXMLAt(String value, int index) throws Exception {
      this.insertCSV_INPUTXMLAt(new SchemaString(value), index);
   }

   public void replaceCSV_INPUTXMLAt(SchemaString value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "CSV_INPUTXML", index, value.toString());
   }

   public void replaceCSV_INPUTXMLAt(String value, int index) throws Exception {
      this.replaceCSV_INPUTXMLAt(new SchemaString(value), index);
   }

   public static int getINPUTXML_OUTPUTXMLMinCount() {
      return 1;
   }

   public static int getINPUTXML_OUTPUTXMLMaxCount() {
      return 1;
   }

   public int getINPUTXML_OUTPUTXMLCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "INPUTXML_OUTPUTXML");
   }

   public boolean hasINPUTXML_OUTPUTXML() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUTXML_OUTPUTXML");
   }

   public SchemaString newINPUTXML_OUTPUTXML() {
      return new SchemaString();
   }

   public SchemaString getINPUTXML_OUTPUTXMLAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "INPUTXML_OUTPUTXML", index))));
   }

   public org.w3c.dom.Node getStartingINPUTXML_OUTPUTXMLCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUTXML_OUTPUTXML");
   }

   public org.w3c.dom.Node getAdvancedINPUTXML_OUTPUTXMLCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUTXML_OUTPUTXML", curNode);
   }

   public SchemaString getINPUTXML_OUTPUTXMLValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getINPUTXML_OUTPUTXML() throws Exception {
      return this.getINPUTXML_OUTPUTXMLAt(0);
   }

   public void removeINPUTXML_OUTPUTXMLAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "INPUTXML_OUTPUTXML", index);
   }

   public void removeINPUTXML_OUTPUTXML() {
      while(this.hasINPUTXML_OUTPUTXML()) {
         this.removeINPUTXML_OUTPUTXMLAt(0);
      }

   }

   public void addINPUTXML_OUTPUTXML(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUTXML_OUTPUTXML", value.toString());
      }

   }

   public void addINPUTXML_OUTPUTXML(String value) throws Exception {
      this.addINPUTXML_OUTPUTXML(new SchemaString(value));
   }

   public void insertINPUTXML_OUTPUTXMLAt(SchemaString value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "INPUTXML_OUTPUTXML", index, value.toString());
   }

   public void insertINPUTXML_OUTPUTXMLAt(String value, int index) throws Exception {
      this.insertINPUTXML_OUTPUTXMLAt(new SchemaString(value), index);
   }

   public void replaceINPUTXML_OUTPUTXMLAt(SchemaString value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "INPUTXML_OUTPUTXML", index, value.toString());
   }

   public void replaceINPUTXML_OUTPUTXMLAt(String value, int index) throws Exception {
      this.replaceINPUTXML_OUTPUTXMLAt(new SchemaString(value), index);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
