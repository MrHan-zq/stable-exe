package com.stable.exe.web.response;

import lombok.Data;
import com.stable.exe.emus.ResponseCode;

import java.io.Serializable;


@Data
public class CommonResponse<T> implements Serializable {

    private String responseCode;
    private String responseDesc;
    private T data;

    public CommonResponse(ResponseCode responseCode) {
        this.responseCode = responseCode.getCode();
        this.responseDesc = responseCode.getDesc();
    }

    public CommonResponse(String code, String desc) {
        this.responseCode = code;
        this.responseDesc = desc;
    }

    public CommonResponse(String code, String desc, T data) {
        this.responseCode = code;
        this.responseDesc = desc;
        this.data = data;
    }

    public CommonResponse(ResponseCode responseCode, T data) {
        this.responseCode = responseCode.getCode();
        this.responseDesc = responseCode.getDesc();
        this.data = data;
    }

    public CommonResponse<T> desc(String desc) {
        if (desc != null && !desc.trim().equals("")) {
            this.responseDesc = desc;
        }
        return this;
    }

    public CommonResponse(T data) {
        this.responseCode = ResponseCode.SUCCESS.getCode();
        this.responseDesc = ResponseCode.SUCCESS.getDesc();
        this.data = data;
    }
}
