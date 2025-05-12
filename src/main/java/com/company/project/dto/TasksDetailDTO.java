package com.company.project.dto;

import com.company.project.entity.TasksDetail;
import com.company.project.util.TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.modelmapper.ModelMapper;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TasksDetailDTO {
    private Integer id;
    private String name;
    private Integer type;
    private String note;

    private Integer bugSeverity;
    private String bugStepsToReproduce;

    private Integer featureBusinessValue;

    @JsonSerialize(using = TimestampSerializer.class)
    private java.sql.Timestamp featureDeadline;

    @JsonSerialize(using = TimestampSerializer.class)
    private java.sql.Timestamp createdAt;

    @JsonSerialize(using = TimestampSerializer.class)
    private java.sql.Timestamp updatedAt;

    public static TasksDetailDTO from(TasksDetail tasksDetail){
        ModelMapper modelMapper = new ModelMapper();
        TasksDetailDTO dto = modelMapper.map(tasksDetail, TasksDetailDTO.class);
        return dto;
    }
}
