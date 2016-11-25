package ru.simplgroupp.ejb.plugins.payment;

import java.util.Map;

import ru.simplgroupp.ejb.PluginConfig;
import ru.simplgroupp.toolkit.common.Utils;

/**
 * Настройки плугина AlfaBankMarengoBean
 */
public class AlfaBankMarengoPluginConfig extends PluginConfig {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String testOrderURL;

    protected String workOrderURL;

    protected String testStatementURL;

    protected String workStatementURL;

    protected String workAccount;
    protected String workInn;
    protected String workOrganization;
    protected String workBik;

    protected String testAccount;
    protected String testInn;
    protected String testOrganization;
    protected String testBik;

    protected boolean useWork = false;

    @Override
    public void load(String prefix, Map<String, Object> source) {
        super.load(prefix, source);
        testOrderURL = (String) source.get(prefix + ".testOrderURL");
        workOrderURL = (String) source.get(prefix + ".workOrderURL");
        testStatementURL = (String) source.get(prefix + ".testStatementURL");
        workStatementURL = (String) source.get(prefix + ".workStatementURL");

        workAccount = (String) source.get(prefix + ".workAccount");
        workInn = (String) source.get(prefix + ".workInn");
        workOrganization = (String) source.get(prefix + ".workOrganization");
        workBik = (String) source.get(prefix + ".workBik");

        testAccount = (String) source.get(prefix + ".testAccount");
        testInn = (String) source.get(prefix + ".testInn");
        testOrganization = (String) source.get(prefix + ".testOrganization");
        testBik = (String) source.get(prefix + ".testBik");

        useWork=Utils.booleanFromNumber(source.get(prefix + ".useWork"));
    }

    @Override
    public void save(String prefix, Map<String, Object> dest) {
        dest.put(prefix + ".testOrderURL", testOrderURL);
        dest.put(prefix + ".workOrderURL", workOrderURL);
        dest.put(prefix + ".testStatementURL", testStatementURL);
        dest.put(prefix + ".workStatementURL", workStatementURL);
        dest.put(prefix + ".useWork", useWork ? 1 : 0);
        dest.put(prefix + ".workAccount", workAccount);
        dest.put(prefix + ".workInn", workInn);
        dest.put(prefix + ".workOrganization", workOrganization);
        dest.put(prefix + ".workBik", workBik);

        dest.put(prefix + ".testAccount", testAccount);
        dest.put(prefix + ".testInn", testInn);
        dest.put(prefix + ".testOrganization", testOrganization);
        dest.put(prefix + ".testBik", testBik);

        super.save(prefix, dest);
    }

    public String getTestOrderURL() {
        return testOrderURL;
    }

    public void setTestOrderURL(String testOrderURL) {
        this.testOrderURL = testOrderURL;
    }

    public String getWorkOrderURL() {
        return workOrderURL;
    }

    public void setWorkOrderURL(String workOrderURL) {
        this.workOrderURL = workOrderURL;
    }

    public String getTestStatementURL() {
        return testStatementURL;
    }

    public void setTestStatementURL(String testStatementURL) {
        this.testStatementURL = testStatementURL;
    }

    public String getWorkStatementURL() {
        return workStatementURL;
    }

    public void setWorkStatementURL(String workStatementURL) {
        this.workStatementURL = workStatementURL;
    }

    public String getWorkAccount() {
        return workAccount;
    }

    public void setWorkAccount(String workAccount) {
        this.workAccount = workAccount;
    }

    public String getWorkInn() {
        return workInn;
    }

    public void setWorkInn(String workInn) {
        this.workInn = workInn;
    }

    public String getWorkOrganization() {
        return workOrganization;
    }

    public void setWorkOrganization(String workOrganization) {
        this.workOrganization = workOrganization;
    }

    public String getWorkBik() {
        return workBik;
    }

    public void setWorkBik(String workBik) {
        this.workBik = workBik;
    }

    public String getTestAccount() {
        return testAccount;
    }

    public void setTestAccount(String testAccount) {
        this.testAccount = testAccount;
    }

    public String getTestInn() {
        return testInn;
    }

    public void setTestInn(String testInn) {
        this.testInn = testInn;
    }

    public String getTestOrganization() {
        return testOrganization;
    }

    public void setTestOrganization(String testOrganization) {
        this.testOrganization = testOrganization;
    }

    public String getTestBik() {
        return testBik;
    }

    public void setTestBik(String testBik) {
        this.testBik = testBik;
    }

    public boolean isUseWork() {
        return useWork;
    }

    public void setUseWork(boolean useWork) {
        this.useWork = useWork;
    }
}
