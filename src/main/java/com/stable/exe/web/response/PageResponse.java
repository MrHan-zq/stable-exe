package com.stable.exe.web.response;

import lombok.Data;
import com.stable.exe.emus.ResponseCode;

import java.util.List;

@Data
public class PageResponse<T> {

    private String responseCode;
    private String responseDesc;
    private Page<T> data;

    public PageResponse(ResponseCode responseCode) {
        this.responseCode = responseCode.getCode();
        this.responseDesc = responseCode.getDesc();
        this.data = new Page<>();
    }


    public PageResponse(List<T> data, int total) {
        this.responseCode = ResponseCode.SUCCESS.getCode();
        this.responseDesc = ResponseCode.SUCCESS.getDesc();
        Page<T> page = new Page<>();
        page.setList(data);
        page.setTotal(total);
        this.data = page;
    }

    public PageResponse(String responseCode, String responseDesc) {
        this.responseCode = responseCode;
        this.responseDesc = responseDesc;
        this.data = new Page<>();
    }

    public PageResponse(String responseCode, String responseDesc,List<T> data, int total) {
        this.responseCode = responseCode;
        this.responseDesc = responseDesc;
        this.data = new Page<>();
        this.data.setList(data);
        this.data.setTotal(total);
    }
}
