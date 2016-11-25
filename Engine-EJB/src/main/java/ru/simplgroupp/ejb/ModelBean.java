package ru.simplgroupp.ejb;

import java.io.StringWriter;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import ru.simplgroupp.persistence.AIModelEntity;
import ru.simplgroupp.persistence.AIModelParamEntity;
import ru.simplgroupp.dao.interfaces.AIModelDAO;
import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.dao.interfaces.SqlDAO;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ModelException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.exception.WorkflowException;
import ru.simplgroupp.interfaces.ModelBeanLocal;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.interfaces.WorkflowBeanLocal;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.ExecutionProgress;
import ru.simplgroupp.toolkit.common.NameValuePair;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.AIModel;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.RefuseReason;
import ru.simplgroupp.util.DecisionKeys;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.MimeTypeKeys;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.util.ModelUtils;
import ru.simplgroupp.workflow.DecisionState;
import ru.simplgroupp.workflow.ProcessKeys;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(ModelBeanLocal.class)
public class ModelBean implements ModelBeanLocal {
	
    @PersistenceContext(unitName = "MicroPU")
    protected EntityManager emMicro;	
    
	@EJB
	WorkflowBeanLocal wfBean;    
	
	@EJB
	ReferenceBooksLocal refBooks;

	@EJB
	ProductDAO productDAO;
	
	@EJB
	AIModelDAO modelDAO;
	
	@EJB
	SqlDAO sqlDAO;
	
	@Inject Logger logger;
	
    @Override
	public AIModelEntity getModel(int id) {
		return modelDAO.getModel(id);
	}
    
    @Override
    public AIModel getModel(int id, Set options) {
    	return modelDAO.getModel(id, options);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)    
    public List<AIModel> findModels(String target, Boolean active, String version, 
			String bodyTpl,Integer productId, Integer wayId) {
    	Integer[] activeStatuses = null;
    	if (active != null) {
    		if (active.booleanValue()) {
    			activeStatuses = new Integer[] {ActiveStatus.ACTIVE};
    		} else {
    			activeStatuses = new Integer[] {ActiveStatus.ARCHIVE, ActiveStatus.DRAFT};
    		}
    	}
    	List<AIModelEntity> lst = listModels(target, activeStatuses, version, bodyTpl, productId, wayId);
    	return wrapModelList(lst);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)     
    public List<AIModel> findModelsWithResults() {
    	List<Integer> lstR = modelDAO.listModelIdsWithResults();
    	List<AIModelEntity> lst = listModels(null, null, null, null, null, null);
    	List<AIModelEntity> lstRes = new ArrayList<AIModelEntity>(lst.size());
    	for (AIModelEntity ent: lst) {
    		if (lstR.contains(ent.getId())) {
    			lstRes.add(ent);
    		}
    	}
    	return wrapModelList(lstRes);
    }
	
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<AIModelEntity> listModels(String target, Integer[] activeStatuses, String version, 
			String bodyTpl,Integer productId, Integer wayId) {
		String sql = "from ru.simplgroupp.persistence.AIModelEntity where (1=1)";
		if (target != null) {
			sql+= " and (target = :target)";
		}
		if (activeStatuses != null && activeStatuses.length > 0) {
			sql+= " and (isActive in (:activeStatuses))";
		}
		if (version != null) {
			sql+= " and (version = :version)";
		}
		if (! StringUtils.isBlank(bodyTpl)) {
			sql+= " and (upper(body) like :bodyTpl)";
		}
		if (productId!=null){
			sql+=" and productId.id=:productId ";
		}
		if (wayId != null) {
			sql+=" and wayId.id=:wayId ";
		}
		sql+=" order by dateCreate desc ";
		Query qry = emMicro.createQuery(sql);
		if (target != null) {
			qry.setParameter("target", target);
		}
		if (activeStatuses != null && activeStatuses.length > 0) {
			ArrayList<Integer> lst = new ArrayList<Integer>(activeStatuses.length);
			for (Integer st: activeStatuses) {
				lst.add(st);
			}
			qry.setParameter("activeStatuses", lst);
		}
		if (version != null) {
			qry.setParameter("version", version);
		}
		if (! StringUtils.isBlank(bodyTpl)) {
			qry.setParameter("bodyTpl", "%" + bodyTpl.trim().toUpperCase() + "%");
		}
		if (productId!=null){
			qry.setParameter("productId", productId);
		}
		if (wayId!=null){
			qry.setParameter("wayId", wayId);
		}		
		List<AIModelEntity> lst = qry.getResultList();
		return lst;
	}
    
