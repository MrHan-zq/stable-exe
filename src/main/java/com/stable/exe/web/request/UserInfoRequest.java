package com.stable.exe.web.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoRequest extends BaseRequest{

    /**
     *用户名/账户
     */
    private String name;

    /**
     *密码（加密）
     */
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
     * 是否加掩码
     */
    private Boolean isMask;
}
