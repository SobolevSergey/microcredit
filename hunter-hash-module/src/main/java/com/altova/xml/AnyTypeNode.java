package com.altova.xml;

import com.altova.types.SchemaString;
import com.altova.types.SchemaType;
import com.altova.xml.Document;
import com.altova.xml.Node;

public class AnyTypeNode extends Node {
   public AnyTypeNode(AnyTypeNode node) {
      super((Node)node);
   }

   public AnyTypeNode(org.w3c.dom.Node node) {
      super(node);
   }

   public AnyTypeNode(org.w3c.dom.Document doc) {
      super(doc);
   }

   public AnyTypeNode(Document doc, String namespaceURI, String prefix, String name) {
      super(doc, namespaceURI, prefix, name);
   }

   public SchemaString getValue() {
      return new SchemaString(getDomNodeValue(this.dereference(this.domNode)));
   }

   public void setValue(SchemaType value) {
      setDomNodeValue(this.domNode, value.toString());
   }

   public void assign(SchemaType value) {
      this.setValue(value);
   }

   public void adjustPrefix() {
   }

   public void addTextNode(String value) throws Exception {
      this.appendDomChild(2, (String)null, (String)null, value.toString());
   }

   public void addComment(String value) throws Exception {
      this.appendDomChild(4, (String)null, (String)null, value.toString());
   }

   public void addCDataNode(String value) throws Exception {
      this.appendDomChild(3, (String)null, (String)null, value.toString());
   }

   public void addProcessingInstruction(String name, String value) throws Exception {
      this.appendDomChild(5, (String)null, name.toString(), value.toString());
   }

   private org.w3c.dom.Node dereference(org.w3c.dom.Node node) {
      return node;
   }
}
