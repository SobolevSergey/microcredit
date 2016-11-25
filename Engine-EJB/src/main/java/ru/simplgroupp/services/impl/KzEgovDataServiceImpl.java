package ru.simplgroupp.services.impl;

import com.google.common.base.Strings;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.simplgroupp.dao.interfaces.KzEgovDataDAO;
import ru.simplgroupp.exception.KzEgovDataException;
import ru.simplgroupp.persistence.KzEgovDataAtsTypeEntity;
import ru.simplgroupp.persistence.KzEgovDataGeonimsTypeEntity;
import ru.simplgroupp.services.KzEgovDataService;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.kzegovdata.Ats;
import ru.simplgroupp.transfer.kzegovdata.Geonims;
import ru.simplgroupp.util.HTTPUtils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Интеграция с порталом Открытые данные Электронного правительства Республики Казахстан
 * http://data.egov.kz
 * http://data.egov.kz/api/v2/s_ats - Адресный регистр, Административно-территориальное устройство
 * http://data.egov.kz/api/v2/s_geonims - Адресный регистр, Устройство населенных пунктов
 * и т.д.
 * @author Rustem Saidaliyev
 */
@Stateless
public class KzEgovDataServiceImpl implements KzEgovDataService {

    private final static String BASE_URL = "http://data.egov.kz/api/v2";

    private final static String RESOURCE_ATS = "/s_ats";

    private final static String RESOURCE_GEONIMS = "/s_geonims";

    private final static String QUERY_ATS_LIST = BASE_URL + RESOURCE_ATS + "?source=" +
            "{\"from\":%s,\"size\":%s,\"query\":{\"bool\":{\"must\":[{\"match\":{\"parent_id\":\"%s\"}},%s{\"match\":{\"actual\":%s}}]}}}";

    private final static String QUERY_GEONIMS_LIST = BASE_URL + RESOURCE_GEONIMS + "?source=" +
            "{\"from\":%s,\"size\":%s,\"query\":{\"bool\":{\"must\":[{\"match\":{\"s_ats_id\":%s}},{\"match\":{\"parent_id\":%s}},%s{\"match\":{\"actual\":true}}]}}}";

    private final static String QUERY_ATS = BASE_URL + RESOURCE_ATS + "?source=" +
            "{\"query\":{\"bool\":{\"must\":[{\"match\":{\"id\":\"%s\"}}]}}}";

    private final static String QUERY_GEONIMS = BASE_URL + RESOURCE_GEONIMS + "?source=" +
            "{\"query\":{\"bool\":{\"must\":[{\"match\":{\"s_ats_id\":%s}},{\"match\":{\"id\":%s}}]}}}";


    private final static int DEFAULT_FROM = 0;

    private final static int DEFAULT_SIZE = 999999999;

    private final static long DEFAULT_ATS_PARENT_ID = 1; // Ats верхнего уровня, Республика Казахстан

    private final static long DEFAULT_GEONIMS_PARENT_ID = 0; // Geonims верхнего уровня

    private final static String RESOURCE_ATS_TYPE = "/d_ats_types?source=" +
            //"{\"size\":999999999,\"query\":{\"bool\":{\"must\":[{\"match\":{\"actual\":true}}]}}}";
            "{\"size\":999999999}";

    private final static String RESOURCE_GEONIMS_TYPE = "/d_geonims_types?source=" +
            //"{\"size\":999999999,\"query\":{\"bool\":{\"must\":[{\"match\":{\"actual\":true}}]}}}";
            "{\"size\":999999999}";

    // TODO рассмотреть вариант удаления по имени Entity
    private static final String SQL_DEL_ATS_TYPE = "delete from kzegovdata_atstype";
    private static final String SQL_DEL_GEONIMS_TYPE = "delete from kzegovdata_geonimstype";

    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;

    @Inject
    Logger logger;

    @EJB
    KzEgovDataDAO kzEgovDataDAO;

    public KzEgovDataServiceImpl() {
        super();
    }

