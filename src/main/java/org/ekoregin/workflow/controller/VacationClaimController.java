package org.ekoregin.workflow.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ekoregin.workflow.dto.TaskDto;
import org.ekoregin.workflow.dto.VacationClaimDto;
import org.ekoregin.workflow.mapper.TaskMapper;
import org.ekoregin.workflow.mapper.VacationClaimMapper;
import org.ekoregin.workflow.model.UserAction;
import org.ekoregin.workflow.model.VacationClaim;
import org.ekoregin.workflow.service.VacationClaimService;
import org.ekoregin.workflow.service.VacationClaimWorkflowService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/claims")
public class VacationClaimController {

    private final VacationClaimService vacationClaimService;
    private final VacationClaimWorkflowService vacationClaimWorkflowService;
    private final VacationClaimMapper vacationClaimMapper;
    private final TaskMapper taskMapper;

    @PostMapping("/{id}/process")
    public void startWorkFlow(@PathVariable("id") String id) {
        log.info("Start workflow from controller with claim id: {}", id);
        vacationClaimWorkflowService.startMyWorkflow(UUID.fromString(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VacationClaimDto create(@RequestBody VacationClaimDto vacationClaimDto) {
        return vacationClaimMapper.toDto(vacationClaimService.create(vacationClaimDto));
    }

    @PutMapping("/{id}")
    public VacationClaimDto update(@RequestBody VacationClaimDto vacationClaimDto, @PathVariable("id") UUID id) {
        VacationClaim vacationClaim = vacationClaimMapper.toEntity(vacationClaimDto);
        return vacationClaimMapper.toDto(vacationClaimService.update(vacationClaim, id));
    }

    @GetMapping
    public List<VacationClaimDto> findAll() {
        return vacationClaimMapper.toDtoList(vacationClaimService.findAll());
    }

    @GetMapping("/{id}")
    public VacationClaimDto findById(@PathVariable("id") UUID id) {
        return vacationClaimMapper.toDto(vacationClaimService.findById(id));
    }

    @GetMapping("/{id}/actions")
    public List<UserAction> getActions(@PathVariable("id") UUID id) {
        return vacationClaimService.getActions(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") UUID id) {
        vacationClaimService.deleteById(id);
    }

    @GetMapping("/tasks")
    public List<TaskDto> getTasks() {
        return taskMapper.toListDto(vacationClaimWorkflowService.findMyTasks());
    }

    @PutMapping("/task/{id}")
    public void setClaimAction(@PathVariable("id") UUID id, @RequestParam("action") String userAction) {
        vacationClaimService.setClaimAction(String.valueOf(id), UserAction.valueOf(userAction));
    }
}
