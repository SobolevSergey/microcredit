package com.experian.rfps.config;

import com.altova.types.SchemaString;
import com.altova.xml.Node;
import com.altova.xml.XmlException;
import com.experian.rfps.config.CSVType;
import com.experian.rfps.config.MODEType;
import com.experian.rfps.config.OPERATIONType;
import com.experian.rfps.config.PATHSType;
import com.experian.rfps.config.STATUSType;
import com.experian.rfps.config.VERSIONType;
import com.experian.rfps.config.XMLType;
import org.w3c.dom.Document;

public class RFPSType extends Node {
   public RFPSType(RFPSType node) {
      super((Node)node);
   }

   public RFPSType(org.w3c.dom.Node node) {
      super(node);
   }

   public RFPSType(Document doc) {
      super(doc);
   }

   public RFPSType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public void adjustPrefix() {
      org.w3c.dom.Node tmpNode;
      for(tmpNode = this.getDomFirstChild(0, (String)null, "VERSION"); tmpNode != null; tmpNode = this.getDomNextChild(0, (String)null, "VERSION", tmpNode)) {
         internalAdjustPrefix(tmpNode, false);
      }

      for(tmpNode = this.getDomFirstChild(0, (String)null, "DATE_VERSION"); tmpNode != null; tmpNode = this.getDomNextChild(0, (String)null, "DATE_VERSION", tmpNode)) {
         internalAdjustPrefix(tmpNode, false);
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "PATHS"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "PATHS", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new PATHSType(tmpNode)).adjustPrefix();
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "MODE"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "MODE", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "STATUS"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "STATUS", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "OPERATION"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "OPERATION", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new OPERATIONType(tmpNode)).adjustPrefix();
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "XML"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "XML", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new XMLType(tmpNode)).adjustPrefix();
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "CSV"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "CSV", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new CSVType(tmpNode)).adjustPrefix();
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "HM_VERSION"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "HM_VERSION", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
      }

   }

   public static int getVERSIONMinCount() {
      return 1;
   }

   public static int getVERSIONMaxCount() {
      return 1;
   }

   public int getVERSIONCount() {
      return this.getDomChildCount(0, (String)null, "VERSION");
   }

   public boolean hasVERSION() {
      return this.hasDomChild(0, (String)null, "VERSION");
   }

   public VERSIONType newVERSION() {
      return new VERSIONType();
   }

   public VERSIONType getVERSIONAt(int index) throws Exception {
      return new VERSIONType(getDomNodeValue(this.dereference(this.getDomChildAt(0, (String)null, "VERSION", index))));
   }

   public org.w3c.dom.Node getStartingVERSIONCursor() throws Exception {
      return this.getDomFirstChild(0, (String)null, "VERSION");
   }

   public org.w3c.dom.Node getAdvancedVERSIONCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(0, (String)null, "VERSION", curNode);
   }

   public VERSIONType getVERSIONValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new VERSIONType(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public VERSIONType getVERSION() throws Exception {
      return this.getVERSIONAt(0);
   }

   public void removeVERSIONAt(int index) {
      this.removeDomChildAt(0, (String)null, "VERSION", index);
   }

   public void removeVERSION() {
      while(this.hasVERSION()) {
         this.removeVERSIONAt(0);
      }

   }

   public void addVERSION(VERSIONType value) {
      if(!value.isNull()) {
         this.appendDomChild(0, (String)null, "VERSION", value.toString());
      }

   }

   public void addVERSION(String value) throws Exception {
      this.addVERSION(new VERSIONType(value));
   }

   public void insertVERSIONAt(VERSIONType value, int index) {
      this.insertDomChildAt(0, (String)null, "VERSION", index, value.toString());
   }

   public void insertVERSIONAt(String value, int index) throws Exception {
      this.insertVERSIONAt(new VERSIONType(value), index);
   }

   public void replaceVERSIONAt(VERSIONType value, int index) {
      this.replaceDomChildAt(0, (String)null, "VERSION", index, value.toString());
   }

   public void replaceVERSIONAt(String value, int index) throws Exception {
      this.replaceVERSIONAt(new VERSIONType(value), index);
   }

   public static int getDATE_VERSIONMinCount() {
      return 0;
   }

   public static int getDATE_VERSIONMaxCount() {
      return 1;
   }

   public int getDATE_VERSIONCount() {
      return this.getDomChildCount(0, (String)null, "DATE_VERSION");
   }

   public boolean hasDATE_VERSION() {
      return this.hasDomChild(0, (String)null, "DATE_VERSION");
   }

   public SchemaString newDATE_VERSION() {
      return new SchemaString();
   }

   public SchemaString getDATE_VERSIONAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(0, (String)null, "DATE_VERSION", index))));
   }

   public org.w3c.dom.Node getStartingDATE_VERSIONCursor() throws Exception {
      return this.getDomFirstChild(0, (String)null, "DATE_VERSION");
   }

   public org.w3c.dom.Node getAdvancedDATE_VERSIONCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(0, (String)null, "DATE_VERSION", curNode);
   }

   public SchemaString getDATE_VERSIONValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new SchemaString(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public SchemaString getDATE_VERSION() throws Exception {
      return this.getDATE_VERSIONAt(0);
   }

   public void removeDATE_VERSIONAt(int index) {
      this.removeDomChildAt(0, (String)null, "DATE_VERSION", index);
   }

   public void removeDATE_VERSION() {
      while(this.hasDATE_VERSION()) {
         this.removeDATE_VERSIONAt(0);
      }

   }

   public void addDATE_VERSION(SchemaString value) {
      if(!value.isNull()) {
         this.appendDomChild(0, (String)null, "DATE_VERSION", value.toString());
      }

   }

   public void addDATE_VERSION(String value) throws Exception {
      this.addDATE_VERSION(new SchemaString(value));
   }

   public void insertDATE_VERSIONAt(SchemaString value, int index) {
      this.insertDomChildAt(0, (String)null, "DATE_VERSION", index, value.toString());
   }

   public void insertDATE_VERSIONAt(String value, int index) throws Exception {
      this.insertDATE_VERSIONAt(new SchemaString(value), index);
   }

   public void replaceDATE_VERSIONAt(SchemaString value, int index) {
      this.replaceDomChildAt(0, (String)null, "DATE_VERSION", index, value.toString());
   }

   public void replaceDATE_VERSIONAt(String value, int index) throws Exception {
      this.replaceDATE_VERSIONAt(new SchemaString(value), index);
   }

   public static int getPATHSMinCount() {
      return 1;
   }

   public static int getPATHSMaxCount() {
      return 1;
   }

   public int getPATHSCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "PATHS");
   }

   public boolean hasPATHS() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "PATHS");
   }

   public PATHSType newPATHS() {
      return new PATHSType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "PATHS"));
   }

   public PATHSType getPATHSAt(int index) throws Exception {
      return new PATHSType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PATHS", index)));
   }

   public org.w3c.dom.Node getStartingPATHSCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "PATHS");
   }

   public org.w3c.dom.Node getAdvancedPATHSCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "PATHS", curNode);
   }

   public PATHSType getPATHSValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new PATHSType(this.dereference(curNode));
      }
   }

   public PATHSType getPATHS() throws Exception {
      return this.getPATHSAt(0);
   }

   public void removePATHSAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "PATHS", index);
   }

   public void removePATHS() {
      while(this.hasPATHS()) {
         this.removePATHSAt(0);
      }

   }

   public void addPATHS(PATHSType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "PATHS", value);
   }

   public void insertPATHSAt(PATHSType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "PATHS", index, value);
   }

   public void replacePATHSAt(PATHSType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "PATHS", index, value);
   }

   public static int getMODEMinCount() {
      return 1;
   }

   public static int getMODEMaxCount() {
      return 1;
   }

   public int getMODECount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "MODE");
   }

   public boolean hasMODE() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "MODE");
   }

   public MODEType newMODE() {
      return new MODEType();
   }

   public MODEType getMODEAt(int index) throws Exception {
      return new MODEType(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "MODE", index))));
   }

   public org.w3c.dom.Node getStartingMODECursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "MODE");
   }

   public org.w3c.dom.Node getAdvancedMODECursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "MODE", curNode);
   }

   public MODEType getMODEValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new MODEType(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public MODEType getMODE() throws Exception {
      return this.getMODEAt(0);
   }

   public void removeMODEAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "MODE", index);
   }

   public void removeMODE() {
      while(this.hasMODE()) {
         this.removeMODEAt(0);
      }

   }

   public void addMODE(MODEType value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "MODE", value.toString());
      }

   }

   public void addMODE(String value) throws Exception {
      this.addMODE(new MODEType(value));
   }

   public void insertMODEAt(MODEType value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "MODE", index, value.toString());
   }

   public void insertMODEAt(String value, int index) throws Exception {
      this.insertMODEAt(new MODEType(value), index);
   }

   public void replaceMODEAt(MODEType value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "MODE", index, value.toString());
   }

   public void replaceMODEAt(String value, int index) throws Exception {
      this.replaceMODEAt(new MODEType(value), index);
   }

   public static int getSTATUSMinCount() {
      return 1;
   }

   public static int getSTATUSMaxCount() {
      return 1;
   }

   public int getSTATUSCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "STATUS");
   }

   public boolean hasSTATUS() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "STATUS");
   }

   public STATUSType newSTATUS() {
      return new STATUSType();
   }

   public STATUSType getSTATUSAt(int index) throws Exception {
      return new STATUSType(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "STATUS", index))));
   }

   public org.w3c.dom.Node getStartingSTATUSCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "STATUS");
   }

   public org.w3c.dom.Node getAdvancedSTATUSCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "STATUS", curNode);
   }

   public STATUSType getSTATUSValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new STATUSType(getDomNodeValue(this.dereference(curNode)));
      }
   }

   public STATUSType getSTATUS() throws Exception {
      return this.getSTATUSAt(0);
   }

   public void removeSTATUSAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "STATUS", index);
   }

   public void removeSTATUS() {
      while(this.hasSTATUS()) {
         this.removeSTATUSAt(0);
      }

   }

   public void addSTATUS(STATUSType value) {
      if(!value.isNull()) {
         this.appendDomChild(1, "urn:mclsoftware.co.uk:hunterII", "STATUS", value.toString());
      }

   }

   public void addSTATUS(String value) throws Exception {
      this.addSTATUS(new STATUSType(value));
   }

   public void insertSTATUSAt(STATUSType value, int index) {
      this.insertDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "STATUS", index, value.toString());
   }

   public void insertSTATUSAt(String value, int index) throws Exception {
      this.insertSTATUSAt(new STATUSType(value), index);
   }

   public void replaceSTATUSAt(STATUSType value, int index) {
      this.replaceDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "STATUS", index, value.toString());
   }

   public void replaceSTATUSAt(String value, int index) throws Exception {
      this.replaceSTATUSAt(new STATUSType(value), index);
   }

   public static int getOPERATIONMinCount() {
      return 1;
   }

   public static int getOPERATIONMaxCount() {
      return 1;
   }

   public int getOPERATIONCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "OPERATION");
   }

   public boolean hasOPERATION() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "OPERATION");
   }

   public OPERATIONType newOPERATION() {
      return new OPERATIONType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "OPERATION"));
   }

   public OPERATIONType getOPERATIONAt(int index) throws Exception {
      return new OPERATIONType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "OPERATION", index)));
   }

   public org.w3c.dom.Node getStartingOPERATIONCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "OPERATION");
   }

   public org.w3c.dom.Node getAdvancedOPERATIONCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "OPERATION", curNode);
   }

   public OPERATIONType getOPERATIONValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new OPERATIONType(this.dereference(curNode));
      }
   }

   public OPERATIONType getOPERATION() throws Exception {
      return this.getOPERATIONAt(0);
   }

   public void removeOPERATIONAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "OPERATION", index);
   }

   public void removeOPERATION() {
      while(this.hasOPERATION()) {
         this.removeOPERATIONAt(0);
      }

   }

   public void addOPERATION(OPERATIONType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "OPERATION", value);
   }

   public void insertOPERATIONAt(OPERATIONType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "OPERATION", index, value);
   }

   public void replaceOPERATIONAt(OPERATIONType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "OPERATION", index, value);
   }

   public static int getXMLMinCount() {
      return 1;
   }

   public static int getXMLMaxCount() {
      return 1;
   }

   public int getXMLCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "XML");
   }

   public boolean hasXML() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "XML");
   }

   public XMLType newXML() {
      return new XMLType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "XML"));
   }

   public XMLType getXMLAt(int index) throws Exception {
      return new XMLType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "XML", index)));
   }

   public org.w3c.dom.Node getStartingXMLCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "XML");
   }

   public org.w3c.dom.Node getAdvancedXMLCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "XML", curNode);
   }

   public XMLType getXMLValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new XMLType(this.dereference(curNode));
      }
   }

   public XMLType getXML() throws Exception {
      return this.getXMLAt(0);
   }

   public void removeXMLAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "XML", index);
   }

   public void removeXML() {
      while(this.hasXML()) {
         this.removeXMLAt(0);
      }

   }

   public void addXML(XMLType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "XML", value);
   }

   public void insertXMLAt(XMLType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "XML", index, value);
   }

   public void replaceXMLAt(XMLType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "XML", index, value);
   }

   public static int getCSVMinCount() {
      return 1;
   }

   public static int getCSVMaxCount() {
      return 1;
   }

   public int getCSVCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "CSV");
   }

   public boolean hasCSV() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "CSV");
   }

   public CSVType newCSV() {
      return new CSVType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "CSV"));
   }

   public CSVType getCSVAt(int index) throws Exception {
      return new CSVType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "CSV", index)));
   }

   public org.w3c.dom.Node getStartingCSVCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "CSV");
   }

   public org.w3c.dom.Node getAdvancedCSVCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "CSV", curNode);
   }

   public CSVType getCSVValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new CSVType(this.dereference(curNode));
      }
   }

   public CSVType getCSV() throws Exception {
      return this.getCSVAt(0);
   }

   public void removeCSVAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "CSV", index);
   }

   public void removeCSV() {
      while(this.hasCSV()) {
         this.removeCSVAt(0);
      }

   }

   public void addCSV(CSVType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "CSV", value);
   }

   public void insertCSVAt(CSVType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "CSV", index, value);
   }

   public void replaceCSVAt(CSVType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "CSV", index, value);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }

   public boolean hasHM_VERSION() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "HM_VERSION");
   }

   public SchemaString getHM_VERSIONAt(int index) throws Exception {
      return new SchemaString(getDomNodeValue(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "HM_VERSION", index))));
   }

   public SchemaString getHM_VERSION() throws Exception {
      return this.getHM_VERSIONAt(0);
   }
}
