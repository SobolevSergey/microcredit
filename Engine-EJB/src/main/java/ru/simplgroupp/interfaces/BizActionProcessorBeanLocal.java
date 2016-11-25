package ru.simplgroupp.interfaces;

import ru.simplgroupp.persistence.BizActionEntity;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ActionRuntimeException;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.rules.AbstractContext;
import ru.simplgroupp.toolkit.common.Identifiable;

import java.util.Map;

/**
 * Выполняет бизнес-действия
 *
 */
public interface BizActionProcessorBeanLocal {

    /**
     * ищем или стартуем все бизнес-действия, которые могут выполняться для выборки записей
     *
     * @throws ActionException
     * @throws ActionRuntimeException
     */
	public void startOrFindAllManyActions() throws ActionException, ActionRuntimeException;

    /**
     * выполняем бизнес-действие
     *
     * @param bizAct           параметры бизнес-действия
     * @param businezzObjectId id объекта
     * @param externalContext  контекст, описывающий условия, в которых происходит вызов 
     * @throws ActionException
     */
	public void executeBizAction(BizActionEntity bizAct, Object businezzObjectId, AbstractContext externalContext) throws ActionException;

    /**
     * запустить действие сейчас
     *
     * @param bizActionId id действия
     * @throws ActionException
     */
	public void runNow(int bizActionId) throws ActionException;

    /**
     * Ищем или стартуем бизнес-действие, которое запускает бизнес-процесс. Если процесс уже идёт, обновляем параметры процесса
     *
     * @param bizAct параметры бизнес-действия
     * @param businessObjectId - id бизнес-объекта
     * @param externalContext - контекст, описывающий условия, в которых происходит вызов 
     * @throws ActionException
     * @throws ActionRuntimeException
     */
	public void startOrFindBizAction(BizActionEntity bizAct, Object businessObjectId,
                              AbstractContext externalContext) throws ActionException;

    /**
     * Запускает бизнес-действие, которое само является частью процесса. 
     * Такое действие описывает, например, возможные варианты завершения задачи пользователя. 
     * Например, для задачи менеджера в процессе "Принять решение по заявке" из задачи могут быть два выхода: "дать кредит" или "отказать". 
     * Каждый из выходов описывается бизнес-действием.
     * 
     * @param businessObjectClass - класс бизнес-объекта
     * @param signalRef - имя бизнес-действия
     * @param procDefKey - определение процесса
     * @param plugin - если действие происходит во время выполнения плагина, то имя плагина, иначе - null.
     * @param businessObjectId - Id бизнес-объекта
     * @param externalContext контекст, описывающий условия, в которых происходит вызов
     * @return null или информацию об ошибке
     * @throws ActionException
     */
	public ExceptionInfo executeBizAction(String businessObjectClass,
                                   String signalRef, String procDefKey, String plugin,
                                   Integer businessObjectId, AbstractContext externalContext)  throws ActionException;

    /**
     * Проверяем, может ли быть выполнено данное действие
     * @param biz - бизнес-объект, над которым должно выполняться действие, например, заявка или займ
     * @param bizAct -бизнес-действие
     * @param externalContext - контекст, описывающий условия, в которых происходит вызов
     * @return
     */
    boolean canExecute(Object biz, BizActionEntity bizAct, AbstractContext externalContext);

    /**
     * Запускает бизнес действие для данного бизнес-объекта. Бизнес-действие запускает процесс. 
     * Если бизнес-действие не предполагает запуск процесса, возвращаем null. Метод сначала 
     * вычисляет предстартовые параметры, и использует их в запущенном процессе как переменные.
     * 
     * @param biz - бизнес-объект, над которым должно выполняться действие, например, заявка или займ
     * @param bizAct - бизнес-действие
     * @param externalContext контекст, описывающий условия, в которых происходит вызов
     * @param bStartOnly если true, то только вычисляем предстартовые параметры, но не запускаем процесс
     * @return вычисленные предстартовые параметры, если bStartOnly = true. Если bStartOnly = false, то всегда возвращаем null.
     * @throws ActionException
     * @throws ActionRuntimeException
     */
    public Map<String, Object> executeBizAction(Identifiable biz,
                                         BizActionEntity bizAct, AbstractContext externalContext,
                                         boolean bStartOnly) throws ActionException, ActionRuntimeException;

    /**
     * Выполняет бизнес-действие для сохранённого списка бизнес-объектов.
     * @param bizAct - бизнес-действие
     * @param listId - id сохранённого списка
     * @param bExclusive - если true, то для данного списка одновременно может выполняться только это действие,
     * если false - то по этому списку одновременно можно запустить другое действие
     * @param externalContext  контекст, описывающий условия, в которых происходит вызов 
     * @throws ActionException
     */
	public void executeBizAction(BizActionEntity bizAct, int listId, boolean bExclusive, AbstractContext externalContext) throws ActionException;

    /**
     * Выполняет бизнес-действие для сохранённого списка бизнес-объектов асинхронно.
     * 
     * @param bizAct - бизнес-действие
     * @param listId - id сохранённого списка
     * @param bExclusive - если true, то для данного списка одновременно может выполняться только это действие,
     * если false - то по этому списку одновременно можно запустить другое действие
     * @param externalContext  контекст, описывающий условия, в которых происходит вызов 
     * @throws ActionException
     */	
	public void executeBizActionAsync(BizActionEntity bizAct, int listId, boolean bExclusive, AbstractContext externalContext) throws ActionException;

}
