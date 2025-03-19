package com.stable.exe.web.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseRequest implements Serializable {

    /**
     * 操作人
     */
    private String operator;
    private String sysCode;
    private Integer pageSize;
    private Integer pageNo;
}
