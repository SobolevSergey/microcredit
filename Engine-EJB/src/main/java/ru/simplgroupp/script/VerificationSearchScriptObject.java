package ru.simplgroupp.script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DoubleRange;
import ru.simplgroupp.toolkit.common.IntegerRange;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Verification;
import ru.simplgroupp.util.SearchUtil;

public class VerificationSearchScriptObject {

	 /**
     * ищем в верификации
     * @param people - человек
     * @param params - параметры поиска
     * @return
     */
    public SearchResult<Verification> search(CreditRequest ccRequest, Object... params ) {
    	SearchResult<Verification> res  = new SearchResult<>();
        res.count = 0;
        res.data = Collections.emptyList();     
        
        Map<String, Object> mpParams = Utils.mapOf(params);
        String partnerName = Convertor.toString( mpParams.get("partner.name") );
        Integer partnerId = Convertor.toInteger( mpParams.get("partner.id") );
        DoubleRange validationScore = SearchUtil.doubleRangeFromMap(mpParams, "validationScore");
        IntegerRange validationMark = SearchUtil.intRangeFromMap(mpParams, "validationMark");
        DoubleRange verificationScore = SearchUtil.doubleRangeFromMap(mpParams, "verificationScore");
        IntegerRange verificationMark = SearchUtil.intRangeFromMap(mpParams, "verificationMark");
        
        res.data = new ArrayList<>(ccRequest.getVerif().size());
        for (Verification ver: ccRequest.getVerif()) {
        	if (partnerName != null && ! partnerName.equals(ver.getPartner().getName())) {
        		continue;
        	}
        	if (partnerId != null && ! partnerId.equals(ver.getPartner().getId())) {
        		continue;
        	}
        	if (validationScore != null && ver.getValidationScore()==null){
        		continue;
        	}
        	if (validationScore != null  && validationScore.getFrom() != null && ! (ver.getValidationScore() >= validationScore.getFrom()) ) {
        		continue;
        	}
        	if (validationScore != null  && validationScore.getTo() != null && ! (ver.getValidationScore() <= validationScore.getTo()) ) {
        		continue;
        	}
        	if (verificationScore != null && ver.getVerificationScore()==null){
        		continue;
        	}
        	if (verificationScore != null && verificationScore.getFrom() != null && ! (ver.getVerificationScore() >= verificationScore.getFrom()) ) {
        		continue;
        	}
        	if (verificationScore != null && verificationScore.getTo() != null && ! (ver.getVerificationScore() <= verificationScore.getTo()) ) {
        		continue;
        	}  
        	if (validationMark != null && ver.getValidationMark()==null){
        		continue;
        	}
        	if (validationMark != null && validationMark.getFrom() != null && ! (ver.getValidationMark().getMarkMin() >= validationMark.getFrom()) ) {
        		continue;
        	}        	
        	if (validationMark != null && validationMark.getTo() != null && ! (ver.getValidationMark().getMarkMax() <= validationMark.getTo()) ) {
        		continue;
        	}   
        	if (verificationMark != null && ver.getVerificationMark()==null){
        		continue;
        	}
        	if (verificationMark != null && verificationMark.getFrom() != null && ! (ver.getVerificationMark().getMarkMin() >= verificationMark.getFrom()) ) {
        		continue;
        	}        	
        	if (verificationMark != null && verificationMark.getTo() != null && ! (ver.getVerificationMark().getMarkMax() <= verificationMark.getTo()) ) {
        		continue;
        	}         	
        
        	res.data.add(ver);
        }
        res.count = res.data.size();
        return res;        
    }
}
