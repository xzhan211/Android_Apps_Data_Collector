package com.example.datacollector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.List;

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

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (exit)
                            break;
                        StringBuilder sb = new StringBuilder();
                        Thread.sleep(1000);
                        Log.i("@@@@@@@@@@@@@@@@@@", "loop");


                        //***********************************************
                        sb.append("****\n");
                        sb.append("time: " + Calendar.getInstance().getTime() + "\n");

                        //-----cpu freq---------
                        sb.append("cpu freq:\n");
                        List<String> cpuFreq = CPUFreq.getFreq();
                        for (String str : cpuFreq) {
                            sb.append(str + "\n");
                        }

                        //-----cpu time---------
                        sb.append("cpu time:\n");
                        List<String> cpuTime = CPUTime.getCPUTimes();
                        for (String str : cpuTime) {
                            sb.append(str + "\n");
                        }

                        //-----battery ---------
                        sb.append("battery:\n");
                        List<String> batteryInfo = GetBattery.getParameter();
                        for (String str : batteryInfo) {
                            sb.append(str + "\n");
                        }

                        //-----GPU -------------
                        sb.append("GPU:\n");
                        List<String> gpuInfo = GPU.getGPU();
                        for (String str : gpuInfo) {
                            sb.append(str + "\n");
                        }

                        //-----net -------------
                        sb.append("network:\n");
                        long[] speed = NetSpeed.getNetSpeed();
                        sb.append("Tx:    " + ((float) speed[1]) / 1000 + "\n");
                        sb.append("Rx:    " + ((float) speed[0]) / 1000 + "\n");

                        //-----All temperature -------------
                        sb.append("All temperature:\n");
                        List<String> allTemp = Temperature.getTemp();
                        for (String str : allTemp) {
                            sb.append(str + "\n");
                        }

                        //-----Memory -------------
                        sb.append("Memory:\n");
                        List<String> memoryInfo = Memory.getData();
                        for (String str : memoryInfo) {
                            sb.append(str + "\n");
                        }

                        FileCacheUtil.getInstance(getApplicationContext()).write(sb.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
