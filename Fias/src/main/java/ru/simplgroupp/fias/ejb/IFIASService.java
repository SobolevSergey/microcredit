package ru.simplgroupp.fias.ejb;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import ru.simplgroupp.fias.persistence.AddrObj;
import ru.simplgroupp.fias.persistence.AddrObjType;
import ru.simplgroupp.fias.persistence.IAddress;
import ru.simplgroupp.fias.persistence.Journal;
import ru.simplgroupp.fias.persistence.Level;
import ru.simplgroupp.fias.persistence.LevelRecord;

import com.theorem.misc.dbase.DBase;

@Local
public interface IFIASService {
    Journal findOperationInProgress();

    Journal startUpdate(int operType);

    Journal endUpdate(Journal jrCurr, int nstatus, String msgs);

    void handleSocrBase(DBase db, boolean bReplace) throws SQLException;

    int countRecord(DBase db, String tablename) throws SQLException;

    String handleAddrObj(DBase db, long nrecFrom, long nrecCount) throws SQLException;

    void cleanAddrObj();

    List<AddrObjType> listTypes(Integer nlevel);

    List<AddrObjType> listTypesExisting(String parentGUID, Boolean bActual);

    List<AddrObj> listAddrObj(Integer aolevel, String codelevel, String parentGUID, Boolean bActual);

    List<AddrObj> listAddrObj(Integer aolevel, String codelevel, String parentGUID, String term, Integer page, Integer pageSize, Boolean bActual);

    List<AddrObj> listAddrObj(int nFirst, int nCount, Integer parentLevel, Map<Integer, String> parentCode, String parentAOGUID, String kladrCode, int[] levels, String name, Boolean bActual);

    boolean hasChildren(String parentGUID, Boolean bActual);

    AddrObj findAddrObjByCode(String parentGUID, Level aolevel, Boolean bActual, String code);

    AddrObj findAddressObject(String aoid);

    List<AddrObj> listRegions(Boolean bActual);

    LevelRecord[] addressToLevels(IAddress address);

    void levelsToAddress(LevelRecord[] source, IAddress address);

    AddrObj findAddressObjectByAoguid(String aoguid);
}

