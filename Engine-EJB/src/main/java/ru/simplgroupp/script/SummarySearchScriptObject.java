package ru.simplgroupp.script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.Summary;

public class SummarySearchScriptObject {

    public SearchResult<Summary> search(CreditRequest creditRequest, Object... params) {
        SearchResult<Summary> res = new SearchResult<>();
        res.count = 0;
        res.data = Collections.emptyList();
        if (creditRequest == null) {
            return res;
        }

        Map<String, Object> mpParams = Utils.mapOf(params);
        Object[] refCodes = findByPrefix(mpParams, "field.code");
        res.data = new ArrayList<>(creditRequest.getSummary().size());
        for (Summary sum : creditRequest.getSummary()) {
            if (mpParams.containsKey("partner.name")) {
                String pname = (String) mpParams.get("partner.name");
                if (pname != null && (!pname.equals(sum.getPartner().getName()))) {
                    continue;
                }
            }


            if (refCodes.length > 0) {
                if (Arrays.binarySearch(refCodes, sum.getFieldRef().getId()) < 0) {
                    continue;
                }
            }
            res.data.add(sum);
        }
        res.count = res.data.size();
        return res;
    }

    private static Object[] findByPrefix(Map<String, Object> source, String keyPrefix) {
        Object[] res = new Object[source.size()];
        int n = 0;
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            if (entry.getKey().startsWith(keyPrefix)) {
                res[n] = entry.getValue();
                n++;
            }
        }
        if (n == 0) {
            return new Object[]{};
        } else {
            return Arrays.copyOf(res, n);
        }
    }
}
