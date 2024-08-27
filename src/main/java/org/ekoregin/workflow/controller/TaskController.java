package org.ekoregin.workflow.controller;

import lombok.RequiredArgsConstructor;
import org.ekoregin.workflow.mapper.TaskMapper;
import org.ekoregin.workflow.mapper.VacationClaimMapper;
import org.ekoregin.workflow.model.UserAction;
import org.ekoregin.workflow.model.VacationClaim;
import org.ekoregin.workflow.service.VacationClaimService;
import org.ekoregin.workflow.service.VacationClaimWorkflowService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final VacationClaimWorkflowService vacationClaimWorkflowService;
    private final VacationClaimService vacationClaimService;
    private final TaskMapper taskMapper;
    private final VacationClaimMapper vacationClaimMapper;

    @GetMapping
    public String tasks(Model model) {
        model.addAttribute("userTasks", taskMapper.toListDto(vacationClaimWorkflowService.findMyTasks()));
        return "tasks";
    }

    @GetMapping("/{taskId}")
    public String formCompleteTask(@PathVariable("taskId") UUID id, Model model) {
        VacationClaim vacationClaim = vacationClaimService.findClaimByTaskId(String.valueOf(id));
        List<UserAction> userActions = vacationClaimService.getActions(vacationClaim.getId());
        model.addAttribute("vacationClaimDto", vacationClaimMapper.toDto(vacationClaim));
        model.addAttribute("userActions", userActions);
        model.addAttribute("taskId", String.valueOf(id));
        return "completeTask";
    }

    @PostMapping("/{id}")
    public String completeTask(@PathVariable("id") UUID id, @RequestParam("action") UserAction userAction) {
        vacationClaimService.completeTask(String.valueOf(id), userAction);
        return "redirect:/tasks";
    }
}
