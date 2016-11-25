package ru.simplgroupp.script;

import java.util.Collections;
import java.util.Map;

import ru.simplgroupp.interfaces.service.EventLogService;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.DateRange;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.EventLog;
import ru.simplgroupp.util.SearchUtil;

public class EventLogSearchScriptObject {
	
	protected EventLogService eventLogServ;
	
	public EventLogSearchScriptObject(EventLogService serv) {
		eventLogServ = serv;
	}
	
	public SearchResult<EventLog> search(Object... params) {
    	SearchResult<EventLog> res  = new SearchResult<>();
        res.count = 0;
        res.data = Collections.emptyList();  		
        
        Map<String, Object> mpParams = Utils.mapOf(params);
        Integer userId = Convertor.toInteger( mpParams.get("user.id") );
        Integer roleId = Convertor.toInteger( mpParams.get("role.id") );
        String surname = Convertor.toString( mpParams.get("people.surname") );
        String crequestNumber = Convertor.toString( mpParams.get("creditRequest.uniqueNumber") );
        Integer crequestId = Convertor.toInteger( mpParams.get("creditRequest.id") );
        Integer eventCode = Convertor.toInteger( mpParams.get("eventCode") );
        String description = Convertor.toString( mpParams.get("description") );
        String os = Convertor.toString( mpParams.get("os") );
        String browser = Convertor.toString( mpParams.get("browser") );
        String geoPlace = Convertor.toString( mpParams.get("geoPlace") );
        DateRange dateEvent = SearchUtil.dateRangeFromMap(mpParams, "eventTime");
        Integer creditId = Convertor.toInteger( mpParams.get("credit.id") );
        String ipaddress = Convertor.toString( mpParams.get("ipaddress") );
        
        res.count = eventLogServ.countLogsTime(userId, roleId, null, surname, crequestNumber, crequestId, eventCode, description, os, browser, geoPlace, dateEvent, creditId, ipaddress);
        if (res.count == 0) {
            return res;
        }        
        
        res.data = eventLogServ.listLogsTime(0, -1, Utils.setOf(), userId, roleId, null, surname, crequestNumber, crequestId, eventCode, description, os, browser, geoPlace, dateEvent, creditId, ipaddress);
        return res;
	}
}
