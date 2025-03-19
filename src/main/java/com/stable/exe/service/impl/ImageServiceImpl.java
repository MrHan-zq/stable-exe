package com.stable.exe.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.stable.exe.emus.ResponseCode;
import com.stable.exe.entity.ImageInfo;
import com.stable.exe.exception.CommonException;
import com.stable.exe.service.ImageService;
import com.stable.exe.utils.CommonUtil;
import com.stable.exe.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private static final String PROJECT_PATH = CommonUtil.getProjectPath();

    @Value("${sd-webui-img-host:http://localhost:8080}")
    private String host;

    @Override
    public String saveTxt2Img(String account, String base64String) {
        try {
            return this.saveImg(account, base64String, "txt2img");
        } catch (Exception e) {
            log.error("文生图，图片保存失败: ", e);
            throw new CommonException(ResponseCode.SYSTEM_ERROR, "文生图，图片保存失败");
        }
    }

    @Override
    public String saveImg2Img(String account, String base64String) {
        try {
            return this.saveImg(account, base64String, "img2img");
        } catch (Exception e) {
            log.error("图生图，保存失败: ", e);
            throw new CommonException(ResponseCode.SYSTEM_ERROR, "图生图，图片保存失败");
        }
    }

    @Override
    public Boolean removeTxt2Img(String account, String fileName) {
        try {
            this.delImg(account, fileName, "txt2img");
        } catch (Exception e) {
            log.error("文生图，删除失败: ", e);
            return false;
        }
        return true;
    }

    @Override
    public Boolean removeImg2Img(String account, String fileName) {
        try {
            this.delImg(account, fileName, "img2img");
        } catch (Exception e) {
            log.error("图生图，删除失败: ", e);
            return false;
        }
        return true;
    }

    @Override
    public List<ImageInfo> getImg2ImgList(String account) {
        try {
            return this.getImgList(account, "img2img");
        } catch (Exception e) {
            log.error("图生图，查询失败: ", e);
            throw new CommonException(ResponseCode.BUSINESS_ERROR, "查询图生图生成的图片失败");
        }
    }

    @Override
    public List<ImageInfo> getTxt2ImgList(String account) {
        try {
            return this.getImgList(account, "txt2img");
        } catch (CommonException ex) {
            throw new CommonException(ex.getCode(), ex.getMessage());
        } catch (Exception e) {
            log.error("图生图，查询失败: ", e);
            throw new CommonException(ResponseCode.BUSINESS_ERROR, "查询文生图生成的图片失败");
        }
    }

    public String getImgUrl(String fileName) {
        String hostUrl = host.endsWith("/") ? host.substring(0, host.length() - 1) : host;
        return String.format("%s/web-static/%s", hostUrl, fileName);
    }

    private List<ImageInfo> getImgList(String account, String type) {
        String hostUrl = host.endsWith("/") ? host.substring(0, host.length() - 1) : host;
        String baseUrl = String.format("%s/web-static/%s/%s", hostUrl, type, account);
        String outPath = String.format("%s/outs/%s/%s", PROJECT_PATH, type, account);
        File file = new File(outPath);
        if (!file.exists()) {
            throw new CommonException(ResponseCode.PARAM_ERROR, "图片路径不存在");
        }
        return Arrays.stream(Objects.requireNonNull(file.listFiles()))
                .map(e -> {
                    ImageInfo info = new ImageInfo();
                    info.setImgUrl(String.format("%s/%s", baseUrl, e.getName()));
                    info.setFileName(e.getName());
                    return info;
                }).toList();
    }

    private String saveImg(String account, String base64String, String type) {
        long time = new Date().getTime();
        String outPath = String.format("%s/outs/%s/%s/%s.png", PROJECT_PATH, type, account, time);
        File file = new File(outPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        ImageUtil.base64ToImg(base64String, outPath);
        String url = String.format("%s/%s/%s.png", type, account, time);
        log.info("saveImg url:{}", url);
        return this.getImgUrl(url);
    }

    private void delImg(String account, String fileName, String type) {
        String outPath = String.format("%s/outs/%s/%s", PROJECT_PATH, type, account);
        File file = new File(outPath);
        if (!file.exists()) {
            throw new CommonException(ResponseCode.PARAM_ERROR, "图片路径不存在");
        }
        List<File> files = Arrays.stream(Objects.requireNonNull(file.listFiles()))
                .filter(e -> StringUtils.equals(fileName, e.getName()))
                .toList();
        files.forEach(File::delete);
    }


//    public static void main(String[] args) {
//        String path = String.format("%s/imgs/1234", PROJECT_PATH);
//        File file = new File(path);
//        if (!file.exists()) {
//            throw new CommonException(ResponseCode.PARAM_ERROR, "图片路径不存在");
//        }
//        String base64 = ImageUtil.img2Base64(file.listFiles()[0].getAbsolutePath());
//        ImageUtil.base64ToImg(base64, PROJECT_PATH+"/imgs/1/1111.jpg");
////        List<File> files = Arrays.stream(Objects.requireNonNull(file.listFiles()))
////                .filter(e -> StringUtils.equals("a.jpeg", e.getName()))
////                .toList();
////        files.forEach(File::delete);
//    }

}
