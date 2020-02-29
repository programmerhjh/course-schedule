package com.neusoft.course.schedule.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/27.
 */
public class StringUtils {

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰
     * @param str
     * @return
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
