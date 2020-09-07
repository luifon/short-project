package com.example.demo.config.Exception;

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
