package ru.simplgroupp.script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Debt;

public class DebtSearchScriptObject {

	/**
     * ищем в долгах
     * @param ccRequest - заявка
     * @param params - параметры
     * @return
     */
    public SearchResult<Debt> search(CreditRequest ccRequest, Object... params) {
        SearchResult<Debt> res = new SearchResult<Debt>();
        res.count = 0;
        res.data = Collections.emptyList();

        Map<String, Object> mpParams = Utils.mapOf(params);
        
        res.data = new ArrayList<>(ccRequest.getDebts().size());
        for (Debt debt: ccRequest.getDebts()) {
        	
        	if (mpParams.containsKey("peopleMain.id")) {
        		Integer peopleId = Convertor.toInteger(mpParams.get("peopleMain.id"));
        		if (peopleId != null) {
        			if (! debt.getPeopleMain().getId().equals(peopleId)) {
        				continue;
        			}
        		}
        	}
        	
        	if (mpParams.containsKey("partner.id")) {
        		Integer partnerId = Convertor.toInteger(mpParams.get("partner.id"));
        		if (partnerId != null) {
        			if (! debt.getPartner().getId().equals(partnerId)) {
        				continue;
        			}
        		}
        	}
        	
        	if (mpParams.containsKey("authority.code")) {
        		Integer authorityId = Convertor.toInteger(mpParams.get("authority.code"));
        		if (authorityId != null) {
        			if (! debt.getAuthorityId().getCodeInteger().equals(authorityId)) {
        				continue;
        			}
        		}
        	}        	
        	
        	res.data.add(debt);
        }

        res.count = res.data.size();
        return res;
    }    
    
   
}
