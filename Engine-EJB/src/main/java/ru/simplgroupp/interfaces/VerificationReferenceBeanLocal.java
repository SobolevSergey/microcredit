package ru.simplgroupp.interfaces;

import ru.simplgroupp.persistence.VerificationReferenceEntity;
import ru.simplgroupp.transfer.VerificationReference;

import javax.ejb.Local;
import java.util.List;
import java.util.Set;

@Local
public interface VerificationReferenceBeanLocal {
    List<VerificationReference> findAllReference(Set options);

    VerificationReferenceEntity saveReference(VerificationReference entity);

    boolean removeReference(int id);

    VerificationReferenceEntity getReferenceEntity(int id);

    VerificationReference getReference(int id, Set options);
}
