package ru.simplgroupp.dao.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
public interface ProductDAO {

	/**
	 * возвращает продукт по id
	 * @param productId - id продукта
	 * @return
	 */
	public ProductsEntity getProduct(Integer productId);
	/**
	 * возвращает продукт по id
	 * @param productId - id продукта
	 * @param options - что инициализируем
	 * @return
	 */
	public Products getProduct(Integer productId,Set options);
	/**
	 * возвращает продукт по умолчанию
	 * @param options - что инициализируем
	 * @return
	 */
	public Products getProductDefault(Set options);
	/**
	 * возвращает продукт по умолчанию
	 * @return
	 */
	public ProductsEntity getProductDefault();
	/**
	 * возвращает продукт по коду
	 * @param code - код продукта
	 * @param options - что инициализируем
	 * @return
	 */
	public Products findProductByCode(String code,Set options);
	/**
	 * возвращает продукт по коду
	 * @param code - код продукта
	 * @return
	 */
	public ProductsEntity findProductByCode(String code);
	/**
	 * сохраняем кредитный продукт
	 * @param product - кредитный продукт
	 */
	public ProductsEntity saveProduct(ProductsEntity product);
	/**
	 * возвращает активные продукты
	 */
	List<ProductsEntity> getProductsActive();
	/**
	 * возвращает все продукты
	 */
	List<Products> getProducts(Set options);
    /**
     * возвращает активные продукты
     * @param options - что инициализируем
     * @return
     */
	List<Products> getProductsActive(Set options);
	/**
	 * возвращает активную конфигурацию продукта на текущую дату
	 * @param productId - id продукта
	 * @return
	 */
	public List<ProductConfigEntity> findProductConfigActive(Integer productId);
	/**
	 * возвращает активную конфигурацию продукта на дату
	 * @param productId - id продукта
	 * @param date - дата
	 * @return
	 */
	public List<ProductConfigEntity> findProductConfigActive(Integer productId,Date date);
	/**
	 * возвращает активную конфигурацию продукта на текущую дату
	 * @param productId - id продукта
	 * @param options - что инициализируем
	 * @return
	 */
	public List<ProductConfig> findProductConfigActive(Integer productId,Set options);
	/**
	 * возвращает активную конфигурацию продукта на дату
	 * @param productId - id продукта
	 * @param date - дата
	 * @param options - что инициализируем
	 * @return
	 */
	public List<ProductConfig> findProductConfigActive(Integer productId,Date date,Set options);
	/**
	 * возращает определенный параметр конфигурации по названию
	 * @param productId - id продукта
	 * @param configTypeId - id вида конфигурации
	 * @param date - дата
	 * @param name - название
	 * @return
	 */
	public ProductConfigEntity findProductConfigActiveByName(Integer productId,Integer configTypeId,
			Date date,String name);
	/**
	 * возращает определенный параметр конфигурации по названию на текущую дату
	 * @param productId - id продукта
	 * @param configTypeId - id вида конфигурации
	 * @param name - название
	 * @return
	 */
	public ProductConfigEntity findProductConfigActiveByName(Integer productId,Integer configTypeId,String name);
	/**
	 * возращает параметры конфигурации по виду конфигурации 
	 * @param productId - id продукта
	 * @param configTypeId - id вида конфигурации
	 * @param date - дата
	 * @return
	 */
	public List<ProductConfigEntity> findProductConfigActiveByType(Integer productId,Integer configTypeId,Date date);
	/**
	 * возращает параметры конфигурации по виду конфигурации на текущую дату
	 * @param productId - id продукта
	 * @param configTypeId - id вида конфигурации
	 * @return
	 */
	public List<ProductConfigEntity> findProductConfigActiveByType(Integer productId,Integer configTypeId);
	/**
	 * возращает параметры конфигурации по названию процесса 
	 * @param productId - id продукта
	 * @param code - название процесса
	 * @param date - дата
	 * @return
	 */
	public List<ProductConfigEntity> findProductConfigActiveByBPName(Integer productId,String code,Date date);
	/**
	 * возращает параметры конфигурации по названию процесса
	 * @param productId - id продукта
	 * @param code - название процесса
     * @return
	 */
	public List<ProductConfigEntity> findProductConfigActiveByBPName(Integer productId,String code);
	/**
	 * возращает параметры конфигурации по виду конфигурации на текущую дату
	 * @param productId - id продукта
	 * @param configTypeId - id вида конфигурации
	 * @param options - что инициализируем
	 * @return
	 */
	public List<ProductConfig> findProductConfigByType(Integer productId,Integer configTypeId,Set options);
	/**
	 * возвращает сообщения по виду конфигурации
	 * @param productId - id продукта
	 * @param configTypeId - id вида конфигурации
	 * @param options - что инициализируем
	 * @return
	 */
	public List<ProductMessages> findProductMessagesByType(Integer productId,Integer configTypeId,Set options);
	/**
	 * возвращает сообщения продукта
	 * @param productId - id продукта
	 * @return
	 */
	public List<ProductMessagesEntity> findProductMessages(Integer productId);
	/**
	 * возвращает правила по виду конфигурации
	 * @param productId - id продукта
	 * @param configTypeId - id вида конфигурации
	 * @param date - дата
	 * @param option - что инициализируем
	 * @return
	 */
	public List<ProductRules> findProductRulesActiveByType(Integer productId,Integer configTypeId,Date date,Set options);
	/**
	 * сохраняет конфигурацию продукта
	 * @param productConfig - конфигурация продукта
	 * @return
	 */
	public ProductConfigEntity saveProductConfig(ProductConfigEntity productConfig);
	/**
	 * сохраняет сообщение продукта
	 * @param message - сообщение
	 * @return
	 */
	public ProductMessagesEntity saveProductMessage(ProductMessagesEntity message);
	/**
	 * сохраняет правило продукта
	 * @param rule - правило
	 * @return
	 */
	public ProductRulesEntity saveProductRule(ProductRulesEntity rule);
	/**
	 * возвращает активнвые скрипты и правила продукта на дату
	 * @param productId - id продукта
	 * @param date - дата
	 * @param options - что инициализируем
	 * @return
	 */
	public List<ProductRules> findProductRulesActive(Integer productId,Date date,Set options);
	/**
	 * возвращает активные скрипты и правила продукта на дату
	 * @param productId - id продукта
	 * @param options - что инициализируем
	 * @return
	 */
	public List<ProductRules> findProductRulesActive(Integer productId,Set options);
	/**
	 * возвращает активные скрипты и правила продукта на дату
	 * @param productId - id продукта
	 * @param options - что инициализируем
	 * @return
	 */
	public List<ProductRulesEntity> findProductRulesActive(Integer productId);
	/**
	 * возвращает правило (скрипт) продукта по виду скрипта и названию
	 * @param productId - id продукта
	 * @param configTypeId - id вида конфигурации
	 * @param scriptTypeId - id типа скрипта
	 * @param date - дата
	 * @param name - название
	 * @return
	 */
	public ProductRulesEntity findProductRulesActiveByScriptTypeAndName(Integer productId,Integer configTypeId,
			Integer scriptTypeId,Date date,String name);
	/**
	 * ищем сообщение по названию
	 * @param productId - id продукта
	 * @param code - название БП
	 * @param name - название сообщения
	 * @return
	 */
	public ProductMessagesEntity findProductMessageByName(Integer productId,String code,String name);
	/**
	 * ищем сообщение по названию - для общих сообщений для всех продуктов
	 * @param name - название сообщения
	 * @return
	 */
	public ProductMessagesEntity findMessageByName(String name);
	/**
	 * ищем сообщения для процесса
	 * @param productId - id продукта
	 * @param code - название БП
	 * @return
	 */
	public List<ProductMessagesEntity> findProductMessagesForProcess(Integer productId,String code);
	/**
	 * возвращаем сообщение из списка по названию
	 * @param messages - список сообщений
	 * @param name - название
	 * @return
	 */
	public ProductMessagesEntity getProductMessageFromList(List<ProductMessagesEntity> messages,String name);
	/**
	 * возвращает список правил по названию knowledge base 
	 * @param kbase - knowledge base
	 * @param date - дата
	 * @return
	 */
	public List<ProductRulesEntity> findAllRulesActiveByKbase(String kbase,Date date);
	/**
	 * список knowledge base
	 * @return
	 */
	public List<String> listKbase();
	
