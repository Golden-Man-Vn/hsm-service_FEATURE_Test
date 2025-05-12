package com.company.project.controller;

import com.company.project.dto.TasksDTO;
import com.company.project.dto.UsersDTO;
import com.company.project.entity.Tasks;
import com.company.project.model.ErrorResponse;
import com.company.project.repository.TasksRepository;
import com.company.project.service.TaskDetailService;
import com.company.project.service.TasksService;
import com.company.project.service.UsersService;
import com.company.project.type.TasksStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Log4j2
@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TasksController {
    @Autowired
    UsersService usersService;

    @Autowired
    TaskDetailService taskDetailService;

    @Autowired
    TasksService tasksService;

    @Autowired
    TasksRepository tasksRepository;

    @PostMapping(value = "/assign")
    public ResponseEntity<Object> assign(
            @RequestParam(name = "userId", required = true) Integer userId,
            @RequestParam(name = "tasksDetailId", required = true) Integer tasksDetailId,
            @RequestParam(name = "note", required = false) String note) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);

        // validate input first
        if((note != null && note.length() > Tasks.TASKS_CONTENT_MAX_LENGTH)){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        var result = usersService.assign(userId, tasksDetailId, note);

        if(result.getKey() == UsersService.ASSIGN_RETURN_USER_NOT_EXIST
                || result.getKey() == UsersService.ASSIGN_RETURN_TASK_DETAIL_NOT_EXIST){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "user or task not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        else if(result.getKey() == UsersService.ASSIGN_RETURN_ALREADY_ASSIGN){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "user already assigned this task detail");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        else if(result.getKey() == UsersService.ASSIGN_RETURN_FAILED){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_FAILED, "unknown error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        TasksDTO dto = TasksDTO.from(result.getValue());
        errorResponse.setData(dto);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
            @PathVariable Integer id,
            @RequestParam(name = "usersId", required = true) Integer usersId,
            @RequestParam(name = "tasksDetailId", required = true) Integer tasksDetailId,
            @RequestParam(name = "status", required = true) Integer status,
            @RequestParam(name = "note", required = false) String note) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);
        if(id == null
                || (note != null && note.length() > Tasks.TASKS_CONTENT_MAX_LENGTH)
                || (status != null && !TasksStatus.isValid(status))){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        var result = tasksService.update(
                id,
                usersId,
                tasksDetailId,
                note,
                status);

        switch (result.getKey()){
            case TasksService.UPDATE_RETURN_SUCCESS:
                break;
            case TasksService.UPDATE_RETURN_TASKS_DETAIL_NOT_EXIST:
                errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "task detail not exist");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            case TasksService.UPDATE_RETURN_USERS_NOT_EXIST:
                errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "user not exist");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            case TasksService.UPDATE_RETURN_NOT_EXIST:
                errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "task not exist");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            default:
                errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_FAILED, "unknown error");
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        TasksDTO dto = TasksDTO.from(result.getValue());
        errorResponse.setData(dto);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> get(
            @PathVariable("id") Integer id) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);

        var tasks = tasksRepository.findOne(id);
        if(tasks == null){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "tasks not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        TasksDTO dto = TasksDTO.from(tasks);
        errorResponse.setData(dto);

        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(
            @PathVariable("id") Integer id) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);

        var tasks = tasksRepository.findOne(id);
        if(tasks == null){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "tasks not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        tasksRepository.delete(tasks);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAll() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);

        var list = tasksRepository.findAll();
        var listDTO = new ArrayList<TasksDTO>();
        for (var item: list) {
            TasksDTO dto = TasksDTO.from(item);
            listDTO.add(dto);
        }

        errorResponse.setData(listDTO);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
}