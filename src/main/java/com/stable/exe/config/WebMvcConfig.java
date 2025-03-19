package com.stable.exe.config;

import com.stable.exe.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("WebMvcConfigurer: {}",String.format("file:%s/outs/", CommonUtil.getProjectPath()));
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/web-static/**")
                .addResourceLocations(String.format("file:%s/outs/", CommonUtil.getProjectPath()));
        //webjars
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/").resourceChain(true);
    }
}
