package com.company.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ErrorResponse {
    public static final int ERROR_RESPONSE_SUCCESS          = 0;
    public static final int ERROR_RESPONSE_FAILED           = 1;
    public static final int ERROR_RESPONSE_INVALID_DATA     = 2;
    public static final int ERROR_RESPONSE_INVALID_ACCOUNT  = 3;
    public static final int ERROR_RESPONSE_NOT_ENOUGH       = 4;
    public static final int ERROR_RESPONSE_OVER_FLOW        = 5;

    private int code;
    private String message;
    private Object data;

    public ErrorResponse(){
        code = ERROR_RESPONSE_SUCCESS;
        setCode(code);
    }

    public ErrorResponse(int code){
        this.code = code;
        setCode(code);
    }

    public ErrorResponse(int code, String extra){
        this.code = code;
        setCode(code);
        message += ": " + extra;
    }

    public void setCode(int code){
        switch (code){
            case ERROR_RESPONSE_SUCCESS:
                message = "Success";
                break;
            case ERROR_RESPONSE_FAILED:
                message = "Failed";
                break;
            case ERROR_RESPONSE_INVALID_DATA:
                message = "Failed - Invalid data";
                break;
            case ERROR_RESPONSE_INVALID_ACCOUNT:
                message = "Failed - Account not exist";
                break;
            case ERROR_RESPONSE_NOT_ENOUGH:
                message = "Failed - Money not enough";
                break;
            case ERROR_RESPONSE_OVER_FLOW:
                message = "Failed - Money not add because of overflow of target account";
                break;
            default:
                message = "Failed - unknown error";
                break;
        }
    }

//    public void setCode(int code, String extra){
//        setCode(code);
//        message += ": " + extra;
//    }
}
