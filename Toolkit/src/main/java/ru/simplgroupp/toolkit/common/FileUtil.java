package ru.simplgroupp.toolkit.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileUtil {
	
	/**
	 * пишем файл
	 * @param filePath - путь к файлу
	 * @param fileContent - содержимое файла
	 */
	public static File writeToFile(String filePath,String fileContent) throws FileNotFoundException{
		File file=new File(filePath);
		return writeToFile(file,fileContent);
	}

	/**
	 * пишем файл
	 * @param file - файл
	 * @param fileContent - содержимое файла
	 */
	public static File writeToFile(File file,String fileContent) throws FileNotFoundException{
		
		PrintWriter out = new PrintWriter(file.getAbsoluteFile());
		try {
            out.print(fileContent);
        } finally {
        	IOUtils.closeQuietly(out);
	
        }
		return file;
	}
	
	/**
	 * пытаемся удалить файл
	 * @param fileName - имя файла
	 */
	public static void deleteFile(String fileName){
		File file=new File(fileName);
		if (file.exists()){
			FileUtils.deleteQuietly(file);
		}
	}
}
