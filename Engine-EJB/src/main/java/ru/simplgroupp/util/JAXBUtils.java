package ru.simplgroupp.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JAXBUtils {
	
	private static final Logger logger = Logger.getLogger(JAXBUtils.class.getName());
	
	public static <T> T unmarshall(Class<T> clz, String sourceXml) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            T cdtrn = (T) jaxbUnmarshaller.unmarshal(new StringReader(sourceXml));
            logger.finest(cdtrn.toString());
            return cdtrn;
        } catch (JAXBException e) {
            logger.log(Level.SEVERE, "Не парсится xml", e);
            return null;
        }
        		
	}
	
	public static <T> T unmarshall(Unmarshaller jaxbUnmarshaller, String sourceXml) {
        try {
            T cdtrn = (T) jaxbUnmarshaller.unmarshal(new StringReader(sourceXml));
            logger.finest(cdtrn.toString());
            return cdtrn;
        } catch (JAXBException e) {
            logger.log(Level.SEVERE, "Не парсится xml", e);
            return null;
        }
        		
	}
	
	public static Unmarshaller prepareUnmarshall(Class[] clz) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clz);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return jaxbUnmarshaller;
		} catch (JAXBException e) {
            logger.log(Level.SEVERE, "Не создаётся unmarshaller", e);
            return null;
        }
	}	
	
	public static Unmarshaller prepareUnmarshall(Class clz) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clz);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return jaxbUnmarshaller;
		} catch (JAXBException e) {
            logger.log(Level.SEVERE, "Не создаётся unmarshaller", e);
            return null;
        }
	}
	
	public static Marshaller prepareMarshall(Class clz) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clz);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();	
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);	
			return jaxbMarshaller;
		} catch (JAXBException e) {
            logger.log(Level.SEVERE, "Не создаётся marshaller", e);
            return null;
        }		
	}
	
	public static Marshaller prepareMarshall(Class[] clz) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clz);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();	
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);	
			return jaxbMarshaller;
		} catch (JAXBException e) {
            logger.log(Level.SEVERE, "Не создаётся marshaller", e);
            return null;
        }		
	}

	
	public static <T> String marshall(Marshaller jaxbMarshaller,T source) {
		try {		
			StringWriter wrt = new StringWriter();		
			jaxbMarshaller.marshal(source, wrt);
			String sxml1 = wrt.toString();	
			return sxml1;
		} catch (JAXBException e) {
            logger.log(Level.SEVERE, "Не записывается xml", e);
            return null;
		}
	}	
	
	public static <T> String marshall(Class<T> clz,T source) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clz);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();	
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);	
			
			StringWriter wrt = new StringWriter();		
			jaxbMarshaller.marshal(source, wrt);
			String sxml1 = wrt.toString();	
			return sxml1;
		} catch (JAXBException e) {
            logger.log(Level.SEVERE, "Не записывается xml", e);
            return null;
		}
	}
}
