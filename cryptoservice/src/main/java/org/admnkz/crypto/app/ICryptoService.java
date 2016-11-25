package org.admnkz.crypto.app;

import java.io.File;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.xml.soap.SOAPMessage;

import org.admnkz.crypto.CryptoException;
import org.admnkz.crypto.data.Certificate;
import org.admnkz.crypto.data.Settings;
import org.admnkz.crypto.data.Status;
import org.admnkz.crypto.data.SubjectType;
import org.w3c.dom.Element;

import ru.simplgroupp.toolkit.common.DateRange;

@Local
public interface ICryptoService 
{
	 String CAP_SECURE_RANDOM = "SecureRandom";
	 String CAP_PRIVATE_KEY_EXPORT = "PrivateKey.export";
	
	 String CAP_VALUE_REQUIRED = "Required";
	 String CAP_VALUE_SUPPORTS = "Supports";
	 String CAP_VALUE_NEVER = "Never";
	 
	 Certificate addCertificate(Certificate asigner, String providerName, byte[] cbody, byte[] cpriv, byte[] csecrzip, String privPassword) throws Exception;
	 Certificate addCertificate(Certificate asigner, String sprovname, byte[] cbody, String cprivPath, String privPassword) throws Exception;
	 /**
	 * ищем сертификат
	 * @param aid - сертификат
	 */
	 Certificate getCertificate(String aid);
	/**
	 * ищем сертификат
	 * @param aid - сертификат
	 * @param options - что инициализируем
	 */
	 Certificate getCertificate(String aid, Set options);
	 String getServiceRootPath();
	 String getDefaultProviderName();
	 /**
	 * ищем настройки сертификата
	 * @param aid - сертификат
	 */
	 Settings getSettings(String setID);
	 ParseCertResult parseCertificate(byte[] cbody) throws CryptoException;
	
	 byte[] sign(final byte[] data, String optkey) throws CryptoException;
	 byte[] sign(final byte[] data, Settings options) throws CryptoException;
	 String signSOAP1(SOAPMessage axisMessage, String settingsID) throws CryptoException;
	 String signSOAP1(SOAPMessage axisMessage, Settings sets) throws CryptoException;
	 SOAPMessage signSOAP(SOAPMessage unsignedSOAP, String settingsID) throws CryptoException;
	 SOAPMessage signSOAP(SOAPMessage axisMessage, Settings options) throws CryptoException;
	 String signSOAP(String unsignedSOAP, Settings options) throws CryptoException;
	 String signSOAP(String unsignedSOAP, String settingsID) throws CryptoException;
	 boolean verify(final byte[] data, byte[] signature, Settings options,boolean bSignatureOnly) throws  CryptoException;
	 boolean verify(final byte[] data, byte[] signature, String optkey, boolean bSignatureOnly) throws  CryptoException;
	 boolean verifySOAP(SOAPMessage axisMessage, String setID, boolean bSignatureOnly) throws CryptoException;
	 boolean verifySOAP(String signedSOAP, Settings options, boolean bSignatureOnly) throws CryptoException;
	 boolean verifySOAP(String signedSOAP, String setID, boolean bSignatureOnly) throws CryptoException;
	
	/**
	 * Список имён провайдеров ЭЦП
	 * @return
	 */
	 String[] listDSigProviders();
	
	/**
	 * Возвращает список имён провайдеров, которые поддерживают заданную возможность.
	 * Например: listProvidersFor("Signature.", false); возвращает все провайдеры, умеющие формировать подпись.
	 * @param prefix - возможность 
	 * @param bExact - сравнивать полностью или должно совпадать только начало.  
	 * @return массив имён провайдеров
	 */
	 String[] listProvidersFor(String prefix, boolean bExact);
	
	/**
	 * Перечень всех поддерживаемых алгоритмов подписи.
	 * @return
	 */
	 Set<String> listDSigAlgorithms();
	
