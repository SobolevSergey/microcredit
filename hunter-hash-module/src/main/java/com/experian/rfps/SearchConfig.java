package com.experian.rfps;

import java.io.FileNotFoundException;
import java.net.URL;

public class SearchConfig {
   private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

   public String getPathFile(String fileName) throws FileNotFoundException {
      String configFilePath = null;
      URL configFileURL = Thread.currentThread().getContextClassLoader().getResource(fileName);
      if(configFileURL != null) {
         configFilePath = configFileURL.getFile();
      }

      return configFilePath;
   }
}
