package ru.simplgroupp.fias.ejb;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.Timer;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import ru.simplgroupp.fias.persistence.Journal;

import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ru.simplgroupp.fias.utils.*;
import ru.simplgroupp.fias.exception.FiasException;
import ru.simplgroupp.fias.persistence.AddrObj;
import ru.simplgroupp.toolkit.common.Convertor;

/**
 * Загрузка и обновление БД ФИАС
 * @author CVB
 */
@Stateful
@TransactionManagement(value = TransactionManagementType.BEAN)
public class FiasUpdater implements FiasUpdaterLocal {
  
  /**
   * Максимальное время выполнения операции обновления (в секундах).
   */
  private static final int updateOperationTimeout = 60*60*6;
  /**
   * Максимальное время выполнения операции полной загрузки базы (в секундах).
   */
  private static final int replaceOperationTimeout = 60*60*24;
  
  private static final org.slf4j.Logger log = LoggerFactory.getLogger(FiasUpdater.class.getName());
  
  private static Journal lastUpdateOperation = null;
  
  @PersistenceContext(unitName="FiasPU")
  private EntityManager em;
  
  @Resource
  private UserTransaction userTransaction;
  
  @Resource(name="downloadServiceUrl")
  private String downloadServiceUrl = "http://fias.nalog.ru/WebServices/Public/DownloadService.asmx";
  
  @Resource(name="externalUnrarCommand")
  private String externalUnrarCommand = "unrar";
  
  @PostConstruct
  public void init() {
    try {
      userTransaction.setTransactionTimeout(1200);
    } catch (SystemException ex) {
      log.error("Не удалось установить таймаут транзакций.", ex);
    }
  }
  
  @Override
  public String getDownloadServiceUrl() {
    return downloadServiceUrl;
  }
  
  @Override
  public void setDownloadServiceUrl(String url) throws FiasException {
    if (StringUtils.isNotBlank(url))
      downloadServiceUrl = url;
    else
      throw new FiasException(FiasException.INCORRECT_PARAMETERS, "Некорректный URL.");
  }
  
  @Override
  public String getExternalUnrarCommand() {
    return externalUnrarCommand;
  }
  
  @Override
  public void setExternalUnrarCommand(String command) throws FiasException {
    if (StringUtils.isNotBlank(command))
      externalUnrarCommand = command;
    else
      throw new FiasException(FiasException.INCORRECT_PARAMETERS, "Не задана команда распаковки файла.");
  }
  
  @Override
  public Date getLastUpdateDate() {
    Journal operation = getLastUpdateOperation();
    return operation == null ? null : operation.getDateStart();
  }
  
  @Override
  public Integer getVersionId() {
    Journal operation = getLastUpdateOperation();
    return operation == null ? null : operation.getVersionId();
  }
  
  @Override
  public String getVersionText() {
    Journal operation = getLastUpdateOperation();
    return operation == null ? null : operation.getVersionText();
  }
  
  @Override
  public int getNumberOfUnprocessedUpdates() throws FiasException {
    List<DownloadFileInfo> updates = getUnprocessedUpdates();
    return updates.size();
  }
  
  @Override
  public void updateFias() throws FiasException {
    updateFias(null, true);
  }
  
