package com.jfeat.am.module.bonus.util;

public class SuccessCip extends Cip {
    private Object data;

    public Object getData() {
        return this.data;
    }

    public SuccessCip setData(Object data) {
        this.data = data;
        return this;
    }

    public SuccessCip(Object data) {
        this();
        this.data = data;
    }

    public SuccessCip() {
        super.status_code = 0;
        super.message = "Success";
    }

    public static SuccessCip create() {
        return new SuccessCip();
    }

    public static SuccessCip create(Object data) {
        return new SuccessCip(data);
    }
}