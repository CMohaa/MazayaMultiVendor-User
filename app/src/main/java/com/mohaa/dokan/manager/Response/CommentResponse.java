package com.mohaa.dokan.manager.Response;



public class CommentResponse {
    private boolean error;
    private String message;

    public CommentResponse() {

    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}