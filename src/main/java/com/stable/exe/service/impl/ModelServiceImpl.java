//package com.stable.exe.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import jakarta.annotation.Resource;
//import com.stable.exe.entity.UserModelInfo;
//import com.stable.exe.mapper.UserModelInfoMapper;
//import com.stable.exe.service.ModelService;
//import com.stable.exe.utils.CommonUtil;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//public class ModelServiceImpl extends ServiceImpl<UserModelInfoMapper, UserModelInfo> implements ModelService {
//
//
//    @Resource
//    private UserModelInfoMapper userModelInfoMapper;
//
//    @Override
//    public void clearUserModelMapping() {
//        String path = CommonUtil.getWorkspaces() + "\\stable-diffusion-webui\\models\\Stable-diffusion";
//        String dbPath = CommonUtil.getWorkspaces() + "\\stable-diffusion-webui\\models\\dreambooth";
//        Map<String, File> sdMap = Maps.newHashMap();
//        Map<String, File> dbMap = Maps.newHashMap();
//
//        File file = new File(path);
//        File dbFile = new File(dbPath);
//        if (!file.exists() && !file.isDirectory() && !dbFile.exists() && !dbFile.isDirectory()) {
//            return;
//        }
//        File[] files = file.listFiles();
//        File[] dbFiles = dbFile.listFiles();
//        if (null != files) {
//            for (File f : files) {
//                String name = f.getName();
//                if (f.isFile()) {
//                    name = f.getName().split("\\.", -1)[0];
//                }
//                sdMap.put(name, f);
//            }
//        }
//        if (null != dbFiles) {
//            for (File f : dbFiles) {
//                if (f.isDirectory()) {
//                    dbMap.put(f.getName(), f);
//                }
//            }
//        }
//
//        List<String> list = userModelInfoMapper.selectUserList();
//        for (String s : list) {
//            List<UserModelInfo> modelInfos = this.list(new QueryWrapper<UserModelInfo>().eq("user_name", s));
//            List<Long> ids = Optional.ofNullable(modelInfos).orElse(Lists.newArrayList()).stream()
//                    .filter(e -> !sdMap.containsKey(e.getModelName()) && !dbMap.containsKey(e.getModelName())).map(UserModelInfo::getId).toList();
//            userModelInfoMapper.deleteBatchIds(ids);
//        }
//    }
//
//
//}
