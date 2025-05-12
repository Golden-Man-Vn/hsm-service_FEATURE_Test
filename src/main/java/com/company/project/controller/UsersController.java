package com.company.project.controller;

import com.company.project.dto.TasksDTO;
import com.company.project.dto.UsersDTO;
import com.company.project.entity.Tasks;
import com.company.project.entity.Users;
import com.company.project.model.ErrorResponse;
import com.company.project.repository.UsersRepository;
import com.company.project.service.TaskDetailService;
import com.company.project.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UsersController {
    @Autowired
    UsersService usersService;

    @Autowired
    TaskDetailService taskDetailService;

    @Autowired
    UsersRepository usersRepository;

    @PostMapping(value = "/new")
    public ResponseEntity<Object> create(
            @RequestParam(name = "userName", required = true) String userName,
            @RequestParam(name = "fullName", required = true) String fullName) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);

        // validate input first
        if(userName == null || userName.isEmpty() || userName.contains(" ") || userName.length() > Users.USERS_USER_NAME_MAX_LENGTH
                || fullName == null || fullName.isEmpty() || fullName.length() > Users.USERS_USER_NAME_MAX_LENGTH){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        var result = usersService.create(userName, fullName);
        if(result.getKey() == UsersService.CREATE_RETURN_CONFLICT){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "user name exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        else if(result.getKey() == UsersService.CREATE_RETURN_FAILED){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_FAILED, "unknown error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // mapping entity to DTO if any
        //ModelMapper modelMapper = new ModelMapper();
        //var u = result.getValue();
        //UsersDTO dto = modelMapper.map(result.getValue(), UsersDTO.class);
        UsersDTO dto = UsersDTO.from(result.getValue());
        errorResponse.setData(dto);

        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

//    @PostMapping(value = "/assign")
//    public ResponseEntity<Object> assign(
//            @RequestParam(name = "userId", required = true) Integer userId,
//            @RequestParam(name = "tasksDetailId", required = true) Integer tasksDetailId,
//            @RequestParam(name = "note", required = false) String note) {
//        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);
//
//        // validate input first
//        if((note != null && note.length() > Tasks.TASKS_CONTENT_MAX_LENGTH)){
//            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA);
//            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//        }
//
//        var result = usersService.assign(userId, tasksDetailId, note);
//
//        if(result.getKey() == UsersService.ASSIGN_RETURN_USER_NOT_EXIST
//                || result.getKey() == UsersService.ASSIGN_RETURN_TASK_DETAIL_NOT_EXIST){
//            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "user or task not exist");
//            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//        }
//        else if(result.getKey() == UsersService.ASSIGN_RETURN_FAILED){
//            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_FAILED, "unknown error");
//            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        TasksDTO dto = TasksDTO.from(result.getValue());
//        errorResponse.setData(dto);
//        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
//    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> get(
            @PathVariable("id") Integer id) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);

        var users = usersRepository.findOne(id);
        if(users == null){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "user not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        UsersDTO dto = UsersDTO.from(users);
        errorResponse.setData(dto);

        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(
            @PathVariable("id") Integer id) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);

        var result = usersService.delete(id);
        if(result.getKey() == UsersService.DELETE_RETURN_USER_NOT_EXIST){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "user not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        else if(result.getKey() == UsersService.DELETE_RETURN_FAILED){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_FAILED, "unknown error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @GetMapping(value = "")
    public ResponseEntity<Object> getAll() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);

        var list = usersRepository.findAll();
        var listDTO = new ArrayList<UsersDTO>();
        for (var item: list) {
            UsersDTO dto = UsersDTO.from(item);
            listDTO.add(dto);
        }

        errorResponse.setData(listDTO);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
            @PathVariable Integer id,
            @RequestParam(name = "fullName", required = true) String fullName) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);
        if(id == null
                ||fullName == null || fullName.isEmpty() || fullName.length() > Users.USERS_USER_NAME_MAX_LENGTH){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        var result = usersService.update(id, fullName);
        if(result.getKey() == UsersService.UPDATE_RETURN_USER_NOT_EXIST){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_INVALID_DATA, "user not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        else if(result.getKey() == UsersService.UPDATE_RETURN_FAILED){
            errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_FAILED, "unknown error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        UsersDTO dto = UsersDTO.from(result.getValue());
        errorResponse.setData(dto);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }


    @PostMapping(value = "/search")
    public ResponseEntity<Object> search(
            @RequestParam(name = "userId", required = false) Integer userId,
            @RequestParam(name = "tasksStatus", required = false) Integer tasksStatus,
            @RequestParam(name = "tasksDetailName", required = false) String tasksDetailName) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorResponse.ERROR_RESPONSE_SUCCESS);

        var result = usersService.search(userId, tasksStatus, tasksDetailName);

        List<UsersDTO> list = new ArrayList<>();
        for(var item: result){
            var dto = UsersDTO.fromV2(item);
            list.add(dto);
        }

        errorResponse.setData(list);
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
}
