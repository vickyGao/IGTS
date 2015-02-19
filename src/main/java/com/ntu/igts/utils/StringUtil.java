package com.ntu.igts.utils;

public class StringUtil {

    public static final String EMPTY = "";

    /**
     * Check whether the given String is empty.
     * 
     * @param str
     * @return true | false
     */
    public static boolean isEmpty(String str) {
        return org.springframework.util.StringUtils.isEmpty(str);
    }
}
