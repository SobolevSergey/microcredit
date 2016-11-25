package ru.simplgroupp.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.AddressBeanLocal;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.interfaces.ScoringSociohubBeanLocal;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.interfaces.service.CreditInfoService;
import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.interfaces.service.RequestsService;
import ru.simplgroupp.persistence.AddressEntity;
import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PeopleContactEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.PeopleMiscEntity;
import ru.simplgroupp.persistence.PeoplePersonalEntity;
import ru.simplgroupp.persistence.RequestsEntity;
import ru.simplgroupp.persistence.ScoringEntity;
import ru.simplgroupp.persistence.UploadingEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.BaseAddress;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.EventCode;
import ru.simplgroupp.transfer.EventType;
import ru.simplgroupp.transfer.FiasAddress;
import ru.simplgroupp.transfer.Partner;
import ru.simplgroupp.transfer.PeopleContact;
import ru.simplgroupp.transfer.Reference;
import ru.simplgroupp.transfer.RequestStatus;
import ru.simplgroupp.transfer.ScoreModel;
import ru.simplgroupp.util.CalcUtils;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.HTTPUtils;
import ru.simplgroupp.util.JsonUtils;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.util.SettingsKeys;
import ru.simplgroupp.util.XmlUtils;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Реальный класс для работы с сервисом Sociohub
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ScoringSociohubBeanLocal.class)
public class ScoringSociohubBean extends AbstractPluginBean implements ScoringSociohubBeanLocal {

  @EJB
  KassaBeanLocal kassaBean;

  @EJB
  AddressBeanLocal addressBean;
  
  @EJB
  RequestsService requestsService;

  @EJB
  ReferenceBooksLocal refBooks;

  @EJB
  PeopleBeanLocal peopleBean;

  @EJB
  EventLogService eventLog;

  @EJB
  CreditInfoService creditInfo;
  
  @EJB
  PeopleDAO peopleDAO;
  
  @EJB
  CreditRequestDAO creditRequestDAO;
  
  @EJB
  RulesBeanLocal rulesBean;
  
  @Inject Logger log;
  
  private static final Map<String, Integer> socNetPartnerIds;
  private SociohubPluginConfig config = null;

  static {
    socNetPartnerIds = new HashMap<>();
    socNetPartnerIds.put("vk", Partner.VKONTAKTE);
    socNetPartnerIds.put("ok", Partner.ODNOKLASSNIKI);
    socNetPartnerIds.put("mm", Partner.MOIMIR);
    socNetPartnerIds.put("fb", Partner.FACEBOOK);
  }
  
  @Override
  public String getSystemName() {
    return ScoringSociohubBeanLocal.SYSTEM_NAME;
  }

  @Override
  public String getSystemDescription() {
    return ScoringSociohubBeanLocal.SYSTEM_DESCRIPTION;
  }

  @Override
  public boolean isFake() {
    return false;
  }

  @Override
  public EnumSet<PluginSystemLocal.Mode> getModesSupported() {
    return EnumSet.of(Mode.SINGLE);
  }

  @Override
  public EnumSet<PluginSystemLocal.ExecutionMode> getExecutionModesSupported() {
    return EnumSet.of(ExecutionMode.AUTOMATIC, ExecutionMode.MANUAL);
  }

  @Override
  public EnumSet<PluginSystemLocal.SyncMode> getSyncModesSupported() {
    return EnumSet.of(SyncMode.SYNC);
  }

  @Override
  public Integer getPartnerId() {
    return Partner.SOCIOHUB;
  }

  @Override
  public Set<String> getModelTargetsSupported() {
    return Utils.setOf(ModelKeys.TARGET_CREDIT_DECISION);
  }

  @Override
  public String getBusinessObjectClass() {
    return CreditRequest.class.getName();
  }

