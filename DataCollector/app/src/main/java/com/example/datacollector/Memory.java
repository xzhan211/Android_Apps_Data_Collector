package com.example.datacollector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private static int printThreadSize() {
        Map<Thread, StackTraceElement[]> stacks = Thread.getAllStackTraces();
        return stacks.size();
    }
}


