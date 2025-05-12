package com.company.project.controller;

import com.company.project.dto.TasksDetailDTO;
import com.company.project.entity.TasksDetail;
import com.company.project.model.ErrorResponse;
import com.company.project.repository.TasksDetailRepository;
import com.company.project.service.TaskDetailService;
import com.company.project.service.UsersService;
import com.company.project.type.BugServerity;
import com.company.project.type.TasksDetailType;
import com.company.project.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@Log4j2
@RestController
@RequestMapping("tasks-detail")
@RequiredArgsConstructor
public class TasksDetailController {
    @Autowired
    TaskDetailService taskDetailService;

    @Autowired
    TasksDetailRepository tasksDetailRepository;

    @PostMapping(value = "/bug/new")
    public ResponseEntity<Object> createBug(
            @RequestParam(name = "name", required = true) String name,
            @RequestParam(name = "note", required = false) String note,
            @RequestParam(name = "bugSeverity", required = true) Integer bugSeverity,
            @RequestParam(name = "bugStepsToReproduce", required = false) String bugStepsToReproduce) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);

        // validate input first
        if (name == null || name.isEmpty() || name.length() > TasksDetail.TASKS_DETAIL_NAME_MAX_LENGTH
            || (bugSeverity != null && !BugServerity.isValid(bugSeverity))
            || (bugStepsToReproduce != null && bugStepsToReproduce.length() > TasksDetail.TASKS_DETAIL_CONTENT_MAX_LENGTH)) {
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        var result = taskDetailService.create(
                name,
                TasksDetailType.BUG.ordinal(),
                note,
                bugSeverity,
                bugStepsToReproduce,
                null,
                null);
        if (result.getKey() == TaskDetailService.CREATE_RETURN_FAILED) {
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_FAILED, "unknown error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // mapping entity to DTO if any
        ModelMapper modelMapper = new ModelMapper();
        var u = result.getValue();
        TasksDetailDTO dto = modelMapper.map(result.getValue(), TasksDetailDTO.class);
        errorResponse.setData(dto);

        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/feature/new")
    public ResponseEntity<Object> createFeature(
            @RequestParam(name = "name", required = true) String name,
            @RequestParam(name = "note", required = false) String note,
            @RequestParam(name = "featureBusinessValue", required = true) Integer featureBusinessValue,
            @RequestParam(name = "featureDeadline", required = false) String featureDeadline) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);
        Date dtDeadLine = DateUtil.fromString(featureDeadline, "yyyy-MM-dd HH:mm:ss");

        // validate input first
        if (name == null || name.isEmpty() || name.length() > TasksDetail.TASKS_DETAIL_NAME_MAX_LENGTH
                || (featureBusinessValue != null && featureBusinessValue < 0)
                || (featureDeadline != null && dtDeadLine == null)) {
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        var result = taskDetailService.create(
                name,
                TasksDetailType.FEATURE.ordinal(),
                note,
                null,
                null,
                featureBusinessValue,
                dtDeadLine != null? new java.sql.Timestamp(dtDeadLine.getTime()): null);
        if (result.getKey() == TaskDetailService.CREATE_RETURN_FAILED) {
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_FAILED, "unknown error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // mapping entity to DTO if any
        //ModelMapper modelMapper = new ModelMapper();
        //var u = result.getValue();
        //TasksDetailDTO dto = modelMapper.map(result.getValue(), TasksDetailDTO.class);
        TasksDetailDTO dto = TasksDetailDTO.from(result.getValue());
        errorResponse.setData(dto);

        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> get(
            @PathVariable("id") Integer id) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);

        var tasksDetail = tasksDetailRepository.findOne(id);
        if(tasksDetail == null){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "task detail not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        var dto = TasksDetailDTO.from(tasksDetail);
        errorResponse.setData(dto);

        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAll() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);

        var list = tasksDetailRepository.findAll();
        var listDTO = new ArrayList<TasksDetailDTO>();
        for (var item: list) {
            TasksDetailDTO dto = TasksDetailDTO.from(item);
            listDTO.add(dto);
        }

        errorResponse.setData(listDTO);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(
            @PathVariable("id") Integer id) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);

        var result = taskDetailService.delete(id);
        if(result.getKey() == TaskDetailService.DELETE_RETURN_NOT_EXIST){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "task detail not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        else if(result.getKey() == TaskDetailService.DELETE_RETURN_FAILED){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_FAILED, "unknown error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @PutMapping("/bug/{id}")
    public ResponseEntity<Object> updateBug(
            @PathVariable Integer id,
            //@RequestParam(name = "type", required = true) Integer type,
            @RequestParam(name = "name", required = true) String name,
            @RequestParam(name = "note", required = false) String note,
            @RequestParam(name = "bugSeverity", required = true) Integer bugSeverity,
            @RequestParam(name = "bugStepsToReproduce", required = false) String bugStepsToReproduce) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);
        if(id == null
            || name == null || name.isEmpty() || name.length() > TasksDetail.TASKS_DETAIL_NAME_MAX_LENGTH
            || (note != null && note.length() > TasksDetail.TASKS_DETAIL_CONTENT_MAX_LENGTH)
            || (bugSeverity != null && !BugServerity.isValid(bugSeverity))
            || (bugStepsToReproduce!= null && bugStepsToReproduce.length() > TasksDetail.TASKS_DETAIL_CONTENT_MAX_LENGTH)){

            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        var result = taskDetailService.update(
                id,
                name,
                note,
                TasksDetailType.BUG.ordinal(),
                bugSeverity,
                bugStepsToReproduce,
                null,
                null);
        if(result.getKey() == TaskDetailService.UPDATE_RETURN_NOT_EXIST){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "task detail not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        else if(result.getKey() == TaskDetailService.UPDATE_RETURN_FAILED){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_FAILED, "unknown error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        TasksDetailDTO dto = TasksDetailDTO.from(result.getValue());
        errorResponse.setData(dto);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @PutMapping("/feature/{id}")
    public ResponseEntity<Object> updateFeature(
            @PathVariable Integer id,
            //@RequestParam(name = "type", required = true) Integer type,
            @RequestParam(name = "name", required = true) String name,
            @RequestParam(name = "note", required = false) String note,
            @RequestParam(name = "featureBusinessValue", required = true) Integer featureBusinessValue,
            @RequestParam(name = "featureDeadline", required = false) String featureDeadline) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);
        Date dtDeadLine = DateUtil.fromString(featureDeadline, "yyyy-MM-dd HH:mm:ss");

        if(id == null
            || name == null || name.isEmpty() || name.length() > TasksDetail.TASKS_DETAIL_NAME_MAX_LENGTH
            || (note != null && note.length() > TasksDetail.TASKS_DETAIL_CONTENT_MAX_LENGTH)
            || featureBusinessValue < 0
            || (featureDeadline != null && dtDeadLine == null)){

            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        var result = taskDetailService.update(
                id,
                name,
                note,
                TasksDetailType.FEATURE.ordinal(),
                null,
                null,
                featureBusinessValue,
                dtDeadLine != null? new java.sql.Timestamp(dtDeadLine.getTime()): null);
        if(result.getKey() == TaskDetailService.UPDATE_RETURN_NOT_EXIST){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "task detail not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        else if(result.getKey() == TaskDetailService.UPDATE_RETURN_FAILED){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_FAILED, "unknown error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        TasksDetailDTO dto = TasksDetailDTO.from(result.getValue());
        errorResponse.setData(dto);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
}