package com.experian.rfps;

import com.experian.rfps.Field;
import com.experian.rfps.Table;
import java.io.PrintWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.log4j.Logger;

public class XmlLineTable {
   private static final Logger log = Logger.getLogger(XmlLineTable.class);
   private XMLOutputFactory outputFactory;
   private XMLStreamWriter writer;
   private PrintWriter outstr;
   private Table table;

   public XmlLineTable(PrintWriter outfile, Table table) throws XMLStreamException {
      this.outstr = outfile;
      this.table = table;
      this.outputFactory = XMLOutputFactory.newInstance();
   }

   public void setOutstr(PrintWriter outstr) {
      this.outstr = outstr;
   }

   public void writeHeader() throws XMLStreamException {
      this.writer = this.outputFactory.createXMLStreamWriter(this.outstr);
      this.writer.setPrefix("", "urn:mclsoftware.co.uk:hunterII");
      this.writer.writeStartDocument("UTF-8", "1.0");
      this.writer.writeStartElement("BATCH");
      this.writer.writeStartElement("ROWS");
   }

   public void writeLine(String[] line) throws XMLStreamException, Exception {
      if(line != null) {
         this.writer.writeStartElement("ROW");

         for(int col = 0; col < line.length; ++col) {
            if(line[col].length() != 0) {
               Field f = this.table.getField(col + 1);
               if(f == null) {
                  String msg = "Not found column number " + (col + 1) + "  of Table " + this.table.getName();
                  log.error(msg);
                  throw new Exception(msg);
               }

               if(col == 0 && line[col].length() > 0 && line[col].codePointAt(0) == '\ufeff') {
                  line[col] = line[col].substring(1);
               }

               this.writer.writeStartElement(f.getName());
               this.writer.writeAttribute("v", line[col]);
               this.writer.writeEndElement();
               if(log.isTraceEnabled()) {
                  log.trace(f.getName() + " v=" + line[col]);
               }
            }
         }

         this.writer.writeEndElement();
      }

   }

   public void writeEnd() throws XMLStreamException {
      Table tableOfvalues = this.table.getAdditionFields();
      if(tableOfvalues != null) {
         this.writer.writeStartElement("STATIC_FIELDS");

         for(int i = 0; i < tableOfvalues.getSize(); ++i) {
            Field field = tableOfvalues.getField(i);
            if(field != null) {
               this.writer.writeStartElement(field.getName());
               this.writer.writeAttribute("v", field.getValue());
               this.writer.writeEndElement();
            }
         }

         this.writer.writeEndElement();
      }

      this.writer.writeEndElement();
      this.writer.writeEndElement();
      this.writer.writeEndDocument();
      this.writer.flush();
   }
}
