package com.example.demo.config.exception;

import java.io.Serializable;

public class ResponseError implements Serializable {

    private String message;

    private ResponseError() {
    }

    public ResponseError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
