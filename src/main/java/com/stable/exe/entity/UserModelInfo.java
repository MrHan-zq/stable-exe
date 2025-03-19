package com.stable.exe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_model_mapping")
public class UserModelInfo {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;


    private Integer type;
    @TableField("user_name")
    private String userName;
    private String title;

    @TableField("model_name")
    private String modelName;

    @TableField("hash_value")
    private String hashValue;
    private String filename;
    private String sha256;
    private String config;
    private Integer selected;
    private String created;

    @TableField("created_date")
    private Date createdDate;
    private String modified;

    @TableField("modified_date")
    private Date modifiedDate;
}