  @Override
  public void updateFias(Integer updateLimit, boolean replaceIfNecessary) throws FiasException {
    log.info("Запущена операция обновления БД ФИАС.");
    List<DownloadFileInfo> updates;
    // Загружаем список обновлений
    try {
      lastUpdateOperation = null;
      updates = getUnprocessedUpdates();
      if (updates.isEmpty()) {
        log.info("Нет обновлений для БД ФИАС.");
        return;
      }
      log.info("Найдено обновлений: " + updates.size());
    } catch (FiasException e) {
      if (e.getCode() == FiasException.FULL_UPDATE_REQUIRED && replaceIfNecessary) {
        log.info("Выполняем полное обновление БД ФИАС.");
        replaceFias();
        return;
      }
      throw e;
    }
    // Протоколируем начало обработки обновлений
    Journal journal = new Journal();
    journal.setOperationType(Journal.OPER_UPDATE);
    journal.setDateStart(new Date());
    journal.setStatus(Journal.STATUS_IN_PROGRESS);
    journal.setMessageText("Найдено обновлений: " + updates.size());
    logOperation(journal);
    // Обрабатываем обновления
    int processedCount = 0;
    for (DownloadFileInfo updateUnfo : updates) {
      try {
        processUpdate(updateUnfo);
        processedCount++;
        String message = "Обработано обновлений: " + processedCount + " из " + updates.size();
        journal.setMessageText(message);
        journal.setVersionId(updateUnfo.versionId);
        journal.setVersionText(updateUnfo.versionText);
        logOperation(journal);
        log.info(message);
      } catch (FiasException fe) {
        journal.setMessageText(journal.getMessageText() + ". Ошибка: " + fe.getMessage());
        journal.setDateFinish(new Date());
        journal.setStatus(Journal.STATUS_FAILURE);
        try {
          logOperation(journal);
        } catch (FiasException ex) {
          log.error("Не удалось сохранить в журнал сообщение \""+journal.getMessageText()+"\"", ex);
        }
        throw fe;
      }
      if (updateLimit != null && updateLimit > 0 && processedCount >= updateLimit)
        break;
    }
    // Протоколируем конец обработки обновлений
    journal.setDateFinish(new Date());
    journal.setStatus(Journal.STATUS_SUCCESS);
    logOperation(journal);
    log.info("Операция обновления БД ФИАС завершена.");
  }
  
  @Override
  public void replaceFias() throws FiasException {
    log.info("Запущена операция полного обновления БД ФИАС.");
    // Протоколируем начало перезагрузки базы
    Journal journal = new Journal();
    journal.setOperationType(Journal.OPER_REPLACE);
    journal.setDateStart(new Date());
    journal.setStatus(Journal.STATUS_IN_PROGRESS);
    logOperation(journal);
    DownloadFileInfo dbInfo;
    File tempDir = null;
    try {
      // Получаем данные для загрузки актуальной базы
      log.info("Запрос сведений для загрузки актуальной БД...");
      dbInfo = getLastDownloadFileInfo();
      log.info("Сведения для загрузки актуальной БД получены.");
      // Скачиваем файл
      tempDir = FiasFileUtils.createTempDir();
      File dbFile = new File(tempDir.getAbsolutePath() + File.separator + "complete_db.rar");
      log.info("Загрузка файла " + dbInfo.completeXmlUrl + "...");
      FiasHttpUtils.downloadFile(dbInfo.completeXmlUrl, dbFile);
      log.info("Файл " + dbInfo.completeXmlUrl + " загружен в " + dbFile.getAbsolutePath());
      // Обрабатываем файл с базой даннных
      processFullDbFile(dbFile);
    } catch (FiasException fe) {
      journal.setMessageText(fe.getMessage());
      journal.setDateFinish(new Date());
      journal.setStatus(Journal.STATUS_FAILURE);
      try {
        logOperation(journal);
      } catch (FiasException e) {
        log.error("Не удалось сохранить в журнал сообщение \""+journal.getMessageText()+"\"", e);
      }
      throw fe;
    } finally {
      FiasFileUtils.deleteTempDir(tempDir, log);
    }
    // Протоколируем конец полного обновления базы
    journal.setDateFinish(new Date());
    journal.setVersionId(dbInfo.versionId);
    journal.setStatus(Journal.STATUS_SUCCESS);
    logOperation(journal);
    log.info("Операция полного обновления БД ФИАС завершена.");
  }
  
