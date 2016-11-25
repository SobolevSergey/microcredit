package ru.simplgroupp.util;

import java.util.HashMap;
import java.util.Map;

import ru.simplgroupp.workflow.ProcessKeys;

public class PaymentUtils {

	public static Map<String,Object> paymentAsParams(Number nType, Number nSumId, Number nSumFrom, Number nSumTo ) {
		Map<String,Object> mp = new HashMap<String,Object>(5);
		
//		String payId = UUID.randomUUID().toString();
//		mp.put(ProcessKeys.VAR_PAYMENT_FORM + "." + payId, new Boolean(true));
		mp.put(ProcessKeys.VAR_PAYMENT_FORM + ".sumFrom", nSumFrom);
		mp.put(ProcessKeys.VAR_PAYMENT_FORM + ".sumTo", nSumTo);
		mp.put(ProcessKeys.VAR_PAYMENT_FORM + ".type", nType);
		mp.put(ProcessKeys.VAR_PAYMENT_FORM + ".sumId", nSumId);		
		return mp;
	}
}
