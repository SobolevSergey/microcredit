package ru.simplgroupp.util;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import ru.simplgroupp.dao.interfaces.CreditRequestDAO;
import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.BaseCredit;
import ru.simplgroupp.transfer.CreditRequest;
import ru.simplgroupp.transfer.PeopleMain;

public class CreditRequestIteratorDB implements Iterator<CreditRequest> {

	private static final Logger logger = Logger.getLogger(CreditRequestIteratorDB.class.getName());
	
	private List<Integer> lstIds = null;
	private int curIdx = -1;
	private CreditRequestDAO crDAO;
	
	public CreditRequestIteratorDB(String crIds, CreditRequestDAO crDAO) {
		super();
		this.crDAO=crDAO;
		lstIds=Convertor.stringToIntegersList(crIds);
		curIdx = -1;
	}
	
	public int getCount() {
		return lstIds.size();
	}

	@Override
	public boolean hasNext() {
		return (lstIds.size() > 0 && curIdx < (lstIds.size()-1));
	}

	@Override
	public CreditRequest next() {
		if (lstIds.size() == 0 || curIdx >= lstIds.size()) {
			return null;
		}
		curIdx++;
		try {
			logger.info("Инициализируем заявку "+lstIds.get(curIdx));
			return crDAO.getCreditRequest(lstIds.get(curIdx), Utils.setOf(
	    			PeopleMain.Options.INIT_EMPLOYMENT, 
	       			PeopleMain.Options.INIT_PEOPLE_PERSONAL, 
	    			PeopleMain.Options.INIT_ADDRESS, 
	    			PeopleMain.Options.INIT_ACCOUNT, 
	    			PeopleMain.Options.INIT_NEGATIVE, 
	    			PeopleMain.Options.INIT_PEOPLE_CONTACT, 
	    			PeopleMain.Options.INIT_PEOPLE_MISC, 
	    			PeopleMain.Options.INIT_SPOUSE,
	    			PeopleMain.Options.INIT_DOCUMENT,
	    			PeopleMain.Options.INIT_CREDIT,
	    			PeopleMain.Options.INIT_SCORING,
	    			PeopleMain.Options.INIT_BLACKLIST,
	    			PeopleMain.Options.INIT_VERIF,
	    			PeopleMain.Options.INIT_DEBT,
	    			PeopleMain.Options.INIT_PHONESUMMARY,
	    			CreditRequest.Options.INIT_DEBT,
	    			CreditRequest.Options.INIT_DOCUMENT,
	    			CreditRequest.Options.INIT_SCORING, 
	    			CreditRequest.Options.INIT_CREDIT,
	    			CreditRequest.Options.INIT_CREDIT_HISTORY,
	    			CreditRequest.Options.INIT_NEGATIVE,
	    			CreditRequest.Options.INIT_SUMMARY,
	    			CreditRequest.Options.INIT_REQUESTS,
	    			CreditRequest.Options.INIT_VERIF,
	    			CreditRequest.Options.INIT_DEVICEINFO,
                    CreditRequest.Options.INIT_ANTIFRAUD,
                    CreditRequest.Options.INIT_ANTIFRAUD_SUSPICION,
                    BaseCredit.Options.INIT_PAYMENTS
	    	));
			
		} catch (Exception e) {
			logger.severe("Не удалось проинициализировать заявку "+lstIds.get(curIdx));
			return null;
		}
	}

	@Override
	public void remove() {

	}
		
}
