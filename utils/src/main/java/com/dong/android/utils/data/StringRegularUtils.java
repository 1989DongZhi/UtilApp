package com.dong.android.utils.data;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/2.
 * 描述：字符串正则匹配工具类
 */

public class StringRegularUtils {

    public static final String TAG = StringRegularUtils.class.getSimpleName();

    /**
     * 正则匹配网址
     */
    public static final String REGULAR_URL =
            "((https|http|ftp|rtsp|mms)?://)"//
                    + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                    + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                    + "|" // 允许IP和DOMAIN（域名）
                    + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                    + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                    + "[a-z]{2,6})" // first level domain- .com or .museum
                    + "(:[0-9]{1,4})?" // 端口- :80
                    + "((/?)|" // a slash isn't required if there is no file name
                    + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)";

    /**
     * 正则匹配手机号码
     */
    public static final String REGULAR_PHONE_NUMBER =
            "((1[3,5,8][0-9])|(14[5,7])|(17[0,1,6,7,8]))\\d{8}";

    /**
     * 正则匹配邮箱
     */
    public static final String REGULAR_EMAIL =
            "([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]" +
                    "@" +
                    "([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)" +
                    "+[a-zA-Z]{2,}";

    /**
     * 获取一段文本中的正则匹配结果
     *
     * @param textString  被检测的文本
     * @param regularType 正则匹配类型
     * @return list类型
     */
    public static List<String> getMatchListByRegex(String textString,
                                                   @REGULAR_TYPE String regularType) {
        List<String> regexList = null;
        Pattern regex = Pattern.compile(regularType);
        Matcher matcher = regex.matcher(textString);
        if (matcher.find()) {
            regexList = new ArrayList<>();
            for (int i = 0; i < matcher.groupCount(); i++) {
                regexList.add(i, matcher.group(i));
            }
        }
        return regexList;
    }

    /**
     * 正则匹配判断
     *
     * @param textString  被监测的文本
     * @param regularType 正则匹配类型
     * @return
     */
    public static boolean isMatchText(String textString, @REGULAR_TYPE String regularType) {
        boolean isMatch = false;
        Pattern regex = Pattern.compile("^" + regularType + "$");
        Matcher matcher = regex.matcher(textString);
        isMatch = matcher.find();
        return isMatch;
    }

    @StringDef({REGULAR_URL, REGULAR_PHONE_NUMBER, REGULAR_EMAIL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface REGULAR_TYPE {
    }

}
