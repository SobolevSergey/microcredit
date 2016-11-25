package ru.simplgroupp.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import ru.simplgroupp.interfaces.BusinessObjectRef;
import ru.simplgroupp.rules.AbstractContext;
import ru.simplgroupp.rules.CreditRequestContext;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Utils;
import ru.simplgroupp.transfer.*;

public class AppUtil {
	
	public static String bizNameToTableName(String bizClass) {
		if (CreditRequest.class.getName().equals(bizClass)) {
			return "creditrequest";
		}
		if (Credit.class.getName().equals(bizClass)) {
			return "credit";
		}
		if (PeopleMain.class.getName().equals(bizClass)) {
			return "peoplemain";
		}
		if (Payment.class.getName().equals(bizClass)) {
			return "payment";
		}
		if (Prolong.class.getName().equals(bizClass)) {
			return "prolong";
		}
		if (Refinance.class.getName().equals(bizClass)) {
			return "refinance";
		}
		// TODO
		return null;
	}

	public static String bizNameToEntityName(String bizClass) {
		if (bizClass.contains("Partner")){
			return bizClass.replace(".transfer.", ".persistence.") + "sEntity";
		} else if (bizClass.contains("Documents")){
			return bizClass.replace(".transfer.Documents", ".persistence.DocumentEntity");
		} 
		return bizClass.replace(".transfer.", ".persistence.") + "Entity";
	}
	
	public static String entityNameTobizName(String entClass) {
		return entClass.replace(".persistence.", ".transfer.").replace("Entity", "");
	}
	
	public static Constructor<? extends BaseTransfer> findConstructor(String bizClassName, String entityClassName) throws Exception {
		Class<? extends BaseTransfer> bizClass = (Class<? extends BaseTransfer>) Class.forName(bizClassName);
		Class entityClass = Class.forName(entityClassName);
		Constructor<? extends BaseTransfer> con = bizClass.getConstructor(entityClass);
		return con;
	}
	
	public static List wrapCollection(Constructor con, List source) throws Exception {
		ArrayList lstRes = new ArrayList(source.size());
		for( Object entity: source) {
			Object res = createBizObject(con, entity);			
			lstRes.add(res);
		}
		return lstRes;
	}
	
	public static <T> List<T> wrapCollectionSilent(Class clzSource, Class<T> clzTarget, List source) {
		Constructor con;
		try {
			con = clzTarget.getConstructor(clzSource);
		} catch (Exception e) {
			return null;
		} 
		if (con == null) {
			return null;
		}
		
		ArrayList lstRes = new ArrayList(source.size());
		for( Object entity: source) {
			try {
				Object res = createBizObject(con, entity);			
				lstRes.add(res);
			} catch (Exception ex) {
				
			}
		}
		return lstRes;
	}	
	
	public static Identifiable findById(Collection<? extends Identifiable> source, Integer id) {
		for (Identifiable aid: source) {
			if (aid.getId().equals(id)) {
				return aid;
			}
		}
		return null;
	}
	
	public static List extractIds(Collection<? extends Identifiable> source) {
		ArrayList lst = new ArrayList(source.size());
		for (Identifiable aid: source) {
			lst.add(aid.getId());
		}
		return lst;
	}
	
	public static <T> T wrapEntity(Constructor<T> con, Object entity) throws Exception {
		if (con == null) {
			return null;
		}
		T res = con.newInstance(entity);
		return res;
	}	
	
	public static <T> T createBizObject(Constructor<T> con, Object entity) throws Exception {
		T res = con.newInstance(entity);
		return res;
	}
	
