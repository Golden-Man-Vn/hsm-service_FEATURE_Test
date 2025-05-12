package com.company.project.test;

import com.company.project.entity.Users;
import com.company.project.repository.UsersRepository;
import com.company.project.service.TaskDetailService;
import com.company.project.service.UsersService;
import com.company.project.type.BugServerity;
import com.company.project.type.TasksDetailType;
import com.company.project.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TaskDetailServiceTest {

    @Autowired
    private UsersService userService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    TaskDetailService taskDetailService;

    @Test
    void testCreateBug() {
        String name = "Bug " + DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss");
        String note = "Often occur";

        Random random = new Random();
        int number = random.nextInt(BugServerity.CRITICAL.ordinal() + 1);
        Integer bugSeverity = number;

        String bugStepsToReproduce = "1. GUI inspect \n2. Mobile setting screen \n3. Blue screen occur";

        var result = taskDetailService.create(
                name,
                TasksDetailType.BUG.ordinal(),
                note,
                bugSeverity,
                bugStepsToReproduce,
                null,
                null);

        assertEquals(result.getKey(), TaskDetailService.CREATE_RETURN_SUCCESS);
        assertEquals(result.getValue().getName(), name);
        assertEquals(result.getValue().getNote(), note);
        assertEquals(result.getValue().getBugSeverity(), bugSeverity);
    }

    @Test
    void testCreateFeature() {
        String name = "Feature " + DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss");
        String note = "New feature for upgrade";

        Random random = new Random();
        int number = random.nextInt(100);
        Integer featureBusinessValue = number;

        java.sql.Timestamp featureDeadline = new java.sql.Timestamp((new Date()).getTime());
        var result = taskDetailService.create(
                name,
                TasksDetailType.BUG.ordinal(),
                note,
                null,
                null,
                featureBusinessValue,
                featureDeadline);

        assertEquals(result.getKey(), TaskDetailService.CREATE_RETURN_SUCCESS);
        assertEquals(result.getValue().getName(), name);
        assertEquals(result.getValue().getNote(), note);
        assertEquals(result.getValue().getFeatureBusinessValue(), featureBusinessValue);
        assertEquals(result.getValue().getFeatureDeadline().toString(), featureDeadline.toString());
    }
}