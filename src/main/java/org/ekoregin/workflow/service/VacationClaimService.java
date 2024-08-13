package org.ekoregin.workflow.service;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.ekoregin.workflow.dto.VacationClaimDto;
import org.ekoregin.workflow.exception.EntityNotFoundException;
import org.ekoregin.workflow.mapper.VacationClaimMapper;
import org.ekoregin.workflow.model.ClaimStatus;
import org.ekoregin.workflow.model.UserAction;
import org.ekoregin.workflow.model.VacationClaim;
import org.ekoregin.workflow.repository.VacationClaimRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VacationClaimService {

    private final VacationClaimRepository vacationClaimRepository;
    private final VacationClaimMapper vacationClaimMapper;

    private final DmnEngine dmnEngine;
    private DmnDecision actionsDecision;

    @Value("classpath:dmn/Actions2.dmn")
    private Resource actionsDmn;


    @PostConstruct
    public void initActionsDmn() throws IOException {
        try (InputStream input = new BufferedInputStream(actionsDmn.getInputStream())) {
            DmnModelInstance modelInstance = Dmn.readModelFromStream(input);
            actionsDecision = dmnEngine.parseDecision("Actions2", modelInstance);
        }
    }

    public List<UserAction> getActions(UUID claimId) {
        VacationClaim claim = findById(claimId);
        ClaimStatus status = claim.getStatus();

        Map<String, Object> variables = new HashMap<>();
        variables.put("status", status.name());

        log.info("Claim status: {}", status.name());


        DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(actionsDecision, variables);

        List<String> actions = result.collectEntries("action");
        log.info("Actions result: {}", actions);
        if (actions == null) {
            return new ArrayList<>();
        }
        return actions.stream().map(UserAction::valueOf).collect(Collectors.toList());
    }

    public VacationClaim findById(@NonNull UUID id) {
        return vacationClaimRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Vacation claim with id " + id + " not found"));
    }

    public List<VacationClaim> findAll() {
        List<VacationClaim> claims = new ArrayList<>();
        vacationClaimRepository.findAll().forEach(claims::add);
        return claims;
    }

    public VacationClaim create(VacationClaimDto vacationClaimDto) {
        VacationClaim vacationClaim = vacationClaimMapper.toEntity(vacationClaimDto);
        vacationClaim.setNew(true);
        vacationClaim.setId(UUID.randomUUID());
        vacationClaim.setCreatedAt(LocalDateTime.now());
        vacationClaim.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return vacationClaimRepository.save(vacationClaim);
    }

    public VacationClaim update(VacationClaim vacationClaim, UUID id) {
        vacationClaimRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Vacation claim with id " + id + " not found")
        );
        vacationClaim.setNew(false);
        return vacationClaimRepository.save(vacationClaim);
    }

    public void deleteById(UUID id) {
        vacationClaimRepository.deleteById(id);
    }
}
