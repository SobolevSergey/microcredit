package com.experian.rfps.config;

import com.altova.types.SchemaString;
import com.altova.xml.Node;
import com.altova.xml.XmlException;
import com.experian.rfps.config.CREATIONType;
import org.w3c.dom.Document;

public class LOGType extends Node {
   public LOGType(LOGType node) {
      super((Node)node);
   }

   public LOGType(org.w3c.dom.Node node) {
      super(node);
   }

   public LOGType(Document doc) {
      super(doc);
   }

   public LOGType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      org.w3c.dom.Node tmpNode;
      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "PATH"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "PATH", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "PREFIX"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "PREFIX", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "CREATION"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "CREATION", tmpNode)) {
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

   public static int getPREFIXMinCount() {
      return 1;
   }

   public static int getPREFIXMaxCount() {
      return 1;
   }

   public int getPREFIXCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "PREFIX");
   }

   public boolean hasPREFIX() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "PREFIX");
   }

   public SchemaString newPREFIX() {
      return new SchemaString();
   }

   public SchemaString getPREFIXAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PREFIX", index))));
   }

   public org.w3c.dom.Node getStartingPREFIXCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "PREFIX");
   }

   public org.w3c.dom.Node getAdvancedPREFIXCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "PREFIX", curNode);
   }

   public SchemaString getPREFIXValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getPREFIX() throws Exception {
      return this.getPREFIXAt(0);
   }

   public void removePREFIXAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PREFIX", index);
   }

   public void removePREFIX() {
      while(this.hasPREFIX()) {
         this.removePREFIXAt(0);
      }

   }

   public void addPREFIX(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "PREFIX", value.toString());
      }

   }

   public void addPREFIX(String value) throws Exception {
      this.addPREFIX(new SchemaString(value));
   }

   public void insertPREFIXAt(SchemaString value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PREFIX", index, value.toString());
   }

   public void insertPREFIXAt(String value, int index) throws Exception {
      this.insertPREFIXAt(new SchemaString(value), index);
   }

   public void replacePREFIXAt(SchemaString value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PREFIX", index, value.toString());
   }

   public void replacePREFIXAt(String value, int index) throws Exception {
      this.replacePREFIXAt(new SchemaString(value), index);
   }

   public static int getCREATIONMinCount() {
      return 1;
   }

   public static int getCREATIONMaxCount() {
      return 1;
   }

   public int getCREATIONCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "CREATION");
   }

   public boolean hasCREATION() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "CREATION");
   }

   public CREATIONType newCREATION() {
      return new CREATIONType();
   }

   public CREATIONType getCREATIONAt(int index) throws Exception {
      return new CREATIONType(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "CREATION", index))));
   }

   public org.w3c.dom.Node getStartingCREATIONCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "CREATION");
   }

   public org.w3c.dom.Node getAdvancedCREATIONCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "CREATION", curNode);
   }

   public CREATIONType getCREATIONValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new CREATIONType(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public CREATIONType getCREATION() throws Exception {
      return this.getCREATIONAt(0);
   }

   public void removeCREATIONAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "CREATION", index);
   }

   public void removeCREATION() {
      while(this.hasCREATION()) {
         this.removeCREATIONAt(0);
      }

   }

   public void addCREATION(CREATIONType value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "CREATION", value.toString());
      }

   }

   public void addCREATION(String value) throws Exception {
      this.addCREATION(new CREATIONType(value));
   }

   public void insertCREATIONAt(CREATIONType value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "CREATION", index, value.toString());
   }

   public void insertCREATIONAt(String value, int index) throws Exception {
      this.insertCREATIONAt(new CREATIONType(value), index);
   }

   public void replaceCREATIONAt(CREATIONType value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "CREATION", index, value.toString());
   }

   public void replaceCREATIONAt(String value, int index) throws Exception {
      this.replaceCREATIONAt(new CREATIONType(value), index);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
