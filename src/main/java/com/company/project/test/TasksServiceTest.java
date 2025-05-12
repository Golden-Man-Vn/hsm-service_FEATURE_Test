package com.company.project.test;

import com.company.project.entity.TasksDetail;
import com.company.project.entity.Users;
import com.company.project.repository.TasksDetailRepository;
import com.company.project.repository.UsersRepository;
import com.company.project.service.TasksService;
import com.company.project.service.UsersService;
import com.company.project.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TasksServiceTest {
    @Autowired
    private UsersService usersService;

    @Autowired
    TasksService tasksService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    TasksDetailRepository tasksDetailRepository;

    @Test
    @Transactional
    @Rollback(false)
    void testAssign() {
        var listUsers = usersRepository.findAll();
        if(listUsers.size() == 0){
            return;
        }

        // find someone free
        Users users = null;
        for(var item: listUsers){
            //var test = item.getSetTask();
            if(item.getSetTask() == null || item.getSetTask().size() == 0){
                users = item;
                break;
            }
        }

        if(users == null){
            Random random = new Random();
            int number = random.nextInt(listUsers.size());
            users = listUsers.get(number);
        }

        // Find any free tasks detail?
        var listDetail = tasksDetailRepository.findAll();
        TasksDetail tasksDetail = null;
        for(var item: listDetail){
            if(item.getSetTask() == null || item.getSetTask().size() == 0){
                tasksDetail = item;
                break;
            }
        }

        if(tasksDetail == null){
            Random random = new Random();
            int number = random.nextInt(listDetail.size());
            tasksDetail = listDetail.get(number);
        }

        String note = "Assign task at " + DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss");
        var result = usersService.assign(users.getId(), tasksDetail.getId(), note);

        boolean test = (result.getKey() == UsersService.ASSIGN_RETURN_SUCCESS) || (result.getKey() == UsersService.ASSIGN_RETURN_ALREADY_ASSIGN);
        assertEquals(test, true);

        if(result.getKey() == UsersService.ASSIGN_RETURN_SUCCESS){
            assertEquals(result.getValue().getUsers().getId(), users.getId());
            assertEquals(result.getValue().getTasksDetail().getId(), tasksDetail.getId());
            assertEquals(result.getValue().getNote(), note);
        }
    }
}
