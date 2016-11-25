package com.altova.xml;

import com.altova.xml.Node;
import com.altova.xml.XmlException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public abstract class Document implements Serializable {
   protected static DocumentBuilderFactory factory = null;
   protected static DocumentBuilder builder = null;
   protected static boolean validation = false;
   protected org.w3c.dom.Document domDocument;
   protected String encoding = "UTF-8";
   protected String schemaLocation = null;
   protected String dtdLocation = null;

   public static void enableValidation(boolean enable) {
      validation = enable;
   }

   protected static synchronized DocumentBuilder getDomBuilder() {
      try {
         if(builder == null) {
            if(factory == null) {
               factory = DocumentBuilderFactory.newInstance();
               factory.setIgnoringElementContentWhitespace(true);
               factory.setNamespaceAware(true);
               if(validation) {
                  factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
                  factory.setValidating(true);
               }
            }

            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {
               public void warning(SAXParseException e) {
               }

               public void error(SAXParseException e) throws XmlException {
                  throw new XmlException(e);
               }

               public void fatalError(SAXParseException e) throws XmlException {
                  throw new XmlException(e);
               }
            });
         }

         return builder;
      } catch (ParserConfigurationException var1) {
         throw new XmlException(var1);
      }
   }

   public synchronized org.w3c.dom.Document getDomDocument() {
      if(this.domDocument == null) {
         this.domDocument = getDomBuilder().newDocument();
      }

      return this.domDocument;
   }

   public Element createRootElement(String namespaceURI, String name) {
      Element rootElement = null;
      if(this.dtdLocation != null && this.dtdLocation.length() != 0) {
         DocumentType docType = getDomBuilder().getDOMImplementation().createDocumentType(name, (String)null, this.dtdLocation);
         this.domDocument = getDomBuilder().getDOMImplementation().createDocument(namespaceURI, name, docType);
         rootElement = this.domDocument.getDocumentElement();
      } else {
         rootElement = this.getDomDocument().createElementNS(namespaceURI, name);
         this.domDocument.appendChild(rootElement);
         rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
         if(this.schemaLocation != null && this.schemaLocation.length() != 0) {
            if(namespaceURI != null && !namespaceURI.equals("")) {
               rootElement.setAttribute("xsi:schemaLocation", namespaceURI + " " + this.schemaLocation);
            } else {
               rootElement.setAttribute("xsi:noNamespaceSchemaLocation", this.schemaLocation);
            }
         }
      }

      return rootElement;
   }

   public void setEncoding(String encoding) {
      this.encoding = encoding;
   }

   public void setSchemaLocation(String schemaLocation) {
      this.schemaLocation = schemaLocation;
   }

   public void setDTDLocation(String dtdLocation) {
      this.dtdLocation = dtdLocation;
   }

   public org.w3c.dom.Node load(String filename) {
      try {
         return getDomBuilder().parse(new File(filename)).getDocumentElement();
      } catch (SAXException var3) {
         throw new XmlException(var3);
      } catch (IOException var4) {
         throw new XmlException(var4);
      }
   }

   public org.w3c.dom.Node load(InputStream istream) {
      try {
         return getDomBuilder().parse(istream).getDocumentElement();
      } catch (SAXException var3) {
         throw new XmlException(var3);
      } catch (IOException var4) {
         throw new XmlException(var4);
      }
   }

   public org.w3c.dom.Node loadFromString(String xml) {
      try {
         return getDomBuilder().parse(new ByteArrayInputStream(xml.getBytes())).getDocumentElement();
      } catch (SAXException var3) {
         throw new XmlException(var3);
      } catch (IOException var4) {
         throw new XmlException(var4);
      }
   }

   public void save(String filename, Node node) {
      Node.internalAdjustPrefix(node.domNode, true);
      node.adjustPrefix();
      internalSave(new StreamResult(new File(filename)), node.domNode.getOwnerDocument(), this.encoding);
   }

   public void save(OutputStream ostream, Node node) {
      Node.internalAdjustPrefix(node.domNode, true);
      node.adjustPrefix();
      internalSave(new StreamResult(ostream), node.domNode.getOwnerDocument(), this.encoding);
   }

   public String saveToString(Node node) {
      Node.internalAdjustPrefix(node.domNode, true);
      node.adjustPrefix();
      StringWriter sw = new StringWriter();
      internalSave(new StreamResult(sw), node.domNode.getOwnerDocument(), this.encoding);
      return sw.toString();
   }

   protected static void internalSave(Result result, org.w3c.dom.Document doc, String encoding) {
      try {
         DOMSource e = new DOMSource(doc);
         Transformer transformer = TransformerFactory.newInstance().newTransformer();
         if(encoding != null) {
            transformer.setOutputProperty("encoding", encoding);
         }

         if(doc.getDoctype() != null) {
            if(doc.getDoctype().getPublicId() != null) {
               transformer.setOutputProperty("doctype-public", doc.getDoctype().getPublicId());
            }

            if(doc.getDoctype().getSystemId() != null) {
               transformer.setOutputProperty("doctype-system", doc.getDoctype().getSystemId());
            }
         }

         transformer.transform(e, result);
      } catch (TransformerConfigurationException var5) {
         throw new XmlException(var5);
      } catch (TransformerException var6) {
         throw new XmlException(var6);
      }
   }

   public org.w3c.dom.Node transform(Node node, String xslFilename) {
      try {
         TransformerFactory e = TransformerFactory.newInstance();
         Transformer transformer = e.newTransformer(new StreamSource(xslFilename));
         DOMResult result = new DOMResult();
         transformer.transform(new DOMSource(node.domNode), result);
         return result.getNode();
      } catch (TransformerException var6) {
         throw new XmlException(var6);
      }
   }

   public abstract void declareNamespaces(Node var1);

   protected void declareNamespace(Node node, String prefix, String URI) {
      node.declareNamespace(prefix, URI);
   }
}
