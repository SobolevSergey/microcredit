package ru.simplgroupp.ejb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ResourceEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import ru.simplgroupp.exception.DeployerException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.toolkit.common.Utils;

@Stateless
@LocalBean
public class CheckProcessBean {
	
	private static Logger logger = Logger.getLogger(CheckProcessBean.class.getName());
	
	@PersistenceContext(unitName="MicroPUStart")
	EntityManager emMicro;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void execute(ProcessConfiguration procConfig, List<ExceptionInfo> statusMessages) {
		
		ProcessEngine wfEngine = procConfig.getEngineConfiguration().buildProcessEngine();
		if (wfEngine == null) {
			statusMessages.add(new ExceptionInfo(0, "Deployer couldn't initialize activiti engine", Type.TECH, ResultType.FATAL));
			return;
		}		

		// определяем, можем ли мы получить новые версии процессов
		boolean bDeploy = true;
		Object[] depRes = getURLContent(procConfig.getDeployURL(), statusMessages);
		Properties propsDeploy = null;
		if (depRes == null || depRes[1] == null) {
			bDeploy = false;
		} else {
			propsDeploy = new Properties();
			try {
				propsDeploy.load((InputStream) depRes[1] );
			} catch (IOException e) {
				statusMessages.add(new ExceptionInfo(0, "Неправильный формат файла " + procConfig.getDeployURL(), Type.TECH, ResultType.NON_FATAL));
				logger.log(Level.SEVERE, "Неправильный формат файла " + procConfig.getDeployURL());
				bDeploy = false;
			}			
		}
		
		if (bDeploy) {
			logger.info("Начинаем обновление процессов");
			// обновляем версии
			List<ProcessInfo> lstConvert = new ArrayList<ProcessInfo>(10);
			
			URL urlParent = (URL) depRes[0];
			String[] procSources = extractProcessSources(propsDeploy);
			for (String procURL: procSources) {
				if (StringUtils.isBlank(procURL.toString())) {
					continue;
				}
				
				ProcessInfo info = handleProcByURL(urlParent, procURL.toString(), wfEngine, statusMessages);
				if (info != null && info.needConvert) {
					lstConvert.add(info);
				}
			}
			wfEngine.close();
			wfEngine = null;
			logger.info("Обновление процессов завершено");
			
			// конвертируем
			if (lstConvert.size() > 0) {
				logger.info("Начинаем конвертацию процессов");
				for (ProcessInfo info: lstConvert) {
					internalConvertProcess(info);
				}
				logger.info("Конвертация процессов завершена");
			}
		} else {
			wfEngine.close();
			wfEngine = null;
			logger.info("Обновление процессов выполнено не будет.");
		}
		
		// проверяем версии
		String[] procConditions = extractProcessCondions(propsDeploy);
		if ((procConfig.getProcessConditions() == null || procConfig.getProcessConditions().size() == 0 ) && procConditions.length == 0 ) {
			return;
		}
		
		if (procConfig.getProcessConditions() != null && procConfig.getProcessConditions().size() > 0 ) {
			procConditions = (String[]) procConfig.getProcessConditions().toArray();
		}		
		Map<String, String> mpLatest = selectLatestVersions();
		for (String scond : procConditions) {
			String[] condValues = scond.split(";");
			if (condValues.length < 4) {
				statusMessages.add(new ExceptionInfo(0, "Неверный формат условия: " + scond, Type.BUSINESS, ResultType.FATAL));
				continue;
			}
			checkCondition(condValues, mpLatest, statusMessages);
		}
	}
	
	private String[] extractProcessSources(Properties props) {
		String[] res = new String[props.size()];
		int n = 0;
		for (Map.Entry entry: props.entrySet()) {
			if (entry.getKey().toString().endsWith(".source")) {
				res[n] = entry.getValue().toString();
				n++;
			}
		}
		return Arrays.copyOf(res, n);
	}
	
	private String[] extractProcessCondions(Properties props) {
		String[] res = new String[props.size()];
		int n = 0;
		for (Map.Entry entry: props.entrySet()) {
			if (entry.getKey().toString().endsWith(".condition")) {
				res[n] = entry.getValue().toString();
				n++;
			}
		}
		return Arrays.copyOf(res, n);
	}	
	
