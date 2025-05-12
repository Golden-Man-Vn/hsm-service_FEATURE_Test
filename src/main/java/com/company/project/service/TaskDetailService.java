package com.company.project.service;

import com.company.project.entity.TasksDetail;
import com.company.project.entity.Users;
import com.company.project.repository.TasksDetailRepository;
import com.company.project.repository.TasksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaskDetailService {
    // create(): return values
    public static final int CREATE_RETURN_SUCCESS = 0;
    public static final int CREATE_RETURN_FAILED = 1;

    // delete()
    public static final int DELETE_RETURN_SUCCESS = 0;
    public static final int DELETE_RETURN_FAILED = 1;
    public static final int DELETE_RETURN_NOT_EXIST = 2;

    // update(): return values
    public static final int UPDATE_RETURN_SUCCESS  = 0;
    public static final int UPDATE_RETURN_FAILED   = 1;
    public static final int UPDATE_RETURN_NOT_EXIST = 2;

    @Autowired
    TasksDetailRepository tasksDetailRepository;

    @Autowired
    TasksRepository tasksRepository;

    public Map.Entry<Integer, TasksDetail> create(
            String name,
            Integer type,
            String note,
            Integer bugSeverity,
            String bugStepsToReproduce,
            Integer featureBusinessValue,
            java.sql.Timestamp featureDeadline){
        try{
            TasksDetail test = new TasksDetail();
            test.setName(name);
            test.setType(type);
            test.setNote(note);
            test.setBugSeverity(bugSeverity);
            test.setBugStepsToReproduce(bugStepsToReproduce);
            test.setFeatureBusinessValue(featureBusinessValue);
            test.setFeatureDeadline(featureDeadline);

            TasksDetail td = tasksDetailRepository.saveAndFlush(test);
            return new AbstractMap.SimpleEntry<>(CREATE_RETURN_SUCCESS, td);
        }
        catch (Exception ex){
            // for better maintenance
            log.error("create(): type: {}, name: {}, EXCEPTION: ", type, name, ex);
            return new AbstractMap.SimpleEntry<>(CREATE_RETURN_FAILED, null);
        }
    }

    @Transactional
    public Map.Entry<Integer, TasksDetail> delete(
            Integer tasksDetailId){
        try{
            TasksDetail tasksDetail = tasksDetailRepository.findOne(tasksDetailId);
            if(tasksDetail == null){
                return new AbstractMap.SimpleEntry<>(DELETE_RETURN_NOT_EXIST, null);
            }

            // delete tasks related
            if(tasksDetail.getSetTask() != null && tasksDetail.getSetTask().size() > 0){
                var listTasks = tasksDetail.getSetTask();
                for (var item: listTasks) {
                    tasksRepository.delete(item);
                }
            }

            // delete user itself
            tasksDetailRepository.delete(tasksDetail);
            return new AbstractMap.SimpleEntry<>(DELETE_RETURN_SUCCESS, null);
        }
        catch (Exception ex){
            // for better maintenance
            log.error("delete(): tasksDetailId: {}, EXCEPTION: ", tasksDetailId, ex);
            return new AbstractMap.SimpleEntry<>(DELETE_RETURN_FAILED, null);
        }
    }

    public Map.Entry<Integer, TasksDetail> update(
            Integer tasksDetailId,
            String name,
            String note,
            Integer type,
            Integer bugSeverity,
            String bugStepsToReproduce,
            Integer featureBusinessValue,
            java.sql.Timestamp featureDeadline){
        try{
            TasksDetail tasksDetail = tasksDetailRepository.findOne(tasksDetailId);
            if(tasksDetail == null){
                return new AbstractMap.SimpleEntry<>(UPDATE_RETURN_NOT_EXIST, null);
            }

            tasksDetail.setName(name);
            tasksDetail.setType(type);
            tasksDetail.setNote(note);
            tasksDetail.setBugSeverity(bugSeverity);
            tasksDetail.setBugStepsToReproduce(bugStepsToReproduce);
            tasksDetail.setFeatureBusinessValue(featureBusinessValue);
            tasksDetail.setFeatureDeadline(featureDeadline);

            tasksDetail = tasksDetailRepository.save(tasksDetail);
            return new AbstractMap.SimpleEntry<>(UPDATE_RETURN_SUCCESS, tasksDetail);
        }
        catch (Exception ex){
            // for better maintenance
            log.error("update(): tasksDetailId: {}, type: {}, EXCEPTION: ", tasksDetailId, type, ex);
            return new AbstractMap.SimpleEntry<>(UPDATE_RETURN_FAILED, null);
        }
    }
}