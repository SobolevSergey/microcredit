package com.experian.rfps;

import java.io.File;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;

public class XSLTransformer {
   private static final Logger log = Logger.getLogger(XSLTransformer.class);
   private TransformerFactory trnfactory = TransformerFactory.newInstance();
   private Transformer transformer = null;
   private String xsltFile;
   private Source xsltSource;

   private XSLTransformer() throws TransformerConfigurationException {
      this.transformer = this.trnfactory.newTransformer();
      this.trnfactory.setErrorListener(new XSLTransformer.ClassListiner((XSLTransformer.ClassListiner)null));
   }

   private XSLTransformer(String xsltFile) throws TransformerConfigurationException {
      this.xsltFile = xsltFile;
      this.xsltSource = new StreamSource(new File(xsltFile));
      this.transformer = this.trnfactory.newTransformer(this.xsltSource);
      this.trnfactory.setErrorListener(new XSLTransformer.ClassListiner((XSLTransformer.ClassListiner)null));
   }

   public static XSLTransformer getInstance(String xsltFile) throws TransformerConfigurationException {
      return new XSLTransformer(xsltFile);
   }

   public static XSLTransformer getInstance() throws TransformerConfigurationException {
      return new XSLTransformer();
   }

   public Transformer getTransformer() {
      return this.transformer;
   }

   private class ClassListiner implements ErrorListener {
      private ClassListiner() {
      }

      public void warning(TransformerException exception) throws TransformerException {
         XSLTransformer.log.warn("XSLT transformator:" + XSLTransformer.this.xsltFile + ". Warning:" + exception.getMessage());
      }

      public void error(TransformerException exception) throws TransformerException {
         XSLTransformer.log.warn("XSLT transformator:" + XSLTransformer.this.xsltFile + ". Error:" + exception.getMessage());
      }

      public void fatalError(TransformerException exception) throws TransformerException {
         XSLTransformer.log.warn("XSLT transformator:" + XSLTransformer.this.xsltFile + ". Fatal Error:" + exception.getMessage());
      }

      // $FF: synthetic method
      ClassListiner(XSLTransformer.ClassListiner var2) {
         this();
      }
   }
}