  @Override
  public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
    Integer creditRequestId = Convertor.toInteger(businessObjectId);
    log.info("Sociohub. Заявка " + creditRequestId + " поступила на обработку.");
    try {
      config = (SociohubPluginConfig) context.getPluginConfig();
      CreditRequest creditRequest;
      try {
        creditRequest = creditRequestDAO.getCreditRequest(creditRequestId, Utils.setOf());
        log.info("Sociohub. Заявка " + creditRequestId + " проинициализирована.");
      } catch (Exception e) {
        log.severe("Не удалось инициализировать кредитную заявку " + creditRequestId);
        throw new ActionException(ErrorKeys.CANT_INIT_OBJECT, "Не удалось инициализировать кредитную заявку", Type.TECH, ResultType.FATAL, e);
      }
      if (creditRequest == null) {
        log.severe("Не удалось инициализировать кредитную заявку " + creditRequestId);
        throw new ActionException(ErrorKeys.CANT_INIT_OBJECT, "Не удалось инициализировать кредитную заявку (значение null)", Type.TECH, ResultType.NON_FATAL, null);
      }
      if (context.getNumRepeats() <= config.getNumberRepeats()) {
        newRequest(creditRequest.getEntity(), getConfig().isUseWork(),getConfig().getCacheDays());
      } else {
        log.severe("Sociohub. Заявка " + creditRequestId + " не обработана.");
        throw new ActionException(ErrorKeys.CANT_MAKE_SKORING, "Не удалось выполнить скоринговый запрос в Sociohub за 3 попытки", Type.TECH, ResultType.FATAL, null);
      }
    } catch (ActionException e) {
      log.severe("Sociohub. Заявка " + creditRequestId + " не обработана. Ошибка " + e + ", " + e.getMessage());
      throw new ActionException("Произошла ошибка", e);
    } catch (Throwable e) {
      log.severe("Sociohub. Заявка " + creditRequestId + " не обработана. Ошибка: " + e.getMessage());
      throw new ActionException("Произошла ошибка", e);
    }
    log.info("Sociohub. Заявка " + creditRequestId + " обработана.");
  }

  /**
   * Добавить в пакет. Не используется в данном случае.
   */
  @Override
  public void addToPacket(String businessObjectClass,
      Object businessObjectId, PluginExecutionContext context) throws ActionException {

  }

  /**
   * Выполнить пакетный запрос. Не используется в данном случае.
   */
  @Override
  public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
    return null;
  }

  /**
   * Асинхронный запрос. Не поддерживается в данном случае.
   */
  @Override
  public boolean sendSingleRequest(String businessObjectClass,
      Object businessObjectId, PluginExecutionContext context) throws ActionException {
    log.warning("Метод sendSingleRequest не поддерживается");
    throw new UnsupportedOperationException();
  }

  /**
   * Ответ на асинхронный запрос. Не поддерживается в данном случае.
   */
  @Override
  public boolean querySingleResponse(String businessObjectClass,
      Object businessObjectId, PluginExecutionContext context) throws ActionException {
    log.warning("Метод querySingleResponse не поддерживается");
    throw new UnsupportedOperationException();
  }

  /**
   * Послать пакетный запрос. Не поддерживается в данном случае.
   */
  @Override
  public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
    log.warning("Метод sendPacketRequest не поддерживается");
    throw new UnsupportedOperationException();
  }

  /**
   * Получить ответ на пакетный запрос. Не поддерживается в данном случае.
   */
  @Override
  public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
    log.warning("Метод queryPacketResponse не поддерживается");
    throw new UnsupportedOperationException();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public RequestsEntity newRequest(CreditRequestEntity cre, Boolean isWork,
		  Integer cacheDays)  throws ActionException {

    // Проверяем, были ли уже успешные запросы по этой заявке
   	boolean inCache=requestsService.isRequestInCache(cre.getPeopleMainId().getId(), 
	    		Partner.SOCIOHUB, new Date(), cacheDays);
	if (inCache){
	   	log.info("По человеку "+cre.getPeopleMainId().getId()+" уже производился запрос в Социохаб, он кеширован");
	    //save log
		try {
			eventLog.saveLog(EventType.INFO,EventCode.OUTER_SCORING_SOCIOHUB,
					"Запрос к внешнему партнеру Социохаб не производился, данные кешированы",
					null,new Date(),creditRequestDAO.getCreditRequestEntity(cre.getId()),null,null);
		} catch (KassaException e) {
			log.severe("Не удалось сохранить лог "+e);
			throw new ActionException(ErrorKeys.CANT_WRITE_LOG,"Не удалось записать лог",Type.TECH, ResultType.NON_FATAL,e);
		} 
	   	return null;
	}
    // Формируем новую регистрационную запись запроса
    Partner parSociohub = refBooks.getPartner(Partner.SOCIOHUB);
    RequestsEntity req=requestsService.addRequest(cre.getPeopleMainId().getId(),cre.getId(), 
    		Partner.SOCIOHUB, RequestStatus.STATUS_IN_WORK, new Date());
    
    // Формируем тело запроса
    String reqBody = getRequestBody(cre, req.getRequestNumber(),false);
    log.info("Строка запроса: " + reqBody);

    // Сохраняем запрос
    req.setRequestbody(reqBody.getBytes());
    req = requestsService.saveRequestEx(req);

    // Отправляем запрос
    String url = isWork ? parSociohub.getUrlWork() : parSociohub.getUrlTest();
    String token = isWork ? parSociohub.getPasswordWork() : parSociohub.getPasswordTest();
    if (StringUtils.isEmpty(url) || StringUtils.isEmpty(token)) {
    	log.severe("Не определен URL или токен для запроса в Социохаб.");
        throw new ActionException(ErrorKeys.PLUGIN_WRONG_CONFIGURATION, "Не определен URL или токен для запроса.", Type.TECH, ResultType.FATAL, null);
    }
    log.info("Url: " + url);
    Map<String, String> params = new HashMap<>();
    params.put("Content-Type", "application/json");
    params.put("X-Token", token);
    byte[] response = null;
    try {
      response = HTTPUtils.sendHttp("POST", url, reqBody.getBytes(XmlUtils.ENCODING_UTF), params, HTTPUtils.createEmptyTrustSSLContext());
      if (response == null) {
    	  log.severe("Пустой ответ сервиса Sociohub.");
    	  requestsService.saveRequestError(req,null,ErrorKeys.NOTHING_TO_DECODE, "Пустой ответ сервиса Sociohub.", Type.TECH, ResultType.FATAL);
      }
    } catch (Exception e) {
    	requestsService.saveRequestError(req,e,ErrorKeys.CANT_COMPLETE_SEND_HTTP, "Не удалось отправить либо получить данные", Type.TECH, ResultType.NON_FATAL);
    }

    // Выполняем предварительную обработку ответа
    String answer = new String(response);
    req.setResponsebody(response);
    req.setResponsebodystring(answer);
    req = requestsService.saveRequestEx(req);

    // Выполняем содержательную обработку ответа
    try {
      req = saveAnswer(req, answer);
    } catch (Exception e) {
    	requestsService.saveRequestError(req,e,ErrorKeys.CANT_PARSE_RESPONSE, "Не удалось разобрать ответ", Type.TECH, ResultType.FATAL);
    }
    req = requestsService.saveRequestEx(req);

    // Сохраняем логи
    try {
      eventLog.saveLog(EventType.INFO,EventCode.OUTER_SCORING_SOCIOHUB, "Был выполнен запрос в Sociohub",
          null,new Date(),creditRequestDAO.getCreditRequestEntity(cre.getId()), null, null);
    } catch (KassaException e) {
      log.severe("Не удалось записать лог: " + e.getMessage());
      throw new ActionException(ErrorKeys.CANT_WRITE_LOG, "Не удалось записать лог", Type.TECH, ResultType.NON_FATAL, e);
    }
    return req;
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public RequestsEntity saveAnswer(RequestsEntity req, String answer)
      throws KassaException {

    req.setResponsedate(new Date());

    // Выполняем парсинг ответа
    JSONParser parser = new JSONParser();
    JSONObject jsonAnswer = null;
    try {
      jsonAnswer = (JSONObject) parser.parse(answer);
    } catch (Exception e) {
      throw new KassaException("Ответ сервиса Sociohub не имеет формат JSON.", e);
    }

    // Анализируем ответ
    if (jsonAnswer.containsKey("error")) {
      req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_ERROR).getEntity());
      req.setResponsemessage((String) jsonAnswer.get("error"));
      throw new KassaException("Сервис вернул ошибку: " + jsonAnswer.get("error"));
    } else if (jsonAnswer.containsKey("user")) {
      JSONObject user = (JSONObject) jsonAnswer.get("user");
      // Обрабатываем профили в соцсетях
      if (user.containsKey("profiles")) {
        JSONArray profiles = (JSONArray) user.get("profiles");
        for (Object element : profiles) {
          JSONObject profile = (JSONObject) element;
          if (!profile.containsKey("social_net")){
        	  log.severe("Неизвестный формат ответа: не найден ключ social_net.");
              throw new KassaException("Неизвестный формат ответа: не найден ключ social_net.");
          }
          String socialNet = (String) profile.get("social_net");
          if (socNetPartnerIds.containsKey(socialNet)) {
            log.info("Обрабатываем профиль в соцсети " + socialNet);
            Integer correctnessScore = Utils.defaultIntegerFromNull(Convertor.toInteger(profile.get("correctness_score")));
            if (correctnessScore >= getConfig().getMinCorrectnessScore()) {
              int partnerId = socNetPartnerIds.get(socialNet);
              // Обрабатываем персональные данные
              processPersonalData(req, profile, partnerId);
              // Обрабатываем дополнительные сведения
              processAdditionalData(req, profile, partnerId);
              // Обрабатываем адрес
              processAddress(req, profile, partnerId);
              // Обрабатываем контакты
              processContacts(req, profile, partnerId);
            }
          }
        }
      }
      // Обрабатываем кредитные рейтинги
      processScoring(req, user);
      // Пишем отчет
      req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
      req.setResponsemessage("Данные найдены и обработаны.");
      log.info("Данные найдены и обработаны.");
    } else if (jsonAnswer.containsKey("id")) {
      req.setRequeststatus(requestsService.getRequestStatus(RequestStatus.STATUS_IS_DONE_WITH_SESSION).getEntity());
      req.setResponsemessage("Данные не найдены.");
      log.info("Данные не найдены.");
    } else {
      throw new KassaException("Неизвестный формат ответа.");
    }

    return req;
  }

  /**
   * Метод не используется
   */
  @Override
  public void checkUploadStatus(UploadingEntity uploading,Boolean isWork) throws KassaException {

  }

  /**
   * Метод не используется
   */
  @Override
  public void uploadHistory(UploadingEntity uploading, Date sendingDate,
      Boolean isWork) throws KassaException {

  }

  /**
   * Метод не поддерживается
   */
  @Override
  public UploadingEntity createFileForUpload(Date sendingDate, Boolean isWork)
      throws KassaException {
    log.warning("Метод createFileForUpload не поддерживается");
    throw new UnsupportedOperationException();
  }

  private SociohubPluginConfig getConfig() {
    if (config == null)
      config = (SociohubPluginConfig) new PluginsSupport().getPluginConfig(SYSTEM_NAME);
    return config;
  }
  
  
  private String getRequestBody(CreditRequestEntity cre, Integer requestNumber,
		  Boolean allCandidates) throws ActionException {
    JSONObject requestJson = new JSONObject();
    requestJson.put("id", requestNumber);
    // Определяем персональные данные
    PeopleMainEntity pme = peopleDAO.getPeopleMainEntity(cre.getPeopleMainId().getId());
    if (pme == null) {
    	log.severe("Не найдены персональные данные по человеку с id "+cre.getPeopleMainId().getId());
        throw new ActionException(ErrorKeys.CANT_MAKE_SKORING, "Не найдены персональные данные.", Type.TECH, ResultType.FATAL, null);
    }
    PeoplePersonalEntity ppe = peopleBean.findPeoplePersonalActive(pme);
   
    requestJson.put("fname", ppe.getName());
    requestJson.put("lname", ppe.getSurname());
    Date birthday = ppe.getBirthdate();
    if (birthday == null) {
      log.severe("Не определена дата рождения по человеку с id "+cre.getPeopleMainId().getId());	
      throw new ActionException(ErrorKeys.CANT_MAKE_SKORING, "Не определена дата рождения.", Type.TECH, ResultType.FATAL, null);
    }
    requestJson.put("bday", DatesUtils.getDay(birthday));
    requestJson.put("bmonth", DatesUtils.getMonth(birthday));
    requestJson.put("byear", DatesUtils.getYear(birthday));
    ArrayList<AddressEntity> addresses = new ArrayList();
    AddressEntity addr = peopleBean.findAddressActive(pme, BaseAddress.REGISTER_ADDRESS);
    if (addr != null) {
      addresses.add(addr);
      if (addr.getIsSame() != null && addr.getIsSame() == 0) {
        addr = peopleBean.findAddressActive(pme, BaseAddress.RESIDENT_ADDRESS);
        if (addr != null) {
          addresses.add(addr);
        }
      }
    }
    JSONArray addrs = new JSONArray();
    for (AddressEntity item : addresses) {
      JSONObject a = new JSONObject();
      if (item.getCountry() != null && StringUtils.isNotEmpty(item.getCountry().getName())) {
        a.put("country", item.getCountry().getName());
      }
      if (StringUtils.isNotEmpty(item.getIndex())) {
        a.put("post_index", item.getIndex());
      }
      if (StringUtils.isNotEmpty(item.getRegionName())) {
        a.put("region", item.getRegionName());
      }
      if (StringUtils.isNotEmpty(item.getCityName())) {
        a.put("city", item.getCityName());
      }
      if (!a.isEmpty()) {
        addrs.add(a);
      }
    }
    if (!addrs.isEmpty()) {
      requestJson.put("places", addrs);
    }
    if (allCandidates){
      JSONObject opt1 = new JSONObject();
      opt1.put("all_candidates", "true");
      requestJson.put("options", opt1);
    }
    return requestJson.toString();
  }

  private void processPersonalData(RequestsEntity req, JSONObject profile, int partnerId) {
    // Определяем значимые для сравнения персональные данные
    String surname = profile.containsKey("l_name_c") ?
        (String) profile.get("l_name_c") : (String) profile.get("l_name");
    if (surname != null) {
    	surname = StringUtils.capitalize(surname);
    }
    String name = profile.containsKey("f_name_c") ?
        (String) profile.get("f_name_c") : (String) profile.get("f_name");
    if (name != null) {
    	name = StringUtils.capitalize(name);
    }
    Integer bDay = Convertor.toInteger(profile.get("dob_d"));
    Integer bMonth = Convertor.toInteger(profile.get("dob_m"));
    Integer bYear = Convertor.toInteger(profile.get("dob_y"));
    Date birthday = bDay != null && bMonth != null && bYear != null ?
        DatesUtils.makeDate(bYear, bMonth, bDay) : null;
    if (StringUtils.isBlank(surname) && StringUtils.isBlank(name) && birthday == null) return;
    
    // Сравниваем с данными из заявки
    CreditRequestEntity cre = creditRequestDAO.getCreditRequestEntity(req.getCreditRequestId().getId());
    PeopleMainEntity pme = cre.getPeopleMainId();
		PeoplePersonalEntity ppe = peopleBean.findPeoplePersonalActive(pme);
    if (StringUtils.isNotBlank(surname) && !surname.equalsIgnoreCase(ppe.getSurname())
        || StringUtils.isNotBlank(name) && !name.equalsIgnoreCase(ppe.getName())
        || birthday != null && !DatesUtils.isSameDay(birthday, ppe.getBirthdate())) {
      
      // Готовим данные для новой записи
     
      String maidenName = (String) profile.get("m_name");
      if (maidenName != null) maidenName = StringUtils.capitalize(maidenName);
      ArrayList<String> birthPlaceItems = new ArrayList<>();
      String value;
      if (StringUtils.isNotBlank(value = (String) profile.get("birth_country")))
        birthPlaceItems.add(StringUtils.capitalize(value));
      if (StringUtils.isNotBlank(value = (String) profile.get("birth_region")))
        birthPlaceItems.add(StringUtils.capitalize(value));
      if (StringUtils.isNotBlank(value = (String) profile.get("birth_city")))
        birthPlaceItems.add(StringUtils.capitalize(value));
      String birthPlace = birthPlaceItems.isEmpty() ? null : StringUtils.join(birthPlaceItems, ", ");
      
      // Проверяем, делалась ли подобная запись раньше
      boolean alreadyExists = false;
			List<PeoplePersonalEntity> ppel = peopleBean.findPeoplePersonal(pme, req.getCreditRequestId(), partnerId, ActiveStatus.ACTIVE);
      if (ppel != null)
        for (PeoplePersonalEntity item : ppel)
          if (StringUtils.equalsIgnoreCase(item.getSurname(), surname)
              && StringUtils.equalsIgnoreCase(item.getName(), name)
              && StringUtils.equalsIgnoreCase(item.getMaidenname(), maidenName)
              && DatesUtils.isSameDay(item.getBirthdate(), birthday)
              && StringUtils.equalsIgnoreCase(item.getBirthplace(), birthPlace)) {
            alreadyExists = true;
            break;
          }
      if (!alreadyExists) {
        // Добавляем новую запись
    	  try {
			peopleBean.newPeoplePersonal(null,pme.getId(), cre.getId(), partnerId, 
					  surname, name, null, maidenName, birthday, birthPlace, null, new Date(), ActiveStatus.ACTIVE);
		} catch (Exception e) {
			log.severe("Не удалось записать персональные данные социохаба ");
		}
        
        log.info("Добавлены персональные данные.");
      }
    }
  }
  
  private void processAdditionalData(RequestsEntity req, JSONObject profile, int partnerId) {
    // Определяем значимые для сравнения данные
    String maritalStatus = (String) profile.get("marital_status");
    if (StringUtils.isBlank(maritalStatus)) return;
    // Сравниваем с данными из заявки
    CreditRequestEntity cre = creditRequestDAO.getCreditRequestEntity(req.getCreditRequestId().getId());
    PeopleMainEntity pme = cre.getPeopleMainId();
		PeopleMiscEntity pmisce = peopleBean.findPeopleMiscActive(pme);
    if (pmisce != null && pmisce.getMarriageId() != null) {
      String code = pmisce.getMarriageId().getCode();
      if (maritalStatus.equals("single") && (code.equals("01") || code.equals("05") || code.equals("06"))
          || maritalStatus.equals("married") && code.equals("02")
          || maritalStatus.equals("engaged") && code.equals("03"))
        return;
    }
    // Проверяем, делались ли записи раньше
    List<PeopleMiscEntity> pmiscel = peopleBean.findPeopleMisc(pme, req.getCreditRequestId(), partnerId, ActiveStatus.ACTIVE);
    if (pmiscel == null || pmiscel.isEmpty()) {
      // Добавляем новую запись
      String code;
      switch (maritalStatus) {
        case "single": code = "01"; break;
        case "married": code = "02"; break;
        case "engaged": code = "03"; break;
        default: return;
      }
      Reference maritalReference = null;
      List<Reference> marriageReferences = refBooks.getMarriageTypes();
      for (Reference item : marriageReferences)
        if (item.getCode().equals(code)) {
          maritalReference = item;
          break;
        }
      if (maritalReference != null) {
        String childrenStatus = (String) profile.get("children");
        Integer childrenCount = null;
        if (StringUtils.isNotBlank(childrenStatus) && childrenStatus.equals("no")){
          childrenCount = 0;
        }
        peopleBean.newPeopleMisc(null, pme.getId(), cre.getId(), partnerId, maritalReference.getCodeInteger(), 
        		childrenCount, null,null, null, null, null, null, false, new Date(), ActiveStatus.ACTIVE, false);
        log.info("Добавлена запись о семейном положении.");
      }
    }
  }

  private void processAddress(RequestsEntity req, JSONObject profile, int partnerId) {
    // Определяем место жительства
    String countryCode = JsonUtils.getStringValue(profile, "country_id");
    String country = JsonUtils.getStringValue(profile, "country");
    if (country != null) country = StringUtils.capitalize(country);
    String region = JsonUtils.getStringValue(profile, "region");
    if (region != null) region = StringUtils.capitalize(region);
    String city = JsonUtils.getStringValue(profile, "city");
    if (city != null) city = StringUtils.capitalize(city);
    // Игнорируем сведения, если их мало
    if (StringUtils.isBlank(country) || StringUtils.isBlank(region) && StringUtils.isBlank(city))
      return;
    // Проверяем, делались ли записи раньше
   
    CreditRequestEntity cre = creditRequestDAO.getCreditRequestEntity(req.getCreditRequestId().getId());
    PeopleMainEntity pme = cre.getPeopleMainId();
	AddressEntity addr = peopleBean.findAddress(pme, partnerId, cre, BaseAddress.RESIDENT_ADDRESS, ActiveStatus.ACTIVE);
    if (addr != null) return;
    // Сохраняем адрес
    ArrayList<String> items = new ArrayList<>();
    items.add(country);
    if (StringUtils.isNotBlank(region)) items.add(region);
    if (StringUtils.isNotBlank(city)) items.add(city);
    String addressText = StringUtils.join(items, ", ");
  
    try {
		AddressEntity address=peopleBean.newAddressFias(null,pme.getId(), cre.getId(), partnerId, 
				FiasAddress.RESIDENT_ADDRESS, addressText, new Date(), null, countryCode, 
				ActiveStatus.ACTIVE,null,null,null,null,null);
		address.setCityName(city);
		addressBean.saveAddressEntity(address);
	
	} catch (Exception e) {
		log.severe("Не удалось записать адрес");
	}
  
    log.info("Добавлен адрес: " + addressText);
}
  
  private void processContacts(RequestsEntity req, JSONObject profile, int partnerId) {
    Partner partner = refBooks.getPartner(partnerId);
    CreditRequestEntity cre = creditRequestDAO.getCreditRequestEntity(req.getCreditRequestId().getId());
    PeopleMainEntity pme = cre.getPeopleMainId();
    
    // Загружаем записанные ранее контакты для дальнейшего сравнения
    List<PeopleContactEntity> partnerContacts = peopleBean.findPeopleByContact(null, null, pme.getId(), partnerId, ActiveStatus.ACTIVE,cre.getId());
    
    // Обрабатываем сотовые телефоны
    ArrayList<String> cellPhones = new ArrayList<>();
    if (profile.containsKey("cell_phone"))
      cellPhones.add(Convertor.fromMask((String) profile.get("cell_phone")));
    if (profile.containsKey("linked_cell_phone")) {
      String phone = Convertor.fromMask((String) profile.get("linked_cell_phone"));
      if (!cellPhones.contains(phone)) cellPhones.add(phone);
    }
    
  //проверяем, работает ли сайт с БД телефонов
  Map<String,Object> sparams=rulesBean.getSiteConstants();
  String site = (String) sparams.get(SettingsKeys.CONNECT_DB_URL); 
  boolean hasConnection=false;
  try {
      hasConnection=HTTPUtils.isConnected(site);
  } catch (Exception e1) {
      log.severe("Не удалось соединиться с сайтом "+site);
  }    
  
  if (!cellPhones.isEmpty()) {
      ArrayList<String> cellPhonesToAdd = new ArrayList<>();
      for (String phone : cellPhones) {
    	String cellPhone = Convertor.fromMask(phone);  
        List<PeopleContactEntity> pcel = peopleBean.findPeopleByContact(PeopleContact.CONTACT_CELL_PHONE, cellPhone, pme.getId(), Partner.CLIENT, ActiveStatus.ACTIVE,null);
        if (pcel.size()==0) {
        	cellPhonesToAdd.add(phone);
        }
      }
      if (!cellPhonesToAdd.isEmpty()) {
    	
        for (String phone : cellPhonesToAdd) {
          if (!containsContact(partnerContacts, PeopleContact.CONTACT_CELL_PHONE, phone)) {
        	  try {
				peopleBean.setPeopleContact(pme, partner.getEntity(), PeopleContact.CONTACT_CELL_PHONE, 
						  phone, false, cre.getId(), new Date(),hasConnection);
			  } catch (Exception e) {
			    log.severe("Не удалось добавить сотовый телефон "+phone);
			  }
            
            log.info("Добавлен сотовый телефон: " + phone);
          }
        }
      }
    }
    
    // Обрабатываем рабочие телефоны
    ArrayList<String> workPhones = JsonUtils.getStringArray(profile, "work_phone");
    if (!workPhones.isEmpty()) {
      ArrayList<String> workPhonesToAdd = new ArrayList<>();
      for (String phone : workPhones) {
        String workPhone = Convertor.fromMask(phone);
        List<PeopleContactEntity> pcel = peopleBean.findPeopleByContact(PeopleContact.CONTACT_WORK_PHONE, workPhone, pme.getId(), Partner.CLIENT, ActiveStatus.ACTIVE,null);
        if (pcel.size()==0) {
        	workPhonesToAdd.add(workPhone);
        }
      }
      if (!workPhonesToAdd.isEmpty()) {
        for (String phone : workPhonesToAdd) {
          if (!containsContact(partnerContacts, PeopleContact.CONTACT_WORK_PHONE, phone)) {
        	  try {
  				peopleBean.setPeopleContact(pme, partner.getEntity(), PeopleContact.CONTACT_WORK_PHONE, 
  						  phone, false, cre.getId(), new Date(),hasConnection);
  			  } catch (Exception e) {
  			    log.severe("Не удалось добавить сотовый телефон "+phone);
  			  }  
              log.info("Добавлен рабочий телефон: " + phone);
          }
        }
      }
    }
    
    // Обрабатываем адреса электронной почты
    ArrayList<String> emails = JsonUtils.getStringArray(profile, "email");
    if (profile.containsKey("linked_email")) {
      String email = Convertor.toString(profile.get("linked_email"));
      if (!emails.contains(email)) {
    	  emails.add(email);
      }
    }
    if (!emails.isEmpty()) {
      ArrayList<String> emailsToAdd = new ArrayList<>();
      for (String email : emails) {
        List<PeopleContactEntity> pcel = peopleBean.findPeopleByContact(PeopleContact.CONTACT_EMAIL, email, pme.getId(), Partner.CLIENT, ActiveStatus.ACTIVE,null);
        if (pcel.size()==0) {
        	emailsToAdd.add(email);
        }
      }
      if (!emailsToAdd.isEmpty()) {
        for (String email : emailsToAdd) {
          if (!containsContact(partnerContacts, PeopleContact.CONTACT_EMAIL, email)) {
        	  try {
  				peopleBean.setPeopleContact(pme, partner.getEntity(), PeopleContact.CONTACT_EMAIL, 
  						  email, false, cre.getId(), new Date());
  			  } catch (Exception e) {
  			    log.severe("Не удалось добавить email "+email);
  			  }
              log.info("Добавлен e-mail: " + email);
          }
        }
      }
    }
    
    // Обрабатываем идентификатор профиля в соцсети
    if (profile.containsKey("item_id")) {
      String profileId = Convertor.toString(profile.get("item_id"));
      if (StringUtils.isNotEmpty(profileId)){
    	  profileId=profileId.trim();
      }
      Integer contactType = null;
      switch (partnerId) {
        case Partner.VKONTAKTE: contactType = PeopleContact.NETWORK_VK; break;
        case Partner.ODNOKLASSNIKI: contactType = PeopleContact.NETWORK_OK; break;
        case Partner.MOIMIR: contactType = PeopleContact.NETWORK_MM; break;
        case Partner.FACEBOOK: contactType = PeopleContact.NETWORK_FB;
      }
      if (contactType != null) {
    	//ищем, писали ли идентификатор по партнеру  
        List<PeopleContactEntity> pcel = peopleBean.findPeopleByContact(contactType, profileId, pme.getId(), partnerId, ActiveStatus.ACTIVE,null);
        if (pcel.size()==0) {
          if (!containsContact(partnerContacts, contactType, profileId)) {
        	  try {
  				peopleBean.setPeopleContact(pme, partner.getEntity(), contactType, 
  						  profileId, false, cre.getId(), new Date());
  			  } catch (Exception e) {
  			    log.severe("Не удалось добавить id из соц.сети "+profileId);
  			  }
              log.info("Добавлен идентификтор профиля в соцсети: " + profileId);
          }
        }//end если не писали идентификатор
      }
    }
    
  }
  
  private void processScoring(RequestsEntity req, JSONObject user) {
    // Проверяем, записывались ли раньше кредитные рейтинги
    CreditRequestEntity cre = creditRequestDAO.getCreditRequestEntity(req.getCreditRequestId().getId());
	List<ScoringEntity> sel = creditInfo.findScorings(cre.getId(), cre.getPeopleMainId().getId(), Partner.SOCIOHUB, null);
    if (sel.size()>0){
    	return;
    }
    
    // Обрабатываем кредитные рейтинги
    int scoreCount = 0;
      
    String[] codes = {"model1", "model2", "model3"};
    for (String modelCode : codes) {
      if (user.containsKey(modelCode)) {
        ScoreModel model = refBooks.getModelByCode(refBooks.getPartnerEntity(Partner.SOCIOHUB), modelCode);
        if (model != null) {
        	creditInfo.saveScoring(cre.getId(), cre.getPeopleMainId().getId(), Partner.SOCIOHUB, 
        			null, modelCode, Convertor.toDouble(user.get(modelCode), CalcUtils.dformat), null,null);
       
          scoreCount++;
        }
      }
    }
    if (scoreCount > 0)
      log.info("Добавлено кредитных рейтингов: " + scoreCount);
  }
  
  // Хелперы
  
  
  private static boolean containsContact(List<PeopleContactEntity> contacts, Integer contactType, String value) {
    if (contacts != null)
      for (PeopleContactEntity ce : contacts)
        if (ce.getContactId().getCodeinteger().equals(contactType) && StringUtils.equals(ce.getValue(), value))
          return true;
    return false;
  }

@Override
public UploadingEntity createFileForUpload(Date sendingDate, Boolean isWork,
		List<Integer> creditIds) throws KassaException {
	
	return null;
}
  
    
}
