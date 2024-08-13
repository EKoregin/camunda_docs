package org.ekoregin.workflow.process;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.ekoregin.workflow.model.ClaimStatus;
import org.ekoregin.workflow.model.VacationClaim;
import org.ekoregin.workflow.service.VacationClaimService;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.ekoregin.workflow.service.MyWorkflowVars.CLAIM_ID;

@Setter
@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateStatus implements JavaDelegate {

    private final VacationClaimService vacationClaimService;

    private Expression status;


    @Override
    public void execute(DelegateExecution context) {
        UUID claimId = (UUID) context.getVariable(CLAIM_ID);
        log.info("Find claim by id: {}", claimId);
        VacationClaim claim = vacationClaimService.findById(claimId);
        String claimStatus = (String) status.getValue(context);
        log.info("Set document status: {}", claimStatus);
        claim.setStatus(ClaimStatus.valueOf(claimStatus));
        vacationClaimService.update(claim, claimId);
    }
}
