package ru.simplgroupp.interfaces;

import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.persistence.QuestionAnswerEntity;
import ru.simplgroupp.workflow.PluginExecutionContext;

/**
 * Интерфейс верификатора
 * @author irina
 *
 */
public interface VerificationBeanLocal extends PluginSystemLocal {
	public static final String SYSTEM_NAME = "verify";
	public static final String SYSTEM_DESCRIPTION = "Вопросы верификатору";
	
	/**
	 * получаем вопрос
	 * @param creditRequestId - id заявки
	 * @param key - ключ
	 */
	public QuestionAnswerEntity getQA(int creditRequestId, String key, ActionContext actionContext);
	/**
	 * добавляем вопрос
	 * @param creditRequestId - id заявки
	 * @param key - ключ
	 */
	public QuestionAnswerEntity addQA(int creditRequestId, String key, ActionContext actionContext);
	/**
	 * удаляем вопрос
	 * @param creditRequestId - id заявки
	 * @param key - ключ
	 */
	public QuestionAnswerEntity removeQA(int creditRequestId, String key, ActionContext actionContext);
	
	/**
	 * Автоматически ответить на все вопросы. Используется при прогоне модели в режиме fake.
	 * @param creditRequestId - id заявки
	 * @param context - контекст плагина
	 */
	public void autoAnswerQAs(int creditRequestId, PluginExecutionContext context);
}