    private JSONArray callService(String url) throws KzEgovDataException {

        /*
        byte urlBytes[] = url.getBytes();
        String urlUnicode;
        try {
            urlUnicode = new String(urlBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.severe("Не удалось конвертировать в UTF-8: " + url + ", ошибка " + e);
            throw new KzEgovDataException(e.getCause());
        }
        */

        logger.info(String.format("HTTP GET %s", url));

        byte[] response = null;

        try {
            response = HTTPUtils.sendHttp("GET", url, null, null, null);
        } catch (Exception e) {
            logger.severe("Не удалось получить ответ от сайта " + url + ", ошибка " + e);
            throw new KzEgovDataException(e.getCause());
        }

        String answer = null;
        try {
            answer = new String(response, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.severe("Не удалось конвертировать в UTF-8: " + new String(response) + ", ошибка " + e);
            throw new KzEgovDataException(e.getCause());
        }

        JSONParser parser = new JSONParser();
        JSONArray jsonAnswer=null;
        //пытаемся его разобрать
        Object object;
        try {
            object = parser.parse(answer);
        } catch (ParseException e) {
            logger.severe("Не удалось разобрать ответ сервиса Открытые данные Электронного правительства Республики Казахстан (Адресный регистр) " + answer);
            throw new KzEgovDataException(e.getCause());
        }

        if (object instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) object;
            String error = (String)jsonObject.get("error"); // если вернулась ошибка
            if (!Strings.isNullOrEmpty(error)) {
                throw new KzEgovDataException(error);
            }
        } else {
            jsonAnswer = (JSONArray) object;
        }

        return jsonAnswer;
    }

    private Ats getAts(JSONObject obj) {

        Ats ats = new Ats();

        ats.setActual(Convertor.toBoolean(obj.get("actual")));
        ats.setAtsTypeId(Convertor.toLong(obj.get("d_ats_type_id")));
        ats.setCato(Convertor.toString(obj.get("cato")));
        ats.setId(Convertor.toLong(obj.get("id")));
        ats.setNameKz(Convertor.toString(obj.get("name_kaz")));
        ats.setNameRu(Convertor.toString(obj.get("name_rus")));
        ats.setParentId(Convertor.toLong(obj.get("parent_id")));
        ats.setRco(Convertor.toString(obj.get("rco")));

        KzEgovDataAtsTypeEntity kzEgovDataAtsTypeEntity = kzEgovDataDAO.getKzEgovDataAtsTypeEntity(ats.getAtsTypeId());
        ats.setAtsTypeNameRu(kzEgovDataAtsTypeEntity.getValueRu());

        return ats;
    }

    private Geonims getGeonims(JSONObject obj) {

        Geonims geonims = new Geonims();

        geonims.setActual(Convertor.toBoolean(obj.get("actual")));
        geonims.setGeonimsTypeId(Convertor.toLong(obj.get("d_geonims_type_id")));
        geonims.setCato(Convertor.toString(obj.get("cato")));
        geonims.setId(Convertor.toLong(obj.get("id")));
        geonims.setNameKz(Convertor.toString(obj.get("name_kaz")));
        geonims.setNameRu(Convertor.toString(obj.get("name_rus")));
        geonims.setParentId(Convertor.toLong(obj.get("parent_id")));
        geonims.setRco(Convertor.toString(obj.get("rco")));
        geonims.setAtsId(Convertor.toLong(obj.get("s_ats_id")));

        KzEgovDataGeonimsTypeEntity kzEgovDataGeonimsTypeEntity = kzEgovDataDAO.getKzEgovDataGeonimsTypeEntity(geonims.getGeonimsTypeId());
        geonims.setGeonimsTypeNameRu(kzEgovDataGeonimsTypeEntity.getValueRu());

        return geonims;
    }


    /**
     * Возвращает список сущностей Ats, @see ru.simplgroupp.transfer.kzegovdata.Ats
     * @param parentId - ID сущности, дочерние сущности которого необходимо вернуть
     * @param page - Разбиение на страницы, номер страницы
     * @param pageSize - Разбиение на страницы, размер страницы
     * @param term - Выражение LIKE поиска
     * @param isActual - true - вернуть только актуальные, false - вернуть все
     * @return
     */
    @Override
    public List<Ats> getAtsList(Long parentId, Integer page, Integer pageSize, String term, boolean isActual) {

        if (pageSize == null) pageSize = DEFAULT_SIZE;
        int from = (page != null) ? ((page-1) * pageSize) : DEFAULT_FROM;
        if (parentId == null) parentId = DEFAULT_ATS_PARENT_ID;
        String wildcard = "";
        if (term != null && !term.isEmpty()) {
            wildcard = String.format("{\"wildcard\":{\"name_rus\":\"*%s*\"}},", term.toLowerCase());
        }

        String url = String.format(QUERY_ATS_LIST, from, pageSize, parentId, wildcard, isActual);


        List<Ats> atsList = new ArrayList<Ats>();

        JSONArray jsonAnswer = null;
        try {
            jsonAnswer = callService(url);
        } catch (KzEgovDataException e) {
            logger.severe(e.getMessage());
            return atsList;
        }

        for (int i = 0 ; i < jsonAnswer.size(); i++) {
            JSONObject obj = (JSONObject)jsonAnswer.get(i);

            atsList.add(getAts(obj));
        }

        return atsList;
    }

