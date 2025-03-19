package com.stable.exe.exception;


import org.apache.commons.lang3.StringUtils;
import com.stable.exe.emus.ResponseCode;

public class CommonException extends RuntimeException {

    private final ResponseCode code;

    private String message;

    private Object data;

    public CommonException(ResponseCode code) {
        this.code = code;
        this.message = code.getDesc();
    }

    public CommonException(ResponseCode code, String message) {
        this.code = code;
        this.message = message;
        if (StringUtils.isBlank(message) && code != null) {
            this.message = code.getDesc();
        }
    }

    public CommonException(Throwable e, ResponseCode code) {
        super(e);
        this.code = code;
        this.message = code.getDesc();
    }

    public ResponseCode getCode() {
        return code;
    }

    @Override
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
}
