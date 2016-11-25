package com.experian.rfps;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Searcher {
   private List<File> list = new LinkedList();

   public Searcher() {
      this.list.add(new File("."));
   }

   public Searcher(String paths) {
      this.list.add(new File("."));
      if(paths != null) {
         String[] entries = paths.split(File.pathSeparator);
         String[] var6 = entries;
         int var5 = entries.length;

         for(int var4 = 0; var4 < var5; ++var4) {
            String entryName = var6[var4];
            this.add(entryName);
         }
      }

   }

   public boolean add(String path) {
      if(".".equalsIgnoreCase(path)) {
         return false;
      } else {
         File file = new File(path);
         if(file.isDirectory()) {
            this.list.add(file);
            return true;
         } else {
            return false;
         }
      }
   }

   public File findFile(String nameFile) {
      File file = null;
      Iterator iter = this.list.iterator();

      while(iter.hasNext()) {
         File f = (File)iter.next();
         file = scanDir("", f, nameFile);
         if(file != null) {
            break;
         }
      }

      return file;
   }

   public String findNameFile(String nameFile) {
      File file = null;
      File fname = new File(nameFile);
      if(fname.exists()) {
         return nameFile;
      } else {
         String nameSearch = fname.getName();
         Iterator iter = this.list.iterator();

         while(iter.hasNext()) {
            File f = (File)iter.next();
            file = scanDir("", f, nameSearch);
            if(file != null) {
               break;
            }
         }

         return file != null?file.getAbsolutePath():null;
      }
   }

   public static File scanDir(String pkg, File dir, String nameConfig) {
      System.out.println("Transformation### scanDir-pkg" + pkg);
      System.out.println("Transformation### scanDir-dir" + dir);
      System.out.println("Transformation### scanDir-nameConfig" + nameConfig);
      File file = null;
      File[] var7;
      int var6 = (var7 = dir.listFiles()).length;

      for(int var5 = 0; var5 < var6; ++var5) {
         File f = var7[var5];
         if(f.isDirectory()) {
            file = scanDir(pkg + f.getName() + File.separator, f, nameConfig);
            if(file != null) {
               break;
            }
         } else if(nameConfig.equalsIgnoreCase(f.getName())) {
            file = f;
            break;
         }
      }

      return file;
   }
}
