package ru.simplgroupp.mail;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.UsersDAO;
import ru.simplgroupp.interfaces.MailBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.UsersBeanLocal;
import ru.simplgroupp.persistence.MessagesEntity;
import ru.simplgroupp.persistence.PaymentMessageEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.ReferenceEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.GenUtils;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Messages;
import ru.simplgroupp.transfer.RefHeader;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.SettingsKeys;
import ru.simplgroupp.util.XmlUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Logger;

@Stateless
@Local(MailBeanLocal.class)
public class MailBean implements MailBeanLocal {

	@PersistenceContext(unitName = "MicroPU")
	protected EntityManager emMicro;
	
	@Inject Logger logger;
	
    @EJB
    RulesBeanLocal rulesBean;
    
    @EJB
	PeopleDAO peopleDAO;
    
    @EJB
    UsersBeanLocal usersBean;
    
    @EJB
    UsersDAO userDAO;
    
    @EJB
    ReferenceBooksLocal refBooks;
    
    private static final int NUMBER_CHARS=6;
    private static final int PASSWORD_NUMBER_CHARS=10;
    
    @Override
    public void send(String subject, String text, String toEmail) {
    	
    	//параметры для рассылки писем
        Map<String,Object> map=setSessionParams();       
        try {
            Message message = new MimeMessage((Session) map.get("session"));
            //от кого
            message.setFrom(new InternetAddress((String) map.get("username")));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //Заголовок письма
            message.setSubject(subject);
            //Содержимое
            //message.setText(text);
            message.setContent(text, "text/html; charset=utf-8");
          	//Отправляем сообщение
            Transport.send(message);

        } catch (MessagingException e) {
            logger.severe("Не удалось послать email сообщение "+e);
        }
    }

    @Override
    public boolean sendSMS(String phoneNumber, String text) {
    	
    	    boolean b=false;
    	    
    	    //параметры для рассылки смс
    	    Map<String,Object> params=rulesBean.getSmsConstants();
    	    
    	    String login=params.get(SettingsKeys.SMS_LOGIN_ADDITIONAL).toString();
            String password=params.get(SettingsKeys.SMS_PASSWORD_ADDITIONAL).toString();
            String fromString=params.get(SettingsKeys.SMS_FROM).toString();
            String url=params.get(SettingsKeys.SMS_URL_ADDITIONAL).toString();
    	
    
        try {
	  
            String st=url + "?user="+login+"&password="+Convertor.toDigest(password)+"&to=" + phoneNumber+"&text="+URLEncoder.encode(text,XmlUtils.ENCODING_UTF)+"&from="+fromString;
            URL url1=new URL(st);
            HttpsURLConnection connection = (HttpsURLConnection) url1.openConnection();
            
            SSLSocketFactory fact = HTTPUtils.createEmptyTrustSSLContext().getSocketFactory();
            ((HttpsURLConnection) connection).setSSLSocketFactory(fact);
    		
    	    ((HttpsURLConnection) connection).setHostnameVerifier(HTTPUtils.allHostsValid()); 
          
            connection.setDoOutput(false); 
            connection.setDoInput(true); 
           
            int code = connection.getResponseCode () ; 
            //если все нормально отправилось
            if ( code == HttpURLConnection.HTTP_OK ) {
            	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String str;

                while( null != ((str = in.readLine()))) {
                	if (str.contains("accepted")){
                	    b=true;
                	}
                	logger.info("Ответ "+str);
                }                
                connection.disconnect();
            }
                
        } catch (Exception e) {
        	logger.severe("Не удалось послать sms сообщение "+e);
        }
        logger.info("выполнение "+b);
        return b;
    }