    @Override
    public AIModel getActiveModel(String target) {
    	AIModelEntity model=findActiveModelFor(target);
    	if (model!=null){
    	    return new AIModel(model);
    	}
    	return null;
    }
    
    @Override
    public AIModel getActiveProductModel(String target,Integer productId) {
    	AIModelEntity model=getActiveProductModelEntity(target,productId);
    	if (model!=null){
    	    return new AIModel(model);
    	}
    	return null;
    }
    
    @Override
    public AIModelEntity getActiveProductModelEntity(String target,Integer productId) {
    	AIModelEntity model=findActiveProductModelFor(target,productId);
      	return model;
    }
    
    private List<AIModel> wrapModelList(List<AIModelEntity> lst) {
    	List<AIModel> lstRes = new ArrayList<AIModel>(lst.size());
    	for (AIModelEntity ent: lst) {
    		AIModel model = new AIModel(ent);
    		lstRes.add(model);
    	}
    	return lstRes;    	
    }
    
    @Override
    public List<String> getScriptTypesSupported() {
    	return Utils.listOf(MimeTypeKeys.JAVASCRIPT);
    }
    
    /**
     * Сохранить модель
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)    
    public AIModel saveModel(AIModel source) {
    	int modelId = source.getId();
    	AIModelEntity ent = source.getEntity();
    	if (source.getProduct()!=null){
    		ent.setProductId(productDAO.getProduct(source.getProduct().getId()));
    	} else {
    		ent.setProductId(null);
    	}
    	if (source.getWay() != null) {
    		ent.setWayId(refBooks.getReferenceEntity(source.getWay().getId()));
    	} else {
    		ent.setWayId(null);
    	}
    	modelDAO.saveModelEntity(ent);
    	return getModel(modelId, Utils.setOf());
    }
    
    /**
     * ищем активную модель по названию
     * @param target - название
     * @return
     */
    public AIModelEntity findActiveModelFor(String target) {
    	List<AIModelEntity> lst = listModels(target, new Integer[] {ActiveStatus.ACTIVE}, null, null,null, null);
    	return (AIModelEntity) Utils.firstOrNull(lst);
    }
    
    /**
     * ищем активную модель по названию и продукту
     * @param target - название
     * @param productId - id продукта
     * @return
     */
    public AIModelEntity findActiveProductModelFor(String target,Integer productId) {
    	List<AIModelEntity> lst = listModels(target, new Integer[] {ActiveStatus.ACTIVE}, null, null,productId, null);
    	return (AIModelEntity) Utils.firstOrNull(lst);
    }
    
    @Override
    public AIModel findActiveModel(String modelKey, Integer productId, Integer wayId) {
    	List<AIModel> models = findModels(modelKey, true, null, null, productId, wayId);
    	AIModel model = null;
        if (models.size() == 0){
        	models = findModels(modelKey, true, null, null, productId, null);
        }
        if (models.size() == 0){
        	models = findModels(modelKey, true, null, null, null, null);
        }
        if (models.size() > 0) {
        	model = models.get(0);
        }
        if (model == null) {
        	return null;
        } else {
        	return model;
        }    	
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)    
    public void makeActive(int modelId) {
    	AIModelEntity cur = modelDAO.getModel(modelId);
    	// ищем предыдущую активную модель, сначала по продукту
    	AIModelEntity prevActive = findActiveProductModelFor(cur.getTarget(),cur.getProductId().getId());
    	//если по продукту нет модели, ищем просто по названию
    	if (prevActive==null){
    		prevActive = findActiveModelFor(cur.getTarget());
    	}
    	//если модель нашлась, делаем ее архивной
    	if (prevActive != null) {
    		prevActive.setIsActive(ActiveStatus.ARCHIVE);
    		emMicro.persist(prevActive);
    	}
    	cur.setIsActive(ActiveStatus.ACTIVE);
    	emMicro.persist(cur);
    }
    
