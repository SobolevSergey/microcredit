package ru.simplgroupp.ejb;

import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.interfaces.*;
import ru.simplgroupp.persistence.QuestionAnswerEntity;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Local(VerificationBeanLocal.class)
public class VerificationBean extends AbstractPluginBean implements VerificationBeanLocal {

    @EJB
    ServiceBeanLocal servBean;

    @EJB
    WorkflowBeanLocal workflow;

    @EJB
    QuestionBeanLocal questBean;

    @Override
    public String getSystemName() {
        return VerificationBeanLocal.SYSTEM_NAME;
    }

    @Override
    public boolean isFake() {
        return false;
    }

    @Override
    public EnumSet<Mode> getModesSupported() {
        return EnumSet.of(Mode.SINGLE);
    }

    @Override
    public EnumSet<ExecutionMode> getExecutionModesSupported() {
        return EnumSet.of(ExecutionMode.MANUAL);
    }

    @Override
    public EnumSet<SyncMode> getSyncModesSupported() {
        return EnumSet.of(SyncMode.SYNC);
    }

    @Override
    public void executeSingle(String businessObjectClass,
                              Object businessObjectId, PluginExecutionContext context)
            throws ActionException {

        int creditRequestId = Convertor.toInteger(businessObjectId);
        autoAnswerQAs(creditRequestId, context);
    }

    @Override
    public void addToPacket(String businessObjectClass,
                            Object businessObjectId, PluginExecutionContext context)
            throws ActionException {
    }

    @Override
    public List<BusinessObjectResult> executePacket(
            PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public boolean sendSingleRequest(String businessObjectClass,
                                     Object businessObjectId, PluginExecutionContext context)
            throws ActionException {
        return false;
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass,
                                       Object businessObjectId, PluginExecutionContext context)
            throws ActionException {
        return false;
    }

    @Override
    public List<BusinessObjectResult> sendPacketRequest(
            PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public List<BusinessObjectResult> queryPacketResponse(
            PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public String getBusinessObjectClass() {
        return CreditRequest.class.getName();
    }

    @Override
    public Set<String> getModelTargetsSupported() {
        return Utils.setOf(ModelKeys.TARGET_CREDIT_DECISION, ModelKeys.TARGET_CREDIT_DECISION_OFFLINE);
    }

    @Override
    public QuestionAnswerEntity getQA(int creditRequestId, String key, ActionContext actionContext) {
        QuestionAnswerEntity ent = questBean.findQA(creditRequestId, key);
        return ent;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public QuestionAnswerEntity addQA(int creditRequestId, String key, ActionContext actionContext) {
        QuestionAnswerEntity ent = questBean.findQA(creditRequestId, key);
        if (ent == null) {
            ent = questBean.addQA(creditRequestId, key);
        }
        return ent;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public QuestionAnswerEntity removeQA(int creditRequestId, String key, ActionContext actionContext) {
        QuestionAnswerEntity ent = questBean.findQA(creditRequestId, key);
        if (ent != null) {
            questBean.removeQA(ent);
        }
        return null;
    }

    @Override
    public void autoAnswerQAs(int creditRequestId, PluginExecutionContext context) {
    }

    @Override
    public String getSystemDescription() {
        return VerificationBeanLocal.SYSTEM_DESCRIPTION;
    }
}
