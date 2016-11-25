package ru.simplgroupp.transfer;

import ru.simplgroupp.persistence.CreditRequestEntity;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudPageEntity;
import ru.simplgroupp.persistence.antifraud.AntifraudPageTypesEntity;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;

public class AntifraudPage extends BaseTransfer<AntifraudPageEntity> implements Serializable, Initializable, Identifiable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2426212705410075256L;
	protected static Constructor<? extends BaseTransfer> wrapConstructor;

    public static Constructor<? extends BaseTransfer> getWrapConstructor() {
        return wrapConstructor;
    }

    static {
        try {
            wrapConstructor = AntifraudPage.class.getConstructor(AntifraudPageEntity.class);
        } catch (NoSuchMethodException | SecurityException e) {
            wrapConstructor = null;
        }
    }

    public AntifraudPage() {
        super();
    }

    public AntifraudPage(AntifraudPageEntity ent) {
        super(ent);
    }

    public Integer getId() {
        return entity.getId();
    }

    public void setId(Integer id) {
        entity.setId(id);
    }

    public AntifraudPageTypesEntity getType() {
        return entity.getType();
    }

    public void setType(AntifraudPageTypesEntity type) {
        entity.setType(type);
    }

    public PeopleMainEntity getPeopleMainId() {
        return entity.getPeopleMainId();
    }

    public void setPeopleMainId(PeopleMainEntity peopleMainId) {
        entity.setPeopleMainId(peopleMainId);
    }

    public CreditRequestEntity getCreditRequestId() {
        return entity.getCreditRequestId();
    }

    public void setCreditRequestId(CreditRequestEntity creditRequestId) {
        entity.setCreditRequestId(creditRequestId);
    }

    public String getSessionId() {
        return entity.getSessionId();
    }

    public void setSessionId(String sessionId) {
        entity.setSessionId(sessionId);
    }

    public Date getDateStart() {
        return entity.getDateStart();
    }

    public void setDateStart(Date dateStart) {
        entity.setDateStart(dateStart);
    }

    public Date getDateEnd() {
        return entity.getDateEnd();
    }

    public void setDateEnd(Date dateEnd) {
        entity.setDateEnd(dateEnd);
    }

    @Override
    public void init(Set options) {

    }
}
