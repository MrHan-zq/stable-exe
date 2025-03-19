package com.stable.exe.service;

import com.stable.exe.entity.ImageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {

    String saveTxt2Img(String account,String base64String);
    String saveImg2Img(String account,String base64String);

    Boolean removeTxt2Img(String account,String fileName);
    Boolean removeImg2Img(String account,String fileName);

    List<ImageInfo> getImg2ImgList(String account);

    List<ImageInfo> getTxt2ImgList(String account);

}
