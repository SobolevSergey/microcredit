package ru.simplgroupp.toolkit.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ZipUtils {

	/**
	 * пакуем один файл
	 * @param filename - файл
	 * @throws IOException
	 */
	public static byte[] ZipSingleFile(File filename) throws IOException
	{
		
		ByteArrayOutputStream fout = new ByteArrayOutputStream(); 
    	ZipOutputStream zos = new ZipOutputStream(fout);
    	try 
    	{
    		ZipEntry ze = new ZipEntry(filename.getName());
    		zos.putNextEntry(ze);
    		IOUtils.write(FileUtils.readFileToByteArray(filename), zos);
    		zos.closeEntry();

    	}
    	finally {
    		zos.flush();
    		zos.close();
    		fout.flush();
    		fout.close();
    	}

    	byte[] bs = fout.toByteArray();
    	return bs;

	}
	
	/**
	 * пакуем несколько файлов
	 * @param lstfile - файлы для пакования
	 * @param target - файл, куда пакуем
	 * @throws IOException
	 */
	public static void ZipMultiFiles(List<File> lstfile, File target) throws IOException
	{
		FileOutputStream fout=new FileOutputStream(target); 
    	ZipOutputStream zos = new ZipOutputStream(fout);
    	for (File file:lstfile){
    	
    		ZipEntry ze = new ZipEntry(file.getName());
    		zos.putNextEntry(ze);
    		IOUtils.write(FileUtils.readFileToByteArray(file), zos);
    		zos.closeEntry();

    	}
    	
       	zos.flush();
    	zos.close();
	
  }
	
	/**
	 * пакуем несколько текстовых файлов
	 * @param source - список файлов
	 * @param prefix - название файла
	 * @param suffix - расширение файла
	 * @param target - файл, куда пишем
	 * @throws IOException
	 */
	public static void zipTxtFiles(List<String> source, String prefix, String suffix, File target) throws IOException {
		FileOutputStream fout=new FileOutputStream(target);
		ZipOutputStream zout=new ZipOutputStream(new BufferedOutputStream(fout));
		int n = 0;
		for (String stxt: source) {
			n++;
			zout.putNextEntry(new ZipEntry( prefix + String.valueOf(n) + suffix ));
			StringReader rdrText = new StringReader(stxt);
			IOUtils.copy(rdrText, zout);
			zout.closeEntry();
		}
		zout.close();
	}
	
	/**
	 * пакуем один файл
	 * @param source - строка, которую пакуем
	 * @param filename - файл
	 * @throws IOException
	 */
	public static byte[] ZipSingleFile(String source,String filename) throws IOException
	{
		
		ByteArrayOutputStream fout = new ByteArrayOutputStream(); 
    	ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fout));
    	try 
    	{
    		ZipEntry ze = new ZipEntry(filename);
    		zos.putNextEntry(ze);
    		StringReader rdrText = new StringReader(source);
			IOUtils.copy(rdrText, zos);
    		zos.closeEntry();

    	}
    	finally {
    		zos.flush();
    		zos.close();
    		fout.flush();
    		fout.close();
    	}

    	byte[] bs = fout.toByteArray();
    	return bs;

	}
	
	/**
	 * распаковываем один файл
	 * @param zipbyte - откуда распаковываем
	 * @throws IOException
	 */
	public static void unzipSingleFile(byte[] zipbyte) throws IOException
	{
		BufferedOutputStream bos = null;
		ByteArrayInputStream bais = new ByteArrayInputStream(zipbyte);
		ZipInputStream zis = new ZipInputStream(bais);
		int count;
		byte data[] = new byte[16000];
		ZipEntry ze;

	    ze = zis.getNextEntry();
		  
	    String zeName = ze.getName();
	    FileOutputStream fos = new FileOutputStream(zeName);
	    bos = new BufferedOutputStream(fos, 16000);
	    while( (count = zis.read(data, 0, 16000) ) != -1 ) {
	      bos.write(data, 0, count);
	    }
	    bos.flush();
	    bos.close();
	    zis.close();
	}
	
	/**
	 * распаковываем один файл из архива с несколькими файлами
	 * @param zipfile - откуда распаковываем
	 * @param entryFileName - какой файл достать
	 * @throws IOException
	 */
	public static byte[] unzipSingleFile(File zipfile, String entryFileName) throws IOException {
		try(FileInputStream fInput = new FileInputStream(zipfile);
				ZipInputStream zipInput = new ZipInputStream(fInput);
				ByteArrayOutputStream fout = new ByteArrayOutputStream(); )
		{
			ZipEntry entry = zipInput.getNextEntry();
            
			while(entry != null){
				String entryName = entry.getName();
				if(entryFileName.equals(entryName)) {
					IOUtils.copy(zipInput, fout);
					break;
				}
				// закрываем ZipEntry и берем следующий
				zipInput.closeEntry();
				entry = zipInput.getNextEntry();
			}
            
			// закрываем последний ZipEntry
			zipInput.closeEntry();
			
			byte[] bs = fout.toByteArray();
	    	return bs;
		}
	}

}
