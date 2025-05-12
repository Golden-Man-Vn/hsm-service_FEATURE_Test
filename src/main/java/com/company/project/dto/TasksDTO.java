package com.company.project.dto;

import com.company.project.entity.Tasks;
import com.company.project.util.TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TasksDTO {
    private Integer id;
    private Integer users;
    private Integer tasksDetail;
    private Integer status;
    private String note;

    @JsonSerialize(using = TimestampSerializer.class)
    private java.sql.Timestamp createdAt;

    @JsonSerialize(using = TimestampSerializer.class)
    private java.sql.Timestamp updatedAt;

    private Object data;

    public static TasksDTO from(Tasks tasks){
        TasksDTO tasksDTO = new TasksDTO();
        tasksDTO.setId(tasks.getId());
        tasksDTO.setUsers(tasks.getUsers().getId());
        tasksDTO.setTasksDetail(tasks.getTasksDetail().getId());
        tasksDTO.setStatus(tasks.getStatus());
        tasksDTO.setNote(tasks.getNote());

        return tasksDTO;
    }

    public static TasksDTO fromV2(Tasks tasks){
        TasksDTO dto = from(tasks);

        var taskDetail = tasks.getTasksDetail();
        if(taskDetail == null){
            return dto;
        }

        dto.data = TasksDetailDTO.from(taskDetail);
        return dto;
    }
}
