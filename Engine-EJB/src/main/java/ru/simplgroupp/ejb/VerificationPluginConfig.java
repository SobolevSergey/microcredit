package ru.simplgroupp.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.simplgroupp.ejb.plugins.payment.ManualPluginConfig;
import ru.simplgroupp.interfaces.RuntimeServices;
import ru.simplgroupp.toolkit.common.SortCriteria;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.QAutoAnswer;
import ru.simplgroupp.transfer.Question;
import ru.simplgroupp.util.QuestionUtil;

public class VerificationPluginConfig extends ManualPluginConfig {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2228677149872131113L;
	protected List<QAutoAnswer> autoAnswers = new ArrayList<QAutoAnswer>(20);

	@Override
	public void load(String prefix, Map<String, Object> source) {
		super.load(prefix, source);

		for (Map.Entry<String, Object> entry: source.entrySet()) {
			if (entry.getKey().startsWith(prefix + "autoAnswer.")) {
				String answerKey = entry.getKey().replace(prefix + "autoAnswer.", "");				
				Object answerValue = entry.getValue();
				
				QAutoAnswer ans = QuestionUtil.findAutoAnswer(autoAnswers, answerKey);
				if (ans != null) {
					ans.setAnswerValue(answerValue);
				}
			}
		}
	}

	@Override
	public void save(String prefix, Map<String, Object> dest) {
		for (QAutoAnswer entry: autoAnswers ) {
			dest.put(prefix + "autoAnswer." + entry.getQuestionKey(), entry.getAnswerValue());
		}
		super.save(prefix, dest);
	}

	public List<QAutoAnswer> getAutoAnswers() {
		return autoAnswers;
	}

	@Override
	public void init(RuntimeServices runtimeServices) {
		super.init(runtimeServices);
		
		List<Question> lstQ =runtimeServices.getQuestionBean().listQuestion(-1, -1, new SortCriteria[] {new SortCriteria("QuestionCode", true)}, Utils.setOf(), null, null, null,null);
		for (Question quest: lstQ) {
			QAutoAnswer ans = new QAutoAnswer();
			ans.setQuestionKey(quest.getQuestionCode());
			ans.setQuestionText(quest.getQuestionText());
			ans.setAnswerType(quest.getAnswerType());
			
			autoAnswers.add(ans);
		}
	}

}
