package com.jfeat.am.module.bonus.util;

import com.jfeat.crud.base.exception.BusinessCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorCip extends Cip {
    private List<Map<String, String>> errors = new ArrayList();

    public ErrorCip add(String field, String error) {
        Map<String, String> map = new HashMap();
        map.put(field, error);
        this.errors.add(map);
        return this;
    }

    public List<Map<String, String>> getErrors() {
        return this.errors;
    }

    public void setErrors(List<Map<String, String>> errors) {
        this.errors = errors;
    }

    public ErrorCip(int code, String message) {
        this.status_code = code;
        this.message = message;
    }

    public ErrorCip(BusinessCode businessCode) {
        this.status_code = businessCode.getCode();
        this.message = businessCode.getMessage();
    }

    public static ErrorCip create(int code, String message) {
        return new ErrorCip(code, message);
    }

    public static ErrorCip create(BusinessCode businessCode) {
        return new ErrorCip(businessCode);
    }
}