	 List<Status> listStatuses();
	 List<SubjectType> listSubjectTypes();
	 void updateStatuses(Date timeNow);
	/**
	 * сохраняем сертификат
	 * @param acert - сертификат
     */
	 Certificate saveCertificate(Certificate acert);
	/**
	 * сохраняем настройки сертификата
	 * @param sets - настройки
	 */
	 Settings saveSettings(Settings sets);
	/**
	 * списки сертификатов
	 * @param nFirstRow - первая запись
	 * @param nRows - сколько записей
	 * @param subjectCN - CN
	 * @param subjectTypeID - тип субъекта
	 * @param statusID - статус
	 * @param providerName - провайдер
	 * @param settingsName - название наше
	 * @param dateStart - дата начала 
	 * @param dateFinish - дата окончания
	 * @param signerID - кто подписывает
	 * @param orderby
	 * @param options - что инициализируем
	 * @return
	 */
	 List<Certificate> listCertificates(int nFirstRow, int nRows, String subjectCN, Integer subjectTypeID, Integer statusID, String providerName, String settingsName, DateRange dateStart, DateRange dateFinish,String signerID, String[] orderby, Set options);
	/**
	 * 
	 * @param subjectCN - CN
	 * @param subjectTypeID - тип субъекта
	 * @param statusID - статус
	 * @param providerName - провайдер
	 * @param settingsName - название наше
	 * @param dateStart - дата начала 
	 * @param dateFinish - дата окончания
	 * @param signerID - кто подписывает
	 * @param options - что инициализируем
	 * @return
	 */
	 int countCertificates(String subjectCN, Integer subjectTypeID, Integer statusID, String providerName, String settingsName, DateRange dateStart, DateRange dateFinish,String signerID);
	 Map<String, String> getProviderCapabities(String providerName);
	/**
	 * удаляем сертификат
	 * @param certID - сертификат
	 * @param bRemoveChilden - удаляем наследуемых от него
	 */
	 void removeCertificate(String certID, boolean bRemoveChilden);
	/**
	 * добавляем настройки для сертификата
	 * @param cert - сертификат
	 * @param sid - название настроек
	 */
	 Settings addSettings(Certificate cert, String sid);
	/**
	 * добавляем настройки для сертификата
	 * @param cert - сертификат
	 */
	 Settings addSettings(Certificate cert);
	/**
	 * удаляем настройки для сертификата
	 * @param cert - сертификат
	 * @param setsID - настройки
	 */
	 void removeSettings(Certificate cert, String setsID);
	/**
	 * удаляем настройки для сертификата
	 * @param setsID - настройки
	 */
	 void removeSettings(String setsID);
	 void updateSelfCheck(Date timeNow);
	
	 byte[] digest(byte[] source, String optkey) throws CryptoException;
	 byte[] digest(byte[] source, Settings sets) throws CryptoException;
	 byte[] signCMS(byte[] source, String optkey) throws CryptoException;
	 byte[] signCMS(byte[] source, Settings sets) throws CryptoException;
    byte[] signCMS(byte[] source, String optkey, boolean detached) throws CryptoException;
    byte[] signCMS(byte[] source, Settings sets, boolean detached) throws CryptoException;
    byte[] signCMS(byte[] source, Settings sets, boolean detached,boolean addChainCerts) throws CryptoException;
    byte[] signCMS(byte[] source, String optkey, boolean detached,boolean addChainCerts) throws CryptoException;
	 boolean verifyCMS(byte[] sig, byte[] data, String optkey, boolean bSignatureOnly) throws CryptoException;
	 boolean verifyCMS(byte[] sig, byte[] data, Settings sets, boolean bSignatureOnly) throws CryptoException;
     byte[] signYandexCMS(byte[] source, String optkey) throws CryptoException;
	 ParseCMSResult parseCMS(byte[] sig) throws CryptoException;
	 MimeMultipart signMime(MimeBodyPart source, Settings sets) throws CryptoException;
	 MimeMultipart signMime(MimeBodyPart source, String optkey) throws CryptoException;
     boolean verifyMime(MimeMultipart source, String optkey, boolean bSignatureOnly) throws CryptoException;
     boolean verifyMime(MimeMultipart source, Settings sets, boolean bSignatureOnly) throws CryptoException;
     Element signXML(Element esource, String setid) throws CryptoException;
     Element signXML(Element esource, Settings sets) throws CryptoException;
     Element signXML(Element esource, Settings sets, String defRefId) throws CryptoException;
     Element signXML(Element esource, String setid, IExternalSigning isign) throws CryptoException;
     Element signXML(Element esource, String setid, String defRefId) throws CryptoException;
     Element signXML(Element esource, Settings sets, IExternalSigning isign) throws CryptoException;	
     List<Settings> listSettings(String templateID);
     boolean checkIsPair(Certificate cc) throws CryptoException;   
    
     boolean verifyCertificate(String certID) throws CryptoException;
     boolean verifyCertificate(Certificate cert) throws CryptoException;
     boolean verifyCertificate(byte[] xcert) throws CryptoException;
    
