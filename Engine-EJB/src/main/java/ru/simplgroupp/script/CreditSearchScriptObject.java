package ru.simplgroupp.script;

import java.util.Collections;
import java.util.Map;

import ru.simplgroupp.interfaces.CreditBeanLocal;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.MoneyRange;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.Credit;
import ru.simplgroupp.util.SearchUtil;

public class CreditSearchScriptObject {

   private CreditBeanLocal creditBean;

    public CreditSearchScriptObject(CreditBeanLocal creditBean) {
        this.creditBean = creditBean;
    }

    public SearchResult<Credit> search(Object... params) {
        SearchResult<Credit> res = new SearchResult<Credit>();
        res.count = 0;
        res.data = Collections.emptyList();

        Map<String, Object> mpParams = Utils.mapOf(params);
        Integer peopleMainId = Convertor.toInteger(mpParams.get("peopleMain.id"));
        Integer accountTypeId = Convertor.toInteger(mpParams.get("accountType.code"));
        Integer partnerId = Convertor.toInteger(mpParams.get("partner.id"));
        DateRange creditDataBeg = SearchUtil.dateRangeFromMap(mpParams, "creditDatabeg");
        DateRange creditDataEnd = SearchUtil.dateRangeFromMap(mpParams, "creditDataend");
        Boolean isOver = Convertor.toBoolean(mpParams.get("isOver"));
        Integer creditType = Convertor.toInteger(mpParams.get("creditType.code"));
        String nomer = Convertor.toString(mpParams.get("nomer"));
        Boolean isSameOrg = Convertor.toBoolean(mpParams.get("isSameOrg"));
        DateRange creditDataEndFact = SearchUtil.dateRangeFromMap(mpParams, "creditDataendFact");
        Integer creditStatus = Convertor.toInteger(mpParams.get("creditStatus.code"));
        MoneyRange creditSum = SearchUtil.moneyRangeFromMap(mpParams, "creditSum");
        MoneyRange stake = SearchUtil.moneyRangeFromMap(mpParams, "stake");
        String surname = Convertor.toString(mpParams.get("surname"));
        Integer active = Convertor.toInteger(mpParams.get("active"));
        res.count = creditBean.countCredit(peopleMainId, accountTypeId, partnerId, creditDataBeg, creditDataEnd,
				creditType, isOver, nomer,isSameOrg, creditDataEndFact, creditStatus,creditSum, stake,surname,active);
        if (res.count==0){
        	return res;
        }
        creditBean.listCredit(0, -1, null, Utils.setOf(), peopleMainId, accountTypeId, partnerId, creditDataBeg, creditDataEnd,
				creditType, isOver, nomer,isSameOrg, creditDataEndFact, creditStatus,creditSum, stake,surname,active);
        return res;
    }
}
