package com.stable.exe.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class WebUIApiResult {

    private List<PILImage> images = new ArrayList<>();
    public Map<String, Object> parameters = new HashMap<>();
    public Map<String, Object> info = new HashMap<>();


    public PILImage getImage() {
        if (images.size() > 0) {
            return this.images.get(0);
        }
        return null;
    }

    public void addImage(String base64Img) {
        images.add(new PILImage(base64Img));
    }

    public WebUIApiResult addInfo(String key, Object value) {
        this.info.put(key, value);
        return this;
    }

    public WebUIApiResult addAllInfo(Map<String, Object> info) {
        this.info = info;
        return this;
    }

}
