package com.stable.exe.emus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ModelType {

    SD_MODEL(0,"StableDiffusion模型"),
    DB_MODEL(1,"Dreambooth模型")
    ;

    private final int code;

    private final String name;
}
