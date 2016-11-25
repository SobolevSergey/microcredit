package ru.simplgroupp.script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Scoring;
import ru.simplgroupp.util.CalcUtils;

public class ScoringSearchScriptObject {

	/**
	 * ищем данные по скорингу
	 * @param creditRequest - заявка
	 * @param params - параметры
	 * @return
	 */
    public SearchResult<Scoring> search(CreditRequest creditRequest, Object... params) {
        SearchResult<Scoring> res = new SearchResult<>();
        res.count = 0;
        res.data = Collections.emptyList();
        if (creditRequest == null) {
            return res;
        }

        Map<String, Object> mpParams = Utils.mapOf(params);
        res.data = new ArrayList<>(creditRequest.getScorings().size());
        for (Scoring scr : creditRequest.getScorings()) {
            if (mpParams.containsKey("partner.name")) {
                String pname = Convertor.toString(mpParams.get("partner.name"));
                if (pname != null && (!pname.equals(scr.getPartner().getName()))) {
                    continue;
                }
            }
            
            if (mpParams.containsKey("partner.id")) {
        		Integer partnerId = Convertor.toInteger(mpParams.get("partner.id"));
        		if (partnerId != null) {
        			if (! scr.getPartner().getId().equals(partnerId)) {
        				continue;
        			}
        		}
        	}
            
            if (mpParams.containsKey("score.from")) {
                Double df = Convertor.toDouble(mpParams.get("score.from"),CalcUtils.dformat_xx);
                if (df != null && !(scr.getScore() >= df)) {
                    continue;
                }
            }
            if (mpParams.containsKey("score.to")) {
            	Double df = Convertor.toDouble(mpParams.get("score.to"),CalcUtils.dformat_xx);
                if (df != null && !(scr.getScore() <= df)) {
                    continue;
                }
            }

            res.data.add(scr);
        }
        res.count = res.data.size();
        return res;
    }
}
