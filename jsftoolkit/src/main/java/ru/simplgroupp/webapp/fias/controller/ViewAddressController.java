package ru.simplgroupp.webapp.fias.controller;

import org.apache.commons.lang.StringUtils;
import ru.simplgroupp.fias.ejb.IFIASService;
import ru.simplgroupp.fias.persistence.AddrObj;
import ru.simplgroupp.fias.persistence.IAddress;
import ru.simplgroupp.fias.persistence.Level;
import ru.simplgroupp.fias.persistence.LevelRecord;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ViewAddressController implements Serializable {

    private static final long serialVersionUID = -8049990156873654L;

    @EJB
    protected IFIASService fiasServ;

    protected IAddress address;

    protected ConcurrentHashMap<Long, LevelRecordC> address_levels = new ConcurrentHashMap<Long, LevelRecordC>(0);

    protected boolean isBottomLevel = false;

    private Logger log = Logger.getLogger(ViewAddressController.class.getName());
    
    public boolean getIsBottomLevel() {
        return isBottomLevel;
    }

    public int getLevelCount() {
        return address_levels.size();
    }

    public Map<Long, LevelRecordC> getAddressLevels() {
        return address_levels;
    }

    public IAddress getAddress() {
        return address;
    }

    public static String extractID(String aid) {
        int n = aid.indexOf(";");
        if (n < 0)
            return aid;
        else
            return aid.substring(0, n);
    }

    public static String extractAOGUID(String aid) {
        int n = aid.indexOf(";");
        if (n < 0)
            return aid;
        int m = aid.indexOf(";", n + 1);
        return aid.substring(n + 1, m);
    }

    public void levelChanged(ValueChangeEvent event) {
     
        Number nlevel = (Number) event.getComponent().getAttributes().get("nlevel");
        log.info("!!!Changed level "+nlevel);
        // удаляем нижележащие уровни
        for (int n = nlevel.intValue() + 1; n <= 9; n++) {
            address_levels.remove(new Long(n));
        }

        if (event.getNewValue() != null) {
            // заполняем выбранный уровень
            String id = extractID(event.getNewValue().toString());
            AddrObj aobj = fiasServ.findAddressObject(id);
            LevelRecordC rec1 = address_levels.get(new Long(nlevel.longValue()));
            rec1.setType(aobj.getType().getCode());
            rec1.setTypeName(aobj.getType().getName());

            // добавляем ещё уровень, если есть под-значения
            addNextLevel(event.getNewValue().toString(), nlevel.intValue());

        }
    }

    private void addNextLevel(String parentAOGUID, int parentNLevel) {
        // добавляем ещё уровень, если есть под-значения
        String aoguid = extractAOGUID(parentAOGUID);
        if (fiasServ.hasChildren(aoguid, true)) {

            LevelRecordC rec1 = new LevelRecordC();
            rec1.setID(null);
            rec1.setAOGUID(null);
            rec1.setNlevel(parentNLevel + 1);
            rec1.setAoLevel(null);
            rec1.setType(null);
            rec1.setTypeName(null);
            rec1.setName(null);
            rec1.setCode(null);

            address_levels.put(new Long(rec1.getNlevel()), rec1);

            isBottomLevel = false;
        } else
            isBottomLevel = true;
    }

    /**
     * Заполнен ли адрес
     *
     * @return
     */
    public boolean getIsCompleted() {
        if (!isBottomLevel) {
            return false;
        }

        for (Entry<Long, LevelRecordC> rec : address_levels.entrySet()) {
            if (StringUtils.isBlank(rec.getValue().getCompositeID())) {
                return false;
            }
        }
        return (!StringUtils.isBlank(address.getHouse()));
    }

    public void setAddress(IAddress address) {
        this.address = address;
        if (address == null)
            return;

        LevelRecord[] ares = fiasServ.addressToLevels(address);
        address_levels.clear();
        if (ares.length == 0) {
            LevelRecordC rec1 = new LevelRecordC();
            rec1.setID(null);
            rec1.setAOGUID(null);
            rec1.setNlevel(0);
            rec1.setAoLevel(null);
            rec1.setType(null);
            rec1.setTypeName(null);
            rec1.setName(null);
            rec1.setCode(null);

            address_levels.put(new Long(rec1.getNlevel()), rec1);
        } else {
            for (LevelRecord rec : ares) {
            	LevelRecordC rec1 = new LevelRecordC();
                rec1.setID(rec.getID());
                rec1.setAOGUID(rec.getAOGUID());
                rec1.setNlevel(rec.getNlevel());
                log.info("!!!Address level number "+rec.getNlevel());
                rec1.setAoLevel(rec.getAoLevel());
                rec1.setType(rec.getType());
                rec1.setTypeName(rec.getTypeName());
                rec1.setName(rec.getName());
                log.info("!!!Address name "+rec.getName());
                rec1.setCode(rec.getCode());
                log.info("!!!Address composite id "+rec1.getCompositeID());
                log.info("!!!------------------!!!");
                address_levels.put(new Long(rec1.getNlevel()), rec1);
            }
            addNextLevel(ares[ares.length - 1].getAOGUID(), ares[ares.length - 1].getNlevel());
        }
    }

    protected List<SelectItem> listRefLevel(String parentGUID) {
        if (parentGUID == null) {
            List<SelectItem> lstRes = new ArrayList<SelectItem>(1);
            SelectItem itm = new SelectItem();
            itm.setLabel("-- не выбран --");
            itm.setValue(null);
            lstRes.add(itm);
            return lstRes;
        }

        List<AddrObj> lstServ;

        if (parentGUID.equalsIgnoreCase("")){
        	lstServ = fiasServ.listRegions(true);	
        	log.info("!!!Region level, parentGUID is empty, size of list "+lstServ.size());
        } else {
            lstServ = fiasServ.listAddrObj(null, null, parentGUID, true);
            log.info("!!!Guid is "+parentGUID);
        }
        List<SelectItem> lstRes = new ArrayList<SelectItem>(lstServ.size());

        SelectItem itm = new SelectItem();
        itm.setLabel("-- не выбран --");
        itm.setValue(null);
        lstRes.add(itm);

        for (AddrObj obj : lstServ) {
            itm = new SelectItem();
            itm.setLabel(obj.getFormalName() + " " + obj.getType().getName().toLowerCase());
            itm.setValue(obj.getID() + ";" + obj.getAOGUID() + ";" + obj.getType().getLevel().toString());
            lstRes.add(itm);
        }
        return lstRes;
    }

    public void levelsToAddress() {
        LevelRecordC[] source = address_levels.values().toArray(new LevelRecordC[]{});
        fiasServ.levelsToAddress(source, address);
    }

    public class LevelRecordC extends LevelRecord {

        public boolean isEmpty() {
            return StringUtils.isEmpty(getID());
        }

        public String getCompositeID() {
            String ss = getID();
            if (!StringUtils.isEmpty(getAOGUID())){
                ss = ss + ";" + getAOGUID();
            }
            if (getAoLevel() != null){
                ss = ss + ";" + getAoLevel().getID();
            }

            return ss;
        }

        public void setCompositeID(String aid) {
            if (StringUtils.isEmpty(aid)) {
                setID(null);
                setAOGUID(null);
            } else {
                int n = aid.indexOf(";");
                setID(aid.substring(0, n));
                int m = aid.indexOf(";", n + 1);
                String ss = aid.substring(n + 1, m);
                setAOGUID(ss);

                ss = aid.substring(m + 1);
                Level lv = Level.getInstance(Integer.parseInt(ss));
                setAoLevel(lv);
            }
        }

        protected LevelRecordC getParent() {
            if (getNlevel() == 0)
                return null;
            else
                return address_levels.get(new Long(getNlevel() - 1));
        }

        public List<SelectItem> getRefLevel() {
            if (getNlevel() == 0){
                return listRefLevel("");
            }
            return listRefLevel(getParent().getAOGUID());
        }
    }
}
