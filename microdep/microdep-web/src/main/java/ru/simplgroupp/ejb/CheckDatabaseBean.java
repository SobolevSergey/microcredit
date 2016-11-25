package ru.simplgroupp.ejb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import ru.simplgroupp.exception.DeployerException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

@Stateless
@LocalBean
public class CheckDatabaseBean {
	
	private static Logger logger = Logger.getLogger(CheckDatabaseBean.class.getName());
	
	@PersistenceContext(unitName="MicroPUStart")
	EntityManager emMicro;
	
	@Resource(mappedName="java:jboss/datasources/MicroDS")
	DataSource dsMicro;
	

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void execute(DatabaseConfiguration dbConf, List<ExceptionInfo> statusMessages) {
		
		// определяем, можем ли мы получить новые версии изменений
		boolean bDeploy = true;
		Object[] depRes = getURLContent(dbConf.getDeployURL(), statusMessages);	
		Properties propsDeploy = null;
		if (depRes == null || depRes[1] == null) {
			bDeploy = false;
		} else {
			propsDeploy = new Properties();
			try {
				propsDeploy.load((InputStream) depRes[1] );
			} catch (IOException e) {
				statusMessages.add(new ExceptionInfo(0, "Неправильный формат файла " + dbConf.getDeployURL(), Type.TECH, ResultType.NON_FATAL));
				logger.log(Level.SEVERE, "Неправильный формат файла " + dbConf.getDeployURL());
				bDeploy = false;
			}			
		}
		
		// временно
		bDeploy = false;
		
		if (bDeploy) {
			logger.info("Начинаем обновление баз");
			File fileParent = (File) depRes[0];
			Map<String,String> procSources = extractDbSources(propsDeploy);
			for (Map.Entry<String,String> entry: procSources.entrySet()) {
				if (StringUtils.isBlank( entry.getValue() )) {
					continue;
				}			
				
				logger.info("Обновляем базу " + entry.getKey());
				handleDbByURL(entry.getKey(), fileParent, entry.getValue(), statusMessages);
			}
			logger.info("Обновление баз завершено");
		} else {
			logger.info("Обновление баз выполнено не будет.");
		}
		
		// проверяем версии БД
		String[] dbConditions = extracDbCondions(propsDeploy);
		if ( dbConditions.length == 0 ) {
			return;
		}		

		Map<String, String> mpLatest = selectLatestVersions();
		for (String scond : dbConditions) {
			String[] condValues = scond.split(";");
			if (condValues.length < 3) {
				statusMessages.add(new ExceptionInfo(0, "Неверный формат условия: " + scond, Type.BUSINESS, ResultType.FATAL));
				continue;
			}
			checkCondition(condValues, mpLatest, statusMessages);
		}		

	}
	
	private void handleDbByURL(String dbName, File fileParent, String procURL, List<ExceptionInfo> statusMessages) {
		try {
			File fl = new File(fileParent, procURL);
			if (! fl.exists()) {
				statusMessages.add(new ExceptionInfo(0, "Неправильный путь к changelog: " + procURL, Type.BUSINESS, ResultType.NON_FATAL));
				logger.log(Level.SEVERE, "Неправильный путь к changelog: " + procURL);
				return;
			}

			DataSource ds = null;
			if ("micro".equals(dbName)) {
				ds = dsMicro;
			}
			
			if (ds != null) {
				internalDeployDb(fl.getPath(), ds, statusMessages);
			}
				
		} catch (DeployerException e) {
			statusMessages.add(new ExceptionInfo(0, "База не будет обновлёна: " + procURL, Type.BUSINESS, ResultType.NON_FATAL));
			logger.log(Level.SEVERE, "База не будет обновлёна: " + procURL , e);
			return;			
		}
	}
	
	private void internalDeployDb(String chFilePath, DataSource ds, List<ExceptionInfo> statusMessages) throws DeployerException {
		try {
			Liquibase lbase = new Liquibase(chFilePath, new FileSystemResourceAccessor(), new JdbcConnection( ds.getConnection() ));
			lbase.validate();
			lbase.update("");
			logger.info("Обновление успешно выполнено");
			// TODO
		} catch (LiquibaseException e) {
			logger.log(Level.SEVERE, "Обновление не выполнено", e);
			ExceptionInfo info = new ExceptionInfo(0, e.getMessage(), Type.BUSINESS, ResultType.NON_FATAL);
			statusMessages.add(info);
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Обновление не выполнено", e);
			ExceptionInfo info = new ExceptionInfo(0, e.getMessage(), Type.BUSINESS, ResultType.NON_FATAL);
			statusMessages.add(info);
		}
	}

	private Map<String, String> extractDbSources(Properties props) {
		Map<String, String> res = new HashMap<>(props.size());
		for (Map.Entry entry: props.entrySet()) {
			if (entry.getKey().toString().endsWith(".source")) {
				String scond = props.getProperty(entry.getKey().toString().replace(".source", ".condition"));
				String[] sc = scond.split(";");
				res.put(sc[0], entry.getValue().toString());
			}
		}
		return res;
	}	
	
	private void checkCondition(String[] condValues, Map<String, String> mpLatest, List<ExceptionInfo> statusMessages) {
		String dbName = condValues[0];
		String minVersion = StringUtils.defaultIfBlank(condValues[1],"0");
		String maxVersion = StringUtils.defaultIfBlank(condValues[2],"0");
		
		String curVersion = mpLatest.get(dbName);
		if (StringUtils.isBlank(curVersion)) {
			// не найден в имеющихся процессах
			statusMessages.add(new ExceptionInfo(0, "База " + dbName + " не найдена" , Type.BUSINESS, ResultType.FATAL));
			return;
		}
		
		Integer iminVersion = Integer.parseInt(minVersion);
		Integer imaxVersion = Integer.parseInt(maxVersion);
		Integer icurVersion = Integer.parseInt(curVersion);
		if (icurVersion.compareTo(iminVersion) >= 0 && icurVersion.compareTo(imaxVersion) <= 0) {
			// версии совпадают
		} else {
			// версии не совпадают
			statusMessages.add(new ExceptionInfo(0, "База " + dbName + " не подходит по версии. Текущая версия " + curVersion + " , а требуется от " + minVersion + " до " + maxVersion , Type.BUSINESS, ResultType.FATAL));
		}
	}	
	
	private Map<String, String> selectLatestVersions() {
		Map<String, String> mpRes = new HashMap<String, String>(4);

		Query qry = emMicro.createNamedQuery("selectDbVersion");
		List lst = qry.getResultList();
		mpRes.put("micro", lst.get(0).toString());
		
		return mpRes;
	}	
	
	private String[] extracDbCondions(Properties props) {
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
	
	private Object[] getURLContent(String surl, List<ExceptionInfo> statusMessages) {
		if (StringUtils.isBlank(surl)) {
			return null;
		}

		try {
			File fl = new File(surl);
			if (! fl.exists()) {
				statusMessages.add(new ExceptionInfo(0, "Неправильный путь к настройкам БД: " + surl, Type.BUSINESS, ResultType.NON_FATAL));
				logger.log(Level.SEVERE, "Неправильный путь к настройкам БД: " + surl );
				return null;				
			}
			FileInputStream stm = new FileInputStream(fl);
			return new Object[]{ fl.getParentFile(), stm };
			
		} catch (IOException e) {
			statusMessages.add(new ExceptionInfo(0, "Невозможно получить данные из: " + surl, Type.TECH, ResultType.NON_FATAL));
			logger.info("Невозможно получить данные из: " + surl);
			return null;
		}		
	}	
}
