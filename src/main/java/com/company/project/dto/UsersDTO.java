package com.company.project.dto;

import com.company.project.entity.Users;
import com.company.project.util.TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Column;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {
    private Integer id;
    private String userName;
    private String fullName;

    private Object data;

    @JsonSerialize(using = TimestampSerializer.class)
    private java.sql.Timestamp createdAt;

    @JsonSerialize(using = TimestampSerializer.class)
    private java.sql.Timestamp updatedAt;

    public static UsersDTO from(Users users){
        ModelMapper modelMapper = new ModelMapper();
        UsersDTO dto = modelMapper.map(users, UsersDTO.class);
        return dto;
    }

    public static UsersDTO fromV2(Users users){
        UsersDTO dto = from(users);

        var setTask = users.getSetTask();
        if(setTask == null){
            return dto;
        }

        List<TasksDTO> list = new ArrayList<>();
        for(var item: setTask){
            list.add(TasksDTO.fromV2(item));
        }
        dto.data = list;

        return dto;
    }
}
