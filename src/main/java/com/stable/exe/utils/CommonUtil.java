package com.stable.exe.utils;

import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import com.stable.exe.web.response.UserInfoResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class CommonUtil {

    public static final ConcurrentHashMap<String, Object> TOKEN_CAHCE = new ConcurrentHashMap<>();

    public static String getProjectPath() {
        return System.getProperty("user.dir");
    }

    public static String getWorkspaces() {
        String path = System.getProperty("user.dir");
        File file = new File(path);
        return file.getParent();
    }

    public static Boolean isChinese(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        Pattern p = Pattern.compile("[一-龥]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static Boolean isSpecialCharacter(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        // 这里定义了一些常见的特殊字符，你可以根据需要添加或删除
        String specialChars = "!#$%^&*()_+<>:\"-}[{]'?/\\《》？、——-—|,。，";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (specialChars.indexOf(c) != -1) {
                return true; // 如果找到特殊字符，则返回true
            }
        }
        return false;// 如果没有找到特殊字符，则返回false
    }

    /**
     * 校验字符串是否包含中文和特殊字符
     *
     * @param str 需要检查的字符串
     * @return
     */
    public static Boolean isChineseOrSpecialCharacter(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return Boolean.TRUE.equals(isChinese(str)) || Boolean.TRUE.equals(isSpecialCharacter(str));
    }

    /**
     * 下驼峰命名转划线
     *
     * @param param
     * @return
     */
    public static String hump2Underline(String param) {
        if (StringUtils.isBlank(param)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        int length = param.length();
        for (int i = 0; i < length; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c) && i != 0) {
                builder.append('_');
                c = Character.toLowerCase(c);
            }
            builder.append(c);
        }
        return builder.toString();
    }

    /**
     * 下划线转驼峰命名
     *
     * @param param
     * @return
     */
    public static String underline2Hump(String param) {
        if (StringUtils.isBlank(param)) {
            return null;
        }
        String[] split = param.split("_", -1);
        if (split.length == 1) {
            return param;
        }
        StringBuilder builder = new StringBuilder(split[0]);
        for (int i = 0; i < split.length; i++) {
            if (i > 0) {
                String temp = split[i];
                builder.append(temp.substring(0, 1).toUpperCase(Locale.ROOT)).append(temp.substring(1));
            }
        }
        return builder.toString();
    }

    public static <T> T obj2Obj(Object source, T target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            return null;
        }
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        if (sourceClass.equals(targetClass)) {
            return (T) source;
        }
        Field[] sourceFields = sourceClass.getDeclaredFields();
        Field[] targetFields = targetClass.getDeclaredFields();
        Map<String, Field> entityMap = Arrays.stream(targetFields).collect(Collectors.toMap(e -> CommonUtil.underline2Hump(e.getName()), Function.identity(), (k1, k2) -> k2));
        for (Field f : sourceFields) {
            String name = CommonUtil.underline2Hump(f.getName());
            if (entityMap.containsKey(name)) {
                Field field = entityMap.get(name);
                field.setAccessible(true);
                Object sourceVal;
                try {
                    f.setAccessible(true);
                    sourceVal = f.get(source);
                    if (Objects.isNull(sourceVal)) {
                        continue;
                    }
                } catch (IllegalAccessException e) {
                    log.error("Field: {} 获取值失败: ", f.getName(), e);
                    continue;
                }
                if (StringUtils.equalsIgnoreCase(f.getType().getSimpleName(), "List")) {
                    List<?> objList = (List<?>) sourceVal;
                    List<?> objectList = List.of();
                    try {
                        if (CollectionUtils.isNotEmpty(objList)){
                            Class<?> aClass = Class.forName(((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName());
                            Object obj;
                            if (StringUtils.equals(aClass.getSimpleName(), "Boolean")){
                                obj = false;
                            }else {
                                obj = aClass.getDeclaredConstructor().newInstance();
                            }
                            objectList = objList.stream().map(e -> obj2Obj(e, obj)).toList();
                        }
                    } catch (Exception e) {
                        log.warn("反射创建 " + targetClass + " 失败: ", e);
//                        objectList = objList.stream().map(en -> obj2Obj(en, new ConceptsEntity())).toList();
                    }
                    try {
                        field.set(target, objectList);
                        continue;
                    } catch (IllegalAccessException e) {
                        log.error("list Field: {} 赋值失败: ", field.getName(), e);
                    }
                }
                try {
                    field.set(target, sourceVal);
                } catch (IllegalAccessException e) {
                    log.error("Field: {} 赋值失败: ", field.getName(), e);
                }
            }
        }
        return target;
    }

    /**
     * 将对象不为空的属性值拼接在url
     *
     * @param url
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String connectUrlParam(String url, T t) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        if (Objects.isNull(t)) {
            return url;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        StringBuilder builder = new StringBuilder(url);
        int index = 0;
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                Object data = f.get(t);
                if (Objects.nonNull(data)) {
                    if (StringUtils.equalsIgnoreCase(f.getType().getSimpleName(), "List")) {
                        data = JacksonUtil.toJsonString(f.get(t));
                    }
                    if (index == 0) {
                        builder.append("?").append(f.getName()).append("=").append(data);
                        index++;
                        continue;
                    }
                    builder.append("&").append(f.getName()).append("=").append(data);
                    index++;
                }
            } catch (IllegalAccessException e) {
                log.error("connectUrlParam field: {} error: ", f.getName(), e);
                if (index > 0) index--;
            }
        }
        return builder.toString();
    }

    public static Map<String, Object> mapHump2Underline(Map<String, Object> old) {
        if (MapUtils.isEmpty(old)) {
            return old;
        }
        Map<String, Object> newMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : old.entrySet()) {
            String newKey = CommonUtil.hump2Underline(entry.getKey());
            Object entryValue = entry.getValue();
            if (Objects.isNull(entryValue)) {
                newMap.put(newKey, null);
                continue;
            }
            if (entryValue instanceof Collections) {
                List value = (List) entryValue;
                List<Map<String, Object>> newList = Lists.newArrayList();
                value.forEach(item -> {
                    if (item instanceof Map) {
                        Map<String, Object> map = (Map<String, Object>) item;
                        newList.add(mapHump2Underline(map));
                    }
                });
                entryValue = newList;
            }
            newMap.put(newKey, entryValue);
        }
        return newMap;
    }

    public static UserInfoResponse getCacheUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            return null;
        }
        UserInfoResponse userInfo = (UserInfoResponse) TOKEN_CAHCE.get(token);
        if (userInfo != null) {
            userInfo.setActivityTime(new Date());
            TOKEN_CAHCE.put(token, userInfo);
        }
        return userInfo;
    }

    public static void setCacheUserInfo(String token, UserInfoResponse userInfo) {
        if (StringUtils.isNoneBlank(token) && Objects.nonNull(userInfo)) {
            TOKEN_CAHCE.put(token, userInfo);
        }
    }

    public static String getSHA256(String input) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha256.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            log.error("getSHA256 error: ", e);
            return null;
        }
    }

    public static String getFileSHA256(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            try (InputStream is = new FileInputStream(filePath)) {
            try (InputStream is = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    digest.update(buffer, 0, read);
                }
            }

            // 获取哈希值
            byte[] hashBytes = digest.digest();

            // 将哈希值转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            String hash = sb.toString();
            System.out.println("SHA-256: " + hash);
            return hash;
        } catch (Exception e) {
            log.error("getFileSHA256 error: ", e);
            return null;
        }
    }

}
