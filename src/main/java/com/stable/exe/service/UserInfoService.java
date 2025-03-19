//package com.stable.exe.service;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.IService;
//import com.stable.exe.entity.UserInfo;
//import com.stable.exe.web.request.UserInfoRequest;
//
///**
// * 用户管理
// * @author hanjun
// * {@code @time} 2024-04-15
// */
//public interface UserInfoService  extends IService<UserInfo> {
//
//    Boolean register(UserInfoRequest request);
//
//    Boolean login(UserInfoRequest request);
//
//    /**
//     * 查询所有用户信息（管理员才能查看）
//     * @return
//     */
//    Page<UserInfo> getUserPage(UserInfoRequest request);
//
//    Boolean isExists(String account);
//
//    Boolean isAdmin(String account);
//
//    Boolean remove(UserInfoRequest request);
//
//    Boolean update(UserInfoRequest request);
//}
