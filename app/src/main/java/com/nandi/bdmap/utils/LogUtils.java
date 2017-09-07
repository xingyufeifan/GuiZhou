package com.nandi.bdmap.utils;

import android.util.Log;

/**
 * Created by qingsong on 2017/4/13.
 * 日志工具类
 */

public class LogUtils {
    private final static int LEVEL = 5;

    private final static String DEFAULT_TAG =LogUtils.class.getName();

    private LogUtils() {
        throw new UnsupportedOperationException("LogUtils()不能被实例化!");
    }

    public static void v(String tag,String msg) {
        if(LEVEL >= 5) Log.v(tag == null ? DEFAULT_TAG:tag,msg == null?"":msg);
    }

    public static void d(String tag,String msg) {
        if(LEVEL >= 4)Log.d(tag == null ? DEFAULT_TAG:tag,msg == null?"":msg);
    }

    public static void i(String tag,String msg) {
        if(LEVEL >= 3)Log.i(tag == null ? DEFAULT_TAG:tag,msg == null?"":msg);
    }

    public static void w(String tag,String msg) {
        if(LEVEL >= 2)Log.w(tag == null ? DEFAULT_TAG:tag,msg == null?"":msg);
    }

    public static void e(String tag,String msg) {
        if(LEVEL >= 1)Log.e(tag == null ? DEFAULT_TAG:tag,msg == null?"":msg);
    }
}
