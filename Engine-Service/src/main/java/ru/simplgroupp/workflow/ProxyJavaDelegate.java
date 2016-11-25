package ru.simplgroupp.workflow;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.Expression;
import org.apache.commons.lang.StringUtils;

public class ProxyJavaDelegate implements JavaDelegate {
	
	private static Logger logger = Logger.getLogger(ProxyJavaDelegate.class.getName());
	
	Expression actualClassName;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		if (actualClassName == null) {
			logger.severe("Не задано имя актуального класса");
			return;
		}
		String sActualClassName = actualClassName.getValue(execution).toString();
		if (StringUtils.isBlank(sActualClassName)) {
			logger.severe("Имя актуального класса не вычислено по выражению: " + actualClassName.getExpressionText());
			return;
		}
		
		JavaDelegate actualDelegate = null;
		try {
			actualDelegate = (JavaDelegate) Class.forName(sActualClassName).newInstance();
		} catch (InstantiationException e) {
			logger.log(Level.SEVERE, "Класс не создан :" + sActualClassName, e);
		} catch (IllegalAccessException e) {
			logger.log(Level.SEVERE, "Ошибка доступа к классу :" + sActualClassName, e);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Класс не найден :" + sActualClassName, e);
		}
		if (actualDelegate == null) {
			return;
		}
		actualDelegate.execute(execution);
		
	}

}
