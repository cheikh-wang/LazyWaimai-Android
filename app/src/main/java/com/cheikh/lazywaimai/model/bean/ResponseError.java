package com.cheikh.lazywaimai.model.bean;

public class ResponseError extends RuntimeException {

    private int status;
    private String message;

    public ResponseError(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseError{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}