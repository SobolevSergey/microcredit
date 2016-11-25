package ru.simplgroupp.fias.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import ru.simplgroupp.fias.exception.FiasException;

/**
 * Утилиты для выполнения запросов HTTP.
 * @author CVB
 */
public class FiasHttpUtils {
  
  /**
   * Скачивает файл.
   * @param sourceUrl URL файла для скачивания.
   * @param destination Файл назначения.
   * @throws FiasException 
   */
  public static void downloadFile(String sourceUrl, File destination) throws FiasException {
    URL url;
    try {
      url = new URL(sourceUrl);
    } catch (MalformedURLException ex) {
      throw new FiasException(FiasException.DOWNLOAD_EXCEPTION, "Некорректный URL файла для скачивания: " + sourceUrl, ex);
    }
    try {
      FileUtils.copyURLToFile(url, destination);
    } catch (IOException ex) {
      throw new FiasException(FiasException.DOWNLOAD_EXCEPTION, "Ошибка при скачивании файла " + sourceUrl, ex);
    }
  }
  
  /**
   * Выполняет запрос HTTP.
   * @param method Метод HTTP (GET, POST)
   * @param httpUrl URL, по которому выполняется запрос
   * @param rmessage Сообщение
   * @param rparams Параметры запроса
   * @return Массив байтов ответа
   * @throws FiasException
   */
  public static byte[] sendHttp(String method, String httpUrl, byte[] rmessage, Map<String, String> rparams)
      throws FiasException {
    try {
      URL url = new URL(httpUrl);
      HttpURLConnection connection;
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod(method);
      if (rparams != null) {
        for (Map.Entry<String, String> param : rparams.entrySet()) {
          if (StringUtils.isNotEmpty(param.getValue())) {
            connection.setRequestProperty(param.getKey(), param.getValue());
          }
        }
      }
      connection.setDoInput(true);
      connection.setDoOutput(true);
      if ("POST".equalsIgnoreCase(method))
        postHttpData(connection, rmessage);
      return getHttpData(connection);
    } catch (IOException e) {
      throw new FiasException(FiasException.HTTP_EXCEPTION, "Ошибка при выполнении запроса HTTP.", e);
    }
  }

  private static void postHttpData(HttpURLConnection conn, byte[] rmessage) throws IOException {
    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
    try {
      wr.write(rmessage);
    } finally {
      IOUtils.closeQuietly(wr);
    }
  }

  private static byte[] getHttpData(HttpURLConnection conn) throws FiasException {
    byte[] res = null;
    InputStream instrm;
    try {
      if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
        instrm = conn.getInputStream();
      } else {
        instrm = conn.getErrorStream();
      }
    } catch (IOException e) {
      throw new FiasException(FiasException.HTTP_EXCEPTION, "Не удалось прочитать ответ", e);
    }
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      try {
        IOUtils.copy(instrm, bos);
      } catch (IOException e) {
        throw new FiasException(FiasException.HTTP_EXCEPTION, "Не удалось записать ответ", e);
      }
      res = bos.toByteArray();
    } finally {
      IOUtils.closeQuietly(bos);
    }
    return res;
  }
  
}
