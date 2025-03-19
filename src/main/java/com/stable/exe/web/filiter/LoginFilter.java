package com.stable.exe.web.filiter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Order(1)
@Component
public class LoginFilter implements Filter {

    private static final String[] EXCLUDE_URL = {"/user/login","/user/register", "/img/download/",".png", ".js", ".css", ".jpg",".ico","/stacks"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String requestURI = httpServletRequest.getRequestURI();
        log.info("LoginFilter requestURI: {}", requestURI);
        filterChain.doFilter(servletRequest, servletResponse);
//
//        if (excludeUrl(requestURI) || StringUtils.equalsIgnoreCase("OPTIONS", httpServletRequest.getMethod())) {
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
//        String token = httpServletRequest.getHeader("token");
//        token = StringUtils.isBlank(token) ? httpServletRequest.getHeader("Token") : token;
//        if (StringUtils.isBlank(token) || null == CommonUtil.getCacheUserInfo(httpServletRequest)) {
//            log.warn("token为空，用户未登录");
//            httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
//            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
//            httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE");
//            httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
//            httpServletResponse.setHeader("Access-Control-Allow-Headers", "trm_access_token,X-Requested-With,Content-Type,token");
//            PrintWriter writer = httpServletResponse.getWriter();
//            writer.write("{\"responseCode\":" + ResponseCode.NO_AUTH.getCode() + ",\"responseDesc\":\"NOT LOGIN\"}");
//            writer.flush();
//            return;
//        }
//        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private boolean excludeUrl(String url) {
        for (String excludeUrl : EXCLUDE_URL) {
            if (url.contains(excludeUrl)) {
                return true;
            }
        }
        return false;
    }
}
