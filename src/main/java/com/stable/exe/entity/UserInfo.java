package com.stable.exe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user_info")
public class UserInfo implements Serializable {

    /**
     *唯一主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     *用户名/账户
     */
    @TableField("user_name")
    private String name;

    /**
     *密码（加密）
     */
    @TableField("user_password")
    private String password;

    /**
     *邮箱地址）
     */
    private String email;

    /**
     *电话号码（加密）
     */
    private String phone;

    /**
     *用户角色 0-管理员 1-普通用户
     */
    @TableField("user_role")
    private Integer role;

    /**
     *创建人
     */
    private String created;

    /**
     *创建时间
     */
    private Date createdDate;

    /**
     *修改人
     */
    private String modified;

    /**
     *修改时间
     */
    private Date modifiedDate;
}
