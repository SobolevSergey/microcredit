package ru.simplgroupp.ejb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.StringUtils;

import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.interfaces.ModelBeanLocal;
import ru.simplgroupp.interfaces.ProductBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.AIModelEntity;
import ru.simplgroupp.persistence.ProductConfigEntity;
import ru.simplgroupp.persistence.ProductMessagesEntity;
import ru.simplgroupp.persistence.ProductRulesEntity;
import ru.simplgroupp.persistence.ProductsEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.ProductConfig;
import ru.simplgroupp.transfer.ProductMessages;
import ru.simplgroupp.transfer.ProductRules;
import ru.simplgroupp.transfer.Products;
import ru.simplgroupp.transfer.RefHeader;
import ru.simplgroupp.util.ProductKeys;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ProductBeanLocal.class)
public class ProductBean implements ProductBeanLocal{
	
	@Inject Logger logger;
	
	@EJB
	ProductDAO productDAO;
	
	@EJB
	ReferenceBooksLocal refBooks;
	
	@EJB 
	ModelBeanLocal modelBean;
	
	/**
	 * делаем map для конфигурации продукта
	 * @param lstConfig - конфигурация	
	 * @param productId - id продукта
	 * @return
	 */
	private Map<String,Object> fireProductConfig(List<ProductConfigEntity> lstConfig,Integer productId){
		
		if (lstConfig.size()>0){
			HashMap<String, Object> res = new HashMap<String, Object>(10);
			for (ProductConfigEntity productConfig:lstConfig){
				//logger.info("Datatype "+productConfig.getDataType()+", datavalue "+productConfig.getDataValue());
				Object value=Convertor.toValue(productConfig.getDataType(), productConfig.getDataValue());
				//logger.info("Name "+productConfig.getName()+", value "+value);
				res.put(productConfig.getName(), value);
			}
			return res;
		}
		return null;
	}
	
	@Override
	public Map<String,Object> getNewRequestProductConfig(Integer productId) {
		List<ProductConfigEntity> lstConfig=productDAO.findProductConfigActiveByType(productId, ProductKeys.PRODUCT_CREDITREQUEST);
		return fireProductConfig(lstConfig,productId);
	}

	@Override
	public Map<String, Object> getNewRequestDefaultProductConfig() {
		return getNewRequestProductConfig(productDAO.getProductDefault().getId());
	}

	@Override
	public String getFuctionJS(Integer productId, Integer configTypeId,
			String name, Date date) {
		ProductRulesEntity productRule=productDAO.findProductRulesActiveByScriptTypeAndName(productId, configTypeId, 
				ProductRules.SCRIPT_JS, date, name);
		if (productRule!=null){
			return productRule.getRuleBody();
		}
		return null;
	}

	@Override
	public String getFunctionStakeNewRequestUnknown(Integer productId) {
		return getFuctionJS(productId,ProductKeys.PRODUCT_CREDITREQUEST,
				ProductKeys.CREDIT_STAKE_INITIAL_UNKNOWN,new Date());
	}

	@Override
	public Map<String, Object> getRefinanceProductConfig(Integer productId) {
		List<ProductConfigEntity> lstConfig=productDAO.findProductConfigActiveByType(productId, ProductKeys.PRODUCT_CREDIT_REFINANCE);
		return fireProductConfig(lstConfig,productId);
	}
	
	@Override
	public Map<String, Object> getProlongProductConfig(Integer productId) {
		List<ProductConfigEntity> lstConfig=productDAO.findProductConfigActiveByType(productId, ProductKeys.PRODUCT_CREDIT_PROLONG);
		return fireProductConfig(lstConfig,productId);
	}

