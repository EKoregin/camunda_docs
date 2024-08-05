package org.ekoregin.workflow.service;

import org.ekoregin.workflow.dto.VacationClaimDto;
import org.ekoregin.workflow.exception.EntityNotFoundException;
import org.ekoregin.workflow.mapper.VacationClaimMapper;
import org.ekoregin.workflow.model.ClaimStatus;
import org.ekoregin.workflow.model.VacationClaim;
import org.ekoregin.workflow.repository.VacationClaimRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VacationClaimServiceTest {

    private static final UUID CLAIM_ID = UUID.fromString("b4c256c3-c70d-4e05-acbd-6dd0b4737e3e");
    public static final String TEST_USER = "Test user";

    @Mock
    private VacationClaimRepository vacationClaimRepository;

    @Mock
    private VacationClaimMapper vacationClaimMapper;

    @InjectMocks
    private VacationClaimService vacationClaimService;

    VacationClaimDto vacationClaimDto;
    VacationClaim vacationClaim;
    VacationClaim vacationSavedClaim;
    VacationClaimDto vacationClaimSavedDto;

    @BeforeAll
    public static void securityContextInit() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @BeforeEach
    public void init() {
        vacationClaimDto = VacationClaimDto.builder()
                .status(ClaimStatus.NEW)
                .description("I'm tired")
                .startedAt(LocalDate.of(2024, 9, 1))
                .finishedAt(LocalDate.of(2024, 9, 21))
                .build();
        vacationClaim = VacationClaim.builder()
                .status(ClaimStatus.NEW)
                .description("I'm tired")
                .startedAt(LocalDate.of(2024, 9, 1))
                .finishedAt(LocalDate.of(2024, 9, 21))
                .build();
        vacationSavedClaim = VacationClaim.builder()
                .id(CLAIM_ID)
                .createdBy(TEST_USER)
                .status(ClaimStatus.NEW)
                .description("I'm tired")
                .startedAt(LocalDate.of(2024, 9, 1))
                .finishedAt(LocalDate.of(2024, 9, 21))
                .build();
        vacationClaimSavedDto = VacationClaimDto.builder()
                .id(CLAIM_ID)
                .createdBy(TEST_USER)
                .status(ClaimStatus.NEW)
                .description("I'm tired")
                .startedAt(LocalDate.of(2024, 9, 1))
                .finishedAt(LocalDate.of(2024, 9, 21))
                .build();
    }

    @Test
    public void createTest() {
        when(vacationClaimMapper.toEntity(vacationClaimDto)).thenReturn(vacationClaim);
        when(vacationClaimMapper.toDto(vacationSavedClaim)).thenReturn(vacationClaimSavedDto);
        when(vacationClaimRepository.save(vacationClaim)).thenReturn(vacationSavedClaim);

        VacationClaimDto actualResult = vacationClaimService.create(vacationClaimDto);

        assertEquals(vacationClaimSavedDto, actualResult);
        verify(vacationClaimRepository).save(vacationClaim);
    }

    @Test
    public void updateWhenOk() {
        vacationClaimDto.setStatus(ClaimStatus.APPROVED);
        vacationClaim.setStatus(ClaimStatus.APPROVED);

        when(vacationClaimRepository.findById(CLAIM_ID)).thenReturn(Optional.ofNullable(vacationSavedClaim));
        when(vacationClaimMapper.toEntity(vacationClaimDto)).thenReturn(vacationClaim);
        when(vacationClaimMapper.toDto(vacationSavedClaim)).thenReturn(vacationClaimSavedDto);
        when(vacationClaimRepository.save(vacationClaim)).thenReturn(vacationSavedClaim);

        VacationClaimDto actualResult = vacationClaimService.update(vacationClaimDto, CLAIM_ID);

        assertEquals(vacationClaimSavedDto, actualResult);
    }

    @Test
    public void updateWhenNotFound() {
        String errMessage = String.format("Vacation claim with id %s not found", CLAIM_ID);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> vacationClaimService.update(vacationClaimDto, CLAIM_ID));

        assertEquals(errMessage, exception.getMessage());
    }


    @Test
    public void findByIdWhenOk() {
        when(vacationClaimRepository.findById(CLAIM_ID)).thenReturn(Optional.ofNullable(vacationSavedClaim));
        when(vacationClaimMapper.toDto(vacationSavedClaim)).thenReturn(vacationClaimSavedDto);

        VacationClaimDto actualResult = vacationClaimService.findById(CLAIM_ID);

        assertEquals(vacationClaimSavedDto, actualResult);
    }

    @Test
    public void findByIdWhenNotFound() {
        String errMessage = String.format("Vacation claim with id %s not found", CLAIM_ID);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> vacationClaimService.findById(CLAIM_ID));

        assertEquals(errMessage, exception.getMessage());
    }

    @Test
    public void deleteById() {
        vacationClaimService.deleteById(CLAIM_ID);

        verify(vacationClaimRepository).deleteById(CLAIM_ID);
    }
}