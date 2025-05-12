package com.company.project.service;

import com.company.project.entity.Tasks;

import com.company.project.repository.TasksDetailRepository;
import com.company.project.repository.TasksRepository;
import com.company.project.repository.UsersRepository;
import com.company.project.type.TasksStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Map;



@Service
@RequiredArgsConstructor
@Log4j2
public class TasksService {
    // update(): return values
    public static final int UPDATE_RETURN_SUCCESS  = 0;
    public static final int UPDATE_RETURN_FAILED   = 1;
    public static final int UPDATE_RETURN_NOT_EXIST = 2;
    public static final int UPDATE_RETURN_USERS_NOT_EXIST = 3;
    public static final int UPDATE_RETURN_TASKS_DETAIL_NOT_EXIST = 4;

    @Autowired
    TasksRepository tasksRepository;

    @Autowired
    TasksDetailRepository tasksDetailRepository;

    @Autowired
    UsersRepository usersRepository;

    public Map.Entry<Integer, Tasks> update(
            Integer id,
            Integer usersId,
            Integer tasksDetailId,
            String note,
            Integer status){
        try{
            Tasks tasks = tasksRepository.findOne(id);
            if(tasks == null){
                return new AbstractMap.SimpleEntry<>(UPDATE_RETURN_NOT_EXIST, null);
            }

            var users = usersRepository.findOne(usersId);
            if(users == null){
                return new AbstractMap.SimpleEntry<>(UPDATE_RETURN_USERS_NOT_EXIST, null);
            }

            var tasksDetail = tasksDetailRepository.findOne(tasksDetailId);
            if(tasksDetail == null){
                return new AbstractMap.SimpleEntry<>(UPDATE_RETURN_TASKS_DETAIL_NOT_EXIST, null);
            }

            tasks.setUsers(users);
            tasks.setTasksDetail(tasksDetail);
            tasks.setNote(note);
            tasks.setStatus(status);

            tasks = tasksRepository.save(tasks);
            return new AbstractMap.SimpleEntry<>(UPDATE_RETURN_SUCCESS, tasks);
        }
        catch (Exception ex){
            // for better maintenance
            log.error("update(): id: {}, status: {}, EXCEPTION: ", id, TasksStatus.fromValue(status), ex);
            return new AbstractMap.SimpleEntry<>(UPDATE_RETURN_FAILED, null);
        }
    }
}
