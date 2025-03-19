package com.stable.exe.config;

// ContextHolder.java
import lombok.Getter;
import org.springframework.context.ConfigurableApplicationContext;

public class ContextHolder {
    @Getter
    private static volatile ConfigurableApplicationContext context;

    // 双重校验锁保证线程安全
    public static void setContext(ConfigurableApplicationContext ctx) {
        if (context == null) {
            synchronized (ContextHolder.class) {
                if (context == null) {
                    context = ctx;
                }
            }
        }
    }

}
