package com.stable.exe;

import com.stable.exe.config.ContextHolder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFXStarter extends Application {

    private static ConfigurableApplicationContext springContext;

    @Override
    public void init() throws Exception {
        super.init();
        springContext = ContextHolder.getContext();
        if (springContext == null) {
            throw new IllegalStateException("Spring上下文未初始化！");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        WebView webView = new WebView();
        webView.getEngine().load("http://localhost:50000"); // 加载 Vue 页面
        primaryStage.setTitle("Embedded Browser");
        primaryStage.setScene(new Scene(webView, 1980, 1080));
        primaryStage.show();
        // JavaFX 初始化代码
        primaryStage.setOnCloseRequest(event -> {
            springContext.close(); // 关闭 Spring 上下文
            Platform.exit(); // 关闭 JavaFX 线程
        });
    }

}
