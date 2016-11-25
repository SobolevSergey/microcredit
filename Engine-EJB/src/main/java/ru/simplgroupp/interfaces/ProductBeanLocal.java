package ru.simplgroupp.interfaces;

import java.util.Date;
import java.util.Map;

import javax.ejb.Local;

import ru.simplgroupp.persistence.ProductConfigEntity;
import ru.simplgroupp.persistence.ProductMessagesEntity;
import ru.simplgroupp.persistence.ProductRulesEntity;
import ru.simplgroupp.persistence.ProductsEntity;
import ru.simplgroupp.transfer.ProductConfig;
import ru.simplgroupp.transfer.ProductMessages;
import ru.simplgroupp.transfer.ProductRules;
import ru.simplgroupp.transfer.Products;

@Local
public interface ProductBeanLocal {

    /**
     * конфигурация продукта для новой заявки
     *
     * @param productId - id продукта
     * @return
     */
    Map<String, Object> getNewRequestProductConfig(Integer productId);

    /**
     * конфигурация продукта по умолчанию для новой заявки
     * @return
     */
    Map<String, Object> getNewRequestDefaultProductConfig();

    /**
     * конфигурация продукта для продления
     *
     * @param productId - id продукта
     * @return
     */
    Map<String, Object> getProlongProductConfig(Integer productId);

    /**
     * конфигурация продукта для рефинансирования
     *
     * @param productId - id продукта
     * @return
     */
    Map<String, Object> getRefinanceProductConfig(Integer productId);

    /**
     * конфигурация продукта для просрочки
     *
     * @param productId - id продукта
     * @return
     */
    Map<String, Object> getOverdueProductConfig(Integer productId);

    /**
     * конфигурация продукта для коллектора
     *
     * @param productId - id продукта
     * @return
     */
    Map<String, Object> getCollectorProductConfig(Integer productId);

    /**
     * Конфигурация продукта погашения кредита
     *
     * @param productID ID продукта
     * @return список конфигурации
     */
    Map<String, Object> getCreditReturnPayment(Integer productID);

    /**
     * Конфигурация продукта для выплаты клиенту
     *
     * @param productID ID продукта
     * @return конфигурация
     */
    Map<String, Object> getCreditPaymentProductConfig(Integer productID);

    /**
     * конфигурация продукта по принятию решений
     *
     * @param productId - id продукта
     * @return
     */
    Map<String, Object> getConsiderProductConfig(Integer productId);

    /**
     * все конфигурации продукта
     *
     * @param productId - id продукта
     * @return
     */
    Map<String, Object> getAllProductConfig(Integer productId);

    /**
     * возвращает конфигурацию для БП
     *
     * @param productId - id продукта
     * @param bpName    - название процесса
     * @return
     */
    Map<String, Object> getProductConfigForBP(Integer productId, String bpName);

    /**
     * возвращает текст js-функции
     *
     * @param productId    - id продукта
     * @param configTypeId - вид конфигурации
     * @param name         - название функции
     * @param date         - дата актуальности
     * @return
     */
    public String getFuctionJS(Integer productId, Integer configTypeId, String name, Date date);

    /**
     * возвращает текст js-функции ставки незарегистрированного пользователя для продукта
     *
     * @return
     */
    public String getFunctionStakeNewRequestUnknown(Integer productId);

    /**
     * изменяем конфигурацию продукта с историей
     *
     * @param configId - id конфигурации продукта
     * @param databeg  - дата изменения
     * @param value    - изменяемое значение
     */
    public void changeProductConfig(Integer configId, Date databeg, String value);

    /**
     * изменяем правило продукта с историей
     *
     * @param ruleId  - id правила
     * @param databeg - дата изменения
     * @param value   - изменяемое значение
     */
    public void changeProductRule(Integer ruleId, Date databeg, String value);

    /**
     * добавляем новый конфиг
     *
     * @param product      - продукт
     * @param configTypeId - вид конфига
     * @param name         - название
     * @param dataType     - тип данных
     * @param databeg      - дата начала
     * @param dataend      - дата окончания
     * @param isActive     - активность
     * @param description  - описание
     * @param value        - значение
     */
    public ProductConfigEntity newProductConfig(ProductsEntity product, Integer configTypeId, String name, String dataType,
                                                Date databeg, Date dataend, Integer isActive, String description, String value);

    /**
     * добавляем новое правило
     *
     * @param product      - продукт
     * @param configTypeId - вид конфига
     * @param name         - название
     * @param kbase        - knowledge base
     * @param databeg      - дата начала
     * @param dataend      - дата окончания
     * @param isActive     - активность
     * @param description  - описание
     * @param ruleBody     - текст правила
     * @param scriptTypeId - тип скрипта
     */
    public ProductRulesEntity newProductRule(ProductsEntity product, Integer configTypeId, String name, String kbase,
                                             Date databeg, Date dataend, Integer isActive, String description, String ruleBody, Integer scriptTypeId);

    /**
     * добавляем новое сообщение
     *
     * @param product      - продукт
     * @param configTypeId - вид конфига
     * @param name         - название
     * @param subject      - тема
     * @param body         - текст
     * @param description  - описание
     * @param signalRef    - сигнал
     */
    public ProductMessagesEntity newProductMessage(ProductsEntity product, Integer configTypeId, String name, String subject,
                                                   String body, String description, String signalRef);

    /**
     * добавляем продукт
     *
     * @param name        - название
     * @param description - описание
     * @param code        - код
     * @param type        - вид продукта (как осуществяются платежи)
     * @return
     */
    public ProductsEntity addProductHeader(String name, String description, String code, Integer type);

    /**
     * добавляем новую запись в конфигурацию
     *
     * @param productId    - id продукта
     * @param configTypeId - вид конфигурации
     * @return
     */
    public ProductConfig addProductConfig(Integer productId, Integer configTypeId);

    /**
     * добавляем новую запись в сообщения
     *
     * @param productId    - id продукта
     * @param configTypeId - вид конфигурации
     * @return
     */
    public ProductMessages addProductMessage(Integer productId, Integer configTypeId);

    /**
     * добавляем новую запись в правила
     *
     * @param productId    - id продукта
     * @param configTypeId - вид конфигурации
     * @return
     */
    public ProductRules addProductRule(Integer productId, Integer configTypeId);

    /**
     * возвращает сообщение для бизнес-действия
     *
     * @param productId - id продукта
     * @param signalRef - сигнал
     * @return
     */
    public ProductMessagesEntity getMessageForBusinessAction(Integer productId, String signalRef);

    /**
     * копируем продукт
     *
     * @param productId - id продукта, который копируем
     * @return
     */
    public Products copyProduct(Integer productId);
}
