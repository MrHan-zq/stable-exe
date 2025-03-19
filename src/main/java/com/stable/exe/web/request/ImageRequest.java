package com.stable.exe.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImageRequest extends BaseRequest{

    /**
     * 图片名称
     */
    private String fileName;

    /**
     * 图片base64码
     */
    private String base64String;

    /**
     * 图片url地址
     */
    private String imgUrl;

    @JsonProperty("positivePrompt")
    private String positivePrompt;

    private String negativePrompt;

    private Integer height;

    private Integer width;
}
