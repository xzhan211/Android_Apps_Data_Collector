package com.example.datacollector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Memory {
    public static List<String> getData() {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
//        result.add("Thread quantity:    " + printThreadSize());
        sb.append("Thread quantity:    " + printThreadSize() + ",");
        String line;
        try {
            FileReader fr = new FileReader("/proc/meminfo");
            BufferedReader in = new BufferedReader(fr, 2048);
            while ((line = in.readLine()) != null) {
                line = line.trim();
//                result.add(line);
                sb.append(line + ",");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sb.deleteCharAt(sb.length() - 1);
            sb.append("\n");
            result.add(sb.toString());
        }
        return result;
    }

    private static int printThreadSize() {
        Map<Thread, StackTraceElement[]> stacks = Thread.getAllStackTraces();
        return stacks.size();
    }
}


