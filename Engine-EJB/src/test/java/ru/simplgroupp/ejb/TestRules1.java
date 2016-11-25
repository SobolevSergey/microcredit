package ru.simplgroupp.ejb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import junit.framework.Assert;

import org.apache.commons.lang.time.DateUtils;
import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.StatelessKieSession;

import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.exception.KassaException;
import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.interfaces.CreditCalculatorBeanLocal;
import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.interfaces.ProductBeanLocal;
import ru.simplgroupp.interfaces.RuleSetBeanLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.rules.BooleanRuleResult;
import ru.simplgroupp.rules.CreditRequestContext;
import ru.simplgroupp.rules.NameValueRuleResult;
import ru.simplgroupp.rules.watched.FieldInfo;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Products;
import ru.simplgroupp.util.CollectorKeys;
import ru.simplgroupp.util.DatesUtils;
import ru.simplgroupp.util.ProductKeys;
import ru.simplgroupp.workflow.ProcessKeys;
import ru.simplgroupp.workflow.SignalRef;
import ru.simplgroupp.workflow.WorkflowObjectActionDef;

@LocalClient
public class TestRules1 {
	
	@EJB
	CreditDAO creditDAO;
	
    @EJB
    KassaBeanLocal kassa;	
    
    @EJB 
    RuleSetBeanLocal ruleSet;
    
    @EJB
    RulesBeanLocal rules;
    
    @EJB
    CreditBeanLocal creditBean;
    
    @EJB
    CreditCalculatorBeanLocal creditCalc;

    @EJB
    ProductBeanLocal productBean;
    
    @EJB
    CreditRequestDAO crDAO;
    
	@Before
	public void setUp() throws Exception {
		System.setProperty("javax.xml.bind.JAXBContext","com.sun.xml.internal.bind.v2.ContextFactory");
		
		final Properties p = new Properties();
		p.load(this.getClass().getResourceAsStream("/test.properties"));
        
        final Context context = EJBContainer.createEJBContainer(p).getContext();
        context.bind("inject", this);
        

        kassa = (KassaBeanLocal) context.lookup("java:global/Engine-EJB/KassaBean!ru.simplgroupp.interfaces.KassaBeanLocal");
        ruleSet = (RuleSetBeanLocal) context.lookup("java:global/Engine-EJB/RuleSetBean!ru.simplgroupp.interfaces.RuleSetBeanLocal");
        rules = (RulesBeanLocal) context.lookup("java:global/Engine-EJB/RulesBean!ru.simplgroupp.interfaces.RulesBeanLocal");
	}
	
//	@Test
	public void testEmpty()  throws Exception {
		Assert.assertNotNull(kassa);
		Assert.assertNotNull(ruleSet);
		
		StatelessKieSession ks = ruleSet.newWatchedFieldsSession();
	}
	
//	@Test
	public void testWatched() {
		List<FieldInfo> lst = rules.calcWatchedFields();
		Assert.assertNotNull(lst);
	}
	
//	@Test
	public void testNewReqConst() {
		Map<String, Object> mp =productBean.getNewRequestProductConfig(Products.PRODUCT_PDL);
		Assert.assertNotNull(mp);
		Assert.assertNotNull(mp.get(ProductKeys.CREDIT_SUM_MIN));
		Assert.assertNotNull(mp.get(ProductKeys.CREDIT_SUM_MAX));
	}
	
//	@Test
	public void testReturnEnabled() throws KassaException {
		int creditRequestId = 2944;
		CreditRequestContext context = new CreditRequestContext();
		CreditRequest req = crDAO.getCreditRequest(creditRequestId, Utils.setOf());
		context.setCreditRequest(req);
		context.setClient(req.getPeopleMain());
		
		BooleanRuleResult res = rules.canCompleteRequest(context);
		Assert.assertTrue(res.getResultValue());
	}
	
   // @Test
	public void testCalcInitial() {
				
		Map<String, Object> rest = creditCalc.calcCreditInitial(new Double(10000), new Double(0.01), new Integer(9), 1);
		Assert.assertNotNull(rest.get(CreditCalculatorBeanLocal.SUM_MAIN));
		Assert.assertNotNull(rest.get(CreditCalculatorBeanLocal.SUM_BACK));
		Assert.assertNotNull(rest.get(CreditCalculatorBeanLocal.SUM_PERCENT));
		for(Map.Entry<String, Object> param:rest.entrySet()){
    		System.out.println(param.getKey()+" "+param.getValue());
    	}
	}
	
	//@Test
	public void testRecompile() throws Exception {
		
	}
	
