package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.*;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

public class IdentityQuestion extends BaseTransfer<IdentityQuestionEntity> implements Serializable, Initializable, Identifiable {
    private static final long serialVersionUID = 3180894000424079359L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = IdentityQuestion.class.getConstructor(IdentityQuestionEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }


    private IdentityTemplate template;

    private Credit credit;

    private CreditRequest creditRequest;

    private PeopleMain peopleMain;

    private List<IdentityOption> options;

    private IdentityOption answer;


    public IdentityQuestion() {
        super();
        entity = new IdentityQuestionEntity();
    }

    public IdentityQuestion(IdentityQuestionEntity value) {
        super(value);

        template = new IdentityTemplate(entity.getTemplate());
        if (entity.getCredit() != null) {
            credit = new Credit(entity.getCredit());
        }
        creditRequest = new CreditRequest(entity.getCreditRequest());
        peopleMain = new PeopleMain(entity.getPeopleMain());
        options = new ArrayList<>();

    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    @Override
    public void init(Set options) {
        // варианты ответов
        if (options != null && options.contains(Options.INIT_IDENTITY_OPTIONS)) {
            Utils.initCollection(entity.getOptions(), options);
            for (IdentityOptionEntity optionEntity : entity.getOptions()) {
                IdentityOption option = new IdentityOption(optionEntity);
                option.init(options);
                this.options.add(option);

                if (option.getAnswer() != null) {
                    answer = option;
                }
            }

            // сортировка по id, больше ее никуда не вставишь и удобней не сделаешь
            Collections.sort(this.options, new Comparator<IdentityOption>() {
                @Override
                public int compare(IdentityOption o1, IdentityOption o2) {
                    return o1.getId() - o2.getId();
                }
            });
        }
    }
    
    public IdentityTemplate getTemplate() {
        return template;
    }

    public void setTemplate(IdentityTemplate template) {
        this.template = template;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public CreditRequest getCreditRequest() {
        return creditRequest;
    }

    public void setCreditRequest(CreditRequest creditRequest) {
        this.creditRequest = creditRequest;
    }

    public PeopleMain getPeopleMain() {
        return peopleMain;
    }

    public void setPeopleMain(PeopleMain peopleMain) {
        this.peopleMain = peopleMain;
    }

    public List<IdentityOption> getOptions() {
        return options;
    }

    public void setOptions(List<IdentityOption> options) {
        this.options = options;
    }

    public Integer getId() {
        return entity.getId();
    }

    public void setId(Integer id) {
        entity.setId(id);
    }

    public String getValue() {
        return entity.getValue();
    }

    public void setValue(String value) {
        entity.setValue(value);
    }

    public Boolean getCorrect() {
        if (answer != null) {
            return answer.getCorrect();
        }

        return null;
    }

    public IdentityOption getAnswer() {
        return answer;
    }

    public enum Options {
        INIT_IDENTITY_OPTIONS;
    }
}

