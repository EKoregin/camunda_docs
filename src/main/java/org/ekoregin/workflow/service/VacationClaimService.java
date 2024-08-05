package org.ekoregin.workflow.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ekoregin.workflow.dto.VacationClaimDto;
import org.ekoregin.workflow.exception.EntityNotFoundException;
import org.ekoregin.workflow.mapper.VacationClaimMapper;
import org.ekoregin.workflow.model.VacationClaim;
import org.ekoregin.workflow.repository.VacationClaimRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VacationClaimService {

    private final VacationClaimRepository vacationClaimRepository;
    private final VacationClaimMapper vacationClaimMapper;

    public VacationClaimDto findById(@NonNull UUID id) {
        VacationClaim vacationClaim = vacationClaimRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Vacation claim with id " + id + " not found")
        );
        return vacationClaimMapper.toDto(vacationClaim);
    }

    public List<VacationClaimDto> findAll() {
        List<VacationClaim> claims = new ArrayList<>();
        vacationClaimRepository.findAll().forEach(claims::add);
        return vacationClaimMapper.toDtoList(claims);
    }

    public VacationClaimDto create(VacationClaimDto vacationClaimDto) {
        VacationClaim vacationClaim = vacationClaimMapper.toEntity(vacationClaimDto);
        vacationClaim.setNew(true);
        vacationClaim.setId(UUID.randomUUID());
        vacationClaim.setCreatedAt(LocalDateTime.now());
        vacationClaim.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return vacationClaimMapper.toDto(vacationClaimRepository.save(vacationClaim));
    }

    public VacationClaimDto update(VacationClaimDto vacationClaimDto, UUID id) {
        vacationClaimRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Vacation claim with id " + id + " not found")
        );
        VacationClaim vacationClaim = vacationClaimMapper.toEntity(vacationClaimDto);
        vacationClaim.setNew(false);
        return vacationClaimMapper.toDto(vacationClaimRepository.save(vacationClaim));
    }

    public void deleteById(UUID id) {
        vacationClaimRepository.deleteById(id);
    }
}
