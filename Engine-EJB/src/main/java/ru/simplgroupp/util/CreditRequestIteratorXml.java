package ru.simplgroupp.util;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.toolkit.common.FileDataHolder;
import ru.simplgroupp.transfer.CreditRequest;

public class CreditRequestIteratorXml implements Iterator<CreditRequest> {

	private static final Logger logger = Logger.getLogger(CreditRequestIteratorXml.class.getName());
	
	private int curIdx = -1;
	private List<Element> xmlSource;
	
	private KassaBeanLocal kassaBean;
	private JAXBContext jaxbContext;
	private Unmarshaller jaxbUnmarshaller;
	
	public CreditRequestIteratorXml(List<FileDataHolder> source, KassaBeanLocal kassaBean) throws JAXBException  {
		super();
		this.kassaBean = kassaBean;
		
		// читаем xml
		xmlSource = new ArrayList<Element>(source.size() * 50);
		for (FileDataHolder fdh: source) {
			
			try {
				String sxml = new String(fdh.getData(), "UTF-8");
				Document xdoc = DocumentHelper.parseText(sxml);
				if (ModelKeys.XML_CRS_ROOT_ELEMENT.equals(xdoc.getRootElement().getName())) {
					List<Element> lst = xdoc.getRootElement().elements(ModelKeys.XML_CR_ELEMENT);
					xmlSource.addAll(lst);
				} else if (ModelKeys.XML_CR_ELEMENT.equals(xdoc.getRootElement().getName())) {
					xmlSource.add(xdoc.getRootElement());
				}
			} catch (UnsupportedEncodingException e) {
				logger.severe("Не удалось сохранить xml в utf-8");
			} catch (DocumentException e) {
				logger.severe("Не удалось создать xml-документ");
			}
			
			curIdx = -1;
		}
		
		jaxbContext = JAXBContext.newInstance("ru.simplgroupp.transfer");
		jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	}
	
	public int getCount() {
		return xmlSource.size();
	}
	
	@Override
	public boolean hasNext() {
		return (xmlSource.size() > 0 && curIdx < (xmlSource.size()-1));
	}

	@Override
	public CreditRequest next() {
		if (xmlSource.size() == 0 || curIdx >= xmlSource.size()) {
			return null;
		}
		curIdx++;
		
		try {
			String sxml = xmlSource.get(curIdx).asXML();
			sxml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + sxml;
			StringReader rdr = new StringReader(sxml);			
			CreditRequest ccRequest = (CreditRequest) jaxbUnmarshaller.unmarshal(rdr);
			return ccRequest;
		} catch (JAXBException e) {
			logger.severe("Не удалось unmarshal xml ");
			return null;
		}
	}

	@Override
	public void remove() {
		
	}
}
