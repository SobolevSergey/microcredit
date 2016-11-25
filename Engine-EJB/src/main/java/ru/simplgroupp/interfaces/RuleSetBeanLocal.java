package ru.simplgroupp.interfaces;

import org.kie.api.runtime.StatelessKieSession;
import ru.simplgroupp.ejb.BusinessEvent;
import ru.simplgroupp.exception.ExceptionInfo;
import ru.simplgroupp.transfer.AIRule;
import ru.simplgroupp.transfer.BizAction;

import javax.ejb.Local;
import java.util.List;

@Local
public interface RuleSetBeanLocal {

    public List<ExceptionInfo> getStateMessages();

    public void clearStateMessages();

    /**
     * Сессия для расчета вида деятельности коллектора
     *
     * @return
     */
    public StatelessKieSession newCreditCollectorSession();

    /**
     * сессия для возврата денег
     *
     * @return
     */
    public StatelessKieSession newReturnSession();

    /**
     * новая сессия правил
     *
     * @param kbaseName название
     * @return
     */
    public StatelessKieSession newSession(String kbaseName);

    /**
     * сессия для рассчета кредита
     *
     * @return
     */
    public StatelessKieSession newCreditCalcSession();

    /**
     * сессия для рассчета дней продления кредита
     *
     * @return
     */
    public StatelessKieSession newLongdaysCalcSession();

    /**
     * сессия для рассчета суммы бонусов
     *
     * @return
     */
    public StatelessKieSession newBonusCalcSession();

    /**
     * сессия для рассчета первоначальных параметров кредита (сумма, дни)
     *
     * @return
     */
    public StatelessKieSession newCreditCalcParamsSession();

    public StatelessKieSession newWatchedFieldsSession();

    public StatelessKieSession newObjectActionsSession(String businessObjectClass,
                                                       String signalRef);

    public StatelessKieSession newProcessActionsSession(String processDefKey,
                                                        String pluginName, String name);

    public List<ExceptionInfo> preCompileBizActionRules(BizAction action);

    void actionChangedEvent(BusinessEvent event);

    /**
     * перекомпилировать правило
     *
     * @param act бизнес-действие
     */
    void recompileRule(BizAction act);

    /**
     * перекомпилировать правило
     *
     * @param rule правило
     */
    void recompileRule(AIRule rule);

    List<ExceptionInfo> preCompileRule(AIRule rule);

    StatelessKieSession newObjectActionsSessionStart(
            String businessObjectClass, String signalRef);

}
