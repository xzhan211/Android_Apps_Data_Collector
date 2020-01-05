package com.example.datacollector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class GetBattery {
    public static List<String> getParameter() {
        List<String> result = new ArrayList<>();

        FileReader fr = null;
        BufferedReader br = null;
        String path = "/sys/class/power_supply/battery/";
        try {
            String text = "";

            fr = new FileReader(path + "voltage_now");
            br = new BufferedReader(fr);
            text = br.readLine();
            result.add("Voltage:    " + Integer.parseInt(text.trim()) / 1000 + " " + "mV");

            fr = new FileReader(path + "temp");
            br = new BufferedReader(fr);
            text = br.readLine();
            result.add("Temperature:    " + Integer.parseInt(text.trim()) / 10 + " " + (char) 0x00B0 + "C");

            fr = new FileReader(path + "capacity");
            br = new BufferedReader(fr);
            text = br.readLine();
            result.add("Percentage:    " + Integer.parseInt(text.trim()) + " " + "%");

            fr = new FileReader(path + "current_now");
            br = new BufferedReader(fr);
            text = br.readLine();
            result.add("Current:    " + Integer.parseInt(text.trim()) / 1000 + " " + "mA");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null)
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return result;
    }
}