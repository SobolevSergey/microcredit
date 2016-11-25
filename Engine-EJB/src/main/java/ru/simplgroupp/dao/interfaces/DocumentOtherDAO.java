package ru.simplgroupp.dao.interfaces;

import ru.simplgroupp.persistence.DocumentOtherEntity;
import ru.simplgroupp.transfer.DocumentsOther;

import javax.ejb.Local;
import java.util.List;
import java.util.Set;

/**
 * Created by cobalt on 16.09.15.
 */
@Local
public interface DocumentOtherDAO {

    /**
     * получить документ
     *
     * @param id - id документа
     * @return
     */
    public DocumentOtherEntity getDocument(Integer id);

    /**
     * получить документ
     *
     * @param id - id документа
     * @param options - что инициализируем
     * @return
     */
    public DocumentsOther getDocument(Integer id, Set options);


    /**
     * Ищет документы пользователя
     * @param id идентификатор пользователя
     * @param type идентификатор в таблицу ref_header
     * @return
     */
    public List<DocumentOtherEntity> findPeopleOtherDocuments(Integer id, Integer type);

    /**
     * Удаляет документ по ID
     * @param id
     */
    public void deleteDocument(Integer id);

    /**
     * Сохраняет документ
     * @param entity
     * @return
     */
    public DocumentOtherEntity saveDocument(DocumentOtherEntity entity);
}
