package com.rocketshipapps.adblockfast.utils;

import android.content.Context;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import com.rocketshipapps.adblockfast.R;

public class Rule {
    public static String TAG = "rule_status";
    public static String PREFERENCE = "adblockfast";
    private static final String OUTPUT = "rules.txt";

    public static File get(Context context) {
        boolean active = context.getSharedPreferences(PREFERENCE, 0).getBoolean(TAG, false);
        
        int res = (active) ? R.raw.blocked: R.raw.unblocked;
        File file = new File(context.getFilesDir(), OUTPUT);
        // Remove any old file lying around
        if (file.exists()) 
            file.delete();
            
        try {
            file.createNewFile();
            InputStream in = context.getResources().openRawResource(res);
            FileOutputStream out = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int read;
            while((read = in.read(buffer)) != -1){
                out.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
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
