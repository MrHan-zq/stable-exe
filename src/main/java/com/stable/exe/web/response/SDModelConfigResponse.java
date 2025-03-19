package com.stable.exe.web.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class SDModelConfigResponse implements Serializable {

    private String title;
    private String modelName;
    private String hash;
    private String sha256;
    private String filename;
    private String config;
    private Boolean selected;
}
