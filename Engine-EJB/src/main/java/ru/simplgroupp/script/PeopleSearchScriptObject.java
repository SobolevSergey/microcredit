package ru.simplgroupp.script;

import ru.simplgroupp.interfaces.PeopleBeanLocal;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.DoubleRange;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BlackList;
import ru.simplgroupp.transfer.PeopleMain;
import ru.simplgroupp.transfer.Verification;
import ru.simplgroupp.util.SearchUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class PeopleSearchScriptObject {

    private PeopleBeanLocal peopleBean;
    public PeopleSearchScriptObject(PeopleBeanLocal peopleBean) {
        this.peopleBean=peopleBean;
    }
    
    /**
     * ищем в верификации
     * @param people - человек
     * @param params - параметры поиска
     * @return
     */
    public SearchResult<Verification> searchVerification(PeopleMain people, Object... params ) {
    	SearchResult<Verification> res  = new SearchResult<>();
        res.count = 0;
        res.data = Collections.emptyList();     
        
        Map<String, Object> mpParams = Utils.mapOf(params);
        String partnerName = Convertor.toString( mpParams.get("partner.name") );
        Integer partnerId = Convertor.toInteger( mpParams.get("partner.id") );
        DoubleRange validationScore = SearchUtil.doubleRangeFromMap(mpParams, "validationScore");
        DoubleRange validationMark = SearchUtil.doubleRangeFromMap(mpParams, "validationMark");
        DoubleRange verificationScore = SearchUtil.doubleRangeFromMap(mpParams, "verificationScore");
        DoubleRange verificationMark = SearchUtil.doubleRangeFromMap(mpParams, "verificationMark");
        
        res.data = new ArrayList<Verification>(people.getVerif().size());
        for (Verification ver: people.getVerif()) {
        	if (partnerName != null && ! partnerName.equals(ver.getPartner().getName())) {
        		continue;
        	}
        	if (partnerId != null && ! partnerId.equals(ver.getPartner().getId())) {
        		continue;
        	}
        	if (validationScore != null && ver.getValidationScore()==null){
        		continue;
        	}
        	if (validationScore != null && validationScore.getFrom() != null && ! (ver.getValidationScore() >= validationScore.getFrom()) ) {
        		continue;
        	}
        	if (validationScore != null && validationScore.getTo() != null && ! (ver.getValidationScore() <= validationScore.getTo()) ) {
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
    
    /**
     * ищем в черных списках
     * @param people - человек
     * @param params - параметры поиска
     * @return
     */
    public SearchResult<BlackList> searchBlackList(PeopleMain people, Object... params ) {
        SearchResult<BlackList> res = new SearchResult<>();
        res.count = 0;
        res.data = Collections.emptyList(); 
        
        Map<String, Object> mpParams = Utils.mapOf(params);
        
        DateRange dataBeg = SearchUtil.dateRangeFromMap(mpParams, "dataBeg");
        DateRange dataEnd = SearchUtil.dateRangeFromMap(mpParams, "dataEnd");
        Integer reasonCode = Convertor.toInteger(mpParams.get("reason.code"));
        
        res.data = new ArrayList<BlackList>(people.getBlackLists().size());
        for (BlackList blk : people.getBlackLists()) {
        	if (reasonCode != null && ! blk.getReason().getCodeInteger().equals(reasonCode)) {
        		continue;
        	}
        	if (dataEnd!=null&&blk.getDataEnd()==null){
        		continue;
        	}
        	if (dataEnd!=null&&dataEnd.getFrom()!=null&&!dataEnd.getFrom().before(blk.getDataEnd())){
        		continue;
        	}
        	if (dataEnd!=null&&dataEnd.getTo()!=null&&!dataEnd.getTo().after(blk.getDataEnd())){
        		continue;
        	}
        	if (dataBeg!=null&&blk.getDataBeg()==null){
        		continue;
        	}
        	if (dataBeg!=null&&dataBeg.getFrom()!=null&&!dataBeg.getFrom().after(blk.getDataBeg())){
        		continue;
        	}
        	if (dataBeg!=null&&dataBeg.getTo()!=null&&!dataBeg.getTo().before(blk.getDataBeg())){
        		continue;
        	}
        	res.data.add(blk);
        }
        res.count = res.data.size();
        return res;
    }

    /**
     * Поиск людей по условиям.
     *
     * @param params - условия парами: имя1, значение1, имя2, значение2, ... ,имяN, значениеN
     */
    public SearchResult<PeopleMain> search(Object... params) {
        SearchResult<PeopleMain> res = new SearchResult<>();
        res.count = 0;
        res.data = Collections.emptyList();

        Map<String, Object> mpParams = Utils.mapOf(params);

        DateRange dateBirth = SearchUtil.dateRangeFromMap(mpParams, "birthDate");
        String surname = Convertor.toString(mpParams.get("surname"));
        String name = Convertor.toString(mpParams.get("name"));
        String midname = Convertor.toString(mpParams.get("midname"));
        String paspNumber = Convertor.toString(mpParams.get("documents.21.number"));
        String paspSeries = Convertor.toString(mpParams.get("documents.21.series"));
        String inn = Convertor.toString(mpParams.get("inn"));
        String snils = Convertor.toString(mpParams.get("snils"));
        String phone=Convertor.toString(mpParams.get("contacts.phone"));
        String email=Convertor.toString(mpParams.get("contacts.email"));

        res.count = peopleBean.countPeopleMain(dateBirth, surname, name, midname, paspNumber, paspSeries, inn, snils,email,phone);
        if (res.count == 0) {
            return res;
        }
        res.data = peopleBean.listPeopleMain(0, -1, null, Utils.setOf(), dateBirth, surname, name, midname, paspNumber, paspSeries, inn, snils,email,phone);
        return res;
    }
}
