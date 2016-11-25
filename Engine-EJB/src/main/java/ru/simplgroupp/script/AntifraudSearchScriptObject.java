package ru.simplgroupp.script;

import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.AntifraudField;
import ru.simplgroupp.transfer.CreditRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class AntifraudSearchScriptObject {

    public SearchResult<AntifraudField> search(CreditRequest ccRequest, Object... params) {

        SearchResult<AntifraudField> res = new SearchResult<>();
        res.count = 0;
        res.data = Collections.emptyList();

        Map<String, Object> mpParams = Utils.mapOf(params);

        res.data = new ArrayList<>(ccRequest.getAntifraudFields().size());
        for (AntifraudField antifraud : ccRequest.getAntifraudFields()) {

            if (mpParams.containsKey("peopleMain.id")) {
                Integer peopleId = Convertor.toInteger(mpParams.get("peopleMain.id"));
                if (peopleId != null) {
                    if (!antifraud.getPeopleMain().getId().equals(peopleId)) {
                        continue;
                    }
                }
            }

            if (mpParams.containsKey("antifraudFieldType")) {
                Integer antifraudFieldTypeId = Convertor.toInteger(mpParams.get("antifraudFieldType"));
                if (antifraudFieldTypeId != null) {
                    if (!antifraud.getAntifraudFieldType().getId().equals(antifraudFieldTypeId)) {
                        continue;
                    }
                }
            }

            res.data.add(antifraud);
        }

        res.count = res.data.size();
        return res;
    }
}