	/**
	 * возвращает конфиг продукта по id
	 * @param id - id конфига
	 * @return
	 */
	public ProductConfigEntity getProductConfig(int id);
	/**
	 * возвращает сообщение для продукта по id
	 * @param id - id сообщения
	 * @return
	 */
	public ProductMessagesEntity getProductMessage(int id);
	/**
	 * возвращает правило для продукта по id
	 * @param id - id правила
	 * @return
	 */
	public ProductRulesEntity getProductRule(int id);
	/**
	 * сохраняем сообщения продукта списком
	 * @param messages - сообщения
	 */
	public void saveListProductMessages(List<ProductMessages> messages);
	/**
	 * сохраняем конфигурацию продукта списком
	 * @param config- конфигурация
	 */
	public void saveListProductConfig(List<ProductConfig> configs);
	/**
	 * сохраняем правила продукта списком
	 * 
	 * @param rules - правила
	 */
	public void saveListProductRules(List<ProductRules> rules);
	/**
	 * удалить продукт
	 * 
	 * @param productId - id продукта
	 */
	public void deleteProduct(Integer productId);
	/**
	 * удалить конфиг продукта
	 * 
	 * @param id - id конфига
	 */
	public void deleteProductConfig(Integer id);
	/**
	 * удалить сообщение продукта
	 * 
	 * @param id - id сообщения
	 */
	public void deleteProductMessage(Integer id);
	/**
	 * удалить правило продукта
	 * 
	 * @param id - id правила
	 */
	public void deleteProductRule(Integer id);
	/**
	 * возвращает список конфигураций продукта
	 * 
	 * @param productId - id продукта
	 * @param date - дата
	 * @param isActive - активность
	 * @param configTypeId - id конфигурации
	 * @param processName - название процесса
	 * @param name - название конфигурации
	 * @return
	 */
	public List<ProductConfigEntity> findListProductConfig(Integer productId,Date date,Integer isActive,
			Integer configTypeId,String processName,String name);
	/**
	 * сделать продукт активным
	 * @param productId - id продукта
	 */
	public void makeProductActive(Integer productId);
	/**
	 * сделать продуктом по умолчанию
	 * @param productId - id продукта
	 */
	public void makeProductDefault(Integer productId);
	
	 /**
     * добавляем продукт
     *
     * @param name        - название
     * @param description - описание
     * @param code        - код
     * @param type        - вид продукта (как осуществяются платежи)
     * @return
     */
	ProductsEntity addProductHeader(String name, String description,
			String code,Integer type);
	
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
     * возвращает сообщение для бизнес-действия
     *
     * @param productId - id продукта
     * @param signalRef - сигнал
     * @return
     */
    public ProductMessagesEntity getMessageForBusinessAction(Integer productId, String signalRef);

}
