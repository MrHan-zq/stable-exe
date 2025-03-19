package com.stable.exe.web;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import com.stable.exe.api.WebUIApi;
import com.stable.exe.emus.ImageTypeEnum;
import com.stable.exe.emus.ResponseCode;
import com.stable.exe.entity.ImageInfo;
import com.stable.exe.exception.CommonException;
import com.stable.exe.model.Img2ImgModel;
import com.stable.exe.model.PILImage;
import com.stable.exe.model.Txt2ImgModel;
import com.stable.exe.model.WebUIApiResult;
import com.stable.exe.service.ImageService;
import com.stable.exe.utils.CommonUtil;
import com.stable.exe.utils.ImageUtil;
import com.stable.exe.web.request.ImageRequest;
import com.stable.exe.web.response.CommonResponse;
import com.stable.exe.web.response.PageResponse;
import com.stable.exe.web.response.UserInfoResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/img")
public class ImageController {

    private static ThreadPoolExecutor executors= new ThreadPoolExecutor(3, 10,
                                      2L,TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>(10));

    @Resource
    private ImageService imageService;

    @Resource
    private WebUIApi webUIApi;

    /**
     * 文生图，返回base64码
     *
     * @param positivePrompt         提示词
     * @param negativePrompt 否定提示词
     * @return base64
     */
    @PostMapping("/txt2img")
    public PageResponse<String> txt2Img(@RequestBody Map<String,Object> payload, HttpServletRequest request) {
        try {
//            String account = CommonUtil.getCacheUserInfo(request).getName();
            Txt2ImgModel txt2ImgModel = this.getImgModel(payload,0);
            WebUIApiResult result = webUIApi.txt2Img(txt2ImgModel);
            if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getImages())) {
                return new PageResponse<>(ResponseCode.NO_DATA);
            }
            List<String> imgList = result.getImages().stream()
                    .map(PILImage::getBase64Img)
                    .map(base64 -> imageService.saveTxt2Img("admin", base64)).toList();
            log.info("文生图 图片地址:{}", imgList);
            return new PageResponse<>(imgList,imgList.size());
        } catch (Exception e) {
            log.error("文生图失败: ", e);
            return new PageResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    @PostMapping("/img2img")
    public PageResponse<String> img2Img(@RequestBody Map<String,Object> payload, HttpServletRequest httpServletRequest) {
        try {
//            String account = CommonUtil.getCacheUserInfo(httpServletRequest).getName();
            String account = "admin";
            Img2ImgModel img2ImgModel = this.getImgModel(payload,0);
            WebUIApiResult result = webUIApi.img2Img(img2ImgModel);
            if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getImages())) {
                return new PageResponse<>(ResponseCode.NO_DATA);
            }
            List<String> imgList = result.getImages().stream()
                    .map(PILImage::getBase64Img)
                    .map(base64 -> imageService.saveTxt2Img(account, base64)).toList();
            log.info("图生图 图片地址:{}", imgList);
            return new PageResponse<>(imgList,imgList.size());
        } catch (Exception e) {
            log.error("图生图失败: ", e);
            return new PageResponse<>(ResponseCode.SYSTEM_ERROR);
        }
    }

    //0=txt2img 1=img2img
    private Img2ImgModel getImgModel(Map<String,Object> payload,int type) {
        Img2ImgModel img2ImgModel = new Img2ImgModel();
        if (MapUtils.isNotEmpty(payload)){
            String positivePrompt = MapUtils.getString(payload, "positivePrompt");
            positivePrompt=MapUtils.getString(payload, "style")+","+positivePrompt;
            img2ImgModel.prompt = positivePrompt;
            img2ImgModel.negative_prompt = MapUtils.getString(payload,"negativePrompt");
            img2ImgModel.width = MapUtils.getInteger(payload,"width");
            img2ImgModel.height = MapUtils.getInteger(payload,"height");
            img2ImgModel.sampler_index = MapUtils.getString(payload,"samplingMethod");
            img2ImgModel.sampler_name = MapUtils.getString(payload,"samplingMethod");
            img2ImgModel.steps = MapUtils.getInteger(payload,"iterationSteps");
            img2ImgModel.cfg_scale = MapUtils.getFloat(payload,"guidanceScale");
            img2ImgModel.batch_size = MapUtils.getInteger(payload,"quantity");
            img2ImgModel.seed = MapUtils.getLong(payload,"randomSeed");
            img2ImgModel.imagesAdd(MapUtils.getString(payload,"base64String"));
        }
        return img2ImgModel;
    }

    @GetMapping("/download/{type}")
    public void downloadImg(@PathVariable String type, String fileName,String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("开始下载图片 fileName:{}", fileName);
        if (!StringUtils.equalsIgnoreCase(type, ImageTypeEnum.TXT_TO_IMG.getType()) && !StringUtils.equalsIgnoreCase(type, ImageTypeEnum.IMG_TO_IMG.getType())) {
            log.error("下载图片失败路径不存在");
            return;
        }
        UserInfoResponse info = CommonUtil.getCacheUserInfo(request);
        info = Objects.isNull(info)?(UserInfoResponse) CommonUtil.TOKEN_CAHCE.get(token):info;
        String path = String.format("%s/outs/%s/%s/%s", CommonUtil.getProjectPath(), type, info.getName(), fileName);
        File file = new File(path);
        if (!file.exists()) {
            log.error("图片不存在，请检查文件名");
            return;
        }
        InputStream inputStream = new FileInputStream(file);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/force-downloa");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        response.addHeader("Content-Length", String.valueOf(file.length()));
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int leg;
        while ((leg = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, leg);
        }
        log.info("下载图片完成");
        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    @PostMapping("/remove/{type}")
    public CommonResponse<Boolean> removeImg(@PathVariable String type, @RequestBody ImageRequest request, HttpServletRequest httpServletRequest) {
        Boolean flag;
        String account = "admin";
        if (StringUtils.equalsIgnoreCase(type, ImageTypeEnum.TXT_TO_IMG.getType())) {
            flag = imageService.removeTxt2Img(account, request.getFileName());
        } else if (StringUtils.equalsIgnoreCase(type, ImageTypeEnum.IMG_TO_IMG.getType())) {
            flag = imageService.removeImg2Img(account, request.getFileName());
        } else {
            log.error("删除图片失败，路径不存在");
            return new CommonResponse<>(ResponseCode.PARAM_ERROR.getCode(), "路径不存在");
        }
        return new CommonResponse<>(flag);
    }

    @GetMapping("/get/list/{type}")
    public PageResponse<ImageInfo> getImgList(@PathVariable String type, HttpServletRequest request) {
        List<ImageInfo> list;
        String account = "admin";
        try {
            if (StringUtils.equalsIgnoreCase(type, ImageTypeEnum.TXT_TO_IMG.getType())) {
                list = imageService.getTxt2ImgList(account);
            } else if (StringUtils.equalsIgnoreCase(type, ImageTypeEnum.IMG_TO_IMG.getType())) {
                list = imageService.getImg2ImgList(account);
            }
            else if (StringUtils.equalsIgnoreCase(type, ImageTypeEnum.ALL_IMG.getType())){
                CompletableFuture<List<ImageInfo>> txtFuture = CompletableFuture.supplyAsync(() -> imageService.getTxt2ImgList(account), executors);
                CompletableFuture<List<ImageInfo>> imgFuture = CompletableFuture.supplyAsync(() -> imageService.getImg2ImgList(account), executors);
                CompletableFuture.allOf(txtFuture, imgFuture).get(5000, TimeUnit.SECONDS);
                list= Stream.concat(txtFuture.get().stream(), imgFuture.get().stream()).collect(Collectors.toList());
            }
            else {
                return new PageResponse<>(ResponseCode.PARAM_ERROR.getCode(), "路径不存在");
            }
            return new PageResponse<>(list, list.size());
        } catch (CommonException e) {
            log.error("查询{}图片列表失败: {}", type, e.getMessage());
            return new PageResponse<>(e.getCode().getCode(), e.getMessage(),new ArrayList<>(), 0);
        } catch (ExecutionException e) {
            log.error("查询{}图片列表失败: ", type);
            return new PageResponse<>(ResponseCode.SYSTEM_ERROR.getCode(), e.getMessage(),new ArrayList<>(), 0);
        } catch (InterruptedException e) {
            log.error("查询{}图片列表失败: ", type, e);
            return new PageResponse<>(ResponseCode.SYSTEM_ERROR.getCode(), e.getMessage(),new ArrayList<>(), 0);
        } catch (TimeoutException e) {
            log.error("查询{}图片列表失败: ", type, e);
            return new PageResponse<>(ResponseCode.SYSTEM_ERROR.getCode(), e.getMessage(),new ArrayList<>(), 0);
        }

    }

    @PostMapping("/upload")
    public CommonResponse<String> uploadModel(MultipartFile file) {
        if (null == file) {
            throw new CommonException(ResponseCode.PARAM_ERROR, "上传文件不能为空");
        }
        try {
            String base64 = ImageUtil.img2Base64(file);
            if (StringUtils.isBlank(base64)) {
                return new CommonResponse<>(ResponseCode.SYSTEM_ERROR.getCode(),"图片转Base64失败");
            }
            return new CommonResponse<>(ResponseCode.SUCCESS, base64);
        } catch (Exception e) {
            log.error("上传图片进行图生图失败: ", e);
            return new CommonResponse<>(ResponseCode.SYSTEM_ERROR.getCode());
        }
    }

}