	//@Test
	public void testCalcInitial1() throws KassaException {
		int creditRequestId = 29504;
		CreditRequest req = crDAO.getCreditRequest(creditRequestId, Utils.setOf());
		Assert.assertNotNull(req);
		Map<String, Object> rest = creditCalc.calcCreditInitial(req);
		
		Assert.assertNotNull(rest.get(CreditCalculatorBeanLocal.SUM_MAIN));
		Assert.assertNotNull(rest.get(CreditCalculatorBeanLocal.SUM_BACK));
		Assert.assertNotNull(rest.get(CreditCalculatorBeanLocal.SUM_PERCENT));
		Assert.assertNotNull(rest.get(CreditCalculatorBeanLocal.DAYS_ACTUAL));
		Assert.assertNotNull(rest.get(CreditCalculatorBeanLocal.DATE_START_ACTUAL));
		
		for(Map.Entry<String, Object> param:rest.entrySet()){
    		System.out.println(param.getKey()+" "+param.getValue());
    	}
	}	
	
	//@Test
	public void testCalcInitialStake() {
		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put(ProductKeys.CREDIT_STAKE_MIN, new Double(0.015));
		params.put(ProductKeys.CREDIT_STAKE_MAX, new Double(0.03));
		
		NameValueRuleResult res = rules.calcInitialStake(5000, 20, params);
		
    	System.out.println(res.getValue(CreditCalculatorBeanLocal.STAKE_INITIAL));
    	
		Assert.assertNotNull(res.getValue(CreditCalculatorBeanLocal.STAKE_INITIAL));
	}	
	
//	@Test
	public void testCalcCreditData() throws Exception {
		
		Map<String, Object> limits1 =productBean.getNewRequestProductConfig(Products.PRODUCT_PDL);
//		NameValueRuleResult res1 = rules.calcInitialStake(5000, 20, limits1);		
				
		Map<String, Object> limits = productBean.getOverdueProductConfig(Products.PRODUCT_PDL);
		limits.putAll(limits1);
//		limits.put(CreditCalculatorBeanLocal.STAKE_INITIAL, res1.getValue(CreditCalculatorBeanLocal.STAKE_INITIAL));
		Credit credit = creditDAO.getCredit(767, Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST));
		Assert.assertNotNull(credit);
		
