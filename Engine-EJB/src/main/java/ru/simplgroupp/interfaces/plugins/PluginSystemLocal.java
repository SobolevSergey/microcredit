package ru.simplgroupp.interfaces.plugins;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.workflow.PluginExecutionContext;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PluginSystemLocal {

    public enum Mode {
        SINGLE, // режим одиночных запросов
        PACKET; // пакетный режим
    }

    public enum ExecutionMode {
        AUTOMATIC,
        MANUAL;
    }

    public enum SyncMode {
        SYNC,
        ASYNC;
    }

    /**
     * Имя подсистемы
     *
     * @return
     */
    String getSystemName();

    /**
     * Заголовок подсистемы
     *
     * @return
     */
    String getSystemDescription();

    /**
     * Заглушка или рабочая версия
     *
     * @return
     */
    boolean isFake();

    /**
     * Какие режимы поддерживаются
     *
     * @return
     */
    EnumSet<Mode> getModesSupported();

    EnumSet<ExecutionMode> getExecutionModesSupported();

    EnumSet<SyncMode> getSyncModesSupported();
    
    /**
     * Возвращает ID системы-партнёра, если оно есть
     * @return
     */
    public Integer getPartnerId();

    /**
     * При расчёте каких моделей может применяться эта подсистема. Если подсистема не участвует при расчёте моделей,
     * возвращать пустой Set. Название модели = ModelKeys.TARGET_*
     *
     * @return
     */
    Set<String> getModelTargetsSupported();

    /**
     * Какой тип бизнес-объектов поддерживается
     *
     * @return
     */
    String getBusinessObjectClass();

    /**
     * Выполнить действия в одиночном режиме
     *
     * @param businessObjectClass - класс бизнес-объекта
     * @param businessObjectId - id бизнес-объекта
     * @param context - контекст плагина
     * @throws ActionException
     */
    void executeSingle(String businessObjectClass, Object businessObjectId,
                              PluginExecutionContext context) throws ActionException;

    /**
     * Добавить заданные объекты в формирующйся пакет
     *
     * @param businessObjectClass - класс бизнес-объекта
     * @param businessObjectId - id бизнес-объекта
     * @param context - контекст плагина
     * @throws ActionException
     */
    void addToPacket(String businessObjectClass, Object businessObjectId,
                            PluginExecutionContext context) throws ActionException;

    /**
     * Выполнить действия в пакетном режиме. Возвращает идентификаторы полученных объектов
     *
     * @param context
     */
    List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException;

    /**
     * Послать одиночный асинхронный запрос. Возвращает true, если ответ получен сразу же, и повторный запрос не нужен
     *
     * @param businessObjectClass - класс бизнес-объекта
     * @param businessObjectId - id бизнес-объекта
     * @param context - контекст плагина
     * @return
     * @throws ActionException
     */
    boolean sendSingleRequest(String businessObjectClass, Object businessObjectId,
                                     PluginExecutionContext context) throws ActionException;

    /**
     * Запросить ответ на одиночный запрос. Возвращает true, если ответ получен
     *
     * @param businessObjectClass - класс бизнес-объекта
     * @param businessObjectId - id бизнес-объекта
     * @param context - контекст плагина
     * @return
     * @throws ActionException
     */
    boolean querySingleResponse(String businessObjectClass, Object businessObjectId,
                                       PluginExecutionContext context) throws ActionException;

    /**
     * Послать пакетный асинхронный запрос. Если ответ получен сразу же,
     * то возвращает идентификаторы полученных объектов или null, если ответа пока нет.
     *
     * @param context - контекст плагина
     */
    List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException;

    /**
     * Запросить ответ на пакетный асинхронный запрос. Возвращает идентификаторы полученных объектов
     * или null, если ответа пока нет.
     *
     * @param context - контекст плагина
     */
    List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException;
    
    public PluginConfig getDebugConfig(String customKey);
    
    public PluginConfig getWorkConfig();
    
    public Map<String, Object> getPersistentVariables(String customKey);
}
