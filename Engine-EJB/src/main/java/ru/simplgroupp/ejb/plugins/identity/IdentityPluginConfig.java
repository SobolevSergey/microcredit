package ru.simplgroupp.ejb.plugins.identity;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;

import java.util.Map;

public class IdentityPluginConfig extends PluginConfig {
    private static final long serialVersionUID = 8993744828989105582L;

    /**
     * количество вопросов 
     */
    private Integer questionAmount = 5;

    /**
     * количество ответов
     */
    private Integer answerAmount = 5;

    /**
     * использовать фейковые вопросы
     */
    private boolean useFakeQuestion = false;

    /**
     * количество фейковых вопросов
     */
    private Integer fakeQuestionAmount = 5;
    /**
	 * сколько повторов при нефатальной ошибке
	 */
	protected int numberRepeats=200;

    public Integer getQuestionAmount() {
        return questionAmount;
    }

    public void setQuestionAmount(Integer questionAmount) {
        this.questionAmount = questionAmount;
    }

    public Integer getAnswerAmount() {
        return answerAmount;
    }

    public void setAnswerAmount(Integer answerAmount) {
        this.answerAmount = answerAmount;
    }

    public boolean isUseFakeQuestion() {
        return useFakeQuestion;
    }

    public void setUseFakeQuestion(boolean useFakeQuestion) {
        this.useFakeQuestion = useFakeQuestion;
    }

    public Integer getFakeQuestionAmount() {
        return fakeQuestionAmount;
    }

    public void setFakeQuestionAmount(Integer fakeQuestionAmount) {
        this.fakeQuestionAmount = fakeQuestionAmount;
    }

    public int getNumberRepeats() {
		return numberRepeats;
	}

	public void setNumberRepeats(int numberRepeats) {
		this.numberRepeats = numberRepeats;
	}

	@Override
    public void load(String prefix, Map<String, Object> source) {
        super.load(prefix, source);

        questionAmount = Convertor.toInteger(source.get(prefix + ".questionAmount"));
        answerAmount = Convertor.toInteger(source.get(prefix + ".answerAmount"));
        fakeQuestionAmount = Convertor.toInteger(source.get(prefix + ".fakeQuestionAmount"));
        numberRepeats=Convertor.toInteger(source.get(prefix + ".numberRepeats"));
        this.useFakeQuestion = Utils.booleanFromNumber(source.get(prefix + ".useFakeQuestion"));
    }

    @Override
    public void save(String prefix, Map<String, Object> dest) {
        dest.put(prefix + ".questionAmount", questionAmount);
        dest.put(prefix + ".answerAmount", answerAmount);
        dest.put(prefix + ".fakeQuestionAmount", fakeQuestionAmount);
        dest.put(prefix + ".useFakeQuestion", (useFakeQuestion) ? 1 : 0);
        dest.put(prefix + ".numberRepeats", numberRepeats);
        super.save(prefix, dest);
    }
}