    /**
     * Удаляет все версии заданной модели
     * @param modelId
     * @throws ModelException
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)       
    public void removeActiveModel(int modelId) throws ModelException {
    	AIModel ent = getModel(modelId, Utils.setOf());
    	if (ent.isBuiltIn()) {
    		throw new ModelException("Модель является необходимой для работы системы и не может быть удалена.");
    	}
    	List<AIModelEntity> lst = this.listModels(ent.getTarget(), null, null, null,ent.getProduct().getId(), ent.getWay().getId());
    	for (AIModelEntity md: lst) {
    		removeModelEntity(md);
    	}
    }
    
    protected void removeModelEntity(AIModelEntity md) {
		sqlDAO.dropView("v_model_" + md.getId());
		String sql="delete from aimodel where id=:id";
		Query qry=emMicro.createNativeQuery(sql);
		qry.setParameter("id",md.getId());
		qry.executeUpdate();
	}
    
    /**
     * Удаляет указанную версию модели
     * @param modelId
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)     
    public void removeModel(int modelId) {
    	AIModelEntity ent = getModel(modelId);
    	if (ent == null) {
    		return;
    	}
    	removeModelEntity(ent);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public AIModelEntity createDraftFrom(int sourceModelId) {
    	AIModelEntity entSource = modelDAO.getModel(sourceModelId);
    	return createDraftFrom(entSource);
    }
    
    /**
     * создаем черновик из имеющейся модели
     * @param entSource - имеющаяся модель
     * @return
     */
    public AIModelEntity createDraftFrom(AIModelEntity entSource) {
    	
    	AIModelEntity entTarget = new AIModelEntity();
    	entTarget.setTarget(entSource.getTarget());
    	entTarget.setDateCreate(new Date());
    	entTarget.setIsActive(ActiveStatus.DRAFT);
    	entTarget.setMimeType(entSource.getMimeType());
    	entTarget.setBody(entSource.getBody());
    	entTarget.setProductId(entSource.getProductId());
    	entTarget.setWayId(entSource.getWayId());
    	entTarget.setDateChange(new Date());
    	entTarget = modelDAO.saveModelEntity(entTarget);
       	return entTarget;
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)    
    public AIModelEntity createModel(String name,Integer productId, Integer wayId) throws ModelException {
    	List<AIModelEntity> lstEx = listModels(name, new Integer[] {ActiveStatus.ACTIVE}, null, null,null, null);
    	if (lstEx.size() > 0) {
    		throw new ModelException("Уже есть модель с таким именем");
    	}
    	AIModelEntity ent = new AIModelEntity();
    	ent.setTarget(name);
    	ent.setMimeType(MimeTypeKeys.JAVASCRIPT);
    	ent.setDateCreate(new Date());
    	ent.setIsActive(ActiveStatus.ACTIVE);
    	ent.setVersion("0.1");
    	if (productId!=null){
    		ent.setProductId(productDAO.getProduct(productId));
    	}
    	if (wayId != null) {
    		ent.setWayId(refBooks.getReferenceEntity(wayId));
    	}
    	emMicro.persist(ent);
    	return ent;
    }
    
    @Override
    public List<AIModel> getModelVersions(String target, boolean bIncludeActive) {
    	Integer[] st = null;
    	if (! bIncludeActive) {
    		st = new Integer[] {ActiveStatus.ARCHIVE, ActiveStatus.DRAFT};
    	}
    	List<AIModelEntity> lst = listModels(target, st, null, null,null, null);
    	return wrapModelList(lst);
    }
    
    @Override
    public List<AIModel> getActiveModels() {
    	List<AIModelEntity> lst = listModels(null, new Integer[] {ActiveStatus.ACTIVE}, null, null,null, null);
    	return wrapModelList(lst);
    }
    
    @Override
    public List<AIModel> getActiveProductModels(Integer productId) {
    	List<AIModelEntity> lst = listModels(null, new Integer[] {ActiveStatus.ACTIVE}, null, null,productId, null);
    	return wrapModelList(lst);
    }
    
