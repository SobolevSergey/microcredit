package com.experian.rfps.config;

import com.altova.types.SchemaString;
import com.altova.xml.Node;
import com.altova.xml.XmlException;
import org.w3c.dom.Document;

public class INPUTFILEType extends Node {
   public INPUTFILEType(INPUTFILEType node) {
      super((Node)node);
   }

   public INPUTFILEType(org.w3c.dom.Node node) {
      super(node);
   }

   public INPUTFILEType(Document doc) {
      super(doc);
   }

   public INPUTFILEType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      org.w3c.dom.Node tmpNode;
      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "PATH"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "PATH", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "ARCHIVE"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "ARCHIVE", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "ERROR"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "ERROR", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "LOG"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "LOG", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

   }

   public static int getPATHMinCount() {
      return 1;
   }

   public static int getPATHMaxCount() {
      return 1;
   }

   public int getPATHCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "PATH");
   }

   public boolean hasPATH() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "PATH");
   }

   public SchemaString newPATH() {
      return new SchemaString();
   }

   public SchemaString getPATHAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PATH", index))));
   }

   public org.w3c.dom.Node getStartingPATHCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "PATH");
   }

   public org.w3c.dom.Node getAdvancedPATHCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "PATH", curNode);
   }

   public SchemaString getPATHValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getPATH() throws Exception {
      return this.getPATHAt(0);
   }

   public void removePATHAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PATH", index);
   }

   public void removePATH() {
      while(this.hasPATH()) {
         this.removePATHAt(0);
      }

   }

   public void addPATH(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "PATH", value.toString());
      }

   }

   public void addPATH(String value) throws Exception {
      this.addPATH(new SchemaString(value));
   }

   public void insertPATHAt(SchemaString value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PATH", index, value.toString());
   }

   public void insertPATHAt(String value, int index) throws Exception {
      this.insertPATHAt(new SchemaString(value), index);
   }

   public void replacePATHAt(SchemaString value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PATH", index, value.toString());
   }

   public void replacePATHAt(String value, int index) throws Exception {
      this.replacePATHAt(new SchemaString(value), index);
   }

   public static int getARCHIVEMinCount() {
      return 1;
   }

   public static int getARCHIVEMaxCount() {
      return 1;
   }

   public int getARCHIVECount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "ARCHIVE");
   }

   public boolean hasARCHIVE() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "ARCHIVE");
   }

   public SchemaString newARCHIVE() {
      return new SchemaString();
   }

   public SchemaString getARCHIVEAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "ARCHIVE", index))));
   }

   public org.w3c.dom.Node getStartingARCHIVECursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "ARCHIVE");
   }

   public org.w3c.dom.Node getAdvancedARCHIVECursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "ARCHIVE", curNode);
   }

   public SchemaString getARCHIVEValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getARCHIVE() throws Exception {
      return this.getARCHIVEAt(0);
   }

   public void removeARCHIVEAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "ARCHIVE", index);
   }

   public void removeARCHIVE() {
      while(this.hasARCHIVE()) {
         this.removeARCHIVEAt(0);
      }

   }

   public void addARCHIVE(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "ARCHIVE", value.toString());
      }

   }

   public void addARCHIVE(String value) throws Exception {
      this.addARCHIVE(new SchemaString(value));
   }

   public void insertARCHIVEAt(SchemaString value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "ARCHIVE", index, value.toString());
   }

   public void insertARCHIVEAt(String value, int index) throws Exception {
      this.insertARCHIVEAt(new SchemaString(value), index);
   }

   public void replaceARCHIVEAt(SchemaString value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "ARCHIVE", index, value.toString());
   }

   public void replaceARCHIVEAt(String value, int index) throws Exception {
      this.replaceARCHIVEAt(new SchemaString(value), index);
   }

   public static int getERRORMinCount() {
      return 1;
   }

   public static int getERRORMaxCount() {
      return 1;
   }

   public int getERRORCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "ERROR");
   }

   public boolean hasERROR() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "ERROR");
   }

   public SchemaString newERROR() {
      return new SchemaString();
   }

   public SchemaString getERRORAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "ERROR", index))));
   }

   public org.w3c.dom.Node getStartingERRORCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "ERROR");
   }

   public org.w3c.dom.Node getAdvancedERRORCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "ERROR", curNode);
   }

   public SchemaString getERRORValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getERROR() throws Exception {
      return this.getERRORAt(0);
   }

   public void removeERRORAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "ERROR", index);
   }

   public void removeERROR() {
      while(this.hasERROR()) {
         this.removeERRORAt(0);
      }

   }

   public void addERROR(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "ERROR", value.toString());
      }

   }

   public void addERROR(String value) throws Exception {
      this.addERROR(new SchemaString(value));
   }

   public void insertERRORAt(SchemaString value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "ERROR", index, value.toString());
   }

   public void insertERRORAt(String value, int index) throws Exception {
      this.insertERRORAt(new SchemaString(value), index);
   }

   public void replaceERRORAt(SchemaString value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "ERROR", index, value.toString());
   }

   public void replaceERRORAt(String value, int index) throws Exception {
      this.replaceERRORAt(new SchemaString(value), index);
   }

   public static int getLOGMinCount() {
      return 1;
   }

   public static int getLOGMaxCount() {
      return 1;
   }

   public int getLOGCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "LOG");
   }

   public boolean hasLOG() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "LOG");
   }

   public SchemaString newLOG() {
      return new SchemaString();
   }

   public SchemaString getLOGAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "LOG", index))));
   }

   public org.w3c.dom.Node getStartingLOGCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "LOG");
   }

   public org.w3c.dom.Node getAdvancedLOGCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "LOG", curNode);
   }

   public SchemaString getLOGValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getLOG() throws Exception {
      return this.getLOGAt(0);
   }

   public void removeLOGAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "LOG", index);
   }

   public void removeLOG() {
      while(this.hasLOG()) {
         this.removeLOGAt(0);
      }

   }

   public void addLOG(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "LOG", value.toString());
      }

   }

   public void addLOG(String value) throws Exception {
      this.addLOG(new SchemaString(value));
   }

   public void insertLOGAt(SchemaString value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "LOG", index, value.toString());
   }

   public void insertLOGAt(String value, int index) throws Exception {
      this.insertLOGAt(new SchemaString(value), index);
   }

   public void replaceLOGAt(SchemaString value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "LOG", index, value.toString());
   }

   public void replaceLOGAt(String value, int index) throws Exception {
      this.replaceLOGAt(new SchemaString(value), index);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
