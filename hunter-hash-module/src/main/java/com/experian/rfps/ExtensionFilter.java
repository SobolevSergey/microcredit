package com.experian.rfps;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExtensionFilter implements FilenameFilter {
   List extensions = new ArrayList();

   public ExtensionFilter(String[] extList) {
      for(int i = 0; i < extList.length; ++i) {
         this.extensions.add("." + extList[i].toUpperCase());
      }

   }

   public boolean accept(File dir, String name) {
      boolean result = false;
      Iterator iter = this.extensions.iterator();

      while(iter.hasNext() && !(result = name.toUpperCase().endsWith((String)iter.next()))) {
         ;
      }

      return result;
   }
}
