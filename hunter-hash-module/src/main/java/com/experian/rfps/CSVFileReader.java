package com.experian.rfps;

import au.com.bytecode.opencsv.CSVReader;
import com.experian.rfps.ICheckRow;
import com.experian.rfps.Table;
import com.experian.rfps.XmlLineTable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class CSVFileReader {
   private static final Logger log = Logger.getLogger(CSVFileReader.class);
   private CSVReader reader = null;
   private File file = null;
   private Table table = null;
   private String encode = null;

   public CSVFileReader(String filename, Table table, String encode) throws FileNotFoundException {
      InputStreamReader isd = null;

      try {
         this.encode = encode;
         this.file = new File(filename);
         isd = new InputStreamReader(new FileInputStream(this.file), encode);
         this.reader = new CSVReader(isd);
         this.table = table;
      } catch (UnsupportedEncodingException var6) {
         java.util.logging.Logger.getLogger(CSVFileReader.class.getName()).log(Level.SEVERE, (String)null, var6);
      }

   }

   public int writeToXml(PrintWriter outstr, int maxRows, ICheckRow classCheck) throws Exception {
      XmlLineTable xmlLineTable = new XmlLineTable(outstr, this.table);
      if(maxRows < 1) {
         throw new Exception("The MaxRows number should be more than 0");
      } else {
         int size = 0;
         String[] line = (String[])null;
         xmlLineTable.writeHeader();

         for(int row = 0; row < maxRows; ++row) {
            line = this.reader.readNext();
            if(line == null) {
               break;
            }

            ++size;
            if(!classCheck.check(line)) {
               log.info("The row with errors is transferred to a file.err under number " + size);
            } else {
               xmlLineTable.writeLine(line);
            }
         }

         xmlLineTable.writeEnd();
         return size;
      }
   }

   protected void finalize() throws Throwable {
      this.close();
      super.finalize();
   }

   public void close() {
      try {
         if(this.reader != null) {
            this.reader.close();
         }
      } catch (IOException var2) {
         ;
      }

   }
}
