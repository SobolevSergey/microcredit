package com.altova.xml;

import com.altova.xml.Document;
import com.altova.xml.XmlException;
import java.io.Serializable;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public abstract class Node implements Serializable {
   protected static final short Attribute = 0;
   protected static final short Element = 1;
   protected static final short Text = 2;
   protected static final short CData = 3;
   protected static final short Comment = 4;
   protected static final short ProcessingInstruction = 5;
   protected org.w3c.dom.Node domNode = null;

   public Node(Node node) {
      this.domNode = node.domNode;
   }

   public Node(org.w3c.dom.Node domNode) {
      this.domNode = domNode;
   }

   public Node(org.w3c.dom.Document domDocument) {
      this.domNode = domDocument.getDocumentElement();
   }

   public Node(Document doc, String namespaceURI, String prefix, String name) {
      this.domNode = doc.createRootElement(namespaceURI, name);
      this.mapPrefix(prefix, namespaceURI);
      doc.declareNamespaces(this);
   }

   public org.w3c.dom.Node getDomNode() {
      return this.domNode;
   }

   protected static String getDomNodeValue(org.w3c.dom.Node node) {
      if(node == null) {
         return null;
      } else {
         String value = node.getNodeValue();
         return value != null?value:getInnerText(node);
      }
   }

   protected static String getInnerText(org.w3c.dom.Node node) {
      StringBuffer text = new StringBuffer();
      NodeList elements = node.getChildNodes();
      int length = elements.getLength();

      for(int i = 0; i < length; ++i) {
         org.w3c.dom.Node child = elements.item(i);
         if(child.getNodeType() == 3) {
            text.append(child.getNodeValue());
         } else if(child.getNodeType() == 1) {
            text.append(getInnerText(child));
         } else if(child.getNodeType() == 4) {
            text.append(child.getNodeValue());
         }
      }

      return text.toString();
   }

   public static String getNodeTextValue(org.w3c.dom.Node node) {
      return getDomNodeValue(node);
   }

   protected static void setDomNodeValue(org.w3c.dom.Node node, String value) {
      if(node != null) {
         if(node.getNodeValue() != null) {
            node.setNodeValue(value);
         } else {
            org.w3c.dom.Node child = node.getFirstChild();
            if(child == null || child.getNodeType() != 3) {
               node.appendChild(node.getOwnerDocument().createTextNode(value));
            }
         }
      }
   }

   public void mapPrefix(String prefix, String URI) {
      if(URI != null && !URI.equals("")) {
         Element element = (Element)this.domNode;
         if(prefix != null && !prefix.equals("")) {
            element.setAttribute("xmlns:" + prefix, URI);
         } else {
            element.setAttribute("xmlns", URI);
         }

      }
   }

   public void assign(Node other) {
      setDomNodeValue(this.domNode, getDomNodeValue(other.domNode).toString());
   }

   protected void declareNamespace(String prefix, String URI) {
      if(URI != null && !URI.equals("")) {
         Element root = this.domNode.getOwnerDocument().getDocumentElement();
         NamedNodeMap attrs = root.getAttributes();
         if(attrs != null) {
            for(int i = 0; i < attrs.getLength(); ++i) {
               Attr attr = (Attr)attrs.item(i);
               if(attr.getValue().equals(URI) && attr.getName().startsWith("xmlns")) {
                  return;
               }
            }
         }

         if(prefix != null && !prefix.equals("")) {
            root.setAttribute("xmlns:" + prefix, URI);
         } else {
            root.setAttribute("xmlns", URI);
         }

      }
   }

   protected org.w3c.dom.Node appendDomChild(int type, String namespaceURI, String name, String value) {
      switch(type) {
      case 0:
         Attr attribute = this.domNode.getOwnerDocument().createAttributeNS(namespaceURI, name);
         attribute.setNodeValue(value);
         this.domNode.getAttributes().setNamedItemNS(attribute);
         return attribute;
      case 1:
         Element element = this.domNode.getOwnerDocument().createElementNS(namespaceURI, name);
         if(value != null && value.length() != 0) {
            element.appendChild(this.domNode.getOwnerDocument().createTextNode(value));
         }

         this.domNode.appendChild(element);
         return element;
      case 2:
         Text text = this.domNode.getOwnerDocument().createTextNode(value);
         this.domNode.appendChild(text);
         return text;
      case 3:
         CDATASection cdata = this.domNode.getOwnerDocument().createCDATASection(value);
         this.domNode.appendChild(cdata);
         return cdata;
      case 4:
         Comment comment = this.domNode.getOwnerDocument().createComment(value);
         this.domNode.appendChild(comment);
         return comment;
      case 5:
         ProcessingInstruction piNode = this.domNode.getOwnerDocument().createProcessingInstruction(name, value);
         this.domNode.appendChild(piNode);
         return piNode;
      default:
         throw new XmlException("Unknown type");
      }
   }

   protected boolean domNodeNameEquals(org.w3c.dom.Node node, String namespaceURI, String localName) {
      if(node == null) {
         return false;
      } else if(localName == null) {
         return true;
      } else {
         String nodeURI = node.getNamespaceURI() == null?"":node.getNamespaceURI();
         String nodeLocalName = node.getLocalName() == null?"":node.getLocalName();
         if(namespaceURI == null) {
            namespaceURI = "";
         }

         return nodeURI.equals(namespaceURI) && nodeLocalName.equals(localName);
      }
   }

   protected int getDomChildCount(int type, String namespaceURI, String name) {
      switch(type) {
      case 0:
         try {
            return ((Element)this.domNode).hasAttributeNS(namespaceURI, name)?1:0;
         } catch (Exception var9) {
            return 0;
         }
      case 1:
         NodeList elements = this.domNode.getChildNodes();
         int length = elements.getLength();
         int count = 0;

         for(int i = 0; i < length; ++i) {
            org.w3c.dom.Node child = elements.item(i);
            if(this.domNodeNameEquals(child, namespaceURI, name)) {
               ++count;
            }
         }

         return count;
      default:
         throw new XmlException("Unknown type");
      }
   }

   protected boolean hasDomChild(int type, String namespaceURI, String name) {
      switch(type) {
      case 0:
         return ((Element)this.domNode).hasAttributeNS(namespaceURI, name);
      case 1:
         NodeList elements = this.domNode.getChildNodes();
         int length = elements.getLength();

         for(int i = 0; i < length; ++i) {
            if(this.domNodeNameEquals(elements.item(i), namespaceURI, name)) {
               return true;
            }
         }

         return false;
      default:
         throw new XmlException("Unknown type");
      }
   }

   protected org.w3c.dom.Node getDomChildAt(int type, String namespaceURI, String name, int index) {
      int count = 0;
      switch(type) {
      case 0:
         return this.domNode.getAttributes().getNamedItemNS(namespaceURI, name);
      case 1:
         NodeList elements = this.domNode.getChildNodes();
         int length = elements.getLength();

         for(int i = 0; i < length; ++i) {
            org.w3c.dom.Node child = elements.item(i);
            if(this.domNodeNameEquals(child, namespaceURI, name) && count++ == index) {
               return child;
            }
         }

         throw new XmlException("Index out of range");
      default:
         throw new XmlException("Unknown type");
      }
   }

   protected org.w3c.dom.Node getDomFirstChild(int type, String namespaceURI, String name) {
      boolean count = false;
      switch(type) {
      case 0:
         if(!((Element)this.domNode).hasAttributeNS(namespaceURI, name)) {
            return null;
         }

         return this.domNode.getAttributes().getNamedItemNS(namespaceURI, name);
      case 1:
         if(!this.domNode.hasChildNodes()) {
            return null;
         } else {
            for(org.w3c.dom.Node child = this.domNode.getFirstChild(); child != null; child = child.getNextSibling()) {
               if(child.getNodeType() == 1 && this.domNodeNameEquals(child, namespaceURI, name)) {
                  org.w3c.dom.Node retElement = child;
                  child = null;
                  return retElement;
               }
            }

            return null;
         }
      default:
         throw new XmlException("Unknown type");
      }
   }

   protected org.w3c.dom.Node getDomNextChild(int type, String namespaceURI, String name, org.w3c.dom.Node childNode) {
      boolean count = false;
      org.w3c.dom.Node child;
      switch(type) {
      case 0:
         return null;
      case 1:
         child = childNode;
         if(childNode != null) {
            child = childNode.getNextSibling();
         }
         break;
      default:
         throw new XmlException("Unknown type");
      }

      while(child != null) {
         if(this.domNodeNameEquals(child, namespaceURI, name)) {
            return child;
         }

         child = child.getNextSibling();
      }

      return null;
   }

   protected org.w3c.dom.Node getDomChild(int type, String namespaceURI, String name) {
      return this.getDomChildAt(type, namespaceURI, name, 0);
   }

   protected org.w3c.dom.Node insertDomChildAt(int type, String namespaceURI, String name, int index, String value) {
      if(type == 0) {
         return this.appendDomChild(type, namespaceURI, name, value);
      } else {
         Element element = this.domNode.getOwnerDocument().createElementNS(namespaceURI, name);
         element.appendChild(this.domNode.getOwnerDocument().createTextNode(value));
         return this.domNode.insertBefore(element, this.getDomChildAt(1, namespaceURI, name, index));
      }
   }

   protected org.w3c.dom.Node insertDomElementAt(String namespaceURI, String name, int index, Node srcNode) {
      srcNode.domNode = this.domNode.insertBefore(this.cloneDomElementAs(namespaceURI, name, srcNode), this.getDomChildAt(1, namespaceURI, name, index));
      return srcNode.domNode;
   }

   protected org.w3c.dom.Node replaceDomChildAt(int type, String namespaceURI, String name, int index, String value) {
      if(type == 0) {
         return this.appendDomChild(type, namespaceURI, name, value);
      } else {
         Element element = this.domNode.getOwnerDocument().createElementNS(namespaceURI, name);
         element.appendChild(this.domNode.getOwnerDocument().createTextNode(value));
         return this.domNode.replaceChild(element, this.getDomChildAt(1, namespaceURI, name, index));
      }
   }

   protected org.w3c.dom.Node replaceDomElementAt(String namespaceURI, String name, int index, Node srcNode) {
      srcNode.domNode = this.domNode.replaceChild(this.cloneDomElementAs(namespaceURI, name, srcNode), this.getDomChildAt(1, namespaceURI, name, index));
      return srcNode.domNode;
   }

   protected org.w3c.dom.Node setDomChildAt(int type, String namespaceURI, String name, String value, int index) {
      int count = 0;
      switch(type) {
      case 0:
         Attr oldAttr = ((Element)this.domNode).getAttributeNodeNS(namespaceURI, name);
         ((Element)this.domNode).setAttributeNS(namespaceURI, name, value);
         return oldAttr;
      case 1:
         NodeList elements = this.domNode.getChildNodes();
         int length = elements.getLength();

         for(int i = 0; i < length; ++i) {
            org.w3c.dom.Node child = elements.item(i);
            if(this.domNodeNameEquals(child, namespaceURI, name) && count++ == index) {
               return child.replaceChild(child.getOwnerDocument().createTextNode(value), child.getFirstChild());
            }
         }

         throw new XmlException("Index out of range");
      default:
         throw new XmlException("Unknown type");
      }
   }

   protected org.w3c.dom.Node setDomChild(int type, String namespaceURI, String name, String value) {
      if(type != 0 && this.getDomChildCount(type, namespaceURI, name) <= 0) {
         this.appendDomChild(type, namespaceURI, name, value);
         return null;
      } else {
         return this.setDomChildAt(type, namespaceURI, name, value, 0);
      }
   }

   protected org.w3c.dom.Node removeDomChildAt(int type, String namespaceURI, String name, int index) {
      int count = 0;
      switch(type) {
      case 0:
         return this.domNode.getAttributes().removeNamedItemNS(namespaceURI, name);
      case 1:
         NodeList elements = this.domNode.getChildNodes();
         int length = elements.getLength();

         for(int i = 0; i < length; ++i) {
            org.w3c.dom.Node child = elements.item(i);
            if(this.domNodeNameEquals(child, namespaceURI, name) && count++ == index) {
               return this.domNode.removeChild(child);
            }
         }

         throw new XmlException("Index out of range");
      default:
         throw new XmlException("Unknown type");
      }
   }

   protected org.w3c.dom.Node appendDomElement(String namespaceURI, String name, Node srcNode) {
      srcNode.domNode = this.domNode.appendChild(this.cloneDomElementAs(namespaceURI, name, srcNode));
      return srcNode.domNode;
   }

   protected Element cloneDomElementAs(String namespaceURI, String name, Node srcNode) {
      Element newDomNode = this.domNode.getOwnerDocument().createElementNS(namespaceURI, name);
      Element srcDomNode = (Element)srcNode.domNode;
      org.w3c.dom.Document doc = newDomNode.getOwnerDocument();
      NodeList list = srcDomNode.getChildNodes();
      int length = list.getLength();

      for(int srcAttributes = 0; srcAttributes < length; ++srcAttributes) {
         newDomNode.appendChild(doc.importNode(list.item(srcAttributes), true));
      }

      NamedNodeMap var12 = srcDomNode.getAttributes();
      NamedNodeMap newAttributes = newDomNode.getAttributes();
      length = var12.getLength();

      for(int i = 0; i < length; ++i) {
         newAttributes.setNamedItemNS((Attr)doc.importNode(var12.item(i), false));
      }

      return newDomNode;
   }

   protected void cloneInto(Element newDomNode) {
      while(this.domNode.getFirstChild() != null) {
         org.w3c.dom.Node srcAttributes = newDomNode.getOwnerDocument().importNode(this.domNode.getFirstChild(), true);
         newDomNode.appendChild(srcAttributes);
         this.domNode.removeChild(this.domNode.getFirstChild());
      }

      NamedNodeMap srcAttributes1 = ((Element)this.domNode).getAttributes();
      NamedNodeMap newAttributes = newDomNode.getAttributes();

      while(srcAttributes1.getLength() > 0) {
         org.w3c.dom.Node n = srcAttributes1.item(0);
         newAttributes.setNamedItem(newDomNode.getOwnerDocument().importNode(n, true));
         srcAttributes1.removeNamedItem(n.getNodeName());
      }

      this.domNode = newDomNode;
   }

   protected static String lookupPrefix(org.w3c.dom.Node node, String URI) {
      if(node != null && URI != null && !URI.equals("")) {
         if(node.getNodeType() != 1) {
            return node.getNodeType() == 2?lookupPrefix(((Attr)node).getOwnerElement(), URI):null;
         } else {
            NamedNodeMap attrs = node.getAttributes();
            if(attrs != null) {
               int len = attrs.getLength();

               for(int i = 0; i < len; ++i) {
                  Attr attr = (Attr)attrs.item(i);
                  String name = attr.getName();
                  String value = attr.getValue();
                  if(value != null && value.equals(URI) && name.startsWith("xmlns:")) {
                     return name.substring(6);
                  }
               }
            }

            return lookupPrefix(node.getParentNode(), URI);
         }
      } else {
         return null;
      }
   }

   protected static String lookupDefaultNamespace(org.w3c.dom.Node node) {
      if(node == null) {
         return null;
      } else {
         if(node.getNodeType() == 1) {
            Attr attr = ((Element)node).getAttributeNode("xmlns");
            if(attr != null) {
               return attr.getValue();
            }
         }

         return lookupDefaultNamespace(node.getParentNode());
      }
   }

   protected static void internalAdjustPrefix(org.w3c.dom.Node node, boolean qualified) {
      if(node != null) {
         if(qualified) {
            String element = lookupPrefix(node, node.getNamespaceURI());
            if(element != null) {
               node.setPrefix(element);
            }
         } else if(node.getNodeType() == 1) {
            Element element1 = (Element)node;
            if(lookupDefaultNamespace(element1) != null) {
               element1.setAttribute("xmlns", "");
            }
         }

      }
   }

   public abstract void adjustPrefix();
}
