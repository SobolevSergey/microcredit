package ru.simplgroupp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.simplgroupp.exception.KassaException;

public class FTPUtils {
	
	final static Logger log = LoggerFactory.getLogger(FTPUtils.class.getName());
	
	/**
	 * @param usr - имя пользователя
	 * @param pwd - пароль
	 * @param host - сервер
	 * @param dir - директория
	 * @param fileName - имя файла на сервере
	 * @param localFileName - имя файла локальное
	 * @param port - порт (может быть, не нужен этот параметр)
	 * @param toFtp - если true, то закидываем на сайт, если false, то берем
	 */
	public static boolean UploadFtp(String usr,String pwd,String host, String dir, String fileName, String localFileName,Integer port,Boolean toFtp) 
		  throws KassaException { 
		
	  boolean success=false;	
	  FTPClient fclient = new FTPClient();
	  
	  try {
	
		if (port!=null){  
		  fclient.connect(host, port);
		} else {
		  fclient.connect(host); 	
		}
		
	    if (!fclient.login(usr, pwd)) {
	    	fclient.disconnect();
	    	return false;
	    }
	    
	    int reply = fclient.getReplyCode();
	    if (!FTPReply.isPositiveCompletion(reply)) {
            fclient.disconnect();
            return false;
        }
	    
	    log.info("установили соединение с сервером "+host);
	    
	    fclient.enterLocalPassiveMode();
        fclient.setFileType(FTP.BINARY_FILE_TYPE);
        if (StringUtils.isNotEmpty(dir)) {
          fclient.changeWorkingDirectory(dir);
        }
        File dfile=null;
        
        //если мы пишем на ftp
        if (toFtp) {
             dfile = new File(localFileName);
             InputStream inputStream = new FileInputStream(dfile);
             success=fclient.storeFile(fileName, inputStream);
             inputStream.close();
             log.info("записали файл на сервер "+host);
        }  else {
        	 dfile = new File(localFileName);
        	 FileOutputStream ost = new FileOutputStream(dfile);
             success = fclient.retrieveFile(fileName, ost);
             ost.close();
             log.info("получили файл с сервера "+host);
        }
        
	   
	  }  
	  catch (IOException e) {
		 log.error("Не удалось отправить файл "+e); 
	     throw new KassaException("Не удалось отправить файл");
	  }
	  finally
	  { 
		  try {
              if (fclient.isConnected()) {
                  fclient.logout();
                  fclient.disconnect();
                  log.info("закрыли соединение с сервером "+host);
              }
          } catch (IOException ex) {
        	  log.error("Не удалось закрыть соединение "+ex); 
        	  throw new KassaException("Не удалось закрыть соединение "+ex);
          }
	  }
	  return success;
	}


}
