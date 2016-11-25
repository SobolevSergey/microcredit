package ru.simplgroupp.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.simplgroupp.exception.KassaException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Locale;
import java.util.Map;
import java.io.File;
import org.apache.commons.io.FileUtils;


public class HTTPUtils {

	final static Logger log = LoggerFactory.getLogger(HTTPUtils.class.getName());
	
	/**
	   * Скачивает файл.
	   * @param sourceUrl URL файла для скачивания.
	   * @param destination Файл назначения.
	   * @throws KassaException 
	   */
	  public static void downloadFile(String sourceUrl, File destination) throws KassaException {
	    URL url;
	    try {
	      url = new URL(sourceUrl);
	    } catch (MalformedURLException ex) {
	      throw new KassaException("Некорректный URL файла для скачивания: " + sourceUrl, ex);
	    }
	    try {
	      FileUtils.copyURLToFile(url, destination);
	    } catch (IOException ex) {
	      throw new KassaException("Ошибка при скачивании файла " + sourceUrl, ex);
	    }
	  }
	
    /**
     * посылаем http
     *
     * @param method     - метод http
     * @param httpUrl    - Url, куда посылаем сообщение
     * @param rmessage   - сообщение
     * @param rparams    - параметры RequestProperty
     * @param sslContext - контекст для защищенного соединения, если обычный http, то null
     * @return - массив байтов ответа
     * @throws KassaException
     */
    public static byte[] sendHttp(String method, String httpUrl,byte[] rmessage,
       Map<String, String> rparams, SSLContext sslContext) throws IOException, KassaException {
        
    	URL url = new URL(httpUrl);

        HttpURLConnection connection;

        if (httpUrl.contains("https") && sslContext != null) {
            SSLSocketFactory fact = sslContext.getSocketFactory();
            log.info("Создали ssl фабрику");
            connection = (HttpsURLConnection) url.openConnection();
            log.info("Открыли соединение");
            ((HttpsURLConnection) connection).setSSLSocketFactory(fact);
            log.info("Подключили ssl фабрику");
           
            ((HttpsURLConnection) connection).setHostnameVerifier(allHostsValid());
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }

        connection.setRequestMethod(method);

        if (rparams != null) {
            for (Map.Entry<String, String> param : rparams.entrySet()) {
                if (StringUtils.isNotEmpty(param.getValue()))
                    connection.setRequestProperty(param.getKey(), param.getValue());
            }
        }

        connection.setDoInput(true);
        connection.setDoOutput(true);
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
        	log.info("Пытаемся послать сообщение");
            postHttpData(connection, rmessage);
            return getHttpData(connection);
        } else if ("GET".equalsIgnoreCase(method)) {
            return getHttpData(connection);
        }
        return null;
    }

    /**
     * посылаем get и получаем ответ
     *
     * @param conn - соединение
     * @throws KassaException
     */
    public static byte[] getHttpData(HttpURLConnection conn) throws KassaException {

        byte[] res = null;
        
        InputStream instrm;
        try {
        	log.info("Код ответа http " + conn.getResponseCode());
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                instrm = conn.getInputStream();
            } else {
                instrm = conn.getErrorStream();
            }
        } catch (IOException e) {
        	log.error("Не удалось прочитать ответ "+ e);
            throw new KassaException("Не удалось прочитать ответ", e);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            try {
                IOUtils.copy(instrm, bos);
                log.info("Прочитали данные ");
            } catch (IOException e) {
            	log.error("Не удалось записать ответ "+ e);
                throw new KassaException("Не удалось записать ответ", e);
            }
            res = bos.toByteArray();
        } finally {
            IOUtils.closeQuietly(bos);
        }


        return res;
    }

    /**
     * посылаем post
     *
     * @param conn     - соединение
     * @param rmessage - сообщение
     */
    public static void postHttpData(HttpURLConnection conn, byte[] rmessage) throws IOException {
        
    	log.info("Собираемся писать ");
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

        log.info("Записали ");
        try {
            wr.write(rmessage);
            log.info("Отправили ");
        } finally {
            IOUtils.closeQuietly(wr);
        }
    }

    /**
     * устанавливаем location для wsdl
     *
     * @param aURL - строка адреса
     * @return - url
     */
    public static URL setWSDLLocation(String aURL) throws KassaException {
        URL url;
        try {
            url = new URL(aURL);

            HttpURLConnection conn;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e1) {
            	log.error("Не получается создать соединение "+e1);
                throw new KassaException("Не получается создать соединение ", e1);
            }

            conn.setReadTimeout(5000);
            int status;
            try {
                status = conn.getResponseCode();
            } catch (IOException e) {
            	log.error("Не получается установить HTTP статус "+e);
                throw new KassaException("Не получается установить HTTP статус ", e);
            }

            //если перемещен
            boolean redirect = false;

            if (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER)
                redirect = true;
            if (redirect)
                url = new URL(conn.getHeaderField("Location"));

            //закрыли
            conn.disconnect();

            return url;
        } catch (MalformedURLException e) {
        	log.error("Не получается создать url из "+e);
            throw new KassaException("Не получается создать url из " + aURL);
        }

    }

    /**
     * создаем пустой ssl context, дабы обмануть крипто-про
     */
    public static SSLContext createEmptyTrustSSLContext() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };


        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            return sc;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * создаем верификатор имен
     */
    public static HostnameVerifier allHostsValid(){
    	HostnameVerifier allHostsValid = new HostnameVerifier() {
  			public boolean verify(String hostname, SSLSession session) {
  				return true;
  			}
  		};    
  		return allHostsValid;
    }
    
    /**
     * возвращает короткое название для браузера
     *
     * @param userAgent - заголовок http
     * @return короткое название для браузера
     */
    public static String getShortBrowserName(String userAgent) {
        if (StringUtils.isEmpty(userAgent)){
        	return "UNKNOWN";
        }
        String lcAgent = userAgent.toLowerCase(Locale.ENGLISH);
        if (lcAgent.contains("msie")) {
            return "MSIE";
        } else if (lcAgent.contains("opr")) {
            return "OPERA";
        } else if (lcAgent.contains("chrome") || lcAgent.contains("crios")) {
            return "CHROME";
        } else if (lcAgent.contains("webkit") || lcAgent.contains("safari")) {
            return "SAFARI";
        } else if (lcAgent.contains("firefox")) {
            return "FIREFOX";
        } else if (lcAgent.contains("trident")) {
            return "MSIE 11+";
        }
        return "UNKNOWN";
    }

    /**
     * возвращает ОС пользователя
     *
     * @param userAgent - заголовок http
     * @return ОС пользователя
     */
    public static String getUserOS(String userAgent) {

        if (StringUtils.isNotEmpty(userAgent)) {
        	if (userAgent.indexOf("(")>0&&userAgent.indexOf(")")>0){
                return userAgent.substring(userAgent.indexOf("(") + 1, userAgent.indexOf(")"));
        	} else {
        		return userAgent;
        	}
        }
        return "";
    }
    /**
     * возвращает закодированную строку для basic авторизации
     * 
     * @param login - логин
     * @param password - пароль
     * @return
     */
    public static String getBasicAuthorizationString(String login,String password){
    	 String stringAuth=login+":"+password;
		 byte[] byt=Base64.encodeBase64(stringAuth.getBytes());		
		 return new String(byt);
    }
    
    /**
     * проверяем коннект
     * @param httpUrl - урл для коннекта
     * @return
     * @throws Exception
     */
    public static boolean isConnected(String httpUrl) throws Exception{
    	URL url = new URL(httpUrl);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return true;
        } else {
            return false;
        }
    }
}
