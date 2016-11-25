package com.experian.rfps.config;

import com.altova.types.SchemaInteger;
import com.altova.xml.AnyTypeNode;
import com.altova.xml.Node;
import com.altova.xml.XmlException;
import com.experian.rfps.config.MAXROWSType;
import org.w3c.dom.Document;

public class OPERATIONType extends Node {
   public OPERATIONType(OPERATIONType node) {
      super((Node)node);
   }

   public OPERATIONType(org.w3c.dom.Node node) {
      super(node);
   }

   public OPERATIONType(Document doc) {
      super(doc);
   }

   public OPERATIONType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      org.w3c.dom.Node tmpNode;
      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "APILOG"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "APILOG", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "MAXROWS"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "MAXROWS", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "INTERVAL"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "INTERVAL", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

   }

   public static int getAPILOGMinCount() {
      return 0;
   }

   public static int getAPILOGMaxCount() {
      return 1;
   }

   public int getAPILOGCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "APILOG");
   }

   public boolean hasAPILOG() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "APILOG");
   }

   public AnyTypeNode newAPILOG() {
      return new AnyTypeNode(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "APILOG"));
   }

   public AnyTypeNode getAPILOGAt(int index) throws Exception {
      return new AnyTypeNode(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "APILOG", index)));
   }

   public org.w3c.dom.Node getStartingAPILOGCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "APILOG");
   }

   public org.w3c.dom.Node getAdvancedAPILOGCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "APILOG", curNode);
   }

   public AnyTypeNode getAPILOGValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new AnyTypeNode(this.dereference(curNode));
      }
   }

   public AnyTypeNode getAPILOG() throws Exception {
      return this.getAPILOGAt(0);
   }

   public void removeAPILOGAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "APILOG", index);
   }

   public void removeAPILOG() {
      while(this.hasAPILOG()) {
         this.removeAPILOGAt(0);
      }

   }

   public void addAPILOG(AnyTypeNode value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "APILOG", value);
   }

   public void insertAPILOGAt(AnyTypeNode value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "APILOG", index, value);
   }

   public void replaceAPILOGAt(AnyTypeNode value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "APILOG", index, value);
   }

   public static int getMAXROWSMinCount() {
      return 0;
   }

   public static int getMAXROWSMaxCount() {
      return 1;
   }

   public int getMAXROWSCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "MAXROWS");
   }

   public boolean hasMAXROWS() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "MAXROWS");
   }

   public MAXROWSType newMAXROWS() {
      return new MAXROWSType();
   }

   public MAXROWSType getMAXROWSAt(int index) throws Exception {
      return new MAXROWSType(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "MAXROWS", index))));
   }

   public org.w3c.dom.Node getStartingMAXROWSCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "MAXROWS");
   }

   public org.w3c.dom.Node getAdvancedMAXROWSCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "MAXROWS", curNode);
   }

   public MAXROWSType getMAXROWSValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new MAXROWSType(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public MAXROWSType getMAXROWS() throws Exception {
      return this.getMAXROWSAt(0);
   }

   public void removeMAXROWSAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "MAXROWS", index);
   }

   public void removeMAXROWS() {
      while(this.hasMAXROWS()) {
         this.removeMAXROWSAt(0);
      }

   }

   public void addMAXROWS(MAXROWSType value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "MAXROWS", value.toString());
      }

   }

   public void addMAXROWS(String value) throws Exception {
      this.addMAXROWS(new MAXROWSType(value));
   }

   public void insertMAXROWSAt(MAXROWSType value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "MAXROWS", index, value.toString());
   }

   public void insertMAXROWSAt(String value, int index) throws Exception {
      this.insertMAXROWSAt(new MAXROWSType(value), index);
   }

   public void replaceMAXROWSAt(MAXROWSType value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "MAXROWS", index, value.toString());
   }

   public void replaceMAXROWSAt(String value, int index) throws Exception {
      this.replaceMAXROWSAt(new MAXROWSType(value), index);
   }

   public static int getINTERVALMinCount() {
      return 0;
   }

   public static int getINTERVALMaxCount() {
      return 1;
   }

   public int getINTERVALCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "INTERVAL");
   }

   public boolean hasINTERVAL() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "INTERVAL");
   }

   public SchemaInteger newINTERVAL() {
      return new SchemaInteger();
   }

   public SchemaInteger getINTERVALAt(int index) throws Exception {
      return new SchemaInteger(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "INTERVAL", index))));
   }

   public org.w3c.dom.Node getStartingINTERVALCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "INTERVAL");
   }

   public org.w3c.dom.Node getAdvancedINTERVALCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "INTERVAL", curNode);
   }

   public SchemaInteger getINTERVALValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaInteger(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaInteger getINTERVAL() throws Exception {
      return this.getINTERVALAt(0);
   }

   public void removeINTERVALAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "INTERVAL", index);
   }

   public void removeINTERVAL() {
      while(this.hasINTERVAL()) {
         this.removeINTERVALAt(0);
      }

   }

   public void addINTERVAL(SchemaInteger value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "INTERVAL", value.toString());
      }

   }

   public void addINTERVAL(String value) throws Exception {
      this.addINTERVAL(new SchemaInteger(value));
   }

   public void insertINTERVALAt(SchemaInteger value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "INTERVAL", index, value.toString());
   }

   public void insertINTERVALAt(String value, int index) throws Exception {
      this.insertINTERVALAt(new SchemaInteger(value), index);
   }

   public void replaceINTERVALAt(SchemaInteger value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "INTERVAL", index, value.toString());
   }

   public void replaceINTERVALAt(String value, int index) throws Exception {
      this.replaceINTERVALAt(new SchemaInteger(value), index);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
