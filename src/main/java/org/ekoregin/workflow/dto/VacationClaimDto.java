package org.ekoregin.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ekoregin.workflow.model.ClaimStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VacationClaimDto {

    private UUID id;
    private String createdBy;
    private ClaimStatus status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDate startedAt;
    private LocalDate finishedAt;
}
