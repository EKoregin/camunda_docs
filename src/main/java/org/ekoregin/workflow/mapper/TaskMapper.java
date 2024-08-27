package org.ekoregin.workflow.mapper;

import org.camunda.bpm.engine.task.Task;
import org.ekoregin.workflow.dto.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    TaskDto toDto(Task task);

    List<TaskDto> toListDto(List<Task> tasks);
}
