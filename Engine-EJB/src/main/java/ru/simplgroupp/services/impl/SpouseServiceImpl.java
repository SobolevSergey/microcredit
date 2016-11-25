package ru.simplgroupp.services.impl;

import org.springframework.data.domain.PageRequest;
import ru.simplgroupp.interfaces.ReferenceBooksLocal;
import ru.simplgroupp.persistence.PeopleMainEntity;
import ru.simplgroupp.persistence.SpouseEntity;
import ru.simplgroupp.persistence.repository.SpouseRepository;
import ru.simplgroupp.services.SpouseService;
import ru.simplgroupp.transfer.ActiveStatus;
import ru.simplgroupp.transfer.RefHeader;
import ru.simplgroupp.transfer.Spouse;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Spouse service implementation
 */
@Singleton
@TransactionManagement
public class SpouseServiceImpl implements SpouseService {

    @Inject
    private SpouseRepository spouseRepository;

    @Inject
    private ReferenceBooksLocal referenceBooks;

    @Override
    public SpouseEntity findActiveByPeople(Integer peopleId) {
        List<SpouseEntity> list = spouseRepository.findActiveByPeople(peopleId, new PageRequest(0, 1));
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public SpouseEntity createSpouse(PeopleMainEntity peopleMain, PeopleMainEntity spousePeople, Integer occupationId,
                                     Date marriageDate) {
        SpouseEntity spouse = new SpouseEntity();
        spouse.setDatabeg(marriageDate);
        spouse.setPeopleMainId(peopleMain);
        spouse.setSpouseId(referenceBooks.findByCodeIntegerEntity(RefHeader.SPOUSE_TYPE, Spouse.CODE_SPOUSE));
        spouse.setIsactive(ActiveStatus.ACTIVE);
        spouse.setPeopleMainSpouseId(spousePeople);

        spouse.setTypeworkId(referenceBooks.findByCodeIntegerEntity(RefHeader.EMPLOY_TYPE, occupationId));
        return spouseRepository.save(spouse);
    }

    @Override
    public SpouseEntity updateSpouse(SpouseEntity spouse, Integer occupationId, Date marriageDate) {
        spouse.setDatabeg(marriageDate);
        spouse.setTypeworkId(referenceBooks.findByCodeIntegerEntity(RefHeader.EMPLOY_TYPE, occupationId));

        return spouse;
    }
}
