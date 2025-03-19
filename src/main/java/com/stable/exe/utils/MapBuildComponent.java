package com.stable.exe.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class MapBuildComponent {

    private Map<String,Object> data=new HashMap<>();

    public static MapBuildComponent build(){
        return new MapBuildComponent();
    }

    public MapBuildComponent put(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    public Map<String,Object> get(){
        return this.data;
    }
}