	private void checkCondition(String[] condValues, Map<String, String> mpLatest, List<ExceptionInfo> statusMessages) {
		String procDefKey = condValues[0];
		String minVersion = StringUtils.defaultIfBlank(condValues[1],"");
		String maxVersion = StringUtils.defaultIfBlank(condValues[2],"");
		boolean bRequired = "required".equals(StringUtils.defaultIfBlank(condValues[3],"required"));
		
		String curVersion = mpLatest.get(procDefKey);
		if (StringUtils.isBlank(curVersion)) {
			// не найден в имеющихся процессах
			if (bRequired) {
				statusMessages.add(new ExceptionInfo(0, "Процесс " + procDefKey + " не найден" , Type.BUSINESS, ResultType.FATAL));
				return;
			} else {
				return;
			}
		}
		
		if (curVersion.compareTo(minVersion) >= 0 && curVersion.compareTo(maxVersion) <= 0) {
			// версии совпадают
		} else {
			// версии не совпадают
			if (bRequired) {
				statusMessages.add(new ExceptionInfo(0, "Процесс " + procDefKey + " не подходит по версии. Текущая версия " + curVersion + " , а требуется от " + minVersion + " до " + maxVersion , Type.BUSINESS, ResultType.FATAL));
			}
		}
	}
	
	private Map<String, String> selectLatestVersions() {
		Query qry = emMicro.createNamedQuery("selectProcDefVersionsLatest");
		List<Object[]> lst = qry.getResultList();
		
		Map<String, String> mpRes = new HashMap<String, String>(lst.size());
		for (Object[] row: lst) {
			mpRes.put(row[1].toString(), row[3].toString());
		}
		return mpRes;
	}
	
	private void internalConvertProcess(ProcessInfo info) {
		for (int i = 0; i <= 6; i++) {
			Query qry = emMicro.createNamedQuery("updateProc" + String.valueOf(i));
			qry.setParameter("newDefId", info.CurrentDefId);
			qry.setParameter("oldDefId", info.PrevDefId);
			qry.executeUpdate();
		}
		
	}

