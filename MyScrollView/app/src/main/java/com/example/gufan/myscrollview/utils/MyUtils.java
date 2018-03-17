package com.example.gufan.myscrollview.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * Created by gufan on 2018/3/17.
 */

public class MyUtils {

    public static String getProcess(Context context,int pid){
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();
        if (runningAppProcessInfos == null){
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo:runningAppProcessInfos) {
            if (processInfo.pid == pid){
                return processInfo.processName;
            }
        }
        return null;
    }

    public void close(Closeable closeable){
        try{
            if (closeable != null){
                closeable.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static DisplayMetrics getScreenMetrics(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

}
