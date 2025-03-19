package com.stable.exe.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ImageInfo implements Serializable {

    /**
     * 图片名称
     */
    private String fileName;

    /**
     * 图片访问地址
     */
    private String imgUrl;
}
