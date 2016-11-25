package com.experian.rfps.config;

import com.altova.types.SchemaString;
import com.altova.xml.Node;
import com.altova.xml.XmlException;
import org.w3c.dom.Document;

public class FIELDType extends Node {
   public FIELDType(FIELDType node) {
      super((Node)node);
   }

   public FIELDType(org.w3c.dom.Node node) {
      super(node);
   }

   public FIELDType(Document doc) {
      super(doc);
   }

   public FIELDType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      org.w3c.dom.Node tmpNode;
      for(tmpNode = this.getDomFirstChild(0, (String)null, "NAME"); tmpNode != null; tmpNode = this.getDomNextChild(0, (String)null, "NAME", tmpNode)) {
         internalAdjustPrefix(tmpNode, false);
      }

      for(tmpNode = this.getDomFirstChild(0, (String)null, "VALUE"); tmpNode != null; tmpNode = this.getDomNextChild(0, (String)null, "VALUE", tmpNode)) {
         internalAdjustPrefix(tmpNode, false);
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

   public static int getVALUE2MinCount() {
      return 1;
   }

   public static int getVALUE2MaxCount() {
      return 1;
   }

   public int getVALUE2Count() {
      return this.getDomChildCount(0, (String)null, "VALUE");
   }

   public boolean hasVALUE2() {
      return this.hasDomChild(0, (String)null, "VALUE");
   }

   public SchemaString newVALUE2() {
      return new SchemaString();
   }

   public SchemaString getVALUE2At(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(0, (String)null, "VALUE", index))));
   }

   public org.w3c.dom.Node getStartingVALUE2Cursor() throws Exception {
      return this.getDomFirstChild(0, (String)null, "VALUE");
   }

   public org.w3c.dom.Node getAdvancedVALUE2Cursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(0, (String)null, "VALUE", curNode);
   }

   public SchemaString getVALUE2ValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getVALUE2() throws Exception {
      return this.getVALUE2At(0);
   }

   public void removeVALUE2At(int index) {
      this.removeDomChildAt(0, (String)null, "VALUE", index);
   }

   public void removeVALUE2() {
      while(this.hasVALUE2()) {
         this.removeVALUE2At(0);
      }

   }

   public void addVALUE2(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(0, (String)null, "VALUE", value.toString());
      }

   }

   public void addVALUE2(String value) throws Exception {
      this.addVALUE2(new SchemaString(value));
   }

   public void insertVALUE2At(SchemaString value, int index) {
      this.insertDomChildAt(0, (String)null, "VALUE", index, value.toString());
   }

   public void insertVALUE2At(String value, int index) throws Exception {
      this.insertVALUE2At(new SchemaString(value), index);
   }

   public void replaceVALUE2At(SchemaString value, int index) {
      this.replaceDomChildAt(0, (String)null, "VALUE", index, value.toString());
   }

   public void replaceVALUE2At(String value, int index) throws Exception {
      this.replaceVALUE2At(new SchemaString(value), index);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
