package com.experian.rfps.transformation;

import com.altova.types.SchemaString;
import com.altova.xml.Node;
import com.altova.xml.XmlException;
import com.experian.rfps.transformation.PARAMETERSType;
import org.w3c.dom.Document;

public class RULEType extends Node {
   public RULEType(RULEType node) {
      super((Node)node);
   }

   public RULEType(org.w3c.dom.Node node) {
      super(node);
   }

   public RULEType(Document doc) {
      super(doc);
   }

   public RULEType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      org.w3c.dom.Node tmpNode;
      for(tmpNode = this.getDomFirstChild(0, (String)null, "NAME"); tmpNode != null; tmpNode = this.getDomNextChild(0, (String)null, "NAME", tmpNode)) {
         internalAdjustPrefix(tmpNode, false);
      }

      for(tmpNode = this.getDomFirstChild(0, (String)null, "CLASS"); tmpNode != null; tmpNode = this.getDomNextChild(0, (String)null, "CLASS", tmpNode)) {
         internalAdjustPrefix(tmpNode, false);
      }

      for(tmpNode = this.getDomFirstChild(0, (String)null, "TYPE"); tmpNode != null; tmpNode = this.getDomNextChild(0, (String)null, "TYPE", tmpNode)) {
         internalAdjustPrefix(tmpNode, false);
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETERS"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETERS", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new PARAMETERSType(tmpNode)).adjustPrefix();
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

   public SchemaString getAttr(String attrName) throws Exception {
      return this.getAttrAt(0, attrName);
   }

   public boolean hasAttr(String attrName) {
      return this.hasDomChild(0, (String)null, attrName);
   }

   public SchemaString getAttrAt(int index, String attrName) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(0, (String)null, attrName, index))));
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

   public SchemaString getDESCRIPTION() throws Exception {
      return this.getAttr("DESCRIPTION");
   }

   public boolean hasDESCRIPTION() {
      return this.hasAttr("DESCRIPTION");
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

   public static int getCLASS2MinCount() {
      return 1;
   }

   public static int getCLASS2MaxCount() {
      return 1;
   }

   public int getCLASS2Count() {
      return this.getDomChildCount(0, (String)null, "CLASS");
   }

   public boolean hasCLASS2() {
      return this.hasDomChild(0, (String)null, "CLASS");
   }

   public SchemaString newCLASS2() {
      return new SchemaString();
   }

   public SchemaString getCLASS2At(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(0, (String)null, "CLASS", index))));
   }

   public org.w3c.dom.Node getStartingCLASS2Cursor() throws Exception {
      return this.getDomFirstChild(0, (String)null, "CLASS");
   }

   public org.w3c.dom.Node getAdvancedCLASS2Cursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(0, (String)null, "CLASS", curNode);
   }

   public SchemaString getCLASS2ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getCLASS2() throws Exception {
      return this.getCLASS2At(0);
   }

   public void removeCLASS2At(int index) {
      this.removeDomChildAt(0, (String)null, "CLASS", index);
   }

   public void removeCLASS2() {
      while(this.hasCLASS2()) {
         this.removeCLASS2At(0);
      }

   }

   public void addCLASS2(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(0, (String)null, "CLASS", value.toString());
      }

   }

   public void addCLASS2(String value) throws Exception {
      this.addCLASS2(new SchemaString(value));
   }

   public void insertCLASS2At(SchemaString value, int index) {
      this.insertDomChildAt(0, (String)null, "CLASS", index, value.toString());
   }

   public void insertCLASS2At(String value, int index) throws Exception {
      this.insertCLASS2At(new SchemaString(value), index);
   }

   public void replaceCLASS2At(SchemaString value, int index) {
      this.replaceDomChildAt(0, (String)null, "CLASS", index, value.toString());
   }

   public void replaceCLASS2At(String value, int index) throws Exception {
      this.replaceCLASS2At(new SchemaString(value), index);
   }

   public static int getTYPEMinCount() {
      return 1;
   }

   public static int getTYPEMaxCount() {
      return 1;
   }

   public int getTYPECount() {
      return this.getDomChildCount(0, (String)null, "TYPE");
   }

   public boolean hasTYPE() {
      return this.hasDomChild(0, (String)null, "TYPE");
   }

   public SchemaString newTYPE() {
      return new SchemaString();
   }

   public SchemaString getTYPEAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(0, (String)null, "TYPE", index))));
   }

   public org.w3c.dom.Node getStartingTYPECursor() throws Exception {
      return this.getDomFirstChild(0, (String)null, "TYPE");
   }

   public org.w3c.dom.Node getAdvancedTYPECursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(0, (String)null, "TYPE", curNode);
   }

   public SchemaString getTYPEValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getTYPE() throws Exception {
      return this.getTYPEAt(0);
   }

   public void removeTYPEAt(int index) {
      this.removeDomChildAt(0, (String)null, "TYPE", index);
   }

   public void removeTYPE() {
      while(this.hasTYPE()) {
         this.removeTYPEAt(0);
      }

   }

   public void addTYPE(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(0, (String)null, "TYPE", value.toString());
      }

   }

   public void addTYPE(String value) throws Exception {
      this.addTYPE(new SchemaString(value));
   }

   public void insertTYPEAt(SchemaString value, int index) {
      this.insertDomChildAt(0, (String)null, "TYPE", index, value.toString());
   }

   public void insertTYPEAt(String value, int index) throws Exception {
      this.insertTYPEAt(new SchemaString(value), index);
   }

   public void replaceTYPEAt(SchemaString value, int index) {
      this.replaceDomChildAt(0, (String)null, "TYPE", index, value.toString());
   }

   public void replaceTYPEAt(String value, int index) throws Exception {
      this.replaceTYPEAt(new SchemaString(value), index);
   }

   public static int getPARAMETERSMinCount() {
      return 0;
   }

   public static int getPARAMETERSMaxCount() {
      return 1;
   }

   public int getPARAMETERSCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETERS");
   }

   public boolean hasPARAMETERS() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETERS");
   }

   public PARAMETERSType newPARAMETERS() {
      return new PARAMETERSType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "PARAMETERS"));
   }

   public PARAMETERSType getPARAMETERSAt(int index) throws Exception {
      return new PARAMETERSType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETERS", index)));
   }

   public org.w3c.dom.Node getStartingPARAMETERSCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETERS");
   }

   public org.w3c.dom.Node getAdvancedPARAMETERSCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETERS", curNode);
   }

   public PARAMETERSType getPARAMETERSValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new PARAMETERSType(this.dereference(curNode));
      }
   }

   public PARAMETERSType getPARAMETERS() throws Exception {
      return this.getPARAMETERSAt(0);
   }

   public void removePARAMETERSAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PARAMETERS", index);
   }

   public void removePARAMETERS() {
      while(this.hasPARAMETERS()) {
         this.removePARAMETERSAt(0);
      }

   }

   public void addPARAMETERS(PARAMETERSType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "PARAMETERS", value);
   }

   public void insertPARAMETERSAt(PARAMETERSType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "PARAMETERS", index, value);
   }

   public void replacePARAMETERSAt(PARAMETERSType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "PARAMETERS", index, value);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