    /**
     * Возвращает список сущностей Geonims, @see ru.simplgroupp.transfer.kzegovdata.Geonims
     * @param atsId - ID сущности Ats, @see ru.simplgroupp.transfer.kzegovdata.Ats
     * @param parentId - ID сущности, дочерние сущности которого необходимо вернуть
     * @param page - Разбиение на страницы, номер страницы
     * @param pageSize - Разбиение на страницы, размер страницы
     * @param term - Выражение LIKE поиска
     * @param isActual - true - вернуть только актуальные, false - вернуть все
     * @return
     */
    @Override
    public List<Geonims> getGeonimsList(Long atsId, Long parentId, Integer page, Integer pageSize, String term, boolean isActual) {

        if (pageSize == null) pageSize = DEFAULT_SIZE;
        int from = (page != null) ? ((page-1) * pageSize) : DEFAULT_FROM;
        if (parentId == null) parentId = DEFAULT_GEONIMS_PARENT_ID;
        String wildcard = "";
        if (term != null && !term.isEmpty()) {
            wildcard = String.format("{\"wildcard\":{\"name_rus\":\"*%s*\"}},", term.toLowerCase());
        }

        String url = String.format(QUERY_GEONIMS_LIST, from, pageSize, atsId, parentId, wildcard, isActual);

        List<Geonims> geonimsList = new ArrayList<Geonims>();

        JSONArray jsonAnswer = null;
        try {
            jsonAnswer = callService(url);
        } catch (KzEgovDataException e) {
            logger.severe(e.getMessage());
            return geonimsList;
        }

        for (int i = 0 ; i < jsonAnswer.size(); i++) {
            JSONObject obj = (JSONObject)jsonAnswer.get(i);

            geonimsList.add(getGeonims(obj));
        }

        return geonimsList;
    }

