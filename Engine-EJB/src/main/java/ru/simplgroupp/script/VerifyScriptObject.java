package ru.simplgroupp.script;

import ru.simplgroupp.ejb.ActionContext;
import ru.simplgroupp.interfaces.QuestionBeanLocal;
import ru.simplgroupp.interfaces.VerificationBeanLocal;
import ru.simplgroupp.persistence.QuestionAnswerEntity;
import ru.simplgroupp.persistence.QuestionVariantEntity;
import ru.simplgroupp.transfer.QuestionAnswer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class VerifyScriptObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6674571169392587382L;
	protected transient int creditRequestId;
	protected transient VerificationBeanLocal verifyBean;
	protected Map<String, Question> questions;
	protected transient ActionContext actionContext;
	protected transient QuestionBeanLocal questBean;
    
    public Map<String, Question> getQuestions() {
    	return questions;
    }

    public VerifyScriptObject(int creditRequestId, VerificationBeanLocal vBean, QuestionBeanLocal quest, ActionContext context) {
        super();
        this.creditRequestId = creditRequestId;
        verifyBean = vBean;
        questions = new HashMap<>(10);
        actionContext = context;
        questBean = quest;
    }

    public Question question(String key) {
        Question quest = questions.get(key);
        if (quest == null) {
            quest = new Question(key);
            questions.put(key, quest);
        }
        return quest;
    }

    public class Question {

        String key;
        QuestionAnswerEntity qaEnt;

        Question(String skey) {
            key = skey;
            qaEnt = verifyBean.getQA(creditRequestId, key, actionContext);
        }

        public Integer status() {
            if (qaEnt == null) {
                return null;
            } else {
                return qaEnt.getAnswerStatus();
            }
        }

        public void add() {
            if (qaEnt == null) {
                qaEnt = verifyBean.addQA(creditRequestId, key, actionContext);
            }
        }

        public void remove() {
            if (qaEnt != null) {
                verifyBean.removeQA(creditRequestId, key, actionContext);
                qaEnt = null;
            }
        }

        public Object answer() {
            if (qaEnt == null) {
                return null;
            } else if (qaEnt.getAnswerStatus() == QuestionAnswer.ANSWER_STATUS_ANSWERED) {
            	Object res = qaEnt.getAnswerValue();
            	if (qaEnt.getAnswerValueRef() != null) {
            		// взять значение из справочника
            		QuestionVariantEntity ent = questBean.getQuestionVariant(qaEnt.getAnswerValueRef());
            		if (ent != null) {
            			return ent.getAnswerValue();
            		}
            	}
                return res;
            } else {
                return null;
            }
        }
    }
}