	@Override
	public void sendAttachment(String subject, String text, String toEmail,
			String fileName) {
		
		//параметры для рассылки писем
        Map<String,Object> map=setSessionParams();          
        try {
        	Message message = new MimeMessage((Session) map.get("session"));
            //от кого
            message.setFrom(new InternetAddress((String) map.get("username")));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //Заголовок письма
            message.setSubject(subject);
            // Тело письма 
            BodyPart messageBodyPart = new MimeBodyPart();
            //Содержимое
             messageBodyPart.setText(text);
             
            // Create a multipar message
             Multipart multipart = new MimeMultipart();

             // Set text message part
             multipart.addBodyPart(messageBodyPart);

             // Part two is attachment
             messageBodyPart = new MimeBodyPart();
             
             DataSource source = new FileDataSource(fileName);
             messageBodyPart.setDataHandler(new DataHandler(source));
             messageBodyPart.setFileName(fileName);
             multipart.addBodyPart(messageBodyPart);

             // Send the complete message parts
             message.setContent(multipart );
            //Отправляем сообщение
            Transport.send(message);

        } catch (MessagingException e) {
        	logger.severe("Не удалось послать email сообщение "+e);
        }
		
	}
	
	class MailAuthenticator extends Authenticator {
	     String user;
	     String pw;
	     public MailAuthenticator (String username, String password) {
	        super();
	        this.user = username;
	        this.pw = password;
	     }
	    public PasswordAuthentication getPasswordAuthentication() {
	       return new PasswordAuthentication(user, pw);
	    }
	}

    public boolean sendSMSV2(String phoneNumber, String text) {
        boolean b=false;
        //параметры для рассылки смс
        Map<String,Object> params=rulesBean.getSmsConstants();

        String login=params.get(SettingsKeys.SMS_LOGIN).toString();
        String password=params.get(SettingsKeys.SMS_PASSWORD).toString();
        String fromString=params.get(SettingsKeys.SMS_FROM).toString();
        String url=params.get(SettingsKeys.SMS_URL).toString();
        //смотрим, с кем интегрируемся
        if (url.toUpperCase().contains("SMSTRAFFIC")){
        	b=sendSMS2(phoneNumber, text,login,password,fromString,url);
        } else if (url.toUpperCase().contains("DIGITAL")) {
        	b=sendSMS3(phoneNumber, text,login,password,fromString,url);
        } else if (url.toUpperCase().contains("INFOTEH")) {
        	b=sendSMS4(phoneNumber, text,login,password,fromString,url);
        } 
        logger.info("выполнение "+b);
        return b;
    }

    /**
     * отправляем смс, интеграция с смс-траффик
     * 
     * @param phoneNumber - телефон
     * @param text - текст
     * @param login - логин
     * @param password - пароль
     * @param fromString - от кого
     * @param url - url
     * @return удалось послать смс или нет
     */
    private boolean sendSMS2(String phoneNumber, String text,String login,String password,
    		String fromString,String url){
    	boolean b=false; 
    	  try {
      	    //параметры для рассылки смс
      	        	 
              ArrayList<BasicNameValuePair> postParameters;
              postParameters = new ArrayList<BasicNameValuePair>();
         
              postParameters.add(new BasicNameValuePair("login", login));
              postParameters.add(new BasicNameValuePair("password", password));
              postParameters.add(new BasicNameValuePair("phones", phoneNumber));
              postParameters.add(new BasicNameValuePair("message", text));
            //  postParameters.add(new BasicNameValuePair("originator", fromString));
              postParameters.add(new BasicNameValuePair("rus", "1"));
              postParameters.add(new BasicNameValuePair("max_parts", "10"));

              String response = doHTTPPost(postParameters);
              logger.info("Sending SMS, version 2, response =  " + response); 
              if (response.contains("<result>OK</result>")){
                  b=true;
              }
             
          } catch (Exception e) {
              logger.severe("Не удалось послать sms сообщение, ошибка "+e);
          }
    	  return b;
    }
    
