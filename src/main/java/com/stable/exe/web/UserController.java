//package com.stable.exe.web;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.google.common.collect.Lists;
//import jakarta.annotation.Resource;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.constraints.NotNull;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import com.stable.exe.emus.ResponseCode;
//import com.stable.exe.entity.UserInfo;
//import com.stable.exe.exception.CommonException;
//import com.stable.exe.utils.CommonUtil;
//import com.stable.exe.web.request.UserInfoRequest;
//import com.stable.exe.web.response.CommonResponse;
//import com.stable.exe.web.response.PageResponse;
//import com.stable.exe.web.response.UserInfoResponse;
//import org.springframework.beans.BeanUtils;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.UUID;
//
//@Slf4j
//@RestController
//@RequestMapping("/user")
//public class UserController {
//
//    @Resource
//    private UserInfoService userInfoService;
//
//    @PostMapping("/register")
//    public CommonResponse<Boolean> register(@RequestBody @NotNull UserInfoRequest request) {
//        try {
//            Boolean isChinese = CommonUtil.isChineseOrSpecialCharacter(request.getName());
//            if (Objects.equals(Boolean.TRUE, isChinese)) {
//                return new CommonResponse<>(ResponseCode.PARAM_ERROR.getCode(), "用户名不能包含中文和字符");
//            }
//            Boolean data = userInfoService.register(request);
//            return new CommonResponse<>(data);
//        } catch (CommonException e) {
//            log.error("注册用户失败: ", e);
//            return new CommonResponse<>(e.getCode().getCode(), e.getMessage());
//        }
//    }
//
//    @PostMapping("/login")
//    public CommonResponse<UserInfoResponse> login(@RequestBody @NotNull UserInfoRequest request,HttpServletRequest httpServletRequest) {
//        if (StringUtils.isBlank(request.getName()) || StringUtils.isBlank(request.getPassword())) {
//            return new CommonResponse<>(ResponseCode.PARAM_ERROR);
//        }
//        try {
//            Boolean isChinese = CommonUtil.isChineseOrSpecialCharacter(request.getName());
//            if (Objects.equals(Boolean.TRUE, isChinese)) {
//                return new CommonResponse<>(ResponseCode.PARAM_ERROR.getCode(), "用户名不能包含中文和字符");
//            }
//            String token = httpServletRequest.getHeader("token");
//            if (StringUtils.isNotBlank(token)) {
//                return new CommonResponse<>(CommonUtil.getCacheUserInfo(httpServletRequest));
//            }
//            Boolean data = userInfoService.login(request);
//            if (Boolean.TRUE.equals(data)) {
//                UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_name", request.getName()));
//                String uuid = UUID.randomUUID().toString().replace("-", "");
//                UserInfoResponse response = new UserInfoResponse();
//                BeanUtils.copyProperties(userInfo, response);
//                response.setToken(uuid);
//                CommonUtil.setCacheUserInfo(uuid, response);
//                return new CommonResponse<>(response);
//            }
//            return new CommonResponse<>(ResponseCode.PARAM_ERROR.getCode(), "登录失败,用户名或密码不正确");
//        } catch (CommonException e) {
//            log.error("用户登录失败: ", e);
//            return new CommonResponse<>(e.getCode().getCode(), e.getMessage());
//        }
//    }
//
//    @GetMapping("/get/user/page")
//    public PageResponse<UserInfo> getUserPage(HttpServletRequest request) {
//        try {
//            UserInfoRequest userInfo = new UserInfoRequest();
//            userInfo.setName(CommonUtil.getCacheUserInfo(request).getName());
//            Page<UserInfo> page = userInfoService.getUserPage(userInfo);
//            List<UserInfo> list = page.getRecords();
//            if (CollectionUtils.isEmpty(list)) {
//                return new PageResponse<>(Lists.newArrayList(), 0);
//            }
//            return new PageResponse<>(list, (int) page.getTotal());
//        } catch (CommonException e) {
//            log.error("查询用户列表失败: ", e);
//            return new PageResponse<>(e.getCode().getCode(), e.getMessage());
//        }
//    }
//
//    @GetMapping("/get/user/detail")
//    public CommonResponse<UserInfo> getUseDetail(HttpServletRequest request) {
//        try {
//            String name = CommonUtil.getCacheUserInfo(request).getName();
//            UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_name", name));
//            return new CommonResponse<>(userInfo);
//        } catch (CommonException e) {
//            log.error("查询用户详情失败: ", e);
//            return new CommonResponse<>(e.getCode().getCode(), e.getMessage());
//        }
//    }
//
//    @PostMapping("/remove")
//    public CommonResponse<Boolean> remove(HttpServletRequest request) {
//        try {
//            UserInfoRequest userInfo = new UserInfoRequest();
//            userInfo.setName(CommonUtil.getCacheUserInfo(request).getName());
//            Boolean data = userInfoService.remove(userInfo);
//            return new CommonResponse<>(data);
//        } catch (CommonException e) {
//            log.error("删除用户失败: ", e);
//            return new CommonResponse<>(e.getCode().getCode(), e.getMessage());
//        }
//    }
//
//    @PostMapping("/modifie")
//    public CommonResponse<Boolean> update(@RequestBody @NotNull UserInfoRequest request, HttpServletRequest httpServletRequest) {
//        try {
//            if (StringUtils.isBlank(request.getName())) {
//                request.setName(CommonUtil.getCacheUserInfo(httpServletRequest).getName());
//            }
//            Boolean flag = userInfoService.update(request);
//            return new CommonResponse<>(flag);
//        } catch (CommonException e) {
//            log.error("查询用户列表失败: ", e);
//            return new CommonResponse<>(e.getCode().getCode(), e.getMessage());
//        }
//    }
//}
