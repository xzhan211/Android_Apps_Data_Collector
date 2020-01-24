package com.example.datacollector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class CPUFreq {

    public static int size = 0;

    public static List<String> getFreq() {
        List<String> result = new ArrayList<>();
        BufferedReader br = null;
        //String temp = "";
        StringBuilder temp = new StringBuilder();
        try {
            File dir = new File("/sys/devices/system/cpu/");

            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (Pattern.matches("cpu[0-9]+", file.getName())) {
                        return true;
                    }
                    return false;
                }
            });

            size = files.length;

            String line = null;
            for (int i = 0; i < size; i++) {
                br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_cur_freq"));
                line = br.readLine();
                if (line != null) {
                    long frequency = Long.parseLong(line);
                    if (frequency < 0) {
                        temp.append("Unknow,");
                    } else {
                        temp.append(frequency + ",");
                    }

                }
                //result.add("cpu" + i + " : " + temp);
            }
            br.close();
        } catch (FileNotFoundException e) {
            result.add(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            temp.deleteCharAt(temp.length()-1);
            temp.append("\n");
            result.add(temp.toString());
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