    /**
     * отправляем смс, интеграция с digital-direct
     * 
     * @param phoneNumber - телефон
     * @param text - текст
     * @param login - логин
     * @param password - пароль
     * @param fromString - от кого
     * @param url - url
     * @return удалось послать смс или нет
     */
    private boolean sendSMS3(String phoneNumber, String text,String login,String password,
    		String fromString,String url){
    	boolean b=false; 
    	try {

             String st=url + "?login="+login+"&pass="+password+"&from="+fromString+"&to=" + phoneNumber+"&text="+URLEncoder.encode(text,XmlUtils.ENCODING_UTF);
             URL url1=new URL(st);
             HttpsURLConnection connection = (HttpsURLConnection) url1.openConnection();

             SSLSocketFactory fact = HTTPUtils.createEmptyTrustSSLContext().getSocketFactory();
             ((HttpsURLConnection) connection).setSSLSocketFactory(fact);

             ((HttpsURLConnection) connection).setHostnameVerifier(HTTPUtils.allHostsValid());

             connection.setDoOutput(false);
             connection.setDoInput(true);

             int code = connection.getResponseCode () ;
             //если все нормально отправилось
             if ( code == HttpURLConnection.HTTP_OK ) {
                 BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                 String str;

                 while( null != ((str = in.readLine()))) {
                     logger.info("Ответ "+str);
                 }
                 connection.disconnect();
                 b = true;
             }

         } catch (Exception e) {
             logger.severe("Не удалось послать sms сообщение "+e);
         }
         return b;
    }
    
    @Override
    public boolean sendSMSV3(String phoneNumber, String text) {

        //параметры для рассылки смс
        Map<String,Object> params=rulesBean.getSmsConstants();

        String login=params.get(SettingsKeys.SMS_LOGIN).toString();
        String password=params.get(SettingsKeys.SMS_PASSWORD).toString();
        String fromString=params.get(SettingsKeys.SMS_FROM).toString();
        String url=params.get(SettingsKeys.SMS_URL).toString();
  
        return sendSMS3(phoneNumber, text,login,password,fromString,url); 

    }

    /**
     * отправляем смс, интеграция с казахским провайдером
     * 
     * @param phoneNumber - телефон
     * @param text - текст
     * @param login - логин
     * @param password - пароль
     * @param fromString - от кого
     * @param url - url
     * @return удалось послать смс или нет
     */
    private boolean sendSMS4(String phoneNumber, String text,String login,String password,
    		String fromString,String url){
    	boolean b=false; 
    	 try {

             String st=url + "?action=sendmessage&messagetype=SMS:TEXT&username="+login+"&password="+password+"&originator="+fromString+"&recipient=" + phoneNumber+"&messagedata="+URLEncoder.encode(text,XmlUtils.ENCODING_UTF);
             URL url1=new URL(st);
             HttpsURLConnection connection = (HttpsURLConnection) url1.openConnection();

             SSLSocketFactory fact = HTTPUtils.createEmptyTrustSSLContext().getSocketFactory();
             ((HttpsURLConnection) connection).setSSLSocketFactory(fact);

             ((HttpsURLConnection) connection).setHostnameVerifier(HTTPUtils.allHostsValid());

             connection.setDoOutput(false);
             connection.setDoInput(true);

             int code = connection.getResponseCode () ;
             //если все нормально отправилось
             if ( code == HttpURLConnection.HTTP_OK ) {
                 BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                 String str;

                 while( null != ((str = in.readLine()))) {
                     logger.info("Ответ "+str);
                 }
                 connection.disconnect();
                 b = true;
             }

         } catch (Exception e) {
             logger.severe("Не удалось послать sms сообщение "+e);
         }
     	 return b;
    }
    
    @Override
    public boolean sendSMSV4(String phoneNumber, String text) {

        //параметры для рассылки смс
        Map<String,Object> params=rulesBean.getSmsConstants();

        String login=params.get(SettingsKeys.SMS_LOGIN).toString();
        String password=params.get(SettingsKeys.SMS_PASSWORD).toString();
        String fromString=params.get(SettingsKeys.SMS_FROM).toString();
        String url=params.get(SettingsKeys.SMS_URL).toString();
      
        return sendSMS4(phoneNumber, text,login,password,fromString,url);
    }

    private String doHTTPPost(List<BasicNameValuePair> postParameters) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
       
       //параметры для рассылки смс
        Map<String,Object> params=rulesBean.getSmsConstants();
        HttpPost httppost = new HttpPost(params.get(SettingsKeys.SMS_URL).toString());
       
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(postParameters,XmlUtils.ENCODING_WINDOWS);
        urlEncodedFormEntity.setContentEncoding(XmlUtils.ENCODING_WINDOWS);
        httppost.setEntity(urlEncodedFormEntity);

