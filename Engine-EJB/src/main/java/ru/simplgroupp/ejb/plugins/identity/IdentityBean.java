package ru.simplgroupp.ejb.plugins.identity;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.ejb.AbstractPluginBean;
import ru.simplgroupp.exception.ActionException;
import ru.simplgroupp.exception.ResultType;
import ru.simplgroupp.exception.Type;
import ru.simplgroupp.interfaces.BusinessObjectResult;
import ru.simplgroupp.interfaces.IdentityQuestionLocal;
import ru.simplgroupp.interfaces.plugins.PluginSystemLocal;
import ru.simplgroupp.interfaces.plugins.identity.IdentityBeanLocal;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.util.ErrorKeys;
import ru.simplgroupp.util.ModelKeys;
import ru.simplgroupp.workflow.PluginExecutionContext;

import javax.ejb.*;
import javax.inject.Inject;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class IdentityBean extends AbstractPluginBean implements IdentityBeanLocal {
	
	@Inject Logger logger;

    @EJB
    private IdentityQuestionLocal identityQuestion;

    @EJB
	CreditRequestDAO creditRequestDAO;

    @Override
    public String getSystemName() {
        return SYSTEM_NAME;
    }

    @Override
    public String getSystemDescription() {
        return SYSTEM_DESCRIPTION;
    }

    @Override
    public boolean isFake() {
        return false;
    }

    @Override
    public EnumSet<PluginSystemLocal.Mode> getModesSupported() {
        return EnumSet.of(Mode.SINGLE);
    }

    @Override
    public EnumSet<PluginSystemLocal.ExecutionMode> getExecutionModesSupported() {
        return EnumSet.of(ExecutionMode.AUTOMATIC);
    }

    @Override
    public EnumSet<PluginSystemLocal.SyncMode> getSyncModesSupported() {
        return EnumSet.of(SyncMode.SYNC);
    }

    @Override
    public Set<String> getModelTargetsSupported() {
    	return Utils.setOf(ModelKeys.TARGET_CREDIT_DECISION);
    }

    @Override
    public String getBusinessObjectClass() {
        return CreditRequest.class.getName();
    }

    @Override
    public void executeSingle(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        
        Integer creditRequestId = Convertor.toInteger(businessObjectId);
        logger.info("!!!!! my plugin executed single - identity "+creditRequestId);
		
		try {
			
			IdentityPluginConfig pluginConfig=(IdentityPluginConfig) context.getPluginConfig();
			
			CreditRequest ccRequest = creditRequestDAO.getCreditRequest(creditRequestId, Utils.setOf());
			logger.info("Identity. Заявка " + creditRequestId + " проинициализирована.");
			//если нет сгенерированных вопросов, добавим 
			if (!identityQuestion.hasQuestions(ccRequest.getPeopleMain().getId())){
			    identityQuestion.getAndSaveQuestions(creditRequestId, pluginConfig.getQuestionAmount(), pluginConfig.getAnswerAmount(), pluginConfig.isUseFakeQuestion());
			}
			if (context.getNumRepeats() <=pluginConfig.getNumberRepeats()) {
			    if (!identityQuestion.isCompleteForPeople(creditRequestId)){
			    	logger.warning("Identity " + creditRequestId + " пока нет ответов на вопросы");
					throw new ActionException(ErrorKeys.TECH_ERROR,"пока нет ответов на вопросы, ждем...",Type.TECH, ResultType.NON_FATAL,null);
			    }
			} else {
				
				logger.severe("Identity " + creditRequestId + " нет ответа на вопросы после "+pluginConfig.getNumberRepeats()+" попыток.");
				throw new ActionException(ErrorKeys.TECH_ERROR,"нет ответа на вопросы после "+pluginConfig.getNumberRepeats()+" попыток.",Type.TECH, ResultType.FATAL,null);
			}
		} catch (ActionException e) {
			
			logger.severe("Identity. Заявка " + creditRequestId + " не обработана. Ошибка "+e+", "+e.getMessage());
			throw new ActionException("Произошла ошибка ",e);
		} catch (Throwable e) {
			
			logger.severe("Identity. Заявка " + creditRequestId + " не обработана. Ошибка "+e.getMessage());
			throw new ActionException("Произошла ошибка ",e);
		}
		
		logger.info("Identity. Заявка " + creditRequestId + " обработана.");
    }

    @Override
    public void addToPacket(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {

    }

    @Override
    public List<BusinessObjectResult> executePacket(PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public boolean sendSingleRequest(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        return false;
    }

    @Override
    public boolean querySingleResponse(String businessObjectClass, Object businessObjectId, PluginExecutionContext context) throws ActionException {
        return false;
    }

    @Override
    public List<BusinessObjectResult> sendPacketRequest(PluginExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public List<BusinessObjectResult> queryPacketResponse(PluginExecutionContext context) throws ActionException {
        return null;
    }
}
