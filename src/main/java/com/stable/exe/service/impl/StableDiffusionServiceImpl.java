package com.stable.exe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stable.exe.api.WebUIApi;
import com.stable.exe.emus.ModelType;
import com.stable.exe.emus.ResponseCode;
import com.stable.exe.entity.UserModelInfo;
import com.stable.exe.exception.CommonException;
import com.stable.exe.service.StableDiffusionService;
import com.stable.exe.utils.CommonUtil;
import com.stable.exe.utils.JacksonUtil;
import com.stable.exe.web.response.SDModelConfigResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class StableDiffusionServiceImpl implements StableDiffusionService {

    private static final Logger log = LoggerFactory.getLogger(StableDiffusionServiceImpl.class);
    @Resource
    private WebUIApi webUIApi;

//    @Resource
//    private ModelService modelService;

    @Override
    public List<SDModelConfigResponse> getSDModelConfigList(HttpServletRequest request) throws Exception {
//        String account = CommonUtil.getCacheUserInfo(request).getName();
//        List<UserModelInfo> modelInfoList = modelService.list(new QueryWrapper<UserModelInfo>().eq("user_name", account)
//                .eq("type", ModelType.SD_MODEL.getCode()));
//        if (CollectionUtils.isNotEmpty(modelInfoList)) {
//            return modelInfoList.stream().map(e -> {
//                SDModelConfigResponse res = new SDModelConfigResponse();
//                BeanUtils.copyProperties(e, res);
//                res.setHash(e.getHashValue());
//                return res;
//            }).toList();
//        }
        String models = webUIApi.getSdModels();
        List<Map<String, Object>> list = JacksonUtil.jsonToObject(models, List.class, Map.class);
        if (CollectionUtils.isEmpty(list)) {
            throw new CommonException(ResponseCode.NO_DATA);
        }
//        //下划线转驼峰
        List<HashMap<String, Object>> mapList = list.stream().map(e -> {
            HashMap<String, Object> map = Maps.newHashMap();
            e.forEach((k, v) -> {
                String key = CommonUtil.underline2Hump(k);
                if (StringUtils.isBlank(key)) {
                    return;
                }
                map.put(key, v);
            });
            return map;
        }).toList();
        List<SDModelConfigResponse> sdModelList = JacksonUtil.jsonToObject(JacksonUtil.toJsonString(mapList), List.class, SDModelConfigResponse.class);
//
        List<SDModelConfigResponse> retList = Optional.ofNullable(sdModelList).orElse(new ArrayList<>()).stream()
//                .filter(e -> StringUtils.equalsIgnoreCase(e.getTitle().substring(0, e.getTitle().indexOf("[")).trim(), "v1-5-pruned-emaonly.safetensors"))
                .toList();
        if (CollectionUtils.isNotEmpty(retList)) {
            retList.get(0).setSelected(true);
        }
//        try {
//            modelService.saveBatch(retList.stream().map(e -> {
//                UserModelInfo info = new UserModelInfo();
//                BeanUtils.copyProperties(e, info);
//                info.setUserName(account);
//                info.setType(ModelType.SD_MODEL.getCode());
//                info.setCreated("sys");
//                info.setCreatedDate(new Date());
//                return info;
//            }).toList());
//        } catch (CommonException e) {
//            log.error("保存用户模型信息失败: ", e);
//        }
        return retList;
    }

    private List<File> getModelFile(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        List<File> fileList = Arrays.stream(files).filter(File::isFile).collect(Collectors.toList());
        Arrays.stream(files).filter(File::isDirectory).forEach(f -> fileList.addAll(Optional.ofNullable(getModelFile(f.getAbsolutePath())).orElse(new ArrayList<>())));
        return fileList;
    }
}