        httppost.setHeader("Content-Type","application/x-www-form-urlencoded");
        httppost.setHeader("Content-Encoding", XmlUtils.ENCODING_WINDOWS);
          
        //Execute and get the response.
        HttpResponse response = httpclient.execute(httppost);

        HttpEntity entity = response.getEntity();

        String theResponse = null;
        if (entity != null) {
            InputStream instream = entity.getContent();
            try {
                theResponse = EntityUtils.toString(entity);
            }catch (Exception e){
                logger.severe("Cannot send SMS, version 2"+e.getMessage());
            }
            finally {
                instream.close();

            }
        }
        return theResponse;
    }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveMessage(Integer peopleId, Integer userId,
			Integer messageType, Integer messageWay, Date messageDate,
			String header, String body) {
        this.saveMessage(peopleId, userId, messageType, messageWay, null, messageDate, header, body);
	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveMessage(Integer peopleId, Integer userId, Integer messageType, Integer messageWay, Integer contactID, Date messageDate, String header, String body) {
        PeopleMainEntity peopleMain=peopleDAO.getPeopleMainEntity(peopleId);
        if (peopleMain==null){
            logger.severe("Не найден человек с id "+peopleId);
            return;
        }
        MessagesEntity message=new MessagesEntity();
        message.setPeopleMainId(peopleMain);
        if (userId!=null){
            message.setUserId(userDAO.getUsersEntity(userId));
        }
        if (messageType!=null){
            message.setMessageTypeId(refBooks.findByCodeIntegerEntity(RefHeader.MESSAGE_TYPE, messageType));
        }
        if (messageWay!=null){
            message.setMessageWayId(refBooks.findByCodeIntegerEntity(RefHeader.EXECUTION_WAY, messageWay));
        }
        if (contactID != null) {
            message.setPeopleContactId(peopleDAO.getPeopleContactEntity(contactID));
        }
        message.setMessageDate(messageDate);
        message.setMessageHeader(header);
        message.setMessageBody(body);
        emMicro.persist(message);
    }

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MessagesEntity getMessage(Integer messageId) {
		return emMicro.find(MessagesEntity.class, messageId);
	}

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Messages getMessages(Integer messageID) {
        return new Messages(getMessage(messageID));
    }

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeMessage(Integer messageId) {
		 Query qry = emMicro.createNamedQuery("removeMessage");
		 qry.setParameter("id", messageId);
	     qry.executeUpdate();	
		
	}

    @Override
    public List<Messages> getMessagesByPeopleAndUserID(Integer peopleID, Integer userID) {
        TypedQuery qry = emMicro.createNamedQuery("messagesByPeopleAndUserID", MessagesEntity.class);
        qry.setParameter("peopleMainID", peopleID);
        qry.setParameter("userID", userID);

        List<MessagesEntity> entities = qry.getResultList();
        List<Messages> result = new ArrayList<>();
        for (MessagesEntity entity : entities) {
            Messages mess = new Messages(entity);
            result.add(mess);
        }
        return result;
    }

    @Override
    public List<Messages> getMessagesByPeopleID(Integer peopleID, Integer typeCode) {
        TypedQuery<MessagesEntity> qry = emMicro.createNamedQuery("messagesByPeopleIDAndTypeCode", MessagesEntity.class);
        qry.setParameter("peopleMainID", peopleID);
        qry.setParameter("typeCode", typeCode);

        List<MessagesEntity> entities = qry.getResultList();
        List<Messages> result = new ArrayList<>();
        for (MessagesEntity entity : entities) {
            Messages mess = new Messages(entity);
            result.add(mess);
        }
        return result;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<Messages> getNewMessageList(Integer typeCode, Set options) {
        TypedQuery<MessagesEntity> qry = emMicro.createNamedQuery("newMessages", MessagesEntity.class);
        qry.setParameter("typeCode", typeCode);

        List<MessagesEntity> entities = qry.getResultList();
        List<Messages> result = new ArrayList<>();
        for (MessagesEntity entity : entities) {
            Messages mess = new Messages(entity);
            mess.init(options);
            result.add(mess);
        }
        return result;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void changeMessageStatus(Integer messageID, Integer status) {
        MessagesEntity message = getMessage(messageID);
        if (!message.getStatus().equals(status)) {
            message.setStatus(status);
            message = emMicro.merge(message);
            emMicro.persist(message);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<Messages> getMessagesByUserID(Integer userID, Integer typeCode, Set options) {
        TypedQuery<MessagesEntity> qry = emMicro.createNamedQuery("messageByUserID", MessagesEntity.class);
        qry.setParameter("userID", userID);
        qry.setParameter("typeCode", typeCode);
        qry.setParameter("inOut", Messages.OUTCOME);

        List<MessagesEntity> entities = qry.getResultList();
        List<Messages> result = new ArrayList<>();
        for (MessagesEntity entity : entities) {
            Messages mess = new Messages(entity);
            mess.init(options);
            result.add(mess);
        }
        return result;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<Messages> getMessagesByTypeCode(Integer typeCode, Set options) {
        TypedQuery<MessagesEntity> qry = emMicro.createNamedQuery("messageByTypeCode", MessagesEntity.class);
        qry.setParameter("typeCode", typeCode);
        qry.setParameter("inOut", Messages.OUTCOME);

        List<MessagesEntity> entities = qry.getResultList();
        List<Messages> result = new ArrayList<>();
        for (MessagesEntity entity : entities) {
            Messages mess = new Messages(entity);
            mess.init(options);
            result.add(mess);
        }
        return result;
    }

    @Override
    public void takeMessage(Integer messageID, Integer userID) {
        MessagesEntity message = getMessage(messageID);
        message.setUserId(userDAO.getUsersEntity(userID));
        message = emMicro.merge(message);
        emMicro.persist(message);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Messages sendMessage(Messages messages) {
        MessagesEntity entity = emMicro.merge(messages.getEntity());
        if (messages.getUser() != null) {
            entity.setUserId(userDAO.getUsersEntity(messages.getUser().getId()));
        }
        if (messages.getPeopleMain() != null) {
            entity.setPeopleMainId(peopleDAO.getPeopleMainEntity(messages.getPeopleMain().getId()));
        }
        if (messages.getMessageType() != null) {
            entity.setMessageTypeId(refBooks.getReferenceEntity(messages.getMessageType().getId()));
        }
        entity.setMessageDate(new Date());
        emMicro.persist(entity);
        return new Messages(entity);
    }

    @Override
    public List<Messages> getMessagesByPeopleID(Integer peopleID, Set options) {
        TypedQuery qry = emMicro.createNamedQuery("messagesByPeopleID", MessagesEntity.class);
        qry.setParameter("peopleMainID", peopleID);

        List<MessagesEntity> entities = qry.getResultList();
        List<Messages> result = new ArrayList<>();
        for (MessagesEntity entity : entities) {
            Messages mess = new Messages(entity);
            mess.init(options);
            result.add(mess);
        }
        return result;
    }

    @Override
    public List<Messages> getMessagesByPeopleIDAndWayID(Integer peopleID, Integer wayID, Set options) {
        TypedQuery<MessagesEntity> qry = emMicro.createNamedQuery("messagesByPeopleIDAndWayID", MessagesEntity.class);
        qry.setParameter("peopleMainID", peopleID);
        qry.setParameter("wayID", wayID);

        List<MessagesEntity> entities = qry.getResultList();
        List<Messages> result = new ArrayList<>();
        for (MessagesEntity entity : entities) {
            Messages mess = new Messages(entity);
            mess.init(options);
            result.add(mess);
        }
        return result;
    }

    /**
	 * устанавливаем параметры для рассылки писем
	 * @return
	 */
	private Map<String,Object> setSessionParams(){
		Map<String,Object> map=new HashMap<String,Object>(2);
		//параметры для рассылки писем
        Map<String,Object> params=rulesBean.getEmailConstants();
        
        final String username = params.get(SettingsKeys.EMAIL_LOGIN).toString();
        final String password = params.get(SettingsKeys.EMAIL_PASSWORD).toString();
        final String mailFrom = Convertor.toString(params.get(SettingsKeys.EMAIL_FROM));

        final Boolean useOurServer = Utils.booleanFromNumber(params.get(SettingsKeys.EMAIL_OUR_SERVER));
        final String smtpPort;
  
        Properties props = new Properties();
        if (useOurServer) {
            smtpPort = params.get(SettingsKeys.EMAIL_SMTP_PORT_NO_TLS).toString();
            props.put("mail.smtp.host", "127.0.0.1");
        } else {
            smtpPort = params.get(SettingsKeys.EMAIL_SMTP_PORT).toString();
            props.put("mail.smtp.host", "smtp." + params.get(SettingsKeys.EMAIL_PREFIX).toString());
        }
        props.put("mail.transport.protocol","smtp");
        props.put("mail.smtp.port", smtpPort);

        Session session;
        if (useOurServer) {
        	map.put("username", mailFrom);
            session = Session.getInstance(props);
        } else {
        	map.put("username", username);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust",  "smtp." + params.get(SettingsKeys.EMAIL_PREFIX).toString());
            session = Session.getInstance(props, new MailAuthenticator(username, password));
        }
        map.put("session", session);
   
        return map;
	}

	@Override
	public String generateCodeForSending() {
		//параметры для генерирования кода
        Map<String,Object> params=rulesBean.getGenerateConstants();
        String code = Convertor.toString(params.get(SettingsKeys.GENERATE_TEST_CODE));
        //если это не тест
        if(Convertor.toInteger(params.get(SettingsKeys.GENERATE_REAL_DIGIT_CODE))==1){
        	if (Convertor.toInteger(params.get(SettingsKeys.GENERATE_CODE_NUMBER_CHARS))!=null){
        		code=GenUtils.genCode(Convertor.toInteger(params.get(SettingsKeys.GENERATE_CODE_NUMBER_CHARS)));
        	} else {
        		code=GenUtils.genCode(NUMBER_CHARS);
        	}
        }
        return code;
	}

	@Override
	public String generatePasswordForSending() {
		//параметры для генерирования кода
        Map<String,Object> params=rulesBean.getGenerateConstants();
        int numchars=0;
        String code="";
        if (Convertor.toInteger(params.get(SettingsKeys.GENERATE_PASSWORD_NUMBER_CHARS))!=null){
    		numchars=Convertor.toInteger(params.get(SettingsKeys.GENERATE_PASSWORD_NUMBER_CHARS));
    	} else {
    		numchars=PASSWORD_NUMBER_CHARS;
    	}
        //если генерируем пароль только из цифр
        if(Convertor.toInteger(params.get(SettingsKeys.GENERATE_PASSWORD_ONLY_DIGITS))==1){
        	code=GenUtils.genCode(numchars);
        } else {
        	code=GenUtils.gen(numchars);
        }
        return code;
	}

    @Override
    public void savePaymentMessage(Integer paymentSystemCode, Date date) {
        PaymentMessageEntity entity = new PaymentMessageEntity();
        entity.setPaymentId(refBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, paymentSystemCode));
        entity.setDate(date);
        entity = emMicro.merge(entity);
        emMicro.persist(entity);
    }

    @Override
    public void deletePaymentMessageByPaymentCode(Integer paymentSystemCode) {
        ReferenceEntity en = refBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, paymentSystemCode);
        Query qry = emMicro.createNamedQuery("deletePaymentMessageByPaymentCode");
        qry.setParameter("id", en.getId());
        qry.executeUpdate();
    }

    @Override
    public PaymentMessageEntity findPaymentMessageByPaymentCode(Integer paymentSystemCode) {
        ReferenceEntity en = refBooks.findByCodeIntegerEntity(RefHeader.ACCOUNT_TYPE, paymentSystemCode);
        Query qry = emMicro.createNamedQuery("findPaymentMessageByPaymentCode");
        qry.setParameter("id", en.getId());
        return (PaymentMessageEntity) Utils.firstOrNull(qry.getResultList());
    }
}