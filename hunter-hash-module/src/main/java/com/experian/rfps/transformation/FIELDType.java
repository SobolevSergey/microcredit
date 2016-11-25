package com.experian.rfps.transformation;

import com.altova.types.SchemaInt;
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

      for(tmpNode = this.getDomFirstChild(0, (String)null, "TYPE"); tmpNode != null; tmpNode = this.getDomNextChild(0, (String)null, "TYPE", tmpNode)) {
         internalAdjustPrefix(tmpNode, false);
      }

      for(tmpNode = this.getDomFirstChild(0, (String)null, "ORDER"); tmpNode != null; tmpNode = this.getDomNextChild(0, (String)null, "ORDER", tmpNode)) {
         internalAdjustPrefix(tmpNode, false);
      }

      for(tmpNode = this.getDomFirstChild(0, (String)null, "MAPPING"); tmpNode != null; tmpNode = this.getDomNextChild(0, (String)null, "MAPPING", tmpNode)) {
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

   public static int getORDERMinCount() {
      return 1;
   }

   public static int getORDERMaxCount() {
      return 1;
   }

   public int getORDERCount() {
      return this.getDomChildCount(0, (String)null, "ORDER");
   }

   public boolean hasORDER() {
      return this.hasDomChild(0, (String)null, "ORDER");
   }

   public SchemaInt newORDER() {
      return new SchemaInt();
   }

   public SchemaInt getORDERAt(int index) throws Exception {
      return new SchemaInt(getDomNodeValue(this.dereference(this.getDomChildAt(0, (String)null, "ORDER", index))));
   }

   public org.w3c.dom.Node getStartingORDERCursor() throws Exception {
      return this.getDomFirstChild(0, (String)null, "ORDER");
   }

   public org.w3c.dom.Node getAdvancedORDERCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(0, (String)null, "ORDER", curNode);
   }

   public SchemaInt getORDERValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaInt(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaInt getORDER() throws Exception {
      return this.getORDERAt(0);
   }

   public void removeORDERAt(int index) {
      this.removeDomChildAt(0, (String)null, "ORDER", index);
   }

   public void removeORDER() {
      while(this.hasORDER()) {
         this.removeORDERAt(0);
      }

   }

   public void addORDER(SchemaInt value) {
      if(!value.isNull()) {
         this.appendDomChild(0, (String)null, "ORDER", value.toString());
      }

   }

   public void addORDER(String value) throws Exception {
      this.addORDER(new SchemaInt(value));
   }

   public void insertORDERAt(SchemaInt value, int index) {
      this.insertDomChildAt(0, (String)null, "ORDER", index, value.toString());
   }

   public void insertORDERAt(String value, int index) throws Exception {
      this.insertORDERAt(new SchemaInt(value), index);
   }

   public void replaceORDERAt(SchemaInt value, int index) {
      this.replaceDomChildAt(0, (String)null, "ORDER", index, value.toString());
   }

   public void replaceORDERAt(String value, int index) throws Exception {
      this.replaceORDERAt(new SchemaInt(value), index);
   }

   public static int getMAPPINGMinCount() {
      return 1;
   }

   public static int getMAPPINGMaxCount() {
      return 1;
   }

   public int getMAPPINGCount() {
      return this.getDomChildCount(0, (String)null, "MAPPING");
   }

   public boolean hasMAPPING() {
      return this.hasDomChild(0, (String)null, "MAPPING");
   }

   public SchemaString newMAPPING() {
      return new SchemaString();
   }

   public SchemaString getMAPPINGAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(0, (String)null, "MAPPING", index))));
   }

   public org.w3c.dom.Node getStartingMAPPINGCursor() throws Exception {
      return this.getDomFirstChild(0, (String)null, "MAPPING");
   }

   public org.w3c.dom.Node getAdvancedMAPPINGCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(0, (String)null, "MAPPING", curNode);
   }

   public SchemaString getMAPPINGValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getMAPPING() throws Exception {
      return this.getMAPPINGAt(0);
   }

   public void removeMAPPINGAt(int index) {
      this.removeDomChildAt(0, (String)null, "MAPPING", index);
   }

   public void removeMAPPING() {
      while(this.hasMAPPING()) {
         this.removeMAPPINGAt(0);
      }

   }

   public void addMAPPING(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(0, (String)null, "MAPPING", value.toString());
      }

   }

   public void addMAPPING(String value) throws Exception {
      this.addMAPPING(new SchemaString(value));
   }

   public void insertMAPPINGAt(SchemaString value, int index) {
      this.insertDomChildAt(0, (String)null, "MAPPING", index, value.toString());
   }

   public void insertMAPPINGAt(String value, int index) throws Exception {
      this.insertMAPPINGAt(new SchemaString(value), index);
   }

   public void replaceMAPPINGAt(SchemaString value, int index) {
      this.replaceDomChildAt(0, (String)null, "MAPPING", index, value.toString());
   }

   public void replaceMAPPINGAt(String value, int index) throws Exception {
      this.replaceMAPPINGAt(new SchemaString(value), index);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
