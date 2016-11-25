package com.experian.rfps.rules;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.NamespaceContext;

public class SimpleNamespaceContext implements NamespaceContext {
   private Map<String, String> urisByPrefix = new HashMap();
   private Map<String, Set> prefixesByURI = new HashMap();

   public SimpleNamespaceContext() {
      this.addNamespace("xml", "http://www.w3.org/XML/1998/namespace");
      this.addNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
   }

   public synchronized void addNamespace(String prefix, String namespaceURI) {
      this.urisByPrefix.put(prefix, namespaceURI);
      if(this.prefixesByURI.containsKey(namespaceURI)) {
         ((Set)this.prefixesByURI.get(namespaceURI)).add(prefix);
      } else {
         HashSet set = new HashSet();
         set.add(prefix);
         this.prefixesByURI.put(namespaceURI, set);
      }

   }

   public String getNamespaceURI(String prefix) {
      if(prefix == null) {
         throw new IllegalArgumentException("prefix cannot be null");
      } else {
         return this.urisByPrefix.containsKey(prefix)?(String)this.urisByPrefix.get(prefix):"";
      }
   }

   public String getPrefix(String namespaceURI) {
      return (String)this.getPrefixes(namespaceURI).next();
   }

   public Iterator getPrefixes(String namespaceURI) {
      if(namespaceURI == null) {
         throw new IllegalArgumentException("namespaceURI cannot be null");
      } else {
         return this.prefixesByURI.containsKey(namespaceURI)?((Set)this.prefixesByURI.get(namespaceURI)).iterator():Collections.EMPTY_SET.iterator();
      }
   }
}
