package com.example.datacollector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static boolean isStart = false;
    private static boolean isStop = false;

    private static boolean firstFlag = false;
    private static ImageView green;
    private static ImageView red;

    private volatile boolean exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String testPath = "";
        String apkRoot = "";

        if (!firstFlag) {
            testPath = getPackageCodePath();
            apkRoot = "chmod 777 " + testPath;
            SystemManager.RootCommand(apkRoot);
            firstFlag = true;
        }

        green = findViewById(R.id.greenImage);
        red = findViewById(R.id.redImage);

        if (isStart == true && isStop == false) {
            green.setVisibility(View.VISIBLE);
            red.setVisibility(View.INVISIBLE);
        } else if (isStart == false && isStop == true) {
            green.setVisibility(View.INVISIBLE);
            red.setVisibility(View.VISIBLE);
        } else if (isStart == false && isStop == false) {
            Button buttonStop = findViewById(R.id.stopUpdate);
            buttonStop.setEnabled(false);
        }
    }

    public void cpuButton(View view) {
        Intent intent = new Intent(this, CPUFreqencyActivity.class);
        startActivity(intent);
    }

    public void cpuTimeButton(View view) {
        Intent intent = new Intent(this, CPUTimeActivity.class);
        startActivity(intent);
    }

    public void batteryButton(View view) {
        Intent intent = new Intent(this, BatteryMessageActivity.class);
        startActivity(intent);
    }

    public void gpuButton(View view) {
        Intent intent = new Intent(this, GPUMessageActivity.class);
        startActivity(intent);
    }

    public void netButton(View view) {
        Intent intent = new Intent(this, NetMessageActivity.class);
        startActivity(intent);
    }

    public void tempButton(View view) {
        Intent intent = new Intent(this, TempMessageActivity.class);
        startActivity(intent);
    }

    public void memoryButton(View view) {
        Intent intent = new Intent(this, MemoryMessageActivity.class);
        startActivity(intent);
    }


    public void startButton(View view) {
        isStart = true;
        isStop = false;
        Log.i("INFO", "click start button");
        green.setVisibility(View.VISIBLE);
        red.setVisibility(View.INVISIBLE);

        Button buttonStart = findViewById(R.id.startUpdate);
        Button buttonStop = findViewById(R.id.stopUpdate);
        buttonStart.setEnabled(false);
        buttonStop.setEnabled(true);

        setButton(false);
        exit = false;
        write();
    }


    public void stopButton(View view) {
        isStart = false;
        isStop = true;
        Log.i("INFO", "click stop button");
        green.setVisibility(View.INVISIBLE);
        red.setVisibility(View.VISIBLE);

        Button buttonStart = findViewById(R.id.startUpdate);
        Button buttonStop = findViewById(R.id.stopUpdate);
        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);

        setButton(true);
        exit = true;
    }

    private void setButton(boolean value) {
        Button buttonCpuFreq = findViewById(R.id.cpuFreq);
        Button buttonCpuTime = findViewById(R.id.cpuTime);
        Button buttonBattery = findViewById(R.id.battery);
        Button buttonGpu = findViewById(R.id.gpu);
        Button buttonNet = findViewById(R.id.net);
        Button buttonTemperature = findViewById(R.id.temperature);
        Button buttonMemory = findViewById(R.id.memory);

        buttonCpuFreq.setEnabled(value);
        buttonCpuTime.setEnabled(value);
        buttonBattery.setEnabled(value);
        buttonGpu.setEnabled(value);
        buttonNet.setEnabled(value);
        buttonTemperature.setEnabled(value);
        buttonMemory.setEnabled(value);
    }

    public void write() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (exit)
                            break;
                        Thread.sleep(1000);

                        //***********************************************
                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilTime).write(Calendar.getInstance().getTimeInMillis() + "\n", "time.csv");

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
                        List<String> result = Memory.getData();
                        for(String str : result) {
                            FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilMemory).write(str + ",", "memory.csv");
                        }
                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilMemory).write("\n", "memory.csv");

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
                    for (int i = 0; i < CPUFreq.size; i++) {
                        title += "cpu" + i + ",";
                    }
                    title += "\n";
                    FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilCpuFreq).write(title, "cpuFreq.csv");

                    //cpu time
                    title = "";
                    for (int i = 0; i < CPUTime.size; i++) {
                        if (i == 0)
                            title += "cpu,";
                        else
                            title += "cpu" + (i - 1) + ",";
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
                            sb.append(type+",");
                        }
                        sb.append("\n");
                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally{
                        FileCacheUtil.getInstance(getApplicationContext(), FileCacheUtil.fileCacheUtilTemp).write(sb.toString(), "temperature.csv");
                    }

                    //memory, looks good. Use python analyze it.

                }
            }
        }.start();
    }
}
