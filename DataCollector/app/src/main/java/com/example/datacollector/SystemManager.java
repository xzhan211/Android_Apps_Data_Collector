package com.example.datacollector;

import android.util.Log;

import java.io.DataOutputStream;

public class SystemManager{
    public static boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");


            //selinux
            os.writeBytes("setenforce 0\n");

            // chmod device node
            os.writeBytes("chmod 777 /sys/class/power_supply/battery/temp" + "\n");
            os.writeBytes("chmod 777 /sys/class/power_supply/battery/voltage_now" + "\n");
            os.writeBytes("chmod 777 /sys/class/power_supply/battery/current_avg" + "\n");
            os.writeBytes("chmod 777 /sys/class/power_supply/battery/capacity" + "\n");

            os.writeBytes("chmod 444 /sys/devices/system/cpu/cpufreq/policy0/cpuinfo_cur_freq" + "\n");
            os.writeBytes("chmod 444 /sys/devices/system/cpu/cpufreq/policy4/cpuinfo_cur_freq" + "\n");
            os.writeBytes("chmod 444 /sys/devices/system/cpu/cpufreq/policy7/cpuinfo_cur_freq" + "\n");


            // only for Nexus 5
//            os.writeBytes("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq" + "\n");
//            os.writeBytes("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/cpuinfo_cur_freq" + "\n");
//            os.writeBytes("chmod 777 /sys/devices/system/cpu/cpu2/cpufreq/cpuinfo_cur_freq" + "\n");
//            os.writeBytes("chmod 777 /sys/devices/system/cpu/cpu3/cpufreq/cpuinfo_cur_freq" + "\n");

            // only for S10
            os.writeBytes("chmod 777 /sys/devices/system/cpu/cpufreq/policy6/cpuinfo_cur_freq" + "\n");
            os.writeBytes("chmod 777 /proc/stat" + "\n");

            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {

            }
            Log.d("*** DEBUG ***", "Root SUC ");
            return true;
        }
    }
}
