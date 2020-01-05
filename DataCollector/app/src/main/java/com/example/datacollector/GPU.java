package com.example.datacollector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class GPU {
    public static List<String> getGPU() {
        List<String> result = new ArrayList<>();
        String line;
        try {
            FileReader fr = new FileReader("/sys/class/kgsl/kgsl-3d0/gpu_busy_percentage");
            BufferedReader in = new BufferedReader(fr, 128);
            if ((line = in.readLine()) != null) {
                line = line.trim();
                result.add("usage: " + line);
            }

            fr = new FileReader("/sys/class/kgsl/kgsl-3d0/temp");
            in = new BufferedReader(fr, 128);
            if ((line = in.readLine()) != null) {
                line = line.trim();
                result.add("temp: " + Float.parseFloat(line) / 1000 + (char) 0x00B0 + "C");
            }

            fr = new FileReader("/sys/class/kgsl/kgsl-3d0/devfreq/cur_freq");
            in = new BufferedReader(fr, 128);
            if ((line = in.readLine()) != null) {
                line = line.trim();
                result.add("freq: " + Long.parseLong(line) + "Hz");
            }

            fr = new FileReader("/sys/class/kgsl/kgsl-3d0/devfreq/available_frequencies");
            in = new BufferedReader(fr, 128);
            if ((line = in.readLine()) != null) {
                line = line.trim();
                result.add("freq option: " + line);
            }

            fr = new FileReader("/sys/class/kgsl/kgsl-3d0/devfreq/governor");
            in = new BufferedReader(fr, 128);
            if ((line = in.readLine()) != null) {
                line = line.trim();
                result.add("Current Mode: " + line);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
