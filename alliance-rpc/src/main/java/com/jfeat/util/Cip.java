package com.jfeat.util;

public abstract class Cip {
    protected int status_code;
    protected String message;

    public Cip() {
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}