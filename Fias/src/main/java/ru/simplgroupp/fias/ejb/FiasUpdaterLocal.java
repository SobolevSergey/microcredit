package ru.simplgroupp.fias.ejb;

import java.util.Date;
import javax.ejb.Local;
import javax.ejb.Timer;
import ru.simplgroupp.fias.exception.FiasException;

/**
 * Загрузка и обновление БД ФИАС
 * @author CVB
 */
@Local
public interface FiasUpdaterLocal {

  /**
   * Возвращает URL Веб-сервиса ФИАС
   * @return 
   */
  public String getDownloadServiceUrl();
  
  /**
   * Устанавливает URL Веб-сервиса ФИАС
   * @param url URL Веб-сервиса ФИАС
   * @throws FiasException 
   */
  public void setDownloadServiceUrl(String url) throws FiasException;
  
  /**
   * Возвращает внешнюю команду для распаковки RAR-архива
   * @return 
   */
  public String getExternalUnrarCommand();
  
  /**
   * Устанавливает внешнюю команду для распаковки RAR-архива
   * @param command Команда для распаковки RAR-архива (без аргументов). Может включать полный путь к исполняемому файлу.
   * @throws FiasException 
   */
  public void setExternalUnrarCommand(String command) throws FiasException;

  /**
   * Возвращает дату последнего обновления БД ФИАС
   * @return 
   */
  public Date getLastUpdateDate();
  
  /**
   * Возвращает текущую версию локальной БД ФИАС
   * @return 
   */
  public Integer getVersionId();
  
  /**
   * Возвращает краткое описание текущей версии локальной БД ФИАС
   * @return 
   */
  public String getVersionText();
  
  /**
   * Возвращает количество необработанных обновлений для БД ФИАС.
   * Если требуется полная загрузка базы данных, поднимается исключение FiasException с кодом FiasException.FULL_UPDATE_REQUIRED
   * @return
   * @throws FiasException 
   */
  public int getNumberOfUnprocessedUpdates() throws FiasException;
  
  /**
   * Выполняет обновление БД ФИАС до актуальной версии. При необходимости выполняется полная перезагрузка базы данных.
   * @throws FiasException 
   */
  public void updateFias() throws FiasException;
  
  /**
   * Выполняет обновление БД ФИАС в соответствии с заданными параметрами
   * @param updateLimit Максимальное количество обрабатываемых обновлений.
   *   Если передано значение <code>null</code>, применяются все доступные обновления.
   * @param replaceIfNecessary Параметр определяет возможность полной перезагрузки БД в случае необходимости.
   *   Если передано <code>false</code> и необходимо полностью перегрузить БД,
   *   поднимается исключение FiasException с кодом FiasException.FULL_UPDATE_REQUIRED
   * @throws FiasException 
   */
  public void updateFias(Integer updateLimit, boolean replaceIfNecessary) throws FiasException;
  
  /**
   * Выполняет полную перезагрузку БД ФИАС
   * @throws FiasException 
   */
  public void replaceFias() throws FiasException;
  
  /**
   * Выполняет полную перезагрузку БД ФИАС из указанного файла (архива RAR)
   * @param filePath Полный путь к файлу на локальном компьютере
   * @param versionId Номер версии БД ФИАС
   * @param versionText Краткое описание версии. Например, "БД ФИАС от 21.05.2015".
   * @throws FiasException 
   */
  public void replaceFias(String filePath, int versionId, String versionText) throws FiasException;
  
  /**
   * Метод для обновления БД ФИАС по расписанию. Вызывает updateFias().
   * @param timer 
   */
  public void handleTimer(Timer timer);
  
}