	@Override
	public Map<String, Object> getOverdueProductConfig(Integer productId) {
		List<ProductConfigEntity> lstConfig=productDAO.findProductConfigActiveByType(productId, ProductKeys.PRODUCT_CREDIT_OVERDUE);
		return fireProductConfig(lstConfig,productId);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeProductConfig(Integer configId,Date databeg,String value) {
		ProductConfigEntity configOld=productDAO.getProductConfig(configId);
		//если дата изменения после даты начала действия конфигурации
		if (databeg!=null&&databeg.after(configOld.getDatabeg())){
		    Date dataend=configOld.getDataend();
		    configOld.setDataend(DateUtils.addDays(databeg, -1));
		    configOld.setIsActive(ActiveStatus.ARCHIVE);
		    configOld=productDAO.saveProductConfig(configOld);
		    try {
			    newProductConfig(configOld.getProductId(),configOld.getConfigTypeId().getCodeinteger(),
					configOld.getName(),configOld.getDataType(),databeg,dataend,ActiveStatus.ACTIVE,
					configOld.getDescription(),value);
		    } catch (Exception e){
			    logger.severe("Не удалось добавить новую конфигурацию для продукта "+configOld.getProductId().getId());
		    }
		}
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void changeProductRule(Integer ruleId,Date databeg,String value) {
		ProductRulesEntity ruleOld=productDAO.getProductRule(ruleId);
		//если дата изменения после даты начала действия правила
		if (databeg!=null&&databeg.after(ruleOld.getDatabeg())){
		    Date dataend=ruleOld.getDataend();
		    ruleOld.setDataend(DateUtils.addDays(databeg, -1));
		    ruleOld.setIsActive(ActiveStatus.ARCHIVE);
		    ruleOld=productDAO.saveProductRule(ruleOld);
		    try {
			    newProductRule(ruleOld.getProductId(),ruleOld.getConfigTypeId().getCodeinteger(),
					ruleOld.getName(),ruleOld.getKbase(),databeg,dataend,ActiveStatus.ACTIVE,
					ruleOld.getDescription(),value,ruleOld.getScriptTypeId());
		    } catch (Exception e){
			    logger.severe("Не удалось добавить новое правило для продукта "+ruleOld.getProductId().getId());
		    }
		}
	}
	
	@Override
	public Map<String, Object> getAllProductConfig(Integer productId) {
		List<ProductConfigEntity> lstConfig=productDAO.findProductConfigActive(productId);
		return fireProductConfig(lstConfig,productId);
	}

	@Override
	public Map<String, Object> getProductConfigForBP(Integer productId,
			String bpName) {
		List<ProductConfigEntity> lstConfig=productDAO.findProductConfigActiveByBPName(productId, bpName);
		return fireProductConfig(lstConfig,productId);
	}
	
	@Override
	public Map<String, Object> getCollectorProductConfig(Integer productId) {
		List<ProductConfigEntity> lstConfig=productDAO.findProductConfigActiveByType(productId, ProductKeys.PRODUCT_COLLECTOR);
		return fireProductConfig(lstConfig,productId);
	}

	@Override
	public Map<String, Object> getCreditReturnPayment(Integer productID) {
		List<ProductConfigEntity> lstConfig = productDAO.findProductConfigActiveByType(productID, ProductKeys.PRODUCT_CREDIT_RETURN_PAYMENT);
		return fireProductConfig(lstConfig, productID);
	}

	@Override
	public Map<String, Object> getCreditPaymentProductConfig(Integer productID) {
		List<ProductConfigEntity> lstConfig = productDAO.findProductConfigActiveByType(productID, ProductKeys.PRODUCT_CREDIT_PAYMENT);
		return fireProductConfig(lstConfig, productID);
	}

	@Override
	public Map<String, Object> getConsiderProductConfig(Integer productId) {
		List<ProductConfigEntity> lstConfig=productDAO.findProductConfigActiveByType(productId, ProductKeys.PRODUCT_CREDIT_DECISION);
		return fireProductConfig(lstConfig,productId);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProductConfigEntity newProductConfig(ProductsEntity product, Integer configTypeId,
			String name, String dataType, Date databeg, Date dataend,
			Integer isActive, String description, String value) {
		return productDAO.newProductConfig(product, configTypeId, name, 
				dataType, databeg, dataend, isActive, description, value);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProductsEntity addProductHeader(String name, String description,
			String code,Integer type) {
		return productDAO.addProductHeader(name, description, code, type);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProductConfig addProductConfig(Integer productId,Integer configTypeId) {
		ProductConfigEntity config=new ProductConfigEntity();
		config.setProductId(productDAO.getProduct(productId));
		config.setConfigTypeId(refBooks.findByCodeIntegerEntity(RefHeader.PRODUCT_CONFIG_TYPE, configTypeId));
		config.setDatabeg(new Date());
		config.setDataend(DateUtils.addYears(new Date(), 10));
		config.setIsActive(ActiveStatus.ACTIVE);
		config=productDAO.saveProductConfig(config);
		return new ProductConfig(config);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProductMessages addProductMessage(Integer productId,	Integer configTypeId) {
		ProductMessagesEntity message=new ProductMessagesEntity();
		message.setProductId(productDAO.getProduct(productId));
		if (configTypeId!=null){
		    message.setConfigTypeId(refBooks.findByCodeIntegerEntity(RefHeader.PRODUCT_CONFIG_TYPE, configTypeId));
		}
		message=productDAO.saveProductMessage(message);
		return new ProductMessages(message);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProductRules addProductRule(Integer productId, Integer configTypeId) {
		ProductRulesEntity rule=new ProductRulesEntity();
		rule.setProductId(productDAO.getProduct(productId));
		rule.setConfigTypeId(refBooks.findByCodeIntegerEntity(RefHeader.PRODUCT_CONFIG_TYPE, configTypeId));
		rule.setDatabeg(new Date());
		rule.setDataend(DateUtils.addYears(new Date(), 10));
		rule.setIsActive(ActiveStatus.ACTIVE);
		rule=productDAO.saveProductRule(rule);
		return new ProductRules(rule);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProductRulesEntity newProductRule(ProductsEntity product, Integer configTypeId,
			String name, String kbase, Date databeg, Date dataend,
			Integer isActive, String description, String ruleBody,Integer scriptTypeId) {
		return productDAO.newProductRule(product, configTypeId, name,
				kbase, databeg, dataend, isActive, description, ruleBody, scriptTypeId);
	}

	@Override
	public ProductMessagesEntity getMessageForBusinessAction(Integer productId,	String signalRef) {
		return productDAO.getMessageForBusinessAction(productId, signalRef);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Products copyProduct(Integer productId) {
		ProductsEntity productEntity=productDAO.getProduct(productId);
		if (productEntity!=null){
			//добавили продукт
			ProductsEntity productNew=addProductHeader(productEntity.getName(),productEntity.getDescription(),
					productEntity.getCode(),productEntity.getPaymentTypeId().getId());
			//конфигурация
			List<ProductConfigEntity> config=productDAO.findProductConfigActive(productId);
			if (config.size()>0){
				for(ProductConfigEntity cfg:config){
					ProductConfigEntity cfgNew=newProductConfig(productNew,cfg.getConfigTypeId().getCodeinteger(),
			           cfg.getName(), cfg.getDataType(), cfg.getDatabeg(), cfg.getDataend(),
			           cfg.getIsActive(), cfg.getDescription(), cfg.getDataValue());
					if (cfgNew!=null){
					    productNew.getProductConfig().add(cfgNew);
					}
				}//end for
			}//end size>0
			//правила
			List<ProductRulesEntity> rules=productDAO.findProductRulesActive(productId);
			if(rules.size()>0){
				for(ProductRulesEntity rule:rules){
					//копируем пока только без компилируемых правил
					// TODO поменять после того, как изменится RulesBean
					if (StringUtils.isEmpty(rule.getKbase())){
					    ProductRulesEntity ruleNew=newProductRule(productNew, rule.getConfigTypeId().getCodeinteger(),
			                rule.getName(), rule.getKbase(), rule.getDatabeg(), rule.getDataend(),
			                rule.getIsActive(), rule.getDescription(), rule.getRuleBody(),rule.getScriptTypeId());
					    if (ruleNew!=null){
						    productNew.getProductRules().add(ruleNew);
					    }
					}
				}//end for
			}//end size>0
			//сообщения
			List<ProductMessagesEntity> messages=productDAO.findProductMessages(productId);
			if (messages.size()>0){
				for(ProductMessagesEntity message:messages){
					ProductMessagesEntity msgNew=newProductMessage(productNew,message.getConfigTypeId().getCodeinteger(),
					   message.getName(), message.getSubject(), message.getBody(),message.getDescription(),message.getSignalRef());
					if (msgNew!=null){
						productNew.getProductMessages().add(msgNew);
					}
				}//end for
			}//end size>0
			//стратегия
			AIModelEntity model=modelBean.getActiveProductModelEntity(null,productId);
			if (model!=null){
				AIModelEntity newModel=modelBean.createDraftFrom(model.getId());
				if (newModel!=null){
					productNew.getModels().add(newModel);
				}
			}// end model не null
			logger.info("Скопировали продукт с кодом "+productEntity.getCode());
			return new Products(productNew);
		}
		return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProductMessagesEntity newProductMessage(ProductsEntity product, Integer configTypeId,
			String name, String subject, String body, String description, String signalRef) {
		return productDAO.newProductMessage(product, configTypeId,
				name, subject, body, description, signalRef);
	} 	
}
