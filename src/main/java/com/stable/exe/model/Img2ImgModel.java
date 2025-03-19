package com.stable.exe.model;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Img2ImgModel extends  Txt2ImgModel {

    public List<String> init_images;
    public Integer resize_mode = 0;
    public Float denoising_strength = 0.75f;
    public Float image_cfg_scale = 1.5f;
    public String mask_image;  // PIL Image mask
    public Integer mask_blur = 4;
    public Integer inpainting_fill = 0;
    public Boolean inpaint_full_res = true;
    public Integer inpaint_full_res_padding = 0;
    public Integer inpainting_mask_invert = 0;
    public Integer initial_noise_multiplier = 1;


    public Img2ImgModel imagesAdd(String img){
        this.init_images=CollectionUtils.isEmpty(this.init_images)? Lists.newArrayList():this.init_images;
        this.init_images.add(img);
        return this;
    }
}
