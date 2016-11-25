package ru.simplgroupp.dao.interfaces;

import java.util.Set;

import javax.ejb.Local;

import ru.simplgroupp.persistence.OfficialDocumentsEntity;
import ru.simplgroupp.transfer.OfficialDocuments;

@Local
public interface OfficialDocumentsDAO {

	/**
	 * получить документ
	 * 
	 * @param id - id документа
	 * @return
	 */
	public OfficialDocumentsEntity getOfficialDocumentEntity(Integer id);
	/**
	 * получить документ
	 * 
	 * @param id - id документа
	 * @param options - что инициализируем
	 * @return
	 */
	public OfficialDocuments getOfficialDocument(Integer id, Set options);
	/**
	 * ищем черновик подписанного документа
	 * 
	 * @param peopleMainId - id человека
	 * @param typeId - вид документа
	 * @return
	 */
	public OfficialDocumentsEntity findOfficialDocumentDraft(Integer peopleMainId,Integer typeId);
	/**
	 * ищем черновик подписанного документа по кредиту
	 * 
	 * @param peopleMainId - id человека
	 * @param typeId - вид документа
	 * @param creditRequestId - id заявки
	 * @return
	 */
	public OfficialDocumentsEntity findOfficialDocumentCreditRequestDraft(Integer peopleMainId,
			Integer creditRequestId,Integer typeId);
	/**
	 * ищем черновик подписанного документа по кредиту
	 * 
	 * @param peopleMainId - id человека
	 * @param typeId - вид документа
	 * @param creditId - id кредита
	 * @return
	 */
	public OfficialDocumentsEntity findOfficialDocumentCreditDraft(Integer peopleMainId,Integer creditId,Integer typeId);
	/**
	 * удаляем подписанный документ
	 * @param id - id документа
	 */
	public void deleteOfficialDocument(int id);
	/**
	 * сохраняем подписанный документ
	 * @param document - документ
	 * @return
	 */
	public OfficialDocuments saveOfficialDocument(OfficialDocuments document);
	/**
	 * сохраняем подписанный документ
	 * @param document - документ
	 * @return
	 */
	public OfficialDocumentsEntity saveOfficialDocument(OfficialDocumentsEntity document);
	/**
	 * ищем документ по id заявки, кредита, продления,...
	 * @param anotherId - id
	 * @param peopleMainId - id человека
	 * @param typeId - id вида документа
	 * @return
	 */
	public OfficialDocumentsEntity findOfficialDocumentByAnotherId(Integer anotherId,Integer peopleMainId,Integer typeId);
}
