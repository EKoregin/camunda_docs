package org.ekoregin.workflow.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.ekoregin.workflow.service.MyWorkflowVars.APPROVER;
import static org.ekoregin.workflow.service.MyWorkflowVars.CLAIM_ID;
import static org.ekoregin.workflow.service.MyWorkflowVars.INITIATOR;

@Slf4j
@Service
@RequiredArgsConstructor
public class VacationClaimWorkflowService {

    private static final String PROCESS_ID = "VacationClaim";
    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public void startMyWorkflow(@NonNull UUID claimId) {
        String me = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} start process {}", me, PROCESS_ID);
        Map<String, Object> context = new HashMap<>();
        context.put(CLAIM_ID, claimId);
        context.put(INITIATOR, me);
        context.put(APPROVER, me);

        runtimeService.startProcessInstanceByKey(PROCESS_ID, context);
    }
}
