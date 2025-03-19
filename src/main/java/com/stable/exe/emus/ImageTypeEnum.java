package com.stable.exe.emus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageTypeEnum {

    TXT_TO_IMG("txt2img","文生图"),
    IMG_TO_IMG("img2img","图生图"),
    ALL_IMG("all","所有")
    ;

    private final String type;

    private final String name;
}
