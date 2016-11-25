package ru.simplgroupp.ejb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.StartCheckBeanLocal;

@Startup
@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Local(StartCheckBeanLocal.class)
public class StartCheckBean implements StartCheckBeanLocal {
	private static Logger log = Logger.getLogger(StartCheckBean.class.getName());
	
	private ApplicationContext appContext;
	
	private List<ExceptionInfo> statusMessages = new ArrayList<ExceptionInfo>(15);
	private int nFatal;
	
	@EJB
	CheckDatabaseBean checkDB;
	
	@EJB
	CheckProcessBean checkProc;	
	
	@PostConstruct
    public void onStartup() {
		appContext = new ClassPathXmlApplicationContext(new String[] {"META-INF/spring-beans.xml"});
	
		check(true, true);
		
	}
	
	@Override
	public boolean isOk() {
		return (nFatal == 0);
	}
	
	@Override
	public List<ExceptionInfo> getStatusMessages() {
		return Collections.unmodifiableList(statusMessages);
	}
	
	@Override
	@Lock(LockType.WRITE)
	public void check(boolean bCheckDatabase, boolean bCheckProcesses) {
		statusMessages.clear();
		nFatal = 0;
		
		try {
			if (bCheckDatabase) {
				log.info("Проверяем БД micro");
				checkDatabase();
			}
			
			if (bCheckProcesses) {
				log.info("Проверяем процессы");
				checkProcesses();
			}
		} catch (RuntimeException ex) {
			statusMessages.add(new ExceptionInfo(0, ex.getMessage(), Type.TECH, ResultType.FATAL));
			nFatal++;
		} finally {
			for (ExceptionInfo info: statusMessages) {
				if (info.getResultType().equals(ResultType.FATAL)) {
					nFatal++;
				}
			}
		}
	}
	
	private void checkProcesses() {
		ProcessConfiguration procConf = (ProcessConfiguration) appContext.getBean("procStartConfiguration");
		if (procConf.getEngineConfiguration() == null) {
			throw new RuntimeException("Deployer engine configuration not found");
		}		
		
		checkProc.execute(procConf, statusMessages);
	}
	
	private void checkDatabase() {
		
		DatabaseConfiguration dbConf = (DatabaseConfiguration) appContext.getBean("dbStartConfiguration");
		if (dbConf == null) {
			throw new RuntimeException("Deployer db configuration not found");
		}			
		
		checkDB.execute(dbConf, statusMessages);
	}
		
}
