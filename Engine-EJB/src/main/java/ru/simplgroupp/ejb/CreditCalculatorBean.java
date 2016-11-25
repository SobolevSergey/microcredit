package ru.simplgroupp.ejb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ru.simplgroupp.dao.interfaces.CreditDAO;
import ru.simplgroupp.dao.interfaces.PeopleDAO;
import ru.simplgroupp.dao.interfaces.ProductDAO;
import ru.simplgroupp.interfaces.CreditCalculatorBeanLocal;
import ru.simplgroupp.interfaces.ProductBeanLocal;
import ru.simplgroupp.interfaces.RulesBeanLocal;
import ru.simplgroupp.persistence.ProductConfigEntity;
import ru.simplgroupp.rules.NameValueRuleResult;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.util.ProductKeys;

/**
 * Кредитный калькулятор. Расчитывает параметры кредитов и оставшиеся суммы.
 */
@Stateless
public class CreditCalculatorBean implements CreditCalculatorBeanLocal {

    @EJB
    RulesBeanLocal rulesBean;

    @EJB
    CreditDAO creditDAO;

    @EJB
    PeopleDAO peopleDAO;
    
    @EJB
    ProductBeanLocal productBean;
    
    @EJB
    ProductDAO productDAO;
    
    @Override
    public Map<String, Object> calcCreditInitial(Double sumMain, Double stake, Integer days,Integer productId) {
        Map<String, Object> params = new HashMap<String, Object>(4);
        //запрашиваемая сумма
        params.put(CreditCalculatorBeanLocal.SUM_MAIN, (sumMain == null) ? 0 : sumMain);
        //ставка
        params.put(CreditCalculatorBeanLocal.STAKE_INITIAL, (stake == null) ? 0 : stake);
        //дней кредита
        params.put(CreditCalculatorBeanLocal.DAYS_INITIAL, (days == null) ? 0 : days);
        Integer addDay=0;
        if (productId!=null){
            ProductConfigEntity cfg=productDAO.findProductConfigActiveByName(productId,
        		ProductKeys.PRODUCT_CREDITREQUEST, ProductKeys.ADDITIONAL_DAYS_FOR_CALCULATE);
            addDay=Convertor.toInteger(cfg.getDataValue());
        }
        //доп.дней для расчета
        params.put(ProductKeys.ADDITIONAL_DAYS_FOR_CALCULATE, addDay);
        NameValueRuleResult res = rulesBean.calcCreditInitial(params);
        return res.getVariables();
    }

    @Override
    public Map<String, Object> calcCreditInitial(CreditRequest ccRequest) {
    	Integer productId=null;
    	Integer addDays=0;
    	if (ccRequest.getProduct()!=null){
    		productId=ccRequest.getProduct().getId();
    		ProductConfigEntity cfg=productDAO.findProductConfigActiveByName(productId,
    	    	ProductKeys.PRODUCT_CREDITREQUEST, ProductKeys.ADDITIONAL_DAYS_FOR_CALCULATE);
    	    addDays=Convertor.toInteger(cfg.getDataValue());
    	}
        NameValueRuleResult res = rulesBean.calcCreditInitial(ccRequest,addDays);
        return res.getVariables();
    }
    
    @Override
    public Map<String, Object> calcRefinance(int creditId, Date dateCalc) {
        Credit credit = creditDAO.getCredit(creditId, Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST));
        if (credit == null) {
        	return null;
        }
        return calcRefinance(credit, dateCalc);
    }    
    
    @Override
    public Map<String, Object> calcRefinance(Credit credit, Date dateCalc) {
    	
      	Map<String,Object> limits =productBean.getRefinanceProductConfig(credit.getProduct().getId());
        return limits;
    }    

    @Override
    public Map<String, Object> calcCredit(int creditId, Date dateCalc) {
        Credit credit = creditDAO.getCredit(creditId, Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST));
        if (credit == null) {
        	return null;
        }
        return calcCredit(credit, dateCalc);
    }

    @Override
    public Map<String, Object> calcCredit(Credit credit, Date dateCalc) {
    	
    	//константы для просрочки
    	Map<String,Object> limits = productBean.getOverdueProductConfig(credit.getProduct().getId());
    	//константы для нового запроса кредита
    	Map<String, Object> limits1 =productBean.getNewRequestProductConfig(credit.getProduct().getId());
    	limits.putAll(limits1);
    	NameValueRuleResult nres = rulesBean.calcCreditData(credit, dateCalc, limits);

        return nres.getVariables();
    }

    @Override
    public double calcInitialStake(double creditSum, int creditDays, Map<String, Object> limits) {
        NameValueRuleResult res = rulesBean.calcInitialStake(creditSum, creditDays, limits);
        return ((Double) res.getValue(CreditCalculatorBeanLocal.STAKE_INITIAL)).doubleValue();
    }

	@Override
	public Map<String, Object> calcCreditParams(Integer peopleId, Map<String, Object> limits) {
		PeopleMain peopleMain=peopleDAO.getPeopleMain(peopleId, Utils.setOf(PeopleMain.Options.INIT_CREDIT));
		Map<String, Object> params = new HashMap<String, Object>();
		//кол-во закрытых кредитов
		params.put(CreditCalculatorBeanLocal.CREDIT_CLOSED_COUNT, peopleMain.getReallyClosedCreditsCount());
		//константы для старта
		params.putAll(limits);
		//макс.сумма, если у человека нет закрытых кредитов
		NameValueRuleResult res = rulesBean.calcCreditParams(params);
		return res.getVariables();
	}

	@Override
	public Map<String, Object>  calcBonusPaymentSum(Double sumBack, Double bonusSum,
			Map<String, Object> limits) {
	   
	    NameValueRuleResult res = rulesBean.calcBonusPaymentSum(sumBack, bonusSum, limits);
	    return res.getVariables();
	}

/*	@Override
	public Map<String, Object>  calcBonusPaymentSum(Double sumBack, Double sumPercent,
			Double bonusSum,Map<String, Object> limits) {
		NameValueRuleResult res = rulesBean.calcBonusPaymentSum(sumBack, sumPercent,bonusSum, limits);
	    return res.getVariables();
	
	}*/
	
	@Override
	public Map<String, Object> calcProlongDays(Credit credit, Date longDate) {
		//константы для продления
    	Map<String,Object> limits = productBean.getProlongProductConfig(credit.getProduct().getId());
    	NameValueRuleResult nres = rulesBean.calcProlongDays(credit, longDate, limits);
        return nres.getVariables();
	}

	@Override
	public Map<String, Object> calcProlongDays(Integer creditId, Date longDate) {
		Credit credit = creditDAO.getCredit(creditId,null);
		if (credit!=null){
			return calcProlongDays(credit,longDate);
		}
		return null;
	}
}
