package com.stable.exe.web.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class DreamBoothModelRequest implements Serializable {

    private String newModelName;
    private String newModelSrc;
    private String newModelSharedSrc;
    private Boolean createFromHub;
    private String newModelUrl;
    private String modelType;
    private Boolean trainUnfrozen;
    private String newModelToken;
    private Boolean newModelExtractEma;
}
