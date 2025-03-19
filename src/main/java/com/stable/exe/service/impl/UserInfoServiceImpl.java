//package com.stable.exe.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import com.stable.exe.emus.ResponseCode;
//import com.stable.exe.emus.UserRoleEnum;
//import com.stable.exe.entity.UserInfo;
//import com.stable.exe.exception.CommonException;
//import com.stable.exe.service.UserInfoService;
//import com.stable.exe.utils.AESUtil;
//import com.stable.exe.utils.MapBuildComponent;
//import com.stable.exe.utils.MaskUtil;
//import com.stable.exe.web.request.UserInfoRequest;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//
//@Slf4j
//@Service
//public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper,UserInfo> implements UserInfoService {
//
//    @Resource
//    private UserInfoMapper userInfoMapper;
//
//    @Override
//    public Boolean register(UserInfoRequest request) {
//        try {
//            UserInfo userInfo = new UserInfo();
//            userInfo.setName(request.getName());
//            userInfo.setEmail(StringUtils.isBlank(request.getEmail())?null:request.getEmail());
//            userInfo.setCreated(request.getName());
//            userInfo.setCreatedDate(new Date());
//            userInfo.setPassword(AESUtil.encrypt(request.getPassword()));
//            userInfo.setPhone(AESUtil.encrypt(request.getPhone()));
//            userInfo.setRole(UserRoleEnum.REGULAR_USER.getCode());
//            int num = userInfoMapper.insert(userInfo);
//            return num > 0;
//        } catch (DuplicateKeyException e) {
//            e.printStackTrace();
//            throw new CommonException(ResponseCode.DB_ERROR, e.getMessage());
//        }
//    }
//
//    @Override
//    public Boolean login(UserInfoRequest request) {
//        Boolean exists = this.isExists(request.getName());
//        if (null == exists || !exists) {
//            throw new CommonException(ResponseCode.PARAM_ERROR, "账户不存在，请注册");
//        }
//        try {
//            return userInfoMapper.exists(new QueryWrapper<UserInfo>().allEq(
//                    MapBuildComponent.build()
//                            .put("user_name", request.getName())
//                            .put("user_password", AESUtil.encrypt(request.getPassword())).get()
//            ));
//        } catch (Exception e) {
//            log.error("用户登录失败: ", e);
//            throw new CommonException(ResponseCode.DB_ERROR, e.getMessage());
//        }
//    }
//
//    @Override
//    public Page<UserInfo> getUserPage(UserInfoRequest request) {
//        try {
//            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
//            if(!isAdmin(request.getName())){
//                queryWrapper.eq("user_name", request.getName());
//            }
//            Page<UserInfo> page = userInfoMapper.selectPage(new Page<>(request.getPageNo(), request.getPageSize()), queryWrapper);
//            this.maskUserInfoList(page.getRecords(), Objects.equals(Boolean.FALSE, request.getIsMask()));
//            return page;
//        } catch (Exception e) {
//            log.error("查询用户列表失败: ", e);
//            throw new CommonException(ResponseCode.DB_ERROR, e.getMessage());
//        }
//    }
//
//    @Override
//    public Boolean isExists(String account) {
//        try {
//            return userInfoMapper.exists(new QueryWrapper<UserInfo>().eq("user_name", account));
//        } catch (Exception e) {
//            log.error("校验账户是否存在失败: ", e);
//            throw new CommonException(ResponseCode.DB_ERROR, e.getMessage());
//        }
//    }
//
//    @Override
//    public Boolean isAdmin(String account) {
//        try {
//            Long count = userInfoMapper.selectCount(
//                    new QueryWrapper<UserInfo>().allEq(MapBuildComponent.build()
//                            .put("user_name", account)
//                            .put("user_role", UserRoleEnum.ADMIN_USER.getCode())
//                            .get()
//                    ));
//            return null != count && count > 0;
//        } catch (Exception e) {
//            throw new CommonException(ResponseCode.DB_ERROR, e.getMessage());
//        }
//    }
//
//    @Override
//    public Boolean remove(UserInfoRequest request) {
//        Boolean isAdmin = this.isAdmin(request.getName());
//        if (!isAdmin) {
//            throw new CommonException(ResponseCode.NO_AUTH);
//        }
//        try {
//            int count = userInfoMapper.delete(new QueryWrapper<UserInfo>().eq("user_name", request.getName()));
//            return count > 0;
//        } catch (Exception e) {
//            log.error("删除用户失败: ", e);
//            throw new CommonException(ResponseCode.DB_ERROR, e.getMessage());
//        }
//    }
//
//    @Override
//    public Boolean update(UserInfoRequest request) {
//        try {
//            UserInfo info = new UserInfo();
//            info.setPassword(AESUtil.encrypt(request.getPassword()));
//            info.setEmail(request.getEmail());
//            info.setPhone(AESUtil.encrypt(request.getPhone()));
//            info.setModified(request.getName());
//            info.setModifiedDate(new Date());
//            int count = userInfoMapper.update(info, new QueryWrapper<UserInfo>().eq("user_name", request.getName()));
//            return count > 0;
//        } catch (Exception e) {
//            log.error("修改用户信息失败: ", e);
//            throw new CommonException(ResponseCode.DB_ERROR, e.getMessage());
//        }
//    }
//
//    private void maskUserInfoList(List<UserInfo> list,boolean isMask){
//        if (CollectionUtils.isNotEmpty(list)){
//            list.forEach(e -> {
//                String phone = AESUtil.encrypt(e.getPhone());
//                if (isMask) {
//                    phone = MaskUtil.maskPhone(phone);
//                }
//                e.setPhone(phone);
//            });
//        }
//    }
//}
