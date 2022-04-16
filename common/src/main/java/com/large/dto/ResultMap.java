package com.large.dto;

import java.util.HashMap;

/**
 *@ClassName: ResponseMap
 *@Author: qmj
 *@Date: 2021-9-2 11:25
 */
public class ResultMap extends HashMap<String, Object> {

    public ResultMap success() {
        this.put("code", 200);
        this.put("result", "success");
        return this;
    }

    public ResultMap error() {
        this.put("code", 500);
        this.put("result", "fail");
        return this;
    }

    public ResultMap fail() {
        this.put("result", "fail");
        this.put("code", 400);
        return this;
    }

    public ResultMap message(String message) {
        this.put("message", message);
        return this;
    }

    public ResultMap data(Object object) {
        this.put("data", object);
        return this;
    }

    public ResultMap data(String str, Object object) {
        this.put(str, object);
        return this;
    }

    public ResultMap exception(Integer code, String message) {
        this.put("code", code);
        this.put("message", message);
        return this;
    }
}
