package com.xiaoluogo.goodtochat.utils;

import android.util.Log;

/**
 * 自己的日志工具类
 */

public class L {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static int level = VERBOSE;

    public static void v(String tag, String msg) {
        if (level <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (level <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (level <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (level <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (level <= ERROR) {
            Log.e(tag, msg);
        }
    }
    public static void v(String msg) {
        if (level <= VERBOSE) {
            Log.v("==xiaoluoGo==", msg);
        }
    }

    public static void d( String msg) {
        if (level <= DEBUG) {
            Log.d("==xiaoluoGo==", msg);
        }
    }

    public static void i( String msg) {
        if (level <= INFO) {
            Log.i("==xiaoluoGo==", msg);
        }
    }

    public static void w( String msg) {
        if (level <= WARN) {
            Log.w("==xiaoluoGo==", msg);
        }
    }

    public static void e(String msg) {
        if (level <= ERROR) {
            Log.e("==xiaoluoGo==", msg);
        }
    }
}
