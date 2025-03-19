package com.stable.exe;

import com.stable.exe.config.ContextHolder;
import jakarta.annotation.PreDestroy;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@Slf4j
@SpringBootApplication
public class StableExeApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(StableExeApplication.class, args);
        // 将上下文存入持有类
        ContextHolder.setContext(context);
//        Application.launch(JavaFXStarter.class);
    }


    @PreDestroy
    public void onShutdown() {
        System.out.println("Spring Boot 正在关闭...");
    }
}
