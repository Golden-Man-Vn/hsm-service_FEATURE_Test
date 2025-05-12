package com.company.project.test;

import com.company.project.entity.Users;
import com.company.project.repository.UsersRepository;
import com.company.project.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.company.project.service.*;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UsersServiceTest {

    @Autowired
    private UsersService userService;

    @Autowired
    UsersRepository usersRepository;

    @Test
    void testCreate() {
        String userName = "User_" + DateUtil.toString(new Date(), "yyyyMMddHHmmss");
        String fullName = "Full name: " + DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss");;

        var result = userService.create(userName, fullName);

        assertEquals(result.getKey(), UsersService.CREATE_RETURN_SUCCESS);
        assertEquals(result.getValue().getUserName(), userName);
        assertEquals(result.getValue().getFullName(), fullName);
    }

    @Test
    void testUpdate() {
        var list = usersRepository.findAll();
        if(list.size() == 0){
            return;
        }

        Random random = new Random();
        int number = random.nextInt(list.size());
        Users users = list.get(number);

        String userName = users.getUserName();
        String fullName = UUID.randomUUID().toString();

        var result = userService.update(users.getId(), fullName);

        assertEquals(result.getKey(), UsersService.UPDATE_RETURN_SUCCESS);
        assertEquals(result.getValue().getUserName(), userName);
        assertEquals(result.getValue().getFullName(), fullName);
    }
}