    @Override
	@Asynchronous
	public Future<Map<String, Object>> executeModelsAsync(Iterator<CreditRequest> source, int prmInTarget,  ActionContext actionContext, 
			List<String> target, ExecutionProgress progress, int modelId) throws ActionException {
    	HashMap<String, Object> mpIndicator = new HashMap<String, Object>(2);
    	try {
			internalExecuteModels(source, prmInTarget, actionContext, target, progress, mpIndicator, modelId);
		} catch (JAXBException e) {
			logger.severe("Произошла ошибка JAXBException при выполнении ModelASync "+e.getMessage());
			throw new ActionException(e.getMessage(), e);
		} catch (DocumentException e) {
			logger.severe("Произошла ошибка DocumentException при выполнении ModelASync "+e.getMessage());
			throw new ActionException(e.getMessage(), e);
		}
    	return new AsyncResult<Map<String, Object>>(mpIndicator);
	}
    
    @Override
	public void executeModelsSync(Iterator<CreditRequest> source, int prmInTarget, ActionContext actionContext, 
			List<String> target, ExecutionProgress progress, int modelId) throws ActionException {
    	HashMap<String, Object> mpIndicator = new HashMap<String, Object>(2);
    	try {
			internalExecuteModels(source, prmInTarget, actionContext, target, progress, mpIndicator, modelId);
		} catch (JAXBException e) {
			logger.severe("Произошла ошибка JAXBException при выполнении ModelSync "+e.getMessage());
			throw new ActionException(e.getMessage(), e);
		} catch (DocumentException e) {
			logger.severe("Произошла ошибка DocumentException при выполнении ModelSync "+e.getMessage());
			throw new ActionException(e.getMessage(), e);
		}
	}    
    
