package com.example.datacollector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CPUTime {
    public static int size;

    protected static List<String> getCPUTimes() {
        List<String> result = new ArrayList<>();
        String line;
        size = 0;
        StringBuilder temp = new StringBuilder();
        try {
            FileReader fr = new FileReader("/proc/stat");
            BufferedReader in = new BufferedReader(fr, 1000);
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("cpu")) {
                    //result.add(line);
                    temp.append(line.substring(4) + ",");
                    size++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            temp.append("\n");
            result.add(temp.toString());
        }
        return result;
    }
}
