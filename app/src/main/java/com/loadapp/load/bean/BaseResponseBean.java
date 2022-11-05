package com.loadapp.load.bean;

import com.blankj.utilcode.util.GsonUtils;

public class BaseResponseBean {
    private int code;
    private String message;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isRequestSuccess(){
        return code == 0 && data != null;
    }

    public String getBodyStr(){
        return GsonUtils.toJson(data);
    }
}
