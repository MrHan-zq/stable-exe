package com.stable.exe.service;

import jakarta.servlet.http.HttpServletRequest;
import com.stable.exe.web.response.SDModelConfigResponse;

import java.util.List;

/**
 * Dreambooth Train status
 * @author hanjun
 * {@code @time} 2024-04-15
 */

public interface StableDiffusionService {

    List<SDModelConfigResponse> getSDModelConfigList(HttpServletRequest request) throws Exception;
}