		NameValueRuleResult res = rules.calcCreditData(credit, new Date(), limits);
		Assert.assertNotNull(res.getValue(CreditCalculatorBeanLocal.INVERVAL_CURRENT));
		Assert.assertNotNull(res.getValue(CreditCalculatorBeanLocal.INVERVAL_START));
		Assert.assertNotNull(res.getValue(CreditCalculatorBeanLocal.DAYS_CURRENT_START));
		Assert.assertNotNull(res.getValue(CreditCalculatorBeanLocal.DAYS_CURRENT_END));
		Assert.assertNotNull(res.getValue(CreditCalculatorBeanLocal.DATE_CURRENT_START));
		Assert.assertNotNull(res.getValue(CreditCalculatorBeanLocal.DATE_CURRENT_END));
		Assert.assertNotNull(res.getValue(CreditCalculatorBeanLocal.SUM_MAIN));
		Assert.assertNotNull(res.getValue(CreditCalculatorBeanLocal.SUM_PERCENT));
		Assert.assertNotNull(res.getValue(CreditCalculatorBeanLocal.SUM_BACK));
		Assert.assertNotNull(res.getValue(CreditCalculatorBeanLocal.STAKE_CURRENT));
	}

	//@Test
	public void testCalcCollector() {
		Date pastDate = new Date(new Date().getTime() - (2 * 24 * 60 * 60 * 1000)); // 1 дня, 24 часа, 60 минут, 60 секунд, 1000 мл.сек

		Credit credit = new Credit();
		credit.setCreditDataEnd(pastDate);

		Map<String, Object> params = productBean.getCollectorProductConfig(Products.PRODUCT_PDL);
		params.put(CollectorKeys.IS_FIRST_DELAY, false);
		params.put(CollectorKeys.MAX_COLLECTOR_TYPE, CollectorKeys.AGENCY);

		NameValueRuleResult res = rules.calcCreditToCollector(credit, params);
		Assert.assertNotNull(res.getValue(CollectorKeys.COLLECTOR_TYPE));
		Assert.assertEquals(res.getValue(CollectorKeys.COLLECTOR_TYPE), CollectorKeys.HARD);

		params.put(CollectorKeys.MAX_COLLECTOR_TYPE, CollectorKeys.HARD);
		res = rules.calcCreditToCollector(credit, params);
		Assert.assertNotNull(res.getValue(CollectorKeys.COLLECTOR_TYPE));
		Assert.assertEquals(res.getValue(CollectorKeys.COLLECTOR_TYPE), CollectorKeys.HARD);

		params.put(CollectorKeys.IS_FIRST_DELAY, true);
		params.remove(CollectorKeys.MAX_COLLECTOR_TYPE);
		res = rules.calcCreditToCollector(credit, params);
		Assert.assertNotNull(res.getValue(CollectorKeys.COLLECTOR_TYPE));
		Assert.assertEquals(res.getValue(CollectorKeys.COLLECTOR_TYPE), CollectorKeys.SOFT);
	}
	
	//@Test
	public void testCalcRepayment() throws Exception{
		
		Credit credit = creditDAO.getCredit(29510, Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST));
		Assert.assertNotNull(credit);
		
		Map<String, Object> res ;
		res= creditCalc.calcCredit(credit, new Date());
		for(Map.Entry<String, Object> param:res.entrySet()){
    		System.out.println(param.getKey()+" "+param.getValue());
    	}
		res = creditCalc.calcCredit(credit, DateUtils.addDays(credit.getCreditDataEnd(),2));
		for(Map.Entry<String, Object> param:res.entrySet()){
    		System.out.println(param.getKey()+" "+param.getValue());
    	}
		/*res = creditCalc.calcCredit(credit, DateUtils.addDays(credit.getCreditDataEnd(),10));
		for(Map.Entry<String, Object> param:res.entrySet()){
    		System.out.println(param.getKey()+" "+param.getValue());
    	}
		res = creditCalc.calcCredit(credit, DateUtils.addDays(credit.getCreditDataEnd(),20));
		for(Map.Entry<String, Object> param:res.entrySet()){
    		System.out.println(param.getKey()+" "+param.getValue());
    	}
		res = creditCalc.calcCredit(credit, DateUtils.addDays(credit.getCreditDataEnd(),200));
		for(Map.Entry<String, Object> param:res.entrySet()){
    		System.out.println(param.getKey()+" "+param.getValue());
    	}*/
    	
	}
	
	//@Test
	public void testProlong() throws Exception {
		int creditRequestId = 601;
		CreditRequest req = crDAO.getCreditRequest(creditRequestId, Utils.setOf());
		Assert.assertNotNull(req);
		
		CreditRequestContext ctx = new CreditRequestContext(req.getPeopleMain(), req);
		ctx.setCredit(req.getAcceptedCredit());
		Map<String,Object>  limits = productBean.getProlongProductConfig(Products.PRODUCT_PDL);
			
		ctx.getParams().putAll(limits);
		limits = null;	
		
		BooleanRuleResult res = rules.getProcessActionEnabled(new SignalRef(ProcessKeys.DEF_CREDIT_REQUEST, null, ProcessKeys.MSG_PROLONG), ctx);
		Assert.assertNotNull(res);
	}
	
	//@Test
	public void testNewCR() throws Exception {
		int creditRequestId = 601;
		CreditRequest req = crDAO.getCreditRequest(creditRequestId, Utils.setOf());
		Assert.assertNotNull(req);
		
		CreditRequestContext ctx = new CreditRequestContext(req.getPeopleMain(), req);
		ctx.setCredit(req.getAcceptedCredit());
		
		WorkflowObjectActionDef actionDef = new WorkflowObjectActionDef();
		actionDef.setBusinessObjectClass("ru.simplgroupp.transfer.PeopleMain");
		actionDef.setSignalRef("msgAddCreditRequest");
		
		BooleanRuleResult res = rules.getObjectActionEnabled(actionDef, ctx);
		Assert.assertNotNull(res);
	}	
	
	@Test
	public void testCalcBonus() {
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put(CreditCalculatorBeanLocal.BONUS_PAY_PERCENT, new Double(0.3));
		params.put(CreditCalculatorBeanLocal.BONUS_PAY_MAX_SUM, new Double(3500));
		params.put(CreditCalculatorBeanLocal.BONUS_RATE, new Double(1));
		params.put(CreditCalculatorBeanLocal.BONUS_USE_PERCENT_ONLY, 0);
		
		NameValueRuleResult res = rules.calcBonusPaymentSum(new Double(15000), new Double(10000), params);
		
		System.out.println(res.getValue(CreditCalculatorBeanLocal.SUM_BONUS_MONEY_FOR_PAYMENT));
    	System.out.println(res.getValue(CreditCalculatorBeanLocal.SUM_BONUS_FOR_PAYMENT));
    	
    	
    	params.put(CreditCalculatorBeanLocal.BONUS_USE_PERCENT_ONLY, 1);
		
		res = rules.calcBonusPaymentSum(new Double(10000), new Double(10000), params);
		
		System.out.println(res.getValue(CreditCalculatorBeanLocal.SUM_BONUS_MONEY_FOR_PAYMENT));
    	System.out.println(res.getValue(CreditCalculatorBeanLocal.SUM_BONUS_FOR_PAYMENT));
    	
	}		

	//@Test
	public void testCalcParams(){
		Map<String, Object> limits =productBean.getNewRequestProductConfig(Products.PRODUCT_PDL);
		Map<String, Object> rest = creditCalc.calcCreditParams(14257, limits);
		for(Map.Entry<String, Object> param:rest.entrySet()){
    		System.out.println(param.getKey()+" "+param.getValue());
    	}
	}
	
	//@Test
	public void testCalcLongdays(){
		Credit credit=creditDAO.getCredit(24758, null);
		Date longDate=DatesUtils.makeDate(2015, 5, 24);
		Map<String, Object> rest = creditCalc.calcProlongDays(credit, longDate);
		for(Map.Entry<String, Object> param:rest.entrySet()){
    		System.out.println(param.getKey()+" "+param.getValue());
    	}
	}
}


