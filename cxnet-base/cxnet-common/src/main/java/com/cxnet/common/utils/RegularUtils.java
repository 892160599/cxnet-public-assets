package com.cxnet.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Aowen
 * @date 2021/3/4
 */
public class RegularUtils {
    /**
     * 0:纯数字
     */
    public static final String NUMBER = "0";
    /**
     * 1：字母和数字需要同时存在
     */
    public static final String NUMBER_AND_LETTER = "1";
    /**
     * 3：大小写字母、数字、特殊符号必须同时存在
     */
    public static final String COMPLEXITY = "2";
    /**
     * 纯数字 6~20位
     */
    private static final String NUM = "^\\d{6,20}$";
    /**
     * 纯字母 6~20位
     */
    private static final String LATTER = "^[A-Za-z]{6,20}$";
    /**
     * 必须由字母和数字组成 6~20位
     */
    private static final String NUM_AND_LETTER = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
    /**
     * 必须由大小写字母、数字组成，可包含特殊符号 6~20位
     */
    private static final String STRONG_PWD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9].{6,20}$";

    /**
     * 必须由大小写字母、数字、特殊符号组成 8~20位
     */
    private static final String STRONGER_PWD = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,20}$";
    private static final String STRONGER_PWD_SIM = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$";

    /**
     * 是否为纯数字
     *
     * @param pwd 密码
     * @return true false
     */
    public static boolean isNum(String pwd) {
        return Pattern.compile(NUM).matcher(pwd).matches();
    }

    /**
     * 是否为纯数字或纯字母——弱密码
     *
     * @param pwd 密码
     * @return true false
     */
    public static boolean isNumOrLetter(String pwd) {
        boolean isNum = Pattern.compile(NUM).matcher(pwd).matches();
        boolean isLetter = Pattern.compile(LATTER).matcher(pwd).matches();
        return isNum || isLetter;
    }

    /**
     * 仅 字母和数字同时存在 6~20位
     *
     * @param pwd 密码
     * @return true false
     */
    public static boolean isNumAndLetter(String pwd) {
        return Pattern.compile(NUM_AND_LETTER).matcher(pwd).matches();
    }

    /**
     * 必须由大小写字母、数字组成，可包含特殊符号 6~20位
     *
     * @param pwd 密码
     * @return true false
     */
    public static boolean isNumAndLetterOrSymbol(String pwd) {
        return Pattern.compile(STRONG_PWD).matcher(pwd).matches();
    }

    /**
     * 大小写字母、数字、特殊符号必须同时存在 8~20位
     *
     * @param pwd 密码
     * @return true false
     */
    public static boolean isStrong(String pwd) {
        return Pattern.compile(STRONGER_PWD).matcher(pwd).matches();
    }

}
