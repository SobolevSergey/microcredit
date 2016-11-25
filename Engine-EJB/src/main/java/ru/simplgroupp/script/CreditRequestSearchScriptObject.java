package ru.simplgroupp.script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import ru.simplgroupp.interfaces.KassaBeanLocal;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.IntegerRange;
import ru.simplgroupp.toolkit.common.MoneyRange;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.QuestionAnswer;
import ru.simplgroupp.util.SearchUtil;

public class CreditRequestSearchScriptObject {

    private KassaBeanLocal kassaBean;

    public CreditRequestSearchScriptObject(KassaBeanLocal kassaBean) {
        this.kassaBean = kassaBean;
    }
   
   
    
    /**
     * ищем в ответах на вопросы верификатора
     * @param ccRequest - заявка
     * @param params - параметры
     * @return
     */
    public SearchResult<QuestionAnswer> searchVerify(CreditRequest ccRequest, Object... params) {
        SearchResult<QuestionAnswer> res = new SearchResult<QuestionAnswer>();
        res.count = 0;
        res.data = Collections.emptyList();

        Map<String, Object> mpParams = Utils.mapOf(params);
        
        res.data = new ArrayList<>(ccRequest.getAnswers().size());
        for (QuestionAnswer qa: ccRequest.getAnswers()) {
        	if (mpParams.containsKey("question.code")) {
        		String code = Convertor.toString(mpParams.get("question.code"));
        		if (code != null) {
        			if (! qa.getQuestion().getQuestionCode().equals(code)) {
        				break;
        			}
        		}
        	}
        	
        	if (mpParams.containsKey("answer.value")) {
        		Object code = mpParams.get("answer.value");
        		if (code != null) {
        			if (! code.equals(qa.getAnswerValue())) {
        				break;
        			}
        		}
        	}        	
        	
        	res.data.add(qa);
        }

        res.count = res.data.size();
        return res;
    }    
    
    public SearchResult<CreditRequest> search(Object... params) {
        SearchResult<CreditRequest> res = new SearchResult<CreditRequest>();
        res.count = 0;
        res.data = Collections.emptyList();

        Map<String, Object> mpParams = Utils.mapOf(params);
        
        Integer peopleMainId = Convertor.toInteger(mpParams.get("peopleMain.id"));
        //Integer rejectReasonId = Convertor.toInteger(mpParams.get("rejectReason.reasonId.codeInteger"));
        Integer rejectReasonId =null;
        Integer statusId = Convertor.toInteger(mpParams.get("status.id"));
        Integer wayId = Convertor.toInteger(mpParams.get("way.code"));
        Integer accepted = null;
        String uniqueNomer = Convertor.toString(mpParams.get("uniqueNomer"));
        String peopleINN = Convertor.toString(mpParams.get("peopleMain.INN"));
        String peopleSNILS = Convertor.toString(mpParams.get("peopleMain.SNILS"));
        String peoplePhone = Convertor.toString(mpParams.get("peopleMain.contact.phone"));
        String peopleEmail = Convertor.toString(mpParams.get("peopleMain.contact.email"));
        DateRange dateContest = SearchUtil.dateRangeFromMap(mpParams, "dateContest");
        IntegerRange days = SearchUtil.intRangeFromMap(mpParams, "days");
        MoneyRange sum = SearchUtil.moneyRangeFromMap(mpParams, "sum");
        Boolean isover = Convertor.toBoolean(mpParams.get("isOver"));
        DateRange creditDateEnd = null;
        String peopleSurname = Convertor.toString(mpParams.get("peopleMain.personal.surname"));
        String peopleName = Convertor.toString(mpParams.get("peopleMain.personal.name"));
        String peopleMidname = Convertor.toString(mpParams.get("peopleMain.personal.midname"));
        Integer docTypeId = Convertor.toInteger(mpParams.get("peopleMain.document.type.id"));
        String docSeries = Convertor.toString(mpParams.get("peopleMain.document.series"));
        String docNomer = Convertor.toString(mpParams.get("peopleMain.document.nomer"));
        
        res.count = kassaBean.countCreditRequests(peopleMainId, rejectReasonId, statusId, accepted, dateContest, days, sum, isover, creditDateEnd, peopleSurname, peopleName, peopleMidname, docTypeId, docSeries, docNomer, 
        		peopleEmail, peoplePhone, peopleSNILS, peopleINN, uniqueNomer, null,wayId);
        if (res.count > 0) {
        	res.data = kassaBean.listCreditRequests(0, -1, null, null, peopleMainId, rejectReasonId, statusId, accepted, dateContest, days, sum, isover, creditDateEnd, peopleSurname, peopleName, 
        			peopleMidname, docTypeId, docSeries, docNomer, peopleEmail, peoplePhone, peopleSNILS, peopleINN, uniqueNomer, null,wayId);
        }
        return res;
    }
}
