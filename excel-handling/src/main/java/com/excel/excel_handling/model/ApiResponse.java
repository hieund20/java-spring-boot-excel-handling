package com.excel.excel_handling.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private int status;

    private String message;
    private T data;
    private Object errors;

    public ApiResponse() {

    }

    public ApiResponse(int status, String message, T data, Object errors) {
        this();
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }
}
