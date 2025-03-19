package com.stable.exe.api;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.stable.exe.constant.CommonConstant;
import com.stable.exe.emus.DmTrainStatusEnum;
import com.stable.exe.emus.ResponseCode;
import com.stable.exe.exception.CommonException;
import com.stable.exe.model.DmTrain;
import com.stable.exe.model.DreamBoothConfig;
import com.stable.exe.model.DreamBoothModel;
import com.stable.exe.model.WebUIApiResult;
import com.stable.exe.utils.CommonUtil;
import com.stable.exe.utils.HttpClientUtil;
import com.stable.exe.utils.HttpUtil;
import com.stable.exe.utils.JacksonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Getter
@Setter
@Component
public class DreamBoothApi {

    @Value("${sd-webui-api-host}")
    private String baseurl;

    private String apiKey;

    @PostConstruct
    public void init() {
        this.baseurl = baseurl + "/dreambooth";
    }

    public WebUIApiResult trainJob(String modelName, Boolean useTx2img) throws Exception {
        synchronized (DmTrain.getInstance()) {
//            if (DmTrainStatusEnum.DM_TRAIN_RUNNING.equals(DmTrain.getInstance().getStatus())) {
//                return new WebUIApiResult().addInfo("msg", "请等待有任务在执行中");
//            }
            DmTrain.getInstance().setStatus(DmTrainStatusEnum.DM_TRAIN_RUNNING);
            StringBuilder url = new StringBuilder(this.baseurl);
            useTx2img = Objects.isNull(useTx2img) || useTx2img;
            url.append("/start_training?").append("model_name=").append(modelName).append("&use_tx2img=").append(useTx2img);
            String data = HttpUtil.postOfString(null, url.toString(), null, 10000);
            return new WebUIApiResult().addInfo(CommonConstant.RESULT_MSG, data);
        }
    }

    /**
     * 取消执行中的任务
     *
     * @return
     * @throws Exception
     */
    public WebUIApiResult cancelJob() {
        synchronized (DmTrain.getInstance()) {
            if (DmTrainStatusEnum.DM_TRAIN_NO_RUNNING.equals(DmTrain.getInstance().getStatus())) {
                return new WebUIApiResult().addInfo("msg", "没有在执行的任务");
            }
            DmTrain.getInstance().setStatus(DmTrainStatusEnum.DM_TRAIN_NO_RUNNING);
            String url = this.connectApiKey(this.baseurl, "/cancel");
            String result;
            try {
                result = HttpClientUtil.getAndGetApiResult(url, null, null);
            } catch (Exception e) {
                log.error("取消DreamBooth任务失败: ", e);
                throw new CommonException(ResponseCode.RPC_ERROR, "取消DreamBooth任务失败");
            }
            Map<String, Object> map = JacksonUtil.jsonToObject(result, Map.class);
            return new WebUIApiResult().addAllInfo(map);
        }
    }

    /**
     * 创建模型
     *
     * @param dreamBoothModel
     * @return
     * @throws Exception
     */
    public WebUIApiResult createModel(DreamBoothModel dreamBoothModel) throws Exception {
        String result = HttpUtil.postOfString(null, this.getCreateModelUrl(dreamBoothModel), null, 300000);
        return new WebUIApiResult().addInfo(CommonConstant.RESULT_MSG, result);
    }

    /**
     * 获取模型配置信息
     *
     * @param modelName
     * @return
     * @throws Exception
     */
    public DreamBoothConfig getModelConfig(String modelName) throws Exception {
        String url = this.connectApiKey(this.baseurl, "/model_config?model_name=" + modelName);
        String result = HttpClientUtil.getAndGetApiResult(url, null, null);
        Map<String, Object> read = JacksonUtil.read(result, Map.class);
        Object obj = read.get("use_shared_src");
        if (Objects.isNull(obj)) {
            read.put("use_shared_src", Collections.singletonList(false));
        }else if (!(obj instanceof List)){
            read.put("use_shared_src", Collections.singletonList(obj));
        }
        return JacksonUtil.jsonToObject(JacksonUtil.toJsonString(read), DreamBoothConfig.class);
    }

    public List<String> getModelNames() throws Exception {
        String url = this.connectApiKey(this.baseurl, "/models");
        String result = HttpClientUtil.getAndGetApiResult(url, null, null);
        return JacksonUtil.jsonToObject(result, List.class, String.class);
    }

