package org.ekoregin.workflow.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ekoregin.workflow.dto.VacationClaimDto;
import org.ekoregin.workflow.service.VacationClaimService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VacationClaimDto create(@RequestBody VacationClaimDto vacationClaimDto) {
        return vacationClaimService.create(vacationClaimDto);
    }

    @PutMapping("/{id}")
    public VacationClaimDto update(@RequestBody VacationClaimDto vacationClaimDto, @PathVariable("id") UUID id) {
        return vacationClaimService.update(vacationClaimDto, id);
    }

    @GetMapping
    public List<VacationClaimDto> findAll() {
        return vacationClaimService.findAll();
    }

    @GetMapping("/{id}")
    public VacationClaimDto findById(@PathVariable("id") UUID id) {
        return vacationClaimService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") UUID id) {
        vacationClaimService.deleteById(id);
    }
}
