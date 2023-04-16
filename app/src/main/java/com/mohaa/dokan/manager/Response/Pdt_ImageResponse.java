package com.mohaa.dokan.manager.Response;



public class Pdt_ImageResponse {
    private boolean error;
    private String message;


    public Pdt_ImageResponse() {

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