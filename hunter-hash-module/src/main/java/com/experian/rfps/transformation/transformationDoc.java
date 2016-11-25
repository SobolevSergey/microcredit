package com.experian.rfps.transformation;

import com.altova.xml.Document;
import com.altova.xml.Node;

public class transformationDoc extends Document {
   public void declareNamespaces(Node node) {
      this.declareNamespace(node, "", "urn:mclsoftware.co.uk:hunterII");
   }
}
