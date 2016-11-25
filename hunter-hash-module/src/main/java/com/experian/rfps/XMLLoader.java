package com.experian.rfps;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLLoader {
   private DocumentBuilderFactory factory;
   private DocumentBuilder builder;
   private SchemaFactory schemaFactory;
   private String strSchema;
   private Schema schema;
   private static final Logger log = Logger.getLogger(XMLLoader.class);
   private boolean valid = true;
   public List<SAXParseException> exceptions = new LinkedList();

   public static XMLLoader getInstance() throws ParserConfigurationException {
      return new XMLLoader();
   }

   private XMLLoader() throws ParserConfigurationException {
      this.factory = DocumentBuilderFactory.newInstance();
      this.schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      this.builder = this.factory.newDocumentBuilder();
      this.builder.setErrorHandler(new XMLLoader.ClassHandler());
   }

   public static XMLLoader getInstance(String XSDSchema) throws SAXException, ParserConfigurationException {
      return new XMLLoader(XSDSchema);
   }

   private XMLLoader(String XSDSchema) throws SAXException, ParserConfigurationException {
      this.strSchema = XSDSchema;
      this.factory = DocumentBuilderFactory.newInstance();
      this.schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      this.schema = this.schemaFactory.newSchema(new File(XSDSchema));
      this.factory.setNamespaceAware(true);
      this.factory.setSchema(this.schema);
      this.builder = this.factory.newDocumentBuilder();
      XMLLoader.ClassHandler ch = new XMLLoader.ClassHandler();
      this.builder.setErrorHandler(ch);
      this.builder.setEntityResolver(ch);
   }

   public void reset() {
      this.exceptions.clear();
      this.valid = true;
   }

   public DocumentBuilder getBuilder() {
      this.reset();
      return this.builder;
   }

   public boolean isValid() {
      return this.valid;
   }

   class ClassHandler extends DefaultHandler {
      public void startElement(String nsURI, String strippedName, String tagName, Attributes attributes) throws SAXException {
         XMLLoader.log.warn("!!!" + tagName);
      }

      public void warning(SAXParseException e) {
         XMLLoader.log.warn("[" + e.getPublicId() + " " + e.getSystemId() + "] " + "Schema:" + XMLLoader.this.strSchema + ". Warning Line " + e.getLineNumber() + ": " + e.getMessage());
      }

      public void error(SAXParseException e) {
         XMLLoader.this.exceptions.add(e);
         XMLLoader.log.error("[" + e.getPublicId() + " " + e.getSystemId() + "] " + "Schema:" + XMLLoader.this.strSchema + ". Error Line " + e.getLineNumber() + ": " + e.getMessage());
         XMLLoader.this.valid = false;
      }

      public void fatalError(SAXParseException e) {
         XMLLoader.this.exceptions.add(e);
         XMLLoader.log.error("[" + e.getPublicId() + " " + e.getSystemId() + "] " + "Schema:" + XMLLoader.this.strSchema + ". Fatal Error Line " + e.getLineNumber() + ": " + e.getMessage() + "\n");
         XMLLoader.this.valid = false;
      }
   }
}