    public WebUIApiResult modifiedModelConfig(DreamBoothConfig config) throws Exception {
        String url = this.connectApiKey(this.baseurl, "/model_config");
        Map<String, Object> read = JacksonUtil.read(JacksonUtil.toJsonString(config), Map.class);
        List<Boolean> useSharedSrc = (List) read.get("use_shared_src");
        read.put("use_shared_src", Optional.ofNullable(useSharedSrc).filter(CollectionUtils::isNotEmpty).map(e -> e.get(0)).orElse(false));
        JSONObject jsonObject = HttpUtil.httpPost(null, url, JacksonUtil.toJsonString(read));
        return new WebUIApiResult().addAllInfo(JacksonUtil.read(JacksonUtil.toJsonString(jsonObject), Map.class));
    }

    public Map<String, Object> getJobStatus() throws Exception {
        String url = CommonUtil.connectUrlParam(this.connectApiKey(this.baseurl, "/status"), null);
        String result = HttpClientUtil.getAndGetApiResult(url, null, null);
        return JacksonUtil.read(result, Map.class);
    }

    private String connectApiKey(String baseurl, String apiUrl) {
        if (StringUtils.isBlank(baseurl) || StringUtils.isBlank(apiUrl)) {
            return null;
        }
        return Optional.ofNullable(this.apiKey)
                .map(e -> {
                    if (apiUrl.contains("?")) {
                        return baseurl + apiUrl + "&api_key=" + e;
                    }
                    return baseurl + apiUrl + "?api_key=" + e;
                })
                .orElse(baseurl + apiUrl);
    }

    private String getCreateModelUrl(DreamBoothModel model) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder(this.baseurl);
        builder.append("/createModel?new_model_name=").append(model.getNew_model_name().trim());

        String fileName = model.getNew_model_src().trim();
        String path = CommonUtil.getWorkspaces() + "\\stable-diffusion-webui\\models\\Stable-diffusion";
        File file = new File(path);
        if (!file.exists() || StringUtils.isBlank(fileName)) {
            throw new CommonException(ResponseCode.RPC_ERROR, "模型不存在");
        }
        File[] files = file.listFiles();
        if (null == files || files.length == 0) {
            throw new CommonException(ResponseCode.RPC_ERROR, "模型不存在");
        }
        String fileDirectory = "";
        List<File> fileList = Arrays.stream(files).filter(File::isFile).filter(e -> e.getName().equals(fileName + ".safetensors")).toList();
        List<File> directoryList = Arrays.stream(files).filter(File::isDirectory).toList();
        for (File e : directoryList) {
            List<File> list = Arrays.stream(Optional.ofNullable(e.listFiles()).orElse(new File[0]))
                    .filter(File::isFile).filter(e1 -> e1.getName().equals(fileName + ".safetensors")).toList();
            if (!list.isEmpty()) {
                fileDirectory = list.get(0).getAbsolutePath() + fileName + ".safetensors";
            }
        }
        if (CollectionUtils.isEmpty(fileList) && StringUtils.isBlank(fileDirectory)) {
            throw new CommonException(ResponseCode.RPC_ERROR, "模型不存在");
        }
        fileDirectory = StringUtils.isBlank(fileDirectory) ? fileList.get(0).getAbsolutePath() : fileDirectory;
        String encode = URLEncoder.encode(fileDirectory, StandardCharsets.UTF_8);
        builder.append("&new_model_src=").append(encode);
        if (StringUtils.isNotBlank(model.getNew_model_shared_src())) {
            builder.append("&new_model_shared_src=").append(model.getNew_model_shared_src().trim());
        }
        builder.append("&create_from_hub=").append(model.getCreate_from_hub());
        if (StringUtils.isNotBlank(model.getNew_model_url())) {
            builder.append("&new_model_url=").append(model.getNew_model_url().trim());
        }
        builder.append("&model_type=").append(model.getModel_type().trim());
        builder.append("&train_unfrozen=").append(model.getTrain_unfrozen());
        if (StringUtils.isNotBlank(model.getNew_model_token())) {
            builder.append("&new_model_token=").append(model.getNew_model_token().trim());
        }
        builder.append("&new_model_extract_ema=").append(model.getNew_model_extract_ema());
        return builder.toString();
    }

//    public static void main(String[] args) {
//        String url="http://localhost:7860/dreambooth/createModel?new_model_name=21313123&new_model_src=v1-5-pruned-emaonly&create_from_hub=false&model_type=v1x&train_unfrozen=true&new_model_extract_ema=false";
//    }
}
