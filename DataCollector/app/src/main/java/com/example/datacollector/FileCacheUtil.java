package com.example.datacollector;

/*
*   this is a tool package for accessing internal storage
*   how to use:
*   write:
*       FileCacheUtil.getInstance(getApplicationContext(),FileCacheUtil.fileCacheUtilTime).write("millisecond\n", "time.csv");
*   read:
*
*
*   location: /data/data/com.example.datacollector/files/fileName.csv
* */


import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import static android.content.Context.MODE_APPEND;


public class FileCacheUtil {

    private Context context;

    private FileCacheUtil(Context context) {
        this.context = context;
    }

    public static FileCacheUtil fileCacheUtilTime;
    public static FileCacheUtil fileCacheUtilCpuFreq;
    public static FileCacheUtil fileCacheUtilCpuTime;
    public static FileCacheUtil fileCacheUtilBattery;
    public static FileCacheUtil fileCacheUtilGpu;
    public static FileCacheUtil fileCacheUtilNet;
    public static FileCacheUtil fileCacheUtilTemp;
    public static FileCacheUtil fileCacheUtilMemory;


    public static FileCacheUtil getInstance(Context context, FileCacheUtil fileCacheUtil) {
        if (fileCacheUtil == null) {
            synchronized (FileCacheUtil.class) {
                if (fileCacheUtil == null) {
                    fileCacheUtil = new FileCacheUtil(context);
                }
            }
        }
        return fileCacheUtil;
    }


    public String read(String path) {
        try {
            FileInputStream inStream = context.openFileInput(path);
            byte[] buffer = new byte[1024];
            int hasRead = 0;
            StringBuilder sb = new StringBuilder();
            while ((hasRead = inStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, hasRead));
            }

            inStream.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(String msg, String path) {
        if (msg == null) {
            return;
        }
        try {
            FileOutputStream fos = context.openFileOutput(path, MODE_APPEND);
            fos.write(msg.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}