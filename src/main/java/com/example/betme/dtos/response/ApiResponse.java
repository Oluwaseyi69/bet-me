package com.example.betme.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse <T>{
    private String status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success (T data, String message){
        return new ApiResponse<>("success", message,data);
    }

    public static <T> ApiResponse<T> error (String message){
        return new ApiResponse<>("failed", message, null);
    }

}
