package ru.simplgroupp.fias.ejb;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import ru.simplgroupp.fias.persistence.ActualStatus;
import ru.simplgroupp.fias.persistence.AddrObj;
import ru.simplgroupp.fias.persistence.AddrObjType;
import ru.simplgroupp.fias.persistence.IAddress;
import ru.simplgroupp.fias.persistence.Journal;
import ru.simplgroupp.fias.persistence.Level;
import ru.simplgroupp.fias.persistence.LevelRecord;


import ru.simplgroupp.toolkit.common.Convertor;

import org.apache.commons.lang.StringUtils;

import com.theorem.misc.dbase.DBase;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class FiasService implements IFIASService 
{

	public static final String SQL_DEL_SOCRBASE = "delete from socrbase";
	public static final String SQL_DEL_ADDROBJ = "delete from addrobj";
	
	private Logger log = Logger.getLogger(FiasService.class.getName());	
	private String dformat="yyyy-MM-dd HH:mm:ss.S";

    @PersistenceContext(unitName="FiasPU")
    protected EntityManager em;
    
    @Resource
    private SessionContext context;
    
    public Journal findOperationInProgress() {
    	String sql = "from ru.simplgroupp.fias.persistence.Journal where (status = 1) order by dateStart desc";
    	Query qry = em.createQuery(sql); 
    	List<Journal> lst = qry.getResultList();
    	if (lst == null || lst.size() == 0 || lst.get(0) == null)
    		return null;
    	else
    		return lst.get(0);
    }
    
    /**
     * Выдаём одну запись по GUID записи.
     * @param aoid
     * @return
     */
    public AddrObj findAddressObject(String aoid) {
    	AddrObj ao = em.find(AddrObj.class, aoid);
    	return ao;
    }
    

    /**
     * Выдаём заданный адресный объект, только его актуальную запись. Если актуальной записи нет, тогда null.
     * @param aoguid - GUID адресного объекта
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AddrObj findAddressObjectByAoguid(String aoguid) {
    	Query qry = em.createQuery("from ru.simplgroupp.fias.persistence.AddrObj where AOGUID = :AOGUID and actualStatus = :ActualStatus");
    	qry.setParameter("AOGUID", aoguid);
    	qry.setParameter("ActualStatus", ActualStatus.ACTUAL.getID());
    	List<AddrObj> addressObjects = qry.getResultList();

        return addressObjects.size() > 0 ? addressObjects.get(0) : null;
    }
    
	public int countRecord(DBase db, String tablename) throws SQLException {
		int nrec = 0;
		db.openTable(tablename);
		try {
			nrec = db.count();
		} finally {
			db.closeTable();
		}
		return nrec;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
	public void cleanAddrObj() {
		log.info("Очищаем addrobj");
		// очистить таблицу
		Query qdel = em.createNativeQuery(SQL_DEL_ADDROBJ);
		qdel.executeUpdate();
		log.info("Очистили addrobj");		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
    public String handleAddrObj(DBase db, long nrecFrom, long nrecCount) throws SQLException 
    {
    	String sres = "";
    	
    	Query qins = em.createNativeQuery("insert into addrobj (aoguid, formalname, regioncode, autocode, areacode, citycode, ctarcode, placecode, streetcode, extrcode, sextcode, offname, postalcode, okato, oktmo, updatedate, shortname, aolevel, parentguid, aoid, nextid, previd, code, plaincode, actstatus, centstatus, operstatus, currstatus, startdate, enddate, normdoc) values (:aoguid, :formalname, :regioncode, :autocode, :areacode, :citycode, :ctarcode, :placecode, :streetcode, :extrcode, :sextcode, :offname, :postalcode, :okato, :oktmo, :updatedate, :shortname, :aolevel, :parentguid, :aoid, :nextid, :previd, :code, :plaincode, :actstatus, :centstatus, :operstatus, :currstatus, :startdate, :enddate, :normdoc)");
    	
    	db.openTable("addrobj");
    	// ищем поля
    	int field_aoguid = db.findColumn("AOGUID"); 
    	int field_formalname = db.findColumn("FORMALNAME");
    	int field_regioncode = db.findColumn("REGIONCODE");
    	int field_autocode = db.findColumn("AUTOCODE");
    	int field_areacode = db.findColumn("AREACODE");
    	int field_citycode = db.findColumn("CITYCODE");
    	int field_ctarcode = db.findColumn("CTARCODE");
    	int field_placecode = db.findColumn("PLACECODE");
    	int field_streetcode = db.findColumn("STREETCODE");
    	int field_extrcode = db.findColumn("EXTRCODE");
    	int field_sextcode = db.findColumn("SEXTCODE");
    	int field_offname = db.findColumn("OFFNAME");
    	int field_postalcode = db.findColumn("POSTALCODE");
    	int field_okato = db.findColumn("OKATO");
    	int field_oktmo = db.findColumn("OKTMO");
    	int field_updatedate = db.findColumn("UPDATEDATE");
    	int field_shortname = db.findColumn("SHORTNAME");
    	int field_aolevel = db.findColumn("AOLEVEL");
    	
    	int field_code = db.findColumn("CODE");
    	int field_plaincode = db.findColumn("PLAINCODE");
    	int field_actstatus = db.findColumn("ACTSTATUS");
    	int field_centstatus = db.findColumn("CENTSTATUS");
    	int field_operstatus = db.findColumn("OPERSTATUS");
    	int field_currstatus = db.findColumn("CURRSTATUS");
    	int field_startdate = db.findColumn("STARTDATE");
    	int field_enddate = db.findColumn("ENDDATE");
    	int field_normdoc = db.findColumn("NORMDOC");
    	
    	int field_parentguid = db.findColumn("PARENTGUID");
    	int field_aoid = db.findColumn("AOID");
    	int field_nextid = db.findColumn("NEXTID");
    	int field_previd = db.findColumn("PREVID");
    	
    	log.info("Заполняем addrobj");
    	try {
    		long nrecord;
    		
    		// переходим на первую запись 
    		if (nrecFrom == 1)
    			db.beforeFirst();
    		else
    			db.absolute(nrecFrom-1);
    		nrecord = nrecFrom-1;
    		
    		int nrech = 0;
    		while (db.next() ) 
    		{
    			nrecord++;
    			nrech++;

    			String aoid = db.getString(field_aoid);
/*    			
    			if (findStreet(aoid) != null)
    				continue;
*/    			
    			String aoguid = db.getString(field_aoguid);
    			String formalname = db.getString(field_formalname);	
    			String regioncode = db.getString(field_regioncode);
    			String autocode = db.getString(field_autocode);
    			String areacode = db.getString(field_areacode);
    			String citycode = db.getString(field_citycode);
    			String ctarcode = db.getString(field_ctarcode);
    			String placecode = db.getString(field_placecode);
    			String streetcode = db.getString(field_streetcode);
    			String extrcode = db.getString(field_extrcode);
    			String sextcode = db.getString(field_sextcode);
    			String offname = db.getString(field_offname);
    			String postalcode = db.getString(field_postalcode);
    			String okato = db.getString(field_okato);
    			String oktmo = db.getString(field_oktmo);
    			String updatedate = db.getString(field_updatedate);
    			String shortname = db.getString(field_shortname);
    			int aolevel = db.getInt(field_aolevel);
    			String code = db.getString(field_code);
    			String plaincode = db.getString(field_plaincode);   
    			int actstatus = db.getInt(field_actstatus);
    			int centstatus = db.getInt(field_centstatus);
    			int operstatus = db.getInt(field_operstatus);
    			int currstatus = db.getInt(field_currstatus);
    			String startdate = db.getString(field_startdate);
    			String enddate = db.getString(field_enddate);
    			String normdoc = db.getString(field_normdoc);   			
    			String parentguid = db.getString(field_parentguid);
    			String nextid = db.getString(field_nextid);
    			String previd = db.getString(field_previd);
    			
    			qins.setParameter("aoguid", aoguid);
    			qins.setParameter("formalname", formalname);
    			qins.setParameter("regioncode", regioncode);
    			qins.setParameter("autocode", autocode);
    			qins.setParameter("areacode", areacode);
    			qins.setParameter("citycode", citycode);
    			qins.setParameter("ctarcode", ctarcode);
    			qins.setParameter("placecode", placecode);
    			qins.setParameter("streetcode", streetcode);
    			qins.setParameter("extrcode", extrcode);
    			qins.setParameter("sextcode", sextcode);
    			qins.setParameter("offname", offname);
    			qins.setParameter("postalcode", postalcode);
    			qins.setParameter("okato", okato);
    			qins.setParameter("oktmo", oktmo);
    			try {
					qins.setParameter("updatedate", Convertor.toDate(updatedate,dformat));
				} catch (Exception e) {
					log.severe("Не удалось преобразовать строку "+updatedate+" в дату "+e);
				}
    			qins.setParameter("shortname", shortname);
    			qins.setParameter("aolevel", aolevel);
    			qins.setParameter("code", code);
    			qins.setParameter("plaincode", plaincode);
    			qins.setParameter("actstatus", actstatus);
    			qins.setParameter("centstatus", centstatus);
    			qins.setParameter("operstatus", operstatus);
    			qins.setParameter("currstatus", currstatus);
    			try {
					qins.setParameter("startdate", Convertor.toDate(startdate,dformat));
				} catch (Exception e) {
					log.severe("Не удалось преобразовать строку "+startdate+" в дату "+e);
				}
    			try {
					qins.setParameter("enddate", Convertor.toDate(enddate,dformat));
				} catch (Exception e) {
					log.severe("Не удалось преобразовать строку "+enddate+" в дату "+e);
				} 
    			qins.setParameter("normdoc", normdoc);
    			
    			qins.setParameter("parentguid", parentguid);
    			qins.setParameter("aoid", aoid);
    			qins.setParameter("nextid", nextid);
    			qins.setParameter("previd", previd);
    			try {
    				qins.executeUpdate();
    			} catch (Exception ex) {
    				log.severe("Не удалось добавить запись "+nrecord+", ошибка "+ex);
    				sres = sres + ";" + nrecord;
    			}
    			
    			if (nrech >= nrecCount)
    				break;
    		}
    	} finally {
    		db.closeTable();
    	}
    	log.info("Заполнили addrobj");
    	return sres;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
    public void handleSocrBase(DBase db, boolean bReplace) throws SQLException 
    {
    	if (bReplace) {
    		log.info("Очищаем socrbase");
    		// очистить таблицу
    		Query qdel = em.createNativeQuery(SQL_DEL_SOCRBASE);
    		qdel.executeUpdate();
    		log.info("Очистили socrbase");
    	}
    	
    	Query qins = em.createNativeQuery("insert into socrbase (level1, scname, socrname) values (:level1, :scname, :socrname)");
    	
    	db.openTable("socrbase");
    	// ищем поля
    	int field_level = db.findColumn("LEVEL"); 
    	int field_scname = db.findColumn("SCNAME");
    	int field_socrname = db.findColumn("SOCRNAME");
//    	int field_kod_t_st = db.findColumn("KOD_T_ST");
    	
    	log.info("Заполняем socrbase");
    	try {
    		while (db.next())
    		{
    			String level = db.getString(field_level);
    			String scname = db.getString(field_scname);
    			String socrname = db.getString(field_socrname);
//    			String kod_t_st = db.getString(field_kod_t_st);
    			
    			qins.setParameter("level1", Integer.parseInt(level));
    			qins.setParameter("scname", scname);
    			qins.setParameter("socrname", socrname);
//    			qins.setParameter("kod_t_st", kod_t_st);
    			qins.executeUpdate();
    		}
    	} finally {
    		db.closeTable();
    	}
    	log.info("Заполнили socrbase");
    }
	
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
	public Journal startUpdate(int operType) {
		Journal jr = new Journal();
		jr.setDateStart(new Date());
		jr.setOperationType(operType);
		jr.setStatus(Journal.STATUS_IN_PROGRESS);
		em.persist(jr);
		
		return jr;
	}
	
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
	public Journal endUpdate(Journal jrCurr, int nstatus, String msgs) 
	{
		jrCurr = em.merge(jrCurr);
		
		jrCurr.setDateFinish(new Date());
		jrCurr.setStatus(nstatus);
		jrCurr.setMessageText(msgs);
		em.persist(jrCurr);
		return jrCurr;
	}

	@Override
	public List<AddrObjType> listTypes(Integer nlevel) 
	{
		String sql = "from ru.simplgroupp.fias.persistence.AddrObjType where (1=1)";
		if (nlevel != null)
			sql = sql + " and (Level = :nlevel)";
		sql = sql + " order by Code";
    	Query qry = em.createQuery(sql);
    	if (nlevel != null)
    		qry.setParameter("nlevel", nlevel);

        return qry.getResultList();
	}
	
	public boolean hasChildren(String parentGUID, Boolean bActual) 
	{
		String sql = "select a from ru.simplgroupp.fias.persistence.AddrObj a where (parentAOGUID = :parentGUID) ";
		if (bActual != null)
			sql = sql + " and (actualStatus = :ActualStatus)";		
		
		TypedQuery<AddrObj> qry = em.createQuery(sql, AddrObj.class);
		qry.setParameter("parentGUID", parentGUID);	
		if (bActual != null) {
			if (bActual) {
                qry.setParameter("ActualStatus", ActualStatus.ACTUAL.getID());
            } else {
                qry.setParameter("ActualStatus", ActualStatus.NOT_ACTUAL.getID());
            }
		}
		List<AddrObj> lst = qry.getResultList();
		lst = filterAddrObj(lst);
		return ! lst.isEmpty();
	}
	
	public List<AddrObj> listAddrObj(Integer aolevel, String codelevel, String parentGUID, 
			Boolean bActual) {
		return listAddrObj(aolevel, codelevel, parentGUID, null, null, null, bActual);
	}

	public List<AddrObj> listAddrObj(Integer aolevel, String codelevel, String parentGUID, String term, 
			Integer page, Integer pageSize, Boolean bActual){
		String sql = "from ru.simplgroupp.fias.persistence.AddrObj where 1=1";
		if (aolevel != null){
			sql = sql + " and (type.Level = :aolevel)";
		}
		if (codelevel != null){
			sql = sql + " and (type.Code = :codelevel)";
		}
		if (bActual != null){
			sql = sql + " and (actualStatus = :ActualStatus)";
		}
		if (StringUtils.isNotEmpty(parentGUID)){
			sql = sql + " and (parentAOGUID = :parentGUID)";
		}
		if (term != null){
			sql = sql + " and (lower(formalName) like lower(:term))";
		}
		sql = sql + " order by type.Level, parentAOGUID, officialName";

		log.info(sql);
		
		TypedQuery<AddrObj> qry = em.createQuery(sql, AddrObj.class);
		if (aolevel != null){
			qry.setParameter("aolevel", aolevel);
		}
		if (codelevel != null){
			qry.setParameter("codelevel", codelevel);
		}
		if (bActual != null) {
			if (bActual) {
				qry.setParameter("ActualStatus", ActualStatus.ACTUAL.getID());
			} else {
				qry.setParameter("ActualStatus", ActualStatus.NOT_ACTUAL.getID());
			}
		}
		if (StringUtils.isNotEmpty(parentGUID)){
			qry.setParameter("parentGUID", parentGUID);
		}
		if (term != null){
			qry.setParameter("term", term + "%");
		}

		if (page != null && pageSize != null) {
			qry.setFirstResult((page-1) * pageSize);
			qry.setMaxResults(pageSize);
		}

		return filterAddrObj(qry.getResultList());
	}
	
	private Map<Integer,String> parseCode(int nLevel, String code) {
		Map<Integer,String> res = new HashMap<>(9);
		if (nLevel >= ru.simplgroupp.fias.persistence.Level.REGION.getID() ) {
			res.put(ru.simplgroupp.fias.persistence.Level.REGION.getID(), code.substring(0, 2));
		}
		if (nLevel >= ru.simplgroupp.fias.persistence.Level.REGION.getID() ) {
			res.put(ru.simplgroupp.fias.persistence.Level.REGION.getID(), code.substring(0, 2));
		}
		
		// TODO
		return res;		
	}
	
	/**
	 * listAddrObj - ищет адресные объекты. Если какой-то параметр = null, то он не принимается во внимание при поиске.
	 * @param nFirst - номер записи, с которой начинать. Если = 0, то с начала.
	 * @param nCount - кол-во записей для показа
	 * @param parentLevel - родительский объект: код уровня
	 * @param parentCode - родительский объект: коды КЛАДР
	 * @param parentAOGUID - код адресного объекта родительского уровня
	 * @param kladrCode - код КЛАДР для записи
	 * @param levels - какие уровни адресных объектов выводить. null - все.
	 * @param name - название адресного объекта. Ищется по любому фрагменту названия без учёта регистра.
	 * @param bActual - искать только в действующих записях или в архивных. null - во всех.  
	 */
	@Override
	public List<AddrObj> listAddrObj(int nFirst, int nCount, Integer parentLevel, Map<Integer,String> parentCode, String parentAOGUID, String kladrCode, int[] levels, String name, Boolean bActual) 
	{
		String sql = "from ru.simplgroupp.fias.persistence.AddrObj where 1=1";

		if (parentLevel != null && parentCode != null) {
			sql = sql + " and (type.Level <> :parentLevel)";
			
			for (Map.Entry<Integer, String> entry: parentCode.entrySet())  {
				if (entry.getKey().equals(ru.simplgroupp.fias.persistence.Level.REGION.getID()))
					sql = sql + " and (regionCode = :parentCode" + entry.getKey().toString() + ")";
				else if (entry.getKey().equals(ru.simplgroupp.fias.persistence.Level.AUTO_OKRUG.getID()))
					sql = sql + " and (autoCode = :parentCode" + entry.getKey().toString() + ")";
				else if (entry.getKey().equals(ru.simplgroupp.fias.persistence.Level.RAION.getID()))
					sql = sql + " and (areaCode = :parentCode" + entry.getKey().toString() + ")";
				else if (entry.getKey().equals(ru.simplgroupp.fias.persistence.Level.CITY.getID()))
					sql = sql + " and (cityCode = :parentCode" + entry.getKey().toString() + ")";
				else if (entry.getKey().equals(ru.simplgroupp.fias.persistence.Level.CITY_TERR.getID()))
					sql = sql + " and (ctarCode = :parentCode" + entry.getKey().toString() + ")";
				else if (parentLevel.equals(ru.simplgroupp.fias.persistence.Level.NP.getID()))
					sql = sql + " and (placeCode = :parentCode" + entry.getKey().toString() + ")";
				else if (entry.getKey().equals(ru.simplgroupp.fias.persistence.Level.STREET.getID()))
					sql = sql + " and (streetCode = :parentCode" + entry.getKey().toString() + ")";	
				else if (entry.getKey().equals(ru.simplgroupp.fias.persistence.Level.DOP_TERR.getID()))
					sql = sql + " and (extrCode = :parentCode" + entry.getKey().toString() + ")";	
				else if (entry.getKey().equals(ru.simplgroupp.fias.persistence.Level.DOP_DOP_TERR.getID()))
					sql = sql + " and (sextCode = :parentCode" + entry.getKey().toString() + ")";		
			}
							
		}
		if (kladrCode != null) {
			sql = sql + " and (kladrCode like :kladrCode)";
		}
		if (bActual != null)
			sql = sql + " and (actualStatus = :ActualStatus)";
		if (parentAOGUID != null)
			sql = sql + " and (parentAOGUID = :parentAOGUID)";
		if (levels != null && levels.length > 0) {
			sql = sql + " and (type.Level in (:levels))";
		}
		if (name != null) {
			sql = sql + " and (upper(formalName) like :name)";
		}
		
		sql = sql + " order by type.Level, parentAOGUID, officialName";
		
		TypedQuery<AddrObj> qry = em.createQuery(sql, AddrObj.class);

		if (parentLevel != null && parentCode != null) {
			qry.setParameter("parentLevel", parentLevel);
			for (Map.Entry<Integer, String> entry: parentCode.entrySet())  {
				qry.setParameter("parentCode" + entry.getKey().toString(), entry.getValue());				
			}
		}
		if (kladrCode != null) {
			qry.setParameter("kladrCode", kladrCode);
		}
		if (bActual != null) {
			if (bActual) {
                qry.setParameter("ActualStatus", ActualStatus.ACTUAL.getID());
            } else {
                qry.setParameter("ActualStatus", ActualStatus.NOT_ACTUAL.getID());
            }
		}
		if (parentAOGUID != null)
			qry.setParameter("parentAOGUID", parentAOGUID);			
		if (levels != null && levels.length > 0) {			
			qry.setParameter("levels", toList(levels));
		}
		if (name != null) {
			qry.setParameter("name", "%" + name.trim() + "%");
		}
		
		if (nFirst >= 0)
			qry.setFirstResult(nFirst);
		if (nCount > 0)
			qry.setMaxResults(nCount);		
		
		List<AddrObj> lst = qry.getResultList();
		lst = filterAddrObj(lst);
		return lst;
	}
	
	private List<Integer> toList(int[] source) {
		ArrayList<Integer> lst = new ArrayList<>(source.length);
		for (int n: source) {
			lst.add(n);
		}
		return lst;
	}
	
	@Override
	public List<AddrObj> listRegions(Boolean bActual) {
		return listAddrObj(ru.simplgroupp.fias.persistence.Level.REGION.getID(), null, null, bActual);
	}
	
	public List<AddrObjType> listTypesExisting(String parentGUID, Boolean bActual) {
		String sql = "select distinct a.type from ru.simplgroupp.fias.persistence.AddrObj as a where (a.parentAOGUID = :parentGUID)";
		if (bActual != null)
			sql = sql + " and (a.actualStatus = :ActualStatus)";		

		Query qry = em.createQuery(sql);
		qry.setParameter("parentGUID", parentGUID);
		if (bActual != null) {
			if (bActual) {
                qry.setParameter("ActualStatus", ActualStatus.ACTUAL.getID());
            } else {
                qry.setParameter("ActualStatus", ActualStatus.NOT_ACTUAL.getID());
            }
		}

        return qry.getResultList();
	}

	@Override
	public AddrObj findAddrObjByCode(String parentGUID, ru.simplgroupp.fias.persistence.Level aolevel,
			Boolean bActual, String code) {
		String sql = "from ru.simplgroupp.fias.persistence.AddrObj where (parentAOGUID = :parentGUID)";
		if (bActual != null)
			sql = sql + " and (actualStatus = :ActualStatus)";		
		if (aolevel.equals(ru.simplgroupp.fias.persistence.Level.REGION))
			sql = sql + " and (regionCode = :code)";
		else if (aolevel.equals(ru.simplgroupp.fias.persistence.Level.AUTO_OKRUG))
			sql = sql + " and (autoCode = :code)";
		else if (aolevel.equals(ru.simplgroupp.fias.persistence.Level.RAION))
			sql = sql + " and (areaCode = :code)";
		else if (aolevel.equals(ru.simplgroupp.fias.persistence.Level.CITY))
			sql = sql + " and (cityCode = :code)";
		else if (aolevel.equals(ru.simplgroupp.fias.persistence.Level.CITY_TERR))
			sql = sql + " and (ctarCode = :code)";
		else if (aolevel.equals(ru.simplgroupp.fias.persistence.Level.NP))
			sql = sql + " and (placeCode = :code)";
		else if (aolevel.equals(ru.simplgroupp.fias.persistence.Level.STREET))
			sql = sql + " and (streetCode = :code)";	
		else if (aolevel.equals(ru.simplgroupp.fias.persistence.Level.DOP_TERR))
			sql = sql + " and (extrCode = :code)";	
		else if (aolevel.equals(ru.simplgroupp.fias.persistence.Level.DOP_DOP_TERR))
			sql = sql + " and (sextCode = :code)";		
		
		Query qry = em.createQuery(sql);
		qry.setParameter("parentGUID", parentGUID);
		if (bActual != null) {
			if (bActual) {
                qry.setParameter("ActualStatus", ActualStatus.ACTUAL.getID());
            } else {
                qry.setParameter("ActualStatus", ActualStatus.NOT_ACTUAL.getID());
            }
		}
		qry.setParameter("code", code);
		
		List<AddrObj> lst = qry.getResultList();
		if (lst.size() > 0)
			return lst.get(0);
		else
			return null;
	}

	/**
	 * Конвертирует адрес в набор уровней.
	 * @param address
	 * @return
	 */
	@Override
	public LevelRecord[] addressToLevels(IAddress address) 
	{
		LevelRecord[] res = address.toLevels();
		
		// дозаполняем из справочника KЛАДР: GUID адресного объекта
        for (LevelRecord levelRecord : res) {
            if (StringUtils.isEmpty(levelRecord.getID()))
                continue;

            AddrObj aobj = findAddressObject(levelRecord.getID());
            levelRecord.setAOGUID(aobj.getAOGUID());
            levelRecord.setName(aobj.getOfficialName());
            levelRecord.setTypeName(aobj.getType().getName());

            if (aobj.getType().getLevel().intValue() == Level.REGION.getID().intValue()) {
                levelRecord.setCode(aobj.getRegionCode());
            } else if (aobj.getType().getLevel().intValue() == Level.AUTO_OKRUG.getID().intValue()) {
                levelRecord.setCode(aobj.getAutoCode());
            } else if (aobj.getType().getLevel().intValue() == Level.RAION.getID().intValue()) {
                levelRecord.setCode(aobj.getAreaCode());
            } else if (aobj.getType().getLevel().intValue() == Level.CITY.getID().intValue()) {
                levelRecord.setCode(aobj.getCityCode());
            } else if (aobj.getType().getLevel().intValue() == Level.CITY_TERR.getID().intValue()) {
                levelRecord.setCode(aobj.getCtarCode());
            } else if (aobj.getType().getLevel().intValue() == Level.NP.getID().intValue()) {
                levelRecord.setCode(aobj.getPlaceCode());
            } else if (aobj.getType().getLevel().intValue() == Level.STREET.getID().intValue()) {
                levelRecord.setCode(aobj.getStreetCode());
            } else if (aobj.getType().getLevel().intValue() == Level.DOP_TERR.getID().intValue()) {
                levelRecord.setCode(aobj.getExtrCode());
            } else if (aobj.getType().getLevel().intValue() == Level.DOP_DOP_TERR.getID().intValue()) {
                levelRecord.setCode(aobj.getSextCode());
            }

        }
		return res;
	}	
	
	/**
	 * Конвертирует набор уровней в адрес.
	 * @param source
	 * @param address
	 */
	@Override
	public void levelsToAddress(LevelRecord[] source, IAddress address)
	{
		address.clearAddress();
		
		for (LevelRecord rec: source) 
		{
					
			if (StringUtils.isEmpty(rec.getID())){
				continue;
			}
			
			AddrObj aobj = findAddressObject(rec.getID());
			if (aobj.getType().getLevel().intValue() == ru.simplgroupp.fias.persistence.Level.REGION.getID().intValue()) 
			{
				// регион
				rec.setCode(aobj.getRegionCode());
			} else if (aobj.getType().getLevel().intValue() == ru.simplgroupp.fias.persistence.Level.AUTO_OKRUG.getID().intValue()) {
				// авт. округ
				rec.setCode(aobj.getAutoCode());
			} else if (aobj.getType().getLevel().intValue() == ru.simplgroupp.fias.persistence.Level.RAION.getID().intValue()) {
				rec.setCode(aobj.getAreaCode());
			} else if (aobj.getType().getLevel().intValue() == ru.simplgroupp.fias.persistence.Level.CITY.getID().intValue()) {
				rec.setCode(aobj.getCityCode());
			} else if (aobj.getType().getLevel().intValue() == ru.simplgroupp.fias.persistence.Level.CITY_TERR.getID().intValue()) {
				rec.setCode(aobj.getCtarCode());
			} else if (aobj.getType().getLevel().intValue() == ru.simplgroupp.fias.persistence.Level.NP.getID().intValue()) {
				rec.setCode(aobj.getPlaceCode());
			} else if (aobj.getType().getLevel().intValue() == ru.simplgroupp.fias.persistence.Level.STREET.getID().intValue()) {
				rec.setCode(aobj.getStreetCode());
			} else if (aobj.getType().getLevel().intValue() == ru.simplgroupp.fias.persistence.Level.DOP_TERR.getID().intValue()) {
				rec.setCode(aobj.getExtrCode());
			} else if (aobj.getType().getLevel().intValue() == ru.simplgroupp.fias.persistence.Level.DOP_DOP_TERR.getID().intValue()) {
				rec.setCode(aobj.getSextCode());
			}
			rec.setName(aobj.getOfficialName());
			rec.setType(aobj.getType().getCode());
			rec.setTypeName(aobj.getType().getName());
		}
		address.fromLevels(source);
	}

	public List<AddrObj> filterAddrObj(List<AddrObj> addrObjs) {
		List<AddrObj> resultList = new ArrayList<>(addrObjs.size());
		Set<String> excludes = new HashSet<>();

		// TODO: move in database
		excludes.add("п/о"); // Почтовое отделение
		excludes.add("с/а"); // Сельская администрация
		excludes.add("с/с"); // Сельсовет
		excludes.add("автодорога");
		excludes.add("ж/д_будка");
		excludes.add("ж/д_казарм");
		excludes.add("ж/д_оп");
		excludes.add("ж/д_платф");
		excludes.add("ж/д_пост");
		excludes.add("ж/д_рзд");
		excludes.add("ж/д_ст");
		excludes.add("жилзона");
		excludes.add("гск");

		for(AddrObj addrObj : addrObjs) {
			if (addrObj != null && !addrObj.getAoLevel().equals(Level.CITY_TERR.getID()) && !excludes.contains(addrObj.getShortName())) {
				resultList.add(addrObj);
			}
		}

		return resultList;
	}
}
