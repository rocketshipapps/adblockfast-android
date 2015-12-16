package com.adblockfast.utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;

public class Rule {
    public static String TAG = "rule_status";
    public static String PREFERENCE = "adblockfast";

    public static File get(Context context) {
        boolean active = context.getSharedPreferences(PREFERENCE, 0).getBoolean(TAG, false);
        Uri uri = Uri.parse("android.resouce://com.adblockfast/raw/" + (active ? "blocked" : "unblocked"));
        return new File(uri.getPath());
    }

    public static boolean active(Context context) {
        return context.getSharedPreferences(PREFERENCE, 0).getBoolean(TAG, false);
    }

    public static boolean exists(Context context) {
        return context.getSharedPreferences(PREFERENCE, 0).contains(TAG);
    }

    public static void disable(Context context) {
        context.getSharedPreferences(PREFERENCE, 0).edit().putBoolean(TAG, false).apply();
    }

    public static void enable(Context context) {
        context.getSharedPreferences(PREFERENCE, 0).edit().putBoolean(TAG, true).apply();
    }
}