    /**
     * Обновление таблицы AR_ATS_TYPE из сервиса Адресного регистра
     * Типы административно-территориальных единиц (области, города, поселки и т.д.)
     * http://data.egov.kz/api/v2/d_ats_types
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateAtsType() {

        logger.info("Начало процесса обновления таблицы AR_ATS_TYPE");

        String url = BASE_URL + RESOURCE_ATS_TYPE;

        JSONArray jsonAnswer = null;
        try {
            jsonAnswer = callService(url);
        } catch (KzEgovDataException e) {
            return;
        }

        // очищаем таблицу
        Query qdel = emMicro.createNativeQuery(SQL_DEL_ATS_TYPE);
        qdel.executeUpdate();

        // заполняем таблицу
        for (int i = 0 ; i < jsonAnswer.size(); i++) {

            JSONObject obj = (JSONObject)jsonAnswer.get(i);

            KzEgovDataAtsTypeEntity KzEgovDataAtsTypeEntity = new KzEgovDataAtsTypeEntity();

            KzEgovDataAtsTypeEntity.setId(Convertor.toLong(obj.get("id")));
            KzEgovDataAtsTypeEntity.setActual(Convertor.toBoolean(obj.get("actual")));
            KzEgovDataAtsTypeEntity.setCode(Convertor.toLong(obj.get("code")));
            KzEgovDataAtsTypeEntity.setShortValueKz(Convertor.toString(obj.get("short_value_kz")));
            KzEgovDataAtsTypeEntity.setShortValueRu(Convertor.toString(obj.get("short_value_ru")));
            KzEgovDataAtsTypeEntity.setValueKz(Convertor.toString(obj.get("value_kz")));
            KzEgovDataAtsTypeEntity.setValueRu(Convertor.toString(obj.get("value_ru")));

            emMicro.persist(KzEgovDataAtsTypeEntity);
        }

        logger.info("Таблица AR_ATS_TYPE успешно обновлена");

    }

    /**
     * Обновление таблицы AR_GEONIMS_TYPE из сервиса Адресного регистра
     * http://data.egov.kz/api/v2/d_geonims_types
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateGeonimsType() {
        logger.info("Начало процесса обновления таблицы AR_GEONIMS_TYPE");

        String url = BASE_URL + RESOURCE_GEONIMS_TYPE;

        JSONArray jsonAnswer = null;
        try {
            jsonAnswer = callService(url);
        } catch (KzEgovDataException e) {
            return;
        }

        // очищаем таблицу
        Query qdel = emMicro.createNativeQuery(SQL_DEL_GEONIMS_TYPE);
        qdel.executeUpdate();

        // заполняем таблицу
        for (int i = 0 ; i < jsonAnswer.size(); i++) {

            JSONObject obj = (JSONObject)jsonAnswer.get(i);

            KzEgovDataGeonimsTypeEntity kzEgovDataGeonimsTypeEntity = new KzEgovDataGeonimsTypeEntity();

            kzEgovDataGeonimsTypeEntity.setId(Convertor.toLong(obj.get("id")));
            kzEgovDataGeonimsTypeEntity.setActual(Convertor.toBoolean(obj.get("actual")));
            kzEgovDataGeonimsTypeEntity.setCode(Convertor.toLong(obj.get("code")));
            kzEgovDataGeonimsTypeEntity.setShortValueKz(Convertor.toString(obj.get("short_value_kz")));
            kzEgovDataGeonimsTypeEntity.setShortValueRu(Convertor.toString(obj.get("short_value_ru")));
            kzEgovDataGeonimsTypeEntity.setValueKz(Convertor.toString(obj.get("value_kz")));
            kzEgovDataGeonimsTypeEntity.setValueRu(Convertor.toString(obj.get("value_ru")));
            kzEgovDataGeonimsTypeEntity.setThisIs(Convertor.toString(obj.get("this_is")));

            emMicro.persist(kzEgovDataGeonimsTypeEntity);
        }

        logger.info("Таблица AR_GEONIMS_TYPE успешно обновлена");
    }

    /**
     * Возвращает Ats @see ru.simplgroupp.transfer.kzegovdata.Ats
     * @param atsId - ID Ats
     * @return
     */
    @Override
    public Ats getAts(Long atsId) throws KzEgovDataException{

        if (atsId == null) {
            throw new KzEgovDataException("Ошибка поиска Ats, atsId = null");
        }

        String url = String.format(QUERY_ATS, atsId);

        JSONArray jsonAnswer = null;
        try {
            jsonAnswer = callService(url);
        } catch (KzEgovDataException e) {
            logger.severe(e.getMessage());
            return null;
        }

        if (jsonAnswer.size() == 0) {
            return null;
        }

        if (jsonAnswer.size() > 1) {
            throw new KzEgovDataException(String.format("Ошибка поиска Ats, найдено более одного Ats, atsId = %s", atsId));
        }

        JSONObject obj = (JSONObject)jsonAnswer.get(0);

        return getAts(obj);
    }

    /**
     * Возвращает Geonims @see ru.simplgroupp.transfer.kzegovdata.Geonims
     * @param atsId - ID Ats
     * @param geonimsId - ID Geonims
     * @return
     */
    @Override
    public Geonims getGeonims(Long atsId, Long geonimsId) throws KzEgovDataException {

        if (atsId == null || geonimsId == null) {
            throw new KzEgovDataException(String.format("Ошибка поиска Geonims, atsId = %s, geonimsId = %s", atsId, geonimsId));
        }

        String url = String.format(QUERY_GEONIMS, atsId, geonimsId);

        JSONArray jsonAnswer = null;
        try {
            jsonAnswer = callService(url);
        } catch (KzEgovDataException e) {
            logger.severe(e.getMessage());
            return null;
        }

        if (jsonAnswer.size() == 0) {
            return null;
        }

        if (jsonAnswer.size() > 1) {
            throw new KzEgovDataException(String.format("Ошибка поиска Geonims, найдено более одного Geonims, atsId = %s, geonimsId = %s", atsId, geonimsId));
        }

        JSONObject obj = (JSONObject)jsonAnswer.get(0);

        return getGeonims(obj);
    }


}
