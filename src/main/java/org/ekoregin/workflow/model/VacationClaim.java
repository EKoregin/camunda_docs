package org.ekoregin.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("vacation_claim")
public class VacationClaim implements Persistable<UUID> {

    @Id
    @Column("id")
    private UUID id;

    @Column("created_by")
    private String createdBy;

    @Column("status")
    private ClaimStatus status;

    @Column("description")
    private String description;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("started_at")
    private LocalDate startedAt;

    @Column("finished_at")
    private LocalDate finishedAt;

    @Transient
    private boolean isNew;
}
