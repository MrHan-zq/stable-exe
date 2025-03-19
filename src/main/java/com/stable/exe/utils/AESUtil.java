package com.stable.exe.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @Author hanjun
 * @Date 2024/04/16
 * @Description AES加密工具类
 * @Version 1.0
 */
@Slf4j
@Component
public class AESUtil {

    /**
     * 默认密钥的KEY
     */
    public static final String AES_KEY = "TRTCDESPHNUIKOHM";


    /**
     * 使用默认key加密字符串
     *
     * @param src
     * @return
     */
    public static String encrypt(String src) {
        return AESUtil.encrypt(src, AES_KEY);
    }


    /**
     * 使用默认key解密字符串
     *
     * @param src
     * @return
     */
    public static String decrypt(String src) {
        return AESUtil.decrypt(src, AES_KEY);
    }


    /**
     * 加密
     * 通过AES 加密成 byte[]
     * 然后将byte数组 转成 16进制的字符串
     *
     * @param src
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String src, String key) {
        if (StringUtils.isBlank(src) || StringUtils.isBlank(key)) {
            log.error("字符串或密钥不能为空");
            return null;
        }

        // 判断Key是否为16位
        if (key.length() != 16) {
            log.error("key长度不是16位");
            return null;
        }
        try {
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(src.getBytes(StandardCharsets.UTF_8));
            return encodeHexString(encrypted);
        } catch (Exception e) {
            log.error("加密失败,sourceData=" + src + "，error:", e);
            return null;
        }

    }


    /**
     * byte 数组转 16进制字符串
     *
     * @param byteArray byte数组
     * @return String
     */
    public static String encodeHexString(byte[] byteArray) {
        StringBuilder hexStringBuffer = new StringBuilder();
        for (byte b : byteArray) {
            hexStringBuffer.append(byteToHex(b));
        }
        return hexStringBuffer.toString();
    }

    /**
     * byte 转16进制字符串
     *
     * @param num byte
     * @return String
     */
    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }


    /**
     * 将16进制字符串转成二进制数组 然后解密
     *
     * @param src 16进制字符串
     * @param key 秘钥
     * @return
     */
    public static String decrypt(String src, String key) {
        try {
            if (StringUtils.isBlank(src) || StringUtils.isBlank(src)){
                log.error("加密字符串或密钥不能为空");
                return null;
            }
            // 判断Key是否为16位
            if (key.length() != 16) {
                log.error("Key长度不是16位");
                return null;
            }
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = parseHexStr2Byte(src);
            //new BASE64Decoder().decodeBuffer(src);//先用base64解密
            if(Objects.isNull(encrypted1)){
                log.error("字符串解密失败");
                return null;
            }
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("字符串解密失败,sourceData="+src+",  error:",e);
            return null;
        }
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    public static void main(String[] args) {
        String str = "1680770916096188416";
        String encrypt = AESUtil.encrypt(str);
        System.out.println("加密字符串：" + encrypt);
        String decrypt = AESUtil.decrypt(encrypt);
        System.out.println("解密密字符串：" + decrypt);
    }


}