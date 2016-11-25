package ru.simplgroupp.script;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.IdentityQuestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class IdentitySearchScriptObject {
    public SearchResult<IdentityQuestion> search(CreditRequest creditRequest, Object... params) {
        SearchResult<IdentityQuestion> result = new SearchResult<>();

        result.count = 0;
        result.data = Collections.emptyList();

        Map<String, Object> mpParams = Utils.mapOf(params);

        result.data = new ArrayList<>(creditRequest.getDebts().size());
        for (IdentityQuestion question : creditRequest.getIdentityQuestions()) {
            if (mpParams.containsKey("peopleMain.id")) {
                Integer peopleId = Convertor.toInteger(mpParams.get("peopleMain.id"));
                if (peopleId != null) {
                    if (!question.getPeopleMain().getId().equals(peopleId)) {
                        continue;
                    }
                }
            }

            result.data.add(question);
        }

        result.count = result.data.size();
        return result;
    }
}
