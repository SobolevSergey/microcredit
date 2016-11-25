package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.RefHeaderEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;
import ru.simplgroupp.toolkit.common.Utils;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RefHeader extends BaseTransfer<RefHeaderEntity> implements Serializable, Initializable, Identifiable {


    public static final int CREDIT_TYPE = 2;
    public static final int CURRENCY_TYPE = 3;
    public static final int INCOME_FREQ_TYPE = 4;
    public static final int MARITAL_STATUS_SYSTEM = 5;
    public static final int EMPLOY_TYPE = 6;
    public static final int PROFESSION_TYPE = 7;
    public static final int EDUCATION_TYPE = 9;
    public static final int REALTY_TYPE = 10;
    public static final int ORGANIZATION_BUSINESS = 11;
    public static final int VECTOR_TYPE = 12;
    public static final int CREDIT_STATUS_TYPE = 13;
    public static final int DOC_VER_TYPE = 14;
    public static final int REFUSE_TYPE = 15;
    public static final int CONTACT_TYPE = 16;
    public static final int ACCOUNT_TYPE = 17;
    public static final int ADDRESS_TYPE = 18;
    public static final int GENDER_TYPE = 20;
    public static final int CRONOS_REQ_TYPE = 21;
    public static final int CRONOS_SYS_TYPE = 23;
    public static final int MISMATCH_TYPE = 24;
    public static final int SPOUSE_TYPE = 27;
    public static final int SYSTEM_DOC_TYPE = 28;
    public static final int EXT_SALARY_TYPE = 29;
    public static final int CREDIT_TYPE_OKB = 30;
    public static final int CREDIT_PAYMENT_FREQ = 33;
    public static final int DOC_END_TYPE = 34;
    public static final int AUTHORITY_TYPE = 35;
    public static final int PAYMENT_TYPE = 36;
    public static final int CREDIT_OVERDUE_STATE = 37;
    public static final int CREDIT_RELATION_STATE = 38;
    public static final int ADDRESS_TYPE_OKB = 40;
    public static final int ADDRESS_TYPE_RS = 41;
    public static final int CREDIT_RELATION_STATE_EQUIFAX = 42;
    public static final int MARITAL_STATE_RS = 43;
    public static final int CREDIT_STATE_RS = 44;
    public static final int CREDIT_OVERDUE_STATE_OKB = 46;
    public static final int CURRENCY_TYPE_OKB = 47;
    public static final int CONTACT_TYPE_RS = 48;
    public static final int PHONE_PAY_DETAIL = 49;
    public static final int PHONE_PAY_SUMMARY_TYPE = 50;
    public static final int PHONE_PAY_TYPE = 51;
    public static final int PAY_SUM_TYPE = 52;
    public static final int PAY_STATUS_TYPE = 54;
    public static final int REFUSE_REASON_TYPE = 55;
    public static final int BLACKLIST_REASON_TYPE = 56;
    public static final int PEOPLE_SUM_OPERATION = 57;
    public static final int PEOPLE_SUM_OPERATION_TYPE = 58;
    public static final int ORGANIZATION_TYPE = 59;
    public static final int CREDIT_DETAIL_OPERATIONS = 60;

    public static final int PEOPLE_BEHAVIOR_GA_TYPE = 61;
    public static final int PEOPLE_BEHAVIOR_TYPE = 62;
    public static final int OFFICIAL_DOCUMENT_TYPE = 63;
    public static final int CREDIT_SUMS = 64;
    public static final int INCAPACITY_TYPE = 65;
    public static final int APPLICATION_WAY = 66;
    public static final int APPLICATION_WAY_RS = 67;
    public static final int APPLICATION_WAY_OKB = 68;
    public static final int EXECUTION_WAY = 69;
    public static final int MESSAGE_TYPE = 70;
    public static final int PRODUCT_CONFIG_TYPE = 71;
    public static final int PAYMENT_PURPOSE = 72;

    public static final int CREDIT_PURPOSE = 73;
    public static final int SYSTEM_FEATURE = 74;
    public static final int CREDIT_TYPE_NBKI = 75;
    public static final int CONTACT_TYPE_NBKI = 77;
    public static final int CREDIT_STATUS_NBKI = 78;
    public static final int COLLECTOR_TYPE = 79;
    public static final int DOCUMENT_SCAN_TYPE = 80;
    public static final int INCAPACITY_TYPE_RS = 81;
    public static final int CALLBACK_CLIENT_TYPE = 82;
    public static final int CALL_STATUS = 83;
    public static final int CALL_RESULT_TYPE = 84;
    public static final int TASK_TYPE = 85;
    public static final int PENSDOC_TYPES = 86;
    public static final int SOURCE_BLACKLIST_TYPES = 87;
    public static final int TIME_RANGE = 88;
    public static final int HUNTER_OBJECTS = 89;
    public static final int ANTIFRAUD_STATUSES = 90;
    public static final int CREDIT_TYPE_RS = 91;
    public static final int VERIFIER_QUESTION_TYPES = 92;

    private static final long serialVersionUID = 2900307943998666659L;
    protected static Constructor<? extends BaseTransfer> wrapConstructor;

    static {
        try {
            wrapConstructor = RefHeader.class.getConstructor(RefHeaderEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    private Partner partners;
    private List<Reference> references;

    public RefHeader(RefHeaderEntity value) {
        super(value);
        partners = new Partner(entity.getPartnersId());
        references = new ArrayList<Reference>(0);
    }
    public RefHeader() {
        super();
        entity = new RefHeaderEntity();
    }

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    public Integer getId() {
        return entity.getId();
    }

    @XmlElement
    public void setId(Integer id) {
        entity.setId(id);
    }

    public String getName() {
        return entity.getName();
    }

    @XmlElement
    public void setName(String name) {
        entity.setName(name);
    }

    public Partner getPartners() {
        return partners;
    }

    @XmlElement
    public void setPartners(Partner partners) {
        this.partners = partners;
    }

    public List<Reference> getReferences() {
        return references;
    }

    @Override
    public void init(Set options) {
        Utils.initCollection(getReferences(), options, Options.INIT_REFERENCE);
    }

    public enum Options {
        INIT_REFERENCE;
    }

}

