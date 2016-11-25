package com.experian.rfps.config;

import com.altova.xml.Document;
import com.altova.xml.Node;

public class RFPSDoc extends Document {
   public void declareNamespaces(Node node) {
      this.declareNamespace(node, "", "urn:mclsoftware.co.uk:hunterII");
   }
}
