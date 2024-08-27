package org.ekoregin.workflow.dto;

import lombok.Data;

@Data
public class TaskDto {

    private String id;
    private String name;
    private String description;
    private String assignee;
}
