package com.frame.core.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * 字符串校验类
 * Created by Administrator on 2015/12/22.
 */
public class StringUtil {

    private static final ThreadLocal<SimpleDateFormat> dateFormate;

    static {
        dateFormate = new ThreadLocal<SimpleDateFormat>() {

            @SuppressLint("SimpleDateFormat")
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
        };
    }

    /**
     * 判断字符串是否有内容
     *
     * @param paramCharSequence     验证的文本
     * @return                      返回
     */
    public static boolean isEmpty(CharSequence paramCharSequence) {
        if (paramCharSequence == null || "".equals(paramCharSequence)) {
            return true;
        }
        for (int i = 0; i < paramCharSequence.length(); i++) {
            int j = paramCharSequence.charAt(i);
            if (j != 32 && j != 9 && j != 13 && j != 10) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将一个java.util.Date对象转换成特定格式的字符串
     *
     * @param date   日期对象
     * @param format 指定格式
     */
    public static String formatDate(Date date, String format) {
        String result = "";
        if (date == null) return result;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            result = formatter.format(date.getTime());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }

    /**
     * 检测email格式
     *
     * @param paramString       验证的文本
     * @return                  返回
     */
    public static boolean isEmail(String paramString) {
        if (isEmpty(paramString))
            return false;

        String checkEmailRule =
                "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)" +
                        "|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern emailer = Pattern.compile(checkEmailRule);// 复杂匹配
        return emailer.matcher(paramString).matches();
    }

    /**
     * 检测手机号码的格式
     *
     * @param paramString   验证的文本
     * @return              返回
     */
    public static boolean isMobileNO(String paramString) {
        /**
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188、147
         * 联通：130、131、132、152、155、156、185、186、145
         * 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或4或5或8，其他位置的可以为0-9
         */
        // "[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (isEmpty(paramString))
            return false;

        // String checkMobileRule = "[1][35478]\\d{9}";
        Pattern pattern = Pattern.compile("^[1]((3[0-9])|(5[^4,\\D])|(8[0,5-9])|(4[5,7])|(7[0,6-8]))\\d{8}$");
        return pattern.matcher(paramString).matches();
    }

    public static String hideMidelMobileNo(String paramString) {
        return hideMidelMobileNo(paramString, '*');
    }

    /**
     * 隐藏手机号中间号码
     *
     * @param paramString   手机号
     * @param h             替代的符号
     * @return              加密后的手机号
     */
    public static String hideMidelMobileNo(String paramString, char h) {
        if (isMobileNO(paramString)) {
            char[] c = paramString.toCharArray();
            for (int i = 3; i < 7; i++) {
                c[i] = h;
            }
            return String.valueOf(c);
        }
        return paramString;
    }

    /**
     * 对&nbsp;|&quot;|&amp;|&lt;|&gt;等html字符转义
     *
     * @param htmlData
     */
    public static String formatHtmlData(String htmlData) {
        htmlData = htmlData.replaceAll("&nbsp;", " ");
        htmlData = htmlData.replaceAll("&amp;", "&");
        htmlData = htmlData.replaceAll("&quot;", "\"");
        htmlData = htmlData.replaceAll("&lt;", "<");
        htmlData = htmlData.replaceAll("&gt;", ">");
        return htmlData;
    }

    public static String friendlyTime(String paramString) {
        if (isInEasternEightZones()) {
        }
        return "";
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentDate() {
        Calendar localCalendar = Calendar.getInstance();
        return dateFormate.get().format(localCalendar.getTime());
    }

    public static Date toDate(String paramString) {
        return toDate(paramString, dateFormate.get());
    }

    public static Date toDate(String paramString, SimpleDateFormat paramSimpleDateFormat) {
        try {
            return paramSimpleDateFormat.parse(paramString);
        } catch (ParseException localParseException) {
            return null;
        }
    }

    /**
     * 判断时区是否在东八区
     *
     * @return
     */
    public static boolean isInEasternEightZones() {
        return TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08");
    }
}