	public static AbstractContext getDefaultContext(Object biz, String businessObjectClass) {
		CreditRequestContext ctx = new CreditRequestContext();
		ctx.setCurrentDate(new Date());
		if (biz == null) {
			return ctx;
		}
		
		if (CreditRequest.class.getName().equals(businessObjectClass)) {
			ctx.setCreditRequest((CreditRequest) biz);
			ctx.setCredit(ctx.getCreditRequest().getAcceptedCredit());
			ctx.setClient(ctx.getCreditRequest().getPeopleMain());
			if (ctx.getCredit() != null) {
				ctx.setProlong(ctx.getCredit().getDraftProlong());
				ctx.setRefinance(ctx.getCredit().getLastRefinance());
			}
		} else if (Credit.class.getName().equals(businessObjectClass)) {
			ctx.setCredit((Credit) biz);
			ctx.setCreditRequest(ctx.getCredit().getCreditRequest());
			ctx.setClient(ctx.getCredit().getPeopleMain());
			ctx.setProlong(ctx.getCredit().getDraftProlong());
			ctx.setRefinance(ctx.getCredit().getLastRefinance());
		} else if (PeopleMain.class.getName().equals(businessObjectClass)) {
			ctx.setClient((PeopleMain) biz);
			
			if (ctx.getClient().getCredits().size()>0){
				ctx.setCredit(ctx.getClient().getSystemCreditActive());
			}
		
			if (ctx.getCredit() != null) {
				ctx.setCreditRequest(ctx.getCredit().getCreditRequest());
				ctx.setProlong(ctx.getCredit().getDraftProlong());
				ctx.setRefinance(ctx.getCredit().getLastRefinance());				
			}
			
		} else if (Payment.class.getName().equals(businessObjectClass)) {
			ctx.setPayment( (Payment) biz );
			ctx.setCredit( ctx.getPayment().getCredit());
			ctx.setClient(ctx.getCredit().getPeopleMain());
			ctx.setCreditRequest(ctx.getCredit().getCreditRequest());
			ctx.setProlong(ctx.getCredit().getDraftProlong());
			ctx.setRefinance(ctx.getCredit().getLastRefinance());				
		} else if (Collector.class.getName().equals(businessObjectClass)) {
			ctx.setCollector((Collector) biz);
			ctx.setCredit(ctx.getCollector().getCredit());
			ctx.setClient(ctx.getCredit().getPeopleMain());
		} else if (Reference.class.getName().equals(businessObjectClass)) {
			ctx.setPaymentSystem((Reference) biz);
		} else if (Partner.class.getName().equals(businessObjectClass)){
			ctx.setPartner((Partner) biz);
		}
		return ctx;
	}
	
	public static Set getDefaultInitOptions(String businessObjectClass) {
		if (CreditRequest.class.getName().equals(businessObjectClass)) {
			return Utils.setOf(CreditRequest.Options.INIT_CREDIT, BaseCredit.Options.INIT_PROLONGS, PeopleMain.Options.INIT_PEOPLE_PERSONAL, PeopleMain.Options.INIT_PEOPLE_MISC,BaseCredit.Options.INIT_REFINANCES);
		} else if (Credit.class.getName().equals(businessObjectClass)) {
			return Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST, BaseCredit.Options.INIT_PROLONGS, PeopleMain.Options.INIT_PEOPLE_PERSONAL, PeopleMain.Options.INIT_PEOPLE_MISC,BaseCredit.Options.INIT_REFINANCES);
		} else if (PeopleMain.class.getName().equals(businessObjectClass)) {
			return Utils.setOf(PeopleMain.Options.INIT_CREDIT, PeopleMain.Options.INIT_CREDIT_REQUEST, PeopleMain.Options.INIT_PEOPLE_PERSONAL, PeopleMain.Options.INIT_PEOPLE_MISC);
		} else if (Payment.class.getName().equals(businessObjectClass)) {
			return Utils.setOf(BaseCredit.Options.INIT_CREDIT_REQUEST, BaseCredit.Options.INIT_PROLONGS, PeopleMain.Options.INIT_PEOPLE_PERSONAL, PeopleMain.Options.INIT_PEOPLE_MISC);
		}
		// TODO
		return Utils.setOf();
	}
	
	public static int calcAppActionHashCode(String Id) {
		return calcAppActionHashCode(Id, new BusinessObjectRef[0]);
	}	

	public static int calcAppActionHashCode(String Id, BusinessObjectRef[] bizRefs) {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Id == null) ? 0 : Id.hashCode());
		result = prime * result + Arrays.hashCode(bizRefs);
		return result;	
	}
}
