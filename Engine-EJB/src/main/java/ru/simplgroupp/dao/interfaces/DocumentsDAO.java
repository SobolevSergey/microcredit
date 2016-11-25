package ru.simplgroupp.dao.interfaces;

import java.util.Set;

import javax.ejb.Local;

import ru.simplgroupp.persistence.DocumentEntity;
import ru.simplgroupp.persistence.DocumentMediaEntity;
import ru.simplgroupp.transfer.DocumentMedia;
import ru.simplgroupp.transfer.Documents;

@Local
public interface DocumentsDAO {
	/**
	 * получить документ
	 * 
	 * @param id - id документа
	 * @return
	 */
	public DocumentEntity getDocument(Integer id);
	/**
	 * получить документ
	 * 
	 * @param id - id документа
	 * @param options - что инициализируем
	 * @return
	 */
	public Documents getDocument(Integer id, Set options);
	/**
	 * получить страницу документа
	 * 
	 * @param id - id страницы документа
	 * @return
	 */
	public DocumentMediaEntity getDocumentMedia(Integer id);
	/**
	 * получить страницу документа
	 * 
	 * @param id - id страницы документа
	 * @param options - что инициализируем
	 * @return
	 */		
	public DocumentMedia getDocumentMedia(Integer id,Set options);
	/**
	 * ищем максимальный номер страницы документа
	 * 
	 * @param documentId - id документа
	 * @return
	 */
	public Integer findMaxDocPageNumber(Integer documentId);
	/**
	 * сохраняем скан страницы документа
	 * 
	 * @param peopleMainId - id человека
	 * @param scanTypeId - вид скана документа
	 * @param documentId - id документа
	 * @param pageNumber - номер страницы
	 * @param mimeType - тип mime
	 * @param mediaPath - путь, если храним не в таблице
	 * @param mediaData - данные
	 * @return
	 */
	public DocumentMediaEntity saveDocumentPage(DocumentMediaEntity docMedia,Integer peopleMainId,Integer scanTypeId,
			Integer documentId,Integer pageNumber,String mimeType,String mediaPath,byte[] mediaData);
	/**
	 * добавляем скан страницы документа
	 * 
	 * @param docMedia - скан
	 * @param peopleMainId - id человека
	 * @return
	 */
	public DocumentMedia addDocumentMedia(DocumentMedia docMedia,Integer peopleMainId);
	/**
	 * удалить страницу документа
	 * 
	 * @param id - id страницы
	 */
	public void deleteDocumentMedia(Integer id);
	/**
	 * удалить все страницы документа
	 * 
	 * @param documentId - id документа
	 */
	public void deleteDocumentMediaAll(Integer documentId);
	/**
	 * сохраняем документ
	 * @param document - документ
	 * @return
	 */
	public DocumentEntity saveDocument(DocumentEntity document);
	/**
	 * сохраняем действительность документа
	 * @param document - документ
	 * @param validity - признак действительности (пишем reasonend)
	 * @return
	 */
	public DocumentEntity saveDocumentValidity(DocumentEntity document,Integer validity);
}
