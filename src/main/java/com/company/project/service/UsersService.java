package com.company.project.service;

import com.company.project.entity.Tasks;
import com.company.project.entity.TasksDetail;
import com.company.project.entity.Users;
import com.company.project.repository.TasksDetailRepository;
import com.company.project.repository.TasksRepository;
import com.company.project.repository.UsersRepository;
import com.company.project.type.TasksStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class UsersService {
    // create(): return values
    public static final int CREATE_RETURN_SUCCESS  = 0;
    public static final int CREATE_RETURN_FAILED   = 1;
    public static final int CREATE_RETURN_CONFLICT = 2;

    // assign(): return values
    public static final int ASSIGN_RETURN_SUCCESS  = 0;
    public static final int ASSIGN_RETURN_FAILED   = 1;
    public static final int ASSIGN_RETURN_USER_NOT_EXIST = 2;
    public static final int ASSIGN_RETURN_TASK_DETAIL_NOT_EXIST = 3;
    public static final int ASSIGN_RETURN_ALREADY_ASSIGN = 4;

    // update(): return values
    public static final int UPDATE_RETURN_SUCCESS  = 0;
    public static final int UPDATE_RETURN_FAILED   = 1;
    public static final int UPDATE_RETURN_USER_NOT_EXIST = 2;

    // delete(): return values
    public static final int DELETE_RETURN_SUCCESS  = 0;
    public static final int DELETE_RETURN_FAILED   = 1;
    public static final int DELETE_RETURN_USER_NOT_EXIST = 2;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    TasksDetailRepository tasksDetailRepository;

    @Autowired
    TasksRepository tasksRepository;


    public Map.Entry<Integer, Users> create(String userName, String fullName){
        try{
            Users test = new Users();
            test.setUserName(userName);
            test.setFullName(fullName);

            Users a = usersRepository.saveAndFlush(test);
            return new AbstractMap.SimpleEntry<>(CREATE_RETURN_SUCCESS, a);
        }
        catch (Exception ex){
            // for better maintenance
            log.error("create(): userName: {}, fullName: {}, EXCEPTION: ", userName, fullName, ex);
            if((ex instanceof DataIntegrityViolationException) || (ex instanceof ConstraintViolationException)){
                return new AbstractMap.SimpleEntry<>(CREATE_RETURN_CONFLICT, null);
            }

            return new AbstractMap.SimpleEntry<>(CREATE_RETURN_FAILED, null);
        }
    }


    public Map.Entry<Integer, Tasks> assign(
            Integer userId,
            Integer tasksDetailId,
            String note){
        try{
            Users users = usersRepository.findOne(userId);
            if(users == null){
                return new AbstractMap.SimpleEntry<>(ASSIGN_RETURN_USER_NOT_EXIST, null);
            }

            TasksDetail tasksDetail = tasksDetailRepository.findOne(tasksDetailId);
            if(tasksDetail == null){
                return new AbstractMap.SimpleEntry<>(ASSIGN_RETURN_TASK_DETAIL_NOT_EXIST, null);
            }

            Tasks tasks = tasksRepository.search(userId, tasksDetailId);
            if(tasks != null){
                return new AbstractMap.SimpleEntry<>(ASSIGN_RETURN_ALREADY_ASSIGN, null);
            }

            tasks = new Tasks();
            tasks.setStatus(TasksStatus.OPEN.ordinal());
            tasks.setUsers(users);
            tasks.setTasksDetail(tasksDetail);
            tasks.setNote(note);

            var result = tasksRepository.saveAndFlush(tasks);
            return new AbstractMap.SimpleEntry<>(ASSIGN_RETURN_SUCCESS, result);
        }
        catch (Exception ex){
            // for better maintenance
            log.error("assign(): userId: {}, tasksDetailId: {}, EXCEPTION: ", userId, tasksDetailId, ex);
            return new AbstractMap.SimpleEntry<>(ASSIGN_RETURN_FAILED, null);
        }
    }

    public Map.Entry<Integer, Users> update(
            Integer userId,
            String fullName){
        try{
            Users users = usersRepository.findOne(userId);
            if(users == null){
                return new AbstractMap.SimpleEntry<>(UPDATE_RETURN_USER_NOT_EXIST, null);
            }

            users.setFullName(fullName);
            users = usersRepository.save(users);

            return new AbstractMap.SimpleEntry<>(UPDATE_RETURN_SUCCESS, users);
        }
        catch (Exception ex){
            // for better maintenance
            log.error("update(): userId: {}, fullName: {}, EXCEPTION: ", userId, fullName, ex);
            return new AbstractMap.SimpleEntry<>(UPDATE_RETURN_FAILED, null);
        }
    }

    @Transactional
    public Map.Entry<Integer, Users> delete(
            Integer userId){
        try{
            Users users = usersRepository.findOne(userId);
            if(users == null){
                return new AbstractMap.SimpleEntry<>(DELETE_RETURN_USER_NOT_EXIST, null);
            }

            // delete tasks related
            if(users.getSetTask() != null && users.getSetTask().size() > 0){
                var listTasks = users.getSetTask();
                for (var item: listTasks) {
                    tasksRepository.delete(item);
                }
            }

            // delete user itself
            usersRepository.delete(users);
            return new AbstractMap.SimpleEntry<>(UPDATE_RETURN_SUCCESS, null);
        }
        catch (Exception ex){
            // for better maintenance
            log.error("delete(): userId: {}, EXCEPTION: ", userId, ex);
            return new AbstractMap.SimpleEntry<>(UPDATE_RETURN_FAILED, null);
        }
    }

    @Transactional
    public List<Users> search(
            Integer userId,
            Integer tasksStatus,
            String tasksDetailName){
        List<Users> result = usersRepository.search(userId, tasksStatus, tasksDetailName);
        return result;
    }
}
