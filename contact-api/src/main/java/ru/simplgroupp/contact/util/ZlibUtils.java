package ru.simplgroupp.contact.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Created by aro on 12.09.2014.
 */
public class ZlibUtils {
    private static final Logger logger = Logger.getLogger(ZlibUtils.class.getName());

    public static byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();

        deflater.end();

        logger.log(Level.FINEST, "Original: " + data.length / 1024 + " Kb");
        logger.log(Level.FINEST, "Compressed: " + output.length / 1024 + " Kb");
        return output;
    }

    public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();

        inflater.end();

        logger.log(Level.FINEST, "Original: " + data.length);
        logger.log(Level.FINEST, "Uncompressed: " + output.length);
        return output;
    }
    public static void decompressIntoFileV2(byte[] data,String fn) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        FileOutputStream outputStream = new FileOutputStream(fn);
        byte[] buffer = new byte[1024];
        long size = 0;
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
            size+=count;
        }
        outputStream.close();

        inflater.end();

        AUtils.transform(new File(fn),AUtils.CHARSET_1251,new File(fn+"_utf8"),"UTF-8");

        logger.log(Level.FINEST, "Original: " + data.length);
        logger.log(Level.FINEST, "Uncompressed: " + size );
    }


    public static void decompressIntoFile(byte[] data,String fn) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        //ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        FileOutputStream out = new FileOutputStream(fn);
        String srus = null;
        String sutf = null;
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            srus = new String(buffer,AUtils.CHARSET_1251);
            sutf = new String(srus.getBytes(),"UTF-8");
            out.write(sutf.getBytes(), 0, sutf.getBytes().length);
        }
        out.close();
        //byte[] output = outputStream.toByteArray();

        inflater.end();

        logger.log(Level.FINEST, "Original: " + data.length);
        //logger.log(Level.FINEST, "Uncompressed: " + output.length);
        //return output;
    }

}
