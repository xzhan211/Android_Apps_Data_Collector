package com.example.datacollector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CPUTime {

    protected static List<String> getCPUTimes() {
        List<String> result = new ArrayList<>();
        String line;
        try {
            FileReader fr = new FileReader("/proc/stat");
            BufferedReader in = new BufferedReader(fr, 1000);
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("cpu")) {
                    result.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