  @Override
  public void replaceFias(String filePath, int versionId, String versionText) throws FiasException {
    log.info("Запущена операция полного обновления БД ФИАС из заданного файла.");
    // Проверяем входные данные
    File dbFile = new File(filePath);
    if (!dbFile.exists())
      throw new FiasException(FiasException.INCORRECT_PARAMETERS, "Не найден файл " + dbFile.getName() + ".");
    if (versionId <= 0)
      throw new FiasException(FiasException.INCORRECT_PARAMETERS, "Некорректный номер версии БД ФИАС.");
    // Протоколируем начало перезагрузки базы
    Journal journal = new Journal();
    journal.setOperationType(Journal.OPER_REPLACE);
    journal.setDateStart(new Date());
    journal.setStatus(Journal.STATUS_IN_PROGRESS);
    logOperation(journal);
    // Обрабатываем файл с базой даннных
    try {
      processFullDbFile(dbFile);
    } catch (FiasException fe) {
      journal.setMessageText(fe.getMessage());
      journal.setDateFinish(new Date());
      journal.setStatus(Journal.STATUS_FAILURE);
      try {
        logOperation(journal);
      } catch (FiasException e) {
        log.error("Не удалось сохранить в журнал сообщение \""+journal.getMessageText()+"\"", e);
      }
      throw fe;
    }
    // Протоколируем конец полного обновления базы
    journal.setDateFinish(new Date());
    journal.setVersionId(versionId);
    if (StringUtils.isNotBlank(versionText))
      journal.setVersionText(versionText);
    journal.setStatus(Journal.STATUS_SUCCESS);
    logOperation(journal);
    log.info("Операция полного обновления БД ФИАС из заданного файла завершена.");
  }
  
  @Override
  public void handleTimer(Timer timer) {
    try {
      updateFias();
    } catch (FiasException e) {
      log.error("Ошибка при обновлении БД ФИАС по расписанию.", e);
    }
  }
  
  private void processUpdate(DownloadFileInfo updateInfo) throws FiasException {
    log.info("Обработка обновления #" + updateInfo.versionId + " (" + updateInfo.versionText + ")");
    File tempDir = null;
    try {
      // Загружаем файл обновлений
      tempDir = FiasFileUtils.createTempDir();
      File updateFile = new File(tempDir.getAbsolutePath() + File.separator + updateInfo.versionId + ".rar");
      log.info("Загрузка файла " + updateInfo.deltaXmlUrl + "...");
      FiasHttpUtils.downloadFile(updateInfo.deltaXmlUrl, updateFile);
      log.info("Файл " + updateInfo.deltaXmlUrl + " загружен в " + updateFile.getAbsolutePath());
      // Распаковываем архив
      log.info("Распаковываем архив...");
      FiasFileUtils.unrarFile(updateFile, tempDir, externalUnrarCommand);
      log.info("Файл " + updateInfo.deltaXmlUrl + " распакован в папку " + tempDir.getAbsolutePath());
      // Обновляем БД
      beginTransaction();
      updateSocrbase(tempDir);
      updateAddrObj(tempDir, false);
      commitTransaction();
    } catch (Exception e) {
      rollbackTransaction();
      throw e instanceof FiasException ? (FiasException) e
          : new FiasException(FiasException.COMMON_EXCEPTION,
              "Ошибка при обработке обновления №" + updateInfo.versionId + ": " + e.getMessage(), e);
    } finally {
      // Удаляем временную папку
      FiasFileUtils.deleteTempDir(tempDir, log);
    }
    log.info("Обновление #" + updateInfo.versionId + " (" + updateInfo.versionText + ") обработано.");
  }
  
