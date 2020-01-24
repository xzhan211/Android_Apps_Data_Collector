package com.example.datacollector;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class DataStorageActivity extends AppCompatActivity {

    public volatile boolean exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datastorage);
        exit = false;

        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Data Collector is Foreground Service :)");
        ContextCompat.startForegroundService(this, serviceIntent);

        writeDataToInternalStorage();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exit = true;
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }

    public void writeDataToInternalStorage() {


        new Thread() {
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss", Locale.getDefault());

            @Override
            public void run() {
                try {
                    while (true) {
                        if (exit)
                            break;
                        Thread.sleep(1000);

                        //***********************************************
//                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilTime).write(Calendar.getInstance().getTimeInMillis() + "\n", "time.csv");
//                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilTime).write(Calendar.getInstance().getTime() + "\n", "time.csv");
                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilTime).write(sdf.format(new Date()) + "\n", "time.csv");

                        //-----cpu freq---------
                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilCpuFreq).write(CPUFreq.getFreq().get(0), "cpuFreq.csv");

                        //-----cpu time---------
                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilCpuTime).write(CPUTime.getCPUTimes().get(0), "cpuTime.csv");

                        //-----battery ---------
                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilBattery).write(GetBattery.getParameter().get(0), "battery.csv");

                        //-----GPU -------------
                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilGpu).write(GPU.getGPU().get(0), "gpu.csv");

                        //-----net -------------
                        long[] speed = NetSpeed.getNetSpeed();
                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilNet).write(((float) speed[1]) / 1000 + ",", "net.csv");
                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilNet).write(((float) speed[0]) / 1000 + "\n", "net.csv");

                        //-----All temperature -------------
                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilTemp).write(Temperature.getTemp().get(0), "temperature.csv");

                        //-----Memory -------------
                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilMemory).write(Memory.getData().get(0), "memory.csv");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    //add title at the last line of every csv file
                    String title;

                    //time
                    FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilTime).write("millisecond\n", "time.csv");

                    //cpu frequency
                    title = "";
                    int last = CPUFreq.size - 1;
                    for (int i = 0; i < CPUFreq.size; i++) {
                        if (i == last)
                            title += "cpu" + i + "\n";
                        else
                            title += "cpu" + i + ",";

                    }
                    title += "\n";
                    FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilCpuFreq).write(title, "cpuFreq.csv");

                    //cpu time
                    title = "";
                    last = CPUTime.size - 1;
                    for (int i = 0; i < CPUTime.size; i++) {
                        if (i == 0)
                            title += "cpu,";
                        else {
                            if (i == last)
                                title += "cpu" + (i - 1) + "\n";
                            else
                                title += "cpu" + (i - 1) + ",";
                        }
                    }
                    title += "\n";
                    FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilCpuTime).write(title, "cpuTime.csv");

                    //battery
                    title = "voltage_now (mV),temperature (C),capacity (%),current_now (mA)\n";
                    FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilBattery).write(title, "battery.csv");


                    //gpu
                    title = "usage (%),temperature (C),frequency (Hz)\n";
                    FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilGpu).write(title, "gpu.csv");

                    //net
                    title = "TX (KB/s),RX (KB/s)\n";
                    FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilNet).write(title, "net.csv");

                    //all temperature
                    title = "";
                    StringBuilder sb = new StringBuilder();
                    String path = "/sys/class/thermal/thermal_zone";
                    BufferedReader br = null;
                    String type;
                    try {
                        for (int i = 0; i < Temperature.size; i++) {
                            br = new BufferedReader(new FileReader(path + i + "/type"));
                            type = br.readLine();
                            sb.append(type + ",");
                        }
                        sb.append("\n");
                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilTemp).write(sb.toString(), "temperature.csv");
                    }

                    //memory, looks good. Use python analyze it.

                }
            }
        }.start();
    }
}
