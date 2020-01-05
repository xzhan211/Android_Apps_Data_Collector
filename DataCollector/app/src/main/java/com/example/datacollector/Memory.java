package com.example.datacollector;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

class Memory {
    public static List<String> getData() {
        List<String> result = new ArrayList<>();
        result.add("Thread quantity:    " + printThreadSize());
        String line;
        try {
            FileReader fr = new FileReader("/proc/meminfo");
            BufferedReader in = new BufferedReader(fr, 2048);
            while ((line = in.readLine()) != null) {
                line = line.trim();
                result.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static int threadQuantity() {
        int cnt = 0;
        try {
            File dir = new File("/proc/");

            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (Pattern.matches("99", file.getName())) {
                        Log.i("@@@@@@@@@@@@@@@@@@@", file.getName());
                        return true;
                    }
                    return false;
                }
            });

            cnt = files.length;

//            File[] files = dir.listFiles();
//            for(File inFile: files){
//                if(inFile.isDirectory())
//                    cnt++;
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnt;
    }

    private static int currentPID() {
        return android.os.Process.myPid();
    }


    private static int printThreadSize() {
        Map<Thread, StackTraceElement[]> stacks = Thread.getAllStackTraces();
//        Set<Thread> set = stacks.keySet();
//        String TAG = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";
//        for (Thread key : set) {
//            StackTraceElement[] stackTraceElements = stacks.get(key);
//            Log.i(TAG, "---- print thread: " + key.getName() + " start ----");
//            for (StackTraceElement st : stackTraceElements) {
//                Log.i(TAG, "StackTraceElement: " + st.toString());
//            }
//            Log.i(TAG, "---- print thread: " + key.getName() + " end ----");
//        }

        return stacks.size();
    }
}


