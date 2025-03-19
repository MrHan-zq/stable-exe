package com.stable.exe.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by wucong3 on 2019/4/12.
 * 数据掩码工具类，掩码时保持原文长度不变
 * 当数据不符合格式的时候，不知道如何打掩码，只好返回原文
 * 不进行纠错，如.com写成.c0m，不符合邮箱格式，不打掩码直接返回
 */
public class MaskUtil {

    /**
     * 公共开放掩码方法，按需打码
     *
     * @param orginal
     * @param beginBit
     * @param lastBit
     * @return
     */
    public static String getMaskString(String orginal, int beginBit, int lastBit) {
        if (beginBit < 0 || lastBit < 0 || StringUtils.isBlank(orginal) || (beginBit + lastBit) > orginal.length()) {
            return orginal;
        }
        String preString = orginal.substring(0, beginBit);
        String subString = orginal.substring(orginal.length() - lastBit);
        StringBuilder midBuilder = new StringBuilder();
        int maskBit = orginal.length() - beginBit - lastBit;
        for (int i = 0; i < maskBit; i++) {
            midBuilder.append('*');
        }

        String midString = midBuilder.toString();
        return preString + midString + subString;
    }

    /**
     * 姓名掩码
     * 三个字掩码，如：张晓明 如：张*明
     * 两个字掩码，如：张明 如：张*
     * 多个字掩码，如：张小明明 如：张**明
     *
     * @param name
     * @return
     */
    public static String maskName(String name) {
        if (StringUtils.isBlank(name)) {
            return name;
        }
        if (name.length() == 1) {
            return name;
        } else if (name.length() == 2) {
            return name.substring(0, 1) + "*";
        } else {
            return getMaskString(name, 1, 1);
        }
    }

    /**
     * 手机号掩码
     *
     * @param phone
     * @return
     */
    public static String maskPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return phone;
        }
        if (Pattern.matches("^1\\d{10}$", phone)) {
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        } else if (Pattern.matches("^01\\d{10}$", phone)) {
            return phone.replaceAll("(\\d{4})\\d{4}(\\d{4})", "$1****$2");
        } else {
            return phone;
        }
    }

    /**
     * 座机号掩码
     *
     * @param telephone
     * @return
     */
    public static String maskTelephone(String telephone) {
        if (StringUtils.isBlank(telephone)) {
            return telephone;
        }
        if (Pattern.matches("^0\\d{10}$", telephone)) {
            return telephone.replaceAll("(\\d{4})\\d{4}(\\d{3})", "$1****$2");
        } else if (Pattern.matches("^0\\d{11}$", telephone)) {
            return telephone.replaceAll("(\\d{4})\\d{4}(\\d{4})", "$1****$2");
        } else {
            return telephone;
        }
    }

    /**
     * 手机号或者座机号掩码
     *
     * @param phone
     * @return
     */
    public static String maskPhoneOrTelephone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return phone;
        }
        if (Pattern.matches("^1\\d{10}$", phone)) {
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        } else if (Pattern.matches("^01\\d{10}$", phone)) {
            return phone.replaceAll("(\\d{4})\\d{4}(\\d{4})", "$1****$2");
        } else if (Pattern.matches("^0\\d{10}$", phone)) {
            return phone.replaceAll("(\\d{4})\\d{4}(\\d{3})", "$1****$2");
        } else if (Pattern.matches("^0\\d{11}$", phone)) {
            return phone.replaceAll("(\\d{4})\\d{4}(\\d{4})", "$1****$2");
        } else {
            return phone;
        }
    }

    /**
     * 银行卡号掩码
     *
     * @param bankCardNo
     * @return
     */
    public static String maskBankCardNo(String bankCardNo) {
        if (StringUtils.isBlank(bankCardNo)) {
            return bankCardNo;
        }
        if (bankCardNo.length() < 10) {
            return bankCardNo;
        } else {
            return getMaskString(bankCardNo, 6, 4);
        }
    }

//    /**
//     * 身份证掩码
//     * @param certificateNo
//     * @return
//     */
//    private static String maskCertificateNo(String certificateNo){
//        return null;
//    }

    /**
     * 邮箱掩码
     *
     * @param email
     * @return
     */
    public static String maskEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return email;
        }
        if (checkEmail(email)) {
            int flagIndex = email.indexOf('@');
            String beginPart = email.substring(0, flagIndex);
            String beginPartMask = getMaskString(beginPart, 2, 0);
            return beginPartMask + email.substring(flagIndex);
        }
        return email;
    }

    public static boolean checkEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return email.matches("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$");
    }

    /**
     * 身份证掩码
     *
     * @param cardId
     * @return
     */
    public static String maskIdCard(String cardId) {
        return getMaskString(cardId, 2, 4);
    }

    private static final String ADD_MASK_CODE_1 = "*";
    private static final String ADD_MASK_CODE_4 = "****";
    private static final String[] ADD_MASK_SUB_KEY = new String[]{"县", "区", "市"};

    /***
     * 地址掩码
     */
    public static String maskAddress(String addr) {
        try {
            if (StringUtils.isBlank(addr)) {
                return addr;
            }

            for (String subKey : ADD_MASK_SUB_KEY) {
                int subIdx = addr.indexOf(subKey);
                if (subIdx != -1) {
                    return addr.substring(0, subIdx + 1) + ADD_MASK_CODE_4;
                }
            }
            return addr.replaceAll("\\d", ADD_MASK_CODE_1);
        } catch (Exception e) {
            return addr;
        }
    }
}