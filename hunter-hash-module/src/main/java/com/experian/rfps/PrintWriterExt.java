package com.experian.rfps;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class PrintWriterExt extends PrintWriter {
   public PrintWriterExt(FileOutputStream fos, String csn) throws FileNotFoundException, UnsupportedEncodingException {
      super(new BufferedWriter(new OutputStreamWriter(fos, csn)), false);
   }
}