	private ProcessInfo handleProcByURL(URL urlParent, String procURL, ProcessEngine wfEngine, List<ExceptionInfo> statusMessages) {
		try {
			URL aurl = new URL(urlParent, procURL);
			Object content = aurl.getContent();
			if (content != null) {
				try (InputStream stm = (InputStream) content) {
					return internalDeployProcess(stm, wfEngine, statusMessages);	
				}				
			} else {
				return null;
			}
		} catch (MalformedURLException e) {
			statusMessages.add(new ExceptionInfo(0, "Неправильный путь к процессу: " + procURL, Type.BUSINESS, ResultType.NON_FATAL));
			logger.log(Level.SEVERE, "Неправильный путь к процессу: " + procURL , e);
			return null;
		} catch (IOException e) {
			statusMessages.add(new ExceptionInfo(0, "Невозможно получить данные из: " + procURL, Type.TECH, ResultType.NON_FATAL));
			logger.log(Level.SEVERE, "Невозможно получить данные из: " + procURL , e);
			return null;
		} catch (DeployerException e) {
			statusMessages.add(new ExceptionInfo(0, "Процесс не будет обновлён: " + procURL, Type.BUSINESS, ResultType.NON_FATAL));
			logger.log(Level.SEVERE, "Процесс не будет обновлён: " + procURL , e);
			return null;			
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProcessInfo internalDeployProcess(InputStream source, ProcessEngine wfEngine, List<ExceptionInfo> statusMessages) throws DeployerException {
		ProcessInfo info = null;
		try {
			boolean bDeploy = false;
			
			// информация о новом процессе
			byte[] barr = IOUtils.toByteArray(source);
			info = analyzeProcess(barr);
			if (StringUtils.isBlank(info.externalVersion)) {
				statusMessages.add(new ExceptionInfo(0, "Неверный формат процесса. Процесс не содержит информации о версии.", Type.BUSINESS, ResultType.NON_FATAL));
				throw new RuntimeException("Неверный формат процесса. Процесс не содержит информации о версии.");
			}
			
			// ищем существующую версию
			ProcessDefinition pdef = wfEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(info.processDefKey).latestVersion().singleResult();
			if (pdef == null) {
				bDeploy = true;
			} else {
				info.PrevDefId = pdef.getId();
				// ищем внешний номер текущей версии
				String sCurrVers = selectProcDefVersion(pdef.getId());
				if (! info.externalVersion.equals(sCurrVers)) {
					bDeploy = true;
					
					// ищем работающие экземпляры процесса
					info.needConvert = (wfEngine.getRuntimeService().createProcessInstanceQuery().processDefinitionId(pdef.getId()).count() > 0);
				}
			}
			
			if (! bDeploy) {
				return info;
			}
			
			// добавляем новую версию процесса
			Deployment dep = null;
			try (	ByteArrayInputStream stm1 = new ByteArrayInputStream(barr);		
					ZipInputStream stm = new ZipInputStream(stm1);
				) {
				dep = wfEngine.getRepositoryService().createDeployment().addZipInputStream(stm).deploy();
				DeploymentEntity depEnt = (DeploymentEntity) dep;
				
				// ищем сообщения
				ResourceEntity resEnt = depEnt.getResource("messages.xml");
				if (resEnt != null) {
					// загружаем сообщения для процесса
					internalLoadMessages(info.processDefKey, resEnt.getBytes());
				}
				
				// ищем настройки
				resEnt = depEnt.getResource("options.xml");
				if (resEnt != null) {
					// загружаем настройки для процесса
					internalLoadOptions(info.processDefKey, resEnt.getBytes());
				}

				// ищем настройки
				resEnt = depEnt.getResource("actions.xml");
				if (resEnt != null) {
					// загружаем действия для процесса
					internalLoadActions(info.processDefKey, resEnt.getBytes());
				}
				
			}			
			ProcessDefinition pnew = wfEngine.getRepositoryService().createProcessDefinitionQuery().deploymentId(dep.getId()).singleResult();
			insertProcDefVersion(pnew.getId(), info.externalVersion);
			info.CurrentDefId = pnew.getId();
			
			return info;
		} catch (IOException e) {
			statusMessages.add(new ExceptionInfo(0, "Ошибка при обработке процесса " + info.processDefKey, Type.TECH, ResultType.NON_FATAL));
			throw new DeployerException("Ошибка при обработке процесса ", e);
		} catch (DocumentException e) {
			statusMessages.add(new ExceptionInfo(0, "Ошибка при обработке процесса" + info.processDefKey, Type.TECH, ResultType.NON_FATAL));
			throw new DeployerException("Ошибка при обработке процесса ", e);
		}	
	}
	
	private void internalLoadActions(String processDefKey, byte[] bytes) throws DocumentException {
		String sxml = null;
		try {
			sxml = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return;
		}
		
		Document xdoc = DocumentHelper.parseText(sxml);
		// TODO проверить правильность xml
		List<String> lstExist = new ArrayList<String>(15);
		for (Object aobj : xdoc.getRootElement().elements("action")) {
			Element elmAct = (Element) aobj;
			
			Element elmRuleEnabled = elmAct.element("rule-enabled");
			Element elmRuleAction = elmAct.element("rule-action");
			String signalRef = elmAct.attributeValue("signal-ref");
			
			/*createOrUpdateAction(processDefKey, signalRef, elmAct.attributeValue("description"), elmAct.attributeValue("business-object-class"),
					elmAct.attributeValue("candidate-groups"), elmAct.attributeValue("candidate-values"), elmAct.attributeValue("assignee"),
					elmAct.attributeValue("system"), elmAct.attributeValue("required"), elmAct.attributeValue("active"),
					(elmRuleEnabled == null)?null:elmRuleEnabled.getText(), (elmRuleAction == null)?null:elmRuleAction.getText());*/
			lstExist.add(signalRef);
		}
		
		// удалить действия, которых больше нет в процессе
		/*Query qryDel = emMicro.createNamedQuery("deleteBizProcAction");		
		
		Query qry = emMicro.createNamedQuery("selectBizProcActions");
		qry.setParameter("processdefkey", processDefKey);
		List<Object[]> lst = qry.getResultList();	
		for (Object[] row: lst) {
			if (StringUtils.isBlank((String) row[1])) {
				continue;
			}
			if (lstExist.indexOf(row[1]) < 0) {
				// удалить действие
				qryDel.setParameter("id", row[0]);
				qryDel.executeUpdate();
			}
		}*/
	}
	
	private void createOrUpdateAction(String processDefKey, String signalRef, String description, String businessObjectClass, 
			String candidateGroups, String candidateUsers, String assignee, String system, String required, String active,
			String ruleEnabled, String ruleAction) {		
		Query qry = emMicro.createNamedQuery("selectBizProcAction");
		qry.setParameter("signalref", signalRef);
		qry.setParameter("processdefkey", processDefKey);
		List lst = qry.getResultList();
		if (lst.size() == 0) {
			// вставить
			qry = emMicro.createNamedQuery("insertBizProcAction");
			qry.setParameter("signalref", signalRef);
			qry.setParameter("businessobjectclass", businessObjectClass);
			qry.setParameter("description", description);
			qry.setParameter("assignee", assignee);
			qry.setParameter("candidategroups", candidateGroups);
			qry.setParameter("candidateusers", candidateUsers);
			qry.setParameter("candidateusers", candidateUsers);			
			qry.setParameter("issystem", Integer.parseInt(StringUtils.defaultIfBlank(system, "0")));
			qry.setParameter("processdefkey", processDefKey);
			qry.setParameter("ruleenabled", ruleEnabled);
			qry.setParameter("ruleaction", ruleAction);
			qry.setParameter("isactive", Integer.parseInt(StringUtils.defaultIfBlank(active, "1")));
			qry.setParameter("isrequired", Integer.parseInt(StringUtils.defaultIfBlank(required, "0")));
			qry.executeUpdate();
		} else {
			// обновить
			qry = emMicro.createNamedQuery("updateBizProcAction");
			qry.setParameter("signalref", signalRef);
			qry.setParameter("businessobjectclass", businessObjectClass);
			qry.setParameter("description", description);
			qry.setParameter("assignee", assignee);
			qry.setParameter("candidategroups", candidateGroups);
			qry.setParameter("candidateusers", candidateUsers);
			qry.setParameter("candidateusers", candidateUsers);			
			qry.setParameter("issystem", Integer.parseInt(StringUtils.defaultIfBlank(system, "0")));
			qry.setParameter("processdefkey", processDefKey);
			qry.setParameter("ruleenabled", ruleEnabled);
			qry.setParameter("ruleaction", ruleAction);
			qry.setParameter("isactive", Integer.parseInt(StringUtils.defaultIfBlank(active, "1")));
			qry.setParameter("isrequired", Integer.parseInt(StringUtils.defaultIfBlank(required, "0")));
			qry.executeUpdate();			
		}
	}
	
	private void internalLoadMessages(String processDefKey, byte[] bytes) throws DocumentException {
		String sxml = null;
		try {
			sxml = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return;
		}
		
		Document xdoc = DocumentHelper.parseText(sxml);
		// TODO проверить правильность xml
		
		//int ruleId = createOrGetRule("ru.simplgroupp.bp.messages." + processDefKey, 2, "Сообщения для процесса " + processDefKey);
		for (Object aobj : xdoc.getRootElement().elements("message")) {
			Element elmMsg = (Element) aobj;
			
			boolean bDefault = false;
			Element elmValue = elmMsg.element("value");
			if (elmValue == null) {
				elmValue = elmMsg.element("default-value");
				bDefault = true;
			}
			
		//	createOrUpdateConstant(ruleId, elmMsg.attributeValue("name"), elmMsg.attributeValue("type"), elmValue.getText(), bDefault, null);
		}
	}
	
	private void internalLoadOptions(String processDefKey, byte[] bytes) throws DocumentException {
		String sxml = null;
		try {
			sxml = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return;
		}
		
		Document xdoc = DocumentHelper.parseText(sxml);
		// TODO проверить правильность xml
		
		//int ruleId = createOrGetRule("ru.simplgroupp.bp.options." + processDefKey, 2, "Настройки для процесса " + processDefKey);
		for (Object aobj : xdoc.getRootElement().elements("option")) {
			Element elmMsg = (Element) aobj;
			
			boolean bDefault = false;
			Element elmValue = elmMsg.element("value");
			if (elmValue == null) {
				elmValue = elmMsg.element("default-value");
				bDefault = true;
			}
			
		//	createOrUpdateConstant(ruleId, elmMsg.attributeValue("name"), elmMsg.attributeValue("type"), elmValue.getText(), bDefault, null);
		}
	}	
	
	private void createOrUpdateConstant(int ruleid, String name, String dataType, String dataValue, boolean bDefault, String description) {
		Query qry = emMicro.createNamedQuery("selectConstant");
		qry.setParameter("ruleid", ruleid);
		qry.setParameter("name", name);
		List lst = qry.getResultList();
		if (lst.size() == 0) {
			// добавить
			qry = emMicro.createNamedQuery("insertConstant");
			qry.setParameter("ruleid", ruleid);
			qry.setParameter("name", name);
			qry.setParameter("datatype", dataType);
			qry.setParameter("description", description);
			if (dataType.equals("C") && dataValue != null && dataValue.length() > 250) {
				qry.setParameter("datavalue", null);
				qry.setParameter("datavaluetext", dataValue);
			} else {
				qry.setParameter("datavalue", dataValue);
				qry.setParameter("datavaluetext", null);				
			}
			qry.executeUpdate();
		} else {
			Object[] row = (Object[]) lst.get(0);
			Integer aid = ((Number) row[0]).intValue();
			String atype = (String) row[1];
			String advalue = (String) row[2];
			String advalueText = (String) row[3];
			String adesc = (String) row[4];
			String adval = StringUtils.defaultIfBlank(advalue, advalueText);
			
			boolean bUpdate = false;
			if (bDefault) {
				if (StringUtils.isBlank(adval)) {
					bUpdate = ! StringUtils.isBlank(dataValue);
				} else {
					bUpdate = false;
				}
			} else {
				bUpdate = ! Utils.equalsNull(adval, dataValue);
			}
			
			if (bUpdate) {
				// обновить
				qry = emMicro.createNamedQuery("updateConstant");
				qry.setParameter("id", aid);
				qry.setParameter("datatype", dataType);	
				qry.setParameter("description", description);
				if (dataType.equals("C") && dataValue != null && dataValue.length() > 250) {
					qry.setParameter("datavalue", null);
					qry.setParameter("datavaluetext", dataValue);
				} else {
					qry.setParameter("datavalue", dataValue);
					qry.setParameter("datavaluetext", null);				
				}	
				qry.executeUpdate();
			}
		}	
	}

	private int createOrGetRule(String ruleName, int ruleType, String description) {
		Query qry = emMicro.createNamedQuery("selectRule");
		qry.setParameter("packagename", ruleName);
		List lst = qry.getResultList();
		if (lst.size() > 0) {
			// есть правило с таким именем
			return ((Number) lst.get(0)).intValue();
		}
		
		qry = emMicro.createNamedQuery("insertRule");
		qry.setParameter("packagename", ruleName);
		qry.setParameter("body", null);
		qry.setParameter("ruletype", ruleType);
		qry.setParameter("description", description);
		qry.setParameter("kbase", null);
		qry.executeUpdate();
		
		qry = emMicro.createNamedQuery("selectRule");
		qry.setParameter("packagename", ruleName);
		lst = qry.getResultList();
		return ((Number) lst.get(0)).intValue();
	}

	private String selectProcDefVersion(String id) {
		Query qry = emMicro.createNamedQuery("selectProcDefVersion");
		qry.setParameter("procDefId", id);
		List lstRes = qry.getResultList();
		if (lstRes.size() > 0) {
			return (String) lstRes.get(0);
		} else {
			return null;
		}
	}
	
	private void insertProcDefVersion(String id, String sversion) {
		Query qry = emMicro.createNamedQuery("insertProcDefVersion");
		qry.setParameter("procDefId", id);
		qry.setParameter("extVersion", sversion);
		qry.executeUpdate();
	}
	
	private ProcessInfo analyzeProcess(byte[] source) throws IOException, DocumentException {
		ProcessInfo info = new ProcessInfo();
		
		try (	ByteArrayInputStream stm1 = new ByteArrayInputStream(source);		
				ZipInputStream stm = new ZipInputStream(stm1);
			) {
			ZipEntry entry = stm.getNextEntry();
			while (entry != null) {
				if (entry.getName().endsWith("pom.properties")) {
					// read version
					Properties props = new Properties();
					props.load(stm);
					info.externalVersion = props.getProperty("version");					
				} else if (entry.getName().endsWith(".bpmn20.xml")) {
					// read process
					String stext = IOUtils.toString(stm);
					Document xdoc = DocumentHelper.parseText(stext);
					Element elmProc = xdoc.getRootElement().element("process");
					info.processDefKey = elmProc.attributeValue("id");
				}
				entry = stm.getNextEntry();
			}
		}
		return info;
	}	

	private Object[] getURLContent(String surl, List<ExceptionInfo> statusMessages) {
		if (StringUtils.isBlank(surl)) {
			return null;
		}

		try {
			URL aurl = new URL(surl);
			Object content = aurl.getContent();
			if (content != null && content instanceof InputStream) {
				InputStream stm = (InputStream) content;
				return new Object[]{ aurl, stm };
			} else {
				return new Object[]{ aurl, null };
			}
		} catch (MalformedURLException e) {
			statusMessages.add(new ExceptionInfo(0, "Неправильный путь к хранилищу процессов: " + surl, Type.BUSINESS, ResultType.NON_FATAL));
			logger.log(Level.SEVERE, "Неправильный путь к хранилищу процессов: " + surl , e);
			return null;
		} catch (IOException e) {
			statusMessages.add(new ExceptionInfo(0, "Невозможно получить данные из: " + surl, Type.TECH, ResultType.NON_FATAL));
			logger.info("Невозможно получить данные из: " + surl);
			return null;
		}		
	}
	
	private class ProcessInfo {
		String processDefKey;
		String externalVersion;
		String PrevDefId;
		String CurrentDefId;
		boolean needConvert = false;
	}	
}
