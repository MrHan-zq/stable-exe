package com.stable.exe.web.response;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfoResponse{

    /**
     *用户名/账户
     */
    private String name;

    /**
     *token）
     */
    private String token;

    /**
     *邮箱地址）
     */
    private String email;

    /**
     *电话号码（加密）
     */
    private String phone;

    /**
     * 最后一次活动时间
     *用于缓存中过期 默认30分钟过期
     */
    private Date activityTime;
}
