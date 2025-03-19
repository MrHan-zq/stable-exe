package com.stable.exe.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Concepts {

    private String class_data_dir = "";
    private BigDecimal class_guidance_scale = new BigDecimal("7.5");
    private Integer class_infer_steps = 40;
    private String class_negative_prompt = "";
    private String class_prompt = "";
    private String class_token = "";
    private String instance_data_dir = "";
    private String instance_prompt = "";
    private String instance_token = "";
    private Boolean is_valid = true;
    private Integer n_save_sample = 1;
    private Integer num_class_images_per = 0;
    private Integer sample_seed = -1;
    private BigDecimal save_guidance_scale = new BigDecimal("7.5");
    private Integer save_infer_steps = 20;
    private String save_sample_negative_prompt = "";
    private String save_sample_prompt = "";
    private String save_sample_template = "";
}
