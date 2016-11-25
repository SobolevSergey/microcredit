package com.experian.rfps;

import au.com.bytecode.opencsv.CSVWriter;
import com.experian.rfps.ICheckRow;
import com.experian.rfps.PrintWriterExt;
import com.experian.rfps.Table;
import com.experian.rfps.Transformation;
import com.experian.rfps.XMLLoader;
import com.experian.rfps.XSLTransformer;
import com.experian.rfps.XmlLineTable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CheckRow implements ICheckRow {
   private static final Logger log = Logger.getLogger(CheckRow.class);
   private XMLLoader loaderCSV;
   private XMLLoader loaderInput;
   private XSLTransformer transformerCSVtoXML;
   private File csvFile = File.createTempFile("$csv", "xml");
   private XmlLineTable xmlFile;
   private File inputFile = File.createTempFile("$inp", "xml");
   private PrintWriterExt printWriter;
   private Table table;
   private Transformation currentTransformation;
   private RandomAccessFile rndCSV;
   private RandomAccessFile rndInput;
   private BufferedInputStream xmlInputCSV;
   private BufferedOutputStream xmlOutput;
   private BufferedInputStream xmlInput;
   private String nameErrorRows;
   private CSVWriter csvWriter;
   private File fileError;
   private PrintWriter errWriter;

   public CheckRow(Transformation trans, Table table, XMLLoader loader, XSLTransformer transformer, XMLLoader loaderCheck, String name) throws IOException {
      this.rndCSV = new RandomAccessFile(this.csvFile, "rw");
      this.rndInput = new RandomAccessFile(this.inputFile, "rw");
      this.nameErrorRows = trans.getInputFileError() + System.getProperty("file.separator") + name + ".err";
      this.printWriter = new PrintWriterExt(new FileOutputStream(this.rndCSV.getFD()), "UTF-8");
      this.xmlOutput = new BufferedOutputStream(new FileOutputStream(this.rndInput.getFD()));
      this.table = table;
      this.loaderCSV = loader;
      this.transformerCSVtoXML = transformer;
      this.loaderInput = loaderCheck;
      this.currentTransformation = trans;
   }

   public boolean check(String[] row) throws Exception {
      boolean result = false;

      try {
         this.rndCSV.setLength(0L);
         this.rndInput.setLength(0L);
         this.xmlFile = new XmlLineTable(this.printWriter, this.table);
         this.xmlFile.writeHeader();
         this.xmlFile.writeLine(row);
         this.xmlFile.writeEnd();

         try {
            this.xmlInputCSV = new BufferedInputStream(new FileInputStream(this.csvFile));
            Document ex = this.loaderCSV.getBuilder().parse(this.xmlInputCSV);
            this.rndCSV.setLength(0L);
            StreamResult xmlresult = new StreamResult(this.xmlOutput);
            DOMSource domSource = new DOMSource(ex);
            this.transformerCSVtoXML.getTransformer().transform(domSource, xmlresult);
            this.xmlInput = new BufferedInputStream(new FileInputStream(this.inputFile));
            ex = this.loaderInput.getBuilder().parse(this.xmlInput);
            if(this.loaderInput.isValid() && this.currentTransformation.makeTransformationInput(ex, 3, Logger.getRootLogger().getLevel())) {
               result = true;
            }
         } catch (SAXException var7) {
            throw new Exception("SAX Error parse xml data.", var7);
         } catch (IOException var8) {
            throw new Exception("IO Error parse xml data ", var8);
         } catch (TransformerException var9) {
            throw new Exception("XSLT Error transformation xml data ", var9);
         }

         if(!result) {
            if(this.csvWriter == null) {
               try {
                  this.fileError = new File(this.nameErrorRows);
                  if(this.fileError.exists()) {
                     this.fileError.delete();
                  }

                  this.fileError.createNewFile();
                  this.errWriter = new PrintWriter(new FileOutputStream(this.fileError));
                  this.csvWriter = new CSVWriter(this.errWriter, ',');
               } catch (Exception var6) {
                  throw new Exception("Error created the file " + this.nameErrorRows, var6);
               }
            }

            this.csvWriter.writeNext(row);
         }

         return result;
      } catch (Exception var10) {
         throw new Exception("Error created one row xml-file from csv-file.", var10);
      }
   }

   public void close() throws IOException {
      this.rndCSV.close();
      this.rndInput.close();
      this.inputFile.delete();
      this.csvFile.delete();
      if(this.csvWriter != null) {
         this.csvWriter.close();
         this.errWriter.close();
      }

   }
}