  private void processFullDbFile(File dbFile) throws FiasException {
    File tempDir = null;
    try {
      // Распаковываем файл в новую временную папку
      tempDir = FiasFileUtils.createTempDir();
      log.info("Распаковка архива...");
      FiasFileUtils.unrarFile(dbFile, tempDir, externalUnrarCommand);
      log.info("Файл " + dbFile.getName() + " распакован в папку " + tempDir.getAbsolutePath());
      // Перегружаем БД
      beginTransaction();
      updateSocrbase(tempDir);
      updateAddrObj(tempDir, true);
      commitTransaction();
    } catch (Exception e) {
      rollbackTransaction();
      throw e instanceof FiasException ? (FiasException) e
          : new FiasException(FiasException.COMMON_EXCEPTION, "Ошибка при обработке файла с полной БД: " + e.getMessage(), e);
    } finally {
      // Удаляем папку с распакованным архивом
      FiasFileUtils.deleteTempDir(tempDir, log);
    }
  }
  
  /**
   * Обновляет таблицу справочников. Не создает собственной транзакции.
   * @param tempDir Папка, в которую распакован архив обновления
   * @throws FiasException 
   */
  private void updateSocrbase(File tempDir) throws FiasException {
    log.info("Обновление справочников...");
    // Очищаем таблицу
    em.createNativeQuery("delete from socrbase").executeUpdate();
    // Находим файл справочника
    File[] files = tempDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) { return name.startsWith("AS_SOCRBASE"); }
    });
    if (files.length != 1)
      throw new FiasException(FiasException.INCORRECT_FORMAT, "В архиве не найден файл справочной таблицы Socrbase.");
    File socrbaseFile = files[0];
    // Выполняем парсинг файла
    org.w3c.dom.Document doc = FiasXmlUtils.getDocFromFile(socrbaseFile);
    if (doc == null)
      throw new FiasException(FiasException.INCORRECT_FORMAT, "Не удалось декодировать файл справочной таблицы Socrbase.");
    // Обрабатываем строки справочника
    Query query = em.createNativeQuery("insert into socrbase (level1, scname, socrname, kod_t_st) values (:level1, :scname, :socrname, :kod_t_st)");
    org.w3c.dom.NodeList nl = doc.getElementsByTagName("AddressObjectType");
    if (nl == null || nl.getLength() == 0)
      throw new FiasException(FiasException.INCORRECT_FORMAT, "Не найдены элементы справочной таблицы Socrbase.");
    for (int i = 0; i < nl.getLength(); i++) {
      query.setParameter("level1", FiasXmlUtils.getIntegerAttr(nl.item(i), "LEVEL", true));
      query.setParameter("scname", FiasXmlUtils.getStringAttr(nl.item(i), "SCNAME", false));
      query.setParameter("socrname", FiasXmlUtils.getStringAttr(nl.item(i), "SOCRNAME", false));
      query.setParameter("kod_t_st", FiasXmlUtils.getIntegerAttr(nl.item(i), "KOD_T_ST", true));
      query.executeUpdate();
    }
    log.info("Справочники обновлены.");
  }
  
  private void updateAddrObj(File tempDir, boolean replace) throws FiasException {
    log.info("Обработка адресов...");
    int addedCount = 0, updatedCount = 0, deletedCount = 0;
    // Очищаем таблицу, если обновление полное
    if (replace)
      em.createNativeQuery("delete from addrobj").executeUpdate();
    // Находим и обрабатываем файл адресов
    File[] files = tempDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) { return name.startsWith("AS_ADDROBJ"); }
    });
    if (files.length > 0) {
      File addrFile = files[0];
      UpdateSaxHandler updateSaxHandler = new UpdateSaxHandler(replace);
      FiasXmlUtils.processXmlFile(addrFile, updateSaxHandler);
      addedCount = updateSaxHandler.getAddedCount();
      updatedCount = updateSaxHandler.getUpdatedCount();
      log.info("Файл адресообразующих элементов обработан.");
    } else if (replace)
      throw new FiasException(FiasException.INCORRECT_FORMAT, "В архиве не найден файл адресообразующих элементов.");
    
    // Обрабатываем технологически удаленные записи
    files = tempDir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) { return name.startsWith("AS_DEL_ADDROBJ"); }
    });
    if (files.length > 0) {
      log.info("Обнаружен файл технологически удаленных записей.");
      File addrFile = files[0];
      DeleteSaxHandler deleteSaxHandler = new DeleteSaxHandler();
      FiasXmlUtils.processXmlFile(addrFile, deleteSaxHandler);
      deletedCount = deleteSaxHandler.getDeletedCount();
      log.info("Файл технологически удаленных записей обработан.");
    }
    log.info("Таблица адресов обновлена. Добавлено записей: " + addedCount
        + ", обновлено: " + updatedCount + ", удалено: " + deletedCount);
  }
  
  private void logOperation(Journal journal) throws FiasException {
    if (journal.getId() == null) {
    	String sql = "from ru.simplgroupp.fias.persistence.Journal where status = :status"
          + " and (operationType = :typeUpdate and dateStart > :checkUpdateStartTime"
          + " or operationType = :typeReplace and dateStart > :checkReplaceStartTime) order by dateStart desc";
    	Query qry = em.createQuery(sql);
      qry.setParameter("status", Journal.STATUS_IN_PROGRESS);
      qry.setParameter("typeUpdate", Journal.OPER_UPDATE);
      qry.setParameter("typeReplace", Journal.OPER_REPLACE);
      long currentTime = (new Date()).getTime();
      qry.setParameter("checkUpdateStartTime", new Date(currentTime - updateOperationTimeout*1000));
      qry.setParameter("checkReplaceStartTime", new Date(currentTime - replaceOperationTimeout*1000));
    	List<Journal> operations = qry.getResultList();
      if (operations != null && !operations.isEmpty()) {
        Journal curOperation = operations.get(0);
        String sDate = (new SimpleDateFormat("dd.MM.yyyy HH:mm")).format(curOperation.getDateStart());
        throw new FiasException(FiasException.UPDATE_IN_PROGRESS, "Предыдущая операция обновления еще не завершена (запущена " + sDate + ").");
      }
    }
    try {
      beginTransaction(false);
      if (journal.getId() != null) journal = em.merge(journal);
      em.persist(journal);
      commitTransaction();
    } catch (FiasException e) {
      rollbackTransaction();
      throw e;
    }
    if (journal.getVersionId() != null) lastUpdateOperation = null;
  }
  
  private Journal getLastUpdateOperation() {
    if (lastUpdateOperation == null) {
      String sql = "from ru.simplgroupp.fias.persistence.Journal where versionId != null order by id desc";
      Query qry = em.createQuery(sql);
      qry.setMaxResults(1);
      List<Journal> operations = qry.getResultList();
      if (operations != null && !operations.isEmpty())
        lastUpdateOperation = operations.get(0);
    }
    return lastUpdateOperation;
  }
  
  private List<DownloadFileInfo> getUnprocessedUpdates() throws FiasException {
    Integer currentVersionId = getVersionId();
    if (currentVersionId == null)
      throw new FiasException(FiasException.FULL_UPDATE_REQUIRED, "Нет сведений о текущей версии БД ФИАС. Требуется полное обновление базы данных.");
    org.w3c.dom.Document responseDoc = getDownloadInfo("GetAllDownloadFileInfo");
    ArrayList<DownloadFileInfo> result = new ArrayList<>();
    org.w3c.dom.NodeList nl = responseDoc.getElementsByTagName("DownloadFileInfo");
    if (nl != null && nl.getLength() > 0) {
      int curVersionIndex = -1;
      for (int i = 0; i < nl.getLength(); i++) {
        DownloadFileInfo fileInfo = new DownloadFileInfo(nl.item(i), true);
        if (Objects.equals(fileInfo.versionId, currentVersionId)) {
          curVersionIndex = i;
          break;
        }
      }
      if (curVersionIndex < 0)
        throw new FiasException(FiasException.FULL_UPDATE_REQUIRED, "Текущяя версия БД ФИАС сильно устарела. Требуется полное обновление базы данных.");
      for (int i = curVersionIndex+1; i < nl.getLength(); i++)
        result.add(new DownloadFileInfo(nl.item(i), false));
    }
    return result;
  }
  
  private DownloadFileInfo getLastDownloadFileInfo() throws FiasException {
    org.w3c.dom.Document responseDoc = getDownloadInfo("GetLastDownloadFileInfo");
    org.w3c.dom.NodeList nl = responseDoc.getElementsByTagName("GetLastDownloadFileInfoResult");
    if (nl == null || nl.getLength() == 0)
      throw new FiasException(FiasException.INCORRECT_FORMAT, "Не найдены сведения для загрузки актуальной базы данных.");
    return new DownloadFileInfo(nl.item(0), false);
  }
  
  private org.w3c.dom.Document getDownloadInfo(String operationName) throws FiasException {
    Document requestDoc = DocumentHelper.createDocument();
    requestDoc.setXMLEncoding("utf-8");
    requestDoc.addElement("soap12:Envelope", "http://www.w3.org/2003/05/soap-envelope")
        .addElement("soap12:Body", "http://www.w3.org/2003/05/soap-envelope")
        .addElement(operationName, "http://fias.nalog.ru/WebServices/Public/DownloadService.asmx");
    HashMap headers = new HashMap();
    headers.put("Content-Type", "text/xml; charset=utf-8");
    byte[] response;
    try {
      response = FiasHttpUtils.sendHttp("POST", downloadServiceUrl, requestDoc.asXML().getBytes("UTF-8"), headers);
    } catch (UnsupportedEncodingException ex) {
      throw new FiasException(FiasException.HTTP_EXCEPTION, "Ошибка при кодировке запроса к сервису обновлений ФИАС.", ex);
    }
    if (response == null)
      throw new FiasException(FiasException.INCORRECT_RESPONSE, "Пустой ответ сервиса обновлений ФИАС.");
    String responseText;
    try {
      responseText = new String(response, "UTF-8");
    } catch (UnsupportedEncodingException ex) {
      throw new FiasException(FiasException.INCORRECT_RESPONSE, "Ошибка при перекодировке ответа сервиса обновлений ФИАС.", ex);
    }
    org.w3c.dom.Document responseDoc = FiasXmlUtils.getDocFromString(responseText);
    if (responseDoc == null)
      throw new FiasException(FiasException.INCORRECT_RESPONSE, "Не удалось декодировать ответ сервиса обновлений ФИАС.");
    return responseDoc;
  }
  
  private void beginTransaction() throws FiasException {
    beginTransaction(true);
  }
  
  private void beginTransaction(boolean commitPrevious) throws FiasException {
    try {
      if (userTransaction.getStatus() == Status.STATUS_ACTIVE)
        if (commitPrevious) commitTransaction();
        else rollbackTransaction();
      else if (userTransaction.getStatus() == Status.STATUS_COMMITTED)
        commitTransaction(true);
      else if (userTransaction.getStatus() == Status.STATUS_ROLLEDBACK)
        rollbackTransaction(true);
      if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION)
        userTransaction.begin();
      else
        throw new FiasException(FiasException.DB_EXCEPTION, "Не удалось создать новую транзакцию БД, так как предыдущая еще не завершена.");
    } catch (SystemException | SecurityException | IllegalStateException | NotSupportedException e) {
      throw new FiasException(FiasException.DB_EXCEPTION, "Ошибка при создании транзакции БД.", e);
    }
  }
  
  private void commitTransaction() throws FiasException {
    commitTransaction(false);
  }
	  
  private void commitTransaction(boolean rollbackHeuristic) throws FiasException {
    try {
      int status = userTransaction.getStatus();
      if (status == Status.STATUS_ACTIVE || rollbackHeuristic && status == Status.STATUS_COMMITTED) userTransaction.commit();
    } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException | SystemException e) {
      throw new FiasException(FiasException.DB_EXCEPTION, "Ошибка при завершении транзакции БД.", e);
    }
  }
	  
  private void rollbackTransaction() throws FiasException {
    rollbackTransaction(false);
  }
    
  private void rollbackTransaction(boolean rollbackHeuristic) throws FiasException {
    try {
      int status = userTransaction.getStatus();
      if (status == Status.STATUS_ACTIVE || rollbackHeuristic && status == Status.STATUS_ROLLEDBACK) userTransaction.rollback();
    } catch (SystemException | IllegalStateException | SecurityException e) {
      throw new FiasException(FiasException.DB_EXCEPTION, "Ошибка при откате транзакции БД.", e);
    }
  }
    
  private class DownloadFileInfo {
    Integer versionId = null;
    String versionText;
    String completeXmlUrl;
    String deltaXmlUrl;
    
    public DownloadFileInfo(org.w3c.dom.Node node, boolean onlyVersionId) throws FiasException {
      String strVersionId = FiasXmlUtils.getChildNodeValue(node, "VersionId", true);
      versionId = Convertor.toInteger(strVersionId);
      if (versionId == null)
        throw new FiasException(FiasException.INCORRECT_FORMAT, "Не удалось определить версию базы данных в информации для скачивания.");
      if (!onlyVersionId) {
        versionText = FiasXmlUtils.getChildNodeValue(node, "TextVersion", true);
        completeXmlUrl = FiasXmlUtils.getChildNodeValue(node, "FiasCompleteXmlUrl", true);
        deltaXmlUrl = FiasXmlUtils.getChildNodeValue(node, "FiasDeltaXmlUrl", true);
      }
    }
  }
  
  private class UpdateSaxHandler extends DefaultHandler {
    
    private final boolean replace;
    private int addedCount = 0;
    private int updatedCount = 0;
    
    public UpdateSaxHandler(boolean replace) { this.replace = replace; }
    
    public int getAddedCount() { return addedCount; }
    public int getUpdatedCount() { return updatedCount; }
    
    @Override
    public void startDocument() throws SAXException {
      addedCount = 0;
      updatedCount = 0;
    }
    
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes attrs)
        throws SAXException {
      if (qName.equals("Object")) {
        try {
          String aoid = FiasXmlUtils.getStringAttr(attrs, "AOID", true);
          AddrObj addr = null;
          if (!replace) addr = em.find(AddrObj.class, aoid);
          boolean isNew = addr == null;
          if (isNew) addr = new AddrObj();
          addr.setID(aoid);
          addr.setAOGUID(FiasXmlUtils.getStringAttr(attrs, "AOGUID", true));
          addr.setFormalName(FiasXmlUtils.getStringAttr(attrs, "FORMALNAME", true));
          addr.setRegionCode(FiasXmlUtils.getStringAttr(attrs, "REGIONCODE", true));
          addr.setAutoCode(FiasXmlUtils.getStringAttr(attrs, "AUTOCODE", true));
          addr.setAreaCode(FiasXmlUtils.getStringAttr(attrs, "AREACODE", true));
          addr.setCityCode(FiasXmlUtils.getStringAttr(attrs, "CITYCODE", true));
          addr.setCtarCode(FiasXmlUtils.getStringAttr(attrs, "CTARCODE", true));
          addr.setPlaceCode(FiasXmlUtils.getStringAttr(attrs, "PLACECODE", true));
          addr.setStreetCode(FiasXmlUtils.getStringAttr(attrs, "STREETCODE", true));
          addr.setExtrCode(FiasXmlUtils.getStringAttr(attrs, "EXTRCODE", true));
          addr.setSextCode(FiasXmlUtils.getStringAttr(attrs, "SEXTCODE", true));
          addr.setOfficialName(FiasXmlUtils.getStringAttr(attrs, "OFFNAME", false));
          addr.setPostalCode(FiasXmlUtils.getStringAttr(attrs, "POSTALCODE", false));
          addr.setOkato(FiasXmlUtils.getStringAttr(attrs, "OKATO", false));
          addr.setOktmo(FiasXmlUtils.getStringAttr(attrs, "OKTMO", false));
          addr.setUpdateDate(FiasXmlUtils.getDateAttr(attrs, "UPDATEDATE", true));
          addr.setShortName(FiasXmlUtils.getStringAttr(attrs, "SHORTNAME", true));
          addr.setAoLevel(FiasXmlUtils.getIntegerAttr(attrs, "AOLEVEL", true));
          addr.setParentAOGUID(FiasXmlUtils.getStringAttr(attrs, "PARENTGUID", false));
          addr.setPrevId(FiasXmlUtils.getStringAttr(attrs, "PREVID", false));
          addr.setNextId(FiasXmlUtils.getStringAttr(attrs, "NEXTID", false));
          addr.setKladrCode(FiasXmlUtils.getStringAttr(attrs, "CODE", false));
          addr.setPlainKladrCode(FiasXmlUtils.getStringAttr(attrs, "PLAINCODE", false));
          addr.setActualStatus(FiasXmlUtils.getIntegerAttr(attrs, "ACTSTATUS", true));
          addr.setCenterStatus(FiasXmlUtils.getIntegerAttr(attrs, "CENTSTATUS", true));
          addr.setOperStatus(FiasXmlUtils.getIntegerAttr(attrs, "OPERSTATUS", true));
          addr.setCurrStatus(FiasXmlUtils.getIntegerAttr(attrs, "CURRSTATUS", true));
          addr.setStartDate(FiasXmlUtils.getDateAttr(attrs, "STARTDATE", true));
          addr.setEndDate(FiasXmlUtils.getDateAttr(attrs, "ENDDATE", false));
          addr.setLiveStatus(FiasXmlUtils.getIntegerAttr(attrs, "LIVESTATUS", true));
          if (isNew) {
        	  if (replace){
                  em.persist(addr);
        	  } else {
                  addr = em.merge(addr);
        	  }
              addedCount++;
          } else {
              addr = em.merge(addr);
              updatedCount++;
          }
          if ((addedCount + updatedCount) % 500 == 0) {
            em.flush();
            commitTransaction();
            beginTransaction();
          }
          if ((addedCount + updatedCount) % 1000 == 0) {
            log.info("Обработано записей: " + (addedCount + updatedCount));
          }
        } catch (FiasException e) {
          throw new SAXException(e);
        }
      }
    }
    
    @Override
    public void endDocument() throws SAXException {
      em.flush();
      log.info("Обработано записей: " + (addedCount + updatedCount));
    }
    
  }
  
  private class DeleteSaxHandler extends DefaultHandler {
    
    private int deletedCount = 0;
    
    public int getDeletedCount() { return deletedCount; }
    
    @Override
    public void startDocument() throws SAXException {
      deletedCount = 0;
    }
    
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes attrs)
        throws SAXException {
      if (localName.equals("Object")) {
        try {
          String aoid = FiasXmlUtils.getStringAttr(attrs, "AOID", true);
          AddrObj addr = em.find(AddrObj.class, aoid);
          if (addr != null) {
            em.remove(addr);
            deletedCount++;
            if ((deletedCount) % 500 == 0) {
              em.flush();
              commitTransaction();
              beginTransaction();
            }
            if ((deletedCount) % 1000 == 0) {
              log.info("Удалено записей: " + deletedCount);
            }
          }
        } catch (FiasException e) {
          throw new SAXException(e);
        }
      }
    }
    
    @Override
    public void endDocument() throws SAXException {
      em.flush();
      log.info("Удалено записей: " + deletedCount);
    }
    
  }
  
}
