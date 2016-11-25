package com.experian.rfps.transformation;

import com.altova.types.SchemaString;
import com.altova.xml.Node;
import com.altova.xml.XmlException;
import com.experian.rfps.transformation.DICTIONARIESType;
import com.experian.rfps.transformation.DOMAINSType;
import com.experian.rfps.transformation.RULESType;
import com.experian.rfps.transformation.TABLESType;
import com.experian.rfps.transformation.VERSIONType;
import com.experian.rfps.transformation.XSDType;
import com.experian.rfps.transformation.XSLType;
import org.w3c.dom.Document;

public class TRANSFORMATIONType extends Node {
   public TRANSFORMATIONType(TRANSFORMATIONType node) {
      super((Node)node);
   }

   public TRANSFORMATIONType(org.w3c.dom.Node node) {
      super(node);
   }

   public TRANSFORMATIONType(Document doc) {
      super(doc);
   }

   public TRANSFORMATIONType(com.altova.xml.Document doc, String namespaceURI, String prefix, String name) {
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

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARIES"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARIES", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new DICTIONARIESType(tmpNode)).adjustPrefix();
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "RULES"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "RULES", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new RULESType(tmpNode)).adjustPrefix();
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "DOMAINS"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "DOMAINS", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new DOMAINSType(tmpNode)).adjustPrefix();
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "TABLES"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "TABLES", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new TABLESType(tmpNode)).adjustPrefix();
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "XSD"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "XSD", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new XSDType(tmpNode)).adjustPrefix();
      }

      for(tmpNode = this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "XSL"); tmpNode != null; tmpNode = this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "XSL", tmpNode)) {
         internalAdjustPrefix(tmpNode, true);
         (new XSLType(tmpNode)).adjustPrefix();
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

   public static int getDICTIONARIESMinCount() {
      return 1;
   }

   public static int getDICTIONARIESMaxCount() {
      return 1;
   }

   public int getDICTIONARIESCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARIES");
   }

   public boolean hasDICTIONARIES() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARIES");
   }

   public DICTIONARIESType newDICTIONARIES() {
      return new DICTIONARIESType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "DICTIONARIES"));
   }

   public DICTIONARIESType getDICTIONARIESAt(int index) throws Exception {
      return new DICTIONARIESType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARIES", index)));
   }

   public org.w3c.dom.Node getStartingDICTIONARIESCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARIES");
   }

   public org.w3c.dom.Node getAdvancedDICTIONARIESCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARIES", curNode);
   }

   public DICTIONARIESType getDICTIONARIESValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new DICTIONARIESType(this.dereference(curNode));
      }
   }

   public DICTIONARIESType getDICTIONARIES() throws Exception {
      return this.getDICTIONARIESAt(0);
   }

   public void removeDICTIONARIESAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "DICTIONARIES", index);
   }

   public void removeDICTIONARIES() {
      while(this.hasDICTIONARIES()) {
         this.removeDICTIONARIESAt(0);
      }

   }

   public void addDICTIONARIES(DICTIONARIESType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "DICTIONARIES", value);
   }

   public void insertDICTIONARIESAt(DICTIONARIESType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "DICTIONARIES", index, value);
   }

   public void replaceDICTIONARIESAt(DICTIONARIESType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "DICTIONARIES", index, value);
   }

   public static int getRULESMinCount() {
      return 1;
   }

   public static int getRULESMaxCount() {
      return 1;
   }

   public int getRULESCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "RULES");
   }

   public boolean hasRULES() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "RULES");
   }

   public RULESType newRULES() {
      return new RULESType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "RULES"));
   }

   public RULESType getRULESAt(int index) throws Exception {
      return new RULESType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "RULES", index)));
   }

   public org.w3c.dom.Node getStartingRULESCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "RULES");
   }

   public org.w3c.dom.Node getAdvancedRULESCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "RULES", curNode);
   }

   public RULESType getRULESValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new RULESType(this.dereference(curNode));
      }
   }

   public RULESType getRULES() throws Exception {
      return this.getRULESAt(0);
   }

   public void removeRULESAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "RULES", index);
   }

   public void removeRULES() {
      while(this.hasRULES()) {
         this.removeRULESAt(0);
      }

   }

   public void addRULES(RULESType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "RULES", value);
   }

   public void insertRULESAt(RULESType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "RULES", index, value);
   }

   public void replaceRULESAt(RULESType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "RULES", index, value);
   }

   public static int getDOMAINSMinCount() {
      return 1;
   }

   public static int getDOMAINSMaxCount() {
      return 1;
   }

   public int getDOMAINSCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "DOMAINS");
   }

   public boolean hasDOMAINS() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "DOMAINS");
   }

   public DOMAINSType newDOMAINS() {
      return new DOMAINSType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "DOMAINS"));
   }

   public DOMAINSType getDOMAINSAt(int index) throws Exception {
      return new DOMAINSType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "DOMAINS", index)));
   }

   public org.w3c.dom.Node getStartingDOMAINSCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "DOMAINS");
   }

   public org.w3c.dom.Node getAdvancedDOMAINSCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "DOMAINS", curNode);
   }

   public DOMAINSType getDOMAINSValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new DOMAINSType(this.dereference(curNode));
      }
   }

   public DOMAINSType getDOMAINS() throws Exception {
      return this.getDOMAINSAt(0);
   }

   public void removeDOMAINSAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "DOMAINS", index);
   }

   public void removeDOMAINS() {
      while(this.hasDOMAINS()) {
         this.removeDOMAINSAt(0);
      }

   }

   public void addDOMAINS(DOMAINSType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "DOMAINS", value);
   }

   public void insertDOMAINSAt(DOMAINSType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "DOMAINS", index, value);
   }

   public void replaceDOMAINSAt(DOMAINSType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "DOMAINS", index, value);
   }

   public static int getTABLESMinCount() {
      return 1;
   }

   public static int getTABLESMaxCount() {
      return 1;
   }

   public int getTABLESCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "TABLES");
   }

   public boolean hasTABLES() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "TABLES");
   }

   public TABLESType newTABLES() {
      return new TABLESType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "TABLES"));
   }

   public TABLESType getTABLESAt(int index) throws Exception {
      return new TABLESType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "TABLES", index)));
   }

   public org.w3c.dom.Node getStartingTABLESCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "TABLES");
   }

   public org.w3c.dom.Node getAdvancedTABLESCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "TABLES", curNode);
   }

   public TABLESType getTABLESValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new TABLESType(this.dereference(curNode));
      }
   }

   public TABLESType getTABLES() throws Exception {
      return this.getTABLESAt(0);
   }

   public void removeTABLESAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "TABLES", index);
   }

   public void removeTABLES() {
      while(this.hasTABLES()) {
         this.removeTABLESAt(0);
      }

   }

   public void addTABLES(TABLESType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "TABLES", value);
   }

   public void insertTABLESAt(TABLESType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "TABLES", index, value);
   }

   public void replaceTABLESAt(TABLESType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "TABLES", index, value);
   }

   public static int getXSDMinCount() {
      return 1;
   }

   public static int getXSDMaxCount() {
      return 1;
   }

   public int getXSDCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "XSD");
   }

   public boolean hasXSD() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "XSD");
   }

   public XSDType newXSD() {
      return new XSDType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "XSD"));
   }

   public XSDType getXSDAt(int index) throws Exception {
      return new XSDType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "XSD", index)));
   }

   public org.w3c.dom.Node getStartingXSDCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "XSD");
   }

   public org.w3c.dom.Node getAdvancedXSDCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "XSD", curNode);
   }

   public XSDType getXSDValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new XSDType(this.dereference(curNode));
      }
   }

   public XSDType getXSD() throws Exception {
      return this.getXSDAt(0);
   }

   public void removeXSDAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "XSD", index);
   }

   public void removeXSD() {
      while(this.hasXSD()) {
         this.removeXSDAt(0);
      }

   }

   public void addXSD(XSDType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "XSD", value);
   }

   public void insertXSDAt(XSDType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "XSD", index, value);
   }

   public void replaceXSDAt(XSDType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "XSD", index, value);
   }

   public static int getXSLMinCount() {
      return 1;
   }

   public static int getXSLMaxCount() {
      return 1;
   }

   public int getXSLCount() {
      return this.getDomChildCount(1, "urn:mclsoftware.co.uk:hunterII", "XSL");
   }

   public boolean hasXSL() {
      return this.hasDomChild(1, "urn:mclsoftware.co.uk:hunterII", "XSL");
   }

   public XSLType newXSL() {
      return new XSLType(this.domNode.getOwnerDocument().createElementNS("urn:mclsoftware.co.uk:hunterII", "XSL"));
   }

   public XSLType getXSLAt(int index) throws Exception {
      return new XSLType(this.dereference(this.getDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "XSL", index)));
   }

   public org.w3c.dom.Node getStartingXSLCursor() throws Exception {
      return this.getDomFirstChild(1, "urn:mclsoftware.co.uk:hunterII", "XSL");
   }

   public org.w3c.dom.Node getAdvancedXSLCursor(org.w3c.dom.Node curNode) throws Exception {
      return this.getDomNextChild(1, "urn:mclsoftware.co.uk:hunterII", "XSL", curNode);
   }

   public XSLType getXSLValueAtCursor(org.w3c.dom.Node curNode) throws Exception {
      if(curNode == null) {
         throw new XmlException("Out of range");
      } else {
         return new XSLType(this.dereference(curNode));
      }
   }

   public XSLType getXSL() throws Exception {
      return this.getXSLAt(0);
   }

   public void removeXSLAt(int index) {
      this.removeDomChildAt(1, "urn:mclsoftware.co.uk:hunterII", "XSL", index);
   }

   public void removeXSL() {
      while(this.hasXSL()) {
         this.removeXSLAt(0);
      }

   }

   public void addXSL(XSLType value) {
      this.appendDomElement("urn:mclsoftware.co.uk:hunterII", "XSL", value);
   }

   public void insertXSLAt(XSLType value, int index) {
      this.insertDomElementAt("urn:mclsoftware.co.uk:hunterII", "XSL", index, value);
   }

   public void replaceXSLAt(XSLType value, int index) {
      this.replaceDomElementAt("urn:mclsoftware.co.uk:hunterII", "XSL", index, value);
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