    /**
     * Возвращает цепочку сертификатов, включая заданный, в формате PKCS#7
     * @param certID - ID сертификата
     * @return
     * @throws CryptoException
     */
     byte[] exportChain(String certID) throws CryptoException;
    
    /**
     * Возвращает цепочку сертификатов в формате PKCS#7
     * @param certs - список сертификатов
     * @return
     * @throws CryptoException
     */
     byte[] exportChain(List<byte[]> certs) throws CryptoException;
    
     byte[] convertPrivateKey(Settings setsFrom, Settings setsTo) throws CryptoException;
    
     class ParseCMSResult 
    {
    	 ArrayList<byte[]> certs = new ArrayList<byte[]>(1);
    	 byte[] signature;
    	 byte[] data;
    	 String digestAlg;
    }
    
     class ParseCertResult {
    	 X509Certificate certificate;
    	 String sha1hash;
    	 String providerName;
    }

     boolean verifyXML(Element source, Settings sets, boolean bSignatureOnly) throws CryptoException;
     boolean verifyXML(Element source, String setID, boolean bSignatureOnly) throws CryptoException;
     SOAPMessage signXMLAsDoc(SOAPMessage source, String ns, String localName, String setid) throws CryptoException;
     SOAPMessage signXMLAsDoc(SOAPMessage source, String ns, String localName, Settings sets) throws CryptoException;
	
    
	/**
	 * создаем ssl context
	 * @param clientSets - наши параметры
	 * @param serverSets - параметры сервера
	  */
	SSLContext createSSLContext(Settings clientSets, Settings serverSets) throws CryptoException;
	/**
	 * создаем ssl context
	 * @param clentSettingsID - наши параметры
	 * @param serverSettingsID - параметры сервера
	  */
    SSLContext createSSLContext(String clentSettingsID, String serverSettingsID) throws CryptoException;
    /**
	 * создаем X509 ssl context
	 * @param clentSettingsID - наши параметры
	 * @param serverSettingsID - параметры сервера
	  */
	SSLContext createX509SSLContext(String clentSettingsID, String serverSettingsID) throws CryptoException;

    /**
     * создаем ssl context, если мы доверяем всем
     * @param clentSettingsID - клиент
     * @param serverSettingsID - сервер
     */
     SSLContext createTrustedSSLContext(String clentSettingsID, String serverSettingsID) throws CryptoException;
    
    /**
     * создаем keyStore
     * @param storeType - тип хранилища
     * @param sets - параметры сертификата
     * @param subjectTypes 
     * @param password - пароль
     * @return
     * @throws CryptoException
     */
     byte[] generateKeyStore(String storeType, Settings sets, int[] subjectTypes, String password) throws CryptoException;
	 byte[] generateKeyStore(String storeType, String settingsID, int[] subjectTypes, String password) throws CryptoException;
	/**
	 * добавляем сертификат
	 * @param asigner - сертификат-подписчик 
	 * @param providerName - крипто-провайдер
	 * @param cbody - сам сертификат, который добавляем
	 * @return
	 * @throws Exception
	 */
	 Certificate addCertificate(Certificate asigner, String providerName, byte[] cbody) throws Exception;
	/**
	 * устанавливаем properties для работы с Крипто-про 
	 */
	 void setSSLProperties();
	
    /**
     * устанавливаем system properties в случае, когда sslcontext не работает
     * @param clentSettingsID - id сертификата
     * @param serverSettingsID - id корневого сертификата
     */
	 void setSystemCryptoProperties(String clentSettingsID, String serverSettingsID,String trustStoreName) throws SecurityException;
    /**
     * подписание на крипто-про
     * 
     * @param data - что подписываем
     * @param settingId - чем подписываем
     * @param detached - отсоединена подпись или нет
     */
	byte[] createJCPCMS(byte[] data, String settingId, boolean detached) throws Exception;
	 /**
     * подписание на крипто-про
     * 
     * @param data - что подписываем
     * @param settingId - чем подписываем
     * @param detached - отсоединена подпись или нет
     * @param filename - файл, если пишем в него данные  
     */
	byte[] createJCPCMS(byte[] data, String settingId, boolean detached,String filename) throws Exception;
    /**
     * проверяем подпись
     * @param data - данные с подписью
     * @param settingId - подпись, которую проверяем
      * @throws Exception
     */
	boolean verifyJCPCMS(byte[] data, String settingId) throws Exception;
    /**
     * проверка подписи 
     * @param data - что проверяем
     * @param settingId - какой сертификат проверяем
     */
    boolean verifyJCPCms(byte[] data, String settingId) throws CryptoException;

