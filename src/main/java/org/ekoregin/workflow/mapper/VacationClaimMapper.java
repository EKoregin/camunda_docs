package org.ekoregin.workflow.mapper;

import org.ekoregin.workflow.dto.VacationClaimDto;
import org.ekoregin.workflow.model.VacationClaim;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VacationClaimMapper {

    VacationClaimDto toDto(VacationClaim vacationClaim);

    VacationClaim toEntity(VacationClaimDto vacationClaimDto);

    List<VacationClaimDto> toDtoList(List<VacationClaim> claims);
}
