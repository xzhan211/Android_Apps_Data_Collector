package com.example.datacollector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Temperature {

    public static int size;
    protected static List<String> getTemp() {
        size = 0;
        List<String> result = new ArrayList<>();
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        try {
            File dir = new File("/sys/class/thermal");

            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (Pattern.matches("thermal_zone[0-9]+", file.getName())) {
                        return true;
                    }
                    return false;
                }
            });

            size = files.length;
            String type = null;
            String temp = null;
            String path = "/sys/class/thermal/thermal_zone";
            for (int i = 0; i < size; i++) {
                br = new BufferedReader(new FileReader(path + i + "/type"));
                type = br.readLine();
                br = new BufferedReader(new FileReader(path + i + "/temp"));
                temp = br.readLine();
                //result.add(type + "   " + temp);
                sb.append(temp + ",");
            }
            br.close();
        } catch (FileNotFoundException e) {
            result.add(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sb.deleteCharAt(sb.length() - 1);
            sb.append("\n");
            result.add(sb.toString());
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