	private void internalExecuteModels(Iterator<CreditRequest> source, int prmInTarget,  ActionContext actionContext, List<String> target, ExecutionProgress progress, Map<String, Object> mpIndicator, int modelId) throws JAXBException, DocumentException {
		// документы на выходе
		Document xdoc = null;
		Element elmRoot = null;
		if (prmInTarget == 2) {
			xdoc = DocumentHelper.createDocument();
			elmRoot = xdoc.addElement("debug-results");
		}  
		
		JAXBContext jaxbContext = JAXBContext.newInstance("ru.simplgroupp.transfer");
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();	
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		
		try {
			int currentIdx = 0;
			while (source.hasNext()) {
				if (progress.finishRequest() ) {
					break;
				}
				
				currentIdx++;
						
				CreditRequest ccRequest = source.next();
				if (ccRequest == null) {
					mpIndicator.put("currentIdx", currentIdx);
					mpIndicator.put("ccRequest", ccRequest);
					progress.progress(mpIndicator);
					continue;
				}
			
				if (prmInTarget == 3) {
					xdoc = DocumentHelper.createDocument();
					elmRoot = xdoc.addElement(ModelKeys.XML_DEBUG_ROOT_ELEMENT);
				}
				Map<String, Object> varsResult = new HashMap<String, Object>(5);
				DecisionState ds = null;
				try {
					wfBean.runDebugModelCR(ccRequest, actionContext, varsResult, modelId);
					ds = (DecisionState) varsResult.get(ProcessKeys.VAR_DECISION_STATE);
				} catch (WorkflowException e) {
					ds = new DecisionState();
					ds.finish(false, ErrorKeys.RUNTIME_ERROR);
					ds.addError(String.valueOf(e.getCode()), e.getMessage());
					logger.severe("Произошла ошибка "+e.getMessage());
					logger.log(Level.SEVERE, "Произошла ошибка ", e);
				}			
				
				Element elmRes = elmRoot.addElement(ModelKeys.XML_DEBUG_RESULT_ELEMENT);
	
				// пишем исходные данные
				
				StringWriter wrt = new StringWriter();		
				jaxbMarshaller.marshal(ccRequest, wrt);
				String sxml1 = wrt.toString();
				
				Document cdoc = DocumentHelper.parseText(sxml1);
				elmRes.add( cdoc.getRootElement().detach());
				
				if (ds.isCompletedOK()) {
					elmRes.addAttribute("status", "ok");		
					
					Element elmR = elmRes.addElement("result");
					elmR.addElement("code").setText(String.valueOf( ds.getResultCode() ));
					
					String resultCodeName = null;
					String resultCodeDescription = null;
					String refuseCode = null;
					String refuseDescription = null;
					if (ds.getResultCode() == DecisionKeys.RESULT_ACCEPT) {
						resultCodeName = "RESULT_ACCEPT";
						resultCodeDescription = "Заявка утверждена";
					} else if (ds.getResultCode() == DecisionKeys.RESULT_MANUAL) {
						resultCodeName = "RESULT_MANUAL";
						resultCodeDescription = "На ручное принятие решения";
					} else if (ds.getResultCode() == DecisionKeys.RESULT_UNDEFINED) {
						resultCodeName = "RESULT_UNDEFINED";
						resultCodeDescription = "Решение не принято";
					} else if (ds.getResultCode() == DecisionKeys.RESULT_ACCEPT_AUTO) {
						resultCodeName = "RESULT_ACCEPT_AUTO";
						resultCodeDescription = "Заявка утверждена автоматически";
					} else if (ds.getResultCode() > 0) {
						resultCodeName = "RESULT_DECLINED";
						resultCodeDescription = "Заявка отказана";
						RefuseReason refr = refBooks.getRefuseReason(ds.getResultCode());
						if (refr != null) {
							refuseCode = refr.getConstantName();
							refuseDescription = refr.getName();
						}
					} 
					if (resultCodeName != null) {
						elmR.addElement("code-name").setText(resultCodeName);
					}
					if (resultCodeDescription != null) {
						elmR.addElement("code-description").setText(resultCodeDescription);
					}	
					if (refuseCode != null) {
						elmR.addElement("refuse-code-name").setText(refuseCode);
					}		
					if (refuseDescription != null) {
						elmR.addElement("refuse-code-description").setText(refuseDescription);
					}					
				} else if (ds.isCompletedErrors()) {
					elmRes.addAttribute("status", "errors");
					Element elmErrors = elmRes.addElement("errors");
					for (NameValuePair err: ds.getErrors()) {
						elmErrors.addElement("error").addAttribute("code", err.getValue().toString()).setText(err.getName());
					}
				}
				
				// промежуточные переменные сортируем по имени
				Map<String, Object> mpVars = (Map<String, Object>) varsResult.get(ProcessKeys.VAR_MODEL_PARAMS);
				Element elmVars = elmRes.addElement("variables");
				TreeSet<String> trs = new TreeSet<String>(mpVars.keySet());
				for (String varName: trs) {
					Element elmVar = elmVars.addElement("variable").addAttribute("name", varName);
					Object avalue = ds.getVars().get(varName);
					if (avalue != null) {
						elmVar.setText(avalue.toString());
					}
				}
				
				if (prmInTarget == 3) {
					String sxml = xdoc.asXML();
					target.add(sxml);
				}			
	
				mpIndicator.put("currentIdx", currentIdx);
				mpIndicator.put("ccRequest", ccRequest);
				mpIndicator.put("decisionState", ds);
				progress.progress(mpIndicator);			
			}
			if (prmInTarget == 2) {
				String sxml = xdoc.asXML();
				target.add(sxml);
			}
		} finally {
			progress.finished();
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)    	
	public AIModel deleteModelParam(int modelId, String name) throws ActionException {
		modelDAO.deleteModelParam(modelId, name);
		return getModel(modelId, Utils.setOf());
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)    	
	public AIModel saveModelParam(AIModelParamEntity source) throws ActionException {
		
		AIModelParamEntity prm = modelDAO.findModelParam(source.getAiModel().getId(), source.getName(), source.getCustomKey());
		ModelUtils.copyParam(source, prm);
		modelDAO.saveModelParam(prm);
		
		return getModel(source.getAiModel().getId(), Utils.setOf());
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)  	
	public void loadModelParams(int modelId, String customKey, Map<String, Object> dest) {
		List<AIModelParamEntity> lst = modelDAO.listModelParamsCK(modelId, customKey);
		ModelUtils.paramsToMap(lst, dest);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)  	
	public List<AIModelParamEntity> openModelSession(int modelId, String customKey) {
		modelDAO.copyModelParams(modelId, (String) null, modelId, customKey);
		List<AIModelParamEntity> lst = modelDAO.listModelParamsCK(modelId, customKey);
		return lst;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED) 	
	public List<AIModelParamEntity> getModelParams(int modelId, String customKey) {
		List<AIModelParamEntity> lst = modelDAO.listModelParamsCK(modelId, customKey);
		return lst;		
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)    	
	public AIModel addModelParam(int modelId, AIModelParamEntity source) throws ActionException {
		// проверить на уникальное имя
		if (modelDAO.findModelParam(modelId, source.getName(), source.getCustomKey()) != null) {
			throw new ActionException("Параметр с таким именем уже существует", Type.BUSINESS, ResultType.FATAL);
		}
		AIModelEntity model = modelDAO.getModel(modelId);
		AIModelParamEntity prm = new AIModelParamEntity();
		prm.setAiModel(model);
		ModelUtils.copyParam(source, prm);
		modelDAO.saveModelParam(prm);
		return getModel(modelId, Utils.setOf());
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public void checkModelParam(int modelId, String customKey, String paramName, String paramType) {
		// проверить на уникальное имя
		if (modelDAO.findModelParam(modelId, paramName, customKey) != null) {
			return;
		}		
		AIModelEntity model = modelDAO.getModel(modelId);
		AIModelParamEntity prm = new AIModelParamEntity();
		prm.setAiModel(model);
		prm.setName(paramName);
		prm.setCustomKey(customKey);
		prm.setDataType(paramType);
		modelDAO.saveModelParam(prm);		
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public void checkModelParam(AIModelEntity model, String customKey, String paramName, String paramType) {
		// проверить на уникальное имя
		if (modelDAO.findModelParam(model.getId(), paramName, customKey) != null) {
			return;
		}		

		AIModelParamEntity prm = new AIModelParamEntity();
		prm.setAiModel(model);
		prm.setName(paramName);
		prm.setCustomKey(customKey);
		prm.setDataType(paramType);
		modelDAO.saveModelParam(prm);		
	}	

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)    
	public void removeDraftModels() {
		Query qry=emMicro.createNamedQuery("deleteModelDraft");
		qry.setParameter("isactive", ActiveStatus.DRAFT);
		qry.executeUpdate();
	} 
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)    	
	public void closeModelSession(AIModel model, String customKey) {
		List<AIModelParamEntity> lst = modelDAO.listModelParamsCK(model.getId(), customKey);
		AIModelParamEntity prmSave = ModelUtils.findParam(lst, ProcessKeys.VAR_SAVE_VARIABLES);
		if (!(prmSave != null && "1".equals(prmSave.getDataValue()))) {
			modelDAO.deleteModelParams(model.getId(), customKey);
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)  	
	public void applyModelParams(int modelId, String customKeys) {
		modelDAO.deleteModelParams(modelId, null);
		modelDAO.copyModelParams(modelId, customKeys, modelId, null);
		
		sqlDAO.dropView("v_model_" + modelId);
		String sql = generateResultView(modelId, null);
		sqlDAO.createView("v_model_" + modelId, sql);
	}
	
	protected String generateResultView(int modelId, String customKey) {
		String sql = "select v.run_id ";
		List<AIModelParamEntity> lst = modelDAO.listModelParamsCK(modelId, customKey);
		for (AIModelParamEntity prm: lst) {
			String ss = "max(case when (p.name = '" + prm.getName() + "') then v.value else null end) as " + prm.getName();
			sql = sql + ", " + ss;
		}
		sql = sql + " from aimodelvalues v inner join aimodelparams p on (p.id = v.aimodelparam_id)";
		sql = sql + " group by v.run_id";
		return sql;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)  	
	public List<AIModelParamEntity> revertModelParams(int modelId, String customKeys) {
		modelDAO.deleteModelParams(modelId, customKeys);
		modelDAO.copyModelParams(modelId, null, modelId, customKeys);
		List<AIModelParamEntity> lst = modelDAO.listModelParamsCK(modelId, customKeys);
		return lst;		
	}	
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public void saveModelParams(int modelId, String customKey, Map<String, Object> mpVars) {
		
		List<AIModelParamEntity> lstEnt = modelDAO.listModelParamsCK(modelId, customKey);
		
		HashSet<String> pnames = new HashSet<String>(lstEnt.size()); 
		for (AIModelParamEntity prm: lstEnt) {
			pnames.add(prm.getName());
		}
		
		// добавляем неописанные параметры
		AIModelEntity model = modelDAO.getModel(modelId);
		for (Map.Entry<String, Object> mp: mpVars.entrySet()) {
			if (pnames.contains(mp.getKey())) {
				continue;
			}
			
			AIModelParamEntity prm = new AIModelParamEntity();
			prm.setAiModel(model);
			prm.setCustomKey(customKey);
			prm.setDataType(Convertor.TYPE_SHORT_STRING);
			prm.setIsIn(0);
			prm.setIsOut(1);
			prm.setName(mp.getKey());
			modelDAO.saveModelParam(prm);
		}
		if (customKey != null) {
			modelDAO.copyModelParams(modelId, customKey, modelId, null);
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)	
	public int saveModelParamValues(int modelId, String customKey, Map<String, Object> mpVars, Integer runId) {
		if (runId == null) {
			runId = modelDAO.getNextRunId();
		}
		List<AIModelParamEntity> lstEnt = modelDAO.listModelParamsCK(modelId, customKey);
		HashSet<String> pnames = new HashSet<String>(lstEnt.size()); 
		for (AIModelParamEntity prm: lstEnt) {
			pnames.add(prm.getName());
			Object avalue = mpVars.get(prm.getName());
			String svalue = Convertor.toString(avalue);
			modelDAO.putModelParamValue(runId, prm.getId(), svalue);
		}
		
		return runId;
	}	
	
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)    
    public void checkDefaultModelParams(int modelId, String customKey) {
    	AIModelEntity model = modelDAO.getModel(modelId);
    	
    	checkModelParam(model, customKey, ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, Convertor.TYPE_SHORT_STRING);
    	checkModelParam(model, null, ProcessKeys.VAR_BUSINESS_OBJECT_CLASS, Convertor.TYPE_SHORT_STRING);
    	checkModelParam(model, customKey, ProcessKeys.VAR_BUSINESS_OBJECT_ID, Convertor.TYPE_INTEGER);
    	checkModelParam(model, null, ProcessKeys.VAR_BUSINESS_OBJECT_ID, Convertor.TYPE_INTEGER);
    	checkModelParam(model, customKey, ProcessKeys.VAR_SAVE_VARIABLES, Convertor.TYPE_INTEGER);
    	checkModelParam(model, null, ProcessKeys.VAR_SAVE_VARIABLES, Convertor.TYPE_INTEGER);
    	checkModelParam(model, customKey, ProcessKeys.VAR_NO_AUTO_REFUSE, Convertor.TYPE_INTEGER);
    	checkModelParam(model, null, ProcessKeys.VAR_NO_AUTO_REFUSE, Convertor.TYPE_INTEGER);    	
    }	
	
    @Override
	public boolean isVariableStandart(String varName) {
		return (ProcessKeys.VAR_SAVE_VARIABLES.equals(varName) 
				|| ProcessKeys.VAR_BUSINESS_OBJECT_CLASS.equals(varName) 
				|| ProcessKeys.VAR_BUSINESS_OBJECT_ID.equals(varName)
				|| ProcessKeys.VAR_NO_AUTO_REFUSE.equals(varName));
	}
	
    @Override
	public boolean isVariableEditable(String varName) {
		return ! (ProcessKeys.VAR_BUSINESS_OBJECT_CLASS.equals(varName) 
				|| ProcessKeys.VAR_BUSINESS_OBJECT_ID.equals(varName)
				);
	}	
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteResults(String businessObjectClass, Integer businessObjectId) {
    	modelDAO.deleteResults(businessObjectClass, businessObjectId);
    }
    
    @Override
    public List<Map> listLastParamValues(String businessObjectClass, Integer businessObjectId) {
    	List<Object[]> lst = modelDAO.listLastResults(businessObjectClass, businessObjectId);
    	List<Map> lstRes = new ArrayList<Map>(lst.size());
    	for (Object[] row: lst) {
    		Map<String, Object> mp = new HashMap<String, Object>(5);
    		mp.put("name", row[0]);
    		mp.put("description", row[1]);
    		mp.put("dataType", row[2]);
    		mp.put("dataValue", row[3]);
    		mp.put("aiModelParamId", row[4]);
    		lstRes.add(mp);
    	}
    	return lstRes;
    }
    
    @Override
    public AIModel getLastModel(int paramId) {
    	Integer modelId = modelDAO.findModelIdByParam(paramId);
    	if (modelId == null) {
    		return null;
    	}
    	AIModel model = modelDAO.getModel(modelId, Utils.setOf());
    	return model;
    }
}