     byte[] decryptCMS(byte[] sig, String setId) throws CryptoException;
     /**
     * шифруем в формате der для крипто-про
     * 
     * @param data - подписанные данные, которые над шифровать
     * @param serverId - сертификат получателя
     * @param filename - файл, если нам надо в него записывать, если не надо, то передаем null
     * @return
     * @throws Exception
     */
     byte[] encryptPKCS7(byte[] data,String serverId,String filename) throws Exception;
    /**
     * запускаем утилиту cryptcp
     * 
     * @param sets - настройки
     * @param certName - название сертификата
     * @param inputFileName - файл, который шифруем
     * @param outputFileName - файл на выходе
     * @param isDer - в кодировке der (true) или base64 (false)
     * @return код выполнения
     * @throws CryptoException
     */
     int runCryptcp(String sets,String certName,String inputFileName,String outputFileName,boolean isDer)  throws CryptoException;
    /**
     * возвращаем строку в формате PKCS7 Encrypted
     * @param source - зашифрованный массив байт
     * @return
     */
     String makePKCS7Encrypted(byte[] source,boolean inStr) throws CryptoException;
    /**
     * возвращаем строку в формате PKCS7 Signed
     * @param source - зашифрованный массив байт
     * @return
     */
     String makePKCS7Signed(byte[] source,boolean inStr) throws CryptoException;

    /**
     * Создаем ЭЦП для запроса в Контакт
     * @param source подписываемый текст в байтах
     * @param passwdPk пароль
     * @param alias алиас в keystore
     * @param keyStoreName имя keystore
     * @param testKeysDir папка ключей, используется только для тестирования, в работе просто null
     * @return сама ЭЦП
     * @throws CryptoException
     */
    byte[] signContactRequest(byte[] source, String passwdPk, String alias, String keyStoreName,String testKeysDir) throws CryptoException;

    /**
     * возвращает строку сообщения из подписанного xml 
     * 
     * @param source - данные с подписью
     * @param encoding - кодировка
     * @return
     * @throws CryptoException
     */
    String extractXmlFromPkcs7(byte[] source,String encoding) throws CryptoException;
    
    /**
	 * Расшифрование PKCS7 Enveloped.
	 * 
	 * @param clientId -  наш сертификат.
	 * @param pkcs7Env - зашифрованное PKCS7 Enveloped.
	 * @param inData - исходные данные. Нужны при проверке detached подписи.
	 * @param detached - флаг detached подписи.
	 * @param outDataFile - файл с расшифрованными данными (если подпись attached).
	 * @return результат проверки.
	 * @throws Exception
	 */
	  boolean decryptPKCS7(String clientId, byte[] pkcs7Env, byte[] inData, 
			boolean detached, String outDataFile) throws CryptoException;
	
	/**
	 * создаем пустой ssl context для ГОСТ
	 * @return
	 */
	 SSLContext createTrustedEmptyGostSSLContext();
	/**
	 * возвращает trust manager для доверия всем сертификатам
	 * @return
	 */
	 TrustManager[] trustAll();
	 /**
	  * расшифровывает сообщение
	  * 
	  * @param clientId - конфигурация получателя
	  * @param encodedMessage - файл с сообщением
	  * @return
	  * @throws CryptoException
	  */
	 public byte[] decryptMessage(String clientId,File encodedMessage) throws CryptoException;
	 /**
	  * 
	  * @param clientId - конфигурация получателя
	  * @param encodedMessage - сообщение
	  * @return
	  * @throws CryptoException
	  */
	 public byte[] decryptMessage(String clientId,byte[] encodedMessage) throws CryptoException;
	 /**
	     * Зашифрование данных в формате Enveloped CMS.
	     * Подходит только для случая, когда можно согласовать 
	     * сертификат отправителя и сертификат получателя 
	     *
	     * @param clientId Конфигурация отправителя.
	     * @param serverId Конфигурация получателя.
	     * @param data Данные для зашифрования.
	     * @param filename в какой файл пишем
	     * @return Enveloped CMS.
	     * @throws Exception
	     */
	  public  byte[] encryptCMS(String clientId,
	        String serverId, byte[] data,String filename) throws CryptoException;
	  
	  /**
	   * отсоединенная подпись для случая, когда исходный файл большой
	   * 
	   * @param clientId - конфигурация отправителя
	   * @param inputFile - файл
	   * @param filename - название файла, в который пишем подпись
	   * @return
	   * @throws CryptoException
	   */
	  public  byte[] signDetachedCMS(String clientId, File inputFile,String filename) throws CryptoException;
}
