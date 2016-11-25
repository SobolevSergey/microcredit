package ru.simplgroupp.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.jpa.JPAUtils;
import ru.simplgroupp.persistence.ProductConfigEntity;
import ru.simplgroupp.persistence.ProductMessagesEntity;
import ru.simplgroupp.persistence.ProductRulesEntity;
import ru.simplgroupp.persistence.ProductsEntity;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.DefaultStatus;
import ru.simplgroupp.transfer.ProductConfig;
import ru.simplgroupp.transfer.ProductMessages;
import ru.simplgroupp.transfer.ProductRules;
import ru.simplgroupp.transfer.Products;
import ru.simplgroupp.transfer.RefHeader;
import ru.simplgroupp.util.AppUtil;
import ru.simplgroupp.util.ProductKeys;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ProductDAOImpl implements ProductDAO{

	@PersistenceContext(unitName = "MicroPU")
	private EntityManager emMicro;	
	
	@EJB
	ReferenceBooksLocal refBooks;
	
	@Inject Logger logger;
	
	@Override
	public ProductsEntity getProduct(Integer productId) {
		return emMicro.find(ProductsEntity.class, productId);
	}

	@Override
	public Products getProduct(Integer productId, Set options) {
		ProductsEntity productEntity=getProduct(productId);
		if (productEntity!=null){
			Products product=new Products(productEntity);
			product.init(options);
			return product;
		}
		return null;
	}

	@Override
	public Products findProductByCode(String code,Set options) {
		ProductsEntity products=findProductByCode(code);
		if (products!=null) {
			Products product=new Products(products);
			product.init(options);
			return product;
		} 
		return null;
		
	}

	@Override
	public ProductsEntity findProductByCode(String code) {
		return (ProductsEntity) JPAUtils.getSingleResult(emMicro, "findProductByCode", Utils.mapOf("code", code));
	}
	
	@Override
	public ProductsEntity saveProduct(ProductsEntity product) {
		ProductsEntity productNew=emMicro.merge(product);
		emMicro.persist(productNew);
		return productNew;
	}
	
    @Override
    public List<ProductsEntity> getProductsActive() {
    	return JPAUtils.getResultList(emMicro, "getProductsActive", Utils.mapOf("isActive", ActiveStatus.ACTIVE));
    }
    
    @Override
    public List<Products> getProductsActive(Set options) {
    	List<ProductsEntity> lst = getProductsActive();
    	List<Products> lstRes;
		try {
			lstRes = AppUtil.wrapCollection(Products.getWrapConstructor(), lst);
		} catch (Exception e) {
			return null;
		}
    	Utils.initCollection(lstRes, options);
    	return lstRes;
    }

    @Override
    public List<Products> getProducts(Set options) {
    	List<ProductsEntity> lst = JPAUtils.getResultList(emMicro, "getProducts", Utils.mapOf());
    	List<Products> lstRes;
		try {
			lstRes = AppUtil.wrapCollection(Products.getWrapConstructor(), lst);
		} catch (Exception e) {
			return null;
		}
    	Utils.initCollection(lstRes, options);
    	return lstRes;
    }
    
	@Override
	public List<ProductConfig> findProductConfigActive(Integer productId,Set options) {
		return findProductConfigActive(productId,new Date(),options);
	}

	@Override
	public List<ProductConfig> findProductConfigActive(Integer productId,
			Date date,Set options) {
		List<ProductConfigEntity> lst=findProductConfigActive(productId,date);
		List<ProductConfig> lstConfig;
		try {
			lstConfig = AppUtil.wrapCollection(ProductConfig.getWrapConstructor(),lst);
		} catch (Exception e) {
			return null;
		}
    	Utils.initCollection(lstConfig, options);
    	return lstConfig;
	}

	@Override
	public ProductConfigEntity findProductConfigActiveByName(Integer productId,
			Integer configTypeId, Date date, String name) {
		List<ProductConfigEntity> lst=findListProductConfig(productId,	date, ActiveStatus.ACTIVE, configTypeId,null, name);
		return (ProductConfigEntity) Utils.firstOrNull(lst);
	}

	@Override
	public ProductConfigEntity findProductConfigActiveByName(Integer productId,
			Integer configTypeId, String name) {
		return findProductConfigActiveByName(productId,configTypeId,new Date(),name);
	}

	@Override
	public List<ProductConfigEntity> findProductConfigActiveByType(Integer productId,
			Integer configTypeId, Date date) {
		return JPAUtils.getResultList(emMicro, "findProductConfigByType", Utils.mapOf("productId", productId,
				"isActive", ActiveStatus.ACTIVE,
				"configTypeId", configTypeId,
				"date", date));
	}

	@Override
	public List<ProductConfigEntity> findProductConfigActiveByType(
			Integer productId, Integer configTypeId) {
		return findProductConfigActiveByType(productId,configTypeId,new Date());
	}

	@Override
	public List<ProductRules> findProductRulesActive(Integer productId,
			Date date, Set options) {
		List<ProductRulesEntity> lst=JPAUtils.getResultList(emMicro, "findProductRules", Utils.mapOf("productId", productId,
				"isActive", ActiveStatus.ACTIVE,
				"date", date));
		List<ProductRules> lstRules;
		try {
			lstRules = AppUtil.wrapCollection(ProductRules.getWrapConstructor(),lst);
		} catch (Exception e) {
			return null;
		}
    	Utils.initCollection(lstRules, options);
    	return lstRules;
	}

	@Override
	public List<ProductRules> findProductRulesActive(Integer productId,Set options) {
		return findProductRulesActive(productId,new Date(),options);
	}

	@Override
	public List<ProductRulesEntity> findProductRulesActive(Integer productId) {
		return JPAUtils.getResultList(emMicro, "findProductRules", Utils.mapOf("productId", productId,
				"isActive", ActiveStatus.ACTIVE,
				"date", new Date()));
	}
	
	@Override
	public ProductRulesEntity findProductRulesActiveByScriptTypeAndName(
			Integer productId, Integer configTypeId, Integer scriptTypeId,
			Date date, String name) {
		return (ProductRulesEntity) JPAUtils.getSingleResult(emMicro, "findProductRulesByScriptTypeAndName", Utils.mapOf(
				"productId", productId,
				"isActive", ActiveStatus.ACTIVE,
				"configTypeId", configTypeId,
				"date", date,
				"name", name,
				"scriptTypeId", scriptTypeId));
	}

	@Override
	public List<String> listKbase() {
		List<String> lstRes = JPAUtils.getResultList(emMicro, "listKbase", Utils.mapOf(
				"scriptTypeId", ProductRules.SCRIPT_KBASE
    			));
		return lstRes;
	}	
	
	@Override
	public List<ProductRulesEntity> findAllRulesActiveByKbase(String kbase,Date date) {
		return JPAUtils.getResultList(emMicro, "findAllRulesByKbase", Utils.mapOf(
				"isActive", ActiveStatus.ACTIVE,
				"date", date,
				"kbase", kbase
    			));
	}
	
	@Override
	public ProductConfigEntity saveProductConfig(ProductConfigEntity productConfig) {
		ProductConfigEntity productConfigNew=emMicro.merge(productConfig);
		emMicro.persist(productConfigNew);
		return productConfigNew;
	}

	@Override
	public Products getProductDefault(Set options) {
    	ProductsEntity productEntity=getProductDefault();
		if (productEntity!=null) {
			Products product=new Products(productEntity);
			product.init(options);
			return product;
		}
		return null;
	}

	@Override
	public ProductsEntity getProductDefault() {
    	return (ProductsEntity) JPAUtils.getSingleResult(emMicro, "getProductDefault", Utils.mapOf(
    			"isActive", ActiveStatus.ACTIVE,
    			"isDefault", DefaultStatus.DEFAULT
    			));
	}
	
	@Override
	public ProductMessagesEntity findProductMessageByName(Integer productId,
			String code, String name) {
	    return (ProductMessagesEntity) JPAUtils.getSingleResult(emMicro, "findProductMessageByName", Utils.mapOf(
				"productId", productId,
				"code", code,
				"name", name
    			));
	}

	@Override
	public ProductMessagesEntity findMessageByName( String name) {
	   return (ProductMessagesEntity) JPAUtils.getSingleResult(emMicro, "findMessageByName", Utils.mapOf(
				"configTypeId", ProductKeys.PRODUCT_CREDIT_MESSAGES,
				"name", name
    			));
	}
	
	@Override
	public List<ProductMessagesEntity> findProductMessagesForProcess(Integer productId,	String code) {
	    return JPAUtils.getResultList(emMicro, "findProductMessagesForProcess", Utils.mapOf(
				"productId", productId,
				"code", code
			));
	}
	
	@Override
	public List<ProductMessagesEntity> findProductMessages(Integer productId) {
	    return JPAUtils.getResultList(emMicro, "findProductMessages", Utils.mapOf(
				"productId", productId
			));
	}
	
	@Override
	public List<ProductConfig> findProductConfigByType(Integer productId,
			Integer configTypeId, Set options) {
		List<ProductConfigEntity> lst=findProductConfigActiveByType(productId,configTypeId);
		List<ProductConfig> lstConfig;
		try {
			lstConfig = AppUtil.wrapCollection(ProductConfig.getWrapConstructor(),lst);
		} catch (Exception e) {
			return null;
		}
    	Utils.initCollection(lstConfig, options);
    	return lstConfig;
	}

	@Override
	public ProductConfigEntity getProductConfig(int id) {
		return emMicro.find(ProductConfigEntity.class, id);
	}

	@Override
	public ProductMessagesEntity saveProductMessage(ProductMessagesEntity message) {
		ProductMessagesEntity messageNew=emMicro.merge(message);
		emMicro.persist(messageNew);
		return messageNew;
	}

	@Override
	public ProductRulesEntity saveProductRule(ProductRulesEntity rule) {
		ProductRulesEntity ruleNew=emMicro.merge(rule);
		emMicro.persist(ruleNew);
		return ruleNew;
	}

	@Override
	public List<ProductMessages> findProductMessagesByType(
			Integer productId, Integer configTypeId, Set options) {
		List<ProductMessagesEntity> lst=JPAUtils.getResultList(emMicro, "findProductMessagesByType", Utils.mapOf(
				"productId", productId,
				"configTypeId", configTypeId
			));
	    List<ProductMessages> lstMessages;
	    try {
			lstMessages = AppUtil.wrapCollection(ProductMessages.getWrapConstructor(),lst);
		} catch (Exception e) {
			return null;
		}
    	Utils.initCollection(lstMessages, options);
    	return lstMessages;
	}

	@Override
	public List<ProductRules> findProductRulesActiveByType(Integer productId,
			Integer configTypeId, Date date, Set options) {
		List<ProductRulesEntity> lst=JPAUtils.getResultList(emMicro, "findProductRulesByType", Utils.mapOf(
				"productId", productId,
				"isActive", ActiveStatus.ACTIVE,
				"configTypeId", configTypeId,
				"date", date));
		List<ProductRules> lstRules;
		try {
			lstRules = AppUtil.wrapCollection(ProductRules.getWrapConstructor(),lst);
		} catch (Exception e) {
			return null;
		}
    	Utils.initCollection(lstRules, options);
    	return lstRules;
		
	}

	@Override
	public void saveListProductMessages(List<ProductMessages> messages) {
		if (messages.size()>0){
			for (ProductMessages message:messages){
				try {
				    saveProductMessage(message.getEntity());
				} catch (Exception e){
					logger.severe("Не удалось сохранить сообщение для продукта "+message.getProduct().getName()+
							", код сообщения "+message.getName()+", ошибка "+e);
				}
			}
		}
		
	}

	@Override
	public void saveListProductConfig(List<ProductConfig> configs) {
		if (configs.size()>0){
			for (ProductConfig config:configs){
				try {
					saveProductConfig(config.getEntity());
				} catch (Exception e){
					logger.severe("Не удалось сохранить конфигурацию для продукта "+config.getProduct().getName()+
							", код конфигурации "+config.getName()+", ошибка "+e);
				}
			}
		}
		
	}

	@Override
	public void saveListProductRules(List<ProductRules> rules) {
		if (rules.size()>0){
			for (ProductRules rule:rules){
				try {
					saveProductRule(rule.getEntity());
				} catch (Exception e){
					logger.severe("Не удалось сохранить правило для продукта "+rule.getProduct().getName()+
							", код правила "+rule.getName()+", ошибка "+e);
				}
			}
		}
		
	}

	@Override
	public List<ProductConfigEntity> findProductConfigActive(Integer productId) {
		return findProductConfigActive(productId,new Date());
	}

	@Override
	public List<ProductConfigEntity> findProductConfigActive(Integer productId,
			Date date) {
		return findListProductConfig(productId,	date, ActiveStatus.ACTIVE, null,null, null);
	}

	@Override
	public void deleteProduct(Integer productId) {
		 Query qry = emMicro.createNamedQuery("deleteProduct");
		 qry.setParameter("id", productId);
	     qry.executeUpdate();	
		
	}

	@Override
	public List<ProductConfigEntity> findProductConfigActiveByBPName(
			Integer productId, String code, Date date) {
		return findListProductConfig(productId,	date, ActiveStatus.ACTIVE, null,code, null);
	}

	@Override
	public List<ProductConfigEntity> findProductConfigActiveByBPName(
			Integer productId, String code) {
		return findListProductConfig(productId,	new Date(), ActiveStatus.ACTIVE, null,code, null);
	}

	@Override
	public List<ProductConfigEntity> findListProductConfig(Integer productId,
			Date date, Integer isActive, Integer configTypeId,
			String processName, String name) {
		String hql = "from ru.simplgroupp.persistence.ProductConfigEntity where (1=1) ";
		if (productId!=null){
			hql+=" and (productId.id = :productId) ";
		}
		if (date!=null){
			hql+=" and (:date between databeg and dataend) ";
		}
		if (isActive!=null){
			hql+=" and (isActive=:isActive) ";
		}
		if (configTypeId!=null){
			hql+=" and (configTypeId.codeinteger=:configTypeId) ";
		}
		if (StringUtils.isNotEmpty(processName)){
			hql+=" and (configTypeId.code=:processName) ";
		}
		if (StringUtils.isNotEmpty(name)){
			hql+=" and (name=:name) ";
		}
		Query qry=emMicro.createQuery(hql);
		if (productId!=null){
			qry.setParameter("productId", productId);
		}
		if (date!=null){
			qry.setParameter("date", date);
		}
		if (isActive!=null){
			qry.setParameter("isActive", isActive);
		}
		if (configTypeId!=null){
			qry.setParameter("configTypeId", configTypeId);
		}
		if (StringUtils.isNotEmpty(processName)){
			qry.setParameter("processName", processName);
		}
		if (StringUtils.isNotEmpty(name)){
			qry.setParameter("name", name);
		}
		List<ProductConfigEntity> lst=qry.getResultList();
		return lst;
	}

	@Override
	public void deleteProductConfig(Integer id) {
		 Query qry = emMicro.createNamedQuery("deleteProductConfig");
		 qry.setParameter("id", id);
	     qry.executeUpdate();	
		
	}

	@Override
	public void deleteProductMessage(Integer id) {
		 Query qry = emMicro.createNamedQuery("deleteProductMessage");
		 qry.setParameter("id", id);
	     qry.executeUpdate();	
	}

	@Override
	public void deleteProductRule(Integer id) {
		 Query qry = emMicro.createNamedQuery("deleteProductRule");
		 qry.setParameter("id", id);
	     qry.executeUpdate();	
		
	}

	@Override
	public ProductMessagesEntity getProductMessage(int id) {
		return emMicro.find(ProductMessagesEntity.class, id);
	}

	@Override
	public ProductRulesEntity getProductRule(int id) {
		return emMicro.find(ProductRulesEntity.class, id);
	}

	@Override
	public ProductMessagesEntity getProductMessageFromList(
			List<ProductMessagesEntity> messages, String name) {
		if (messages.size()>0){
			for (ProductMessagesEntity message:messages){
				if (message.getName().equalsIgnoreCase(name)){
					return message;
				}
			}
		}
		return null;
	}

	@Override
	public void makeProductActive(Integer productId) {
		ProductsEntity product=getProduct(productId);
		if (product!=null){
		    product.setIsActive(ActiveStatus.ACTIVE);
	        saveProduct(product);
		}
	}

	@Override
	public void makeProductDefault(Integer productId) {
		ProductsEntity product=getProduct(productId);
		if (product!=null){
			//ищем продукт по умолчанию на текущий момент
			ProductsEntity productDefault=getProductDefault();
			//убираем свойство по умолчанию
			if (productDefault!=null){
				productDefault.setIsDefault(DefaultStatus.NOT_DEFAULT);
				saveProduct(productDefault);
			}
		    product.setIsDefault(DefaultStatus.DEFAULT);
	        saveProduct(product);	
		}
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProductsEntity addProductHeader(String name, String description,
			String code,Integer type) {
		ProductsEntity product=new ProductsEntity();
		product.setIsActive(ActiveStatus.DRAFT);
		product.setCode(code);
		product.setDescription(description);
		product.setName(name);
		product.setPaymentTypeId(refBooks.getReferenceEntity(type));
		product.setIsDefault(DefaultStatus.NOT_DEFAULT);
		emMicro.persist(product);
		return product;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProductConfigEntity newProductConfig(ProductsEntity product, Integer configTypeId,
			String name, String dataType, Date databeg, Date dataend,
			Integer isActive, String description, String value) {
		ProductConfigEntity configNew=new ProductConfigEntity();
		configNew.setProductId(product);
		configNew.setConfigTypeId(refBooks.findByCodeIntegerEntity(RefHeader.PRODUCT_CONFIG_TYPE,configTypeId));
		configNew.setName(name);
		configNew.setDataType(dataType);
		configNew.setDatabeg(databeg);
		configNew.setDataend(dataend);
		configNew.setIsActive(isActive);
		configNew.setDescription(description);
		configNew.setDataValue(value);
		emMicro.persist(configNew);
		return configNew;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProductRulesEntity newProductRule(ProductsEntity product, Integer configTypeId,
			String name, String kbase, Date databeg, Date dataend,
			Integer isActive, String description, String ruleBody,Integer scriptTypeId) {
		ProductRulesEntity productRule=new ProductRulesEntity();
		productRule.setProductId(product);
		productRule.setConfigTypeId(refBooks.findByCodeIntegerEntity(RefHeader.PRODUCT_CONFIG_TYPE,configTypeId));
		productRule.setName(name);
		productRule.setKbase(kbase);
		productRule.setDatabeg(databeg);
		productRule.setDataend(dataend);
		productRule.setIsActive(isActive);
		productRule.setDescription(description);
		productRule.setRuleBody(ruleBody);
		productRule.setScriptTypeId(scriptTypeId);
		emMicro.persist(productRule);
		return productRule;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProductMessagesEntity newProductMessage(ProductsEntity product, Integer configTypeId,
			String name, String subject, String body, String description, String signalRef) {
		ProductMessagesEntity message=new ProductMessagesEntity();
		message.setProductId(product);
		message.setConfigTypeId(refBooks.findByCodeIntegerEntity(RefHeader.PRODUCT_CONFIG_TYPE,configTypeId));
		message.setName(name);
		message.setBody(body);
		message.setSubject(subject);
		message.setDescription(description);
		message.setSignalRef(signalRef);
		emMicro.persist(message);
		return message;
	} 	
	
	@Override
	public ProductMessagesEntity getMessageForBusinessAction(Integer productId,	String signalRef) {
		String hql="from ru.simplgroupp.persistence.ProductMessagesEntity where (configTypeId.codeinteger=:configTypeId) and (signalRef=:signalRef) ";
		if (productId!=null){
			hql+=" and productId.id=:productId";
		}
		Query qry=emMicro.createQuery(hql);
		qry.setParameter("configTypeId", ProductKeys.PRODUCT_CREDIT_MESSAGES);
		qry.setParameter("signalRef", signalRef);
		if (productId!=null){
			qry.setParameter("productId", productId);
		}
		List<ProductMessagesEntity> lst=qry.getResultList();
		return (ProductMessagesEntity) Utils.firstOrNull(lst);
	}
}
