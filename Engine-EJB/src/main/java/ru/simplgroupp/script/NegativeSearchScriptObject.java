package ru.simplgroupp.script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Negative;

public class NegativeSearchScriptObject {

	 /**
     * ищем негатив
     * @param ccRequest - заявка
     * @param params - параметры
     * @return
     */
    public SearchResult<Negative> search(CreditRequest ccRequest, Object... params) {
        SearchResult<Negative> res = new SearchResult<Negative>();
        res.count = 0;
        res.data = Collections.emptyList();

        Map<String, Object> mpParams = Utils.mapOf(params);
        
        res.data = new ArrayList<>(ccRequest.getNegatives().size());
        for (Negative neg: ccRequest.getNegatives()) {
        	if (mpParams.containsKey("peopleMain.id")) {
        		Integer peopleId = Convertor.toInteger(mpParams.get("peopleMain.id"));
        		if (peopleId != null) {
        			if (! neg.getPeopleMain().getId().equals(peopleId)) {
        				continue;
        			}
        		}
        	}
        	
        	if (mpParams.containsKey("partner.id")) {
        		Integer peopleId = Convertor.toInteger(mpParams.get("partner.id"));
        		if (peopleId != null) {
        			if (! neg.getPartner().getId().equals(peopleId)) {
        				continue;
        			}
        		}
        	}
        	
        	if (mpParams.containsKey("partner.name")) {
        		String peopleId = Convertor.toString(mpParams.get("partner.name"));
        		if (peopleId != null) {
        			if (! neg.getPartner().getName().equals(peopleId)) {
        				continue;
        			}
        		}
        	} 
        	
        	if (mpParams.containsKey("article.name")) {
        		String peopleId = Convertor.toString(mpParams.get("article.name"));
        		if (peopleId != null) {
        			if (! neg.getArticleId().getName().contains(peopleId)) {
        				continue;
        			}
        		}
        	}
        	
        	if (mpParams.containsKey("article.article")) {
        		String peopleId = Convertor.toString(mpParams.get("article.article"));
        		if (peopleId != null) {
        			if (! neg.getArticleId().getArticle().equals(peopleId)) {
        				continue;
        			}
        		}
        	}     
        	
               	
        	if (mpParams.containsKey("article.groupId")) {
        		String peopleId = Convertor.toString(mpParams.get("article.groupId"));
        		if (peopleId != null) {
        			if (! neg.getArticleId().getGroupId().equals(peopleId)) {
        				continue;
        			}
        		}
        	} 
        	
        	if (mpParams.containsKey("article.subGroupId")) {
        		String peopleId = Convertor.toString(mpParams.get("article.subGroupId"));
        		if (peopleId != null) {
        			if (! neg.getArticleId().getSubgroupId().equals(peopleId)) {
        				continue;
        			}
        		}
        	}   
        	
        	if (mpParams.containsKey("article.lasts")) {
        		String peopleId = Convertor.toString(mpParams.get("article.lasts"));
        		if (peopleId != null) {
        			if (! neg.getArticleId().getLasts().equals(peopleId)) {
        				continue;
        			}
        		}
        	}         	
        	
        	res.data.add(neg);
        }

        res.count = res.data.size();
        return res;
    }      
}
