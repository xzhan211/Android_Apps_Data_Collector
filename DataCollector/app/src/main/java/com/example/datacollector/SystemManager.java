package com.example.datacollector;

import android.app.Activity;
import android.util.Log;

import java.io.DataOutputStream;

public class SystemManager extends Activity {
    public static boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");

            // chmod device node
            /*
                battery : chmod 777 addr
                addr:
                /sys/class/power_supply/battery/temp
                /sys/class/power_supply/battery/voltage_now
                /sys/class/power_supply/battery/current_avg
                /sys/class/power_supply/battery/capacity
            */
            os.writeBytes("chmod 777 /sys/class/power_supply/battery/temp" + "\n");
            os.writeBytes("chmod 777 /sys/class/power_supply/battery/voltage_now" + "\n");
            os.writeBytes("chmod 777 /sys/class/power_supply/battery/current_avg" + "\n");
            os.writeBytes("chmod 777 /sys/class/power_supply/battery/capacity" + "\n");


            /*
                cpu freq : chmod 444 addr
                addr:
                /sys/devices/system/cpu/cpufreq/policy0/cpuinfo_cur_freq
                /sys/devices/system/cpu/cpufreq/policy4/cpuinfo_cur_freq
                /sys/devices/system/cpu/cpufreq/policy7/cpuinfo_cur_freq
            */
            os.writeBytes("chmod 444 /sys/devices/system/cpu/cpufreq/policy0/cpuinfo_cur_freq" + "\n");
            os.writeBytes("chmod 444 /sys/devices/system/cpu/cpufreq/policy4/cpuinfo_cur_freq" + "\n");
            os.writeBytes("chmod 444 /sys/devices/system/cpu/cpufreq/policy7/cpuinfo_cur_freq" + "\n");


            //selinux
            os.writeBytes("setenforce 0\n");

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
