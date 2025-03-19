package com.stable.exe.web;

import com.stable.exe.api.DreamBoothApi;
import com.stable.exe.api.WebUIApi;
import com.stable.exe.constant.CommonConstant;
import com.stable.exe.emus.DmTrainStatusEnum;
import com.stable.exe.emus.ResponseCode;
import com.stable.exe.exception.CommonException;
import com.stable.exe.model.DmTrain;
import com.stable.exe.model.WebUIApiResult;
import com.stable.exe.service.StableDiffusionService;
import com.stable.exe.utils.CommonUtil;
import com.stable.exe.utils.ImageUtil;
import com.stable.exe.web.response.CommonResponse;
import com.stable.exe.web.response.PageResponse;
import com.stable.exe.web.response.SDModelConfigResponse;
import com.stable.exe.web.response.UserInfoResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;

@Slf4j
@RestController
public class StableDiffusionController {

    @Resource
    private DreamBoothApi dreamBoothApi;

    @Resource
    private WebUIApi webUIApi;

    @Resource
    private StableDiffusionService stableDiffusionService;

//    @Resource
//    private ModelService modelService;

    @GetMapping("/sd/model/configs")
    public PageResponse<SDModelConfigResponse> getSDModelConfig(HttpServletRequest request) {
        try {
            List<SDModelConfigResponse> result = stableDiffusionService.getSDModelConfigList(request);
            if (CollectionUtils.isEmpty(result)) {
                return new PageResponse<>(ResponseCode.SYSTEM_ERROR.getCode(), "ModelConfig数据转换失败");
            }
            return new PageResponse<>(result, result.size());
        } catch (CommonException e) {
            log.error("find StableDiffusion model info error: ", e);
            return new PageResponse<>(e.getCode());
        } catch (Exception e) {
            log.error("find StableDiffusion model info error: ", e);
            return new PageResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

//    @GetMapping("/db/model/info")
//    public CommonResponse<DreamBoothConfigEntity> getBoothModelConfig(String modelName, HttpServletRequest request) {
//        if (StringUtils.isBlank(modelName)) {
//            return new CommonResponse<>(ResponseCode.NO_DATA);
//        }
//        try {
//            UserInfoResponse info = CommonUtil.getCacheUserInfo(request);
//            if (Objects.isNull(info)) {
//                return new CommonResponse<>(ResponseCode.NO_AUTH);
//            }
//            boolean exists = modelService.exists(new QueryWrapper<UserModelInfo>().eq("user_name", info.getName())
//                    .eq("model_name", modelName).eq("type", ModelType.DB_MODEL.getCode()));
//            if (exists) {
//                DreamBoothConfig modelConfig = dreamBoothApi.getModelConfig(modelName);
//                if (Objects.nonNull(modelConfig)) {
//                    DreamBoothConfigEntity dreamBoothConfigEntity = CommonUtil.obj2Obj(modelConfig, new DreamBoothConfigEntity());
//                    List<Concepts> conceptsList = modelConfig.getConcepts_list();
//                    if (CollectionUtils.isNotEmpty(conceptsList)) {
//                        ConceptsEntity conceptsEntity = CommonUtil.obj2Obj(conceptsList.get(0), new ConceptsEntity());
//                        if (dreamBoothConfigEntity != null) {
//                            dreamBoothConfigEntity.setConceptsList(Collections.singletonList(conceptsEntity));
//                        }
//                    }
//                    return new CommonResponse<>(dreamBoothConfigEntity);
//                }
//            }
//            return new CommonResponse<>(ResponseCode.NO_DATA);
//        } catch (Exception e) {
//            log.error("find StableDiffusion model info error: ", e);
//            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
//        }
//    }
//
//    @GetMapping("/db/model/name/list")
//    public PageResponse<String> getModelNames(HttpServletRequest request) {
//        try {
//            UserInfoResponse info = CommonUtil.getCacheUserInfo(request);
//            if (Objects.isNull(info)) {
//                return new PageResponse<>(ResponseCode.NO_AUTH);
//            }
//            List<UserModelInfo> modelInfos = modelService.list(new QueryWrapper<UserModelInfo>().eq("user_name", info.getName())
//                    .eq("type", ModelType.DB_MODEL.getCode()));
//            List<String> list = Optional.ofNullable(modelInfos).orElse(new ArrayList<>()).stream().map(UserModelInfo::getModelName).toList();
//            return new PageResponse<>(list, list.size());
//        } catch (Exception e) {
//            log.error("find StableDiffusion model name error: ", e);
//            return new PageResponse<>(ResponseCode.SYSTEM_ERROR);
//        }
//    }

//    @PostMapping("/db/modified/model/info")
//    public CommonResponse<Boolean> modifiedBoothModelConfig(@RequestBody DreamBoothConfigEntity request, HttpServletRequest httpServletRequest) {
//        try {
//            UserInfoResponse info = CommonUtil.getCacheUserInfo(httpServletRequest);
//            if (Objects.isNull(info)) {
//                return new CommonResponse<>(ResponseCode.NO_AUTH);
//            }
//            boolean exists = modelService.exists(new QueryWrapper<UserModelInfo>().eq("user_name", info.getName())
//                    .eq("model_name", request.getModelName()).eq("type", ModelType.DB_MODEL.getCode()));
//            if (exists) {
//                DreamBoothConfig dreamBoothConfig = CommonUtil.obj2Obj(request, new DreamBoothConfig());
//                WebUIApiResult result = dreamBoothApi.modifiedModelConfig(dreamBoothConfig);
//                return new CommonResponse<Boolean>(ResponseCode.SUCCESS)
//                        .desc(MapUtils.getString(result.getInfo(), CommonConstant.RESULT_MSG, ResponseCode.SUCCESS.getDesc()));
//            }
//            return new CommonResponse<>(ResponseCode.SUCCESS);
//        } catch (Exception e) {
//            log.error("update StableDiffusion model info error: ", e);
//            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
//        }
//    }


//    @PostMapping("/db/model/save")
//    public CommonResponse<Boolean> saveDbModel(@RequestBody DreamBoothModelRequest request, HttpServletRequest httpServletRequest) {
//
//        DreamBoothModel boothModel = new DreamBoothModel();
//        CommonUtil.obj2Obj(request, boothModel);
//        try {
//            UserInfoResponse info = CommonUtil.getCacheUserInfo(httpServletRequest);
//            if (Objects.isNull(info)) {
//                return new CommonResponse<>(ResponseCode.NO_AUTH);
//            }
//            WebUIApiResult result = dreamBoothApi.createModel(boothModel);
//            saveUserModelInfo(info.getName(), request.getNewModelName());
//            return new CommonResponse<Boolean>(ResponseCode.SUCCESS)
//                    .desc(MapUtils.getString(result.getInfo(), CommonConstant.RESULT_MSG, ResponseCode.SUCCESS.getDesc()));
//
//        } catch (Exception e) {
//            log.error("create DreamBooth model error: ", e);
//            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
//        }
//    }

    @GetMapping("/db/cancel/job")
    public CommonResponse<Boolean> cancelDBJob() {
        try {
            WebUIApiResult result = dreamBoothApi.cancelJob();
            return new CommonResponse<Boolean>(ResponseCode.SUCCESS)
                    .desc(MapUtils.getString(result.getInfo(), CommonConstant.RESULT_MSG, ResponseCode.SUCCESS.getDesc()));
        } catch (Exception e) {
            log.error("DreamBooth cancelJob error: ", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @GetMapping("/db/train/job")
    public CommonResponse<Boolean> trainDBJob(@NotEmpty String modelName, Boolean useTx2img, HttpServletRequest httpServletRequest) {
        try {
            UserInfoResponse info = CommonUtil.getCacheUserInfo(httpServletRequest);
            if (Objects.isNull(info)) {
                return new CommonResponse<>(ResponseCode.NO_AUTH);
            }
            WebUIApiResult result = dreamBoothApi.trainJob(modelName, useTx2img);
            return new CommonResponse<Boolean>(null == result ? ResponseCode.RPC_ERROR : ResponseCode.SUCCESS)
                    .desc(MapUtils.getString(result.getInfo(), CommonConstant.RESULT_MSG, ResponseCode.SUCCESS.getDesc()));
        } catch (Exception e) {
            log.error("DreamBooth trainJob error: ", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @GetMapping("/set/sd/model")
    public CommonResponse<Boolean> changeModel(@NotEmpty String modelName) {
        try {
            String result = webUIApi.changeModel(modelName);
            return new CommonResponse<Boolean>(ResponseCode.SUCCESS).desc(result);
        } catch (Exception e) {
            log.error("change model error: ", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @GetMapping("/get/job/complete/status")
    public CommonResponse<Boolean> jobIsComplete() {
        try {
            Map<String, Object> map = dreamBoothApi.getJobStatus();
            if (MapUtils.isEmpty(map)) {
                return new CommonResponse<>(ResponseCode.RPC_ERROR);
            }
            Map currentState = MapUtils.getMap(map, "current_state");
            Boolean active = MapUtils.getBoolean(currentState, "active", false);
            if (!active){
                DmTrain.getInstance().setStatus(DmTrainStatusEnum.DM_TRAIN_NO_RUNNING);
            }
            return new CommonResponse<>(active);
        } catch (Exception e) {
            log.error("查询任务执行状态失败: ", e);
        }
        return new CommonResponse<>(true);
    }

    @GetMapping("/get/job/complete/img")
    public CommonResponse<String> jobIsComplete(@NotEmpty String modelName) {
        try {
            String path = CommonUtil.getWorkspaces() + "\\stable-diffusion-webui\\models\\dreambooth\\" + modelName;
            File file = new File(path);
            if (file.exists()) {
                File[] listFiles = file.listFiles();
                List<File> samples = Arrays.stream(Optional.ofNullable(listFiles).orElse(new File[0])).filter(e -> e.getName().equals("samples")).toList();
                if (CollectionUtils.isNotEmpty(samples)) {
                    File[] files = samples.get(0).listFiles();
                    List<File> list = Arrays.stream(Optional.ofNullable(files).orElse(new File[0])).filter(f -> f.isFile() && f.getName().endsWith(".png")).toList();
                    if (CollectionUtils.isNotEmpty(list)) {
                        String base64 = ImageUtil.b64Img(list.get(list.size() - 1).getAbsolutePath());
                        return new CommonResponse<>(base64);
                    }
                }
            }
            return new CommonResponse<>(ResponseCode.PARAM_ERROR);
        } catch (Exception e) {
            log.error("查询任务执行状态失败: ", e);
            return new CommonResponse<>(ResponseCode.RPC_ERROR);
        }
    }

//    @PostMapping("/upload/model")
//    public void uploadModel(HttpServletRequest request, MultipartFile... files) throws IOException {
//        if (null == files || files.length == 0) {
//            throw new CommonException(ResponseCode.PARAM_ERROR, "上传文件不能为空");
//        }
//        String account = CommonUtil.getCacheUserInfo(request).getName();
//        Map<String, String> map = new HashMap<>();
//        for (MultipartFile file : files) {
//            String outPath = String.format("%s/stable-diffusion-webui/models/Stable-diffusion/%s", CommonUtil.getProjectPath(), file.getName());
//            File uploadFile = new File(outPath);
//            file.transferTo(uploadFile);
//            map.put(file.getName(), outPath);
//        }
//        try {
//            String models = webUIApi.getSdModels();
//            List<Map<String, Object>> list = JacksonUtil.jsonToObject(models, List.class, Map.class);
//            Optional.ofNullable(list).orElse(new ArrayList<>()).forEach(e -> {
//                for (Map.Entry<String, Object> entry : e.entrySet()) {
//                    String key = CommonUtil.underline2Hump(entry.getKey());
//                    if (StringUtils.isBlank(key)) {
//                        return;
//                    }
//                    entry.setValue(key);
//                }
//            });
//            List<SDModelConfigResponse> sdModelList = JacksonUtil.jsonToObject(JacksonUtil.toJsonString(list), List.class, SDModelConfigResponse.class);
//            List<UserModelInfo> modelInfos = Optional.ofNullable(sdModelList).orElse(Lists.newArrayList())
//                    .stream()
//                    .filter(e -> map.containsKey(e.getTitle().substring(0, e.getTitle().indexOf("[")).trim()))
//                    .map(e -> {
//                        UserModelInfo modelInfo = new UserModelInfo();
//                        BeanUtils.copyProperties(e, modelInfo);
//                        modelInfo.setCreated(account);
//                        modelInfo.setCreatedDate(new Date());
//                        modelInfo.setHashValue(e.getHash());
//                        return modelInfo;
//                    }).toList();
//            modelService.saveBatch(modelInfos);
//        } catch (Exception e) {
//            log.error("save user model mapping error: ", e);
//        }
//    }
//
//    private void saveUserModelInfo(String userName, String modelName) {
//        boolean exists = modelService.exists(new QueryWrapper<UserModelInfo>().eq("user_name", userName).eq("model_name", modelName)
//                .eq("type", ModelType.DB_MODEL.getCode()));
//        if (!exists) {
//            UserModelInfo entity = new UserModelInfo();
//            entity.setType(ModelType.DB_MODEL.getCode());
//            entity.setUserName(userName);
//            entity.setModelName(modelName);
//            entity.setCreated(userName);
//            entity.setCreatedDate(new Date());
//            modelService.save(entity);
//        }
//
//    }

}
