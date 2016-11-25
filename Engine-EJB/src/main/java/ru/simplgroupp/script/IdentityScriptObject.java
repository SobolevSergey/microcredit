package ru.simplgroupp.script;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.ejb.plugins.identity.IdentityPluginConfig;
import ru.simplgroupp.interfaces.IdentityQuestionLocal;

import java.io.Serializable;

public class IdentityScriptObject implements Serializable {
    private static final long serialVersionUID = -8993047285844825582L;
    protected transient int creditRequestId;
    protected transient IdentityPluginConfig pluginConfig;
    protected transient IdentityQuestionLocal questionBean;


    public IdentityScriptObject(int creditRequestId, PluginConfig pluginConfig, IdentityQuestionLocal questionBean) {
        super();
        this.creditRequestId = creditRequestId;
        this.pluginConfig = (IdentityPluginConfig) pluginConfig;
        this.questionBean = questionBean;
    }

    public void getQuestions() {
        questionBean.getQuestions(creditRequestId, pluginConfig.getQuestionAmount(), pluginConfig.getAnswerAmount(), pluginConfig.isUseFakeQuestion());
    }

    public void createQuestions() {
        questionBean.getAndSaveQuestions(creditRequestId, pluginConfig.getQuestionAmount(), pluginConfig.getAnswerAmount(), pluginConfig.isUseFakeQuestion());
    }
}
