package com.experian.rfps.config;

import com.altova.xml.Node;
import com.altova.xml.XmlException;
import com.experian.rfps.config.INPUTFILEType;
import com.experian.rfps.config.LOGType;
import com.experian.rfps.config.OUTPUTFILEType;
import org.w3c.dom.Document;

public class PATHSType extends Node {
   public PATHSType(PATHSType node) {
      super((Node)node);
   }

   public PATHSType(org.w3c.dom.Node node) {
      super(node);
   }

   public PATHSType(Document doc) {
      super(doc);
   }

   public PATHSType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      org.w3c.dom.Node tmpNode;
      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUTFILE"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUTFILE", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new INPUTFILEType(tmpNode)).adjustPrefix();
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUTFILE"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUTFILE", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new OUTPUTFILEType(tmpNode)).adjustPrefix();
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "LOG"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "LOG", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new LOGType(tmpNode)).adjustPrefix();
      }

   }

   public static int getINPUTFILEMinCount() {
      return 1;
   }

   public static int getINPUTFILEMaxCount() {
      return 1;
   }

   public int getINPUTFILECount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "INPUTFILE");
   }

   public boolean hasINPUTFILE() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUTFILE");
   }

   public INPUTFILEType newINPUTFILE() {
      return new INPUTFILEType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "INPUTFILE"));
   }

   public INPUTFILEType getINPUTFILEAt(int index) throws Exception {
      return new INPUTFILEType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "INPUTFILE", index)));
   }

   public org.w3c.dom.Node getStartingINPUTFILECursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUTFILE");
   }

   public org.w3c.dom.Node getAdvancedINPUTFILECursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "INPUTFILE", curNode);
   }

   public INPUTFILEType getINPUTFILEValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new INPUTFILEType(this.dereference(curNode));
      }
   }

   public INPUTFILEType getINPUTFILE() throws Exception {
      return this.getINPUTFILEAt(0);
   }

   public void removeINPUTFILEAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "INPUTFILE", index);
   }

   public void removeINPUTFILE() {
      while(this.hasINPUTFILE()) {
         this.removeINPUTFILEAt(0);
      }

   }

   public void addINPUTFILE(INPUTFILEType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "INPUTFILE", value);
   }

   public void insertINPUTFILEAt(INPUTFILEType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "INPUTFILE", index, value);
   }

   public void replaceINPUTFILEAt(INPUTFILEType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "INPUTFILE", index, value);
   }

   public static int getOUTPUTFILEMinCount() {
      return 1;
   }

   public static int getOUTPUTFILEMaxCount() {
      return 1;
   }

   public int getOUTPUTFILECount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUTFILE");
   }

   public boolean hasOUTPUTFILE() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUTFILE");
   }

   public OUTPUTFILEType newOUTPUTFILE() {
      return new OUTPUTFILEType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "OUTPUTFILE"));
   }

   public OUTPUTFILEType getOUTPUTFILEAt(int index) throws Exception {
      return new OUTPUTFILEType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUTFILE", index)));
   }

   public org.w3c.dom.Node getStartingOUTPUTFILECursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUTFILE");
   }

   public org.w3c.dom.Node getAdvancedOUTPUTFILECursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUTFILE", curNode);
   }

   public OUTPUTFILEType getOUTPUTFILEValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new OUTPUTFILEType(this.dereference(curNode));
      }
   }

   public OUTPUTFILEType getOUTPUTFILE() throws Exception {
      return this.getOUTPUTFILEAt(0);
   }

   public void removeOUTPUTFILEAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "OUTPUTFILE", index);
   }

   public void removeOUTPUTFILE() {
      while(this.hasOUTPUTFILE()) {
         this.removeOUTPUTFILEAt(0);
      }

   }

   public void addOUTPUTFILE(OUTPUTFILEType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "OUTPUTFILE", value);
   }

   public void insertOUTPUTFILEAt(OUTPUTFILEType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "OUTPUTFILE", index, value);
   }

   public void replaceOUTPUTFILEAt(OUTPUTFILEType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "OUTPUTFILE", index, value);
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

   public LOGType newLOG() {
      return new LOGType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "LOG"));
   }

   public LOGType getLOGAt(int index) throws Exception {
      return new LOGType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "LOG", index)));
   }

   public org.w3c.dom.Node getStartingLOGCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "LOG");
   }

   public org.w3c.dom.Node getAdvancedLOGCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "LOG", curNode);
   }

   public LOGType getLOGValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new LOGType(this.dereference(curNode));
      }
   }

   public LOGType getLOG() throws Exception {
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

   public void addLOG(LOGType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "LOG", value);
   }

   public void insertLOGAt(LOGType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "LOG", index, value);
   }

   public void replaceLOGAt(LOGType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "LOG", index, value);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
