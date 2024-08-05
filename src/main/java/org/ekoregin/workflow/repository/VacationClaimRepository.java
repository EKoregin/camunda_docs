package org.ekoregin.workflow.repository;

import org.ekoregin.workflow.model.VacationClaim;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VacationClaimRepository extends CrudRepository<VacationClaim, UUID> {
}
